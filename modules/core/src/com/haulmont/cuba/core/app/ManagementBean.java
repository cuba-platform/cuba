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
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.varia.scheduler.Schedulable;

/**
 * Base class for MBeans.<br>
 * Main purpose is to provide login/logout support for methods invoked
 * from JBoss schedulers or JMX-console.
 */
public class ManagementBean
{
    /**
     * Base class for creating of <code>org.jboss.varia.scheduler.Schedulable</code> instances.<br>
     * Used for setting login credentials when invoking MBean methods from JBoss schedulers.
     */
    public static abstract class LoginSupport implements Schedulable
    {
        protected String user;
        protected String password;

        public LoginSupport(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public void perform(Date now, long remainingRepetitions) {
            ServerSecurityUtils.setSecurityAssociation(user, "md5:" + password);
            invoke(now, remainingRepetitions);
        }

        public abstract void invoke(Date now, long remainingRepetitions);
    }

    private ThreadLocal<Boolean> loginPerformed = new ThreadLocal<Boolean>();

    /**
     * Performs login with credentials set by web container (JMX-console)
     * or a {@link LoginSupport} instance.<br>
     * Should be placed inside try/finally block with logout in "finally" section
     * @throws LoginException
     */
    protected void login() throws LoginException {
        UUID sessionId = ServerSecurityUtils.getSessionId();
        if (sessionId == null) {
            String[] info = ServerSecurityUtils.getUserInfo();
            if (info == null)
                throw new LoginException("No user information in security context");
            String name = info[0];
            String password = info[1];
            if (password.startsWith("md5:"))
                password = password.substring("md5:".length(), password.length());
            else
                password = DigestUtils.md5Hex(info[1]);

            UserSession session = getLoginWorker().login(name, password, Locale.getDefault());
            ServerSecurityUtils.setSecurityAssociation(name, session.getId());
            loginPerformed.set(true);
        }
    }

    /**
     * Performs logout if there was previous login in this thread.<br>
     * Should be placed into "finally" section of a try/finally block. 
     */
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
