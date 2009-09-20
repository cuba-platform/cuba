package com.haulmont.cuba.web.app.ui.security.role.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.core.global.MessageProvider;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public class RoleEditor extends AbstractEditor {
    public RoleEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        initPermissionControls(
                "sec$Target.entityPermissions.lookup",
                "entity-permissions",
                PermissionType.ENTITY_OP);

        initPermissionControls(
                "sec$Target.propertyPermissions.lookup",
                "property-permissions",
                PermissionType.ENTITY_ATTR);

        initPermissionControls(
                "sec$Target.screenPermissions.lookup",
                "screen-permissions",
                PermissionType.SCREEN);

        initPermissionControls(
                "sec$Target.specificPermissions.lookup",
                "specific-permissions",
                PermissionType.SPECIFIC);
    }

    protected void initPermissionControls(final String lookupAction, final String permissionsStorage, final PermissionType permissionType) {
        final Datasource ds = getDsContext().get(permissionsStorage);
        ds.refresh();

        final Table table = getComponent(permissionsStorage);
        table.addAction(new AbstractAction("grant") {
            public void actionPerform(Component component) {
                openLookup(lookupAction, new Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        @SuppressWarnings({"unchecked"})
                        Collection<PermissionConfig.Target> targets = items;
                        for (PermissionConfig.Target target : targets) {
                            createPermissionItem(permissionsStorage, target, permissionType, 1);
                        }
                    }
                }, WindowManager.OpenType.THIS_TAB);
            }

            @Override
            public String getCaption() {
                return MessageProvider.getMessage(getClass(), "permissions.grant");
            }
        });
        table.addAction(new AbstractAction("decline") {
            public void actionPerform(Component component) {
                openLookup(lookupAction, new Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        @SuppressWarnings({"unchecked"})
                        Collection<PermissionConfig.Target> targets = items;
                        for (PermissionConfig.Target target : targets) {
                            createPermissionItem(permissionsStorage, target, permissionType, 0);
                        }
                    }
                }, WindowManager.OpenType.NEW_TAB);
            }

            @Override
            public String getCaption() {
                return MessageProvider.getMessage(getClass(), "permissions.decline");
            }
        });

        final TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createRemoveAction(false);
    }

    protected Set<PermissionConfig.Target> substract(
            Collection<PermissionConfig.Target> c1, Collection<PermissionConfig.Target> c2)
    {
        final HashSet<PermissionConfig.Target> res = new HashSet<PermissionConfig.Target>(c1);
        res.removeAll(c2);

        return res;
    }

//    protected Collection<PermissionConfig.Target> getPermissions(MetaClass metaClass) {
//        final Set<PermissionConfig.Target> res = new HashSet<PermissionConfig.Target>();
//
//        final CollectionDatasource<Permission, Permission> ds = getDsContext().get("permissions");
//        final Collection<Permission> permissions = ds.getItemIds();
//
//        String prefix = "entity:" + metaClass.getName();
//
//        for (Permission permission : permissions) {
//            final String target = permission.getTarget();
//            if (target.startsWith(prefix)) {
//                String permissionName = target.substring(prefix.length() + 1);
//                res.add(new PermissionConfig.Target(target, permissionName, target));
//            }
//        }
//
//        return res;
//    }

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

//    protected void removePermission(String dsName, PermissionConfig.Target target, PermissionType type, Integer value) {
//        final CollectionDatasource<Permission, Object> ds = getDsContext().get(dsName);
//        final Collection<Object> permissions = new HashSet<Object>(ds.getItemIds());
//
//        for (Object id : permissions) {
//            Permission permission = ds.getItem(id);
//            if (ObjectUtils.equals(permission.getTarget(), target.getValue()) &&
//                    type.equals(permission.getType()))
//            {
//                ds.removeItem(permission);
//            }
//        }
//    }
}
