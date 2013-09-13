/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.security.RestorablePermissionDatasource;
import com.haulmont.cuba.gui.security.UiPermissionsDatasource;
import com.haulmont.cuba.gui.security.entity.UiPermissionTarget;
import com.haulmont.cuba.gui.security.entity.UiPermissionVariant;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class UiPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionsColoredColumns(Table uiPermissionsTable);
    }

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private LookupField screenFilter;

    @Inject
    private TextField componentTextField;

    @Inject
    private RestorablePermissionDatasource uiPermissionsDs;

    @Inject
    private UiPermissionsDatasource uiPermissionTargetsDs;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    private BoxLayout selectedComponentPanel;

    @Inject
    private CheckBox readOnlyCheckBox;

    @Inject
    private CheckBox hideCheckBox;

    @Inject
    private CheckBox showCheckBox;

    @Inject
    private GroupTable uiPermissionsTable;

    private boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
        Collection<WindowInfo> windows = windowConfig.getWindows();
        Map<String, Object> screens = new LinkedHashMap<>();
        for (WindowInfo windowInfo : windows) {
            String id = windowInfo.getId();
            String menuId = "menu-config." + id;
            String localeMsg = messages.getMessage(AppConfig.getMessagesPack(), menuId);
            String title = menuId.equals(localeMsg) ? id : id + " ( " + localeMsg + " )";
            screens.put(title, id);
        }
        screenFilter.setOptionsMap(screens);

        Companion companion = getCompanion();
        companion.initPermissionsColoredColumns(uiPermissionsTable);

        uiPermissionTargetsDs.addListener(new CollectionDsListenerAdapter<UiPermissionTarget>() {
            @Override
            public void itemChanged(Datasource<UiPermissionTarget> ds, UiPermissionTarget prevItem, UiPermissionTarget item) {
                if (!selectedComponentPanel.isVisible() && (item != null))
                    selectedComponentPanel.setVisible(true);
                if (selectedComponentPanel.isVisible() && (item == null))
                    selectedComponentPanel.setVisible(false);

                updateCheckBoxes(item);
            }

            @Override
            public void valueChanged(UiPermissionTarget source, String property, Object prevValue, Object value) {
                if ("permissionVariant".equals(property)) {
                    updateCheckBoxes(uiPermissionsTable.<UiPermissionTarget>getSingleSelected());
                }
            }
        });

        attachCheckBoxListener(readOnlyCheckBox, UiPermissionVariant.READ_ONLY);
        attachCheckBoxListener(hideCheckBox, UiPermissionVariant.HIDE);
        attachCheckBoxListener(showCheckBox, UiPermissionVariant.SHOW);

        uiPermissionTargetsDs.setPermissionDs(uiPermissionsDs);

        uiPermissionsDs.refresh();
        uiPermissionTargetsDs.refresh();


        boolean hasPermissionsToCreatePermission = userSession.isEntityOpPermitted(
                metadata.getSession().getClass(Permission.class), EntityOp.CREATE);

        setEditable(hasPermissionsToCreatePermission);
    }

    private void setEditable(boolean editable) {
        hideCheckBox.setEditable(editable);
        showCheckBox.setEditable(editable);
        readOnlyCheckBox.setEditable(editable);
        Button addPermissionBtn = getComponent("addPermissionBtn");
        if (addPermissionBtn != null) {
            addPermissionBtn.setEnabled(editable);
        }
    }

    private void attachCheckBoxListener(CheckBox checkBox, final UiPermissionVariant activeVariant) {
        checkBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (itemChanging)
                    return;

                if (uiPermissionsTable.getSelected().isEmpty())
                    return;

                itemChanging = true;

                UiPermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(value, activeVariant);
                UiPermissionTarget target = uiPermissionsTable.getSingleSelected();
                markItemPermission(permissionVariant, target);

                uiPermissionTargetsDs.updateItem(target);

                itemChanging = false;
            }
        });
    }

    private void markItemPermission(UiPermissionVariant permissionVariant,
                                    UiPermissionTarget target) {
        if (target != null) {
            target.setPermissionVariant(permissionVariant);
            if (permissionVariant != UiPermissionVariant.NOTSET) {
                // Create permission
                int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                PermissionUiHelper.createPermissionItem(uiPermissionsDs, roleDs,
                        target.getPermissionValue(), PermissionType.UI, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (Permission p : uiPermissionsDs.getItems()) {
                    if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
                        permission = p;
                        break;
                    }
                }

                if (permission != null)
                    uiPermissionsDs.removeItem(permission);
            }
        }
    }

    private void updateCheckBoxes(UiPermissionTarget item) {
        itemChanging = true;

        if (item != null) {
            readOnlyCheckBox.setValue(item.getPermissionVariant() == UiPermissionVariant.READ_ONLY);
            hideCheckBox.setValue(item.getPermissionVariant() == UiPermissionVariant.HIDE);
            showCheckBox.setValue(item.getPermissionVariant() == UiPermissionVariant.SHOW);
        }

        itemChanging = false;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void addUiPermission() {
        String screen = screenFilter.getValue();
        String component = componentTextField.getValue();
        if (StringUtils.isNotBlank(screen) && StringUtils.isNotBlank(component)) {
            UiPermissionTarget target = new UiPermissionTarget("ui:" + screen + ":" + component,
                    screen + ":" + component, screen + ":" + component, UiPermissionVariant.NOTSET);
            target.setScreen(screen);
            target.setComponent(component);
            uiPermissionTargetsDs.addItem(target);
        }
    }
}