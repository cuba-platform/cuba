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
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.CollectionChangeType;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.Sorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

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
        if (item != null) {
            int idx = getItemIndex(item.getId());
            if (idx == -1) {
                throw new IllegalArgumentException("CollectionContainer does not contain " + item);
            }
            E existingItem = collection.get(idx);
            super.setItem(existingItem);
        } else {
            super.setItem(null);
        }
    }

    @Override
    public List<E> getItems() {
        return Collections.unmodifiableList(collection);
    }

    @Override
    public List<E> getMutableItems() {
        return new ObservableList<>(collection, (changeType, changes) -> {
            buildIdMap();
            clearItemIfNotExists();
            fireCollectionChanged(changeType, changes);
        });
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

    @Nullable
    @Override
    public E getItemOrNull(Object entityId) {
        int idx = getItemIndex(entityId);
        return idx != -1 ? collection.get(idx) : null;
    }

    @Override
    public int getItemIndex(Object entityId) {
        Integer idx = idMap.get(entityId);
        return idx != null ? idx : -1;
    }

    @Override
    public void replaceItem(E entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
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
            collection.set(idx, entity);
            changeType = CollectionChangeType.SET_ITEM;
        } else {
            collection.add(entity);
            changeType = CollectionChangeType.ADD_ITEMS;
        }
        attachListener(entity);
        buildIdMap();
        fireCollectionChanged(changeType, Collections.singletonList(entity));
    }

    @Override
    public E getItem(Object entityId) {
        E item = getItemOrNull(entityId);
        if (item == null)
            throw new IllegalArgumentException("Item with id='" + entityId + "' not found");
        return item;
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

    protected void fireCollectionChanged(CollectionChangeType type, Collection<? extends E> changes) {
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
