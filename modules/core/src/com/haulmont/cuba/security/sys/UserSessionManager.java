/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.security.sys;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.global.UuidSource;
import com.haulmont.cuba.core.sys.DefaultPermissionValuesConfig;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.app.group.AccessGroupDefinitionsComposer;
import com.haulmont.cuba.security.app.role.RolesHelper;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.group.AccessGroupDefinition;
import com.haulmont.cuba.security.role.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * INTERNAL.
 * <p>
 * System-level class managing {@link UserSession}s.
 */
@Component(UserSessionManager.NAME)
public class UserSessionManager {

    private final Logger log = LoggerFactory.getLogger(UserSessionManager.class);

    public static final String NAME = "cuba_UserSessionManager";

    @Inject
    protected UuidSource uuidSource;

    @Inject
    protected UserSessionsAPI sessions;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Persistence persistence;

    @Inject
    protected EntityStates entityStates;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DefaultPermissionValuesConfig defaultPermissionValuesConfig;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected RolesHelper rolesHelper;

    @Inject
    protected AccessGroupDefinitionsComposer groupsComposer;

    /**
     * Create a new session and fill it with security data. Must be called inside a transaction.
     *
     * @param user   user instance
     * @param locale user locale
     * @param system create system session
     * @return new session instance
     */
    public UserSession createSession(User user, Locale locale, boolean system) {
        return createSession(uuidSource.createUuid(), user, locale, system, null);
    }

    /**
     * Create a new session and fill it with security data. Must be called inside a transaction.
     *
     * @param user          user instance
     * @param locale        user locale
     * @param system        create system session
     * @param securityScope security scope
     * @return new session instance
     */
    public UserSession createSession(User user, Locale locale, boolean system, String securityScope) {
        return createSession(uuidSource.createUuid(), user, locale, system, securityScope);
    }

    /**
     * Create a new session and fill it with security data. Must be called inside a transaction.
     *
     * @param sessionId target session id
     * @param user      user instance
     * @param locale    user locale
     * @param system    create system session
     * @return new session instance
     */
    public UserSession createSession(UUID sessionId, User user, Locale locale, boolean system) {
        return createSession(sessionId, user, locale, system, null);
    }

    /**
     * Create a new session and fill it with security data. Must be called inside a transaction.
     *
     * @param sessionId     target session id
     * @param user          user instance
     * @param locale        user locale
     * @param system        create system session
     * @param securityScope security profile
     * @return new session instance
     */
    public UserSession createSession(UUID sessionId, User user, Locale locale, boolean system, String securityScope) {
        List<RoleDefinition> roles = new ArrayList<>();

        for (RoleDefinition role : rolesHelper.getRoleDefinitionsForUser(user, false)) {
            if (role != null) {
                String expectedScope = securityScope == null ? SecurityScope.DEFAULT_SCOPE_NAME : securityScope;
                String actualScope = role.getSecurityScope() == null ? SecurityScope.DEFAULT_SCOPE_NAME : role.getSecurityScope();
                if (Objects.equals(expectedScope, actualScope)) {
                    roles.add(role);
                }
            }
        }
        UserSession session = new UserSession(sessionId, user, roles, locale, system);
        compilePermissions(session, roles);

        if (user.getGroup() == null && Strings.isNullOrEmpty(user.getGroupNames())) {
            throw new IllegalStateException("User is not in a Group");
        }
        AccessGroupDefinition groupDefinition = compileGroupDefinition(user.getGroup(), user.getGroupNames());
        compileConstraints(session, groupDefinition);
        compileSessionAttributes(session, groupDefinition);
        session.setPermissionUndefinedAccessPolicy(rolesHelper.getPermissionUndefinedAccessPolicy());

        return session;
    }

    /**
     * Create a new session from existing for another user and fill it with security data for that new user.
     * Must be called inside a transaction.
     *
     * @param src  existing session
     * @param user another user instance
     * @return new session with the same ID as existing
     */
    public UserSession createSession(UserSession src, User user) {
        List<RoleDefinition> roles = new ArrayList<>();
        for (RoleDefinition role : rolesHelper.getRoleDefinitionsForUser(user, false)) {
            if (role != null) {
                roles.add(role);
            }
        }
        UserSession session = new UserSession(src, user, roles, src.getLocale());
        compilePermissions(session, roles);
        if (user.getGroup() == null && Strings.isNullOrEmpty(user.getGroupNames())) {
            throw new IllegalStateException("User is not in a Group");
        }

        AccessGroupDefinition groupDefinition = compileGroupDefinition(user.getGroup(), user.getGroupNames());
        compileConstraints(session, groupDefinition);
        compileSessionAttributes(session, groupDefinition);
        session.setPermissionUndefinedAccessPolicy(rolesHelper.getPermissionUndefinedAccessPolicy());

        return session;
    }

    protected void compilePermissions(UserSession session, List<RoleDefinition> roles) {
        session.setJoinedRole(buildJoinedRoleDefinition(roles));
    }

    protected RoleDefinition buildJoinedRoleDefinition(List<RoleDefinition> roles) {
        RoleDefinition effectiveRole = BasicRoleDefinition.builder().build();
        for (RoleDefinition role : roles) {
            effectiveRole = RoleDefinitionsJoiner.join(effectiveRole, role);
        }

        if (serverConfig.getDefaultPermissionValuesConfigEnabled()) {
            for (Map.Entry<String, Permission> entry : defaultPermissionValuesConfig.getDefaultPermissionValues()
                    .entrySet()) {
                String target = entry.getKey();
                Permission permission = entry.getValue();
                PermissionsContainer permissionsContainer = PermissionsUtils.getPermissionsByType(effectiveRole,
                        permission.getType());
                if (permissionsContainer.getExplicitPermissions().get(target) == null) {
                    permissionsContainer.getExplicitPermissions().put(target, permission.getValue());
                    if (permission.getType() == PermissionType.ENTITY_OP ||
                            permission.getType() == PermissionType.ENTITY_ATTR) {
                        String extendedTarget = PermissionsUtils.evaluateExtendedEntityTarget(target);
                        if (!Strings.isNullOrEmpty(extendedTarget)) {
                            permissionsContainer.getExplicitPermissions().put(extendedTarget, permission.getValue());
                        }
                    }
                }
            }
        }
        return effectiveRole;
    }

    protected AccessGroupDefinition compileGroupDefinition(Group group, String groupName) {
        AccessGroupDefinition groupDefinition;
        if (group != null) {
            groupDefinition = groupsComposer.composeGroupDefinitionFromDb(group.getId());
        } else {
            groupDefinition = groupsComposer.composeGroupDefinition(groupName);
        }
        return groupDefinition;
    }

    protected void compileConstraints(UserSession session, AccessGroupDefinition groupDefinition) {
        session.setConstraints(groupDefinition.accessConstraints());
    }

    protected void compileSessionAttributes(UserSession session, AccessGroupDefinition groupDefinition) {
        Map<String, Serializable> sessionAttributes = groupDefinition.sessionAttributes();

        for (Map.Entry<String, Serializable> entry : sessionAttributes.entrySet()) {
            if (entry.getValue() != null) {
                session.setAttribute(entry.getKey(), entry.getValue());
            } else {
                session.removeAttribute(entry.getKey());
            }
        }
    }

    /**
     * @deprecated use {@link UserSessionsAPI#add(UserSession)}}
     */
    @Deprecated
    public void storeSession(UserSession session) {
        sessions.add(session);
    }

    /**
     * @deprecated use {@link UserSessionsAPI#remove(UserSession)}}
     */
    @Deprecated
    public void removeSession(UserSession session) {
        sessions.remove(session);
    }

    /**
     * @deprecated use {@link UserSessionsAPI#getNN(UUID)}}
     */
    @Deprecated
    public UserSession getSession(UUID sessionId) {
        UserSession session = findSession(sessionId);
        if (session == null) {
            throw new NoUserSessionException(sessionId);
        }
        return session;
    }

    /**
     * @deprecated use {@link UserSessionsAPI#get(UUID)}
     */
    @Deprecated
    public UserSession findSession(UUID sessionId) {
        return sessions.getAndRefresh(sessionId, false);
    }

    public Integer getPermissionValue(User user, PermissionType permissionType, String target) {
        Integer result;
        List<RoleDefinition> roles = new ArrayList<>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user = em.find(User.class, user.getId());
            for (RoleDefinition role : rolesHelper.getRoleDefinitionsForUser(user, false)) {
                if (role != null) {
                    roles.add(role);
                }
            }
            RoleDefinition joinedRole = buildJoinedRoleDefinition(roles);
            result = PermissionsUtils.getResultingPermissionValue(joinedRole, permissionType, target,
                    rolesHelper.getPermissionUndefinedAccessPolicy());
            tx.commit();
        } finally {
            tx.end();
        }
        return result;
    }

    /**
     * INTERNAL
     */
    public void clearPermissionsOnUser(UserSession session) {
        List<User> users = new ArrayList<>();
        users.add(session.getUser());
        if (session.getSubstitutedUser() != null) {
            users.add(session.getSubstitutedUser());
        }
        for (User user : users) {
            if (entityStates.isDetached(user) && user.getUserRoles() != null) {
                List<UserRole> userRoles = user.getUserRoles().stream()
                        .filter(ur -> entityStates.isLoaded(ur, "role"))
                        .collect(Collectors.toList());
                for (UserRole ur : userRoles) {
                    if (entityStates.isLoaded(ur, "role") && ur.getRole() != null) {
                        ur.getRole().setPermissions(null);
                    }
                }
            }
        }
    }
}
