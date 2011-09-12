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
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.config.PermissionVariant;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.security.ScreenPermissionTreeDatasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public class RoleEditor extends AbstractEditor {

    private Set<String> initialized = new HashSet<String>();
    private Table table;
    private PopupButton screenPermissionsGrant;
    private PopupButton entityPermissionsGrant;
    private PopupButton propertyPermissionsGrant;
    private PopupButton specificPermissionsGrant;

    private CollectionDatasource<Permission, UUID> screenPermissionsGrants;
    private TreeTable screenPermissionsTree;
    private ScreenPermissionTreeDatasource screensPermissionDs;
    private java.util.List<PermissionVariant> variantList;

    public RoleEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        screenPermissionsGrants.refresh();
        screensPermissionDs.setPermissionDs(screenPermissionsGrants);
        screenPermissionsTree.refresh();
        screenPermissionsTree.expandAll();
    }

    @Override
    public void init(Map<String, Object> params) {

        screenPermissionsGrants = getDsContext().get("screen-permissions");
        screenPermissionsTree = getComponent("screen-permissions-tree");

        String lookupAction = "sec$Target.screenPermissions.lookup";

        Action allowAction = new OpenPermissionAction("allow", lookupAction, "screen-permissions",
                PermissionType.SCREEN, PermissionValue.ALLOW.name(), PermissionValue.ALLOW.getValue());

        Action denyAction = new OpenPermissionAction("deny", lookupAction, "screen-permissions",
                PermissionType.SCREEN, PermissionValue.DENY.name(), PermissionValue.DENY.getValue());

        screenPermissionsGrant = getComponent("screen-permissions-grant");
        screenPermissionsGrant.addAction(allowAction);
        screenPermissionsGrant.addAction(denyAction);
        if(!PersistenceHelper.isNew(params.get("item"))){
            getComponent("name").setEnabled(false);
        }
        Tabsheet tabsheet = getComponent("permissions-types");
        tabsheet.addListener(new Tabsheet.TabChangeListener() {
            public void tabChanged(Tabsheet.Tab newTab) {
                if ("entity-permissions-tab".equals(newTab.getName())) {
                    initPermissionControls(
                            "sec$Target.entityPermissions.lookup",
                            "entity-permissions",
                            PermissionType.ENTITY_OP);
                    table = getComponent("entity-permissions");
                    entityPermissionsGrant = getComponent("entity-permissions-grant");
                    if(entityPermissionsGrant.getActions().isEmpty()){
                        entityPermissionsGrant.addAction(table.getAction("allow"));
                        entityPermissionsGrant.addAction(table.getAction("deny"));
                    }
                } else if ("property-permissions-tab".equals(newTab.getName())) {
                    initPermissionControls(
                            "sec$Target.propertyPermissions.lookup",
                            "property-permissions",
                            PermissionType.ENTITY_ATTR);
                    table = getComponent("property-permissions");
                    propertyPermissionsGrant = getComponent("property-permissions-grant");
                    if(propertyPermissionsGrant.getActions().isEmpty()){
                        propertyPermissionsGrant.addAction(table.getAction("modify"));
                        propertyPermissionsGrant.addAction(table.getAction("view"));
                        propertyPermissionsGrant.addAction(table.getAction("forbid"));
                    }
                } else if ("specific-permissions-tab".equals(newTab.getName())) {
                    initPermissionControls(
                            "sec$Target.specificPermissions.lookup",
                            "specific-permissions",
                            PermissionType.SPECIFIC);
                    table = getComponent("specific-permissions");
                    specificPermissionsGrant = getComponent("specific-permissions-grant");
                    if(specificPermissionsGrant.getActions().isEmpty()){
                        specificPermissionsGrant.addAction(table.getAction("allow"));
                        specificPermissionsGrant.addAction(table.getAction("deny"));
                    }
                }
            }
        });

        initScreenPermissionTree();
    }

    private void initScreenPermissionTree() {
        variantList = Arrays.asList(
                PermissionVariant.ALLOWED,
                PermissionVariant.DISALLOWED,
                PermissionVariant.NOTSET);

        screensPermissionDs = getDsContext().get("screen-permissions-tree-ds");

        screenPermissionsTree.setStyleProvider(new ScreensTreeStyleProvider());

        final ValueListener<LookupField> permissionChangeListener = new ValueListener<LookupField>() {
            @Override
            public void valueChanged(LookupField source, String property, Object prevValue, Object value) {
                if (value != null) {
                    markItemPermission((PermissionVariant) value);
                }
            }
        };

        screenPermissionsTree.addGeneratedColumn("permissionVariant", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(Table table,final Object itemId) {
                LookupField lookupField = AppConfig.getFactory().createComponent(LookupField.NAME);
                lookupField.setWidth("300px");
                lookupField.setOptionsList(variantList);
                lookupField.setRequired(true);

                PermissionConfig.Target target = (PermissionConfig.Target) screensPermissionDs.getItem(itemId);
                lookupField.setValue(target.getPermissionVariant());
                lookupField.addListener(permissionChangeListener);
                return lookupField;
            }
        });

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
    }

    private void markItemPermission(PermissionVariant permissionVariant) {
        PermissionConfig.Target target = screenPermissionsTree.getSingleSelected();
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
                createPermissionItem("screen-permissions", target, PermissionType.SCREEN, value);
            } else {
                // Remove permission
                Permission permission = null;
                for (UUID id : screenPermissionsGrants.getItemIds()) {
                    Permission p = screenPermissionsGrants.getItem(id);
                    if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
                        permission = p;
                        break;
                    }
                }
                if (permission != null)
                    screenPermissionsGrants.removeItem(permission);
            }
            screenPermissionsTree.refresh();
        }
    }

    private void hideMenuPopupButton(){
        if(screenPermissionsGrant != null)
            screenPermissionsGrant.setPopupVisible(false);
        if(entityPermissionsGrant != null)
            entityPermissionsGrant.setPopupVisible(false);
        if(propertyPermissionsGrant != null)
            propertyPermissionsGrant.setPopupVisible(false);
        if(specificPermissionsGrant != null)
            specificPermissionsGrant.setPopupVisible(false);

    }

    protected void initPermissionControls(final String lookupAction, final String permissionsStorage,
                                          final PermissionType permissionType)
    {
        if (initialized.contains(permissionsStorage))
            return;
        initialized.add(permissionsStorage);

        final Datasource ds = getDsContext().get(permissionsStorage);
        ds.refresh();

        final Table table = getComponent(permissionsStorage);
        table.setMultiSelect(true);

        if(permissionType != PermissionType.ENTITY_ATTR){
            table.addAction(new OpenPermissionAction("allow",lookupAction, permissionsStorage,
                                    permissionType, PermissionValue.ALLOW.name(),  PermissionValue.ALLOW.getValue()));
            table.addAction(new OpenPermissionAction("deny",lookupAction, permissionsStorage,
                                    permissionType, PermissionValue.DENY.name(),  PermissionValue.DENY.getValue()));
        } else {
            table.addAction(new OpenPermissionAction("modify",lookupAction, permissionsStorage,
                                    permissionType, PropertyPermissionValue.MODIFY.name(),  PropertyPermissionValue.MODIFY.getValue()));
            table.addAction(new OpenPermissionAction("view",lookupAction, permissionsStorage,
                                    permissionType, PropertyPermissionValue.VIEW.name(),  PropertyPermissionValue.VIEW.getValue()));
            table.addAction(new OpenPermissionAction("forbid",lookupAction, permissionsStorage,
                                    permissionType, "FORBID",  PropertyPermissionValue.DENY.getValue()));
        }

        table.addAction(new RemoveAction(table, false));

        initTableColumns(permissionsStorage);
    }

    protected void initTableColumns(String tableId) {
        final Table table = getComponent(tableId);

        table.addGeneratedColumn(
                "target",
                new Table.ColumnGenerator() {
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

    protected Set<PermissionConfig.Target> substract(
            Collection<PermissionConfig.Target> c1, Collection<PermissionConfig.Target> c2)
    {
        final HashSet<PermissionConfig.Target> res = new HashSet<PermissionConfig.Target>(c1);
        res.removeAll(c2);

        return res;
    }

    protected void createPermissionItem(String dsName, PermissionConfig.Target target, PermissionType type, Integer value) {
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
            @SuppressWarnings({"unchecked"})
            final Datasource<Role> roleDs = getDsContext().get("role");

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

    protected class OpenPermissionAction extends AbstractAction{
        private String lookupAction;
        private String permissionsStorage;
        private PermissionType permissionType;
        private String name;
        private int value;
        public OpenPermissionAction(String id,String lookupAction, String permissionsStorage,
                                    PermissionType permissionType,String name, int value){
            super(id);
            this.lookupAction = lookupAction;
            this.permissionsStorage = permissionsStorage;
            this.permissionType = permissionType;
            this.name = name;
            this.value = value;
        }
        public void actionPerform(Component component) {
            final PermissionsLookup permissionsLookup = openLookup(lookupAction, null, WindowManager.OpenType.THIS_TAB,
                    Collections.<String, Object>singletonMap("param$PermissionValue", name));
            permissionsLookup.setLookupHandler(new Lookup.Handler() {
                public void handleLookup(Collection items) {
                    @SuppressWarnings({"unchecked"})
                    Collection<PermissionConfig.Target> targets = items;
                    for (PermissionConfig.Target target : targets) {
                        createPermissionItem(permissionsStorage, target, permissionType, value);
                    }
                    if (permissionType == PermissionType.SCREEN)
                        screenPermissionsTree.refresh();
                }
            });
            permissionsLookup.addListener(new CloseListener() {
                public void windowClosed(String actionId) {
                    hideMenuPopupButton();
                }
            });
        }

        @Override
        public String getCaption() {
            if(permissionType != PermissionType.ENTITY_ATTR)
                return MessageProvider.getMessage(getClass(),"PermissionValue."+name);
            else
                return MessageProvider.getMessage(getClass(),"PropertyPermissionValue."+name);
        }
    }
}
