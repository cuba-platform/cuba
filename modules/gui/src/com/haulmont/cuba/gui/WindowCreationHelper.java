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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used by the framework when it creates frames and windows. Not for use in application code.
 */
public final class WindowCreationHelper {

    private static final Pattern INNER_COMPONENT_PATTERN = Pattern.compile("(.+?)\\[(.+?)\\]");
    private static final Pattern COMPONENT_ACTION_PATTERN = Pattern.compile("(.+?)<(.+?)>");

    private static final Logger log = LoggerFactory.getLogger(WindowCreationHelper.class);

    private WindowCreationHelper() {
    }

    /**
     * Apply UI permissions to a frame.
     *
     * @param container frame
     */
    public static void applyUiPermissions(Frame container) {
        Window window = container instanceof Window ? (Window) container : ComponentsHelper.getWindow(container);
        if (window == null) {
            log.warn(String.format("Unable to find window for container %s with id '%s'", container.getClass(), container.getId()));
            return;
        }

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        UserSession userSession = sessionSource.getUserSession();

        String screenId = window.getId();
        Map<String, Integer> uiPermissions = userSession.getPermissionsByType(PermissionType.UI);
        for (Map.Entry<String, Integer> permissionEntry : uiPermissions.entrySet()) {
            String target = permissionEntry.getKey();
            String targetComponentId = getTargetComponentId(target, screenId);
            if (targetComponentId != null) {
                if (targetComponentId.contains("[")) {
                    applyCompositeComponentPermission(window, screenId, permissionEntry.getValue(), targetComponentId);
                } else if (targetComponentId.contains(">")) {
                    applyComponentActionPermission(window, screenId, permissionEntry.getValue(), targetComponentId);
                } else {
                    applyComponentPermission(window, screenId, permissionEntry.getValue(), targetComponentId);
                }
            }
        }
    }

    @Nullable
    private static String getTargetComponentId(String target, String screenId) {
        if (StringUtils.isNotBlank(target)) {
            int delimiterIndex = target.indexOf(Permission.TARGET_PATH_DELIMETER);
            if (delimiterIndex >= 0) {
                String targetScreenId = target.substring(0, delimiterIndex);
                if (Objects.equals(screenId, targetScreenId)) {
                    return target.substring(delimiterIndex + 1);
                }
            }
        }
        return null;
    }

    private static void applyComponentPermission(Window window, String screenId,
                                                 Integer permissionValue, String targetComponentId) {
        Component component = window.getComponent(targetComponentId);

        if (component != null) {
            if (permissionValue == UiPermissionValue.HIDE.getValue()) {
                component.setVisible(false);
            } else if (permissionValue == UiPermissionValue.READ_ONLY.getValue()) {
                if (component instanceof Component.Editable) {
                    ((Component.Editable) component).setEditable(false);
                } else {
                    component.setEnabled(false);
                }
            }
        } else {
            log.info(String.format("Couldn't find component %s in window %s", targetComponentId, screenId));
        }
    }

    private static void applyCompositeComponentPermission(Window window, String screenId,
                                                          Integer permissionValue, String componentId) {
        final Matcher matcher = INNER_COMPONENT_PATTERN.matcher(componentId);
        if (matcher.find()) {
            final String customComponentId = matcher.group(1);
            final String subComponentId = matcher.group(2);
            final Component compositeComponent = window.getComponent(customComponentId);

            if (compositeComponent != null) {
                if (compositeComponent instanceof UiPermissionAware) {
                    UiPermissionAware uiPermissionAwareComponent = (UiPermissionAware) compositeComponent;
                    UiPermissionValue uiPermissionValue = UiPermissionValue.fromId(permissionValue);

                    UiPermissionDescriptor permissionDescriptor;
                    if (subComponentId.contains("<")) {
                        final Matcher actionMatcher = COMPONENT_ACTION_PATTERN.matcher(subComponentId);
                        if (actionMatcher.find()) {
                            final String actionHolderComponentId = actionMatcher.group(1);
                            final String actionId = actionMatcher.group(2);

                            permissionDescriptor = new UiPermissionDescriptor(uiPermissionValue, screenId,
                                    actionHolderComponentId, actionId);
                        } else {
                            log.warn(String.format("Incorrect permission definition for component %s in window %s", subComponentId, screenId));
                            return;
                        }
                    } else {
                        permissionDescriptor = new UiPermissionDescriptor(uiPermissionValue, screenId, subComponentId);
                    }

                    uiPermissionAwareComponent.applyPermission(permissionDescriptor);
                }
            } else {
                log.info(String.format("Couldn't find component %s in window %s", componentId, screenId));
            }
        }
    }

    /**
     * Process permissions for actions in action holder
     *
     * @param window          Window
     * @param screenId        Screen Id
     * @param permissionValue Permission value
     * @param componentId     Component Id
     */
    private static void applyComponentActionPermission(Window window, String screenId,
                                                       Integer permissionValue, String componentId) {
        final Matcher matcher = COMPONENT_ACTION_PATTERN.matcher(componentId);
        if (matcher.find()) {
            final String customComponentId = matcher.group(1);
            final String actionId = matcher.group(2);
            final Component actionHolderComponent = window.getComponent(customComponentId);
            if (actionHolderComponent != null) {
                if (actionHolderComponent instanceof SecuredActionsHolder) {
                    ActionsPermissions permissions =
                            ((SecuredActionsHolder) actionHolderComponent).getActionsPermissions();
                    if (permissionValue == UiPermissionValue.HIDE.getValue()) {
                        permissions.addHiddenActionPermission(actionId);
                    } else if (permissionValue == UiPermissionValue.READ_ONLY.getValue()) {
                        permissions.addDisabledActionPermission(actionId);
                    }
                } else {
                    log.warn(String.format("Couldn't apply permission on action %s for component %s in window %s",
                            actionId, customComponentId, screenId));
                }
            } else {
                log.info(String.format("Couldn't find component %s in window %s", componentId, screenId));
            }
        } else {
            log.warn(String.format("Incorrect permission definition for component %s in window %s", componentId, screenId));
        }
    }
}