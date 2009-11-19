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
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.app.UserSessionsMBean;
import com.haulmont.cuba.core.*;

import java.util.*;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

public class UserSessionManager
{
    private static UserSessionManager instance;

    private UserSessionsAPI sessions;

    public static UserSessionManager getInstance() {
        if (instance == null) {
            instance = new UserSessionManager();
        }
        return instance;
    }

    public UserSessionManager() {
        UserSessionsMBean mBean = Locator.lookupMBean(UserSessionsMBean.class, UserSessionsMBean.OBJECT_NAME);
        sessions = mBean.getAPI();
    }

    public UserSession createSession(User user, Locale locale) {
        List<String> roleNames = new ArrayList<String>();
        List<Role> roles = new ArrayList<Role>();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole() != null) {
                roleNames.add(userRole.getRole().getName());
                roles.add(userRole.getRole());
            }
        }
        UserSession session = new UserSession(
                user, roleNames.toArray(new String[roleNames.size()]), locale);
        compilePermissions(session, roles);
        compileConstraints(session, user.getGroup());
        sessions.add(session);
        return session;
    }

    public UserSession updateSession(UserSession src, User user) {
        List<String> roleNames = new ArrayList<String>();
        List<Role> roles = new ArrayList<Role>();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole() != null) {
                roleNames.add(userRole.getRole().getName());
                roles.add(userRole.getRole());
            }
        }
        UserSession session = new UserSession(
                src, user, roleNames.toArray(new String[roleNames.size()]), src.getLocale());
        compilePermissions(session, roles);
        compileConstraints(session, user.getGroup());
        sessions.remove(src);
        sessions.add(session);
        return session;
    }

    private void compilePermissions(UserSession session, List<Role> roles) {
        for (Role role : roles) {
            if (BooleanUtils.isTrue(role.getSuperRole()))
                return;
        }
        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                PermissionType type = permission.getType();
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
                "where h.group.id = ?1");
        q.setParameter(1, group);
        @SuppressWarnings({"unchecked"})
        List<Constraint> constraints = q.getResultList();
        List<Constraint> list = new ArrayList<Constraint>(constraints);
        list.addAll(group.getConstraints());
        for (Constraint constraint : list) {
            session.addConstraint(constraint.getEntityName(), constraint.getJoinClause(), constraint.getWhereClause());
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

    public Integer getPermissionValue(User user, PermissionType permissionType, String target) {
        Integer result;
        List<String> roleNames = new ArrayList<String>();
        List<Role> roles = new ArrayList<Role>();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            user = em.find(User.class, user.getId());
            for (UserRole userRole : user.getUserRoles()) {
                if (userRole.getRole() != null) {
                    roleNames.add(userRole.getRole().getName());
                    roles.add(userRole.getRole());
                }
            }
            UserSession session = new UserSession(
                    user, roleNames.toArray(new String[roleNames.size()]), SecurityProvider.currentUserSession().getLocale());
            compilePermissions(session, roles);
            result = session.getPermissionValue(permissionType, target);
            tx.commit();
        } finally {
            tx.end();
        }
        return result; 
    }
}
