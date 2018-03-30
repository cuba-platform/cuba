/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.data;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * vaadin8 document
 *
 * @param <V>
 */
public interface EntityValueSource<E extends Entity, V> extends ValueSource<V> {
    MetaClass getMetaClass();
    MetaPropertyPath getMetaPropertyPath();

    E getItem();

    Subscription addInstanceChangeListener(Consumer<InstanceChangeEvent<E>> listener);

    class InstanceChangeEvent<E extends Entity> extends EventObject {
        private final E prevItem;
        private final E item;

        public InstanceChangeEvent(EntityValueSource<E, ?> source, E prevItem, E item) {
            super(source);
            this.prevItem = prevItem;
            this.item = item;
        }

        @SuppressWarnings("unchecked")
        @Override
        public EntityValueSource<E, ?> getSource() {
            return (EntityValueSource<E, ?>) super.getSource();
        }

        /**
         * @return current item
         */
        @Nullable
        public E getItem() {
            return item;
        }

        /**
         * @return previous selected item
         */
        @Nullable
        public E getPrevItem() {
            return prevItem;
        }
    }
}