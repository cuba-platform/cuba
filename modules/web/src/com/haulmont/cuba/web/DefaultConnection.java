/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.haulmont.cuba.web.auth.WebAuthConfig;

import java.util.Locale;
import java.util.Map;

/**
 * Default {@link Connection} implementation for web-client.
 *
 * @author gorodnov
 * @version $Id$
 */
public class DefaultConnection extends AbstractConnection implements ExternallyAuthenticatedConnection {

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    @Override
    public void login(String login, String password, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        update(doLogin(login, password, locale, getLoginParams()));
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param login         login name
     * @param password      encrypted password
     * @param locale        client locale
     * @param loginParams   login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLogin(String login, String password, Locale locale, Map<String, Object> loginParams) throws LoginException {
        return loginService.login(login, password, locale, loginParams);
    }

    @Override
    public void loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        update(doLoginByRememberMe(login, rememberMeToken, locale, getLoginParams()));
    }

    /**
     * Forward login logic to {@link com.haulmont.cuba.security.app.LoginService}.
     * Can be overridden to change login logic.
     *
     * @param login         login name
     * @param password      encrypted password
     * @param locale        client locale
     * @param loginParams   login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLoginByRememberMe(String login, String password, Locale locale, Map<String, Object> loginParams) throws LoginException {
        return loginService.loginByRememberMe(login, password, locale, loginParams);
    }

    @Override
    public void loginAfterExternalAuthentication(String login, Locale locale) throws LoginException {
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null");
        }

        String password = configuration.getConfig(WebAuthConfig.class).getTrustedClientPassword();
        update(doLoginTrusted(login, password, locale, getLoginParams()));

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
     * @param login         login name
     * @param password      encrypted password
     * @param locale        client locale
     * @param loginParams   login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    protected UserSession doLoginTrusted(String login, String password, Locale locale, Map<String, Object> loginParams) throws LoginException {
        return loginService.loginTrusted(login, password, locale, loginParams);
    }

    protected Map<String, Object> getLoginParams() {
        return ParamsMap.of(ClientType.class.getSimpleName(), ClientType.WEB.name());
    }

    @Override
    public String logout() {
        super.logout();
        return configuration.getConfig(WebAuthConfig.class).getExternalAuthentication() ? "login" : "";
    }

    @Override
    public boolean checkRememberMe(String login, String rememberMeToken) {
        return loginService.checkRememberMe(login, rememberMeToken);
    }
}