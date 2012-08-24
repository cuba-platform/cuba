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
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_PortalAnonymousSessionHolder")
public class AnonymousSessionHolder {

    @Inject
    protected Configuration configuration;

    @Inject
    protected LoginService loginService;

    @Inject
    protected UserSessionService userSessionSource;

    private volatile UserSession anonymousSession;

    public UserSession getSession() {
        if (anonymousSession == null) {
            synchronized (this) {
                if (anonymousSession == null)
                    anonymousSession = loginAsAnonymous();
            }
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

            AppContext.setSecurityContext(new SecurityContext(userSession));
            userSessionSource.pingSession();
            AppContext.setSecurityContext(null);
        }
    }
}