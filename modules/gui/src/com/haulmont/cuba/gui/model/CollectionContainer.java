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

package com.haulmont.cuba.gui.model;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public interface CollectionContainer<E extends Entity> extends InstanceContainer<E> {

    List<E> getItems();

    List<E> getMutableItems();

    void setItems(@Nullable Collection<E> entities);

    E getItem(Object entityId);

    @Nullable
    E getItemOrNull(Object entityId);

    int getItemIndex(Object entityId);

    /**
     *
     */
    class CollectionChangeEvent<T extends Entity> extends EventObject {

        public CollectionChangeEvent(CollectionContainer<T> container) {
            super(container);
        }

        @SuppressWarnings("unchecked")
        @Override
        public CollectionContainer<T> getSource() {
            return (CollectionContainer) super.getSource();
        }

        @Override
        public String toString() {
            return "CollectionChangeEvent{" +
                    "source=" + source +
                    '}';
        }
    }

    Subscription addCollectionChangeListener(Consumer<CollectionChangeEvent<E>> listener);

    Sorter getSorter();

    void setSorter(Sorter sorter);
}
