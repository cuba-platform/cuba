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

package com.haulmont.cuba.gui.components.data.meta;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.Options;

import java.util.EventObject;
import java.util.function.Consumer;

public interface EntityOptions<E extends Entity> extends Options<E>, EntityDataUnit {
    /**
     * Set current item in the source.
     *
     * @param item the item to set
     */
    void setSelectedItem(E item);

    /**
     * @return true if the underlying collection contains an item with the specified ID
     */
    boolean containsItem(E item);

    /**
     * Update an item in the collection if it is already there.
     */
    void updateItem(E item);

    /**
     * Refreshes the source moving it to the {@link BindingState#ACTIVE} state
     */
    void refresh();

    Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener);

    class ValueChangeEvent<T> extends EventObject {
        private final T prevValue;
        private final T value;

        public ValueChangeEvent(Options<T> source, T prevValue, T value) {
            super(source);
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Options<T> getSource() {
            return (Options<T>) super.getSource();
        }

        public T getPrevValue() {
            return prevValue;
        }

        public T getValue() {
            return value;
        }
    }
}