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

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.sys.EventTarget;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Object having a value.
 */
public interface HasValue<V> {
    V getValue();

    void setValue(V value);

    default void clear() {
        setValue(getEmptyValue());
    }

    default V getEmptyValue() {
        return null;
    }

    default boolean isEmpty() {
        return Objects.equals(getValue(), getEmptyValue());
    }

    @SuppressWarnings("unchecked")
    default Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener) {
        return ((EventTarget) this).addListener(ValueChangeEvent.class, (Consumer) listener);
    }

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    default void removeValueChangeListener(Consumer<ValueChangeEvent<V>> listener) {
        ((EventTarget) this).removeListener(ValueChangeEvent.class, (Consumer) listener);
    }

    /**
     * Describes value change event.
     */
    class ValueChangeEvent<V> extends EventObject {
        private final V prevValue;
        private final V value;

        // vaadin8 add isUserOriginated !!!!!

        public ValueChangeEvent(HasValue component, V prevValue, V value) {
            super(component);
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public HasValue<V> getSource() {
            return (HasValue) super.getSource();
        }

        /**
         * @return component
         */
        @SuppressWarnings("unchecked")
        public HasValue<V> getComponent() {
            return (HasValue) super.getSource();
        }

        /**
         * @return previous value
         */
        @Nullable
        public V getPrevValue() {
            return prevValue;
        }

        /**
         * @return current value
         */
        @Nullable
        public V getValue() {
            return value;
        }
    }
}