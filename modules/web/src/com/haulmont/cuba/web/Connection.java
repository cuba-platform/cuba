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

import java.util.HashSet;
import java.util.Set;

public class Connection
{
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
        session = getLoginService().login(login, password, profileName, App.getInstance().getLocale());
        connected = true;
        this.login = login;
        this.password = password;
        fireConnectionListeners();
    }

    public void loginActiveDirectory(String activeDirectoryUser) throws LoginException {
        loginActiveDirectory(activeDirectoryUser, null);
    }

    public void loginActiveDirectory(String activeDirectoryUser, String profileName) throws LoginException {
        session = getLoginService().loginActiveDirectory(activeDirectoryUser, profileName, App.getInstance().getLocale());
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
        fireConnectionListeners();
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    private void fireConnectionListeners() {
        for (ConnectionListener listener : listeners) {
            listener.connectionStateChanged(this);
        }
    }
}
