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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.ds.RestorablePermissionDatasource;
import com.haulmont.cuba.gui.app.security.ds.ScreenComponentsTreeDatasource;
import com.haulmont.cuba.gui.app.security.ds.UiPermissionsDatasource;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.*;

public class UiPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionsColoredColumns(Table<UiPermissionTarget> uiPermissionsTable);
    }

    @Inject
    protected Datasource<Role> roleDs;

    @Inject
    protected LookupField<String> screenFilter;

    @Inject
    protected TextField<String> componentTextField;

    @Inject
    protected RestorablePermissionDatasource uiPermissionsDs;

    @Inject
    protected UiPermissionsDatasource uiPermissionTargetsDs;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    @Inject
    protected BoxLayout selectedComponentPanel;

    @Inject
    protected CheckBox readOnlyCheckBox;

    @Inject
    protected CheckBox hideCheckBox;

    @Inject
    protected CheckBox showCheckBox;

    @Inject
    protected GroupTable<UiPermissionTarget> uiPermissionsTable;

    @Inject
    protected Button removePermissionBtn;

    @Inject
    protected Button addPermissionBtn;

    @Inject
    protected Companion companion;

    @Inject
    protected GroupBoxLayout editPane;

    @Inject
    protected Tree<ScreenComponentDescriptor> componentsTree;

    @Inject
    protected VBoxLayout componentsTreeBox;

    @Inject
    protected Button componentsTreeBtn;

    @Inject
    protected ScreenComponentsTreeDatasource componentDescriptorsDs;

    @Inject
    protected Icons icons;

    protected boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        initScreenFilter();

        companion.initPermissionsColoredColumns(uiPermissionsTable);

        // Remove useless information about screen component descriptors
        componentsTree.removeAction("showSystemInfo");

        componentsTree.addAction(new CopyComponentIdAction("copyComponentId"));

        uiPermissionTargetsDs.addItemChangeListener(e -> {
            if (!selectedComponentPanel.isVisible() && (e.getItem() != null)) {
                selectedComponentPanel.setVisible(true);
            }
            if (selectedComponentPanel.isVisible() && (e.getItem() == null)) {
                selectedComponentPanel.setVisible(false);
            }

            updateCheckBoxes(e.getItem());
        });

        uiPermissionTargetsDs.addItemPropertyChangeListener(e -> {
            if ("permissionVariant".equals(e.getProperty())) {
                updateCheckBoxes(uiPermissionsTable.getSingleSelected());
            }
        });

        attachCheckBoxListener(readOnlyCheckBox, UiPermissionVariant.READ_ONLY);
        attachCheckBoxListener(hideCheckBox, UiPermissionVariant.HIDE);
        attachCheckBoxListener(showCheckBox, UiPermissionVariant.SHOW);

        uiPermissionTargetsDs.setPermissionDs(uiPermissionsDs);

        uiPermissionsDs.refresh(getParamsForDatasource());
        uiPermissionTargetsDs.refresh();

        boolean isCreatePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);
        boolean isDeletePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.DELETE);
        boolean isRoleEditPermitted = security.isEntityOpPermitted(metadata.getClass(Role.class), EntityOp.UPDATE);

        final boolean hasPermissionsToModifyPermission = isCreatePermitted && isDeletePermitted && isRoleEditPermitted;

        RemoveAction removeAction = new RemoveAction(uiPermissionsTable, false);
        removeAction.setAfterRemoveHandler(removedItems -> {
            if (!removedItems.isEmpty()) {
                UiPermissionTarget removedPermission = (UiPermissionTarget) removedItems.iterator().next();
                markItemPermission(UiPermissionVariant.NOTSET, removedPermission);
            }
        });
        removeAction.setEnabled(hasPermissionsToModifyPermission);
        removeAction.setIcon(null);
        removeAction.setCaption(getMessage("actions.RemoveSelected"));

        removePermissionBtn.setAction(removeAction);
        if (roleDs.getItem().isPredefined()) {
            removePermissionBtn.setVisible(false);
        }
        uiPermissionsTable.addAction(removeAction);

        editPane.setEnabled(security.isEntityOpPermitted(Role.class, EntityOp.UPDATE));

        applyPermissions(hasPermissionsToModifyPermission);
    }

    protected void initScreenFilter() {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        Collection<WindowInfo> windows = sortWindowInfos(windowConfig.getWindows());
        Map<String, String> screens = new LinkedHashMap<>();
        for (WindowInfo windowInfo : windows) {
            String id = windowInfo.getId();
            String menuId = "menu-config." + id;
            String localeMsg = messages.getMessage(AppConfig.getMessagesPack(), menuId);
            String title = menuId.equals(localeMsg) ? id : localeMsg + " (" + id + ")";
            screens.put(title, id);
        }
        screenFilter.setOptionsMap(screens);

        componentsTreeBtn.setEnabled(screenFilter.getValue() != null);
        screenFilter.addValueChangeListener(e -> {
            componentsTreeBtn.setEnabled(screenFilter.getValue() != null);

            componentDescriptorsDs.setScreenId(screenFilter.getValue());
            componentDescriptorsDs.refresh();

            componentsTree.expandTree();
        });
    }

    public void changeComponentsTreeVisibility() {
        if (componentsTreeBox.isVisible()) {
            componentsTreeBox.setVisible(false);
            componentsTreeBtn.setDescription(getMessage("componentsTree.show"));
        } else if (StringUtils.isNotBlank(screenFilter.getValue())) {
            componentsTreeBox.setVisible(true);
            componentsTreeBtn.setDescription(getMessage("componentsTree.hide"));
        }
    }

    protected Collection<WindowInfo> sortWindowInfos(Collection<WindowInfo> infos) {
        List<WindowInfo> infosContainer = new ArrayList<>(infos);

        infosContainer.sort((o1, o2) -> {
            if (o1.getId().contains("$") != o2.getId().contains("$")) {
                if (o1.getId().contains("$")) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return o1.getId().compareTo(o2.getId());
            }
        });

        return infosContainer;
    }

    protected void applyPermissions(boolean editable) {
        editable = editable && !roleDs.getItem().isPredefined();

        if (!editable) {
            hideCheckBox.setEditable(false);
            showCheckBox.setEditable(false);
            readOnlyCheckBox.setEditable(false);

            addPermissionBtn.setEnabled(false);
        }
    }

    protected void attachCheckBoxListener(CheckBox checkBox, UiPermissionVariant activeVariant) {
        checkBox.addValueChangeListener(e -> {
            if (itemChanging)
                return;

            if (uiPermissionsTable.getSelected().isEmpty())
                return;

            itemChanging = true;

            UiPermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(e.getValue(), activeVariant);
            UiPermissionTarget target = uiPermissionsTable.getSingleSelected();
            markItemPermission(permissionVariant, target);

            uiPermissionTargetsDs.updateItem(target);

            itemChanging = false;
        });
    }

    protected void markItemPermission(UiPermissionVariant permissionVariant,
                                    UiPermissionTarget target) {
        if (target != null) {
            target.setPermissionVariant(permissionVariant);
            if (permissionVariant != UiPermissionVariant.NOTSET) {
                // Create permission
                int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                PermissionUiHelper.createPermissionItem(uiPermissionsDs, roleDs,
                        target.getPermissionValue(), PermissionType.UI, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (Permission p : uiPermissionsDs.getItems()) {
                    if (Objects.equals(p.getTarget(), target.getPermissionValue())) {
                        permission = p;
                        break;
                    }
                }

                if (permission != null)
                    uiPermissionsDs.removeItem(permission);
            }
        }
    }

    protected void updateCheckBoxes(UiPermissionTarget item) {
        itemChanging = true;

        if (item != null) {
            readOnlyCheckBox.setValue(item.getPermissionVariant() == UiPermissionVariant.READ_ONLY);
            hideCheckBox.setValue(item.getPermissionVariant() == UiPermissionVariant.HIDE);
            showCheckBox.setValue(item.getPermissionVariant() == UiPermissionVariant.SHOW);
        }

        itemChanging = false;
    }

    public void addUiPermission() {
        String screen = screenFilter.getValue();
        String component = componentTextField.getValue();
        if (StringUtils.isNotBlank(screen) && StringUtils.isNotBlank(component)) {
            UiPermissionTarget target = new UiPermissionTarget("ui:" + screen + ":" + component,
                    screen + ":" + component, screen + ":" + component, UiPermissionVariant.NOTSET);
            target.setScreen(screen);
            target.setComponent(component);
            uiPermissionTargetsDs.addItem(target);

            uiPermissionsTable.expandPath(target);
            uiPermissionsTable.setSelected(target);
        }
    }

    public void collapseTree() {
        componentsTree.collapseTree();
    }

    public void expandTree() {
        componentsTree.expandTree();
    }

    protected Map<String, Object> getParamsForDatasource() {
        Map<String, Object> params = new HashMap<>();

        params.put("role", roleDs.getItem());
        params.put("permissionType", PermissionType.UI);

        return params;
    }

    /**
     * Copies an id attribute of {@link #componentsTree} selected component to {@link #componentTextField}.
     * Disabled if no id is set.
     */
    protected class CopyComponentIdAction extends BaseAction {

        protected String selectedComponentId;

        private CopyComponentIdAction(String id) {
            super(id);
            setShortcut("CTRL-C");
            setIcon(icons.get(CubaIcon.COPY));
            setCaption(getMessage("actions.copyComponentId"));
        }

        @Override
        public void refreshState() {
            ScreenComponentDescriptor selectedComponent = componentsTree.getSingleSelected();
            if (selectedComponent != null) {
                selectedComponentId = selectedComponent.getElement().attributeValue("id");
            }
            super.refreshState();
        }

        @Override
        protected boolean isApplicable() {
            return selectedComponentId != null;
        }

        @Override
        public void actionPerform(Component component) {
            componentTextField.setValue(componentTextField.getRawValue() + selectedComponentId);
        }
    }
}