/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class PortalConnection implements Connection {

    protected final List<ConnectionListener> listeners = new ArrayList<>();

    protected volatile boolean connected;

    protected volatile PortalSession session;

    protected Log log = LogFactory.getLog(Connection.class);

    @Inject
    protected Configuration configuration;

    @Inject
    protected LoginService loginService;

    @Inject
    protected PortalSessionFactory portalSessionFactory;

    @Override
    public synchronized void login(String login, String password, Locale locale,
                                   @Nullable String ipAddress, @Nullable String clientInfo) throws LoginException {
        UserSession userSession = loginService.login(login, password, locale);
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
        loginService.logout();

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