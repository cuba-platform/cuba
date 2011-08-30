/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private UserSession session;

    private Log log = LogFactory.getLog(Connection.class);

    public void login(String login, String password, Locale locale) throws LoginException {
        LoginService loginService = ServiceLocator.lookup(LoginService.NAME);
        UserSession userSession = loginService.login(login, password, locale);
        session = new ClientUserSession(userSession);
        AppContext.setSecurityContext(new SecurityContext(session));

        connected = true;
        fireConnectionListeners();
    }

    public void logout() {
        try {
            LoginService loginService = ServiceLocator.lookup(LoginService.NAME);
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

    public boolean isConnected() {
        return connected;
    }

    public UserSession getSession() {
        return session;
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
