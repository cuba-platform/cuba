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

import com.haulmont.cuba.security.entity.*;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Helps to work with predefined roles in the user interface.
 */
public interface RolesService {

    String NAME = "cuba_RolesService";

    /**
     * Returns a collection of all roles ({@link Role} objects) available in the application ({@link
     * SecurityStorageMode} is taken into account). If there are predefined roles ({@link RoleDefinition} objects in the
     * source code), they will be converted to {@link Role} and will also be returned in the result collection. The
     * resulting roles permissions collections are not filled.
     *
     * @return list of all roles ({@link Role} objects) available in the application.
     */
    Collection<Role> getAllRoles();

    @Nullable
    RoleDefinition getRoleDefinitionByName(String name);

    /**
     * Finds the predefined role definition by the name and converts it to the {@link Role} object.
     *
     * @param roleDefinitionName name of a predefined role
     * @return {@code Role} object that contains all permissions of a predefined role or null if role definition is not
     * found.
     */
    @Nullable
    Role getRoleDefinitionAndTransformToRole(String roleDefinitionName);

    /**
     * Transforms the given {@code roleDefinition} to the {@link Role} object. By default, all permissions will also be
     * transformed, if you don't need them in the resulting role, use the {@link RoleTransformationOption#DO_NOT_INCLUDE_PERMISSIONS}
     * option.
     */
    Role transformToRole(RoleDefinition roleDefinition, RoleTransformationOption... transformationOptions);

    /**
     * Finds a predefined role definition by the name and builds a collection of {@link Permission} objects that has a
     * given {@code permissionType}.
     *
     * @param predefinedRoleName name of a predefined role
     * @param permissionType     type of permissions that should be returned
     * @return collection of {@code Permission} objects or an empty collection if the predefined role doesn't exist
     */
    Collection<Permission> getPermissions(String predefinedRoleName, PermissionType permissionType);

    /**
     * @return {@code true} if roles are stored in a source code and in a database, {@code false} otherwise. See {@link
     * SecurityStorageMode}
     */
    boolean isRoleStorageMixedMode();

    /**
     * Returns all roles marked as default (both from the database and from the source code).
     */
    Collection<Role> getDefaultRoles();

    /**
     * Returns a collection of {@link Role} objects assigned to the {@code User}. If the user role is associated with a
     * predefined role definition, the predefined role will be converted to the {@code Role}
     *
     * @return collection of {@link Role} objects
     */
    Collection<Role> getRolesForUser(User user);

    /**
     * Returns a collection of {@link RoleDefinition} objects assigned to the {@code User}. If the user role is
     * associated with the database role, the database role will be converted to the {@code RoleDefinition}
     *
     * @return collection of {@link RoleDefinition} objects
     */
    Collection<RoleDefinition> getRoleDefinitionsForUser(User user);

    /**
     * Returns a policy for resolving permission values that are not explicitly defined in roles. For roles policy v1
     * {@link #getRolesPolicyVersion()} if a role doesn't define any explicit permission then this target is allowed,
     * for policy v2 the undefined permission is denied.
     */
    Access getPermissionUndefinedAccessPolicy();

    /**
     * Returns the roles policy version.
     * <ul>
     *     <li>1 - Security implementation used before CUBA 7.2: undefined permissions are resolved to allowed, roles
     *     types are used</li>
     *     <li>2 - New roles resolving implementation introduced in CUBA 7.2: undefined permissions are resolved to
     *     denied, the only possible permission is ALLOW (user cannot select DENY), design-time roles can be used</li>
     * </ul>
     */
    int getRolesPolicyVersion();
}
