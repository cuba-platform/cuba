/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.security.ScreenPermissionTreeDatasource;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Collection;
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

    private boolean itemChanged = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        screenPermissionsTreeDs.addListener(new CollectionDatasourceListener<PermissionTarget>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
            }

            @Override
            public void itemChanged(Datasource<PermissionTarget> ds,
                                    PermissionTarget prevItem, PermissionTarget item) {
                if (!selectedScreenPanel.isVisible() && (item != null))
                    selectedScreenPanel.setVisible(true);
                if (selectedScreenPanel.isVisible() && (item == null))
                    selectedScreenPanel.setVisible(false);
            }

            @Override
            public void stateChanged(Datasource<PermissionTarget> ds, Datasource.State prevState, Datasource.State state) {
            }

            @Override
            public void valueChanged(PermissionTarget source, String property, Object prevValue, Object value) {
            }
        });

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

        screenPermissionsTreeDs.addListener(new DatasourceListener<PermissionTarget>() {
            @Override
            public void itemChanged(Datasource<PermissionTarget> ds,
                                    PermissionTarget prevItem, PermissionTarget item) {
                updateCheckBoxes(item);
            }

            @Override
            public void stateChanged(Datasource<PermissionTarget> ds,
                                     Datasource.State prevState, Datasource.State state) {
                // Do nothing
            }

            @Override
            public void valueChanged(PermissionTarget source,
                                     String property, Object prevValue, Object value) {
                if ("permissionVariant".equals(property))
                    updateCheckBoxes(source);
            }

            private void updateCheckBoxes(PermissionTarget item) {
                itemChanged = true;
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
                    allowCheckBox.setValue(false);
                }
                itemChanged = false;
            }
        });

        allowCheckBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (!itemChanged) {
                    itemChanged = true;

                    if (value == Boolean.TRUE)
                        markItemPermission(PermissionVariant.ALLOWED);
                    else
                        markItemPermission(PermissionVariant.NOTSET);

                    disallowCheckBox.setValue(false);

                    itemChanged = false;
                }
            }
        });

        disallowCheckBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (!itemChanged) {
                    itemChanged = true;

                    if (value == Boolean.TRUE)
                        markItemPermission(PermissionVariant.DISALLOWED);
                    else
                        markItemPermission(PermissionVariant.NOTSET);

                    allowCheckBox.setValue(false);

                    itemChanged = false;
                }
            }
        });
    }

    private void markItemPermission(PermissionVariant permissionVariant) {
        PermissionTarget target = screenPermissionsTree.getSingleSelected();
        if (target != null) {
            int value = 0;
            target.setPermissionVariant(permissionVariant);
            if (permissionVariant != PermissionVariant.NOTSET) {
                // Create permission
                switch (permissionVariant) {
                    case ALLOWED:
                        value = PermissionValue.ALLOW.getValue();
                        break;

                    case DISALLOWED:
                        value = PermissionValue.DENY.getValue();
                        break;
                }
                createPermissionItem("screenPermissionsDs", target, PermissionType.SCREEN, value);
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

    protected void createPermissionItem(String dsName, PermissionTarget target, PermissionType type, Integer value) {
        final CollectionDatasource<Permission, UUID> ds = getDsContext().get(dsName);
        final Collection<UUID> permissionIds = ds.getItemIds();

        Permission permission = null;
        for (UUID id : permissionIds) {
            Permission p = ds.getItem(id);
            if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
                permission = p;
                break;
            }
        }

        if (permission == null) {
            final Permission newPermission = new Permission();
            newPermission.setRole(roleDs.getItem());
            newPermission.setTarget(target.getPermissionValue());
            newPermission.setType(type);
            newPermission.setValue(value);

            ds.addItem(newPermission);
        } else {
            permission.setValue(value);
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
                    if (item instanceof PermissionTarget) {
                        PermissionVariant permissionVariant = ((PermissionTarget) item).getPermissionVariant();
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