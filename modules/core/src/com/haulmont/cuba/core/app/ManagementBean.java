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
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Base class for MBeans.<br>
 * Main purpose is to provide login/logout support for methods invoked from schedulers or JMX-console.
 */
public class ManagementBean
{
    private Log log = LogFactory.getLog(getClass());

    private LoginWorker loginWorker;

    private UserSessionManager userSessionManager;

    private ThreadLocal<Boolean> loginPerformed = new ThreadLocal<Boolean>();

    protected UUID sessionId;

    @Inject
    public void setLoginWorker(LoginWorker loginWorker) {
        this.loginWorker = loginWorker;
    }

    @Inject
    public void setUserSessionManager(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    /**
     * Performs login with credentials set in cuba-app.properties<br>
     * First checks if a current thread session exists or the bean is already logged in and that session is still valid.
     * If no, performs login and stores sessionId in the protected field.<br>
     * No logout assumed.
     * @throws LoginException
     */
    protected void loginOnce() throws LoginException {
        // first check if a current thread session exists - may be got here from Web UI 
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext != null && userSessionManager.findSession(securityContext.getSessionId()) != null) {
            return;
        }
        // no current thread session - so work with the internal session
        UserSession session;
        if (sessionId == null || userSessionManager.findSession(sessionId) == null) {
            String name;
            String password;
            if (securityContext == null || securityContext.getUser() == null || securityContext.getPassword() == null) {
                ManagementBean.Credentials credentialsForLogin = getCredentialsForLogin();
                name = credentialsForLogin.getUserName();
                password = credentialsForLogin.getPassword();
            } else {
                name = securityContext.getUser();
                password = securityContext.getPassword();
            }
            if (password.startsWith("md5:"))
                password = password.substring("md5:".length(), password.length());
            else
                password = DigestUtils.md5Hex(password);

            session = loginWorker.loginSystem(name, password);
            loginPerformed.set(true);
            sessionId = session.getId();
        } else {
            session = userSessionManager.getSession(sessionId);
        }
        AppContext.setSecurityContext(new SecurityContext(session));
    }

    /**
     * Performs login with credentials set in cuba-app.properties<br>
     * Should be placed inside try/finally block with logout in "finally" section
     * @throws LoginException
     */
    protected void login() throws LoginException {
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext == null || userSessionManager.findSession(securityContext.getSessionId()) == null) {
            String name;
            String password;
            if (securityContext == null || securityContext.getUser() == null || securityContext.getPassword() == null) {
                ManagementBean.Credentials credintialsForLogin = getCredentialsForLogin();
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

            UserSession session = loginWorker.loginSystem(name, password);
            AppContext.setSecurityContext(new SecurityContext(session));
            loginPerformed.set(true);
        }
    }

    protected Credentials getCredentialsForLogin() {
        return new Credentials(AppContext.getProperty("cuba.jmxUserLogin"), AppContext.getProperty("cuba.jmxUserPassword"));
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
