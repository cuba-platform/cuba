/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.IpMatcher;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that encapsulates common connection behaviour for web-client.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractConnection implements Connection {

    private static Log log = LogFactory.getLog(AbstractConnection.class);

    private Map<ConnectionListener, Object> connListeners = new HashMap<ConnectionListener, Object>();
    private Map<UserSubstitutionListener, Object> usListeners = new HashMap<UserSubstitutionListener, Object>();

    private boolean connected;
    private UserSession session;

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    @Nullable
    public UserSession getSession() {
        return session;
    }

    @Override
    public void update(UserSession session) throws LoginException {
        this.session = new ClientUserSession(session);
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
        AppContext.setSecurityContext(new SecurityContext(session));

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

    @Override
    public void substituteUser(User substitutedUser) {
        session = new ClientUserSession(getLoginService().substituteUser(substitutedUser));
        fireSubstitutionListeners();
    }

    @Override
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

        AppContext.setSecurityContext(null);

        connected = false;
        session = null;
    }

    @Override
    public void addListener(ConnectionListener listener) {
        connListeners.put(listener, null);
    }

    @Override
    public void removeListener(ConnectionListener listener) {
        connListeners.remove(listener);
    }

    @Override
    public void addListener(UserSubstitutionListener listener) {
        usListeners.put(listener, null);
    }

    @Override
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
