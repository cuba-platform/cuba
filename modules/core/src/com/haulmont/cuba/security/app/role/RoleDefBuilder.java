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
@Component(RoleDefBuilder.NAME)
@Scope("prototype")
public class RoleDefBuilder {

    public static final String NAME = "cuba_RoleDefBuilder";

    private EntityAccessPermissions entityAccessPermissions;
    private EntityAttributeAccessPermissions entityAttributeAccessPermissions;
    private SpecificPermissions specificPermissions;
    private ScreenPermissions screenPermissions;
    private ScreenElementsPermissions screenElementsPermissions;
    private RoleType roleType;
    private String roleName;
    private String description;


    /**
     * INTERNAL
     */
    private RoleDefBuilder() {
        entityAccessPermissions = new EntityAccessPermissions();
        entityAttributeAccessPermissions = new EntityAttributeAccessPermissions();
        specificPermissions = new SpecificPermissions();
        screenPermissions = new ScreenPermissions();
        screenElementsPermissions = new ScreenElementsPermissions();
        roleType = RoleType.STANDARD;
        description = "";
    }

    /**
     * INTERNAL
     */
    private RoleDefBuilder(RoleType roleType) {
        this();
        this.roleType = roleType;
    }

    /**
     * INTERNAL
     */
    private RoleDefBuilder(Role role) {
        this();
        this.roleType = role.getType();
        this.roleName = role.getName();
        this.description = role.getDescription();
        join(role);
    }

    /**
     * INTERNAL
     */
    private RoleDefBuilder(RoleDef role) {
        this();
        this.roleType = role.getRoleType();
        this.roleName = role.getName();
        this.description = role.getDescription();
        join(role);
    }

    /**
     * @return new builder with default role parameters: {@code roleType = RoleType.STANDARD},
     * {@code description} and {@code name} are empty.
     */
    public static RoleDefBuilder createRole() {
        return AppBeans.getPrototype(NAME);
    }

    /**
     * @param roleType {@link RoleType}, default value is {@code RoleType.STANDARD}
     * @return new builder with empty role name and description
     */
    public static RoleDefBuilder createRole(RoleType roleType) {
        return AppBeans.getPrototype(NAME, roleType);
    }

    /**
     * @param role source {@link Role} object. Following parameters are taken from this role: name,
     *             description, roleType, permissions
     * @return new builder
     */
    public static RoleDefBuilder createRole(Role role) {
        return AppBeans.getPrototype(NAME, role);
    }

    /**
     * @param role source {@link RoleDef} object. Following parameters are taken from this role: name,
     *             description, roleType, permissions
     * @return new builder
     */
    public static RoleDefBuilder createRole(RoleDef role) {
        return AppBeans.getPrototype(NAME, role);
    }

    /**
     * Specifies the name of the role.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder withName(String name) {
        this.roleName = name;

        return this;
    }

    /**
     * Specifies the description of the role.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder withDescription(String description) {
        this.description = description;

        return this;
    }

    /**
     * Specifies the role type.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder withRoleType(RoleType roleType) {
        this.roleType = roleType;
        return this;
    }

    protected RoleDefBuilder withPermission(Permission permission) {
        joinPermission(permission);

        return this;
    }

    protected RoleDefBuilder withPermission(PermissionType permissionType, String target, int access) {
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
    public RoleDefBuilder withEntityAccessPermission(MetaClass targetClass, EntityOp operation, Access access) {
        return withPermission(PermissionType.ENTITY_OP,
                targetClass.getName() + Permission.TARGET_PATH_DELIMETER + operation.getId(),
                access.getId());
    }

    /**
     * Adds permission to access one of the entity properties.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder withEntityAttrAccessPermission(MetaClass targetClass, String property, EntityAttrAccess access) {
        return withPermission(PermissionType.ENTITY_ATTR,
                targetClass.getName() + Permission.TARGET_PATH_DELIMETER + property,
                access.getId());
    }

    /**
     * Adds permission to access one of the specific permissions.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder withSpecificPermission(String target, Access access) {
        return withPermission(PermissionType.SPECIFIC, target, access.getId());
    }

    /**
     * Adds permission to access one of the screens.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder withScreenPermission(String windowAlias, Access access) {
        return withPermission(PermissionType.SCREEN, windowAlias, access.getId());
    }

    /**
     * Adds permission to access one of the screen elements.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder withScreenElementPermission(String windowAlias, String component, Access access) {
        return withPermission(PermissionType.UI,
                windowAlias + Permission.TARGET_PATH_DELIMETER + component,
                access.getId());
    }

    /**
     * Adds all role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder join(RoleDef role) {
        joinApplicationRole(role);
        joinGenericUiRole(role);

        return this;
    }

    /**
     * Adds entityAccess, entityAttributeAccess and specific role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder join(ApplicationRole role) {
        joinApplicationRole(role);

        return this;
    }

    /**
     * Adds screen and screenElements role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder join(GenericUiRole role) {
        joinGenericUiRole(role);

        return this;
    }

    /**
     * Adds all role permissions to the constructed role.
     *
     * @return current instance of the builder
     */
    public RoleDefBuilder join(Role role) {
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
    public RoleDef build() {
        return new BasicUserRoleDef(roleName, description, roleType, entityAccessPermissions,
                entityAttributeAccessPermissions, specificPermissions, screenPermissions, screenElementsPermissions);
    }


    protected void joinApplicationRole(ApplicationRole role) {
        addPermissions(entityAccessPermissions, getPermissions(role.entityAccess()));
        addPermissions(entityAttributeAccessPermissions, getPermissions(role.attributeAccess()));
        addPermissions(specificPermissions, getPermissions(role.specificPermissions()));
    }

    protected void joinGenericUiRole(GenericUiRole role) {
        addPermissions(screenPermissions, getPermissions(role.screenAccess()));
        addPermissions(screenElementsPermissions, getPermissions(role.screenElementsAccess()));
    }

    protected void joinPermission(Permission permission) {
        switch (permission.getType()) {
            case ENTITY_OP:
                addPermission(entityAccessPermissions, permission);
                break;
            case ENTITY_ATTR:
                addPermission(entityAttributeAccessPermissions, permission);
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
