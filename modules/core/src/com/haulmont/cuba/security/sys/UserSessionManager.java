/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.sys;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.core.*;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * System-level class managing {@link UserSession}s.
 *
 * @version $Id$
 *
 * @author krivopustov
 */
@ManagedBean(UserSessionManager.NAME)
public class UserSessionManager
{
    public static final String NAME = "cuba_UserSessionManager";

    @Inject
    private UserSessionsAPI sessions;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private Persistence persistence;

    private UserSession NO_USER_SESSION;

    private static Log log = LogFactory.getLog(UserSessionManager.class);

    private UserSessionManager() {
        User noUser = new User();
        noUser.setLogin("server");
        NO_USER_SESSION = new UserSession(noUser, Collections.<Role>emptyList(), Locale.getDefault(), true) {
            @Override
            public UUID getId() {
                return AppContext.NO_USER_CONTEXT.getSessionId();
            }
        };
    }

    /**
     * Create a new session and fill it with security data. Must be called inside a transaction.
     * @param user      user instance
     * @param locale    user locale
     * @param system    create system session
     * @return          new session instance
     */
    public UserSession createSession(User user, Locale locale, boolean system) {
        List<Role> roles = new ArrayList<Role>();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole() != null) {
                roles.add(userRole.getRole());
            }
        }
        UserSession session = new UserSession(user, roles, locale, system);
        compilePermissions(session, roles);
        compileConstraints(session, user.getGroup());
        compileSessionAttributes(session, user.getGroup());
        return session;
    }

    /**
     * Create a new session from existing for another user and fill it with security data for that new user.
     * Must be called inside a transaction.
     * @param src   existing session
     * @param user  another user instance
     * @return      new session with the same ID as existing
     */
    public UserSession createSession(UserSession src, User user) {
        List<Role> roles = new ArrayList<Role>();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole() != null) {
                roles.add(userRole.getRole());
            }
        }
        UserSession session = new UserSession(src, user, roles, src.getLocale());
        compilePermissions(session, roles);
        compileConstraints(session, user.getGroup());
        compileSessionAttributes(session, user.getGroup());
        return session;
    }

    private void compilePermissions(UserSession session, List<Role> roles) {
        for (Role role : roles) {
            if (RoleType.SUPER.equals(role.getType())) {
                // Don't waste memory, as the user with SUPER role has all permissions.
                return;
            }
        }
        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                PermissionType type = permission.getType();
                if (type != null && permission.getValue() != null) {
                    session.addPermission(type, permission.getTarget(), permission.getValue());
                }
            }
        }
    }

    private void compileConstraints(UserSession session, Group group) {
        EntityManager em = persistence.getEntityManager();
        Query q = em.createQuery("select c from sec$GroupHierarchy h join h.parent.constraints c " +
                "where h.group.id = ?1");
        q.setParameter(1, group);
        List<Constraint> constraints = q.getResultList();
        List<Constraint> list = new ArrayList<Constraint>(constraints);
        list.addAll(group.getConstraints());
        for (Constraint constraint : list) {
            session.addConstraint(constraint.getEntityName(), constraint.getJoinClause(), constraint.getWhereClause());
        }
    }

    private void compileSessionAttributes(UserSession session, Group group) {
        List<SessionAttribute> list = new ArrayList<SessionAttribute>(group.getSessionAttributes());

        EntityManager em = persistence.getEntityManager();
        Query q = em.createQuery("select a from sec$GroupHierarchy h join h.parent.sessionAttributes a " +
                "where h.group.id = ?1 order by h.level desc");
        q.setParameter(1, group);
        List<SessionAttribute> attributes = q.getResultList();
        list.addAll(attributes);

        for (SessionAttribute attribute : list) {
            Datatype datatype = Datatypes.get(attribute.getDatatype());
            try {
                if (session.getAttributeNames().contains(attribute.getName())) {
                    log.warn("Duplicate definition of '" + attribute.getName() + "' session attribute in the group hierarchy");
                }
                session.setAttribute(attribute.getName(), (Serializable) datatype.parse(attribute.getStringValue()));
            } catch (ParseException e) {
                throw new RuntimeException("Unable to set session attribute " + attribute.getName(), e);
            }
        }
    }

    /**
     * Store session in the distributed sessions cache.
     * Should be called outside of transaction to ensure all persistent objects have been detached.
     * @param session   session instance
     */
    public void storeSession(UserSession session) {
        sessions.add(session);
    }

    /**
     * Remove the session from the distributed sessions cache.
     * Should be called outside of transaction to ensure all persistent objects have been detached.
     * @param session   session instance
     */
    public void removeSession(UserSession session) {
        sessions.remove(session);
    }

    /**
     * Search for session in cache.
     * @param sessionId session's ID
     * @return          session instance
     * @throws NoUserSessionException in case of session with the specified ID is not found in cache.
     */
    public UserSession getSession(UUID sessionId) {
        UserSession session = findSession(sessionId);
        if (session == null) {
            throw new NoUserSessionException(sessionId);
        }
        return session;
    }

    /**
     * Search for session in cache.
     * @param sessionId session's ID
     * @return          session instance or null if not found
     */
    public UserSession findSession(UUID sessionId) {
        if (AppContext.isStarted())
            return sessions.get(sessionId);
        else
            return NO_USER_SESSION;
    }

    public Integer getPermissionValue(User user, PermissionType permissionType, String target) {
        Integer result;
        List<Role> roles = new ArrayList<Role>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user = em.find(User.class, user.getId());
            for (UserRole userRole : user.getUserRoles()) {
                if (userRole.getRole() != null) {
                    roles.add(userRole.getRole());
                }
            }
            UserSession session = new UserSession(user, roles, userSessionSource.getLocale(), false);
            compilePermissions(session, roles);
            result = session.getPermissionValue(permissionType, target);
            tx.commit();
        } finally {
            tx.end();
        }
        return result; 
    }
}
