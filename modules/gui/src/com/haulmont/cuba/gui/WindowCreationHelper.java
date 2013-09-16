/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used by the framework when it creates frames and windows. Not for use in application code.
 *
 * @author artamonov
 * @version $Id$
 */
public class WindowCreationHelper {

    private static Log log = LogFactory.getLog(WindowCreationHelper.class);

    private WindowCreationHelper() {
    }

    /**
     * Apply UI permissions to a frame.
     *
     * @param container frame
     */
    public static void applyUiPermissions(IFrame container) {
        Window window = container instanceof Window ? (Window) container : ComponentsHelper.getWindow(container);
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();

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
                if (customComponent instanceof TabSheet) {
                    final TabSheet tabsheet = (TabSheet) customComponent;
                    final TabSheet.Tab tab = tabsheet.getTab(subComponentId);
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
                    final FieldGroup.FieldConfig field = fieldGroup.getField(subComponentId);
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

    /**
     * Deploy views defined in <code>metadataContext</code> of a frame.
     * @param rootElement  root element of a frame XML
     */
    public static void deployViews(Element rootElement) {
        Element metadataContextEl = rootElement.element("metadataContext");
        if (metadataContextEl != null) {
            AbstractViewRepository viewRepository = (AbstractViewRepository) AppBeans.get(ViewRepository.NAME);
            for (Element fileEl : Dom4j.elements(metadataContextEl, "deployViews")) {
                String resource = fileEl.attributeValue("name");
                InputStream resourceInputStream = AppBeans.get(Resources.class).getResourceAsStream(resource);
                if (resourceInputStream == null)
                    throw new RuntimeException("View resource not found: " + resource);

                try {
                    viewRepository.deployViews(resourceInputStream);
                } finally {
                    IOUtils.closeQuietly(resourceInputStream);
                }
            }

            for (Element viewEl : Dom4j.elements(metadataContextEl, "view")) {
                viewRepository.deployView(metadataContextEl, viewEl);
            }
        }
    }
}