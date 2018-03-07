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

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 *
 */
@SuppressWarnings("NullableProblems")
public class ObservableSet<T> extends ForwardingSet<T> {

    private final Set<T> delegate;
    private final Runnable onCollectionChanged;

    public ObservableSet(Set<T> delegate, Runnable onCollectionChanged) {
        this.delegate = delegate;
        this.onCollectionChanged = onCollectionChanged;
    }

    private Object writeReplace() throws ObjectStreamException {
        return delegate;
    }

    protected void fireCollectionChanged() {
        if (onCollectionChanged != null)
            onCollectionChanged.run();
    }

    @Override
    protected Set<T> delegate() {
        return delegate;
    }

    @Override
    public boolean add(T element) {
        boolean changed = super.add(element);
        if (changed)
            fireCollectionChanged();
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = super.removeAll(collection);
        if (changed)
            fireCollectionChanged();
        return changed;
    }

    @Override
    public boolean remove(Object object) {
        boolean changed = super.remove(object);
        if (changed)
            fireCollectionChanged();
        return changed;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean changed = super.addAll(collection);
        if (changed)
            fireCollectionChanged();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = super.retainAll(collection);
        if (changed)
            fireCollectionChanged();
        return changed;
    }

    @Override
    public void clear() {
        boolean wasEmpty = isEmpty();
        super.clear();
        if (!wasEmpty)
            fireCollectionChanged();
    }

    @Override
    public Iterator<T> iterator() {
        return new ObservableIterator<>(super.iterator(), onCollectionChanged);
    }
}
