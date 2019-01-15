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

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.ValueSource;

/**
 * Object that holds data binding information for {@link HasValue} UI component.
 *
 * @param <V> type of value
 */
public interface ValueBinding<V> extends Binding {
    ValueSource<V> getSource();
    HasValue<V> getComponent();

    /**
     * Activates value binding - reads value from source to UI component if state is {@link BindingState#ACTIVE}.
     */
    void activate();

    /**
     * Updates all changes since the previous write to the value source.
     */
    void write();

    /**
     * Discards all changes since last write. The component updates its value from the value source.
     */
    void discard();

    /**
     * @return {@code true} if the buffered mode is on, {@code false} otherwise
     */
    boolean isBuffered();

    /**
     * Sets the buffered mode.
     * <p>
     * When in buffered mode, the component value changes will not be reflected
     * in value source until {@link #write()} is called. Calling {@link #discard()}
     * will revert the components value to the value of the value source.
     * <p>
     * When in non-buffered mode both read and write operations will be done
     * directly on the value source. In this mode the {@link #write()} and
     * {@link #discard()} methods serve no purpose.
     * <p>
     * If the value in the component has been modified since the last value source update
     * and the buffered mode is switched off at runtime, then the component will
     * update its value from the value source.
     *
     * @param buffered {@code true} if the buffered mode should be turned on, {@code false} otherwise
     */
    void setBuffered(boolean buffered);

    /**
     * Tests if the value stored in the component has been modified since it was
     * last updated from the value source.
     *
     * @return {@code true} if the value in the component has been modified
     * since the last value source update, {@code false} if not.
     */
    boolean isModified();
}