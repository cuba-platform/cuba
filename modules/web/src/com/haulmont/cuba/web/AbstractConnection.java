/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.IpMatcher;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Abstract class that encapsulates common connection behaviour for web-client.
 *
 */
public abstract class AbstractConnection implements Connection {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected Map<ConnectionListener, Object> connListeners = new HashMap<>();
    protected Map<UserSubstitutionListener, Object> usListeners = new HashMap<>();

    protected boolean connected;

    protected LoginService loginService = AppBeans.get(LoginService.NAME);
    protected UserSessionService userSessionService = AppBeans.get(UserSessionService.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);

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
            throw new RuntimeException("Unable to login internal", e);
        }
    }

    protected void internalLogin(UserSession session) throws LoginException {
        AppContext.setSecurityContext(new SecurityContext(session));

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

        if (Boolean.TRUE.equals(session.getUser().getTimeZoneAuto()))
            session.setTimeZone(detectTimeZone());

        fireConnectionListeners();

        if (log.isDebugEnabled()) {
            log.debug(String.format("Logged in: user=%s, ip=%s, clientInfo=%s",
                    session.getUser().getLogin(), app.getClientAddress(), clientInfo));
        }
    }

    protected String makeClientInfo() {
        Page page = AppUI.getCurrent().getPage();
        WebBrowser webBrowser = page.getWebBrowser();

        Configuration configuration = AppBeans.get(Configuration.NAME);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        String serverInfo = "Web (" +
                globalConfig.getWebHostName() + ":" +
                globalConfig.getWebPort() + "/" +
                globalConfig.getWebContextName() + ") ";

        return serverInfo + webBrowser.getBrowserApplication();
    }

    protected TimeZone detectTimeZone() {
        Page page = AppUI.getCurrent().getPage();
        WebBrowser webBrowser = page.getWebBrowser();

        int offset = webBrowser.getTimezoneOffset() / 1000 / 60;
        String hours = StringUtils.leftPad(String.valueOf(offset / 60), 2, '0');
        String mins = StringUtils.leftPad(String.valueOf(offset % 60), 2, '0');
        char sign = offset >= 0 ? '+' : '-';
        return TimeZone.getTimeZone("GMT" + sign + hours + mins);
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
    public void addConnectionListener(ConnectionListener listener) {
        connListeners.put(listener, null);
    }

    @Override
    public void removeConnectionListener(ConnectionListener listener) {
        connListeners.remove(listener);
    }

    @Override
    public void addSubstitutionListener(UserSubstitutionListener listener) {
        usListeners.put(listener, null);
    }

    @Override
    public void removeSubstitutionListener(UserSubstitutionListener listener) {
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