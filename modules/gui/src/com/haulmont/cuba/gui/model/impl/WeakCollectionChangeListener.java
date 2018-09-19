/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.CollectionContainer;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public class WeakCollectionChangeListener<E extends Entity>
        implements Consumer<CollectionContainer.CollectionChangeEvent<E>> {

    private WeakReference<Consumer<CollectionContainer.CollectionChangeEvent<E>>> reference;
    private Subscription subscription;

    @SuppressWarnings("unchecked")
    public WeakCollectionChangeListener(CollectionContainer container,
                                        Consumer<CollectionContainer.CollectionChangeEvent<E>> collectionChangeListener) {
        reference = new WeakReference(collectionChangeListener);
        subscription = container.addCollectionChangeListener(this);
    }

    @Override
    public void accept(CollectionContainer.CollectionChangeEvent<E> e) {
        Consumer<CollectionContainer.CollectionChangeEvent<E>> collectionChangeListener = reference.get();
        if (collectionChangeListener != null) {
            collectionChangeListener.accept(e);
        } else {
            subscription.remove();
        }
    }

    public void removeItself() {
        subscription.remove();
    }
}