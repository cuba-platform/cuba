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
import java.util.Map;

/**
 * INTERNAL
 */
public final class PermissionsUtils {

    private PermissionsUtils() {
    }

    public static String getEntityOperationTarget(MetaClass metaClass, EntityOp entityOp) {
        return getEntityOperationTarget(metaClass.getName(), entityOp);
    }

    public static String getEntityOperationTarget(String entityName, EntityOp entityOp) {
        return entityName + Permission.TARGET_PATH_DELIMETER + entityOp.getId();
    }

    public static String getEntityAttributeTarget(MetaClass metaClass, String property) {
        return getEntityAttributeTarget(metaClass.getName(), property);
    }

    public static String getEntityAttributeTarget(String entityName, String property) {
        return entityName + Permission.TARGET_PATH_DELIMETER + property;
    }

    public static String getScreenComponentTarget(String screenId, String component) {
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
                return role.screenComponentPermissions();
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
            MetaClass metaClass = metadata.getClass(entityName);
            if (metaClass != null) {
                Class extendedClass = metadata.getExtendedEntities().getExtendedClass(metaClass);
                if (extendedClass != null) {
                    MetaClass extMetaClass = metadata.getClassNN(extendedClass);
                    return extMetaClass.getName() + Permission.TARGET_PATH_DELIMETER + target.substring(pos + 1);
                }
            }
        }
        return null;
    }

    /**
     * Method returns a resulting permission value, trying to find a value in the following order:
     * <ul>
     *     <li>explicit permission value in the role definition</li>
     *     <li>wildcard permission value in the role definition</li>
     *     <li>a value used for undefined permissions (based on cuba.security.rolesPolicyVersion application property)</li>
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
            permissionValue = getWildcardPermissionValue(permissionsContainer, target);
        }
        if (permissionValue == null) {
            permissionValue = getPermissionUndefinedAccessValue(type, permissionUndefinedAccessPolicy);
        }
        return permissionValue;
    }

    @Nullable
    public static Integer getWildcardPermissionValue(PermissionsContainer permissionsContainer,
                                                     String target) {
        Integer permissionValue = null;
        Map<String, Integer> explicitPermissions = permissionsContainer.getExplicitPermissions();
        if (permissionsContainer instanceof EntityPermissionsContainer) {
            String[] split = target.split(":");
            if (split.length == 2) {
                //e.g. *:create
                String wildcardTarget = "*:" + split[1];
                permissionValue = explicitPermissions.get(wildcardTarget);
            }
        } else if (permissionsContainer instanceof EntityAttributePermissionsContainer) {
            String[] split = target.split(":");
            if (split.length == 2) {
                //e.g. sec$User:*
                String wildcardTarget = split[0] + ":*";
                permissionValue = explicitPermissions.get(wildcardTarget);
                if (permissionValue == null) {
                    permissionValue = explicitPermissions.get("*:*");
                }
            }
        } else if (permissionsContainer instanceof ScreenPermissionsContainer) {
            permissionValue = explicitPermissions.get("*");
        } else if (permissionsContainer instanceof SpecificPermissionsContainer) {
            permissionValue = explicitPermissions.get("*");
        }
        return permissionValue;
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
