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

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.app.security.ds.ScreenPermissionTreeDatasource;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.BasicPermissionTreeStyleProvider;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 */
public class ScreenPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionColoredColumns(TreeTable<BasicPermissionTarget> screenPermissionsTree);
    }

    @Inject
    protected Datasource<Role> roleDs;

    @Inject
    protected CollectionDatasource<Permission, UUID> screenPermissionsDs;

    @Inject
    protected TreeTable<BasicPermissionTarget> screenPermissionsTree;

    @Inject
    protected ScreenPermissionTreeDatasource screenPermissionsTreeDs;

    @Inject
    protected BoxLayout selectedScreenPanel;

    @Inject
    protected CheckBox allowCheckBox;

    @Inject
    protected CheckBox disallowCheckBox;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Companion companion;

    protected boolean itemChanging = false;

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

        companion.initPermissionColoredColumns(screenPermissionsTree);

        screenPermissionsTreeDs.addItemChangeListener(e -> {
            if (!selectedScreenPanel.isVisible() && (e.getItem() != null)) {
                selectedScreenPanel.setVisible(true);
            }
            if (selectedScreenPanel.isVisible() && (e.getItem() == null)) {
                selectedScreenPanel.setVisible(false);
            }

            updateCheckBoxes(e.getItem());
        });

        screenPermissionsTreeDs.addItemPropertyChangeListener(e -> {
            if ("permissionVariant".equals(e.getProperty())) {
                updateCheckBoxes(e.getItem());
            }
        });

        allowCheckBox.addValueChangeListener(e -> {
            if (!itemChanging) {
                itemChanging = true;

                markItemPermission(PermissionUiHelper.getCheckBoxVariant(e.getValue(), PermissionVariant.ALLOWED));

                itemChanging = false;
            }
        });

        disallowCheckBox.addValueChangeListener(e -> {
            if (!itemChanging) {
                itemChanging = true;

                markItemPermission(PermissionUiHelper.getCheckBoxVariant(e.getValue(), PermissionVariant.DISALLOWED));

                itemChanging = false;
            }
        });

        boolean isCreatePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);
        boolean isDeletePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.DELETE);

        boolean hasPermissionsToModifyPermission = isCreatePermitted && isDeletePermitted;

        allowCheckBox.setEditable(hasPermissionsToModifyPermission);
        disallowCheckBox.setEditable(hasPermissionsToModifyPermission);
    }

    protected void updateCheckBoxes(BasicPermissionTarget item) {
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

    protected void markItemPermission(PermissionVariant permissionVariant) {
        for (BasicPermissionTarget target : screenPermissionsTree.getSelected()) {
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
                if (permission != null) {
                    screenPermissionsDs.removeItem(permission);
                }
            }
            // trigger generated column update
            screenPermissionsTreeDs.updateItem(target);
        }
    }
}