/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.IpMatcher;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.sys.VaadinSessionAwareSecurityContext;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that encapsulates common connection behaviour for web-client.
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractConnection implements Connection {

    protected Log log = LogFactory.getLog(getClass());

    protected Map<ConnectionListener, Object> connListeners = new HashMap<>();
    protected Map<UserSubstitutionListener, Object> usListeners = new HashMap<>();

    protected boolean connected;

    protected LoginService loginService = AppBeans.get(LoginService.NAME);
    protected UserSessionService userSessionService = AppBeans.get(UserSessionService.NAME);
    protected Messages messages = AppBeans.get(Messages.class);

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    @Nullable
    public UserSession getSession() {
        return VaadinSession.getCurrent().getAttribute(UserSession.class);
    }

    protected void setSession(ClientUserSession clientUserSession) {
        VaadinSession.getCurrent().setAttribute(UserSession.class, clientUserSession);
    }

    @Override
    public boolean isAlive() {
        if (!isConnected()) {
            return false;
        }

        UserSession session = getSession();
        if (session == null) {
            return false;
        }

        try {
            userSessionService.getUserSession(session.getId());
        } catch (NoUserSessionException ignored) {
            return false;
        }

        return true;
    }

    @Override
    public void update(UserSession session) throws LoginException {
        ClientUserSession clientUserSession = new ClientUserSession(session);

        setSession(clientUserSession);

        connected = true;

        try {
            internalLogin(clientUserSession);
        } catch (LoginException | RuntimeException e) {
            internalLogout();
            throw e;
        } catch (Exception e) {
            internalLogout();
            throw new RuntimeException(e);
        }
    }

    protected void internalLogin(UserSession session) throws LoginException {
        AppContext.setSecurityContext(new VaadinSessionAwareSecurityContext());

        App app = App.getInstance();

        if (!StringUtils.isBlank(session.getUser().getIpMask())) {
            IpMatcher ipMatcher = new IpMatcher(session.getUser().getIpMask());
            if (!ipMatcher.match(app.getClientAddress())) {
                log.info(String.format("IP address %s is not permitted for user %s", app.getClientAddress(), session.getUser().toString()));
                throw new LoginException(messages.getMessage(getClass(), "login.invalidIP"));
            }
        }

        session.setAddress(app.getClientAddress());
        String clientInfo = makeClientInfo();
        session.setClientInfo(clientInfo);

        fireConnectionListeners();

        if (log.isDebugEnabled()) {
            log.debug(String.format("Logged in: user=%s, ip=%s, clientInfo=%s",
                    session.getUser().getLogin(), app.getClientAddress(), clientInfo));
        }
    }

    protected String makeClientInfo() {
        Page page = AppUI.getCurrent().getPage();
        WebBrowser webBrowser = page.getWebBrowser();

        GlobalConfig globalConfig = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);
        String serverInfo = "Web (" +
                globalConfig.getWebHostName() + ":" +
                globalConfig.getWebPort() + "/" +
                globalConfig.getWebContextName() + ") ";

        return serverInfo + webBrowser.getBrowserApplication();
    }

    @Override
    public void substituteUser(User substitutedUser) {
        setSession(new ClientUserSession(loginService.substituteUser(substitutedUser)));
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

    protected void internalLogout() {
        loginService.logout();

        AppContext.setSecurityContext(null);
        usListeners.clear();
        connected = false;
        setSession(null);
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

    protected void fireConnectionListeners() throws LoginException {
        for (ConnectionListener listener : connListeners.keySet()) {
            listener.connectionStateChanged(this);
        }
    }

    protected void fireSubstitutionListeners() {
        for (UserSubstitutionListener listener : usListeners.keySet()) {
            listener.userSubstituted(this);
        }
    }
}