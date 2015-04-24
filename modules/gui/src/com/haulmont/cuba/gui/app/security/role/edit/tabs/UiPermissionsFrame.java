/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.app.security.ds.RestorablePermissionDatasource;
import com.haulmont.cuba.gui.app.security.ds.UiPermissionsDatasource;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionVariant;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class UiPermissionsFrame extends AbstractFrame {

    public interface Companion {
        void initPermissionsColoredColumns(Table uiPermissionsTable);
    }

    @Inject
    protected Datasource<Role> roleDs;

    @Inject
    protected LookupField screenFilter;

    @Inject
    protected TextField componentTextField;

    @Inject
    protected RestorablePermissionDatasource uiPermissionsDs;

    @Inject
    protected UiPermissionsDatasource uiPermissionTargetsDs;

    @Inject
    protected UserSession userSession;

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
    protected GroupTable uiPermissionsTable;

    @Inject
    protected Button removePermissionBtn;

    @Inject
    protected Button addPermissionBtn;

    protected boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        Collection<WindowInfo> windows = sortWindowInfos(windowConfig.getWindows());
        Map<String, Object> screens = new LinkedHashMap<>();
        for (WindowInfo windowInfo : windows) {
            String id = windowInfo.getId();
            String menuId = "menu-config." + id;
            String localeMsg = messages.getMessage(AppConfig.getMessagesPack(), menuId);
            String title = menuId.equals(localeMsg) ? id : localeMsg + " (" + id + ")";
            screens.put(title, id);
        }
        screenFilter.setOptionsMap(screens);

        Companion companion = getCompanion();
        companion.initPermissionsColoredColumns(uiPermissionsTable);

        uiPermissionTargetsDs.addListener(new CollectionDsListenerAdapter<UiPermissionTarget>() {
            @Override
            public void itemChanged(Datasource<UiPermissionTarget> ds, UiPermissionTarget prevItem, UiPermissionTarget item) {
                if (!selectedComponentPanel.isVisible() && (item != null))
                    selectedComponentPanel.setVisible(true);
                if (selectedComponentPanel.isVisible() && (item == null))
                    selectedComponentPanel.setVisible(false);

                updateCheckBoxes(item);
            }

            @Override
            public void valueChanged(UiPermissionTarget source, String property, Object prevValue, Object value) {
                if ("permissionVariant".equals(property)) {
                    updateCheckBoxes(uiPermissionsTable.<UiPermissionTarget>getSingleSelected());
                }
            }
        });

        attachCheckBoxListener(readOnlyCheckBox, UiPermissionVariant.READ_ONLY);
        attachCheckBoxListener(hideCheckBox, UiPermissionVariant.HIDE);
        attachCheckBoxListener(showCheckBox, UiPermissionVariant.SHOW);

        uiPermissionTargetsDs.setPermissionDs(uiPermissionsDs);

        uiPermissionsDs.refresh();
        uiPermissionTargetsDs.refresh();

        boolean isCreatePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.CREATE);
        boolean isDeletePermitted = security.isEntityOpPermitted(Permission.class, EntityOp.DELETE);

        final boolean hasPermissionsToModifyPermission = isCreatePermitted && isDeletePermitted;

        RemoveAction removeAction = new RemoveAction(uiPermissionsTable, false) {
            @Override
            protected void afterRemove(Set selected) {
                if (!selected.isEmpty()) {
                    markItemPermission(UiPermissionVariant.NOTSET, (UiPermissionTarget) selected.iterator().next());
                }
            }

            @Override
            protected boolean isPermitted() {
                return hasPermissionsToModifyPermission && super.isPermitted();
            }
        };
        removeAction.setIcon(null);
        removeAction.setCaption(getMessage("actions.RemoveSelected"));

        removePermissionBtn.setAction(removeAction);
        uiPermissionsTable.addAction(removeAction);

        applyPermissions(hasPermissionsToModifyPermission);
    }

    protected Collection<WindowInfo> sortWindowInfos(Collection<WindowInfo> infos) {
        List<WindowInfo> infosContainer = new ArrayList<>(infos);

        Collections.sort(infosContainer, new Comparator<WindowInfo>() {
            @Override
            public int compare(WindowInfo o1, WindowInfo o2) {
                if (o1.getId().contains("$") != o2.getId().contains("$")) {
                    if (o1.getId().contains("$")) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return o1.getId().compareTo(o2.getId());
                }
            }
        });

        return infosContainer;
    }

    protected void applyPermissions(boolean editable) {
        if (!editable) {
            hideCheckBox.setEditable(editable);
            showCheckBox.setEditable(editable);
            readOnlyCheckBox.setEditable(editable);

            addPermissionBtn.setEnabled(editable);
        }
    }

    protected void attachCheckBoxListener(CheckBox checkBox, final UiPermissionVariant activeVariant) {
        checkBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (itemChanging)
                    return;

                if (uiPermissionsTable.getSelected().isEmpty())
                    return;

                itemChanging = true;

                UiPermissionVariant permissionVariant = PermissionUiHelper.getCheckBoxVariant(value, activeVariant);
                UiPermissionTarget target = uiPermissionsTable.getSingleSelected();
                markItemPermission(permissionVariant, target);

                uiPermissionTargetsDs.updateItem(target);

                itemChanging = false;
            }
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
                    if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
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
}