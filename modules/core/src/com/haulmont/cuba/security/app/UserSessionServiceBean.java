/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2009 18:29:50
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * Service facade to user sessions management
 */
@Service(UserSessionService.NAME)
public class UserSessionServiceBean implements UserSessionService
{
    @Inject
    private UserSessionManager userSessionManager;

    @Inject
    private UserSessionsAPI userSessions;

    @Inject
    private UserSessionSource userSessionSource;

    public UserSession getUserSession(UUID sessionId) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        return userSession;
    }

    public void setSessionAttribute(UUID sessionId, String name, Serializable value) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setAttribute(name, value);
        userSessions.propagate(sessionId);
    }

    public void setSessionAddress(UUID sessionId, String address) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setAddress(address);
        userSessions.propagate(sessionId);
    }

    public void setSessionClientInfo(UUID sessionId, String clientInfo) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setClientInfo(clientInfo);
        userSessions.propagate(sessionId);
    }

    public Collection<UserSessionEntity> getUserSessionInfo() {
        UserSessionsAPI us =  Locator.lookup(UserSessionsAPI.NAME);
        Collection<UserSessionEntity> userSessionList = us.getUserSessionInfo();
        return userSessionList;
    }

    public void killSession(UUID id) {
        UserSessionsAPI us =  Locator.lookup(UserSessionsAPI.NAME);
        us.killSession(id);
    }

    public void pingSession() {
        userSessionSource.getUserSession();
    }

    public Integer getPermissionValue(User user, PermissionType permissionType, String target) {
        return userSessionManager.getPermissionValue(user, permissionType, target);
    }
}
