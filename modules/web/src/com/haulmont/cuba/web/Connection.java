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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Connection
{
    private Log log = LogFactory.getLog(Connection.class);

    private Set<ConnectionListener> listeners = new HashSet<ConnectionListener>();

    private boolean connected;
    private UserSession session;
    private String login;
    private String password;

    public boolean isConnected() {
        return connected;
    }

    public UserSession getSession() {
        return session;
    }

    private LoginService getLoginService() {
        LoginService ls = ServiceLocator.lookup(LoginService.JNDI_NAME);
        return ls;
    }

    public void login(String login, String password) throws LoginException {
        login(login, password, null);
    }

    public void login(String login, String password, String profileName) throws LoginException {
        session = getLoginService().login(login, password, App.getInstance().getLocale());
        connected = true;
        this.login = login;
        this.password = password;
        ServerSecurityUtils.setSecurityAssociation(session.getLogin(), session.getId());
        fireConnectionListeners();
    }

    public void loginActiveDirectory(String activeDirectoryUser) throws LoginException {
        loginActiveDirectory(activeDirectoryUser, null);
    }

    public void loginActiveDirectory(String activeDirectoryUser, String profileName) throws LoginException {
        session = getLoginService().loginActiveDirectory(activeDirectoryUser, App.getInstance().getLocale());
        connected = true;
        this.login = activeDirectoryUser;
        this.password = null;
        fireConnectionListeners();
    }

    public void changeProfile(String profile) throws LoginException {
        logout();
        if (password != null) {
            login(login, password, profile);
        }
        else {
            loginActiveDirectory(login, profile);
        }
    }

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

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    private void fireConnectionListeners() throws LoginException {
        for (ConnectionListener listener : listeners) {
            listener.connectionStateChanged(this);
        }
    }
}
