/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import java.util.HashSet;

/**
 * @author artamonov
 * @version $Id$
 */
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
            ((Action.UiPermissionAware) action).setUiPermissionEnabled(false);
        }
    }

    public void addHiddenActionPermission(String actionId) {
        if (hiddenActionsIds == null) {
            hiddenActionsIds = new HashSet<>();
        }

        hiddenActionsIds.add(actionId);
        Action action = actionsHolder.getAction(actionId);
        if (action instanceof Action.UiPermissionAware) {
            ((Action.UiPermissionAware) action).setUiPermissionVisible(false);
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
                ((Action.UiPermissionAware) action).setUiPermissionVisible(false);
            }
            if (isDisabledAction(action.getId())) {
                ((Action.UiPermissionAware) action).setUiPermissionEnabled(false);
            }
        }
    }
}