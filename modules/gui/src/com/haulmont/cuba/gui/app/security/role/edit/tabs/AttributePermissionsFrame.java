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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.app.security.ds.MultiplePermissionTargetsDatasource;
import com.haulmont.cuba.gui.app.security.ds.RestorablePermissionDatasource;
import com.haulmont.cuba.gui.app.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.app.security.entity.AttributeTarget;
import com.haulmont.cuba.gui.app.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;

public class AttributePermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionColoredColumn(Table<MultiplePermissionTarget> propertyPermissionsTable);
    }

    @Inject
    protected Companion companion;

    @Inject
    protected Datasource<Role> roleDs;

    @Inject
    protected RestorablePermissionDatasource propertyPermissionsDs;

    @Inject
    protected MultiplePermissionTargetsDatasource attributeTargetsDs;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Security security;

    /* Selection */

    @Inject
    protected Table<MultiplePermissionTarget> propertyPermissionsTable;

    @Inject
    protected Label selectedTargetCaption;

    @Inject
    protected Label selectedTargetLocalCaption;

    /* Panels */

    @Inject
    protected BoxLayout selectedEntityPanel;

    @Inject
    protected ScrollBoxLayout editGridContainer;

    /* Filter */

    @Inject
    protected TextField entityFilter;

    @Inject
    protected CheckBox assignedOnlyCheckBox;

    @Inject
    protected CheckBox systemLevelCheckBox;

    /* Checkboxes */

    protected CheckBox allModifyCheck;

    protected CheckBox allReadOnlyCheck;

    protected CheckBox allHideCheck;

    protected boolean itemChanging = false;

    protected boolean hasPermissionsToModifyPermission;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private GroupBoxLayout editPane;

    protected class AttributePermissionControl {

        protected Label attributeLabel;
        protected CheckBox modifyCheckBox;
        protected CheckBox readOnlyCheckBox;
        protected CheckBox hideCheckBox;

        protected MultiplePermissionTarget item;
        protected String attributeName;

        public AttributePermissionControl(MultiplePermissionTarget item, String attributeName) {
            this.item = item;
            this.attributeName = attributeName;

            AttributePermissionVariant permissionVariant = item.getPermissionVariant(attributeName);

            attributeLabel = componentsFactory.createComponent(Label.class);
            attributeLabel.setValue(attributeName);

            modifyCheckBox = componentsFactory.createComponent(CheckBox.class);
            modifyCheckBox.setAlignment(Alignment.MIDDLE_CENTER);
            modifyCheckBox.setFrame(getFrame());
            modifyCheckBox.setDescription(attributeName);
            modifyCheckBox.setId(attributeName + "_modifyCheckBox");

            readOnlyCheckBox = componentsFactory.createComponent(CheckBox.class);
            readOnlyCheckBox.setAlignment(Alignment.MIDDLE_CENTER);
            readOnlyCheckBox.setFrame(getFrame());
            readOnlyCheckBox.setDescription(attributeName);
            readOnlyCheckBox.setId(attributeName + "_readOnlyCheckBox");

            hideCheckBox = componentsFactory.createComponent(CheckBox.class);
            hideCheckBox.setAlignment(Alignment.MIDDLE_CENTER);
            hideCheckBox.setFrame(getFrame());
            hideCheckBox.setDescription(attributeName);
            hideCheckBox.setId(attributeName + "_hideCheckBox");

            updateCheckers(permissionVariant);

            attachListener(modifyCheckBox, AttributePermissionVariant.MODIFY);
            attachListener(readOnlyCheckBox, AttributePermissionVariant.READ_ONLY);
            attachListener(hideCheckBox, AttributePermissionVariant.HIDE);
        }

        public Label getAttributeLabel() {
            return attributeLabel;
        }

        public CheckBox getModifyCheckBox() {
            return modifyCheckBox;
        }

        public CheckBox getReadOnlyCheckBox() {
            return readOnlyCheckBox;
        }

        public CheckBox getHideCheckBox() {
            return hideCheckBox;
        }

        public String getAttributeName() {
            return attributeName;
        }

        protected void updateCheckers(AttributePermissionVariant permissionVariant) {
            if (modifyCheckBox.isEditable()) {
                modifyCheckBox.setValue(permissionVariant == AttributePermissionVariant.MODIFY);
            }
            if (readOnlyCheckBox.isEditable()) {
                readOnlyCheckBox.setValue(permissionVariant == AttributePermissionVariant.READ_ONLY);
            }
            if (hideCheckBox.isEditable()) {
                hideCheckBox.setValue(permissionVariant == AttributePermissionVariant.HIDE);
            }
        }

        protected void attachListener(CheckBox checkBox, final AttributePermissionVariant activeVariant) {
            checkBox.addValueChangeListener(e -> {
                if (itemChanging) {
                    return;
                }

                if (propertyPermissionsTable.getSelected().isEmpty()) {
                    return;
                }

                itemChanging = true;

                markTargetPermission(PermissionUiHelper.getCheckBoxVariant(e.getValue(), activeVariant));

                if (activeVariant != AttributePermissionVariant.MODIFY)
                    modifyCheckBox.setValue(false);

                if (activeVariant != AttributePermissionVariant.READ_ONLY)
                    readOnlyCheckBox.setValue(false);

                if (activeVariant != AttributePermissionVariant.HIDE)
                    hideCheckBox.setValue(false);

                allModifyCheck.setValue(isAllModified());
                allReadOnlyCheck.setValue(isAllReadOnly());
                allHideCheck.setValue(isAllHide());

                // todo enforce property change instead of item
                attributeTargetsDs.updateItem(item);

                itemChanging = false;
            });
        }

        public void markTargetPermission(AttributePermissionVariant permissionVariant) {
            if (!permissionApplicable(permissionVariant)) {
                return;
            }

            item.assignPermissionVariant(attributeName, permissionVariant);
            String permissionValue = item.getPermissionValue() + Permission.TARGET_PATH_DELIMETER + attributeName;
            if (permissionVariant != AttributePermissionVariant.NOTSET) {
                // Create permission
                int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                PermissionUiHelper.createPermissionItem(propertyPermissionsDs, roleDs,
                        permissionValue, PermissionType.ENTITY_ATTR, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (Permission p : propertyPermissionsDs.getItems()) {
                    if (Objects.equals(p.getTarget(), permissionValue)) {
                        permission = p;
                        break;
                    }
                }

                if (permission != null) {
                    propertyPermissionsDs.removeItem(permission);
                }
            }
        }

        private boolean permissionApplicable(AttributePermissionVariant permissionVariant) {
            return permissionVariant == AttributePermissionVariant.NOTSET
                    || (permissionVariant == AttributePermissionVariant.MODIFY && modifyCheckBox.isEditable())
                    || (permissionVariant == AttributePermissionVariant.READ_ONLY && readOnlyCheckBox.isEditable())
                    || (permissionVariant == AttributePermissionVariant.HIDE && hideCheckBox.isEditable());
        }
    }

    protected final List<AttributePermissionControl> permissionControls = new LinkedList<>();

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        if (!PersistenceHelper.isNew(params.get("ITEM"))) {
            assignedOnlyCheckBox.setValue(Boolean.TRUE);
        }

        assignedOnlyCheckBox.addValueChangeListener(e -> {
            applyFilter();
        });

        attributeTargetsDs.setPermissionDs(propertyPermissionsDs);
        attributeTargetsDs.setFilter(
                new EntityNameFilter<>(
                        metadata, assignedOnlyCheckBox, systemLevelCheckBox, entityFilter));

        propertyPermissionsDs.refresh();

        // client specific code
        companion.initPermissionColoredColumn(propertyPermissionsTable);

        entityFilter.addEnterPressListener(e -> applyFilter());

        attributeTargetsDs.addItemChangeListener(e -> {
            MultiplePermissionTarget item = e.getItem();
            boolean itemExists = item != null;
            if (!selectedEntityPanel.isVisible() && itemExists) {
                selectedEntityPanel.setVisible(true);
            }
            if (selectedEntityPanel.isVisible() && (item == null)) {
                selectedEntityPanel.setVisible(false);
            }

            String name = null;
            String localName = null;
            if (itemExists) {
                name = item.getEntityMetaClassName();
                localName = item.getLocalName();
            }

            selectedTargetCaption.setVisible(itemExists);
            selectedTargetCaption.setValue(name);
            selectedTargetCaption.setDescription(name);
            selectedTargetLocalCaption.setVisible(itemExists);
            selectedTargetLocalCaption.setValue(localName);
            selectedTargetLocalCaption.setDescription(localName);

            clearEditGrid();
            if (itemExists) {
                compileEditPane(item);
            }
        });

        attributeTargetsDs.refresh();

        boolean isCreatePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);
        boolean isDeletePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.DELETE);
        hasPermissionsToModifyPermission = isCreatePermitted && isDeletePermitted;

        editPane.setEnabled(security.isEntityOpPermitted(metadata.getClass(Role.class), EntityOp.UPDATE));
    }

    public void applyFilter() {
        attributeTargetsDs.refresh();
        if (attributeTargetsDs.getItemIds().isEmpty()) {
            String message;
            Object value = entityFilter.getValue();
            if (Boolean.TRUE.equals(assignedOnlyCheckBox.getValue()))
                message = String.format(getMessage("noAssignedItemsForFilter"), value != null ? value : " ");
            else
                message = String.format(getMessage("noItemsForFilter"), value != null ? value : " ");
            showNotification(message, NotificationType.HUMANIZED);
        }
    }

    protected void attachAllCheckboxListener(CheckBox checkBox, final AttributePermissionVariant activeVariant) {
        checkBox.addValueChangeListener(e -> {
            if (itemChanging) {
                return;
            }

            if (propertyPermissionsTable.getSelected().isEmpty()) {
                return;
            }

            itemChanging = true;
            MultiplePermissionTarget item = propertyPermissionsTable.getSingleSelected();
            if (item != null) {

                for (AttributePermissionControl control : permissionControls) {
                    AttributePermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(e.getValue(), activeVariant);
                    control.markTargetPermission(permissionVariant);
                    control.updateCheckers(permissionVariant);
                }

                // todo enforce value change
                propertyPermissionsTable.repaint();

                allModifyCheck.setValue(isAllModified());
                allReadOnlyCheck.setValue(isAllReadOnly());
                allHideCheck.setValue(isAllHide());
            }

            itemChanging = false;
        });
    }

    private boolean isAllModified() {
        return permissionControls.stream().allMatch(c -> c.modifyCheckBox.getValue() || !c.modifyCheckBox.isEditable());
    }

    private boolean isAllReadOnly() {
        return permissionControls.stream().allMatch(c -> c.readOnlyCheckBox.getValue() || !c.readOnlyCheckBox.isEditable());
    }

    private boolean isAllHide() {
        return permissionControls.stream().allMatch(c -> c.hideCheckBox.getValue() || !c.hideCheckBox.isEditable());
    }

    protected void applyPermissions(boolean editable) {
        allHideCheck.setEditable(editable);
        allModifyCheck.setEditable(editable);
        allReadOnlyCheck.setEditable(editable);

        MetaClass metaClass = attributeTargetsDs.getItem().getEntityMetaClass();
        for (AttributePermissionControl attributePermissionControl : permissionControls) {
            attributePermissionControl.getHideCheckBox().setEditable(editable);
            attributePermissionControl.getHideCheckBox().setEnabled(editable);

            attributePermissionControl.getReadOnlyCheckBox().setEditable(editable);
            attributePermissionControl.getReadOnlyCheckBox().setEnabled(editable);

            String attributeName = attributePermissionControl.getAttributeName();
            if (userSession.isEntityAttrPermitted(metaClass, attributeName, EntityAttrAccess.MODIFY)) {
                boolean canUpdateEntity = userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE);
                attributePermissionControl.getModifyCheckBox().setEditable(canUpdateEntity && editable);
                attributePermissionControl.getModifyCheckBox().setEnabled(canUpdateEntity && editable);
            } else {
                attributePermissionControl.getModifyCheckBox().setEditable(false);
                attributePermissionControl.getModifyCheckBox().setEnabled(false);
            }
        }
    }

    protected void clearEditGrid() {
        Collection<Component> components = new ArrayList<>(editGridContainer.getComponents());
        for (Component component : components)
            editGridContainer.remove(component);
        permissionControls.clear();
    }

    protected void compileEditPane(MultiplePermissionTarget item) {
        GridLayout editGrid = componentsFactory.createComponent(GridLayout.class);
        editGrid.setFrame(this);
        editGrid.setId("editGrid");
        editGrid.setWidth("100%");
        editGrid.setColumns(4);
        editGrid.setMargin(true);
        editGrid.setColumnExpandRatio(0, 1f);
        editGrid.setColumnExpandRatio(1, 0);
        editGrid.setColumnExpandRatio(2, 0);
        editGrid.setColumnExpandRatio(3, 0);

        editGrid.setRows(item.getPermissions().size() + 2);

        compileDefaultControls(editGrid);

        initPermissionControls(item, editGrid);

        editGridContainer.add(editGrid);

        applyPermissions(hasPermissionsToModifyPermission);
    }

    private void initPermissionControls(MultiplePermissionTarget item, GridLayout editGrid) {
        int i = 0;

        MetaClass metaClass = item.getEntityMetaClass();
        for (AttributeTarget target : item.getPermissions()) {
            if (!userSession.isEntityAttrPermitted(metaClass, target.getId(), EntityAttrAccess.VIEW)) {
                continue;
            }

            AttributePermissionControl control = new AttributePermissionControl(item, target.getId());
            int gridRow = i + 2;

            editGrid.add(control.getAttributeLabel(), 0, gridRow);
            editGrid.add(control.getModifyCheckBox(), 1, gridRow);
            editGrid.add(control.getReadOnlyCheckBox(), 2, gridRow);
            editGrid.add(control.getHideCheckBox(), 3, gridRow);

            control.getModifyCheckBox().setAlignment(Alignment.MIDDLE_CENTER);
            control.getReadOnlyCheckBox().setAlignment(Alignment.MIDDLE_CENTER);
            control.getHideCheckBox().setAlignment(Alignment.MIDDLE_CENTER);

            permissionControls.add(control);
            i++;
        }
    }

    protected void compileDefaultControls(GridLayout editGrid) {
        Label emptyLabel = componentsFactory.createComponent(Label.class);
        editGrid.add(emptyLabel, 0, 0);

        Label modifyLabel = componentsFactory.createComponent(Label.class);
        modifyLabel.setValue(getMessage("checkbox.modify"));
        modifyLabel.setAlignment(Alignment.MIDDLE_CENTER);
        modifyLabel.setStyleName("centered");

        BoxLayout modifyBox = componentsFactory.createComponent(HBoxLayout.class);
        modifyBox.setMargin(false, true, false, false);
        modifyBox.setFrame(getFrame());
        modifyBox.add(modifyLabel);

        editGrid.add(modifyBox, 1, 0);

        Label readOnlyLabel = componentsFactory.createComponent(Label.class);
        readOnlyLabel.setValue(getMessage("checkbox.readOnly"));
        readOnlyLabel.setAlignment(Alignment.MIDDLE_CENTER);
        readOnlyLabel.setStyleName("centered");

        BoxLayout readOnlyBox = componentsFactory.createComponent(HBoxLayout.class);
        readOnlyBox.setMargin(false, true, false, true);
        readOnlyBox.setFrame(getFrame());
        readOnlyBox.add(readOnlyLabel);

        editGrid.add(readOnlyBox, 2, 0);

        Label hideLabel = componentsFactory.createComponent(Label.class);
        hideLabel.setValue(getMessage("checkbox.hide"));
        hideLabel.setAlignment(Alignment.MIDDLE_CENTER);
        hideLabel.setStyleName("centered");

        BoxLayout hideBox = componentsFactory.createComponent(HBoxLayout.class);
        hideBox.setFrame(getFrame());
        hideBox.setMargin(false, false, false, true);
        hideBox.add(hideLabel);

        editGrid.add(hideBox, 3, 0);

        allModifyCheck = componentsFactory.createComponent(CheckBox.class);
        allReadOnlyCheck = componentsFactory.createComponent(CheckBox.class);
        allHideCheck = componentsFactory.createComponent(CheckBox.class);

        allModifyCheck.setFrame(getFrame());
        allReadOnlyCheck.setFrame(getFrame());
        allHideCheck.setFrame(getFrame());

        allModifyCheck.setId("allAttributesModifyCheck");
        allReadOnlyCheck.setId("allAttributesReadOnlyCheck");
        allHideCheck.setId("allAttributesHideCheck");

        attachAllCheckboxListener(allModifyCheck, AttributePermissionVariant.MODIFY);
        attachAllCheckboxListener(allReadOnlyCheck, AttributePermissionVariant.READ_ONLY);
        attachAllCheckboxListener(allHideCheck, AttributePermissionVariant.HIDE);

        emptyLabel = componentsFactory.createComponent(Label.class);
        emptyLabel.setValue(getMessage("allEntities"));
        editGrid.add(emptyLabel, 0, 1);

        editGrid.add(allModifyCheck, 1, 1);
        editGrid.add(allReadOnlyCheck, 2, 1);
        editGrid.add(allHideCheck, 3, 1);

        allModifyCheck.setAlignment(Alignment.MIDDLE_CENTER);
        allReadOnlyCheck.setAlignment(Alignment.MIDDLE_CENTER);
        allHideCheck.setAlignment(Alignment.MIDDLE_CENTER);
    }
}