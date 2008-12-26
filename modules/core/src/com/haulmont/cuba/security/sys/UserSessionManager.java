/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 12:09:22
 *
 * $Id$
 */
package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;

import java.util.*;

public class UserSessionManager
{
    private static UserSessionManager instance;

    private UserSessions sessions;

    public static UserSessionManager getInstance() {
        if (instance == null) {
            instance = new UserSessionManager();
        }
        return instance;
    }

    public UserSessionManager() {
        sessions = new UserSessionsCache();
    }

    public UserSession createSession(User user, Profile profile, Locale locale) {
        List<String> roleNames = new ArrayList<String>();
        List<Role> roles = new ArrayList<Role>();
        for (ProfileRole profileRole : profile.getProfileRoles()) {
            roleNames.add(profileRole.getRole().getName());
            roles.add(profileRole.getRole());
        }
        UserSession session = new UserSession(user, roleNames.toArray(new String[roleNames.size()]), locale);
        compilePermissions(session, roles);
        compileConstraints(session, profile.getGroup());
        sessions.add(session);
        return session;
    }

    private void compilePermissions(UserSession session, List<Role> roles) {
        for (Role role : roles) {
            if (role.isSuperRole())
                return;
        }
        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                PermissionType type = PermissionType.fromId(permission.getType());
                if (type != null && permission.getValue() != null) {
                    Integer value = session.getPermissionValue(type, permission.getTarget());
                    if (value == null || value < permission.getValue()) {
                        session.addPermission(type, permission.getTarget(), permission.getValue());
                    }
                }
            }
        }
    }

    private void compileConstraints(UserSession session, Group group) {
        EntityManager em = PersistenceProvider.getEntityManager();
        Query q = em.createQuery("select c from sec$GroupHierarchy h join h.parent.constraints c " +
                "where h.group = ?1");
        q.setParameter(1, group);
        List<Constraint> constraints = q.getResultList();
        List<Constraint> list = new ArrayList<Constraint>(constraints);
        list.addAll(group.getConstraints());
        for (Constraint constraint : list) {
            session.addConstraint(constraint.getEntityName(), constraint.getWhereClause());
        }
    }

    public void removeSession(UserSession session) {
        sessions.remove(session);
    }

    public UserSession getSession(UUID sessionId) {
        UserSession session = findSession(sessionId);
        if (session == null) {
            throw new NoUserSessionException(sessionId);
        }
        return session;
    }

    public UserSession findSession(UUID sessionId) {
        return sessions.get(sessionId);
    }
}
