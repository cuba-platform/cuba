/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
public class Connection {

    private List<ConnectionListener> listeners = new ArrayList<ConnectionListener>();

    protected boolean connected;

    protected UserSession session;

    protected Log log = LogFactory.getLog(getClass());

    public void login(String login, String password, Locale locale) throws LoginException {
        LoginService loginService = AppBeans.get(LoginService.NAME);
        UserSession userSession = loginService.login(login, password, locale);
        session = new ClientUserSession(userSession);
        AppContext.setSecurityContext(new SecurityContext(session));

        updateSessionClientInfo();

        connected = true;
        fireConnectionListeners();
    }

    protected void updateSessionClientInfo() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            session.setAddress(address.getHostName() + " (" + address.getHostAddress() + ")");
        } catch (UnknownHostException e) {
            log.warn("Unable to obtain local IP address", e);
        }
        session.setClientInfo(makeClientInfo());
    }

    protected String makeClientInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("os{");
        sb.append("name=").append(System.getProperty("os.name"));
        sb.append(", arch=").append(System.getProperty("os.arch"));
        sb.append(", version=").append(System.getProperty("os.version"));
        sb.append("}, ");
        sb.append("java{");
        sb.append("vendor=").append(System.getProperty("java.vendor"));
        sb.append(", version=").append(System.getProperty("java.version"));
        sb.append("}");
        return sb.toString();
    }

    public void logout() {
        try {
            LoginService loginService = AppBeans.get(LoginService.NAME);
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

    protected void fireConnectionListeners() throws LoginException {
        for (ConnectionListener listener : listeners) {
            listener.connectionStateChanged(this);
        }
    }
}
