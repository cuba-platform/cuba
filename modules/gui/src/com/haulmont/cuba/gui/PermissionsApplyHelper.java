/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper for apply permissions to UI components
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class PermissionsApplyHelper {

    private static Log log = LogFactory.getLog(PermissionsApplyHelper.class);

    private PermissionsApplyHelper() {
    }

    /**
     * Apply UI permissions for frame
     *
     * @param container Frame
     */
    public static void applyUiPermissions(IFrame container) {
        Window window = ComponentsHelper.getWindow(container);
        UserSession userSession = UserSessionProvider.getUserSession();

        String screenId = window.getId();
        Map<String, Integer> uiPermissions = userSession.getPermissionsByType(PermissionType.UI);
        for (Map.Entry<String, Integer> permissionEntry : uiPermissions.entrySet()) {
            String target = permissionEntry.getKey();
            if (StringUtils.isNotBlank(target)) {
                int delimeterIndex = target.indexOf(Permission.TARGET_PATH_DELIMETER);
                if (delimeterIndex >= 0) {

                    // Target screen
                    String targetScreenId = target.substring(0, delimeterIndex);
                    if (StringUtils.equals(screenId, targetScreenId)) {

                        // Target component
                        String componentId = target.substring(delimeterIndex + 1);
                        if (componentId.contains("[")) {//custom process for tabsheet & fieldgroup
                            processCustomComponents(window, screenId, permissionEntry, componentId);
                        } else {
                            Component component = window.getComponent(componentId);

                            if (component != null) {
                                Integer permissionValue = permissionEntry.getValue();
                                if (permissionValue == UiPermissionValue.HIDE.getValue())
                                    component.setVisible(false);
                                else if (permissionValue == UiPermissionValue.READ_ONLY.getValue()) {
                                    if (component instanceof Component.Editable) {
                                        ((Component.Editable) component).setEditable(false);
                                    } else {
                                        component.setEnabled(false);
                                    }
                                }

                            } else {
                                log.info(String.format("Couldn't find component %s in window %s", componentId, screenId));
                            }
                        }
                    }
                }
            }
        }
    }

    private static void processCustomComponents(Window window, String screenId, Map.Entry<String, Integer> permissionEntry, String componentId) {
        final Pattern pattern = Pattern.compile("(.+?)\\[(.+?)\\]");
        final Matcher matcher = pattern.matcher(componentId);
        if (matcher.find()) {
            final String customComponentId = matcher.group(1);
            final String subComponentId = matcher.group(2);
            final Component customComponent = window.getComponent(customComponentId);
            if (customComponent != null) {
                if (customComponent instanceof Tabsheet) {
                    final Tabsheet tabsheet = (Tabsheet) customComponent;
                    final Tabsheet.Tab tab = tabsheet.getTab(subComponentId);
                    if (tab != null) {
                        Integer permissionValue = permissionEntry.getValue();
                        if (permissionValue == UiPermissionValue.HIDE.getValue())
                            tab.setVisible(false);
                        else if (permissionValue == UiPermissionValue.READ_ONLY.getValue()) {
                            tab.setEnabled(false);
                        }
                    }
                } else if (customComponent instanceof FieldGroup) {
                    FieldGroup fieldGroup = (FieldGroup) customComponent;
                    final FieldGroup.Field field = fieldGroup.getField(subComponentId);
                    if (field != null) {
                        Integer permissionValue = permissionEntry.getValue();
                        if (permissionValue == UiPermissionValue.HIDE.getValue()) {
                            fieldGroup.setVisible(field, false);
                        } else if (permissionValue == UiPermissionValue.READ_ONLY.getValue()) {
                            fieldGroup.setEditable(field, false);
                        }
                    }
                }
            } else {
                log.info(String.format("Couldn't find component %s in window %s", componentId, screenId));
            }
        }
    }
}