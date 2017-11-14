/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.security.listeners;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.sys.ConditionalOnAppProperty;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.security.events.*;
import com.haulmont.cuba.web.sys.RequestContext;
import com.vaadin.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Locale;

import static com.haulmont.cuba.web.security.ExternalUserCredentials.isLoggedInWithExternalAuth;

/**
 * @deprecated Forwards application events to legacy authentication mechanism based on {@link CubaAuthProvider}.
 */
@Deprecated
@ConditionalOnAppProperty(property = "cuba.web.externalAuthentication", value = "true")
@Component("cuba_LegacyLoginEventsForwarder")
public class LegacyLoginEventsForwarder {

    public static final String EXTERNAL_PRINCIPAL_ATTRIBUTE = "EXTERNAL_PRINCIPAL";
    public static final String LOGIN_ON_START_ATTRIBUTE = "LOGIN_ON_START";

    private final Logger log = LoggerFactory.getLogger(LegacyLoginEventsForwarder.class);

    @Inject
    protected CubaAuthProvider authProvider;
    @Inject
    protected WebAuthConfig webAuthConfig;

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void userConnected(UserConnectedEvent event) {
        Connection connection = event.getConnection();

        if (connection.isAuthenticated()) {
            authProvider.userSessionLoggedIn(connection.getSessionNN());
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void appStarted(AppStartedEvent event) {
        App app = event.getApp();
        Locale locale = app.getLocale();

        Principal principal = getSessionPrincipal();

        // Login on start only on first request from user
        if (isTryLoginOnStart()
                && principal != null
                && webAuthConfig.getExternalAuthentication()) {

            String userName = principal.getName();
            log.debug("Trying to login after external authentication as {}", userName);
            try {
                app.getConnection().loginAfterExternalAuthentication(userName, locale);
            } catch (LoginException e) {
                log.trace("Unable to login on start", e);
            } finally {
                // Close attempt login on start
                setTryLoginOnStart(false);
            }
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void appInitialized(AppInitializedEvent event) {
        if (webAuthConfig.getExternalAuthentication()) {
            Principal principal = RequestContext.get().getRequest().getUserPrincipal();
            setSessionPrincipal(principal);
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void pingExternalAuthentication(SessionHeartbeatEvent event) {
        Connection connection = event.getSource().getConnection();

        if (connection.isAuthenticated()
                && isLoggedInWithExternalAuth(connection.getSessionNN())) {
            try {
                // Ping external authentication
                if (webAuthConfig.getExternalAuthentication()) {
                    UserSession session = connection.getSession();
                    if (session != null) {
                        authProvider.pingUserSession(session);
                    }
                }
            } catch (NoUserSessionException ignored) {
                // ignore no user session exception
            } catch (Exception e) {
                log.warn("Exception while external authenticated session ping", e);
            }
        }
    }

    @EventListener
    protected void redirectToExternalAuthentication(AppLoggedOutEvent event) {
        Connection connection = event.getApp().getConnection();

        if (webAuthConfig.getExternalAuthentication()
                && isLoggedInWithExternalAuth(connection.getSessionNN())) {
            String loggedOutUrl = connection.logoutExternalAuthentication();
            event.setRedirectUrl(loggedOutUrl);
        }
    }

    protected boolean isTryLoginOnStart() {
        Object attribute = VaadinSession.getCurrent().getAttribute(LOGIN_ON_START_ATTRIBUTE);
        if (attribute != null) {
            return (Boolean) attribute;
        }
        return true;
    }

    protected void setTryLoginOnStart(boolean tryLoginOnStart) {
        VaadinSession.getCurrent().setAttribute(EXTERNAL_PRINCIPAL_ATTRIBUTE, tryLoginOnStart);
    }

    protected Principal getSessionPrincipal() {
        return (Principal) VaadinSession.getCurrent().getAttribute(EXTERNAL_PRINCIPAL_ATTRIBUTE);
    }

    protected void setSessionPrincipal(Principal principal) {
        VaadinSession.getCurrent().setAttribute(EXTERNAL_PRINCIPAL_ATTRIBUTE, principal);
    }
}