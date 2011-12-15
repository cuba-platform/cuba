/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.google.common.base.Predicate;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.security.EntityPermissionTargetsDatasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.ui.OperationPermissionTarget;
import com.haulmont.cuba.security.ui.PermissionVariant;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityPermissionsFrame extends AbstractFrame {

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private CollectionDatasource<Permission, UUID> entityPermissionsDs;

    @Inject
    private EntityPermissionTargetsDatasource entityTargetsDs;

    @Inject
    private Table entityPermissionsTable;

    @Inject
    private TextField entityFilter;

    @Inject
    private CheckBox assignedOnlyCheckBox;

    @Inject
    private Button applyFilterBtn;

    @Inject
    private BoxLayout selectedEntityPanel;

    /* Checkboxes */

    @Inject
    private CheckBox allAllowCheck;

    @Inject
    private CheckBox allDenyCheck;

    @Inject
    private CheckBox createAllowCheck;

    @Inject
    private CheckBox createDenyCheck;

    @Inject
    private CheckBox readAllowCheck;

    @Inject
    private CheckBox readDenyCheck;

    @Inject
    private CheckBox updateAllowCheck;

    @Inject
    private CheckBox updateDenyCheck;

    @Inject
    private CheckBox deleteAllowCheck;

    @Inject
    private CheckBox deleteDenyCheck;

    private boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        assignedOnlyCheckBox.setValue(Boolean.TRUE);

        entityTargetsDs.setPermissionDs(entityPermissionsDs);
        entityTargetsDs.setFilter(new Predicate<OperationPermissionTarget>() {
            @Override
            public boolean apply(@Nullable OperationPermissionTarget target) {
                if (target != null) {
                    if (Boolean.TRUE.equals(assignedOnlyCheckBox.getValue()) && !target.isAssigned())
                        return false;

                    String filterValue = entityFilter.getValue();
                    if (StringUtils.isNotBlank(filterValue)) {
                        String permissionValue = target.getPermissionValue();
                        int delimeterIndex = target.getPermissionValue().indexOf(Permission.TARGET_PATH_DELIMETER);
                        if (delimeterIndex >= 0)
                            permissionValue = permissionValue.substring(0, delimeterIndex);
                        return StringUtils.containsIgnoreCase(permissionValue, filterValue);
                    } else
                        return true;
                }
                return false;
            }
        });
        applyFilterBtn.setAction(new AbstractAction("action.apply") {
            @Override
            public void actionPerform(Component component) {
                entityTargetsDs.refresh();
            }
        });

        attachCheckBoxListener(createAllowCheck, "createPermissionVariant", EntityOp.CREATE, PermissionVariant.ALLOWED);
        attachCheckBoxListener(createDenyCheck, "createPermissionVariant", EntityOp.CREATE, PermissionVariant.DISALLOWED);

        attachCheckBoxListener(readAllowCheck, "readPermissionVariant", EntityOp.READ, PermissionVariant.ALLOWED);
        attachCheckBoxListener(readDenyCheck, "readPermissionVariant", EntityOp.READ, PermissionVariant.DISALLOWED);

        attachCheckBoxListener(updateAllowCheck, "updatePermissionVariant", EntityOp.UPDATE, PermissionVariant.ALLOWED);
        attachCheckBoxListener(updateDenyCheck, "updatePermissionVariant", EntityOp.UPDATE, PermissionVariant.DISALLOWED);

        attachCheckBoxListener(deleteAllowCheck, "deletePermissionVariant", EntityOp.DELETE, PermissionVariant.ALLOWED);
        attachCheckBoxListener(deleteDenyCheck, "deletePermissionVariant", EntityOp.DELETE, PermissionVariant.DISALLOWED);

        attachAllCheckBoxListener(allAllowCheck, PermissionVariant.ALLOWED);
        attachAllCheckBoxListener(allDenyCheck, PermissionVariant.DISALLOWED);

        entityTargetsDs.addListener(new CollectionDsListenerAdapter<OperationPermissionTarget>() {
            @Override
            public void itemChanged(Datasource<OperationPermissionTarget> ds,
                                    OperationPermissionTarget prevItem, OperationPermissionTarget item) {
                if (!selectedEntityPanel.isVisible() && (item != null))
                    selectedEntityPanel.setVisible(true);
                if (selectedEntityPanel.isVisible() && (item == null))
                    selectedEntityPanel.setVisible(false);

                updateCheckBoxes(item);
            }

            @Override
            public void valueChanged(OperationPermissionTarget source, String property, Object prevValue, Object value) {
                if ("createPermissionVariant".equals(property) ||
                        "readPermissionVariant".equals(property) ||
                        "updatePermissionVariant".equals(property) ||
                        "deletePermissionVariant".equals(property))
                    updateCheckBoxes(source);
            }

            private void updateCheckBoxes(PermissionVariant permissionVariant,
                                          CheckBox allowCheckBox, CheckBox denyCheckBox) {
                if (permissionVariant == PermissionVariant.ALLOWED) {
                    allowCheckBox.setValue(true);
                    denyCheckBox.setValue(false);
                } else if (permissionVariant == PermissionVariant.DISALLOWED) {
                    denyCheckBox.setValue(true);
                    allowCheckBox.setValue(false);
                } else {
                    allowCheckBox.setValue(false);
                    denyCheckBox.setValue(false);
                }
            }

            private void updateCheckBoxes(OperationPermissionTarget item) {
                itemChanging = true;

                if (item != null) {
                    updateCheckBoxes(item.getCreatePermissionVariant(), createAllowCheck, createDenyCheck);
                    updateCheckBoxes(item.getReadPermissionVariant(), readAllowCheck, readDenyCheck);
                    updateCheckBoxes(item.getUpdatePermissionVariant(), updateAllowCheck, updateDenyCheck);
                    updateCheckBoxes(item.getDeletePermissionVariant(), deleteAllowCheck, deleteDenyCheck);

                    allAllowCheck.setValue(item.isAllowedAll());
                    allDenyCheck.setValue(item.isDeniedAll());
                } else {
                    for (CheckBox checkBox : getCheckBoxes())
                        checkBox.setValue(false);
                }

                itemChanging = false;
            }
        });

        entityPermissionsDs.refresh();
        entityTargetsDs.refresh();
    }

    protected CheckBox[] getCheckBoxes() {
        return new CheckBox[]{
                createAllowCheck, createDenyCheck,
                readAllowCheck, readDenyCheck,
                updateAllowCheck, updateDenyCheck,
                deleteAllowCheck, deleteDenyCheck,
                allAllowCheck, allDenyCheck
        };
    }

    private void attachAllCheckBoxListener(CheckBox checkBox, final PermissionVariant activeVariant) {
        checkBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (itemChanging)
                    return;

                itemChanging = true;

                PermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(value, activeVariant);

                markItemPermission("createPermissionVariant", EntityOp.CREATE, permissionVariant);
                markItemPermission("readPermissionVariant", EntityOp.READ, permissionVariant);
                markItemPermission("updatePermissionVariant", EntityOp.UPDATE, permissionVariant);
                markItemPermission("deletePermissionVariant", EntityOp.DELETE, permissionVariant);

                itemChanging = false;
            }
        });
    }

    private void attachCheckBoxListener(CheckBox checkBox, final String metaProperty,
                                        final EntityOp operation, final PermissionVariant activeVariant) {

        checkBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (itemChanging)
                    return;

                itemChanging = true;

                markItemPermission(metaProperty, operation,
                        PermissionUiHelper.getCheckBoxVariant(value, activeVariant));

                itemChanging = false;
            }
        });
    }

    private void markItemPermission(String property, EntityOp operation, PermissionVariant permissionVariant) {
        OperationPermissionTarget target = entityPermissionsTable.getSingleSelected();
        if (target != null) {
            target.setValueEx(property, permissionVariant);
            String permissionValue = target.getPermissionValue() + Permission.TARGET_PATH_DELIMETER + operation.getId();
            if (permissionVariant != PermissionVariant.NOTSET) {
                // Create permission
                int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                PermissionUiHelper.createPermissionItem(entityPermissionsDs, roleDs,
                        permissionValue, PermissionType.ENTITY_OP, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (UUID id : entityPermissionsDs.getItemIds()) {
                    Permission p = entityPermissionsDs.getItem(id);
                    if (ObjectUtils.equals(p.getTarget(), permissionValue)) {
                        permission = p;
                        break;
                    }
                }
                if (permission != null)
                    entityPermissionsDs.removeItem(permission);
            }
        }
    }
}
