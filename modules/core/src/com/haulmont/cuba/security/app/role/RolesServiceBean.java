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

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.role.RoleDefinition;
import com.haulmont.cuba.security.role.RolesService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

@Service(RolesService.NAME)
public class RolesServiceBean implements RolesService {

    @Inject
    protected DataManager dataManager;

    @Inject
    protected RolesRepository rolesRepository;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected Logger log;

    @Override
    public Collection<Role> getAllRoles() {
        Map<String, Role> rolesForGui = new HashMap<>();

        for (Map.Entry<String, RoleDefinition> entry : rolesRepository.getNameToPredefinedRoleMapping().entrySet()) {
            rolesForGui.put(entry.getKey(), rolesRepository.getRoleWithoutPermissions(entry.getValue()));
        }

        if (isRoleStorageMixedMode()) {
            List<Role> roles = dataManager.load(Role.class)
                    .query("select r from sec$Role r order by r.name")
                    .list();

            for (Role role : roles) {
                if (rolesForGui.containsKey(role.getName())) {
                    log.warn("Role name '{}' is used for some predefined role. " +
                            "Also there is the persisted Role object with the same name.", role.getName());
                    continue;
                }
                rolesForGui.put(role.getName(), role);
            }
        }

        return new ArrayList<>(rolesForGui.values());
    }

    @Override
    public Role getRoleByName(String predefinedRoleName) {
        return rolesRepository.getRoleWithoutPermissions(rolesRepository.getRoleDefinitionByName(predefinedRoleName));
    }

    @Override
    public Collection<Permission> getPermissions(String predefinedRoleName, PermissionType permissionType) {
        return rolesRepository.getPermissions(predefinedRoleName, permissionType);
    }

    @Override
    public boolean isRoleStorageMixedMode() {
        return true;
    }

    @Override
    public Map<String, Role> getDefaultRoles() {
        return rolesRepository.getDefaultRoles();
    }

    @Override
    public Collection<RoleDefinition> getRoleDefinitions(@Nullable Collection<UserRole> userRoles) {
        return rolesRepository.getRoleDefinitions(userRoles);
    }

    @Override
    public RoleDefinition getRoleDefinitionByName(String predefinedRoleName) {
        return rolesRepository.getRoleDefinitionByName(predefinedRoleName);
    }
}
