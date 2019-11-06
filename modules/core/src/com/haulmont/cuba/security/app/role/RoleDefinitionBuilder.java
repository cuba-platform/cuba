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

package com.haulmont.cuba.security.app.role;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.haulmont.cuba.security.role.PermissionsUtils.*;

/**
 * Builder class that helps to create custom predefined roles.
 */
@Component(RoleDefinitionBuilder.NAME)
@Scope("prototype")
public class RoleDefinitionBuilder {

    public static final String NAME = "cuba_RoleDefinitionBuilder";

    private EntityPermissions entityPermissions;
    private EntityAttributePermissions entityAttributePermissions;
    private SpecificPermissions specificPermissions;
    private ScreenPermissions screenPermissions;
    private ScreenElementsPermissions screenElementsPermissions;
    private RoleType roleType;
    private String roleName;
    private String description;


    /**
     * INTERNAL
     */
    private RoleDefinitionBuilder() {
        entityPermissions = new EntityPermissions();
        entityAttributePermissions = new EntityAttributePermissions();
        specificPermissions = new SpecificPermissions();
        screenPermissions = new ScreenPermissions();
        screenElementsPermissions = new ScreenElementsPermissions();
        roleType = RoleType.STANDARD;
        description = "";
    }

    /**
     * @return new builder with default role parameters: {@code roleType = RoleType.STANDARD},
     * {@code description} and {@code name} are empty.
     */
    public static RoleDefinitionBuilder create() {
        return AppBeans.getPrototype(NAME);
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
     * Specifies the role type.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withRoleType(RoleType roleType) {
        this.roleType = roleType;
        return this;
    }

    protected RoleDefinitionBuilder withPermission(Permission permission) {
        joinPermission(permission);

        return this;
    }

    protected RoleDefinitionBuilder withPermission(PermissionType permissionType, String target, int access) {
        Permission permission = new Permission();
        permission.setType(permissionType);
        permission.setValue(access);
        permission.setTarget(target);

        joinPermission(permission);

        return this;
    }

    /**
     * Adds permission to access one of the entity operations.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withEntityAccessPermission(MetaClass targetClass, EntityOp operation, Access access) {
        return withPermission(PermissionType.ENTITY_OP,
                targetClass.getName() + Permission.TARGET_PATH_DELIMETER + operation.getId(),
                access.getId());
    }

    /**
     * Adds permission to access one of the entity properties.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder withEntityAttrAccessPermission(MetaClass targetClass, String property, EntityAttrAccess access) {
        return withPermission(PermissionType.ENTITY_ATTR,
                targetClass.getName() + Permission.TARGET_PATH_DELIMETER + property,
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
                windowAlias + Permission.TARGET_PATH_DELIMETER + component,
                access.getId());
    }

    /**
     * Adds all role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder join(RoleDefinition role) {
        joinApplicationRole(role);
        joinGenericUiRole(role);

        return this;
    }

    /**
     * Adds entityAccess, entityAttributeAccess and specific role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder join(ApplicationRole role) {
        joinApplicationRole(role);

        return this;
    }

    /**
     * Adds screen and screenElements role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder join(GenericUiRole role) {
        joinGenericUiRole(role);

        return this;
    }

    /**
     * Adds all role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefinitionBuilder join(Role role) {
        if (role.getPermissions() != null) {
            for (Permission permission : role.getPermissions()) {
                joinPermission(permission);
            }
        }
        return this;
    }

    /**
     * Returns the built role
     */
    public RoleDefinition build() {
        return new BasicRoleDefinition(roleName, description, roleType, entityPermissions,
                entityAttributePermissions, specificPermissions, screenPermissions, screenElementsPermissions);
    }


    protected void joinApplicationRole(ApplicationRole role) {
        addPermissions(entityPermissions, getPermissions(role.entityPermissions()));
        addPermissions(entityAttributePermissions, getPermissions(role.entityAttributePermissions()));
        addPermissions(specificPermissions, getPermissions(role.specificPermissions()));
    }

    protected void joinGenericUiRole(GenericUiRole role) {
        addPermissions(screenPermissions, getPermissions(role.screenPermissions()));
        addPermissions(screenElementsPermissions, getPermissions(role.screenElementsPermissions()));
    }

    protected void joinPermission(Permission permission) {
        switch (permission.getType()) {
            case ENTITY_OP:
                addPermission(entityPermissions, permission);
                break;
            case ENTITY_ATTR:
                addPermission(entityAttributePermissions, permission);
                break;
            case SPECIFIC:
                addPermission(specificPermissions, permission);
                break;
            case SCREEN:
                addPermission(screenPermissions, permission);
                break;
            case UI:
                addPermission(screenElementsPermissions, permission);
                break;
            default:
                throw new IllegalArgumentException("Unsupported permission type.");
        }
    }
}
