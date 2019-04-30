/*
 * Copyright (c) 2008-2019 Haulmont.
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

import java.util.function.Consumer;

/**
 * Component that fires {@link AttachEvent} and {@link DetachEvent} events.
 */
public interface AttachNotifier {

    /**
     * @return whether a component is attached to a window
     */
    boolean isAttached();

    /**
     * Notifies all listeners that component has been attached.
     */
    void attached();

    /**
     * Notifies all listeners that component has been detached.
     */
    void detached();

    /**
     * Registers a new attached listener.
     *
     * @param listener a listener to add
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addAttachListener(Consumer<AttachEvent> listener);

    /**
     * Registers a new detached listener.
     *
     * @param listener a listener to add
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addDetachListener(Consumer<DetachEvent> listener);
}
