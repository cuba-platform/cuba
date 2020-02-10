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
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.RolesService;
import org.apache.commons.lang3.BooleanUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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

    @Inject
    protected GroupBoxLayout specificEditPane;

    @Inject
    protected RolesService rolesService;

    protected int rolesPolicyVersion;

    protected boolean itemChanging = false;

    protected boolean permissionsLoaded;

    @Inject
    protected CheckBox specificWildcardCheckBox;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        permissionsLoaded = BooleanUtils.isTrue((Boolean) params.get("permissionsLoaded"));
        rolesPolicyVersion = rolesService.getRolesPolicyVersion();

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

        specificPermissionsDs.refresh(getParamsForDatasource());
        specificPermissionsTreeDs.setPermissionDs(specificPermissionsDs);
        specificPermissionsTreeDs.refresh();
        specificPermissionsTree.expandAll();

        boolean isCreatePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);
        boolean isDeletePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.DELETE);
        boolean hasPermissionsToModifyPermission = isCreatePermitted && isDeletePermitted;

        applyPermissions(hasPermissionsToModifyPermission);

        specificEditPane.setEnabled(security.isEntityOpPermitted(metadata.getClass(Role.class), EntityOp.UPDATE));

        disallowCheckBox.setVisible(rolesPolicyVersion == 1);

        initWildcardCheckBox();
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
        editable = editable && !roleDs.getItem().isPredefined();

        allowCheckBox.setEditable(editable);
        disallowCheckBox.setEditable(editable);
        specificWildcardCheckBox.setEditable(editable);
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
                        if (Objects.equals(p.getTarget(), target.getPermissionValue())) {
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

    protected Map<String, Object> getParamsForDatasource() {
        Map<String, Object> params = new HashMap<>();

        params.put("role", roleDs.getItem());
        params.put("permissionType", PermissionType.SPECIFIC);
        params.put("permissionsLoaded", permissionsLoaded);

        return params;
    }

    protected void initWildcardCheckBox() {
        if (rolesPolicyVersion == 1) {
            specificWildcardCheckBox.setVisible(false);
            return;
        }
        Permission wildcardPermission = getWildcardPermission();
        if (wildcardPermission != null) {
            specificWildcardCheckBox.setValue(true);
        }

        specificWildcardCheckBox.addValueChangeListener(e -> {
            PermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(e.getValue(),
                    PermissionVariant.ALLOWED);
            String permissionTarget = "*";
            if (permissionVariant != PermissionVariant.NOTSET) {
                // Create permission
                int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                PermissionUiHelper.createPermissionItem(specificPermissionsDs, roleDs,
                        permissionTarget, PermissionType.SPECIFIC, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (Permission p : specificPermissionsDs.getItems()) {
                    if (Objects.equals(p.getTarget(), permissionTarget)) {
                        permission = p;
                        break;
                    }
                }
                if (permission != null) {
                    specificPermissionsDs.removeItem(permission);
                }
            }
            updateCheckboxesEnabledByWildcard();
        });

        updateCheckboxesEnabledByWildcard();
    }

    @Nullable
    protected Permission getWildcardPermission() {
        for (Permission p : specificPermissionsDs.getItems()) {
            if (Objects.equals(p.getTarget(), "*")) {
                return p;
            }
        }
        return null;
    }

    protected void updateCheckboxesEnabledByWildcard() {
        Permission wildcardPermission = getWildcardPermission();
        allowCheckBox.setEnabled(wildcardPermission == null);
    }
}