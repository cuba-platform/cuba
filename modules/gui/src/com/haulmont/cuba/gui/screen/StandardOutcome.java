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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.gui.builders.AfterScreenCloseEvent;

/**
 * A possible outcome of screens extending {@link StandardEditor} and {@link StandardLookup}.
 * <p>
 * Constants of this enum can be used instead of {@link CloseAction} instances in {@link Screen#close(StandardOutcome)}
 * method to initiate closing and in {@link AfterScreenCloseEvent#closedWith(StandardOutcome)}
 * method to determine how the screen was closed.
 *
 * @see #CLOSE
 * @see #COMMIT
 * @see #DISCARD
 * @see #SELECT
 */
public enum StandardOutcome {

    /**
     * The screen is closed without an explicit commit. However, the screen notifies the user if there are unsaved changes.
     */
    CLOSE(FrameOwner.WINDOW_CLOSE_ACTION),

    /**
     * The screen is closed after an explicit commit. If the screen still contains unsaved changes, the user is notified about it.
     */
    COMMIT(FrameOwner.WINDOW_COMMIT_AND_CLOSE_ACTION),

    /**
     * The screen is closed without an explicit commit and it did not notify the user about unsaved changes.
     */
    DISCARD(FrameOwner.WINDOW_DISCARD_AND_CLOSE_ACTION),

    /**
     * The screen is closed after the user selected an item in the lookup component.
     */
    SELECT(LookupScreen.LOOKUP_SELECT_CLOSE_ACTION);

    private CloseAction closeAction;

    StandardOutcome(CloseAction closeAction) {
        this.closeAction = closeAction;
    }

    public CloseAction getCloseAction() {
        return closeAction;
    }
}
