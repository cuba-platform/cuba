/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 12:43:55
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.gui.ServiceLocator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Connection to the middleware.
 * <br>Can be obtained via {@link com.haulmont.cuba.web.App#getConnection()} method.
 */
public class Connection
{
    private Log log = LogFactory.getLog(Connection.class);

    private Set<ConnectionListener> listeners = new HashSet<ConnectionListener>();

    private boolean connected;
    private UserSession session;

    /**
     * True if the web application is succesfully logged in to middleware and a user session exists.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Current user session. Null if not connected.
     */
    public UserSession getSession() {
        return session;
    }

    /**
     * Set user session for this connection
     */
    public void setSession(UserSession session) {
        this.session = session;
    }

    private LoginService getLoginService() {
        LoginService ls = ServiceLocator.lookup(LoginService.JNDI_NAME);
        return ls;
    }

    /**
     * Perform login
     * @param login login name
     * @param password encrypted password
     * @throws LoginException
     */
    public void login(String login, String password) throws LoginException {
        session = getLoginService().login(login, password, App.getInstance().getLocale());
        connected = true;
        ServerSecurityUtils.setSecurityAssociation(session.getUser().getLogin(), session.getId());
        fireConnectionListeners();
    }

    /**
     * Perform login using password stored in ActiveDirectory
     * @param login login name
     * @throws LoginException
     */
    public void loginActiveDirectory(String login) throws LoginException {
        session = getLoginService().loginActiveDirectory(login, App.getInstance().getLocale());
        connected = true;
        ServerSecurityUtils.setSecurityAssociation(session.getUser().getLogin(), session.getId());
        fireConnectionListeners();
    }

    /**
     * Perform logout
     */
    public void logout() {
        if (!connected)
            return;

        LoginService ls = getLoginService();
        ls.logout();

        connected = false;
        session = null;
        try {
            fireConnectionListeners();
        } catch (LoginException e) {
            log.warn("Exception on logout:", e);
        }
    }

    /**
     * Register connection listener
     */
    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregister connection listener
     */
    public void removeListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    private void fireConnectionListeners() throws LoginException {
        for (ConnectionListener listener : listeners) {
            listener.connectionStateChanged(this);
        }
    }
}
