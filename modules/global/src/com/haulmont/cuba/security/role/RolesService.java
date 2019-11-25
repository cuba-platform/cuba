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

import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserRole;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * Helps to work with predefined roles in the user interface.
 */
public interface RolesService {

    String NAME = "cuba_RolesService";

    /**
     * @return list of all roles ({@link Role} objects) available in the application.
     * Depending on the mode, the list contains both predefined roles and roles from the database.
     */
    Collection<Role> getAllRoles();

    /**
     * @param predefinedRoleName name of a predefined role
     * @return {@code Role} object that contains all permissions of a predefined role.
     */
    Role getRoleByName(String predefinedRoleName);

    /**
     * @param predefinedRoleName name of a predefined role
     * @param permissionType type of permissions that should be returned
     * @return collection of {@code Permission} objects
     */
    Collection<Permission> getPermissions(String predefinedRoleName, PermissionType permissionType);

    /**
     * @return {@code true} if database roles mode is available, {@code false} otherwise
     */
    boolean isDatabaseModeAvailable();

    /**
     * @return {@code true} if predefined roles mode is available, {@code false} otherwise
     */
    boolean isPredefinedRolesModeAvailable();

    /**
     * @return all available default roles.
     * Key - role name, Value - {@link Role} object or {@code null} for predefined roles
     */
    Map<String, Role> getDefaultRoles();

    /**
     * @return {@code true} if the application has at least one predefined role
     */
    boolean applicationHasPredefinedRoles();

    /**
     * @param userRoles collection of {@link UserRole} objects
     * @return collection of {@link RoleDefinition} objects associated with {@link UserRole} objects from param
     */
    Collection<RoleDefinition> getRoleDefinitions(@Nullable Collection<UserRole> userRoles);

    /**
     * @param predefinedRoleName name of a predefined role
     * @return {@code RoleDefinition} object that contains all permissions of a predefined role
     */
    RoleDefinition getRoleDefinitionByName(String predefinedRoleName);

}
