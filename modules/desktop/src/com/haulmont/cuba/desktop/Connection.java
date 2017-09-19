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

package com.haulmont.cuba.desktop;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.SessionParams;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Connection {

    private final Logger log = LoggerFactory.getLogger(Connection.class);

    private List<ConnectionListener> listeners = new ArrayList<>();

    protected boolean connected;

    protected UserSession session;

    public void login(String login, String password, Locale locale) throws LoginException {
        UserSession userSession = doLogin(login, password, locale, getLoginParams());
        ClientUserSession clientUserSession = new ClientUserSession(userSession);
        clientUserSession.setAuthenticated(true);

        session = clientUserSession;
        AppContext.setSecurityContext(new SecurityContext(session));
        log.info("Logged in: {}", session);

        updateSessionClientInfo();

        connected = true;
        fireConnectionListeners();
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.auth.AuthenticationService}.
     * Can be overridden to change login logic.
     *
     * @param login       login name
     * @param password    encrypted password
     * @param locale      client locale
     * @param loginParams login parameters
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLogin(String login, String password, Locale locale, Map<String, Object> loginParams)
            throws LoginException {
        AbstractClientCredentials credentials = new LoginPasswordCredentials(login, password, locale);
        setCredentialsParams(credentials, loginParams);

        AuthenticationService authenticationService = AppBeans.get(AuthenticationService.NAME);
        return authenticationService.login(credentials).getSession();
    }

    protected void setCredentialsParams(AbstractClientCredentials credentials, Map<String, Object> loginParams) {
        credentials.setClientInfo(makeClientInfo());
        credentials.setClientType(ClientType.DESKTOP);

        Optional<InetAddress> address = Optional.empty();
        try {
            address = Optional.ofNullable(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            log.warn("Unable to obtain local IP address", e);
        }

        if (address.isPresent()) {
            credentials.setIpAddress(address.get().getHostAddress());
            credentials.setHostName(address.get().getHostName());
        }

        credentials.setParams(loginParams);

        Configuration configuration = AppBeans.get(Configuration.class);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        if (!globalConfig.getLocaleSelectVisible()) {
            credentials.setOverrideLocale(false);
        }
    }

    protected Map<String, Object> getLoginParams() {
        Optional<InetAddress> address = Optional.empty();
        try {
            address = Optional.ofNullable(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            log.warn("Unable to obtain local IP address", e);
        }

        return ParamsMap.of(
                ClientType.class.getName(), ClientType.DESKTOP.name(),
                SessionParams.HOST_NAME.getId(), address.map(InetAddress::getHostName).orElse(null),
                SessionParams.IP_ADDERSS.getId(), address.map(InetAddress::getHostAddress).orElse(null),
                SessionParams.CLIENT_INFO.getId(), makeClientInfo()
        );
    }

    protected void updateSessionClientInfo() {
        if (Boolean.TRUE.equals(session.getUser().getTimeZoneAuto())) {
            session.setTimeZone(TimeZone.getDefault());
        }
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
            AuthenticationService authenticationService = AppBeans.get(AuthenticationService.NAME);
            authenticationService.logout();
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