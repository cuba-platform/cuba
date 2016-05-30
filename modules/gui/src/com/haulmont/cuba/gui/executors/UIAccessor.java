/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.cuba.gui.executors;

/**
 * Interface that allows to read/write state of UI from background threads.
 *
 * @see BackgroundWorker#getUIAccessor()
 */
public interface UIAccessor {
    /**
     * Provides exclusive access to UI state from outside a UI event handling thread.
     *
     * The given runnable is executed while holding the UI lock to ensure
     * exclusive access to UI state.
     *
     * Please note that the runnable might be invoked on a different thread or
     * later on the current thread, which means that custom thread locals might
     * not have the expected values when the runnable is executed.
     *
     * @param runnable runnable
     */
    void access(Runnable runnable);

    /**
     * Locks the UI and runs the provided Runnable right away.
     *
     * The given runnable is executed while holding the UI lock to ensure
     * exclusive access to UI state.
     *
     * @param runnable runnable
     */
    void accessSynchronously(Runnable runnable);
}