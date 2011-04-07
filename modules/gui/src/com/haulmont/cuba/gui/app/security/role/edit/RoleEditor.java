/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
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

    public interface Companion {
        void initTable(Table table);
    }

    public RoleEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        initPermissionControls(
                "sec$Target.screenPermissions.lookup",
                "screen-permissions",
                PermissionType.SCREEN);
        table = getComponent("screen-permissions");
        screenPermissionsGrant = getComponent("screen-permissions-grant");
        screenPermissionsGrant.addAction(table.getAction("allow"));
        screenPermissionsGrant.addAction(table.getAction("deny"));
        if(!PersistenceHelper.isNew((Role)params.get("item"))){
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
        Companion companion = getCompanion();
        if (companion != null) {
            companion.initTable(table);
        }
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
            if (ObjectUtils.equals(p.getTarget(), target.getValue())) {
                permission = p;
                break;
            }
        }

        if (permission == null) {
            @SuppressWarnings({"unchecked"})
            final Datasource<Role> roleDs = getDsContext().get("role");

            final Permission newPermission = new Permission();
            newPermission.setRole(roleDs.getItem());
            newPermission.setTarget(target.getValue());
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
