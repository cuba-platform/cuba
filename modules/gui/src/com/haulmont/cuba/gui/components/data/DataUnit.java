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

public interface DataUnit {
    /**
     * @return a state of this source
     */
    BindingState getState();

    /**
     * Registers a new state change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addStateChangeListener(Consumer<StateChangeEvent> listener);

    /**
     * An event that is fired when DataUnit state is changed.
     */
    class StateChangeEvent extends EventObject {
        protected BindingState state;

        public StateChangeEvent(DataUnit source, BindingState state) {
            super(source);
            this.state = state;
        }

        @Override
        public DataUnit getSource() {
            return (DataUnit) super.getSource();
        }

        public BindingState getState() {
            return state;
        }
    }
}