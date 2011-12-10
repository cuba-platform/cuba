/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.ScreenPermissionsFrame;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.ui.BasicPermissionTarget;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.*;

public class RoleEditor extends AbstractEditor {

    private Set<String> initialized = new HashSet<String>();
//    private PopupButton entityPermissionsGrant;
    private PopupButton propertyPermissionsGrant;
    private PopupButton specificPermissionsGrant;

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private ScreenPermissionsFrame screensTabFrame;

    public RoleEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        screensTabFrame.setItem();

        if (!PersistenceHelper.isNew(item)) {
            getComponent("name").setEnabled(false);
        }
    }

    @Override
    public void init(Map<String, Object> params) {
        Tabsheet tabsheet = getComponent("permissions-types");
        tabsheet.addListener(new PermissionTabChangeListener());
    }

    private void hideMenuPopupButton() {
//        if (entityPermissionsGrant != null)
//            entityPermissionsGrant.setPopupVisible(false);
        if (propertyPermissionsGrant != null)
            propertyPermissionsGrant.setPopupVisible(false);
        if (specificPermissionsGrant != null)
            specificPermissionsGrant.setPopupVisible(false);

    }

    protected void initPermissionControls(final String lookupAction, final String permissionsStorage,
                                          final PermissionType permissionType) {
        if (initialized.contains(permissionsStorage))
            return;
        initialized.add(permissionsStorage);

        final Table table = getComponent(permissionsStorage);
        table.getDatasource().refresh();
        table.setMultiSelect(true);

        if (permissionType != PermissionType.ENTITY_ATTR) {
            table.addAction(new OpenPermissionAction("allow", lookupAction, permissionsStorage,
                    permissionType, PermissionValue.ALLOW.name(), PermissionValue.ALLOW.getValue()));
            table.addAction(new OpenPermissionAction("deny", lookupAction, permissionsStorage,
                    permissionType, PermissionValue.DENY.name(), PermissionValue.DENY.getValue()));
        } else {
            table.addAction(new OpenPermissionAction("modify", lookupAction, permissionsStorage,
                    permissionType, PropertyPermissionValue.MODIFY.name(), PropertyPermissionValue.MODIFY.getValue()));
            table.addAction(new OpenPermissionAction("view", lookupAction, permissionsStorage,
                    permissionType, PropertyPermissionValue.VIEW.name(), PropertyPermissionValue.VIEW.getValue()));
            table.addAction(new OpenPermissionAction("forbid", lookupAction, permissionsStorage,
                    permissionType, "FORBID", PropertyPermissionValue.DENY.getValue()));
        }

        table.addAction(new RemoveAction(table, false));

        initTableColumns(permissionsStorage);
    }

    protected void initTableColumns(String tableId) {
        final Table table = getComponent(tableId);

        table.addGeneratedColumn(
                "target",
                new Table.ColumnGenerator() {
                    @Override
                    public Component generateCell(Table table, Object itemId) {
                        Permission permission = (Permission) table.getDatasource().getItem(itemId);
                        if (permission.getTarget() == null)
                            return null;
                        Label label = AppConfig.getFactory().createComponent(Label.NAME);
                        if (permission.getType().equals(PermissionType.SCREEN)) {
                            String id = permission.getTarget();
                            String caption = MenuConfig.getMenuItemCaption(id.substring(id.indexOf(":") + 1));
                            label.setValue(id + " (" + caption + ")");
                        } else {
                            label.setValue(permission.getTarget());
                        }
                        return label;
                    }
                }
        );

        table.addGeneratedColumn(
                "value",
                new Table.ColumnGenerator() {
                    @Override
                    public Component generateCell(Table table, Object itemId) {
                        Permission permission = (Permission) table.getDatasource().getItem(itemId);
                        if (permission.getValue() == null)
                            return null;
                        Label label = AppConfig.getFactory().createComponent(Label.NAME);
                        if (permission.getType().equals(PermissionType.ENTITY_ATTR)) {
                            if (permission.getValue() == 0)
                                label.setValue(frame.getMessage("PropertyPermissionValue.DENY"));
                            else if (permission.getValue() == 1)
                                label.setValue(frame.getMessage("PropertyPermissionValue.VIEW"));
                            else
                                label.setValue(frame.getMessage("PropertyPermissionValue.MODIFY"));
                        } else {
                            if (permission.getValue() == 0)
                                label.setValue(frame.getMessage("PermissionValue.DENY"));
                            else
                                label.setValue(frame.getMessage("PermissionValue.ALLOW"));
                        }
                        return label;
                    }
                }
        );
    }

    protected void createPermissionItem(String dsName, BasicPermissionTarget target, PermissionType type, Integer value) {
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

    protected class OpenPermissionAction extends AbstractAction {
        private String lookupAction;
        private String permissionsStorage;
        private PermissionType permissionType;
        private String name;
        private int value;

        public OpenPermissionAction(String id, String lookupAction, String permissionsStorage,
                                    PermissionType permissionType, String name, int value) {
            super(id);
            this.lookupAction = lookupAction;
            this.permissionsStorage = permissionsStorage;
            this.permissionType = permissionType;
            this.name = name;
            this.value = value;
        }

        @Override
        public void actionPerform(Component component) {
            final PermissionsLookup permissionsLookup = openLookup(lookupAction, null, WindowManager.OpenType.THIS_TAB,
                    Collections.<String, Object>singletonMap("param$PermissionValue", name));
            permissionsLookup.setLookupHandler(new Lookup.Handler() {
                @Override
                public void handleLookup(Collection items) {
                    @SuppressWarnings({"unchecked"})
                    Collection<BasicPermissionTarget> targets =
                            Collections.checkedCollection(items, BasicPermissionTarget.class);

                    for (BasicPermissionTarget target : targets) {
                        createPermissionItem(permissionsStorage, target, permissionType, value);
                    }
                }
            });
            permissionsLookup.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    hideMenuPopupButton();
                }
            });
        }

        @Override
        public String getCaption() {
            if (permissionType != PermissionType.ENTITY_ATTR)
                return MessageProvider.getMessage(getClass(), "PermissionValue." + name);
            else
                return MessageProvider.getMessage(getClass(), "PropertyPermissionValue." + name);
        }
    }

    private class PermissionTabChangeListener implements Tabsheet.TabChangeListener {
        @Override
        public void tabChanged(Tabsheet.Tab newTab) {
//            if ("entityPermissionsTab".equals(newTab.getName())) {
//                if (!initialized.contains("entityPermissionsTab")) {
//                    EntityPermissionsFrame entitiesTabFrame = getComponent("entitiesTabFrame");
//
//                    entitiesTabFrame.init(Collections.<String, Object>emptyMap());
//                    initialized.add("entityPermissionsTab");
//                }
//
//                initPermissionControls(
//                        "sec$Target.entityPermissions.lookup",
//                        "entitiesTabFrame.entityPermissionsTable",
//                        PermissionType.ENTITY_OP);
//                Table table = getComponent("entitiesTabFrame.entityPermissionsTable");
//                entityPermissionsGrant = getComponent("entitiesTabFrame.entityPermissionsGrant");
//                if (entityPermissionsGrant.getActions().isEmpty()) {
//                    entityPermissionsGrant.addAction(table.getAction("allow"));
//                    entityPermissionsGrant.addAction(table.getAction("deny"));
//                }
//            } else
            if ("propertyPermissionsTab".equals(newTab.getName())) {
                initPermissionControls(
                        "sec$Target.propertyPermissions.lookup",
                        "attributesTabFrame.propertyPermissionsTable",
                        PermissionType.ENTITY_ATTR);
                Table table = getComponent("attributesTabFrame.propertyPermissionsTable");
                propertyPermissionsGrant = getComponent("attributesTabFrame.propertyPermissionsGrant");
                if (propertyPermissionsGrant.getActions().isEmpty()) {
                    propertyPermissionsGrant.addAction(table.getAction("modify"));
                    propertyPermissionsGrant.addAction(table.getAction("view"));
                    propertyPermissionsGrant.addAction(table.getAction("forbid"));
                }
            } else if ("specificPermissionsTab".equals(newTab.getName())) {
                initPermissionControls(
                        "sec$Target.specificPermissions.lookup",
                        "specificTabFrame.specificPermissionsTable",
                        PermissionType.SPECIFIC);
                Table table = getComponent("specificTabFrame.specificPermissionsTable");
                specificPermissionsGrant = getComponent("specificTabFrame.specificPermissionsGrant");
                if (specificPermissionsGrant.getActions().isEmpty()) {
                    specificPermissionsGrant.addAction(table.getAction("allow"));
                    specificPermissionsGrant.addAction(table.getAction("deny"));
                }
            }
        }
    }
}
