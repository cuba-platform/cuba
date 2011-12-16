/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.google.common.base.Predicate;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.security.EntityPermissionTargetsDatasource;
import com.haulmont.cuba.gui.security.RestorablePermissionDatasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.ui.OperationPermissionTarget;
import com.haulmont.cuba.security.entity.ui.PermissionVariant;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
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
    private RestorablePermissionDatasource entityPermissionsDs;

    @Inject
    private EntityPermissionTargetsDatasource entityTargetsDs;

    @Inject
    private Table entityPermissionsTable;

    /* Filter */

    @Inject
    private TextField entityFilter;

    @Inject
    private CheckBox assignedOnlyCheckBox;

    /* Buttons */

    @Inject
    private Button applyFilterBtn;

    @Inject
    private Button applyPermissionMaskBtn;

    /* Panels */

    @Inject
    private BoxLayout applyPermissionPane;

    @Inject
    private BoxLayout selectedEntityPanel;

    /* Checkboxes */

    @Inject
    private CheckBox allAllowCheck;

    @Inject
    private CheckBox allDenyCheck;

    /* Checkbox operations controls */

    private class EntityOperationControl {

        private CheckBox allowChecker;

        private CheckBox denyChecker;

        private String metaProperty;

        private EntityOp operation;

        private EntityOperationControl(EntityOp operation, String metaProperty, String allowChecker, String denyChecker) {
            this.operation = operation;
            this.metaProperty = metaProperty;
            this.allowChecker = getComponent(allowChecker);
            this.denyChecker = getComponent(denyChecker);
        }

        public CheckBox getAllowChecker() {
            return allowChecker;
        }

        public CheckBox getDenyChecker() {
            return denyChecker;
        }

        public String getMetaProperty() {
            return metaProperty;
        }

        public EntityOp getOperation() {
            return operation;
        }
    }

    private EntityOperationControl[] operationControls;

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

        attachAllCheckBoxListener(allAllowCheck, PermissionVariant.ALLOWED);
        attachAllCheckBoxListener(allDenyCheck, PermissionVariant.DISALLOWED);

        operationControls = new EntityOperationControl[]{
                new EntityOperationControl(EntityOp.CREATE, "createPermissionVariant", "createAllowCheck", "createDenyCheck"),
                new EntityOperationControl(EntityOp.READ, "readPermissionVariant", "readAllowCheck", "readDenyCheck"),
                new EntityOperationControl(EntityOp.UPDATE, "updatePermissionVariant", "updateAllowCheck", "updateDenyCheck"),
                new EntityOperationControl(EntityOp.DELETE, "deletePermissionVariant", "deleteAllowCheck", "deleteDenyCheck")
        };

        for (EntityOperationControl control : operationControls) {
            attachCheckBoxListener(control.getAllowChecker(),
                    control.getMetaProperty(), control.getOperation(), PermissionVariant.ALLOWED);
            attachCheckBoxListener(control.getDenyChecker(),
                    control.getMetaProperty(), control.getOperation(), PermissionVariant.DISALLOWED);
        }

        entityTargetsDs.addListener(new CollectionDsListenerAdapter<OperationPermissionTarget>() {
            @Override
            public void itemChanged(Datasource<OperationPermissionTarget> ds,
                                    OperationPermissionTarget prevItem, OperationPermissionTarget item) {
                if (!selectedEntityPanel.isVisible() && (item != null))
                    selectedEntityPanel.setVisible(true);
                if (selectedEntityPanel.isVisible() && (item == null))
                    selectedEntityPanel.setVisible(false);

                Set selected = entityPermissionsTable.getSelected();
                if (!selected.isEmpty() && (selected.size() > 1))
                    applyPermissionPane.setVisible(true);
                else
                    applyPermissionPane.setVisible(false);

                updateCheckBoxes(item);
            }

            @Override
            public void valueChanged(OperationPermissionTarget source, String property, Object prevValue, Object value) {
                if (isSingleSelection()) {
                    if ("createPermissionVariant".equals(property) ||
                            "readPermissionVariant".equals(property) ||
                            "updatePermissionVariant".equals(property) ||
                            "deletePermissionVariant".equals(property))
                        updateCheckBoxes(source);
                }
            }
        });

        applyPermissionMaskBtn.setAction(new AbstractAction("action.apply") {
            @Override
            public void actionPerform(Component component) {
                Set selected = entityPermissionsTable.getSelected();
                if (!selected.isEmpty() && (selected.size() > 1)) {
                    for (Object obj : selected) {
                        OperationPermissionTarget target = (OperationPermissionTarget) obj;
                        for (EntityOperationControl control : operationControls) {
                            PermissionVariant variant;

                            if (Boolean.TRUE.equals(control.getAllowChecker().getValue())) {
                                variant = PermissionVariant.ALLOWED;
                            } else if (Boolean.TRUE.equals(control.getDenyChecker().getValue())) {
                                variant = PermissionVariant.DISALLOWED;
                            } else {
                                variant = PermissionVariant.NOTSET;
                            }

                            markTargetPermission(target, control.getMetaProperty(), control.getOperation(), variant);
                        }
                    }
                    showNotification(getMessage("notification.applied"), NotificationType.HUMANIZED);
                }
            }
        });

        entityPermissionsDs.refresh();
        entityTargetsDs.refresh();
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

        if (isSingleSelection()) {
            if (item != null) {
                for (EntityOperationControl control : operationControls) {
                    updateCheckBoxes(item.<PermissionVariant>getValue(control.getMetaProperty()),
                            control.getAllowChecker(), control.getDenyChecker());
                }

                allAllowCheck.setValue(item.isAllowedAll());
                allDenyCheck.setValue(item.isDeniedAll());
            } else {
                deselectAllCheckers();
            }
        } else {
            deselectAllCheckers();
        }

        itemChanging = false;
    }

    private void deselectAllCheckers() {
        for (EntityOperationControl control : operationControls) {
            control.getAllowChecker().setValue(false);
            control.getDenyChecker().setValue(false);
        }
    }

    private boolean isSingleSelection() {
        return entityPermissionsTable.getSelected().size() == 1;
    }

    private EntityOperationControl getOperationControl(CheckBox checkBox) {
        for (EntityOperationControl control : operationControls) {
            if ((control.getAllowChecker() == checkBox) || (control.getDenyChecker() == checkBox))
                return control;
        }
        return null;
    }

    private void attachAllCheckBoxListener(CheckBox checkBox, final PermissionVariant activeVariant) {
        checkBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (itemChanging)
                    return;

                if (entityPermissionsTable.getSelected().isEmpty())
                    return;

                itemChanging = true;

                PermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(value, activeVariant);

                if (isSingleSelection()) {
                    markItemPermission("createPermissionVariant", EntityOp.CREATE, permissionVariant);
                    markItemPermission("readPermissionVariant", EntityOp.READ, permissionVariant);
                    markItemPermission("updatePermissionVariant", EntityOp.UPDATE, permissionVariant);
                    markItemPermission("deletePermissionVariant", EntityOp.DELETE, permissionVariant);
                } else {
                    for (EntityOperationControl control : operationControls) {
                        control.getAllowChecker().setValue(permissionVariant == PermissionVariant.ALLOWED);
                        control.getDenyChecker().setValue(permissionVariant == PermissionVariant.DISALLOWED);
                    }

                    allAllowCheck.setValue(permissionVariant == PermissionVariant.ALLOWED);
                    allDenyCheck.setValue(permissionVariant == PermissionVariant.DISALLOWED);
                }

                itemChanging = false;
            }
        });
    }

    private void attachCheckBoxListener(final CheckBox checkBox, final String metaProperty,
                                        final EntityOp operation, final PermissionVariant activeVariant) {

        checkBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (itemChanging)
                    return;

                if (entityPermissionsTable.getSelected().isEmpty())
                    return;

                itemChanging = true;

                if (isSingleSelection()) {
                    markItemPermission(metaProperty, operation,
                            PermissionUiHelper.getCheckBoxVariant(value, activeVariant));
                } else {
                    EntityOperationControl checkerControl = getOperationControl(checkBox);

                    if (activeVariant == PermissionVariant.ALLOWED) {
                        checkerControl.getDenyChecker().setValue(false);
                    } else if (activeVariant == PermissionVariant.DISALLOWED) {
                        checkerControl.getAllowChecker().setValue(false);
                    }

                    boolean isAllowedAll = true;
                    boolean isDenyAll = true;

                    for (EntityOperationControl control : operationControls) {
                        isAllowedAll &= Boolean.TRUE.equals(control.getAllowChecker().getValue());
                        isDenyAll &= Boolean.TRUE.equals(control.getDenyChecker().getValue());
                    }

                    allAllowCheck.setValue(isAllowedAll);
                    allDenyCheck.setValue(isDenyAll);
                }

                itemChanging = false;
            }
        });
    }

    private void markItemPermission(String property, EntityOp operation, PermissionVariant permissionVariant) {
        OperationPermissionTarget target = entityPermissionsTable.getSingleSelected();
        markTargetPermission(target, property, operation, permissionVariant);
    }

    private void markTargetPermission(OperationPermissionTarget target,
                                      String property, EntityOp operation, PermissionVariant permissionVariant) {
        if (target != null) {
            target.setValue(property, permissionVariant);
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
