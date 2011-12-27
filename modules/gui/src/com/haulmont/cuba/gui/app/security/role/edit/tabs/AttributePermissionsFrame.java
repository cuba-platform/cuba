/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.security.MultiplePermissionTargetsDatasource;
import com.haulmont.cuba.gui.security.RestorablePermissionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.ui.AttributePermissionVariant;
import com.haulmont.cuba.security.entity.ui.AttributeTarget;
import com.haulmont.cuba.security.entity.ui.MultiplePermissionTarget;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class AttributePermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionColoredColumn(Table propertyPermissionsTable);
    }

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private RestorablePermissionDatasource propertyPermissionsDs;

    @Inject
    private MultiplePermissionTargetsDatasource attributeTargetsDs;

    /* Selection */

    @Inject
    private Table propertyPermissionsTable;

    @Inject
    private Label selectedTargetCaption;

    /* Panels */

    @Inject
    private BoxLayout selectedEntityPanel;

    @Inject
    private BoxLayout editGridContainer;

    /* Filter */

    @Inject
    private TextField entityFilter;

    @Inject
    private CheckBox assignedOnlyCheckBox;

    /* Buttons */

    @Inject
    private Button applyFilterBtn;

    /* Checkboxes */

    @Inject
    private CheckBox allModifyCheck;

    @Inject
    private CheckBox allReadOnlyCheck;

    @Inject
    private CheckBox allHideCheck;

    /* */

    private boolean itemChanging = false;

    private ComponentsFactory uiFactory = AppConfig.getFactory();

    private class AttributePermissionControl {

        private Label attributeLabel;
        private CheckBox modifyCheckBox;
        private CheckBox readOnlyCheckBox;
        private CheckBox hideCheckBox;

        private MultiplePermissionTarget item;
        private String attributeName;

        private AttributePermissionControl(MultiplePermissionTarget item, String attributeName) {
            this.item = item;
            this.attributeName = attributeName;

            AttributePermissionVariant permissionVariant = item.getPermissionVariant(attributeName);

            attributeLabel = uiFactory.createComponent(Label.NAME);
            attributeLabel.setFrame(AttributePermissionsFrame.this);
            attributeLabel.setValue(attributeName);

            modifyCheckBox = uiFactory.createComponent(CheckBox.NAME);
            modifyCheckBox.setAlignment(Alignment.MIDDLE_CENTER);
            modifyCheckBox.setFrame(AttributePermissionsFrame.this);
            attachListener(modifyCheckBox, AttributePermissionVariant.MODIFY);

            readOnlyCheckBox = uiFactory.createComponent(CheckBox.NAME);
            readOnlyCheckBox.setAlignment(Alignment.MIDDLE_CENTER);
            readOnlyCheckBox.setFrame(AttributePermissionsFrame.this);
            attachListener(readOnlyCheckBox, AttributePermissionVariant.READ_ONLY);

            hideCheckBox = uiFactory.createComponent(CheckBox.NAME);
            hideCheckBox.setAlignment(Alignment.MIDDLE_CENTER);
            hideCheckBox.setFrame(AttributePermissionsFrame.this);
            attachListener(hideCheckBox, AttributePermissionVariant.HIDE);

            updateCheckers(permissionVariant);
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

        private void updateCheckers(AttributePermissionVariant permissionVariant) {
            modifyCheckBox.setValue(permissionVariant == AttributePermissionVariant.MODIFY);
            readOnlyCheckBox.setValue(permissionVariant == AttributePermissionVariant.READ_ONLY);
            hideCheckBox.setValue(permissionVariant == AttributePermissionVariant.HIDE);
        }

        private void attachListener(CheckBox checkBox, final AttributePermissionVariant activeVariant) {
            checkBox.addListener(new ValueListener<CheckBox>() {
                @Override
                public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                    if (itemChanging)
                        return;

                    if (propertyPermissionsTable.getSelected().isEmpty())
                        return;

                    itemChanging = true;

                    markTargetPermission(PermissionUiHelper.getCheckBoxVariant(value, activeVariant));

                    if (activeVariant != AttributePermissionVariant.MODIFY)
                        modifyCheckBox.setValue(false);

                    if (activeVariant != AttributePermissionVariant.READ_ONLY)
                        readOnlyCheckBox.setValue(false);

                    if (activeVariant != AttributePermissionVariant.HIDE)
                        hideCheckBox.setValue(false);

                    allModifyCheck.setValue(item.isAllModified());
                    allReadOnlyCheck.setValue(item.isAllReadOnly());
                    allHideCheck.setValue(item.isAllHide());

                    // todo enforce value change
                    propertyPermissionsTable.repaint();

                    itemChanging = false;
                }
            });
        }

        public void markTargetPermission(AttributePermissionVariant permissionVariant) {
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
                for (UUID id : propertyPermissionsDs.getItemIds()) {
                    Permission p = propertyPermissionsDs.getItem(id);
                    if (ObjectUtils.equals(p.getTarget(), permissionValue)) {
                        permission = p;
                        break;
                    }
                }

                if (permission != null)
                    propertyPermissionsDs.removeItem(permission);
            }
        }
    }

    private final List<AttributePermissionControl> permissionControls = new LinkedList<AttributePermissionControl>();

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        assignedOnlyCheckBox.setValue(Boolean.TRUE);

        attributeTargetsDs.setPermissionDs(propertyPermissionsDs);
        attributeTargetsDs.setFilter(
                new EntityNameFilter<MultiplePermissionTarget>(assignedOnlyCheckBox, entityFilter));

        applyFilterBtn.setAction(new AbstractAction("action.apply") {
            @Override
            public void actionPerform(Component component) {
                attributeTargetsDs.refresh();
            }
        });

        attachAllCheckboxListener(allModifyCheck, AttributePermissionVariant.MODIFY);
        attachAllCheckboxListener(allReadOnlyCheck, AttributePermissionVariant.READ_ONLY);
        attachAllCheckboxListener(allHideCheck, AttributePermissionVariant.HIDE);

        // client specific code
        Companion companion = getCompanion();
        companion.initPermissionColoredColumn(propertyPermissionsTable);

        attributeTargetsDs.addListener(new CollectionDsListenerAdapter<MultiplePermissionTarget>() {
            @Override
            public void itemChanged(Datasource<MultiplePermissionTarget> ds,
                                    MultiplePermissionTarget prevItem, MultiplePermissionTarget item) {
                if (!selectedEntityPanel.isVisible() && (item != null))
                    selectedEntityPanel.setVisible(true);
                if (selectedEntityPanel.isVisible() && (item == null))
                    selectedEntityPanel.setVisible(false);

                selectedTargetCaption.setVisible(item != null);
                selectedTargetCaption.setValue(item != null ? item.getCaption() : "");

                clearEditGrid();
                if (item != null)
                    compileEditPane(item);
            }
        });

        propertyPermissionsDs.refresh();
        attributeTargetsDs.refresh();
    }

    private void attachAllCheckboxListener(CheckBox checkBox, final AttributePermissionVariant activeVariant) {
        checkBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (itemChanging)
                    return;

                if (propertyPermissionsTable.getSelected().isEmpty())
                    return;

                itemChanging = true;
                MultiplePermissionTarget item = propertyPermissionsTable.getSingleSelected();
                if (item != null) {

                    for (AttributePermissionControl control : permissionControls) {
                        AttributePermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(value, activeVariant);
                        control.markTargetPermission(permissionVariant);
                        control.updateCheckers(permissionVariant);
                    }

                    // todo enforce value change
                    propertyPermissionsTable.repaint();

                    allModifyCheck.setValue(item.isAllModified());
                    allReadOnlyCheck.setValue(item.isAllReadOnly());
                    allHideCheck.setValue(item.isAllHide());
                }

                itemChanging = false;
            }
        });
    }

    private void clearEditGrid() {
        Collection<Component> components = new ArrayList<Component>(editGridContainer.getComponents());
        for (Component component : components)
            editGridContainer.remove(component);
        permissionControls.clear();
    }

    private void compileEditPane(MultiplePermissionTarget item) {
        GridLayout editGrid = uiFactory.createComponent(GridLayout.NAME);
        editGrid.setFrame(this);
        editGrid.setId("editGrid");
        editGrid.setWidth("100%");
        editGrid.setColumns(4);
        editGrid.setMargin(true);
        editGrid.setColumnExpandRatio(0, 0.4f);
        editGrid.setColumnExpandRatio(1, 0.2f);
        editGrid.setColumnExpandRatio(2, 0.2f);
        editGrid.setColumnExpandRatio(3, 0.2f);

        editGrid.setRows(item.getPermissions().size() + 1);

        Label emptyLabel = uiFactory.createComponent(Label.NAME);
        editGrid.add(emptyLabel, 0, 0);

        Label modifyLabel = uiFactory.createComponent(Label.NAME);
        modifyLabel.setAlignment(Alignment.MIDDLE_CENTER);
        modifyLabel.setValue(getMessage("checkbox.modify"));
        editGrid.add(modifyLabel, 1, 0);

        Label readOnlyLabel = uiFactory.createComponent(Label.NAME);
        readOnlyLabel.setAlignment(Alignment.MIDDLE_CENTER);
        readOnlyLabel.setValue(getMessage("checkbox.readOnly"));
        editGrid.add(readOnlyLabel, 2, 0);

        Label hideLabel = uiFactory.createComponent(Label.NAME);
        hideLabel.setAlignment(Alignment.MIDDLE_CENTER);
        hideLabel.setValue(getMessage("checkbox.hide"));
        editGrid.add(hideLabel, 3, 0);

        int i = 0;
        for (AttributeTarget target : item.getPermissions()) {
            AttributePermissionControl control = new AttributePermissionControl(item, target.getId());
            int gridRow = i + 1;

            editGrid.add(control.getAttributeLabel(), 0, gridRow);
            editGrid.add(control.getModifyCheckBox(), 1, gridRow);
            editGrid.add(control.getReadOnlyCheckBox(), 2, gridRow);
            editGrid.add(control.getHideCheckBox(), 3, gridRow);

            permissionControls.add(control);
            i++;
        }

        editGridContainer.add(editGrid);
    }
}