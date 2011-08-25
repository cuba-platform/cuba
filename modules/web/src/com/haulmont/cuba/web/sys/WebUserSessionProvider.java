/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.04.2009 15:24:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;

import java.io.Serializable;

public class WebUserSessionProvider extends UserSessionProvider
{
    protected UserSession __getUserSession() {
        return App.getInstance().getConnection().getSession();
    }

    @Override
    protected void __setSessionAttribute(String name, Serializable value) {
        UserSession userSession = __getUserSession();
        userSession.setAttribute(name, value);
        UserSessionService uss = ServiceLocator.lookup(UserSessionService.NAME);
        uss.setSessionAttribute(userSession.getId(), name, value);
    }
}
