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
import java.util.stream.Stream;

/**
 * todo JavaDoc
 *
 * @param <V> type of option object
 */
public interface OptionsSource<V> {
    Stream<V> getOptions();

    BindingState getState();

    Subscription addStateChangeListener(Consumer<StateChangeEvent<V>> listener);
    Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener);
    Subscription addOptionsChangeListener(Consumer<OptionsChangeEvent<V>> listener);

    // todo
    class StateChangeEvent<V> extends EventObject {
        protected BindingState state;

        public StateChangeEvent(OptionsSource<V> source, BindingState state) {
            super(source);
            this.state = state;
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionsSource<V> getSource() {
            return (OptionsSource<V>) super.getSource();
        }

        public BindingState getState() {
            return state;
        }
    }

    // todo
    class ValueChangeEvent<V> extends EventObject {
        private final V prevValue;
        private final V value;

        public ValueChangeEvent(OptionsSource<V> source, V prevValue, V value) {
            super(source);
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionsSource<V> getSource() {
            return (OptionsSource<V>) super.getSource();
        }

        public V getPrevValue() {
            return prevValue;
        }

        public V getValue() {
            return value;
        }
    }

    // todo
    class OptionsChangeEvent<V> extends EventObject {
        public OptionsChangeEvent(OptionsSource<V> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionsSource<V> getSource() {
            return (OptionsSource<V>) super.getSource();
        }
    }
}