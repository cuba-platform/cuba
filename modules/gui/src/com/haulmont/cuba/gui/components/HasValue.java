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

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * UI component having a value.
 *
 * @param <V> value type
 */
public interface HasValue<V> extends Component {
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

    Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeValueChangeListener(Consumer<ValueChangeEvent<V>> listener);

    /**
     * Describes value change event.
     */
    class ValueChangeEvent<V> extends EventObject implements HasUserOriginated {
        private final V prevValue;
        private final V value;
        private final boolean userOriginated;

        public ValueChangeEvent(HasValue component, V prevValue, V value) {
            this(component, prevValue, value, false);
        }

        public ValueChangeEvent(Object source, V prevValue, V value, boolean userOriginated) {
            super(source);
            this.prevValue = prevValue;
            this.value = value;
            this.userOriginated = userOriginated;
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

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }
}