/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.security.role;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * INTERNAL
 */
public final class PermissionsUtils {

    private PermissionsUtils() {
    }

    public static Map<String, Integer> getPermissions(Permissions permissions) {
        return Collections.unmodifiableMap(permissions.getPermissions());
    }

    public static Integer getPermissionValue(Permissions permissions, String target) {
        return permissions.getPermissions().get(target);
    }

    public static void addPermission(Permissions permissions, Permission permission) {
        checkPermission(permissions, permission);

        addPermissionWithoutCheck(permissions, permission);
    }

    public static void addPermission(Permissions permissions, String target, @Nullable String extTarget, int value) {
        Integer currentValue = permissions.getPermissions().get(target);
        if (currentValue == null || currentValue < value) {
            permissions.getPermissions().put(target, value);
            if (extTarget != null)
                permissions.getPermissions().put(extTarget, value);
        }
    }

    public static void addPermissions(Permissions permissionsObj, Collection<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        for (Permission p : permissions) {
            checkPermission(permissionsObj, p);
        }
        for (Permission p : permissions) {
            addPermissionWithoutCheck(permissionsObj, p);
        }
    }

    public static void addPermissions(Permissions permissionsObj, Map<String, Integer> permissions) {
        for (Map.Entry<String, Integer> entry : permissions.entrySet()) {
            addPermission(permissionsObj, entry.getKey(), null, entry.getValue());
        }
    }

    public static void removePermission(Permissions permissions, String target) {
        permissions.getPermissions().remove(target);
    }

    public static void removePermissions(Permissions permissions) {
        permissions.getPermissions().clear();
    }

    public static boolean isReadOperationPermitted(EntityPermissions permissions, MetaClass metaClass) {
        return getPermissionValue(permissions, getEntityOperationTarget(metaClass, EntityOp.READ)) > 0;
    }

    public static boolean isCreateOperationPermitted(EntityPermissions permissions, MetaClass metaClass) {
        return getPermissionValue(permissions, getEntityOperationTarget(metaClass, EntityOp.CREATE)) > 0;
    }

    public static boolean isUpdateOperationPermitted(EntityPermissions permissions, MetaClass metaClass) {
        return getPermissionValue(permissions, getEntityOperationTarget(metaClass, EntityOp.UPDATE)) > 0;
    }

    public static boolean isDeleteOperationPermitted(EntityPermissions permissions, MetaClass metaClass) {
        return getPermissionValue(permissions, getEntityOperationTarget(metaClass, EntityOp.DELETE)) > 0;
    }

    public static boolean isAttributeReadOperationPermitted(EntityAttributePermissions permissions, MetaClass metaClass, String property) {
        return getPermissionValue(permissions, getEntityAttributeTarget(metaClass, property)) > 0;
    }

    public static boolean isAttributeModifyOperationPermitted(EntityAttributePermissions permissions, MetaClass metaClass, String property) {
        return getPermissionValue(permissions, getEntityAttributeTarget(metaClass, property)) > 1;
    }

    public static boolean isScreenAccessPermitted(ScreenPermissions permissions, String screenId) {
        return getPermissionValue(permissions, screenId) > 0;
    }

    public static boolean isScreenElementPermitted(ScreenElementsPermissions permissions, String screenId, String elementId) {
        return getPermissionValue(permissions, getScreenElementTarget(screenId, elementId)) > 0;
    }

    public static boolean isSpecificAccessPermitted(SpecificPermissions permissions, String specificPermission) {
        return getPermissionValue(permissions, specificPermission) > 0;
    }

    private static void checkPermission(Permissions p, Permission permission) {
        PermissionType permissionType = getPermissionType(p);
        if (permissionType == null) {
            throw new IllegalArgumentException("Unknown permissionType");
        }
        if (permission == null || !permissionType.equals(permission.getType())){
            throw new IllegalArgumentException("Permission type must be " + permissionType.name());
        }
    }

    private static void addPermissionWithoutCheck(Permissions p, Permission permission) {
        Integer currentValue = p.getPermissions().get(permission.getTarget());
        if (currentValue == null || currentValue < permission.getValue()) {
            p.getPermissions().put(permission.getTarget(), permission.getValue());
        }
    }

    private static PermissionType getPermissionType(Permissions permissions) {
        if (permissions instanceof EntityPermissions) {
            return PermissionType.ENTITY_OP;
        } else if (permissions instanceof EntityAttributePermissions) {
            return PermissionType.ENTITY_ATTR;
        } else if (permissions instanceof ScreenPermissions) {
            return PermissionType.SCREEN;
        } else if (permissions instanceof ScreenElementsPermissions) {
            return PermissionType.UI;
        } else if (permissions instanceof SpecificPermissions) {
            return PermissionType.SPECIFIC;
        }
        return null;
    }

    public static String getEntityOperationTarget(MetaClass metaClass, EntityOp entityOp) {
        return metaClass.getName() + Permission.TARGET_PATH_DELIMETER + entityOp.getId();
    }

    public static String getEntityAttributeTarget(MetaClass metaClass, String property) {
        return metaClass.getName() + Permission.TARGET_PATH_DELIMETER + property;
    }

    public static String getScreenElementTarget(String screenId, String component) {
        return screenId + Permission.TARGET_PATH_DELIMETER + component;
    }
}
