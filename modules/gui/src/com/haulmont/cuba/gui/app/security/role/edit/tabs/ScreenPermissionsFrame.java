/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.app.security.role.edit.BasicPermissionTreeStyleProvider;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.security.ScreenPermissionTreeDatasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.gui.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class ScreenPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionColoredColumns(TreeTable screenPermissionsTree);
    }

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private CollectionDatasource<Permission, UUID> screenPermissionsDs;

    @Inject
    private TreeTable screenPermissionsTree;

    @Inject
    private ScreenPermissionTreeDatasource screenPermissionsTreeDs;

    @Inject
    private BoxLayout selectedScreenPanel;

    @Inject
    private CheckBox allowCheckBox;

    @Inject
    private CheckBox disallowCheckBox;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    private boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        screenPermissionsTree.setStyleProvider(new BasicPermissionTreeStyleProvider());

        screenPermissionsTree.addAction(new AbstractAction("actions.Allow") {
            @Override
            public void actionPerform(Component component) {
                markItemPermission(PermissionVariant.ALLOWED);
            }
        });
        screenPermissionsTree.addAction(new AbstractAction("actions.Disallow") {
            @Override
            public void actionPerform(Component component) {
                markItemPermission(PermissionVariant.DISALLOWED);
            }
        });
        screenPermissionsTree.addAction(new AbstractAction("actions.DropRule") {
            @Override
            public void actionPerform(Component component) {
                markItemPermission(PermissionVariant.NOTSET);
            }
        });

        Companion companion = getCompanion();
        companion.initPermissionColoredColumns(screenPermissionsTree);

        screenPermissionsTreeDs.addListener(new CollectionDsListenerAdapter<BasicPermissionTarget>() {
            @Override
            public void itemChanged(Datasource<BasicPermissionTarget> ds,
                                    BasicPermissionTarget prevItem, BasicPermissionTarget item) {
                if (!selectedScreenPanel.isVisible() && (item != null))
                    selectedScreenPanel.setVisible(true);
                if (selectedScreenPanel.isVisible() && (item == null))
                    selectedScreenPanel.setVisible(false);

                updateCheckBoxes(item);
            }

            @Override
            public void valueChanged(BasicPermissionTarget source,
                                     String property, Object prevValue, Object value) {
                if ("permissionVariant".equals(property))
                    updateCheckBoxes(source);
            }

            private void updateCheckBoxes(BasicPermissionTarget item) {
                itemChanging = true;
                if (item != null) {
                    boolean visible = !item.getId().startsWith("root:");
                    allowCheckBox.setVisible(visible);
                    disallowCheckBox.setVisible(visible);

                    if (item.getPermissionVariant() == PermissionVariant.ALLOWED) {
                        allowCheckBox.setValue(true);
                        disallowCheckBox.setValue(false);
                    } else if (item.getPermissionVariant() == PermissionVariant.DISALLOWED) {
                        disallowCheckBox.setValue(true);
                        allowCheckBox.setValue(false);
                    } else {
                        allowCheckBox.setValue(false);
                        disallowCheckBox.setValue(false);
                    }
                } else {
                    allowCheckBox.setValue(false);
                    disallowCheckBox.setValue(false);
                }
                itemChanging = false;
            }
        });

        allowCheckBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (!itemChanging) {
                    itemChanging = true;

                    markItemPermission(PermissionUiHelper.getCheckBoxVariant(value, PermissionVariant.ALLOWED));

                    itemChanging = false;
                }
            }
        });

        disallowCheckBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (!itemChanging) {
                    itemChanging = true;
//
                    markItemPermission(PermissionUiHelper.getCheckBoxVariant(value, PermissionVariant.DISALLOWED));

                    itemChanging = false;
                }
            }
        });

        boolean hasPermissionsToCreatePermission = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);

        allowCheckBox.setEditable(hasPermissionsToCreatePermission);
        disallowCheckBox.setEditable(hasPermissionsToCreatePermission);
    }

    public void loadPermissions() {
        screenPermissionsDs.refresh();
        screenPermissionsTreeDs.setPermissionDs(screenPermissionsDs);
        screenPermissionsTree.refresh();
        screenPermissionsTree.expandAll();
        screenPermissionsTree.collapse("root:others");
    }

    public void setEditable(boolean editable) {
        allowCheckBox.setEditable(editable);
        disallowCheckBox.setEditable(editable);
    }

    private void markItemPermission(PermissionVariant permissionVariant) {
        BasicPermissionTarget target = screenPermissionsTree.getSingleSelected();
        if (target != null) {
            target.setPermissionVariant(permissionVariant);
            if (permissionVariant != PermissionVariant.NOTSET) {
                // Create permission
                int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                PermissionUiHelper.createPermissionItem(screenPermissionsDs, roleDs,
                        target.getPermissionValue(), PermissionType.SCREEN, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (Permission p : screenPermissionsDs.getItems()) {
                    if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
                        permission = p;
                        break;
                    }
                }
                if (permission != null)
                    screenPermissionsDs.removeItem(permission);
            }
            screenPermissionsTreeDs.updateItem(target);
        }
    }
}