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

package com.haulmont.cuba.portal.sys;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.portal.App;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.portal.ConnectionListener;
import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.portal.sys.security.PortalSecurityContext;
import com.haulmont.cuba.portal.sys.security.PortalSessionFactory;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 */
public class PortalConnection implements Connection {

    protected final List<ConnectionListener> listeners = new ArrayList<>();

    protected volatile boolean connected;

    protected volatile PortalSession session;

    protected Logger log = LoggerFactory.getLogger(Connection.class);

    @Inject
    protected Configuration configuration;

    @Inject
    protected LoginService loginService;

    @Inject
    protected PortalSessionFactory portalSessionFactory;

    @Override
    public synchronized void login(String login, String password, Locale locale,
                                   @Nullable String ipAddress, @Nullable String clientInfo) throws LoginException {
        UserSession userSession = doLogin(login, password, locale);
        session = portalSessionFactory.createPortalSession(userSession, locale);

        // replace security context
        PortalSecurityContext portalSecurityContext = new PortalSecurityContext(session);
        portalSecurityContext.setPortalApp(App.getInstance());

        // middleware service is called just below
        AppContext.setSecurityContext(portalSecurityContext);
        session.setAddress(ipAddress);

        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        String serverInfo = "Portal (" +
                globalConfig.getWebHostName() + ":" +
                globalConfig.getWebPort() + "/" +
                globalConfig.getWebContextName() + ") ";

        session.setClientInfo(serverInfo + clientInfo);
        session.setAuthenticated(true);

        connected = true;
        fireConnectionListeners();
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param login     login name
     * @param password  encrypted password
     * @param locale    client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLogin(String login, String password, Locale locale) throws LoginException {
        return loginService.login(login, password, locale);
    }

    @Override
    public synchronized void login(Locale locale, @Nullable String ipAddress, @Nullable String clientInfo) throws LoginException {
        // get anonymous session
        session = portalSessionFactory.createPortalSession(null, locale);

        // replace security context
        PortalSecurityContext portalSecurityContext = new PortalSecurityContext(session);
        portalSecurityContext.setPortalApp(App.getInstance());

        // middleware service is called just below
        AppContext.setSecurityContext(portalSecurityContext);
        if (StringUtils.isNotBlank(ipAddress)) {
            session.setAddress(ipAddress);
        }
        if (StringUtils.isNotBlank(clientInfo)) {
            session.setClientInfo(clientInfo);
        }
        connected = true;
    }

    @Override
    public synchronized void logout() {
        try {
            loginService.logout();
            AppContext.setSecurityContext(null);
        } catch (Exception e) {
            log.warn("Error on logout", e);
        }

        connected = false;
        try {
            fireConnectionListeners();
        } catch (LoginException e) {
            log.warn("Error on logout", e);
        }
        session = null;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public PortalSession getSession() {
        return session;
    }

    @Override
    public synchronized void update(PortalSession session) throws LoginException {
        internalLogout();

        this.session = session;
        connected = true;

        try {
            internalLogin();
        } catch (LoginException | RuntimeException e) {
            internalLogout();
            throw e;
        } catch (Exception e) {
            internalLogout();
            throw new RuntimeException(e);
        }
    }

    protected void internalLogin() throws LoginException {
        PortalSecurityContext securityContext = new PortalSecurityContext(session);
        securityContext.setPortalApp(App.getInstance());

        AppContext.setSecurityContext(securityContext);

        fireConnectionListeners();

        if (log.isDebugEnabled()) {
            log.debug(String.format("Logged in: user=%s", session.getUser().getLogin()));
        }
    }

    protected void internalLogout() {
        if (session != null && session.isAuthenticated()) {
            loginService.logout();
        }

        AppContext.setSecurityContext(null);

        connected = false;
        session = null;
    }

    @Override
    public void addListener(ConnectionListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener))
                listeners.add(listener);
        }
    }

    @Override
    public void removeListener(ConnectionListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void fireConnectionListeners() throws LoginException {
        List<ConnectionListener> activeListeners;
        synchronized (listeners) {
            activeListeners = new ArrayList<>(listeners);
        }

        for (ConnectionListener listener : activeListeners) {
            listener.connectionStateChanged(this);
        }
    }
}