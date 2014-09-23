/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.config.PortalConfig;
import com.haulmont.cuba.portal.sys.exceptions.NoMiddlewareConnectionException;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_PortalAnonymousSessionHolder")
public class AnonymousSessionHolder {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected PortalConfig portalConfig;

    @Inject
    protected PasswordEncryption passwordEncryption;

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
        String login = portalConfig.getAnonymousUserLogin();
        String password = portalConfig.getTrustedClientPassword();

        Configuration configuration = AppBeans.get(Configuration.NAME);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        Collection<Locale> locales = globalConfig.getAvailableLocales().values();

        Locale defaultLocale = locales.iterator().next();
        UserSession userSession;
        try {
            userSession = loginService.loginTrusted(login, password, defaultLocale);
            // Set client info on middleware
            AppContext.setSecurityContext(new SecurityContext(userSession));

            String portalLocationString = getPortalNetworkLocation();
            String portalClientInfo = "Portal Anonymous Session";
            if (StringUtils.isNotBlank(portalLocationString)) {
                portalClientInfo += " (" + portalLocationString + ")";
            }

            userSessionService.setSessionClientInfo(userSession.getId(), portalClientInfo);
            AppContext.setSecurityContext(null);
        } catch (LoginException e) {
            throw new NoMiddlewareConnectionException("Unable to login as anonymous portal user", e);
        } catch (Exception e) {
            throw new NoMiddlewareConnectionException("Unable to connect to middleware services", e);
        }
        return userSession;
    }

    private String getPortalNetworkLocation() {
        return globalConfig.getWebHostName() + ":" +
               globalConfig.getWebPort() + "/" +
               globalConfig.getWebContextName();
    }

    /**
     * Scheduled ping session
     */
    @SuppressWarnings("unused")
    public void pingSession() {
        // only if anonymous session initialized
        UserSession session = anonymousSession;
        if (session != null) {
            pingSession(session);
        }
    }

    private void pingSession(UserSession userSession) {
        AppContext.setSecurityContext(new SecurityContext(userSession));
        UserSession savedSession = anonymousSession;
        try {
            userSessionService.getMessages();
        } catch (NoUserSessionException e) {
            log.warn("Anonymous session has been lost, restoring it");
            synchronized (this) {
                if (Objects.equals(savedSession, anonymousSession)) {
                    // auto restore anonymous session
                    anonymousSession = loginAsAnonymous();
                }
            }
        }
        AppContext.setSecurityContext(null);
    }
}
