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

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.security.entity.ScreenComponentPermission;

/**
 * Stores id of subcomponent and UI permission value which will be applied to this subcomponent
 * or ids of subcomponent and its action and UI permission value which will be applied to subcomponent's action.
 */
public class UiPermissionDescriptor {

    private ScreenComponentPermission permissionValue;
    private String screenId;
    private String subComponentId;

    private String actionHolderComponentId;
    private String actionId;

    public UiPermissionDescriptor(ScreenComponentPermission permissionValue, String screenId, String subComponentId) {
        this.permissionValue = permissionValue;
        this.screenId = screenId;
        this.subComponentId = subComponentId;
    }

    public UiPermissionDescriptor(ScreenComponentPermission permissionValue, String screenId, String actionHolderComponentId,
                                  String actionId) {
        this.permissionValue = permissionValue;
        this.screenId = screenId;

        this.actionHolderComponentId = actionHolderComponentId;
        this.actionId = actionId;
    }

    public ScreenComponentPermission getPermissionValue() {
        return permissionValue;
    }

    public String getScreenId() {
        return screenId;
    }

    public String getSubComponentId() {
        return subComponentId;
    }

    public String getActionHolderComponentId() {
        return actionHolderComponentId;
    }

    public String getActionId() {
        return actionId;
    }
}