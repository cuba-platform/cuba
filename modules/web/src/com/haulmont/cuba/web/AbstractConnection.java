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
package com.haulmont.cuba.web;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.IpMatcher;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * Abstract class that encapsulates common connection behaviour for web-client.
 */
public abstract class AbstractConnection implements Connection {

    private static final Logger log = LoggerFactory.getLogger(AbstractConnection.class);

    protected List<ConnectionListener> connectionListeners = new ArrayList<>();
    protected List<UserSubstitutionListener> userSubstitutionListeners = new ArrayList<>();

    protected boolean connected;

    @Inject
    protected AuthenticationService authenticationService;
    @Inject
    protected TrustedClientService trustedClientService;
    @Inject
    protected UserSessionService userSessionService;
    @Inject
    protected Messages messages;
    @Inject
    protected GlobalConfig globalConfig;

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public boolean isAuthenticated() {
        if (!connected) {
            return false;
        }

        UserSession session = getSession();
        return session instanceof ClientUserSession
                && ((ClientUserSession) session).isAuthenticated();
    }

    @Override
    @Nullable
    public UserSession getSession() {
        return VaadinSession.getCurrent().getAttribute(UserSession.class);
    }

    protected void setSession(ClientUserSession clientUserSession) {
        VaadinSession.getCurrent().setAttribute(UserSession.class, clientUserSession);
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
    public void update(UserSession session, SessionMode sessionMode) throws LoginException {
        update(session, sessionMode, null);
    }

    @Override
    public void update(UserSession session, SessionMode sessionMode,
                       @Nullable Consumer<UserSessionInitEvent> sessionInitializer) throws LoginException {
        LoginContextParameters lcp = getLoginContextParameters();

        ClientUserSession clientUserSession = new ClientUserSession(session);
        clientUserSession.setAuthenticated(sessionMode == SessionMode.AUTHENTICATED);

        setSession(clientUserSession);

        connected = true;

        try {
            internalLogin(clientUserSession, sessionInitializer);
        } catch (LoginException | RuntimeException e) {
            internalLogout(false);

            setLoginContextParameters(lcp);

            throw e;
        } catch (Exception e) {
            internalLogout(false);

            setLoginContextParameters(lcp);

            throw new RuntimeException("Unable to login internal", e);
        }
    }

    protected LoginContextParameters getLoginContextParameters() {
        return new LoginContextParameters(AppContext.getSecurityContext(), connected, (ClientUserSession) getSession());
    }

    protected void setLoginContextParameters(LoginContextParameters lcp) {
        setSession(lcp.getUserSession());
        AppContext.setSecurityContext(lcp.getSecurityContext());
        this.connected = lcp.isConnected();
    }

    protected void internalLogin(UserSession session, @Nullable Consumer<UserSessionInitEvent> sessionInitializer)
            throws LoginException {
        AppContext.setSecurityContext(new SecurityContext(session));

        App app = App.getInstance();

        boolean sessionIsAuthenticated = true;
        if (session instanceof ClientUserSession) {
            sessionIsAuthenticated = ((ClientUserSession) session).isAuthenticated();
        }

        if (sessionIsAuthenticated && !StringUtils.isBlank(session.getUser().getIpMask())) {
            IpMatcher ipMatcher = new IpMatcher(session.getUser().getIpMask());
            if (!ipMatcher.match(app.getClientAddress())) {
                log.info("IP address {} is not permitted for user {}", app.getClientAddress(), session.getUser());

                throw new LoginException(messages.getMainMessage("login.invalidIP"));
            }
        }

        String clientInfo = makeClientInfo();
        session.setClientInfo(clientInfo);

        if (Boolean.TRUE.equals(session.getUser().getTimeZoneAuto())) {
            session.setTimeZone(detectTimeZone());
        }

        if (sessionInitializer != null) {
            sessionInitializer.accept(new UserSessionInitEvent(this, session));
        }

        fireConnectionListeners();

        log.debug("Logged in: user={}, ip={}, clientInfo={}",
                session.getUser().getLogin(), app.getClientAddress(), clientInfo);
    }

    protected String makeClientInfo() {
        // timezone info is passed only on VaadinSession creation
        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        webBrowser.updateRequestDetails(VaadinService.getCurrentRequest());

        //noinspection UnnecessaryLocalVariable
        String serverInfo = String.format("Web (%s:%s/%s) %s",
                globalConfig.getWebHostName(),
                globalConfig.getWebPort(),
                globalConfig.getWebContextName(),
                webBrowser.getBrowserApplication());

        return serverInfo;
    }

    protected TimeZone detectTimeZone() {
        // timezone info is passed only on VaadinSession creation
        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        webBrowser.updateRequestDetails(VaadinService.getCurrentRequest());

        int offset = webBrowser.getTimezoneOffset() / 1000 / 60;
        char sign = offset >= 0 ? '+' : '-';
        int absOffset = Math.abs(offset);

        String hours = StringUtils.leftPad(String.valueOf(absOffset / 60), 2, '0');
        String minutes = StringUtils.leftPad(String.valueOf(absOffset % 60), 2, '0');

        return TimeZone.getTimeZone("GMT" + sign + hours + minutes);
    }

    @Override
    public void substituteUser(User substitutedUser) {
        ClientUserSession clientUserSession = new ClientUserSession(authenticationService.substituteUser(substitutedUser));
        clientUserSession.setAuthenticated(true);

        setSession(clientUserSession);

        fireSubstitutionListeners();
    }

    @Override
    public void logout() {
        internalLogout();
        try {
            fireConnectionListeners();
        } catch (LoginException e) {
            log.warn("Exception on logout:", e);
        }
    }

    protected void internalLogout() {
        internalLogout(true);
    }

    protected void internalLogout(boolean disconnect) {
        if (getSession() instanceof ClientUserSession
                && ((ClientUserSession) getSession()).isAuthenticated()) {
            authenticationService.logout();
        }
        AppContext.setSecurityContext(null);
        connected = false;
        setSession(null);
        if (disconnect) {
            userSubstitutionListeners.clear();
        }
    }

    @Override
    public void addConnectionListener(ConnectionListener listener) {
        if (!connectionListeners.contains(listener)) {
            connectionListeners.add(listener);
        }
    }

    @Override
    public void removeConnectionListener(ConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    @Override
    public void addSubstitutionListener(UserSubstitutionListener listener) {
        if (!userSubstitutionListeners.contains(listener)) {
            userSubstitutionListeners.add(listener);
        }
    }

    @Override
    public void removeSubstitutionListener(UserSubstitutionListener listener) {
        userSubstitutionListeners.remove(listener);
    }

    protected void fireConnectionListeners() throws LoginException {
        for (ConnectionListener listener : new ArrayList<>(connectionListeners)) {
            listener.connectionStateChanged(this);
        }
    }

    protected void fireSubstitutionListeners() {
        for (UserSubstitutionListener listener : new ArrayList<>(userSubstitutionListeners)) {
            listener.userSubstituted(this);
        }
    }

    protected static class LoginContextParameters {
        private final SecurityContext securityContext;
        private final boolean connected;
        private final ClientUserSession userSession;

        public LoginContextParameters(SecurityContext securityContext, boolean connected, ClientUserSession userSession) {
            this.securityContext = securityContext;
            this.connected = connected;
            this.userSession = userSession;
        }

        public SecurityContext getSecurityContext() {
            return securityContext;
        }

        public boolean isConnected() {
            return connected;
        }

        public ClientUserSession getUserSession() {
            return userSession;
        }
    }
}