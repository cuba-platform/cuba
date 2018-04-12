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
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import com.haulmont.cuba.gui.data.ValueListener;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.Objects;

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

    /**
     * vaadin8 for removal
     *
     * @deprecated Use {@link #addValueChangeListener(ValueChangeListener)}
     */
    @Deprecated
    default void addListener(ValueListener listener) {
        addValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    // vaadin8 for removal
    @Deprecated
    default void removeListener(ValueListener listener) {
        removeValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    Subscription addValueChangeListener(ValueChangeListener listener);
    void removeValueChangeListener(ValueChangeListener listener);

    /**
     * Describes value change event.
     *
     * todo V parameter
     */
    class ValueChangeEvent extends EventObject {
        private final HasValue component;
        private final Object prevValue;
        private final Object value;

        // vaadin8 add isUserOriginated !!!

        public ValueChangeEvent(HasValue component, Object prevValue, Object value) {
            super(component);
            this.component = component;
            this.prevValue = prevValue;
            this.value = value;
        }

        @Override
        public HasValue getSource() {
            return (HasValue) super.getSource();
        }

        /**
         * @return component
         */
        public HasValue getComponent() {
            return component;
        }

        /**
         * @return previous value
         */
        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        /**
         * @return current value
         */
        @Nullable
        public Object getValue() {
            return value;
        }
    }

    /**
     * Listener to value change events.
     */
    @FunctionalInterface
    interface ValueChangeListener {
        /**
         * Called when value of Component changed.
         *
         * @param e event object
         */
        void valueChanged(ValueChangeEvent e);
    }
}