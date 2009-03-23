package com.haulmont.cuba.web.app.ui.security.role.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoleEditor extends AbstractEditor {
    public RoleEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        initEntityPermissionsTab();

        Tree screenPermissionsTree = getComponent("screen-permissions-tree");
        screenPermissionsTree.getDatasource().refresh();

        Tree specificPermissionsTree = getComponent("specific-permissions-tree");
        specificPermissionsTree.getDatasource().refresh();
    }

    protected void initEntityPermissionsTab() {
        final Tree entityPermissionsTree = getComponent("entity-permissions-tree");
        final OptionsGroup entityPermissions = getComponent("entity-permissions");

        final AtomicBoolean disableValueListener = new AtomicBoolean(false);

        @SuppressWarnings({"unchecked"})
        CollectionDatasource<PermissionConfig.Target, String> entityPermissionsDs =
                entityPermissionsTree.getDatasource();

        entityPermissionsDs.refresh();
        entityPermissionsDs.addListener(new DatasourceListener<PermissionConfig.Target>() {
            public void itemChanged(Datasource<PermissionConfig.Target> ds, PermissionConfig.Target prevItem, PermissionConfig.Target item) {
                try {
                    disableValueListener.set(true);
                    if (item != null) {
                        final String id = item.getId();
                        if (id.startsWith("entity:")) {
                            MetaClass metaClass = MetadataProvider.getSession().getClass(id.substring("entity:".length()));
                            Collection<PermissionConfig.Target> permissions = getPermissions(metaClass);
                            entityPermissions.setValue(permissions);
                        }
                    } else {
                        entityPermissions.setValue(Collections.emptySet());
                    }
                } finally {
                    disableValueListener.set(false);
                }
            }

            public void stateChanged(Datasource<PermissionConfig.Target> ds, Datasource.State prevState, Datasource.State state) {}
            public void valueChanged(PermissionConfig.Target source, String property, Object prevValue, Object value) {}
        });

        entityPermissions.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (disableValueListener.get()) return;
                
                @SuppressWarnings({"unchecked"})
                Collection<PermissionConfig.Target> permissions = (Collection<PermissionConfig.Target>) value;
                @SuppressWarnings({"unchecked"})
                Collection<PermissionConfig.Target> prevPermissions = (Collection<PermissionConfig.Target>) prevValue;

                if (!ObjectUtils.equals(permissions, prevPermissions)) {
                    if (permissions == null) permissions = Collections.emptySet();
                    if (prevPermissions == null) prevPermissions = Collections.emptySet();

                    final Set<PermissionConfig.Target> addPermissions = substract(permissions, prevPermissions);
                    for (PermissionConfig.Target target : addPermissions) {
                        addPermission(target, PermissionType.ENTITY_OP);
                    }

                    final Set<PermissionConfig.Target> removePermissions = substract(prevPermissions, permissions);
                    for (PermissionConfig.Target target : removePermissions) {
                        removePermission(target, PermissionType.ENTITY_OP);
                    }
                }
            }
        });

        entityPermissionsTree.expandTree();
    }

    protected Set<PermissionConfig.Target> substract(
            Collection<PermissionConfig.Target> c1, Collection<PermissionConfig.Target> c2)
    {
        final HashSet<PermissionConfig.Target> res = new HashSet<PermissionConfig.Target>(c1);
        res.removeAll(c2);

        return res;
    }

    protected Collection<PermissionConfig.Target> getPermissions(MetaClass metaClass) {
        final Set<PermissionConfig.Target> res = new HashSet<PermissionConfig.Target>();

        final CollectionDatasource<Permission, Permission> ds = getDsContext().get("permissions");
        final Collection<Permission> permissions = ds.getItemIds();

        String prefix = "entity:" + metaClass.getName();

        for (Permission permission : permissions) {
            final String target = permission.getTarget();
            if (target.startsWith(prefix)) {
                String permissionName = target.substring(prefix.length() + 1);
                res.add(new PermissionConfig.Target(target, permissionName, target));
            }
        }

        return res;
    }

    protected void addPermission(PermissionConfig.Target target, PermissionType type) {
        final CollectionDatasource<Permission, Permission> ds = getDsContext().get("permissions");
        final Collection<Permission> permissions = ds.getItemIds();

        boolean hasPermission = false;
        for (Permission permission : permissions) {
            if (ObjectUtils.equals(permission.getTarget(), target.getValue())) {
                hasPermission = true;
            }
        }

        if (!hasPermission) {
            @SuppressWarnings({"unchecked"})
            final Datasource<Role> roleDs = getDsContext().get("role");

            final Permission permission = new Permission();
            permission.setRole(roleDs.getItem());
            permission.setTarget(target.getValue());
            permission.setType(type);

            ds.addItem(permission);
        }
    }

    protected void removePermission(PermissionConfig.Target target, PermissionType type) {
        final CollectionDatasource<Permission, Permission> ds = getDsContext().get("permissions");
        final Collection<Permission> permissions = ds.getItemIds();

        for (Permission permission : new HashSet<Permission>(permissions)) {
            if (ObjectUtils.equals(permission.getTarget(), target.getValue()) &&
                    type.equals(permission.getType()))
            {
                ds.removeItem(permission);
            }
        }
    }
}
