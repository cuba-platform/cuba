/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.CollectionChangeType;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.Sorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 *
 */
public class CollectionContainerImpl<E extends Entity>
        extends InstanceContainerImpl<E> implements CollectionContainer<E> {

    private static final Logger log = LoggerFactory.getLogger(CollectionContainerImpl.class);

    protected List<E> collection = new ArrayList<>();

    protected Map<Object, Integer> idMap = new HashMap<>();
    protected Sorter sorter;

    public CollectionContainerImpl(MetaClass metaClass) {
        super(metaClass);
    }

    @Override
    public void setItem(@Nullable E item) {
        E prevItem = this.item;

        if (item != null) {
            int idx = getItemIndex(item.getId());
            if (idx == -1) {
                throw new IllegalArgumentException("CollectionContainer does not contain " + item);
            }
            this.item = collection.get(idx);
        } else {
            this.item = null;
        }

        fireItemChanged(prevItem);
    }

    @Override
    public List<E> getItems() {
        return Collections.unmodifiableList(collection);
    }

    @Override
    public List<E> getMutableItems() {
        return new ObservableList<>(collection, idMap,
                (changeType, changes) -> {
                    buildIdMap();
                    clearItemIfNotExists();
                    fireCollectionChanged(changeType, changes);
                },
                this::detachListener,
                this::attachListener
        );
    }

    @Override
    public void setItems(@Nullable Collection<E> entities) {
        detachListener(collection);
        collection.clear();
        if (entities != null) {
            collection.addAll(entities);
            attachListener(collection);
        }
        buildIdMap();
        clearItemIfNotExists();
        fireCollectionChanged(CollectionChangeType.REFRESH, Collections.emptyList());
    }

    @Nonnull
    @Override
    public E getItem(Object entityId) {
        E item = getItemOrNull(entityId);
        if (item == null)
            throw new IllegalArgumentException("Item with id='" + entityId + "' not found");
        return item;
    }

    @Nullable
    @Override
    public E getItemOrNull(Object entityId) {
        int idx = getItemIndex(entityId);
        return idx != -1 ? collection.get(idx) : null;
    }

    @Override
    public int getItemIndex(Object entityId) {
        if (entityId instanceof Entity && !(entityId instanceof EmbeddableEntity)) {
            // if an entity instance is passed instead of id, check if the entity is of valid class and extract id
            Entity entity = (Entity) entityId;
            if (!entityMetaClass.getJavaClass().isAssignableFrom(entity.getClass())) {
                throw new IllegalArgumentException("Invalid entity class: " + entity.getClass());
            } else {
                entityId = entity.getId();
            }
        }
        Integer idx = idMap.get(entityId);
        return idx != null ? idx : -1;
    }

    @Override
    public boolean containsItem(Object entityId) {
        return getItemIndex(entityId) > -1;
    }

    @Override
    public void replaceItem(E entity) {
        checkNotNullArgument(entity, "entity is null");

        Object id = entity.getId();
        int idx = getItemIndex(id);
        CollectionChangeType changeType;
        if (idx > -1) {
            E prev = collection.get(idx);
            detachListener(prev);
            if (prev == getItemOrNull()) {
                this.item = entity;
                fireItemChanged(prev);
            }
            replaceInCollection(idx, entity);
            changeType = CollectionChangeType.SET_ITEM;
        } else {
            addToCollection(entity);
            changeType = CollectionChangeType.ADD_ITEMS;
        }
        attachListener(entity);
        buildIdMap();
        fireCollectionChanged(changeType, Collections.singletonList(entity));
    }

    protected void replaceInCollection(int idx, E entity) {
        collection.set(idx, entity);
    }

    protected void addToCollection(E entity) {
        collection.add(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addCollectionChangeListener(Consumer<CollectionChangeEvent<E>> listener) {
        return events.subscribe(CollectionChangeEvent.class, (Consumer) listener);
    }

    @Override
    public Sorter getSorter() {
        return sorter;
    }

    @Override
    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    @Override
    public void unmute(UnmuteEventsMode mode) {
        this.listenersEnabled = true;

        if (mode ==  UnmuteEventsMode.FIRE_REFRESH_EVENT) {
            fireCollectionChanged(CollectionChangeType.REFRESH, Collections.emptyList());
        }
    }

    protected void fireCollectionChanged(CollectionChangeType type, Collection<? extends E> changes) {
        if (!listenersEnabled) {
            return;
        }

        CollectionChangeEvent<E> collectionChangeEvent = new CollectionChangeEvent<>(this, type, changes);
        log.trace("collectionChanged: {}", collectionChangeEvent);
        events.publish(CollectionChangeEvent.class, collectionChangeEvent);
    }

    protected void attachListener(Collection<E> entities) {
        for (E entity : entities) {
            attachListener(entity);
        }
    }

    protected void detachListener(Collection<E> entities) {
        for (E entity : entities) {
            detachListener(entity);
        }
    }

    protected void buildIdMap() {
        idMap.clear();
        for (int i = 0; i < collection.size(); i++) {
            idMap.put(collection.get(i).getId(), i);
        }
    }

    protected void clearItemIfNotExists() {
        if (item != null) {
            int idx = getItemIndex(item.getId());
            if (idx == -1) {
                // item doesn't exist in the collection
                E prevItem = item;
                detachListener(prevItem);
                item = null;
                fireItemChanged(prevItem);
            } else {
                E newItem = collection.get(idx);
                if (newItem != item) {
                    E prevItem = item;
                    detachListener(prevItem);
                    item = newItem;
                    fireItemChanged(prevItem);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "CollectionContainerImpl{" +
                "entity=" + entityMetaClass +
                ", view=" + view +
                ", size=" + collection.size() +
                '}';
    }
}