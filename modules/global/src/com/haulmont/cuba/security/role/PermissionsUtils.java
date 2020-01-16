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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.*;

import javax.annotation.Nullable;

/**
 * INTERNAL
 */
public final class PermissionsUtils {

    private PermissionsUtils() {
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

    public static PermissionsContainer getPermissionsByType(RoleDefinition role, PermissionType permissionType) {
        switch (permissionType) {
            case ENTITY_OP:
                return role.entityPermissions();
            case ENTITY_ATTR:
                return role.entityAttributePermissions();
            case SPECIFIC:
                return role.specificPermissions();
            case SCREEN:
                return role.screenPermissions();
            case UI:
                return role.screenElementsPermissions();
            default:
                throw new IllegalArgumentException("Unsupported permission type " + permissionType);
        }
    }

    @Nullable
    public static String evaluateExtendedEntityTarget(String target) {
        Metadata metadata = AppBeans.get(Metadata.class);
        int pos = target.indexOf(Permission.TARGET_PATH_DELIMETER);
        if (pos > -1) {
            String entityName = target.substring(0, pos);
            Class extendedClass = metadata.getExtendedEntities().getExtendedClass(metadata.getClassNN(entityName));
            if (extendedClass != null) {
                MetaClass extMetaClass = metadata.getClassNN(extendedClass);
                return extMetaClass.getName() + Permission.TARGET_PATH_DELIMETER + target.substring(pos + 1);
            }
        }
        return null;
    }

    /**
     * Method returns a resulting permission value, trying to find a value in the following order:
     * <ul>
     *     <li>explicit permission value in the role definition</li>
     *     <li>default permission value in the role definition</li>
     *     <li>a value used for undefined permissions (system-level config {@code ServerConfig#getPermissionUndefinedAccessPolicy})</li>
     * </ul>
     *
     * @return an integer that represents a permission value
     */
    public static Integer getResultingPermissionValue(RoleDefinition roleDefinition,
                                                      PermissionType type,
                                                      String target,
                                                      Access permissionUndefinedAccessPolicy) {
        PermissionsContainer permissionsContainer = PermissionsUtils.getPermissionsByType(roleDefinition, type);
        Integer permissionValue = permissionsContainer.getExplicitPermissions().get(target);
        if (permissionValue == null) {
            permissionValue = getDefaultPermissionValue(roleDefinition, type, target);
        }
        if (permissionValue == null) {
            permissionValue = getPermissionUndefinedAccessValue(type, permissionUndefinedAccessPolicy);
        }
        return permissionValue;
    }

    @Nullable
    private static Integer getDefaultPermissionValue(RoleDefinition roleDefinition,
                                                     PermissionType type,
                                                     String target) {
        HasSecurityAccessValue access = null;
        switch (type) {
            case SCREEN:
                access = roleDefinition.screenPermissions().getDefaultScreenAccess();
                break;
            case ENTITY_OP:
                access = roleDefinition.entityPermissions().getDefaultAccessByTarget(target);
                break;
            case ENTITY_ATTR:
                access = roleDefinition.entityAttributePermissions().getDefaultEntityAttributeAccess();
                break;
            case SPECIFIC:
                access = roleDefinition.specificPermissions().getDefaultSpecificAccess();
                break;
            case UI:
                break;
            default:
                throw new IllegalArgumentException("Unsupported permission type " + type);
        }
        return access != null ? access.getId() : null;
    }

    private static Integer getPermissionUndefinedAccessValue(PermissionType type,
                                                             Access permissionUndefinedAccessPolicy) {
        if (permissionUndefinedAccessPolicy == Access.DENY) return Access.DENY.getId();
        switch (type) {
            case ENTITY_OP:
            case SPECIFIC:
            case SCREEN:
            case UI:
                return Access.ALLOW.getId();
            case ENTITY_ATTR:
                return EntityAttrAccess.MODIFY.getId();
            default:
                throw new IllegalArgumentException("Unsupported permission type " + type);
        }
    }
}
