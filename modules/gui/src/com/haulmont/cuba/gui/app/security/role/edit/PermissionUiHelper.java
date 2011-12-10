/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class PermissionUiHelper {

    public static void createPermissionItem(CollectionDatasource<Permission, UUID> ds, Datasource<Role> roleDs,
                                            String permissionValue, PermissionType type, Integer value) {
        final Collection<UUID> permissionIds = ds.getItemIds();

        Permission permission = null;
        for (UUID id : permissionIds) {
            Permission p = ds.getItem(id);
            if (ObjectUtils.equals(p.getTarget(), permissionValue)) {
                permission = p;
                break;
            }
        }

        if (permission == null) {
            final Permission newPermission = new Permission();
            newPermission.setRole(roleDs.getItem());
            newPermission.setTarget(permissionValue);
            newPermission.setType(type);
            newPermission.setValue(value);

            ds.addItem(newPermission);
        } else {
            permission.setValue(value);
        }
    }
}