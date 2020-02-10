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

package com.haulmont.cuba.security.app.role;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.cuba.security.role.SecurityStorageMode.MIXED;

/**
 * Class contains miscellaneous useful methods for working with predefined roles
 */
@Component("cuba_RolesHelper")
public class RolesHelper {

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected PredefinedRoleDefinitionRepository predefinedRoleDefinitionRepository;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected Metadata metadata;

    private Logger log = LoggerFactory.getLogger(RolesHelper.class);

    public Collection<Role> getDefaultRoles() {
        return getDefaultRoles(null);
    }

    public Collection<Role> getDefaultRoles(@Nullable EntityManager em) {
        List<Role> result = new ArrayList<>();

        for (RoleDefinition roleDefinition : predefinedRoleDefinitionRepository.getRoleDefinitions()) {
            if (roleDefinition.isDefault()) {
                result.add(transformToRole(roleDefinition, RoleTransformationOption.DO_NOT_INCLUDE_PERMISSIONS));
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
            result.addAll(defaultRoles);
        }

        return result;
    }

    /**
     * Returns a collection of {@link RoleDefinition} objects assigned to the {@code User}. If the user role is
     * associated with the database role, the database role will be converted to the {@code RoleDefinition}
     *
     * @param user       the user
     * @param reloadUser if set to true then the passed {@code user} will be reloaded with the proper view (contains
     *                   roles with permissions). If the parameter value is false then this check won't be performed.
     *                   Set the parameter to false when the method is invoked from within the opened transaction (i.e.
     *                   from the {@link com.haulmont.cuba.security.sys.UserSessionManager}) - in this case all
     *                   non-loaded fields will be fetched when they are accessed within the method.
     * @return collection of {@link RoleDefinition} objects
     */
    public Collection<RoleDefinition> getRoleDefinitionsForUser(User user, boolean reloadUser) {
        if (reloadUser) {
            View userWithRolesView = ViewBuilder.of(User.class)
                    .add("userRoles", userRoleViewBuilder -> {
                        userRoleViewBuilder
                                .add("roleName")
                                .add("role", roleViewBuilder ->
                                        roleViewBuilder
                                                .addView(View.LOCAL)
                                                .add("permissions", View.LOCAL)
                                );
                    })
                    .build();
            user = dataManager.reload(user, userWithRolesView);
        }

        List<UserRole> userRoles = user.getUserRoles();
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
            RoleDefinition role = predefinedRoleDefinitionRepository.getRoleDefinitionByName((ur.getRoleName()));
            if (role != null) {
                ur.setRoleDefinition(role);
                result.put(role.getName(), role);
            }
        }

        if (serverConfig.getRolesStorageMode() == MIXED) {
            for (UserRole ur : userRolesWithRoleObject) {
                Role role = ur.getRole();
                if (predefinedRoleDefinitionRepository.getRoleDefinitionByName(role.getName()) != null) {
                    log.warn("User '{}' has link to the persisted role '{}', but this role name is used for some predefined role. " +
                            "Persisted role's permissions will not be taken into account.", ur.getUser().getLogin(), role.getName());
                    continue;
                }
                RoleDefinition roleDefinition = transformToRoleDefinition(role);
                ur.setRoleDefinition(roleDefinition);
                result.put(roleDefinition.getName(), roleDefinition);
            }
        }
        return new ArrayList<>(result.values());
    }

    public Role transformToRole(RoleDefinition roleDefinition, RoleTransformationOption... transformationOptions) {
        Role role = metadata.create(Role.class);
        role.setPredefined(true);
        role.setName(roleDefinition.getName());
        role.setLocName(roleDefinition.getLocName());
        role.setDescription(roleDefinition.getDescription());
        role.setDefaultRole(roleDefinition.isDefault());
        role.setSecurityScope(roleDefinition.getSecurityScope());

        if (!ArrayUtils.contains(transformationOptions, RoleTransformationOption.DO_NOT_INCLUDE_PERMISSIONS)) {
            Set<Permission> permissions = new HashSet<>(transformToPermissionsCollection(roleDefinition.entityPermissions(), PermissionType.ENTITY_OP,
                    role));
            permissions.addAll(transformToPermissionsCollection(roleDefinition.entityAttributePermissions(), PermissionType.ENTITY_ATTR, role));
            permissions.addAll(transformToPermissionsCollection(roleDefinition.specificPermissions(), PermissionType.SPECIFIC, role));
            permissions.addAll(transformToPermissionsCollection(roleDefinition.screenPermissions(), PermissionType.SCREEN, role));
            permissions.addAll(transformToPermissionsCollection(roleDefinition.screenComponentPermissions(), PermissionType.UI, role));

            role.setPermissions(permissions);
        }

        return role;
    }

    public Collection<Permission> transformToPermissionsCollection(PermissionsContainer permissionsContainer,
                                                                   PermissionType type,
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

    public RoleDefinition transformToRoleDefinition(Role role) {
        BasicRoleDefinition.BasicRoleDefinitionBuilder roleDefinitionBuilder = BasicRoleDefinition.builder()
                .withName(role.getName())
                .withDescription(role.getDescription())
                .withSecurityScope(role.getSecurityScope())
                .withPermissions(role.getPermissions());

        if (serverConfig.getRolesPolicyVersion() == 1) {
            fillWildcardPermissionsByRoleType(roleDefinitionBuilder, role.getType());
            if (RoleType.SUPER == role.getType()) {
                roleDefinitionBuilder.withIsSuper(true);
            }
        }

        return roleDefinitionBuilder
                .build();
    }

    /**
     * Defines a policy for resolving permission values that are not defined in roles. For roles policy v1, if a role
     * doesn't define any explicit permission then this target is allowed.
     * <p>
     * For new v2 security mechanism the undefined permission should be resolved as denied.
     */
    public Access getPermissionUndefinedAccessPolicy() {
        return serverConfig.getRolesPolicyVersion() == 1 ? Access.ALLOW : Access.DENY;
    }

    protected void fillWildcardPermissionsByRoleType(BasicRoleDefinition.BasicRoleDefinitionBuilder builder,
                                                     @Nullable RoleType roleType) {
        Integer entityCreate = null;
        Integer entityRead = null;
        Integer entityUpdate = null;
        Integer entityDelete = null;
        Integer entityAttr = null;
        Integer specific = null;
        Integer screen = null;
        if (roleType != null) {
            switch (roleType) {
                case SUPER:
                    entityCreate = Access.ALLOW.getId();
                    entityRead = Access.ALLOW.getId();
                    entityUpdate = Access.ALLOW.getId();
                    entityDelete = Access.ALLOW.getId();
                    entityAttr = EntityAttrAccess.MODIFY.getId();
                    screen = Access.ALLOW.getId();
                    specific = Access.ALLOW.getId();
                    break;
                case DENYING:
                    entityCreate = Access.DENY.getId();
                    entityRead = Access.DENY.getId();
                    entityUpdate = Access.DENY.getId();
                    entityDelete = Access.DENY.getId();
                    screen = Access.DENY.getId();
                    specific = Access.DENY.getId();
                    break;
                case READONLY:
                    entityCreate = Access.DENY.getId();
                    entityUpdate = Access.DENY.getId();
                    entityDelete = Access.DENY.getId();
                    break;
                case STANDARD:
                    break;
            }
        }
        if (entityCreate != null)
            builder.withPermission(PermissionType.ENTITY_OP, "*:create", entityCreate);
        if (entityRead != null)
            builder.withPermission(PermissionType.ENTITY_OP, "*:read", entityRead);
        if (entityUpdate != null)
            builder.withPermission(PermissionType.ENTITY_OP, "*:update", entityUpdate);
        if (entityDelete != null)
            builder.withPermission(PermissionType.ENTITY_OP, "*:delete", entityDelete);
        if (entityAttr != null)
            builder.withPermission(PermissionType.ENTITY_ATTR, "*:*", entityAttr);
        if (screen != null)
            builder.withPermission(PermissionType.SCREEN, "*", screen);
        if (specific != null)
            builder.withPermission(PermissionType.SPECIFIC, "*", specific);
    }
}