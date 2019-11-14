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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.gui.components.Window;

/**
 * Marker interface for UI controllers.
 *
 * @see Screen
 * @see ScreenFragment
 */
public interface FrameOwner {

    /**
     * A {@link CloseAction} used when the screen is closed without an explicit commit. However, the screen notifies
     * the user if there are unsaved changes.
     */
    CloseAction WINDOW_CLOSE_ACTION = new StandardCloseAction(Window.CLOSE_ACTION_ID);

    /**
     * A {@link CloseAction} used when the screen is closed after an explicit commit. If the screen still contains
     * unsaved changes, the user is notified about it.
     */
    CloseAction WINDOW_COMMIT_AND_CLOSE_ACTION = new StandardCloseAction(Window.COMMIT_ACTION_ID);

    /**
     * A {@link CloseAction} used when the screen is closed without an explicit commit and it should not notify the user
     * about unsaved changes.
     */
    CloseAction WINDOW_DISCARD_AND_CLOSE_ACTION = new StandardCloseAction(Window.CLOSE_ACTION_ID, false);

    ScreenOptions NO_OPTIONS = new ScreenOptions() {
        @Override
        public String toString() {
            return "{NO OPTIONS}";
        }
    };
}