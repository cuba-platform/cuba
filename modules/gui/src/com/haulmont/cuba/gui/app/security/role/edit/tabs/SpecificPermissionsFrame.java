/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.app.security.role.edit.BasicPermissionTreeStyleProvider;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.app.security.ds.SpecificPermissionTreeDatasource;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
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
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class SpecificPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionColoredColumns(TreeTable specificPermissionsTree);
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

    protected boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        specificPermissionsTree.setStyleProvider(new BasicPermissionTreeStyleProvider());

        Companion companion = getCompanion();
        companion.initPermissionColoredColumns(specificPermissionsTree);

        specificPermissionsTreeDs.addListener(new CollectionDsListenerAdapter<BasicPermissionTarget>() {
            @Override
            public void itemChanged(Datasource<BasicPermissionTarget> ds,
                                    BasicPermissionTarget prevItem, BasicPermissionTarget item) {
                if (!selectedPermissionPanel.isVisible() && (item != null)) {
                    selectedPermissionPanel.setVisible(!item.getId().startsWith("category:"));
                }
                if (selectedPermissionPanel.isVisible() && (item == null))
                    selectedPermissionPanel.setVisible(false);

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

                    markItemPermission(PermissionUiHelper.getCheckBoxVariant(value, PermissionVariant.DISALLOWED));

                    itemChanging = false;
                }
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

    protected void applyPermissions(boolean editable) {
        if (!editable) {
            allowCheckBox.setEditable(editable);
            disallowCheckBox.setEditable(editable);
        }
    }

    protected void markItemPermission(PermissionVariant permissionVariant) {
        BasicPermissionTarget target = specificPermissionsTree.getSingleSelected();
        if (target != null) {
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
