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

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.auth.RememberMeCredentials;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.SessionParams;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

/**
 * Default {@link Connection} implementation for web-client.
 */
@Component(Connection.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefaultConnection extends AbstractConnection implements ExternallyAuthenticatedConnection {

    @Inject
    protected WebAuthConfig webAuthConfig;
    @Inject
    protected CubaAuthProvider authProvider;

    @Override
    public void login(String login, String password, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        update(doLogin(login, password, locale, getLoginParams()), SessionMode.AUTHENTICATED);
    }

    @Override
    public void loginAnonymous(Locale locale) throws LoginException {
        UserSession session = doLoginAnonymous(locale);
        if (session == null) {
            throw new LoginException("Unable to obtain anonymous session");
        }
        session.setLocale(locale);

        update(session, SessionMode.ANONYMOUS);
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param login       login name
     * @param password    encrypted password
     * @param locale      client locale
     * @param loginParams login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLogin(String login, String password, Locale locale, Map<String, Object> loginParams)
            throws LoginException {
        AbstractClientCredentials credentials = new LoginPasswordCredentials(login, password, locale);
        setCredentialsParams(credentials, loginParams);
        return authenticationService.login(credentials).getSession();
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param locale client locale
     * @return obtained user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLoginAnonymous(Locale locale) throws LoginException {
        return trustedClientService.getAnonymousSession(webAuthConfig.getTrustedClientPassword());
    }

    @Override
    public void loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        update(doLoginByRememberMe(login, rememberMeToken, locale, getLoginParams()), SessionMode.AUTHENTICATED);
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param login           login name
     * @param rememberMeToken rememberMe token
     * @param locale          client locale
     * @param loginParams     login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLoginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> loginParams)
            throws LoginException {
        AbstractClientCredentials credentials = new RememberMeCredentials(login, rememberMeToken, locale);
        setCredentialsParams(credentials, loginParams);
        return authenticationService.login(credentials).getSession();
    }

    @Override
    public void loginAfterExternalAuthentication(String login, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        String password = webAuthConfig.getTrustedClientPassword();
        UserSession userSession = doLoginTrusted(login, password, locale, getLoginParams());
        update(userSession, SessionMode.AUTHENTICATED, sessionInitEvent -> {
            UserSession session = sessionInitEvent.getUserSession();
            session.setAttribute(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE, true);
            authProvider.userSessionLoggedIn(session);
        });
    }

    @Override
    public String logoutExternalAuthentication() {
        return authProvider.logout();
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param login       login name
     * @param password    encrypted password
     * @param locale      client locale
     * @param loginParams login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLoginTrusted(String login, String password, Locale locale, Map<String, Object> loginParams)
            throws LoginException {
        AbstractClientCredentials credentials = new TrustedClientCredentials(login, password, locale);
        setCredentialsParams(credentials, loginParams);
        return authenticationService.login(credentials).getSession();
    }

    protected void setCredentialsParams(AbstractClientCredentials credentials, Map<String, Object> loginParams) {
        credentials.setClientInfo(makeClientInfo());
        credentials.setClientType(ClientType.WEB);
        credentials.setIpAddress(App.getInstance().getClientAddress());
        credentials.setParams(loginParams);
        if (!globalConfig.getLocaleSelectVisible()) {
            credentials.setOverrideLocale(false);
        }
    }

    protected Map<String, Object> getLoginParams() {
        return ParamsMap.of(
                ClientType.class.getName(), ClientType.WEB.name(),
                SessionParams.IP_ADDERSS.getId(), App.getInstance().getClientAddress(),
                SessionParams.CLIENT_INFO.getId(), makeClientInfo()
        );
    }
}