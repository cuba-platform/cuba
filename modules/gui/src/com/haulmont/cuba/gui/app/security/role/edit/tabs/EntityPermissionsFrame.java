/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.global.Metadata;
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
import com.haulmont.cuba.gui.security.entity.OperationPermissionTarget;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntityPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionColoredColumns(Table entityPermissionsTable);
    }

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private RestorablePermissionDatasource entityPermissionsDs;

    @Inject
    private EntityPermissionTargetsDatasource entityTargetsDs;

    @Inject
    private Table entityPermissionsTable;

    @Inject
    private Label selectedTargetCaption;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    /* Filter */

    @Inject
    private TextField entityFilter;

    @Inject
    private CheckBox assignedOnlyCheckBox;

    @Inject
    private CheckBox systemLevelCheckBox;

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

        private Label operationLabel;

        private String metaProperty;

        private EntityOp operation;

        private boolean controlVisible = true;

        private EntityOperationControl(EntityOp operation, String metaProperty, String operationLabel,
                                       String allowChecker, String denyChecker) {
            this.operation = operation;
            this.metaProperty = metaProperty;
            this.operationLabel = getComponent(operationLabel);
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

        public Label getOperationLabel() {
            return operationLabel;
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

    private EntityOperationControl[] operationControls;

    private boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        assignedOnlyCheckBox.setValue(Boolean.TRUE);
        systemLevelCheckBox.setValue(Boolean.FALSE);

        entityTargetsDs.setPermissionDs(entityPermissionsDs);
        entityTargetsDs.setFilter(new EntityNameFilter<OperationPermissionTarget>(
                metadata, assignedOnlyCheckBox, systemLevelCheckBox, entityFilter));

        initCheckBoxesControls();

        entityPermissionsDs.refresh();

        Companion companion = getCompanion();
        companion.initPermissionColoredColumns(entityPermissionsTable);

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

                updateEditPane(item, selected);

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

        entityTargetsDs.refresh();

        boolean hasPermissionsToCreatePermission = userSession.isEntityOpPermitted(
                metadata.getSession().getClass(Permission.class), EntityOp.CREATE);

        setEditable(hasPermissionsToCreatePermission);
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

    private void setEditable(boolean editable) {
        allAllowCheck.setEditable(editable);
        allDenyCheck.setEditable(editable);
        for(EntityOperationControl entityOperationControl : operationControls) {
            entityOperationControl.getAllowChecker().setEditable(editable);
            entityOperationControl.getDenyChecker().setEditable(editable);
        }
    }

    private void initCheckBoxesControls() {
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
                        "updateAllowCheck", "updateDenyCheck") {
                    @Override
                    public boolean applicableToEntity(Class javaClass) {
                        return Updatable.class.isAssignableFrom(javaClass);
                    }
                },
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
    private void updateEditPane(OperationPermissionTarget item, Set selected) {
        if (item != null) {
            if (selected.size() == 1) {
                selectedTargetCaption.setVisible(true);
                selectedTargetCaption.setValue(item.getCaption());

                // check compatibility, hide not applicable operations
                for (EntityOperationControl control : operationControls) {
                    if (control.applicableToEntity(item.getEntityClass()))
                        control.show();
                    else
                        control.hide();
                }

            } else if (selected.size() > 1) {
                selectedTargetCaption.setVisible(false);
                selectedTargetCaption.setValue("");

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

        allAllowCheck.setValue(false);
        allDenyCheck.setValue(false);
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
                    for (EntityOperationControl control : operationControls) {
                        OperationPermissionTarget target = entityPermissionsTable.getSingleSelected();
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

                entityPermissionsTable.repaint();

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
