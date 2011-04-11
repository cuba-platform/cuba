/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class Connection {

    private List<ConnectionListener> listeners = new ArrayList<ConnectionListener>();

    private boolean connected;

    public void login(String login, String password, Locale locale) throws LoginException {
        connected = true;
        fireConnectionListeners();
    }

    public void logout() {
        connected = false;
        try {
            fireConnectionListeners();
        } catch (LoginException e) {
            //
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public UserSession getSession() {
        return null;
    }

    public void addListener(ConnectionListener listener) {
        if (!listeners.contains(listener))
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
