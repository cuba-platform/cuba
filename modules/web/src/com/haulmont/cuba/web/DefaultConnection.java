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
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.LoginFailedException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Default {@link Connection} implementation for web-client.
 */
@Component(Connection.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefaultConnection extends AbstractConnection implements ExternallyAuthenticatedConnection {

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected WebAuthConfig webAuthConfig;

    @Override
    public void login(String login, String password, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        update(doLogin(login, password, locale, getLoginParams()), SessionMode.AUTHENTICATED);
    }

    @Override
    public void loginAnonymous(Locale locale) throws LoginException {
        UUID anonymousSessionId = globalConfig.getAnonymousSessionId();

        UserSession session = doLoginAnonymous(anonymousSessionId, locale);
        if (session == null) {
            throw new LoginFailedException("Unable to obtain anonymous session");
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
        return loginService.login(login, password, locale, loginParams);
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param locale client locale
     * @return obtained user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLoginAnonymous(UUID anonymousSessionId, Locale locale) throws LoginException {
        return loginService.getSession(anonymousSessionId);
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
     * @param login       login name
     * @param password    encrypted password
     * @param locale      client locale
     * @param loginParams login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLoginByRememberMe(String login, String password, Locale locale, Map<String, Object> loginParams)
            throws LoginException {
        return loginService.loginByRememberMe(login, password, locale, loginParams);
    }

    @Override
    public void loginAfterExternalAuthentication(String login, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        String password = webAuthConfig.getTrustedClientPassword();
        update(doLoginTrusted(login, password, locale, getLoginParams()), SessionMode.AUTHENTICATED);

        UserSession session = getSession();
        if (session == null) {
            throw new IllegalStateException("Null session after login");
        }
        session.setAttribute(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE, true);
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
        return loginService.loginTrusted(login, password, locale, loginParams);
    }

    protected Map<String, Object> getLoginParams() {
        return ParamsMap.of(ClientType.class.getName(), ClientType.WEB.name());
    }

    @Override
    public boolean checkRememberMe(String login, String rememberMeToken) {
        return loginService.checkRememberMe(login, rememberMeToken);
    }
}