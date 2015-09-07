/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.app.security.ds.EntityPermissionTargetsDatasource;
import com.haulmont.cuba.gui.app.security.ds.RestorablePermissionDatasource;
import com.haulmont.cuba.gui.app.security.entity.OperationPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntityPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionColoredColumns(Table entityPermissionsTable);
        void initTextFieldFilter(TextField entityFilter, Runnable runnable);
    }

    @Inject
    protected Companion companion;

    @Inject
    protected Datasource<Role> roleDs;

    @Inject
    protected RestorablePermissionDatasource entityPermissionsDs;

    @Inject
    protected EntityPermissionTargetsDatasource entityTargetsDs;

    @Inject
    protected Table<OperationPermissionTarget> entityPermissionsTable;

    @Inject
    protected Label selectedTargetCaption;

    @Inject
    protected Label selectedTargetLocalCaption;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    /* Filter */

    @Inject
    protected TextField entityFilter;

    @Inject
    protected CheckBox assignedOnlyCheckBox;

    @Inject
    protected CheckBox systemLevelCheckBox;

    /* Panels */

    @Inject
    protected BoxLayout applyPermissionPane;

    @Inject
    protected BoxLayout selectedEntityPanel;

    /* Checkboxes */

    @Inject
    protected CheckBox allAllowCheck;

    @Inject
    protected CheckBox allDenyCheck;

    /* Checkbox operations controls */

    protected class EntityOperationControl {

        protected CheckBox allowChecker;

        protected CheckBox denyChecker;

        protected Label operationLabel;

        protected String metaProperty;

        protected EntityOp operation;

        protected boolean controlVisible = true;

        public EntityOperationControl(EntityOp operation, String metaProperty, String operationLabel,
                                       String allowChecker, String denyChecker) {
            this.operation = operation;
            this.metaProperty = metaProperty;
            this.operationLabel = (Label) getComponent(operationLabel);
            this.allowChecker = (CheckBox) getComponent(allowChecker);
            this.denyChecker = (CheckBox) getComponent(denyChecker);
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

        public void setControlVisible(boolean visible) {
            controlVisible = visible;
            operationLabel.setVisible(visible);
            allowChecker.setVisible(visible);
            denyChecker.setVisible(visible);
        }

        public boolean isControlVisible() {
            return controlVisible;
        }

        public void show() {
            setControlVisible(true);
        }

        public void hide() {
            setControlVisible(false);
        }

        public boolean applicableToEntity(Class javaClass) {
            return true;
        }
    }

    protected EntityOperationControl[] operationControls;

    protected boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        assignedOnlyCheckBox.setValue(Boolean.TRUE);
        systemLevelCheckBox.setValue(Boolean.FALSE);

        entityTargetsDs.setPermissionDs(entityPermissionsDs);
        entityTargetsDs.setFilter(new EntityNameFilter<>(
                metadata, assignedOnlyCheckBox, systemLevelCheckBox, entityFilter));

        initCheckBoxesControls();

        entityPermissionsDs.refresh();

        companion.initPermissionColoredColumns(entityPermissionsTable);
        companion.initTextFieldFilter(entityFilter, this::applyFilter);

        entityTargetsDs.addItemChangeListener(e -> {
            if (!selectedEntityPanel.isVisible() && (e.getItem() != null)) {
                selectedEntityPanel.setVisible(true);
            }
            if (selectedEntityPanel.isVisible() && (e.getItem() == null)) {
                selectedEntityPanel.setVisible(false);
            }

            Set selected = entityPermissionsTable.getSelected();
            if (!selected.isEmpty() && (selected.size() > 1)) {
                applyPermissionPane.setVisible(true);
            } else {
                applyPermissionPane.setVisible(false);
            }

            updateEditPane(e.getItem(), selected);

            updateCheckBoxes(e.getItem());
        });

        entityTargetsDs.addItemPropertyChangeListener(e -> {
            if (isSingleSelection()) {
                if ("createPermissionVariant".equals(e.getProperty()) ||
                        "readPermissionVariant".equals(e.getProperty()) ||
                        "updatePermissionVariant".equals(e.getProperty()) ||
                        "deletePermissionVariant".equals(e.getProperty()))
                    updateCheckBoxes(e.getItem());
            }
        });

        entityTargetsDs.refresh();

        boolean isCreatePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);
        boolean isDeletePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.DELETE);
        boolean hasPermissionsToModifyePermission = isCreatePermitted && isDeletePermitted;

        applyPermissions(hasPermissionsToModifyePermission);
    }

    @SuppressWarnings("unused")
    public void applyFilter() {
        entityTargetsDs.refresh();
        if (entityTargetsDs.getItemIds().isEmpty()) {
            String message;
            Object value = entityFilter.getValue();
            if (Boolean.TRUE.equals(assignedOnlyCheckBox.getValue()))
                message = String.format(getMessage("noAssignedItemsForFilter"), value != null ? value : " ");
            else
                message = String.format(getMessage("noItemsForFilter"), value != null ? value : " ");
            showNotification(message, NotificationType.HUMANIZED);
        }
    }

    @SuppressWarnings("unused")
    public void applyPermissionMask() {
        Set<OperationPermissionTarget> selected = entityPermissionsTable.getSelected();
        if (!selected.isEmpty() && (selected.size() > 1)) {
            for (OperationPermissionTarget target : selected) {
                for (EntityOperationControl control : operationControls) {
                    if (control.isControlVisible() && control.applicableToEntity(target.getEntityClass())) {
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
                entityTargetsDs.updateItem(target);
            }
            showNotification(getMessage("notification.applied"), NotificationType.HUMANIZED);
        }
    }

    protected void applyPermissions(boolean editable) {
        allAllowCheck.setEditable(editable);
        allDenyCheck.setEditable(editable);
        for(EntityOperationControl entityOperationControl : operationControls) {
            entityOperationControl.getAllowChecker().setEditable(editable);
            entityOperationControl.getDenyChecker().setEditable(editable);
        }
    }

    protected void initCheckBoxesControls() {
        operationControls = new EntityOperationControl[]{
                new EntityOperationControl(EntityOp.CREATE, "createPermissionVariant", "createOpLabel",
                        "createAllowCheck", "createDenyCheck") {
                    @Override
                    public boolean applicableToEntity(Class javaClass) {
                        return javaClass.isAnnotationPresent(javax.persistence.Entity.class);
                    }
                },
                new EntityOperationControl(EntityOp.READ, "readPermissionVariant", "readOpLabel",
                        "readAllowCheck", "readDenyCheck"),
                new EntityOperationControl(EntityOp.UPDATE, "updatePermissionVariant", "updateOpLabel",
                        "updateAllowCheck", "updateDenyCheck"),
                new EntityOperationControl(EntityOp.DELETE, "deletePermissionVariant", "deleteOpLabel",
                        "deleteAllowCheck", "deleteDenyCheck") {
                    @Override
                    public boolean applicableToEntity(Class javaClass) {
                        return javaClass.isAnnotationPresent(javax.persistence.Entity.class);
                    }
                }
        };

        attachAllCheckBoxListener(allAllowCheck, PermissionVariant.ALLOWED);
        attachAllCheckBoxListener(allDenyCheck, PermissionVariant.DISALLOWED);

        for (EntityOperationControl control : operationControls) {
            // Allow checkbox
            attachCheckBoxListener(control.getAllowChecker(), control.getMetaProperty(), control.getOperation(),
                    PermissionVariant.ALLOWED);
            // Deny checkbox
            attachCheckBoxListener(control.getDenyChecker(), control.getMetaProperty(), control.getOperation(),
                    PermissionVariant.DISALLOWED);
        }
    }

    /**
     * Update edit controls visibility
     *
     * @param item     Target ds item
     * @param selected Selected set in table
     */
    protected void updateEditPane(OperationPermissionTarget item, Set selected) {
        if (item != null) {
            if (selected.size() == 1) {
                String caption = item.getCaption();

                String name;
                String localName;

                int delimiterIndex = caption.lastIndexOf(" ");
                if (delimiterIndex >= 0) {
                    localName = caption.substring(0, delimiterIndex);
                    name = caption.substring(delimiterIndex + 1);
                } else {
                    name = caption;
                    localName = "";
                }

                selectedTargetCaption.setVisible(true);
                selectedTargetCaption.setValue(name);

                selectedTargetLocalCaption.setVisible(true);
                selectedTargetLocalCaption.setValue(localName);

                // check compatibility, hide not applicable operations
                for (EntityOperationControl control : operationControls) {
                    if (control.applicableToEntity(item.getEntityClass())) {
                        control.show();
                    } else {
                        control.hide();
                    }
                }

            } else if (selected.size() > 1) {
                selectedTargetCaption.setVisible(false);
                selectedTargetCaption.setValue("");

                selectedTargetLocalCaption.setVisible(false);
                selectedTargetLocalCaption.setValue("");

                // show all
                for (EntityOperationControl control : operationControls)
                    control.show();

                // exclude not applicable operations
                for (Object obj : selected) {
                    OperationPermissionTarget target = (OperationPermissionTarget) obj;
                    for (EntityOperationControl control : operationControls) {
                        if (control.isControlVisible() && !control.applicableToEntity(target.getEntityClass()))
                            control.hide();
                    }
                }
            }
        }
    }

    protected void updateCheckBoxes(PermissionVariant permissionVariant,
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

    protected void updateCheckBoxes(OperationPermissionTarget item) {

        itemChanging = true;

        if (isSingleSelection()) {
            if (item != null) {
                for (EntityOperationControl control : operationControls) {
                    updateCheckBoxes(item.<PermissionVariant>getValue(control.getMetaProperty()),
                            control.getAllowChecker(), control.getDenyChecker());
                }

                boolean isAllowedAll = true;
                boolean isDenyAll = true;

                for (EntityOperationControl control : operationControls) {
                    if (control.isControlVisible()) {
                        isAllowedAll &= Boolean.TRUE.equals(control.getAllowChecker().getValue());
                        isDenyAll &= Boolean.TRUE.equals(control.getDenyChecker().getValue());
                    }
                }

                allAllowCheck.setValue(isAllowedAll);
                allDenyCheck.setValue(isDenyAll);
            } else {
                deselectAllCheckers();
            }
        } else {
            deselectAllCheckers();
        }

        itemChanging = false;
    }

    protected void deselectAllCheckers() {
        for (EntityOperationControl control : operationControls) {
            control.getAllowChecker().setValue(false);
            control.getDenyChecker().setValue(false);
        }

        allAllowCheck.setValue(false);
        allDenyCheck.setValue(false);
    }

    protected boolean isSingleSelection() {
        return entityPermissionsTable.getSelected().size() == 1;
    }

    protected EntityOperationControl getOperationControl(CheckBox checkBox) {
        for (EntityOperationControl control : operationControls) {
            if ((control.getAllowChecker() == checkBox) || (control.getDenyChecker() == checkBox))
                return control;
        }
        return null;
    }

    protected void attachAllCheckBoxListener(CheckBox checkBox, final PermissionVariant activeVariant) {
        checkBox.addValueChangeListener(e -> {
            if (itemChanging)
                return;

            if (entityPermissionsTable.getSelected().isEmpty())
                return;

            itemChanging = true;

            PermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(e.getValue(), activeVariant);

            if (isSingleSelection()) {
                for (EntityOperationControl control : operationControls) {
                    OperationPermissionTarget target = entityPermissionsTable.getSingleSelected();
                    //noinspection ConstantConditions
                    if (control.applicableToEntity(target.getEntityClass())) {
                        markItemPermission(control.getMetaProperty(), control.getOperation(), permissionVariant);
                    }
                }
            } else {
                for (EntityOperationControl control : operationControls) {
                    control.getAllowChecker().setValue(permissionVariant == PermissionVariant.ALLOWED);
                    control.getDenyChecker().setValue(permissionVariant == PermissionVariant.DISALLOWED);
                }

                allAllowCheck.setValue(permissionVariant == PermissionVariant.ALLOWED);
                allDenyCheck.setValue(permissionVariant == PermissionVariant.DISALLOWED);
            }

            entityPermissionsTable.repaint();

            itemChanging = false;
        });
    }

    protected void attachCheckBoxListener(final CheckBox checkBox, final String metaProperty,
                                        final EntityOp operation, final PermissionVariant activeVariant) {
        checkBox.addValueChangeListener(e -> {
            if (itemChanging)
                return;

            if (entityPermissionsTable.getSelected().isEmpty())
                return;

            itemChanging = true;

            if (isSingleSelection()) {
                markItemPermission(metaProperty, operation,
                        PermissionUiHelper.getCheckBoxVariant(e.getValue(), activeVariant));
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
                    if (control.isControlVisible()) {
                        isAllowedAll &= Boolean.TRUE.equals(control.getAllowChecker().getValue());
                        isDenyAll &= Boolean.TRUE.equals(control.getDenyChecker().getValue());
                    }
                }

                allAllowCheck.setValue(isAllowedAll);
                allDenyCheck.setValue(isDenyAll);
            }

            entityPermissionsTable.repaint();

            itemChanging = false;
        });
    }

    protected void markItemPermission(String property, EntityOp operation, PermissionVariant permissionVariant) {
        OperationPermissionTarget target = entityPermissionsTable.getSingleSelected();
        markTargetPermission(target, property, operation, permissionVariant);
    }

    protected void markTargetPermission(OperationPermissionTarget target,
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
                for (Permission p : entityPermissionsDs.getItems()) {
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