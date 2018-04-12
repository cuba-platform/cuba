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

/**
 * Data aware component that supports buffered write mode.
 */
public interface Buffered {
    /**
     * Updates all changes since the previous commit to the data source.
     */
    void commit();

    /**
     * Discards all changes since last commit. The object updates its value from the data source.
     */
    void discard();

    /**
     * @return {@code true} if buffered mode is on, {@code false} otherwise
     */
    boolean isBuffered();

    /**
     * Sets the buffered mode.
     * <p>
     * When in buffered mode, an internal buffer will be used to store changes
     * until {@link #commit()} is called. Calling {@link #discard()} will revert
     * the internal buffer to the value of the data source.
     * <p>
     * When in non-buffered mode both read and write operations will be done
     * directly on the data source. In this mode the {@link #commit()} and
     * {@link #discard()} methods serve no purpose.
     *
     * @param buffered {@code true} if buffered mode should be turned on, {@code false} otherwise
     */
    void setBuffered(boolean buffered);

    /**
     * Tests if the value stored in the object has been modified since it was
     * last updated from the data source.
     *
     * @return {@code true} if the value in the object has been modified
     *         since the last data source update, {@code false} if not.
     */
    boolean isModified();
}