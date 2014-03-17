/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AbstractUserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.ManagedBean;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(UserSessionSource.NAME)
public class DesktopUserSessionSource extends AbstractUserSessionSource {

    @Override
    public boolean checkCurrentUserSession() {
        return App.getInstance().getConnection().isConnected() && App.getInstance().getConnection().getSession() != null;
    }

    @Override
    public UserSession getUserSession() {
        UserSession session = App.getInstance().getConnection().getSession();
        if (session == null)
            throw new IllegalStateException("No user session");
        return session;
    }
}
