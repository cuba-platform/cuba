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

import java.util.EventObject;
import java.util.function.Consumer;

/**
 * vaadin8 document
 *
 * @param <V> todo
 */
public interface ValueSource<V> {
    V getValue();
    void setValue(V value);

    boolean isReadOnly();

    Class<V> getType();

    BindingState getState();

    Subscription addStateChangeListener(Consumer<StateChangeEvent<V>> listener);
    Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener);

    // todo
    class StateChangeEvent<V> extends EventObject {
        protected BindingState state;

        public StateChangeEvent(ValueSource<V> source, BindingState state) {
            super(source);
            this.state = state;
        }

        @SuppressWarnings("unchecked")
        @Override
        public ValueSource<V> getSource() {
            return (ValueSource<V>) super.getSource();
        }

        public BindingState getState() {
            return state;
        }
    }

    // todo
    class ValueChangeEvent<V> extends EventObject {
        private final V prevValue;
        private final V value;

        public ValueChangeEvent(ValueSource<V> source, V prevValue, V value) {
            super(source);
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public ValueSource<V> getSource() {
            return (ValueSource<V>) super.getSource();
        }

        public V getPrevValue() {
            return prevValue;
        }

        public V getValue() {
            return value;
        }
    }
}