/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.01.2009 11:47:54
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.app.LoginWorker;

import java.util.UUID;
import java.util.Locale;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.codec.digest.DigestUtils;

public class ManagementBean
{
    private ThreadLocal<Boolean> loginPerformed = new ThreadLocal<Boolean>();

    protected void login() throws LoginException {
        UUID sessionId = ServerSecurityUtils.getSessionId();
        if (sessionId == null) {
            String[] info = ServerSecurityUtils.getUserInfo();
            if (info == null)
                throw new LoginException("No user information in security context");
            String name = info[0];
            String password = DigestUtils.md5Hex(info[1]);

            UserSession session = getLoginWorker().login(name, password, Locale.getDefault());
            ServerSecurityUtils.setSecurityAssociation(name, session.getId());
            loginPerformed.set(true);
        }
    }

    protected void logout() {
        if (BooleanUtils.isTrue(loginPerformed.get())) {
            UUID sessionId = ServerSecurityUtils.getSessionId();
            if (sessionId != null)
                getLoginWorker().logout();
            loginPerformed.remove();
        }
    }

    private LoginWorker getLoginWorker() {
        return Locator.lookupLocal(LoginWorker.JNDI_NAME);
    }
}
