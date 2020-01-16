/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.config.PortalConfig;
import com.haulmont.cuba.portal.sys.exceptions.NoMiddlewareConnectionException;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.SessionParams;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Objects;

@Component("cuba_PortalAnonymousSessionHolder")
public class AnonymousSessionHolder {

    private static final Logger log = LoggerFactory.getLogger(AnonymousSessionHolder.class);

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected PortalConfig portalConfig;

    @Inject
    protected AuthenticationService authenticationService;

    @Inject
    protected UserSessionService userSessionService;

    @Inject
    protected MessageTools messagesTools;

    protected volatile UserSession anonymousSession;

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

    protected UserSession loginAsAnonymous() {
        String login = portalConfig.getAnonymousUserLogin();
        String password = portalConfig.getTrustedClientPassword();

        UserSession userSession;
        try {
            String portalLocationString = getPortalNetworkLocation();
            String portalClientInfo = "Portal Anonymous Session";
            if (StringUtils.isNotBlank(portalLocationString)) {
                portalClientInfo += " (" + portalLocationString + ")";
            }

            TrustedClientCredentials credentials = new TrustedClientCredentials(login, password,
                    messagesTools.getDefaultLocale());
            credentials.setClientType(ClientType.PORTAL);
            credentials.setClientInfo(portalClientInfo);
            credentials.setParams(ParamsMap.of(
                    ClientType.class.getName(), AppContext.getProperty("cuba.clientType"),
                    SessionParams.CLIENT_INFO.getId(), portalClientInfo
            ));
            credentials.setSecurityScope(portalConfig.getSecurityScope());

            userSession = authenticationService.login(credentials).getSession();
        } catch (LoginException e) {
            throw new NoMiddlewareConnectionException("Unable to login as anonymous portal user", e);
        } catch (Exception e) {
            throw new NoMiddlewareConnectionException("Unable to connect to middleware services", e);
        }
        return userSession;
    }

    protected String getPortalNetworkLocation() {
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

    protected void pingSession(UserSession userSession) {
        AppContext.withSecurityContext(new SecurityContext(userSession), () -> {
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
        });
    }
}