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

import com.google.common.collect.ForwardingList;
import com.haulmont.cuba.gui.model.CollectionChangeType;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 */
@SuppressWarnings("NullableProblems")
public class ObservableList<T> extends ForwardingList<T> implements Serializable {

    private List<T> delegate;
    private BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged;

    public ObservableList(List<T> delegate, BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged) {
        this.delegate = delegate;
        this.onCollectionChanged = onCollectionChanged;
    }

    private Object writeReplace() throws ObjectStreamException {
        return delegate;
    }

    protected void fireCollectionChanged(CollectionChangeType type, Collection<? extends T> changes) {
        if (onCollectionChanged != null)
            onCollectionChanged.accept(type, changes);
    }

    protected void fireCollectionRefreshed() {
        fireCollectionChanged(CollectionChangeType.REFRESH, Collections.emptyList());
    }

    @Override
    protected List<T> delegate() {
        return delegate;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        fireCollectionChanged(CollectionChangeType.ADD_ITEMS, Collections.singletonList(element));
    }

    @Override
    public boolean add(T element) {
        boolean changed = super.add(element);
        if (changed)
            fireCollectionChanged(CollectionChangeType.ADD_ITEMS, Collections.singletonList(element));
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> elements) {
        boolean changed = super.addAll(index, elements);
        if (changed)
            fireCollectionChanged(CollectionChangeType.ADD_ITEMS, elements);
        return changed;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean changed = super.addAll(collection);
        if (changed)
            fireCollectionChanged(CollectionChangeType.ADD_ITEMS, collection);
        return changed;
    }

    @Override
    public T set(int index, T element) {
        T prev = super.set(index, element);
        if (prev != element) {
            fireCollectionChanged(CollectionChangeType.SET_ITEM, Collections.singletonList(element));
        }
        return prev;
    }

    @Override
    public T remove(int index) {
        T entity = super.remove(index);
        fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, Collections.singletonList(entity));
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object object) {
        boolean changed = super.remove(object);
        if (changed)
            fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, Collections.singletonList((T) object));
        return changed;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = super.removeAll(collection);
        if (changed)
            fireCollectionRefreshed();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = super.retainAll(collection);
        if (changed)
            fireCollectionRefreshed();
        return changed;
    }

    @Override
    public void clear() {
        boolean wasEmpty = isEmpty();
        super.clear();
        if (!wasEmpty)
            fireCollectionRefreshed();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ObservableListIterator<>(super.listIterator(), onCollectionChanged);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ObservableListIterator<>(super.listIterator(index), onCollectionChanged);
    }

    @Override
    public Iterator<T> iterator() {
        return new ObservableIterator<>(super.iterator(), onCollectionChanged);
    }
}
