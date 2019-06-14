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
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.role.Permissions;
import com.haulmont.cuba.security.role.PermissionsUtils;
import com.haulmont.cuba.security.role.RoleDef;
import com.haulmont.cuba.security.role.RolesStorageMode;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Bean contains information about all predefined roles.
 * Also has a set of methods needed to support different modes of working with roles (see {@link RolesStorageMode})
 */
@Component(RolesRepository.NAME)
public class RolesRepository {
    public static final String NAME = "cuba_RolesRepository";

    @Inject
    protected List<RoleDef> predefinedRoles;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected GlobalConfig config;

    protected Map<String, RoleDef> nameToPredefinedRoleMapping;

    public Collection<RoleDef> getRoleDefs(@Nullable Collection<UserRole> userRoles) {
        if (userRoles == null) {
            return null;
        }

        Map<String, RoleDef> result = new HashMap<>();

        List<UserRole> userRolesWithRoleDef = userRoles.stream()
                .filter(ur -> ur.getRoleDef() != null)
                .collect(Collectors.toList());

        List<UserRole> userRolesWithRoleName = userRoles.stream()
                .filter(ur -> ur.getRoleDef() == null && ur.getRoleName() != null)
                .collect(Collectors.toList());

        List<UserRole> userRolesWithRoleObject = userRoles.stream()
                .filter(ur -> ur.getRoleDef() == null && ur.getRole() != null)
                .collect(Collectors.toList());

        userRolesWithRoleDef.stream()
                .filter(ur -> ur.getRoleDef() != null)
                .forEach(ur -> result.put(ur.getRoleDef().getName(), ur.getRoleDef()));

        if (isPredefinedRolesModeAvailable()) {
            for (UserRole ur : userRolesWithRoleName) {
                RoleDef role = getRoleDefByName(ur.getRoleName());
                if (role != null) {
                    ur.setRoleDef(role);
                    result.put(role.getName(), role);
                }
            }
        }

        if (isDatabaseModeAvailable()) {
            for (UserRole ur : userRolesWithRoleObject) {
                RoleDef role = RoleDefBuilder.createRole(ur.getRole()).build();
                ur.setRoleDef(role);
                result.put(role.getName(), role);
            }
        }

        return result.values();
    }

    public RoleDef getRoleDefByName(String roleName) {
        return nameToPredefinedRoleMapping.get(roleName);
    }

    public Map<String, Role> getDefaultRoles() {
        return getDefaultRoles(null);
    }

    public Map<String, Role> getDefaultRoles(EntityManager em) {

        Map<String, Role> defaultUserRoles = new HashMap<>();

        if (isPredefinedRolesModeAvailable()) {
            for (Map.Entry<String, RoleDef> entry : nameToPredefinedRoleMapping.entrySet()) {
                if (entry.getValue().isDefault()) {
                    defaultUserRoles.put(entry.getKey(), null);
                }
            }
        }

        if (isDatabaseModeAvailable()) {
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

    public Role getRoleWithPermissions(RoleDef roleDef) {
        if (roleDef == null) {
            return null;
        }

        Role role = getRoleWithoutPermissions(roleDef);

        Set<Permission> permissions = new HashSet<>(transformPermissions(PermissionType.ENTITY_OP,
                roleDef.entityAccess(), role));
        permissions.addAll(transformPermissions(PermissionType.ENTITY_ATTR, roleDef.attributeAccess(), role));
        permissions.addAll(transformPermissions(PermissionType.SPECIFIC, roleDef.specificPermissions(), role));
        permissions.addAll(transformPermissions(PermissionType.SCREEN, roleDef.screenAccess(), role));
        permissions.addAll(transformPermissions(PermissionType.UI, roleDef.screenElementsAccess(), role));

        role.setPermissions(permissions);

        return role;
    }

    public Role getRoleWithoutPermissions(RoleDef roleDef) {
        if (roleDef == null) {
            return null;
        }
        Role role = metadata.create(Role.class);
        role.setPredefined(true);
        role.setName(roleDef.getName());
        role.setDescription(roleDef.getDescription());
        role.setType(roleDef.getRoleType());
        role.setDefaultRole(roleDef.isDefault());

        return role;
    }

    protected Set<Permission> transformPermissions(PermissionType type, Permissions permissions, Role role) {
        if (permissions == null || role == null || type == null) {
            return Collections.emptySet();
        }
        Set<Permission> result = new HashSet<>();

        for (Map.Entry<String, Integer> entry : PermissionsUtils.getPermissions(permissions).entrySet()) {
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
        Permissions permissions;
        RoleDef roleDef = getRoleDefByName(predefinedRoleName);
        switch (permissionType) {
            case ENTITY_OP:
                permissions = roleDef.entityAccess();
                break;
            case ENTITY_ATTR:
                permissions = roleDef.attributeAccess();
                break;
            case SPECIFIC:
                permissions = roleDef.specificPermissions();
                break;
            case SCREEN:
                permissions = roleDef.screenAccess();
                break;
            case UI:
                permissions = roleDef.screenElementsAccess();
                break;
            default:
                permissions = null;
        }

        return transformPermissions(permissionType, permissions, getRoleWithoutPermissions(roleDef));
    }

    public boolean isDatabaseModeAvailable() {
        RolesStorageMode valueFromConfig = config.getRolesStorageMode();
        if (valueFromConfig == null) {
            return true;
        }

        return RolesStorageMode.DATABASE.equals(valueFromConfig) || RolesStorageMode.MIXED.equals(valueFromConfig);
    }

    public boolean isPredefinedRolesModeAvailable() {
        RolesStorageMode valueFromConfig = config.getRolesStorageMode();
        if (valueFromConfig == null) {
            return true;
        }

        return RolesStorageMode.SOURCE_CODE.equals(valueFromConfig) || RolesStorageMode.MIXED.equals(valueFromConfig);
    }

    protected Map<String, RoleDef> getNameToPredefinedRoleMapping() {
        return nameToPredefinedRoleMapping;
    }

    @PostConstruct
    private void initNameToPredefinedRoleMapping() {
        nameToPredefinedRoleMapping = new ConcurrentHashMap<>();

        for (RoleDef role : predefinedRoles) {
            nameToPredefinedRoleMapping.put(role.getName(), role);
        }
    }

    /**
     * Allows you to register a role created using the {@link RoleDefBuilder}.
     * This method should be invoked during application startup.
     *
     * @param roleDef role to register
     */
    public void registerRole(RoleDef roleDef) {
        nameToPredefinedRoleMapping.put(roleDef.getName(), roleDef);
    }
}
