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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.CollectionContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CollectionContainerImpl<T extends Entity> extends InstanceContainerImpl<T> implements CollectionContainer<T> {

    protected List<T> collection = new ArrayList<>();

    public CollectionContainerImpl(MetaClass metaClass) {
        super(metaClass);
    }

    @Override
    public List<T> getItems() {
        return Collections.unmodifiableList(collection);
    }

    @Override
    public List<T> getMutableItems() {
        return new ObservableList<>(collection, this::fireCollectionChanged);
    }


    @Override
    public void setItems(@Nullable Collection<T> entities) {
        detachListener(collection);
        collection.clear();
        if (entities != null) {
            collection.addAll(entities);
            attachListener(collection);
        }
        fireCollectionChanged();
        
        if (item != null && !collection.contains(item)) {
            setItem(null);
        }
    }

    @Nullable
    @Override
    public T getItem(Object entityId) {
        return collection.stream()
                .filter(entity -> entity.getId().equals(entityId))
                .findAny()
                .orElse(null);
    }

    @Override
    public void addCollectionChangeListener(CollectionChangeListener<T> listener) {
        getEventRouter().addListener(CollectionChangeListener.class, listener);
    }

    @Override
    public void removeCollectionChangeListener(CollectionChangeListener<T> listener) {
        getEventRouter().removeListener(CollectionChangeListener.class, listener);
    }

    protected void fireCollectionChanged() {
        CollectionChangeEvent<T> collectionChangeEvent = new CollectionChangeEvent<>(this);
        //noinspection unchecked
        getEventRouter().fireEvent(CollectionChangeListener.class, CollectionChangeListener::collectionChanged, collectionChangeEvent);
    }

    protected void attachListener(Collection<T> entities) {
        for (T entity : entities) {
            attachListener(entity);
        }
    }

    protected void detachListener(Collection<T> entities) {
        for (T entity : entities) {
            detachListener(entity);
        }
    }
}
