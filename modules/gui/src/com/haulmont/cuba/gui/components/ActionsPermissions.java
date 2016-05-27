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
 *
 */

package com.haulmont.cuba.gui.components;

import java.util.HashSet;

public class ActionsPermissions {

    private Component.SecuredActionsHolder actionsHolder;

    private HashSet<String> disabledActionsIds;
    private HashSet<String> hiddenActionsIds;

    public ActionsPermissions(Component.SecuredActionsHolder actionsHolder) {
        this.actionsHolder = actionsHolder;
    }

    public void addDisabledActionPermission(String actionId) {
        if (disabledActionsIds == null) {
            disabledActionsIds = new HashSet<>();
        }
        disabledActionsIds.add(actionId);

        Action action = actionsHolder.getAction(actionId);
        if (action instanceof Action.UiPermissionAware) {
            ((Action.UiPermissionAware) action).setEnabledByUiPermissions(false);
        }
    }

    public void addHiddenActionPermission(String actionId) {
        if (hiddenActionsIds == null) {
            hiddenActionsIds = new HashSet<>();
        }

        hiddenActionsIds.add(actionId);
        Action action = actionsHolder.getAction(actionId);
        if (action instanceof Action.UiPermissionAware) {
            ((Action.UiPermissionAware) action).setVisibleByUiPermissions(false);
        }
    }

    public boolean isHiddenAction(String actionId) {
        if (hiddenActionsIds == null) {
            return false;
        }
        return hiddenActionsIds.contains(actionId);
    }

    public boolean isDisabledAction(String actionId) {
        if (disabledActionsIds == null) {
            return false;
        }
        return disabledActionsIds.contains(actionId);
    }

    public void apply(Action action) {
        if (action instanceof Action.UiPermissionAware) {
            if (isHiddenAction(action.getId())) {
                ((Action.UiPermissionAware) action).setVisibleByUiPermissions(false);
            }
            if (isDisabledAction(action.getId())) {
                ((Action.UiPermissionAware) action).setEnabledByUiPermissions(false);
            }
        }
    }
}