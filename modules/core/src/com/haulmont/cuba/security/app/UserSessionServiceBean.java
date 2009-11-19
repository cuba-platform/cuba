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

import com.haulmont.cuba.security.sys.UserSessionManager;
import com.haulmont.cuba.security.app.UserSessionsMBean;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.SecurityProvider;

import javax.ejb.Stateless;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;

/**
 * Service facade to user sessions management
 */
@Stateless(name = UserSessionService.JNDI_NAME)
public class UserSessionServiceBean implements UserSessionService
{
    public void putSessionAttribute(UUID sessionId, String name, Serializable value) {
        UserSession userSession = UserSessionManager.getInstance().getSession(sessionId);
        userSession.setAttribute(name, value);
    }

    public Collection<UserSessionEntity> getUserSessionInfo() {
        UserSessionsMBean mBean =  Locator.lookupMBean(UserSessionsMBean.class, UserSessionsMBean.OBJECT_NAME);
        Collection<UserSessionEntity> userSessionList = mBean.getAPI().getUserSessionInfo();
        return userSessionList;
    }

    public void killSession(UUID id) {
        UserSessionsMBean mBean =  Locator.lookupMBean(UserSessionsMBean.class, UserSessionsMBean.OBJECT_NAME);
        mBean.getAPI().killSession(id);        
    }

    public void pingSession() {
        SecurityProvider.currentUserSession();
    }

    public Integer getPermissionValue(User user, PermissionType permissionType, String target) {
        return UserSessionManager.getInstance().getPermissionValue(user, permissionType, target);
    }
}
