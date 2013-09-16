/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.google.common.base.Predicate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.security.RestorablePermissionDatasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.gui.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.security.entity.UiPermissionVariant;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class PermissionUiHelper {

    public static PermissionVariant getCheckBoxVariant(Object value, PermissionVariant activeVariant) {
        PermissionVariant permissionVariant;
        if (Boolean.TRUE.equals(value))
            permissionVariant = activeVariant;
        else
            permissionVariant = PermissionVariant.NOTSET;
        return permissionVariant;
    }

    public static AttributePermissionVariant getCheckBoxVariant(Object value, AttributePermissionVariant activeVariant) {
        AttributePermissionVariant permissionVariant;
        if (Boolean.TRUE.equals(value))
            permissionVariant = activeVariant;
        else
            permissionVariant = AttributePermissionVariant.NOTSET;
        return permissionVariant;
    }

    public static int getPermissionValue(PermissionVariant permissionVariant) {
        int value = 0;
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
        }
        return value;
    }

    public static int getPermissionValue(AttributePermissionVariant permissionVariant) {
        int value = 0;
        if (permissionVariant != AttributePermissionVariant.NOTSET) {
            // Create permission
            switch (permissionVariant) {
                case MODIFY:
                    value = PropertyPermissionValue.MODIFY.getValue();
                    break;

                case HIDE:
                    value = PropertyPermissionValue.DENY.getValue();
                    break;

                case READ_ONLY:
                    value = PropertyPermissionValue.VIEW.getValue();
                    break;
            }
        }
        return value;
    }

    /**
     * Add or edit permission item in datasource
     * @param ds Datasource
     * @param roleDs Role darasource
     * @param permissionTarget Permission identifier
     * @param type Permission type
     * @param value Permission value
     */
    public static void createPermissionItem(CollectionDatasource<Permission, UUID> ds, Datasource<Role> roleDs,
                                            final String permissionTarget, PermissionType type, Integer value) {
        Permission permission = null;
        for (Permission p : ds.getItems()) {
            if (ObjectUtils.equals(p.getTarget(), permissionTarget)) {
                permission = p;
                break;
            }
        }

        if (permission == null) {
            // workaround for idx_sec_permission_unique
            // restore entity instead of create
            if (ds instanceof RestorablePermissionDatasource) {
                RestorablePermissionDatasource datasource = (RestorablePermissionDatasource) ds;

                permission = datasource.findRemovedEntity(new Predicate<Permission>() {
                    @Override
                    public boolean apply(@Nullable Permission p) {
                        if (p != null)
                            return ObjectUtils.equals(p.getTarget(), permissionTarget);
                        return false;
                    }
                });
                if (permission != null)
                    datasource.restoreEntity(permission);
            }
        }

        if (permission == null) {
            final Permission newPermission = new Permission();
            newPermission.setRole(roleDs.getItem());
            newPermission.setTarget(permissionTarget);
            newPermission.setType(type);
            newPermission.setValue(value);

            ds.addItem(newPermission);
        } else {
            permission.setValue(value);
        }
    }

    public static UiPermissionVariant getCheckBoxVariant(Object value, UiPermissionVariant activeVariant) {
        UiPermissionVariant permissionVariant;
        if (Boolean.TRUE.equals(value))
            permissionVariant = activeVariant;
        else
            permissionVariant = UiPermissionVariant.NOTSET;
        return permissionVariant;
    }

    public static int getPermissionValue(UiPermissionVariant permissionVariant) {
        int value = 0;
        if (permissionVariant != UiPermissionVariant.NOTSET) {
            // Create permission
            switch (permissionVariant) {
                case HIDE:
                    value = UiPermissionValue.HIDE.getValue();
                    break;

                case READ_ONLY:
                    value = UiPermissionValue.READ_ONLY.getValue();
                    break;

                case SHOW:
                    value = UiPermissionValue.SHOW.getValue();
                    break;
            }
        }
        return value;
    }
}