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

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.app.LoginWorker;

import java.util.UUID;
import java.util.Locale;
import java.util.Date;

import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.inject.Inject;

/**
 * Base class for MBeans.<br>
 * Main purpose is to provide login/logout support for methods invoked
 * from JBoss schedulers or JMX-console.
 */
public class ManagementBean
{
    private Log log = LogFactory.getLog(getClass());

    private LoginWorker loginWorker;

    private UserSessionManager userSessionManager;

    @Inject
    public void setLoginWorker(LoginWorker loginWorker) {
        this.loginWorker = loginWorker;
    }

    @Inject
    public void setUserSessionManager(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    /**
     * Base class for creating of <code>org.jboss.varia.scheduler.Schedulable</code> instances.<br>
     * Used for setting login credentials when invoking MBean methods from JBoss schedulers.
     * <p>DEPRECATED - use Spring scheduling
     */
    @Deprecated
    public static abstract class LoginSupport //implements Schedulable
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
     * Performs login with credentials set in app.properties
     * or a {@link LoginSupport} instance.<br>
     * Should be placed inside try/finally block with logout in "finally" section
     * @throws LoginException
     */
    protected void login() throws LoginException {
        UUID sessionId = ServerSecurityUtils.getSessionId();
        if (sessionId == null || userSessionManager.findSession(sessionId) == null) {
            String name;
            String password;
            SecurityContext securityContext = ServerSecurityUtils.getSecurityAssociation();
            if (securityContext == null || securityContext.getUser() == null || securityContext.getPassword() == null) {
                ManagementBean.Credentials credintialsForLogin = getCredintialsForLogin();
                name = credintialsForLogin.getUserName();
                password = credintialsForLogin.getPassword();
            } else {
                name = securityContext.getUser();
                password = securityContext.getPassword();
            }
            if (password.startsWith("md5:"))
                password = password.substring("md5:".length(), password.length());
            else
                password = DigestUtils.md5Hex(password);

            UserSession session = loginWorker.login(name, password, Locale.getDefault());
            ServerSecurityUtils.setSecurityAssociation(name, session.getId());
            loginPerformed.set(true);
        }
    }

    protected Credentials getCredintialsForLogin() {
        return new Credentials(AppContext.getProperty("cuba.jmxUserLogin"), AppContext.getProperty("cuba.jmxUserPassword"));
    }

    /**
     * Performs logout if there was previous login in this thread.<br>
     * Should be placed into "finally" section of a try/finally block.
     */
    protected void logout() {
        try {
            if (BooleanUtils.isTrue(loginPerformed.get())) {
                UUID sessionId = ServerSecurityUtils.getSessionId();
                if (sessionId != null) {
                    loginWorker.logout();
                    ServerSecurityUtils.clearSecurityAssociation();
                }
                loginPerformed.remove();
            }
        } catch (Exception e) {
            log.error("Error logging out", e);
        }
    }

    public class Credentials {

        private String userName;

        private String password;

        public Credentials(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

}
