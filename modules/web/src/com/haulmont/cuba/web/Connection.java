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

import com.haulmont.cuba.security.service.LoginService;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Locale;

public class Connection
{
    private WebApplication app;

    private Set<ConnectionListener> listeners = new HashSet<ConnectionListener>();

    private boolean connected;
    private UserSession session;

    public Connection(WebApplication app) {
        this.app = app;
    }

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
            profiles = ls.authenticate(login, password, Locale.getDefault()); // TODO KK: pass client locale
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        return profiles;
    }

    public void login(String login, String password, String profileName) {
        LoginService ls = ServiceLocator.lookup(LoginService.JNDI_NAME);
        try {
            session = ls.login(login, password, profileName, Locale.getDefault()); // TODO KK: pass client locale
            connected = true;
            fireListeners();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        if (!connected)
            return;
        
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
