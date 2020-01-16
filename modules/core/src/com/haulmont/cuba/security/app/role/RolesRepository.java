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

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.app.RoleDefinitionBuilder;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;
import com.haulmont.cuba.security.role.PermissionsContainer;
import com.haulmont.cuba.security.role.RoleDefinition;
import com.haulmont.cuba.security.role.SecurityStorageMode;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.haulmont.cuba.security.role.SecurityStorageMode.MIXED;

/**
 * Bean contains information about all predefined roles.
 * Also has a set of methods needed to support different modes of working with roles (see {@link SecurityStorageMode})
 */
@Component(RolesRepository.NAME)
public class RolesRepository {
    public static final String NAME = "cuba_RolesRepository";

    @Inject
    protected List<RoleDefinition> predefinedRoles;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected Logger log;

    protected Map<String, RoleDefinition> nameToPredefinedRoleMapping;

    public Collection<RoleDefinition> getRoleDefinitions(@Nullable Collection<UserRole> userRoles) {
        if (userRoles == null) {
            return null;
        }

        Map<String, RoleDefinition> result = new HashMap<>();

        List<UserRole> userRolesWithRoleDef = userRoles.stream()
                .filter(ur -> ur.getRoleDefinition() != null)
                .collect(Collectors.toList());

        List<UserRole> userRolesWithRoleName = userRoles.stream()
                .filter(ur -> ur.getRoleDefinition() == null && ur.getRoleName() != null)
                .collect(Collectors.toList());

        List<UserRole> userRolesWithRoleObject = userRoles.stream()
                .filter(ur -> ur.getRoleDefinition() == null && ur.getRole() != null)
                .collect(Collectors.toList());

        userRolesWithRoleDef.stream()
                .filter(ur -> ur.getRoleDefinition() != null)
                .forEach(ur -> result.put(ur.getRoleDefinition().getName(), ur.getRoleDefinition()));

        for (UserRole ur : userRolesWithRoleName) {
            RoleDefinition role = getRoleDefinitionByName(ur.getRoleName());
            if (role != null) {
                ur.setRoleDefinition(role);
                result.put(role.getName(), role);
            }
        }

        if (serverConfig.getRolesStorageMode() == MIXED) {
            for (UserRole ur : userRolesWithRoleObject) {
                Role role = ur.getRole();
                if (nameToPredefinedRoleMapping.containsKey(role.getName())) {
                    log.warn("User '{}' has link to the persisted role '{}', but this role name is used for some predefined role. " +
                            "Persisted role's permissions will not be taken into account.", ur.getUser().getLogin(), role.getName());
                    continue;
                }
                RoleDefinition roleDefinition = RoleDefinitionBuilder.create()
                        .withName(role.getName())
                        .withDescription(role.getDescription())
                        .withSecurityScope(role.getSecurityScope())
                        .withPermissions(role.getPermissions())
                        .withDefaultScreenAccess(role.getDefaultScreenAccess())
                        .withDefaultEntityCreateAccess(role.getDefaultEntityCreateAccess())
                        .withDefaultEntityReadAccess(role.getDefaultEntityReadAccess())
                        .withDefaultEntityUpdateAccess(role.getDefaultEntityUpdateAccess())
                        .withDefaultEntityDeleteAccess(role.getDefaultEntityDeleteAccess())
                        .withDefaultEntityAttributeAccess(role.getDefaultEntityAttributeAccess())
                        .withDefaultSpecificAccess(role.getDefaultSpecificAccess())
                        .build();
                ur.setRoleDefinition(roleDefinition);
                result.put(roleDefinition.getName(), roleDefinition);
            }
        }

        return new ArrayList<>(result.values());
    }

    public RoleDefinition getRoleDefinitionByName(String roleName) {
        return nameToPredefinedRoleMapping.get(roleName);
    }

    public Map<String, Role> getDefaultRoles() {
        return getDefaultRoles(null);
    }

    public Map<String, Role> getDefaultRoles(EntityManager em) {

        Map<String, Role> defaultUserRoles = new HashMap<>();

        for (Map.Entry<String, RoleDefinition> entry : nameToPredefinedRoleMapping.entrySet()) {
            if (entry.getValue().isDefault()) {
                defaultUserRoles.put(entry.getKey(), getRoleWithoutPermissions(entry.getValue()));
            }
        }

        if (serverConfig.getRolesStorageMode() == MIXED) {
            List<Role> defaultRoles;
            String defaultRolesSql = "select r from sec$Role r where r.defaultRole = true";
            if (em != null) {
                defaultRoles = em.createQuery(defaultRolesSql, Role.class)
                        .getResultList();
            } else {
                LoadContext<Role> loadContext = LoadContext.create(Role.class);
                loadContext.setQueryString(defaultRolesSql);
                defaultRoles = dataManager.loadList(loadContext);
            }

            for (Role role : defaultRoles) {
                defaultUserRoles.put(role.getName(), role);
            }
        }

        return defaultUserRoles;
    }

    public Role getRoleWithPermissions(RoleDefinition roleDefinition) {
        if (roleDefinition == null) {
            return null;
        }

        Role role = getRoleWithoutPermissions(roleDefinition);

        Set<Permission> permissions = new HashSet<>(transformPermissions(PermissionType.ENTITY_OP,
                roleDefinition.entityPermissions(), role));
        permissions.addAll(transformPermissions(PermissionType.ENTITY_ATTR, roleDefinition.entityAttributePermissions(), role));
        permissions.addAll(transformPermissions(PermissionType.SPECIFIC, roleDefinition.specificPermissions(), role));
        permissions.addAll(transformPermissions(PermissionType.SCREEN, roleDefinition.screenPermissions(), role));
        permissions.addAll(transformPermissions(PermissionType.UI, roleDefinition.screenElementsPermissions(), role));

        role.setPermissions(permissions);

        return role;
    }

    public Role getRoleWithoutPermissions(RoleDefinition roleDefinition) {
        if (roleDefinition == null) {
            return null;
        }
        Role role = metadata.create(Role.class);
        role.setPredefined(true);
        role.setName(roleDefinition.getName());
        role.setLocName(roleDefinition.getLocName());
        role.setDescription(roleDefinition.getDescription());
        role.setDefaultRole(roleDefinition.isDefault());
        role.setSecurityScope(roleDefinition.getSecurityScope());

        EntityPermissionsContainer entityPermissionsContainer = roleDefinition.entityPermissions();
        role.setDefaultEntityCreateAccess(entityPermissionsContainer.getDefaultEntityCreateAccess());
        role.setDefaultEntityReadAccess(entityPermissionsContainer.getDefaultEntityReadAccess());
        role.setDefaultEntityUpdateAccess(entityPermissionsContainer.getDefaultEntityUpdateAccess());
        role.setDefaultEntityDeleteAccess(entityPermissionsContainer.getDefaultEntityDeleteAccess());
        role.setDefaultScreenAccess(roleDefinition.screenPermissions().getDefaultScreenAccess());
        role.setDefaultEntityAttributeAccess(roleDefinition.entityAttributePermissions().getDefaultEntityAttributeAccess());
        role.setDefaultSpecificAccess(roleDefinition.specificPermissions().getDefaultSpecificAccess());

        return role;
    }

    protected Set<Permission> transformPermissions(PermissionType type,
                                                   PermissionsContainer permissionsContainer,
                                                   Role role) {
        if (permissionsContainer == null || role == null || type == null) {
            return Collections.emptySet();
        }
        Set<Permission> result = new HashSet<>();

        for (Map.Entry<String, Integer> entry : permissionsContainer.getExplicitPermissions().entrySet()) {
            Permission permission = metadata.create(Permission.class);
            permission.setTarget(entry.getKey());
            permission.setValue(entry.getValue());
            permission.setType(type);
            permission.setRole(role);

            result.add(permission);
        }

        return result;
    }

    public Collection<Permission> getPermissions(String predefinedRoleName, PermissionType permissionType) {
        PermissionsContainer permissionsContainer;
        RoleDefinition roleDefinition = getRoleDefinitionByName(predefinedRoleName);
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
                permissionsContainer = roleDefinition.screenElementsPermissions();
                break;
            default:
                permissionsContainer = null;
        }

        return transformPermissions(permissionType, permissionsContainer, getRoleWithoutPermissions(roleDefinition));
    }

    protected Map<String, RoleDefinition> getNameToPredefinedRoleMapping() {
        return nameToPredefinedRoleMapping;
    }

    @PostConstruct
    private void initNameToPredefinedRoleMapping() {
        nameToPredefinedRoleMapping = new ConcurrentHashMap<>();

        for (RoleDefinition role : predefinedRoles) {
            nameToPredefinedRoleMapping.put(role.getName(), role);
        }
    }

    /**
     * Allows you to register a role created using the {@link RoleDefinitionBuilder}.
     * This method should be invoked during application startup.
     *
     * @param roleDefinition role to register
     */
    public void registerRole(RoleDefinition roleDefinition) {
        nameToPredefinedRoleMapping.put(roleDefinition.getName(), roleDefinition);
    }
}
