/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.global.Metadata;
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
import com.haulmont.cuba.gui.security.SpecificPermissionTreeDatasource;
import com.haulmont.cuba.gui.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;
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
    private Datasource<Role> roleDs;

    @Inject
    private CollectionDatasource<Permission, UUID> specificPermissionsDs;

    @Inject
    private TreeTable specificPermissionsTree;

    @Inject
    private SpecificPermissionTreeDatasource specificPermissionsTreeDs;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    private BoxLayout selectedPermissionPanel;

    @Inject
    private CheckBox allowCheckBox;

    @Inject
    private CheckBox disallowCheckBox;

    private boolean itemChanging = false;

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

        boolean hasPermissionsToCreatePermission = userSession.isEntityOpPermitted(
                metadata.getSession().getClass(Permission.class), EntityOp.CREATE);

        setEditable(hasPermissionsToCreatePermission);
    }

    private void setEditable(boolean editable) {
        allowCheckBox.setEditable(editable);
        disallowCheckBox.setEditable(editable);
    }

    private void markItemPermission(PermissionVariant permissionVariant) {
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
                if (permission != null)
                    specificPermissionsDs.removeItem(permission);
            }
            specificPermissionsTreeDs.updateItem(target);
        }
    }
}
