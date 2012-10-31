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

import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Base class for MBeans.<br>
 * Main purpose is to provide login/logout support for methods invoked from schedulers or JMX-console.
 */
public class ManagementBean {
    private Log log = LogFactory.getLog(getClass());

    @Inject
    private LoginWorker loginWorker;

    @Inject
    private UserSessionManager userSessionManager;

    @Inject
    protected PasswordEncryption passwordEncryption;

    private ThreadLocal<Boolean> loginPerformed = new ThreadLocal<>();

    protected UUID sessionId;

    /**
     * Performs login with credentials set in cuba-app.properties<br>
     * First checks if a current thread session exists or the bean is already logged in and that session is still valid.
     * If no, performs login and stores sessionId in the protected field.<br>
     * No logout assumed.
     *
     * @throws LoginException If access denied
     */
    protected void loginOnce() throws LoginException {
        // first check if a current thread session exists - may be got here from Web UI 
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext != null && userSessionManager.findSession(securityContext.getSessionId()) != null) {
            return;
        }
        // no current thread session or it is expired - so work with the internal session
        UserSession session;
        if (sessionId == null || userSessionManager.findSession(sessionId) == null) {
            // internal session doesn't exist or expired
            ManagementBean.Credentials credentialsForLogin = getCredentialsForLogin();
            String name = credentialsForLogin.getUserName();

            session = loginWorker.loginSystem(name);
            loginPerformed.set(true);
            sessionId = session.getId();
        } else {
            session = userSessionManager.getSession(sessionId);
        }
        AppContext.setSecurityContext(new SecurityContext(session));
    }

    /**
     * Performs cleanup for SecurityContext if there was previous loginOnce in this thread.<br>
     * Should be placed into "finally" section of a try/finally block.
     */
    protected void clearSecurityContext() {
        AppContext.setSecurityContext(null);
    }

    /**
     * Performs login with credentials set in cuba-app.properties<br>
     * Should be placed inside try/finally block with logout in "finally" section
     *
     * @throws LoginException If access denied
     */
    protected void login() throws LoginException {
        // first check if a current thread session exists - may be got here from Web UI
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext == null || userSessionManager.findSession(securityContext.getSessionId()) == null) {
            // no current thread session or it is expired
            ManagementBean.Credentials credentialsForLogin = getCredentialsForLogin();
            String name = credentialsForLogin.getUserName();

            UserSession session = loginWorker.loginSystem(name);
            AppContext.setSecurityContext(new SecurityContext(session));
            loginPerformed.set(true);
        }
    }

    protected Credentials getCredentialsForLogin() {
        return new Credentials(AppContext.getProperty("cuba.jmxUserLogin"));
    }

    /**
     * Performs logout if there was previous login in this thread.<br>
     * Should be placed into "finally" section of a try/finally block.
     */
    protected void logout() {
        try {
            if (BooleanUtils.isTrue(loginPerformed.get())) {
                SecurityContext securityContext = AppContext.getSecurityContext();
                if (securityContext != null) {
                    loginWorker.logout();
                    AppContext.setSecurityContext(null);
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

        public Credentials(String userName) {
            this.userName = userName;
        }

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
