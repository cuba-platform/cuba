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

package com.haulmont.cuba.security.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;

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
    private ScreenElementsPermissionsContainer screenElementsPermissions;
    private String roleName;
    private String description;
    private String securityScope;

    /**
     * INTERNAL
     */
    private RoleDefinitionBuilder() {
        entityPermissions = new EntityPermissionsContainer();
        entityAttributePermissions = new EntityAttributePermissionsContainer();
        specificPermissions = new SpecificPermissionsContainer();
        screenPermissions = new ScreenPermissionsContainer();
        screenElementsPermissions = new ScreenElementsPermissionsContainer();
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
                PermissionsUtils.getScreenElementTarget(windowAlias, component),
                access.getId());
    }

    public RoleDefinitionBuilder withDefaultScreenAccess(Access defaultAccess) {
        this.screenPermissions.setDefaultScreenAccess(defaultAccess);
        return this;
    }

    public RoleDefinitionBuilder withDefaultEntityCreateAccess(Access defaultAccess) {
        this.entityPermissions.setDefaultEntityCreateAccess(defaultAccess);
        return this;
    }

    public RoleDefinitionBuilder withDefaultEntityReadAccess(Access defaultAccess) {
        this.entityPermissions.setDefaultEntityReadAccess(defaultAccess);
        return this;
    }

    public RoleDefinitionBuilder withDefaultEntityUpdateAccess(Access defaultAccess) {
        this.entityPermissions.setDefaultEntityUpdateAccess(defaultAccess);
        return this;
    }

    public RoleDefinitionBuilder withDefaultEntityDeleteAccess(Access defaultAccess) {
        this.entityPermissions.setDefaultEntityDeleteAccess(defaultAccess);
        return this;
    }

    public RoleDefinitionBuilder withDefaultEntityAttributeAccess(EntityAttrAccess defaultAccess) {
        this.entityAttributePermissions.setDefaultEntityAttributeAccess(defaultAccess);
        return this;
    }

    public RoleDefinitionBuilder withDefaultSpecificAccess(Access defaultAccess) {
        this.specificPermissions.setDefaultSpecificAccess(defaultAccess);
        return this;
    }

    /**
     * Returns the built role
     */
    public RoleDefinition build() {
        return BasicRoleDefinition.builder()
                .withName(roleName)
                .withDescription(description)
                .withSecurityScope(securityScope)
                .withEntityPermissions(entityPermissions)
                .withEntityAttributePermissions(entityAttributePermissions)
                .withSpecificPermissions(specificPermissions)
                .withScreenPermissions(screenPermissions)
                .withScreenElementsPermissions(screenElementsPermissions)
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
