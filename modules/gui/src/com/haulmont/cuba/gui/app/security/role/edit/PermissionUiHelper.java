/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.app.security.ds.RestorablePermissionDatasource;
import com.haulmont.cuba.gui.app.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionVariant;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import org.apache.commons.lang.ObjectUtils;

import java.util.UUID;

/**
 */
public final class PermissionUiHelper {

    private PermissionUiHelper() {
    }

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
     * @param roleDs Role datasource
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

                permission = datasource.findRemovedEntity(p ->
                    p != null && ObjectUtils.equals(p.getTarget(), permissionTarget)
                );
                if (permission != null) {
                    datasource.restoreEntity(permission);
                }
            }
        }

        if (permission == null) {
            Metadata metadata = AppBeans.get(Metadata.NAME);

            Permission newPermission = metadata.create(Permission.class);
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