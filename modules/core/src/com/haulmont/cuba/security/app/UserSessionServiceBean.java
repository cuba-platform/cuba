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
import com.haulmont.cuba.security.global.UserSession;

import javax.ejb.Stateless;
import java.util.UUID;
import java.io.Serializable;

@Stateless(name = UserSessionService.JNDI_NAME)
public class UserSessionServiceBean implements UserSessionService
{
    public void putSessionAttribute(UUID sessionId, String name, Serializable value) {
        UserSession userSession = UserSessionManager.getInstance().getSession(sessionId);
        userSession.setAttribute(name, value);
    }
}
