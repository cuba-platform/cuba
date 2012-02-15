/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

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
                            log.info(String.format("Couldn't find component %s in window %s", component, screenId));
                        }
                    }
                }
            }
        }
    }
}