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

import com.google.common.collect.ForwardingIterator;
import com.haulmont.cuba.gui.model.CollectionChangeType;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.BiConsumer;

/**
 *
 */
class ObservableIterator<T> extends ForwardingIterator<T> {

    private Iterator<T> delegate;
    private BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged;

    protected ObservableIterator(Iterator<T> delegate,
                                 BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged) {
        this.delegate = delegate;
        this.onCollectionChanged = onCollectionChanged;
    }

    protected void fireCollectionChanged(CollectionChangeType type, Collection<? extends T> changes) {
        if (onCollectionChanged != null)
            onCollectionChanged.accept(type, changes);
    }

    @Override
    protected Iterator<T> delegate() {
        return delegate;
    }

    @Override
    public void remove() {
        super.remove();
        fireCollectionChanged(CollectionChangeType.REFRESH, Collections.emptyList());
    }
}