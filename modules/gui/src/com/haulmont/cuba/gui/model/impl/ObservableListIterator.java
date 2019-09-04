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

import com.google.common.collect.ForwardingListIterator;
import com.haulmont.cuba.gui.model.CollectionChangeType;

import java.util.Collection;
import java.util.Collections;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 */
@SuppressWarnings("NullableProblems")
class ObservableListIterator<T> extends ForwardingListIterator<T> {

    private ListIterator<T> delegate;
    private BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged;
    private Consumer<T> onAddItem;

    protected ObservableListIterator(ListIterator<T> delegate,
                                     BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged,
                                     Consumer<T> onAddItem) {
        this.delegate = delegate;
        this.onCollectionChanged = onCollectionChanged;
        this.onAddItem = onAddItem;
    }

    protected void fireCollectionChanged(CollectionChangeType type, Collection<? extends T> changes) {
        if (onCollectionChanged != null)
            onCollectionChanged.accept(type, changes);
    }

    @Override
    protected ListIterator<T> delegate() {
        return delegate;
    }

    @Override
    public void add(T element) {
        super.add(element);
        doOnAddItem(element);
        fireCollectionChanged(CollectionChangeType.ADD_ITEMS, Collections.singletonList(element));
    }

    @Override
    public void set(T element) {
        throw new UnsupportedOperationException("ObservableListIterator does not support 'set' operation");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("ObservableListIterator does not support 'remove' operation");
    }

    protected void doOnAddItem(T item) {
        if (item != null && onAddItem != null) {
            onAddItem.accept(item);
        }
    }
}
