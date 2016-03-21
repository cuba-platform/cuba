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
import com.haulmont.cuba.gui.app.security.ds.SpecificPermissionTreeDatasource;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.BasicPermissionTreeStyleProvider;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class SpecificPermissionsFrame extends AbstractFrame {

    protected static final String CATEGORY_PREFIX = "category:";

    public interface Companion {
        void initPermissionColoredColumns(TreeTable<BasicPermissionTarget> specificPermissionsTree);
    }

    @Inject
    protected Datasource<Role> roleDs;

    @Inject
    protected CollectionDatasource<Permission, UUID> specificPermissionsDs;

    @Inject
    protected TreeTable<BasicPermissionTarget> specificPermissionsTree;

    @Inject
    protected SpecificPermissionTreeDatasource specificPermissionsTreeDs;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Security security;

    @Inject
    protected BoxLayout selectedPermissionPanel;

    @Inject
    protected CheckBox allowCheckBox;

    @Inject
    protected CheckBox disallowCheckBox;

    @Inject
    protected Companion companion;

    protected boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        specificPermissionsTree.setStyleProvider(new BasicPermissionTreeStyleProvider());

        companion.initPermissionColoredColumns(specificPermissionsTree);

        specificPermissionsTreeDs.addItemChangeListener(e -> {
            boolean visible = false;
            if (e.getItem() != null) {
                for (BasicPermissionTarget target : specificPermissionsTree.getSelected()) {
                    visible |= !target.getId().startsWith(CATEGORY_PREFIX);
                }
            }
            selectedPermissionPanel.setVisible(visible);

            updateCheckBoxes(e.getItem());
        });

        specificPermissionsTreeDs.addItemPropertyChangeListener(e -> {
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

        specificPermissionsDs.refresh();
        specificPermissionsTreeDs.setPermissionDs(specificPermissionsDs);
        specificPermissionsTreeDs.refresh();
        specificPermissionsTree.expandAll();

        boolean isCreatePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);
        boolean isDeletePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.DELETE);
        boolean hasPermissionsToModifyPermission = isCreatePermitted && isDeletePermitted;

        applyPermissions(hasPermissionsToModifyPermission);
    }

    protected void updateCheckBoxes(BasicPermissionTarget item) {
        itemChanging = true;
        if (item != null) {
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

    protected void applyPermissions(boolean editable) {
        if (!editable) {
            allowCheckBox.setEditable(false);
            disallowCheckBox.setEditable(false);
        }
    }

    protected void markItemPermission(PermissionVariant permissionVariant) {
        for (BasicPermissionTarget target : specificPermissionsTree.getSelected()) {
            if (!target.getId().startsWith(CATEGORY_PREFIX)) {
                target.setPermissionVariant(permissionVariant);
                if (permissionVariant != PermissionVariant.NOTSET) {
                    // Create permission
                    int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                    PermissionUiHelper.createPermissionItem(specificPermissionsDs, roleDs,
                            target.getPermissionValue(), PermissionType.SPECIFIC, value);
                } else {
                    // Remove permission
                    Permission permission = null;
                    for (Permission p : specificPermissionsDs.getItems()) {
                        if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
                            permission = p;
                            break;
                        }
                    }
                    if (permission != null) {
                        specificPermissionsDs.removeItem(permission);
                    }
                }
                // trigger generated column update
                specificPermissionsTreeDs.updateItem(target);
            }
        }
    }
}