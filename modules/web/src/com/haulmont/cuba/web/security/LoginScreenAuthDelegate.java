/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.security;

import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.auth.RememberMeCredentials;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.InternalAuthenticationException;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.login.LoginScreen;
import com.haulmont.cuba.web.app.loginwindow.AppLoginWindow;
import com.haulmont.cuba.web.sys.VaadinSessionScope;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

import static com.haulmont.cuba.web.App.*;
import static com.haulmont.cuba.web.App.COOKIE_PASSWORD;

/**
 * Is intended to use from {@link LoginScreen}, {@link AppLoginWindow} and provides performing log in.
 */
@Component(LoginScreenAuthDelegate.NAME)
@Scope(VaadinSessionScope.NAME)
public class LoginScreenAuthDelegate {
    public static final String NAME = "cuba_LoginScreenAuthDelegate";

    private static final Logger log = LoggerFactory.getLogger(LoginScreenAuthDelegate.class);

    protected App app;
    protected Connection connection;

    protected GlobalConfig globalConfig;
    protected WebConfig webConfig;

    protected UserManagementService userManagementService;
    protected Messages messages;

    @Inject
    protected void setApp(App app) {
        this.app = app;
    }

    @Inject
    protected void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Inject
    protected void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    @Inject
    protected void setWebConfig(WebConfig webConfig) {
        this.webConfig = webConfig;
    }

    @Inject
    protected void setUserManagementService(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    /**
     * Performs log in.
     *
     * @param login                  user login
     * @param password               user password
     * @param selectedLocale         selected locale
     * @param isLocalesSelectVisible is locales select visible
     * @throws InternalAuthenticationException if authentication mechanisms cannot process an authentication request
     * @throws LoginException                  if authentication fails
     */
    public void doLogin(String login, String password, Locale selectedLocale, boolean isLocalesSelectVisible)
            throws InternalAuthenticationException, LoginException {
        password = password != null ? password : "";

        app.setLocale(selectedLocale);

        doLogin(new LoginPasswordCredentials(login, password, selectedLocale), isLocalesSelectVisible);

        // locale could be set on the server
        if (connection.getSession() != null) {
            Locale loggedInLocale = connection.getSession().getLocale();

            if (globalConfig.getLocaleSelectVisible()) {
                app.addCookie(App.COOKIE_LOCALE, loggedInLocale.toLanguageTag());
            }
        }
    }

    /**
     * Performs log in with credentials.
     *
     * @param credentials            user credentials
     * @param isLocalesSelectVisible is locales select visible
     * @throws LoginException if authentication fails
     */
    public void doLogin(Credentials credentials, boolean isLocalesSelectVisible) throws LoginException {
        if (credentials instanceof AbstractClientCredentials) {
            ((AbstractClientCredentials) credentials).setOverrideLocale(isLocalesSelectVisible);
        }
        connection.login(credentials);
    }

    /**
     * Do login with "remember me" cookies.
     *
     * @param isLocalesSelectVisible is locales field visible
     */
    public void doRememberMeLogin(boolean isLocalesSelectVisible) {
        if (!webConfig.getRememberMeEnabled()) {
            return;
        }

        String rememberMeCookie = app.getCookieValue(COOKIE_REMEMBER_ME);
        if (!Boolean.parseBoolean(rememberMeCookie)) {
            return;
        }

        String encodedLogin = app.getCookieValue(COOKIE_LOGIN) != null
                ? app.getCookieValue(COOKIE_LOGIN) : "";
        String login = URLEncodeUtils.decodeUtf8(encodedLogin);

        String rememberMeToken = app.getCookieValue(COOKIE_PASSWORD) != null
                ? app.getCookieValue(COOKIE_PASSWORD) : "";

        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(rememberMeToken)) {
            return;
        }

        boolean tokenValid = userManagementService.isRememberMeTokenValid(login, rememberMeToken);
        if (!tokenValid) {
            resetRememberCookies();
            return;
        }

        Locale locale = messages.getTools().getDefaultLocale();

        String lastLocale = app.getCookieValue(COOKIE_LOCALE);
        if (lastLocale != null
                && !lastLocale.isEmpty()) {
            Map<String, Locale> availableLocales = globalConfig.getAvailableLocales();
            for (Locale availableLocale : availableLocales.values()) {
                if (availableLocale.toLanguageTag().equals(lastLocale)) {
                    locale = availableLocale;
                }
            }
        }

        if (StringUtils.isNotEmpty(rememberMeToken)) {
            RememberMeCredentials credentials = new RememberMeCredentials(login, rememberMeToken, locale);
            credentials.setOverrideLocale(isLocalesSelectVisible);
            try {
                connection.login(credentials);
            } catch (LoginException e) {
                log.info("Failed to login with remember me token. Reset corresponding cookies.");
                resetRememberCookies();
            }
        }
    }

    /**
     * Sets "remember me" cookies.
     *
     * @param login login to save.
     */
    public void setRememberMeCookies(String login) {
        if (connection.isAuthenticated() && webConfig.getRememberMeEnabled()) {
            int rememberMeExpiration = globalConfig.getRememberMeExpirationTimeoutSec();

            app.addCookie(COOKIE_REMEMBER_ME, Boolean.TRUE.toString(), rememberMeExpiration);

            String encodedLogin = URLEncodeUtils.encodeUtf8(login);
            app.addCookie(COOKIE_LOGIN, StringEscapeUtils.escapeJava(encodedLogin), rememberMeExpiration);

            UserSession session = connection.getSession();
            if (session == null) {
                throw new IllegalStateException("Unable to get session after login");
            }
            User user = session.getUser();
            String rememberMeToken = userManagementService.generateRememberMeToken(user.getId());
            app.addCookie(COOKIE_PASSWORD, rememberMeToken, rememberMeExpiration);
        } else {
            resetRememberCookies();
        }
    }

    /**
     * Clears cookies.
     */
    public void resetRememberCookies() {
        app.removeCookie(COOKIE_REMEMBER_ME);
        app.removeCookie(COOKIE_LOGIN);
        app.removeCookie(COOKIE_PASSWORD);
    }

    /**
     * Contains user's auth information. Is used in login screens for saving state and initializing auth value.
     */
    public static class AuthInfo {

        protected final String login;
        protected final String password;
        protected final Boolean rememberMe;

        public AuthInfo(String login, String password, Boolean rememberMe) {
            this.login = login;
            this.password = password;
            this.rememberMe = rememberMe;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public Boolean getRememberMe() {
            return rememberMe;
        }
    }
}
