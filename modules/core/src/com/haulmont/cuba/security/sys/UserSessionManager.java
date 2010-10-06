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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.core.*;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

@ManagedBean(UserSessionManager.NAME)
public class UserSessionManager
{
    public static final String NAME = "cuba_UserSessionManager";

    private UserSessionsAPI sessions;

    private UserSession NO_USER_SESSION;

    private static Log log = LogFactory.getLog(UserSessionManager.class);

    public static UserSessionManager getInstance() {
        // TODO KK: remove this, change to injection
        return AppContext.getApplicationContext().getBean(NAME, UserSessionManager.class);
    }

    private UserSessionManager() {
        User noUser = new User();
        noUser.setLogin("server");
        NO_USER_SESSION = new UserSession(noUser, new String[0], Locale.getDefault(), true) {
            @Override
            public UUID getId() {
                return AppContext.NO_USER_CONTEXT.getSessionId();
            }
        };
    }

    @Inject
    public void setSessions(UserSessionsAPI sessions) {
        this.sessions = sessions;
    }

    public UserSession createSession(User user, Locale locale, boolean system) {
        List<String> roleNames = new ArrayList<String>();
        List<Role> roles = new ArrayList<Role>();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole() != null) {
                roleNames.add(userRole.getRole().getName());
                roles.add(userRole.getRole());
            }
        }
        UserSession session = new UserSession(
                user, roleNames.toArray(new String[roleNames.size()]), locale, system);
        compilePermissions(session, roles);
        compileConstraints(session, user.getGroup());
        compileSessionAttributes(session, user.getGroup());
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
        compileSessionAttributes(session, user.getGroup());
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
        List<Constraint> constraints = q.getResultList();
        List<Constraint> list = new ArrayList<Constraint>(constraints);
        list.addAll(group.getConstraints());
        for (Constraint constraint : list) {
            session.addConstraint(constraint.getEntityName(), constraint.getJoinClause(), constraint.getWhereClause());
        }
    }

    private void compileSessionAttributes(UserSession session, Group group) {
        List<SessionAttribute> list = new ArrayList<SessionAttribute>(group.getSessionAttributes());

        EntityManager em = PersistenceProvider.getEntityManager();
        Query q = em.createQuery("select a from sec$GroupHierarchy h join h.parent.sessionAttributes a " +
                "where h.group.id = ?1 order by h.level desc");
        q.setParameter(1, group);
        List<SessionAttribute> attributes = q.getResultList();
        list.addAll(attributes);

        for (SessionAttribute attribute : list) {
            Datatype datatype = Datatypes.getInstance().get(attribute.getDatatype());
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
        if (AppContext.isStarted())
            return sessions.get(sessionId);
        else
            return NO_USER_SESSION;
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
                    user, roleNames.toArray(new String[roleNames.size()]), SecurityProvider.currentUserSession().getLocale(), false);
            compilePermissions(session, roles);
            result = session.getPermissionValue(permissionType, target);
            tx.commit();
        } finally {
            tx.end();
        }
        return result; 
    }
}
