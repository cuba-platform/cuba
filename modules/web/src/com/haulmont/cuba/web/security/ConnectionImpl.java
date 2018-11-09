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

package com.haulmont.cuba.web.security;

import com.haulmont.bali.events.EventHub;
import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.*;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.security.events.*;
import com.haulmont.cuba.web.sys.VaadinSessionScope;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * Default {@link Connection} implementation for web-client.
 */
@Component(Connection.NAME)
@Scope(VaadinSessionScope.NAME)
public class ConnectionImpl implements Connection {

    private static final Logger log = LoggerFactory.getLogger(ConnectionImpl.class);

    @Inject
    protected AuthenticationService authenticationService;
    @Inject
    protected UserSessionService userSessionService;
    @Inject
    protected List<LoginProvider> loginProviders;

    @Inject
    protected Events events;
    @Inject
    protected Messages messages;
    @Inject
    protected BackgroundWorker backgroundWorker;
    @Inject
    protected GlobalConfig globalConfig;

    // initial or used on login IP of the user
    protected String userRemoteAddress = null;

    protected EventHub eventHub = new EventHub();

    @Override
    public void login(Credentials credentials) throws LoginException {
        backgroundWorker.checkUIAccess();

        preprocessCredentials(credentials);

        AuthenticationDetails authenticationDetails = loginInternal(credentials);

        ClientUserSession clientUserSession = createSession(authenticationDetails.getSession());
        if (credentials instanceof AnonymousUserCredentials) {
            clientUserSession.setAuthenticated(false);
        } else {
            clientUserSession.setAuthenticated(true);
        }

        UserSession previousSession = getSession();

        setSessionInternal(clientUserSession);

        publishUserConnectedEvent(credentials);

        fireStateChangeListeners(previousSession, clientUserSession);
    }

    protected ClientUserSession createSession(UserSession userSession) {
        return new ClientUserSession(userSession);
    }

    protected void preprocessCredentials(Credentials credentials) {
        if (credentials instanceof AbstractClientCredentials) {
            AbstractClientCredentials clientCredentials = (AbstractClientCredentials) credentials;
            clientCredentials.setClientType(ClientType.WEB);
            clientCredentials.setClientInfo(makeClientInfo());
            clientCredentials.setTimeZone(detectTimeZone());

            String currentUserRemoteAddress = getUserRemoteAddress();
            // update userRemoteAddress if current HTTP request is available
            if (currentUserRemoteAddress != null) {
                this.userRemoteAddress = currentUserRemoteAddress;
            }

            clientCredentials.setIpAddress(userRemoteAddress);
        }
    }

    @Nullable
    protected String getUserRemoteAddress() {
        VaadinRequest currentRequest = VaadinService.getCurrentRequest();
        return currentRequest != null ? currentRequest.getRemoteAddr() : null;
    }

    protected String makeClientInfo() {
        // timezone info is passed only on VaadinSession creation
        WebBrowser webBrowser = getWebBrowserDetails();

        //noinspection UnnecessaryLocalVariable
        String serverInfo = String.format("Web (%s:%s/%s) %s",
                globalConfig.getWebHostName(),
                globalConfig.getWebPort(),
                globalConfig.getWebContextName(),
                webBrowser.getBrowserApplication());

        return serverInfo;
    }

    protected TimeZone detectTimeZone() {
        WebBrowser webBrowser = getWebBrowserDetails();

        int offset = webBrowser.getTimezoneOffset() / 1000 / 60;
        char sign = offset >= 0 ? '+' : '-';
        int absOffset = Math.abs(offset);

        String hours = StringUtils.leftPad(String.valueOf(absOffset / 60), 2, '0');
        String minutes = StringUtils.leftPad(String.valueOf(absOffset % 60), 2, '0');

        return TimeZone.getTimeZone("GMT" + sign + hours + minutes);
    }

    protected WebBrowser getWebBrowserDetails() {
        // timezone info is passed only on VaadinSession creation
        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        VaadinRequest currentRequest = VaadinService.getCurrentRequest();
        // update web browser instance if current request is not null
        // it can be null in case of background/async processing of login request
        if (currentRequest != null) {
            webBrowser.updateRequestDetails(currentRequest);
        }
        return webBrowser;
    }

    protected AuthenticationDetails loginInternal(Credentials credentials) throws LoginException {
        Class<? extends Credentials> credentialsClass = credentials.getClass();

        AuthenticationDetails details = null;
        try {
            publishBeforeLoginEvent(credentials);

            List<LoginProvider> providers = getProviders();

            for (LoginProvider provider : providers) {
                if (!provider.supports(credentialsClass)) {
                    continue;
                }

                log.trace("Login attempt using {}", provider.getClass().getName());

                try {
                    details = provider.login(credentials);

                    if (details != null) {
                        log.trace("Login successful for {}", credentials);

                        // publish login success
                        publishUserSessionStartedEvent(credentials, details);

                        return details;
                    }
                } catch (LoginException e) {
                    // publish auth fail
                    publishLoginFailed(credentials, provider, e);

                    throw e;
                } catch (RuntimeException re) {
                    InternalAuthenticationException ie =
                            new InternalAuthenticationException("Exception is thrown by login provider", re);

                    // publish auth fail
                    publishLoginFailed(credentials, provider, ie);

                    throw ie;
                }
            }
        } finally {
            publishAfterLoginEvent(credentials, details);
        }

        throw new UnsupportedCredentialsException(
                "Unable to find login provider that supports credentials class "
                        + credentialsClass.getName());
    }

    protected void fireStateChangeListeners(UserSession previousSession, UserSession newSession) {
        StateChangeEvent event = new StateChangeEvent(this, previousSession, newSession);
        eventHub.publish(StateChangeEvent.class, event);
    }

    protected void fireSubstitutionListeners() {
        UserSubstitutedEvent event = new UserSubstitutedEvent(this);
        eventHub.publish(UserSubstitutedEvent.class, event);
    }

    protected void publishUserConnectedEvent(Credentials credentials) {
        events.publish(new UserConnectedEvent(this, credentials));
    }

    protected void publishBeforeLoginEvent(Credentials credentials) throws LoginException {
        try {
            events.publish(new BeforeLoginEvent(credentials));
        } catch (UndeclaredThrowableException e) {
            rethrowLoginException(e);
        }
    }

    protected void publishAfterLoginEvent(Credentials credentials, AuthenticationDetails authenticationDetails) {
        events.publish(new AfterLoginEvent(credentials, authenticationDetails));
    }

    protected void publishLoginFailed(Credentials credentials, LoginProvider provider, LoginException e)
            throws LoginException {
        try {
            events.publish(new LoginFailureEvent(credentials, provider, e));
        } catch (UndeclaredThrowableException re) {
            rethrowLoginException(re);
        }
    }

    protected void publishUserSessionStartedEvent(Credentials credentials, AuthenticationDetails authenticationDetails) {
        events.publish(new UserSessionStartedEvent(this, credentials, authenticationDetails));
    }

    protected void rethrowLoginException(RuntimeException e) throws LoginException {
        Throwable cause = e.getCause();
        if (cause instanceof LoginException) {
            throw (LoginException) cause;
        } else {
            throw e;
        }
    }

    protected ClientUserSession getSessionInternal() {
        return (ClientUserSession) VaadinSession.getCurrent().getAttribute(UserSession.class);
    }

    protected void setSessionInternal(ClientUserSession userSession) {
        VaadinSession.getCurrent().setAttribute(UserSession.class, userSession);
        if (userSession != null) {
            AppContext.setSecurityContext(new SecurityContext(userSession));
        } else {
            AppContext.setSecurityContext(null);
        }
    }

    @Override
    public void logout() {
        backgroundWorker.checkUIAccess();

        ClientUserSession session = getSessionInternal();

        if (session == null) {
            throw new IllegalStateException("There is no active session");
        }
        if (!session.isAuthenticated()) {
            throw new IllegalStateException("Active session is not authenticated");
        }

        if (session.isAuthenticated()) {
            try {
                authenticationService.logout();
            } catch (NoUserSessionException e) {
                log.debug("An attempt to perform logout for expired session: {}", session, e);
            }
        }

        publishUserSessionFinishedEvent(session);

        UserSession previousSession = getSession();

        setSessionInternal(null);

        eventHub.unsubscribe(UserSubstitutedEvent.class);

        publishDisconnectedEvent(previousSession);

        fireStateChangeListeners(previousSession, null);
    }

    protected void publishUserSessionFinishedEvent(UserSession session) {
        events.publish(new UserSessionFinishedEvent(this, session));
    }

    protected void publishUserSessionSubstitutedEvent(UserSession previousSession, UserSession session) {
        events.publish(new UserSessionSubstitutedEvent(this, previousSession, session));
    }

    protected void publishDisconnectedEvent(UserSession previousSession) {
        events.publish(new UserDisconnectedEvent(this, previousSession));
    }

    @Override
    @Nullable
    public UserSession getSession() {
        return getSessionInternal();
    }

    @Override
    public void substituteUser(User substitutedUser) {
        UserSession previousSession = getSession();

        UserSession session = authenticationService.substituteUser(substitutedUser);

        ClientUserSession clientUserSession = createSession(session);
        clientUserSession.setAuthenticated(true);

        setSessionInternal(clientUserSession);

        publishUserSessionSubstitutedEvent(previousSession, clientUserSession);

        fireSubstitutionListeners();
    }

    @Override
    public boolean isConnected() {
        return getSessionInternal() != null;
    }

    @Override
    public boolean isAuthenticated() {
        ClientUserSession session = getSessionInternal();
        return session != null && session.isAuthenticated();
    }

    protected List<LoginProvider> getProviders() {
        return loginProviders;
    }

    @Override
    public boolean isAlive() {
        if (!isConnected()) {
            return false;
        }

        UserSession session = getSession();
        if (session == null) {
            return false;
        }

        try {
            userSessionService.getUserSession(session.getId());
        } catch (NoUserSessionException ignored) {
            return false;
        }

        return true;
    }

    @Override
    public void addStateChangeListener(Consumer<StateChangeEvent> listener) {
        eventHub.subscribe(StateChangeEvent.class, listener);
    }

    @Override
    public void removeStateChangeListener(Consumer<StateChangeEvent> listener) {
        eventHub.unsubscribe(StateChangeEvent.class, listener);
    }

    @Override
    public void addUserSubstitutionListener(Consumer<UserSubstitutedEvent> listener) {
        eventHub.subscribe(UserSubstitutedEvent.class, listener);
    }

    @Override
    public void removeUserSubstitutionListener(Consumer<UserSubstitutedEvent> listener) {
        eventHub.unsubscribe(UserSubstitutedEvent.class, listener);
    }

    @PostConstruct
    protected void init() {
        this.userRemoteAddress = getUserRemoteAddress();
    }
}