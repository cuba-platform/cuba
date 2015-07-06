/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class Connection {

    private List<ConnectionListener> listeners = new ArrayList<>();

    protected boolean connected;

    protected UserSession session;

    protected Log log = LogFactory.getLog(getClass());

    public void login(String login, String password, Locale locale) throws LoginException {
        UserSession userSession = doLogin(login, password, locale);
        session = new ClientUserSession(userSession);
        AppContext.setSecurityContext(new SecurityContext(session));
        log.info("Logged in: " + session);

        updateSessionClientInfo();

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
        LoginService loginService = AppBeans.get(LoginService.NAME);
        return loginService.login(login, password, locale);
    }

    protected Map<String, Object> getLoginParams() {
        return ParamsMap.of(ClientType.class.getSimpleName(), ClientType.DESKTOP.name());
    }

    protected void updateSessionClientInfo() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            session.setAddress(address.getHostName() + " (" + address.getHostAddress() + ")");
        } catch (UnknownHostException e) {
            log.warn("Unable to obtain local IP address", e);
        }
        session.setClientInfo(makeClientInfo());

        if (Boolean.TRUE.equals(session.getUser().getTimeZoneAuto()))
            session.setTimeZone(TimeZone.getDefault());
    }

    protected String makeClientInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Desktop ")
                .append("os{")
                .append("name=").append(System.getProperty("os.name"))
                .append(", arch=").append(System.getProperty("os.arch"))
                .append(", version=").append(System.getProperty("os.version"))
                .append("}, ")
                .append("java{")
                .append("vendor=").append(System.getProperty("java.vendor"))
                .append(", version=").append(System.getProperty("java.version"))
                .append("}");
        return sb.toString();
    }

    public void logout() {
        try {
            LoginService loginService = AppBeans.get(LoginService.NAME);
            loginService.logout();
            AppContext.setSecurityContext(null);
            log.info("Logged out: " + session);
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
