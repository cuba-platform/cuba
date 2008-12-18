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

import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.app.LoginService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Connection
{
    private Set<ConnectionListener> listeners = new HashSet<ConnectionListener>();

    private boolean connected;
    private UserSession session;

    public boolean isConnected() {
        return connected;
    }

    public UserSession getSession() {
        return session;
    }

    public List<Profile> authenticate(String login, String password) {
        LoginService ls = ServiceLocator.lookup(LoginService.JNDI_NAME);
        List<Profile> profiles;
        try {
            profiles = ls.authenticate(login, password, App.getInstance().getLocale());
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        return profiles;
    }

    public void login(String login, String password, String profileName) {
        LoginService ls = ServiceLocator.lookup(LoginService.JNDI_NAME);
        try {
            session = ls.login(login, password, profileName, App.getInstance().getLocale());
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        connected = true;
        fireListeners();
    }

    public void logout() {
        if (!connected)
            return;

        LoginService ls = ServiceLocator.lookup(LoginService.JNDI_NAME);
        ls.logout();

        connected = false;
        session = null;
        fireListeners();
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    private void fireListeners() {
        for (ConnectionListener listener : listeners) {
            listener.connectionStateChanged(this);
        }
    }
}
