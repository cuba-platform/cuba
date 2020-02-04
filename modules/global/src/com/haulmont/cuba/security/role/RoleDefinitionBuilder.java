/*
 * Copyright (c) 2008-2020 Haulmont.
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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.*;

import java.util.Collection;

/**
 * Builder class that helps to create custom predefined roles.
 */
public class RoleDefinitionBuilder {

    public static final String NAME = "cuba_RoleDefinitionBuilder";

    private EntityPermissionsContainer entityPermissions;
    private EntityAttributePermissionsContainer entityAttributePermissions;
    private SpecificPermissionsContainer specificPermissions;
    private ScreenPermissionsContainer screenPermissions;
    private ScreenComponentPermissionsContainer screenElementsPermissions;
    private String roleName;
    private String description;
    private String securityScope;
    private boolean isDefault;

    /**
     * INTERNAL
     */
    private RoleDefinitionBuilder() {
        entityPermissions = new EntityPermissionsContainer();
        entityAttributePermissions = new EntityAttributePermissionsContainer();
        specificPermissions = new SpecificPermissionsContainer();
        screenPermissions = new ScreenPermissionsContainer();
        screenElementsPermissions = new ScreenComponentPermissionsContainer();
        description = "";
    }

    /**
     * @return new builder with default role parameters: {@code description} and {@code name} are empty.
     */
    public static RoleDefinitionBuilder create() {
        return new RoleDefinitionBuilder();
    }

    /**
     * Specifies the name of the role.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withName(String name) {
        this.roleName = name;

        return this;
    }

    /**
     * Specifies the description of the role.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withDescription(String description) {
        this.description = description;

        return this;
    }

    /**
     * Specifies the security scope of the role.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withSecurityScope(String securityScope) {
        this.securityScope = securityScope;

        return this;
    }

    public RoleDefinitionBuilder withIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    protected RoleDefinitionBuilder withPermission(Permission permission) {
        addPermission(permission.getType(), permission.getTarget(), permission.getValue());
        return this;
    }

    public RoleDefinitionBuilder withPermissions(Collection<Permission> permissions) {
        if (permissions != null) {
            for (Permission permission : permissions) {
                addPermission(permission.getType(), permission.getTarget(), permission.getValue());
            }
        }
        return this;
    }

    public RoleDefinitionBuilder withPermission(PermissionType permissionType, String target, int access) {
        addPermission(permissionType, target, access);
        return this;
    }

    /**
     * Adds permission to access one of the entity operations.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withEntityAccessPermission(MetaClass targetClass, EntityOp operation, Access access) {
        return withPermission(PermissionType.ENTITY_OP,
                PermissionsUtils.getEntityOperationTarget(targetClass, operation),
                access.getId());
    }

    public RoleDefinitionBuilder withEntityAccessPermission(Class<? extends Entity> targetClass, EntityOp operation, Access access) {
        MetaClass metaClass = AppBeans.get(Metadata.class).getClassNN(targetClass);
        return withPermission(PermissionType.ENTITY_OP,
                PermissionsUtils.getEntityOperationTarget(metaClass, operation),
                access.getId());
    }

    /**
     * Adds permission to access one of the entity properties.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withEntityAttrAccessPermission(MetaClass targetClass, String property, EntityAttrAccess access) {
        return withPermission(PermissionType.ENTITY_ATTR,
                PermissionsUtils.getEntityAttributeTarget(targetClass, property),
                access.getId());
    }

    public RoleDefinitionBuilder withEntityAttrAccessPermission(Class<? extends Entity> targetClass, String property, EntityAttrAccess access) {
        MetaClass metaClass = AppBeans.get(Metadata.class).getClassNN(targetClass);
        return withPermission(PermissionType.ENTITY_ATTR,
                PermissionsUtils.getEntityAttributeTarget(metaClass, property),
                access.getId());
    }

    /**
     * Adds permission to access one of the specific permissions.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withSpecificPermission(String target, Access access) {
        return withPermission(PermissionType.SPECIFIC, target, access.getId());
    }

    /**
     * Adds permission to access one of the screens.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withScreenPermission(String windowAlias, Access access) {
        return withPermission(PermissionType.SCREEN, windowAlias, access.getId());
    }

    /**
     * Adds permission to access one of the screen elements.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withScreenElementPermission(String windowAlias, String component, Access access) {
        return withPermission(PermissionType.UI,
                PermissionsUtils.getScreenComponentTarget(windowAlias, component),
                access.getId());
    }

    /**
     * Returns the built role
     */
    public RoleDefinition build() {
        return BasicRoleDefinition.builder()
                .withName(roleName)
                .withDescription(description)
                .withSecurityScope(securityScope)
                .withEntityPermissionsContainer(entityPermissions)
                .withEntityAttributePermissionsContainer(entityAttributePermissions)
                .withSpecificPermissionsContainer(specificPermissions)
                .withScreenPermissionsContainer(screenPermissions)
                .withScreenComponentPermissionsContainer(screenElementsPermissions)
                .withIsDefault(isDefault)
                .build();
    }

    protected void addPermission(PermissionType permissionType, String target, int access) {
        switch (permissionType) {
            case ENTITY_OP:
                entityPermissions.getExplicitPermissions().put(target, access);
                break;
            case ENTITY_ATTR:
                entityAttributePermissions.getExplicitPermissions().put(target, access);
                break;
            case SPECIFIC:
                specificPermissions.getExplicitPermissions().put(target, access);
                break;
            case SCREEN:
                screenPermissions.getExplicitPermissions().put(target, access);
                break;
            case UI:
                screenElementsPermissions.getExplicitPermissions().put(target, access);
                break;
            default:
                throw new IllegalArgumentException("Unsupported permission type.");
        }
    }
}
