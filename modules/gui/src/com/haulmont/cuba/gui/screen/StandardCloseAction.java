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
 *
 */

package com.haulmont.cuba.gui.screen;

/**
 * A close action implementation used in the base screens provided by the framework: {@link Screen},
 * {@link StandardEditor}, {@link StandardLookup}.
 * <p>
 * If its {@link #isCheckForUnsavedChanges()} flag is set to true, the screen checks if it contains unsaved changes and asks the
 * user whether to commit or discard them.
 */
public class StandardCloseAction implements CloseAction, ChangeTrackerCloseAction {

    private final String actionId;
    private final boolean checkForUnsavedChanges;

    /**
     * Constructs the close action with the given id and {@link #checkForUnsavedChanges} flag set to true.
     *
     * @param actionId an identifier of the close action to distinguish it from other actions of the same type
     */
    public StandardCloseAction(String actionId) {
        this(actionId, true);
    }

    /**
     * Constructs the close action with the given id and {@link #isCheckForUnsavedChanges()} flag.
     *
     * @param actionId an identifier of the close action to distinguish it from other actions of the same type
     * @param checkForUnsavedChanges indicates whether the screen using this action should check for unsaved changes
     */
    public StandardCloseAction(String actionId, boolean checkForUnsavedChanges) {
        this.actionId = actionId;
        this.checkForUnsavedChanges = checkForUnsavedChanges;
    }

    /**
     * An identifier of the close action to distinguish it from other actions of the same type.
     */
    public String getActionId() {
        return actionId;
    }

    @Override
    public String toString() {
        return "CloseAction{" +
                "actionId='" + actionId + '\'' +
                '}';
    }

    @Override
    public boolean isCheckForUnsavedChanges() {
        return checkForUnsavedChanges;
    }
}