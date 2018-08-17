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

public class StandardCloseAction implements CloseAction {

    private final String actionId;
    private final boolean checkForUnsavedChanges;

    public StandardCloseAction(String actionId) {
        this.actionId = actionId;
        this.checkForUnsavedChanges = true;
    }

    public StandardCloseAction(String actionId, boolean checkForUnsavedChanges) {
        this.actionId = actionId;
        this.checkForUnsavedChanges = checkForUnsavedChanges;
    }

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