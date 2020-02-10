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

import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Service(RolesService.NAME)
public class RolesServiceBean implements RolesService {

    @Inject
    protected DataManager dataManager;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected PersistenceSecurity persistenceSecurity;

    @Inject
    protected Logger log;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected PredefinedRoleDefinitionRepository predefinedRoleDefinitionRepository;

    @Inject
    protected Metadata metadata;

    @Inject
    protected RolesHelper rolesHelper;

    @Inject
    protected EntityStates entityStates;

    @Override
    public Collection<Role> getAllRoles() {
        Map<String, Role> allRolesMap = new HashMap<>();

        Collection<RoleDefinition> predefinedRoleDefinitions = predefinedRoleDefinitionRepository.getRoleDefinitions();

        for (RoleDefinition predefinedRoleDefinition : predefinedRoleDefinitions) {
            allRolesMap.put(predefinedRoleDefinition.getName(),
                    rolesHelper.transformToRole(predefinedRoleDefinition,
                            RoleTransformationOption.DO_NOT_INCLUDE_PERMISSIONS));
        }

        if (isRoleStorageMixedMode()) {
            List<Role> roles = dataManager.load(Role.class)
                    .query("select r from sec$Role r order by r.name")
                    .list();

            for (Role role : roles) {
                if (allRolesMap.containsKey(role.getName())) {
                    log.warn("Role name '{}' is used for some predefined role. " +
                            "Also there is the persisted Role object with the same name.", role.getName());
                    continue;
                }
                allRolesMap.put(role.getName(), role);
            }
        }

        List<Role> result = new ArrayList<>(allRolesMap.size());
        for (Role role : allRolesMap.values()) {
            if (!persistenceSecurity.filterByConstraints(role)) {
                result.add(role);
            }
        }
        return result;
    }

    @Override
    @Nullable
    public RoleDefinition getRoleDefinitionByName(String name) {
        return predefinedRoleDefinitionRepository.getRoleDefinitionByName(name);
    }

    @Override
    public Role getRoleDefinitionAndTransformToRole(String roleDefinitionName) {
        RoleDefinition roleDefinition = predefinedRoleDefinitionRepository.getRoleDefinitionByName(roleDefinitionName);
        return roleDefinition != null ?
                rolesHelper.transformToRole(roleDefinition, RoleTransformationOption.DO_NOT_INCLUDE_PERMISSIONS) :
                null;
    }

    @Override
    public boolean isRoleStorageMixedMode() {
        return serverConfig.getRolesStorageMode() == SecurityStorageMode.MIXED;
    }

    @Override
    public Collection<Role> getDefaultRoles() {
        return rolesHelper.getDefaultRoles();
    }

    @Override
    public Role transformToRole(RoleDefinition roleDefinition, RoleTransformationOption... transformationOptions) {
        return rolesHelper.transformToRole(roleDefinition, transformationOptions);
    }

    public Collection<Permission> getPermissions(String predefinedRoleName, PermissionType permissionType) {
        PermissionsContainer permissionsContainer;
        RoleDefinition roleDefinition = predefinedRoleDefinitionRepository.getRoleDefinitionByName(predefinedRoleName);
        if (roleDefinition == null) return Collections.emptyList();
        switch (permissionType) {
            case ENTITY_OP:
                permissionsContainer = roleDefinition.entityPermissions();
                break;
            case ENTITY_ATTR:
                permissionsContainer = roleDefinition.entityAttributePermissions();
                break;
            case SPECIFIC:
                permissionsContainer = roleDefinition.specificPermissions();
                break;
            case SCREEN:
                permissionsContainer = roleDefinition.screenPermissions();
                break;
            case UI:
                permissionsContainer = roleDefinition.screenComponentPermissions();
                break;
            default:
                throw new RuntimeException("Unsupported permission type: " + permissionType);
        }

        Role role = rolesHelper.transformToRole(roleDefinition, RoleTransformationOption.DO_NOT_INCLUDE_PERMISSIONS);
        return rolesHelper.transformToPermissionsCollection(permissionsContainer, permissionType, role);
    }

    @Override
    public Collection<Role> getRolesForUser(User user) {
        return getRoleDefinitionsForUser(user).stream()
                .map(rolesHelper::transformToRole)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<RoleDefinition> getRoleDefinitionsForUser(User user) {
        return rolesHelper.getRoleDefinitionsForUser(user, true);
    }

    @Override
    public Access getPermissionUndefinedAccessPolicy() {
        return rolesHelper.getPermissionUndefinedAccessPolicy();
    }

    @Override
    public int getRolesPolicyVersion() {
        return serverConfig.getRolesPolicyVersion();
    }

}
