/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 12:43:55
 *
 * $Id: AbstractConnection.java 3253 2010-11-25 12:41:14Z gorodnov $
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.IpMatcher;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.web.sys.WebSecurityUtils;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Connection to the middleware.
 * <br>Can be obtained via {@link com.haulmont.cuba.web.App#getConnection()} method.
 */
public abstract class AbstractConnection implements Connection {
    private static Log log = LogFactory.getLog(AbstractConnection.class);

    private Map<ConnectionListener, Object> connListeners = new HashMap<ConnectionListener, Object>();
    private Map<UserSubstitutionListener, Object> usListeners = new HashMap<UserSubstitutionListener, Object>();

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

    public void update(UserSession session) throws LoginException {
        this.session = session;
        connected = true;

        try {
            internalLogin();
        } catch (LoginException e) {
            internalLogout();
            throw e;
        } catch (RuntimeException e) {
            internalLogout();
            throw e;
        } catch (Exception e) {
            internalLogout();
            throw new RuntimeException(e);
        }
    }

    void internalLogin() throws LoginException {
        WebSecurityUtils.setSecurityAssociation(session.getUser().getLogin(), session.getId());

        App app = App.getInstance();

        if (!StringUtils.isBlank(session.getUser().getIpMask())) {
            IpMatcher ipMatcher = new IpMatcher(session.getUser().getIpMask());
            if (!ipMatcher.match(app.getClientAddress())) {
                log.info(String.format("IP address %s is not permitted for user %s", app.getClientAddress(), session.getUser().toString()));
                throw new LoginException(MessageProvider.getMessage(getClass(), "login.invalidIP"));
            }
        }

        session.setAddress(app.getClientAddress());
        WebBrowser browser = ((WebApplicationContext) app.getContext()).getBrowser();
        session.setClientInfo(browser.getBrowserApplication());

        fireConnectionListeners();

        if (log.isDebugEnabled()) {
            log.debug(String.format("Logged in: user=%s, ip=%s, browser=%s",
                    session.getUser().getLogin(), app.getClientAddress(), browser.getBrowserApplication()));
        }
    }

    /**
     * Substitute user. Current user session will get rights and constraints of substituted user.
     */
    public void substituteUser(User substitutedUser) {
        session = getLoginService().substituteUser(substitutedUser);
        fireSubstitutionListeners();
    }

    /**
     * Perform logout
     */
    public String logout() {
        if (!connected)
            return null;
        internalLogout();
        try {
            fireConnectionListeners();
        } catch (LoginException e) {
            log.warn("Exception on logout:", e);
        }
        return null;
    }

    void internalLogout() {
        LoginService ls = getLoginService();
        ls.logout();

        WebSecurityUtils.clearSecurityAssociation();

        connected = false;
        session = null;
    }

    public void addListener(ConnectionListener listener) {
        connListeners.put(listener, null);
    }

    public void removeListener(ConnectionListener listener) {
        connListeners.remove(listener);
    }

    public void addListener(UserSubstitutionListener listener) {
        usListeners.put(listener, null);
    }

    public void removeListener(UserSubstitutionListener listener) {
        usListeners.remove(listener);
    }

    void fireConnectionListeners() throws LoginException {
        for (ConnectionListener listener : connListeners.keySet()) {
            listener.connectionStateChanged(this);
        }
    }

    void fireSubstitutionListeners() {
        for (UserSubstitutionListener listener : usListeners.keySet()) {
            listener.userSubstituted(this);
        }
    }

    protected LoginService getLoginService() {
        LoginService ls = ServiceLocator.lookup(LoginService.NAME);
        return ls;
    }
}
