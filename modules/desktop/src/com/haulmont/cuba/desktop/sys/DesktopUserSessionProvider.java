/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;

import java.io.Serializable;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopUserSessionProvider extends UserSessionProvider {

    @Override
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
