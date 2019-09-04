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

import com.google.common.collect.ForwardingSet;
import com.haulmont.cuba.gui.model.CollectionChangeType;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;

@SuppressWarnings("NullableProblems")
public class ObservableSet<T> extends ForwardingSet<T> implements Serializable {

    private static final long serialVersionUID = 7237243645914200614L;

    private final Set<T> delegate;
    private transient final BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged;

    public ObservableSet(Set<T> delegate, BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged) {
        this.delegate = delegate;
        this.onCollectionChanged = onCollectionChanged;
    }

    private Object writeReplace() throws ObjectStreamException {
        Set result = delegate;
        while (result instanceof ObservableSet) {
            result = ((ObservableSet) result).delegate;
        }
        return result;
    }

    protected void fireCollectionChanged(CollectionChangeType type, Collection<? extends T> changes) {
        if (onCollectionChanged != null)
            onCollectionChanged.accept(type, changes);
    }

    protected void fireCollectionRefreshed() {
        fireCollectionChanged(CollectionChangeType.REFRESH, Collections.emptyList());
    }

    @Override
    protected Set<T> delegate() {
        return delegate;
    }

    @Override
    public boolean add(T element) {
        boolean changed = super.add(element);
        if (changed)
            fireCollectionChanged(CollectionChangeType.ADD_ITEMS, Collections.singletonList(element));
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = super.removeAll(collection);
        if (changed)
            fireCollectionRefreshed();
        return changed;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object object) {
        boolean changed = super.remove(object);
        if (changed)
            fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, Collections.singletonList((T) object));
        return changed;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean changed = super.addAll(collection);
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
    public Iterator<T> iterator() {
        return new ObservableIterator<>(super.iterator());
    }
}