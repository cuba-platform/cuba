/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.config.PortalConfig;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_PortalAnonymousSessionHolder")
public class AnonymousSessionHolder {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    protected Configuration configuration;

    @Inject
    protected LoginService loginService;

    @Inject
    protected UserSessionService userSessionService;

    private volatile UserSession anonymousSession;

    public UserSession getSession() {
        boolean justLoggedIn = false;
        if (anonymousSession == null) {
            synchronized (this) {
                if (anonymousSession == null) {
                    anonymousSession = loginAsAnonymous();
                    justLoggedIn = true;
                }
            }
        }
        if (!justLoggedIn) {
            pingSession(anonymousSession);
        }
        return anonymousSession;
    }

    private UserSession loginAsAnonymous() {
        PortalConfig config = configuration.getConfig(PortalConfig.class);
        String login = config.getMiddlewareLogin();
        String password = config.getMiddlewarePassword();

        if (password.startsWith("md5:")) {
            password = password.substring("md5:".length(), password.length());
        } else {
            password = DigestUtils.md5Hex(password);
        }

        Locale defaulLocale = new Locale(config.getDefaultLocale());
        UserSession userSession;
        try {
            userSession = loginService.login(login, password, defaulLocale);
            // Set client info on middleware
            AppContext.setSecurityContext(new SecurityContext(userSession));
            userSessionService.setSessionClientInfo(userSession.getId(), "Portal Anonymous Session");
            AppContext.setSecurityContext(null);
        } catch (LoginException e) {
            throw new Error("Unable to login as anonymous portal user", e);
        }
        return userSession;
    }

    /**
     * Scheduled ping session
     */
    @SuppressWarnings("unused")
    public void pingSession() {
        // only if anonymous session initialized
        if (anonymousSession != null) {
            UserSession userSession = getSession();

            pingSession(userSession);
        }
    }

    private void pingSession(UserSession userSession) {
        AppContext.setSecurityContext(new SecurityContext(userSession));
        try {
            userSessionService.pingSession();
        } catch (NoUserSessionException e) {
            log.warn("Anonymous session has been lost, try restore");
            // auto restore anonymous session
            anonymousSession = null;
            getSession();
        }
        AppContext.setSecurityContext(null);
    }
}