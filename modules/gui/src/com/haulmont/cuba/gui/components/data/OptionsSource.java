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
 * @param <I> type of option object
 */
public interface OptionsSource<I> {
    Stream<I> getOptions();

    BindingState getState();

    Subscription addStateChangeListener(Consumer<StateChangeEvent<I>> listener);
    Subscription addValueChangeListener(Consumer<ValueChangeEvent<I>> listener);
    Subscription addOptionsChangeListener(Consumer<OptionsChangeEvent<I>> listener);

    // todo
    class StateChangeEvent<T> extends EventObject {
        protected BindingState state;

        public StateChangeEvent(OptionsSource<T> source, BindingState state) {
            super(source);
            this.state = state;
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionsSource<T> getSource() {
            return (OptionsSource<T>) super.getSource();
        }

        public BindingState getState() {
            return state;
        }
    }

    // todo
    class ValueChangeEvent<T> extends EventObject {
        private final T prevValue;
        private final T value;

        public ValueChangeEvent(OptionsSource<T> source, T prevValue, T value) {
            super(source);
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionsSource<T> getSource() {
            return (OptionsSource<T>) super.getSource();
        }

        public T getPrevValue() {
            return prevValue;
        }

        public T getValue() {
            return value;
        }
    }

    // todo
    class OptionsChangeEvent<T> extends EventObject {
        public OptionsChangeEvent(OptionsSource<T> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionsSource<T> getSource() {
            return (OptionsSource<T>) super.getSource();
        }
    }
}