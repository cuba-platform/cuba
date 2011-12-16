/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.security.ScreenPermissionTreeDatasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;
import com.haulmont.cuba.security.entity.ui.PermissionVariant;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ScreenPermissionsFrame extends AbstractFrame {

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private CollectionDatasource<Permission, UUID> screenPermissionsDs;

    @Inject
    private TreeTable screenPermissionsTree;

    @Inject
    private ScreenPermissionTreeDatasource screenPermissionsTreeDs;

    @Inject
    private BoxLayout selectedScreenPanel;

    @Inject
    private CheckBox allowCheckBox;

    @Inject
    private CheckBox disallowCheckBox;

    private boolean itemChanging = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        screenPermissionsDs = getDsContext().get("screenPermissionsDs");
        screenPermissionsTreeDs = getDsContext().get("screenPermissionsTreeDs");

        screenPermissionsTree.setStyleProvider(new ScreenTreeStyleProvider());

        screenPermissionsTree.addAction(new AbstractAction("actions.Allow") {
            @Override
            public void actionPerform(Component component) {
                markItemPermission(PermissionVariant.ALLOWED);
            }
        });
        screenPermissionsTree.addAction(new AbstractAction("actions.Disallow") {
            @Override
            public void actionPerform(Component component) {
                markItemPermission(PermissionVariant.DISALLOWED);
            }
        });
        screenPermissionsTree.addAction(new AbstractAction("actions.DropRule") {
            @Override
            public void actionPerform(Component component) {
                markItemPermission(PermissionVariant.NOTSET);
            }
        });

        screenPermissionsTreeDs.addListener(new CollectionDsListenerAdapter<BasicPermissionTarget>() {
            @Override
            public void itemChanged(Datasource<BasicPermissionTarget> ds,
                                    BasicPermissionTarget prevItem, BasicPermissionTarget item) {
                if (!selectedScreenPanel.isVisible() && (item != null))
                    selectedScreenPanel.setVisible(true);
                if (selectedScreenPanel.isVisible() && (item == null))
                    selectedScreenPanel.setVisible(false);

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
//
//                    disallowCheckBox.setValue(false);

                    itemChanging = false;
                }
            }
        });

        disallowCheckBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (!itemChanging) {
                    itemChanging = true;
//
                    markItemPermission(PermissionUiHelper.getCheckBoxVariant(value, PermissionVariant.DISALLOWED));

//                    allowCheckBox.setValue(false);

                    itemChanging = false;
                }
            }
        });
    }

    private void markItemPermission(PermissionVariant permissionVariant) {
        BasicPermissionTarget target = screenPermissionsTree.getSingleSelected();
        if (target != null) {
            target.setPermissionVariant(permissionVariant);
            if (permissionVariant != PermissionVariant.NOTSET) {
                // Create permission
                int value = PermissionUiHelper.getPermissionValue(permissionVariant);
                PermissionUiHelper.createPermissionItem(screenPermissionsDs, roleDs,
                        target.getPermissionValue(), PermissionType.SCREEN, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (UUID id : screenPermissionsDs.getItemIds()) {
                    Permission p = screenPermissionsDs.getItem(id);
                    if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
                        permission = p;
                        break;
                    }
                }
                if (permission != null)
                    screenPermissionsDs.removeItem(permission);
            }
            screenPermissionsTree.repaint();
        }
    }

    public void setItem() {
        screenPermissionsDs.refresh();
        screenPermissionsTreeDs.setPermissionDs(screenPermissionsDs);
        screenPermissionsTree.refresh();
        screenPermissionsTree.expandAll();
    }

    private class ScreenTreeStyleProvider implements Table.StyleProvider {
        @Override
        public String getStyleName(Entity item, Object property) {
            if (property != null) {
                MetaPropertyPath metaPropertyPath = (MetaPropertyPath) property;
                if ("caption".equals(metaPropertyPath.getMetaProperty().getName())) {
                    if (item instanceof BasicPermissionTarget) {
                        PermissionVariant permissionVariant = ((BasicPermissionTarget) item).getPermissionVariant();
                        switch (permissionVariant) {
                            case ALLOWED:
                                return "allowedItem";

                            case DISALLOWED:
                                return "disallowedItem";
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public String getItemIcon(Entity item) {
            return null;
        }
    }
}