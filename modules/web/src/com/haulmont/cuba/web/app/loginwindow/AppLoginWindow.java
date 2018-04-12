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
 */

package com.haulmont.cuba.web.app.loginwindow;

import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.components.*;
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
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

public class AppLoginWindow extends AbstractWindow implements Window.TopLevelWindow {

    private static final Logger log = LoggerFactory.getLogger(AppLoginWindow.class);

    protected static final ThreadLocal<AuthInfo> authInfoThreadLocal = new ThreadLocal<>();

    public static final String COOKIE_REMEMBER_ME = "rememberMe";
    public static final String COOKIE_LOGIN = "rememberMe.Login";
    public static final String COOKIE_PASSWORD = "rememberMe.Password";

    public static class AuthInfo {
        private String login;
        private String password;
        private Boolean rememberMe;

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

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected WebAuthConfig webAuthConfig;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected App app;

    @Inject
    protected Connection connection;

    @Inject
    protected Embedded logoImage;

    @Inject
    protected TextField<String> loginField;

    @Inject
    protected CheckBox rememberMeCheckBox;

    @Inject
    protected Label rememberMeSpacer;

    @Inject
    protected PasswordField passwordField;

    @Inject
    protected Label localesSelectLabel;

    @Inject
    protected LookupField<Locale> localesSelect;

    protected boolean loginByRememberMe = false;

    protected ValueChangeListener loginChangeListener;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        loginField.requestFocus();

        initPoweredByLink();

        initLogoImage();

        initDefaultCredentials();

        initLocales();

        initRememberMe();
    }

    protected void initPoweredByLink() {
        Component poweredByLink = getComponent("poweredByLink");
        if (poweredByLink != null) {
            poweredByLink.setVisible(webConfig.getLoginDialogPoweredByLinkVisible());
        }
    }

    protected void initLocales() {
        localesSelect.setOptionsMap(globalConfig.getAvailableLocales());
        localesSelect.setValue(app.getLocale());

        boolean localeSelectVisible = globalConfig.getLocaleSelectVisible();
        localesSelect.setVisible(localeSelectVisible);
        localesSelectLabel.setVisible(localeSelectVisible);

        localesSelect.addValueChangeListener(e -> {
            Locale selectedLocale = (Locale) e.getValue();

            app.setLocale(selectedLocale);

            authInfoThreadLocal.set(new AuthInfo(loginField.getValue(), passwordField.getValue(),
                    rememberMeCheckBox.getValue()));
            try {
                app.createTopLevelWindow();
            } finally {
                authInfoThreadLocal.set(null);
            }
        });
    }

    protected void initLogoImage() {
        String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", app.getLocale());
        if (StringUtils.isBlank(loginLogoImagePath) || "loginWindow.logoImage".equals(loginLogoImagePath)) {
            logoImage.setVisible(false);
        } else {
            logoImage.setSource("theme://" + loginLogoImagePath);
        }
    }

    protected void initRememberMe() {
        loginChangeListener = e -> loginByRememberMe = false;

        if (!webConfig.getRememberMeEnabled()) {
            rememberMeCheckBox.setValue(false);

            rememberMeSpacer.setVisible(false);
            rememberMeCheckBox.setVisible(false);
            return;
        }

        String rememberMeCookie = app.getCookieValue(COOKIE_REMEMBER_ME);
        if (Boolean.parseBoolean(rememberMeCookie)) {
            String encodedLogin = app.getCookieValue(COOKIE_LOGIN) != null ? app.getCookieValue(COOKIE_LOGIN) : "";
            String login = URLEncodeUtils.decodeUtf8(encodedLogin);

            String rememberMeToken = app.getCookieValue(COOKIE_PASSWORD) != null ? app.getCookieValue(COOKIE_PASSWORD) : "";
            if (StringUtils.isNotEmpty(rememberMeToken)) {
                rememberMeCheckBox.setValue(true);
                loginField.setValue(login);

                passwordField.setValue(rememberMeToken);
                loginByRememberMe = true;
            }

            loginField.addValueChangeListener(loginChangeListener);
            passwordField.addValueChangeListener(loginChangeListener);
        }
    }

    protected void initDefaultCredentials() {
        AuthInfo authInfo = authInfoThreadLocal.get();
        if (authInfo != null) {
            loginField.setValue(authInfo.getLogin());
            passwordField.setValue(authInfo.getPassword());
            rememberMeCheckBox.setValue(authInfo.getRememberMe());

            localesSelect.requestFocus();

            authInfoThreadLocal.set(null);

            return;
        }

        String defaultUser = webConfig.getLoginDialogDefaultUser();
        if (!StringUtils.isBlank(defaultUser) && !"<disabled>".equals(defaultUser)) {
            loginField.setValue(defaultUser);
        } else {
            loginField.setValue("");
        }

        String defaultPassw = webConfig.getLoginDialogDefaultPassword();
        if (!StringUtils.isBlank(defaultPassw) && !"<disabled>".equals(defaultPassw)) {
            passwordField.setValue(defaultPassw);
        } else {
            passwordField.setValue("");
        }
    }

    protected void showUnhandledExceptionOnLogin(@SuppressWarnings("unused") Exception e) {
        String title = messages.getMainMessage("loginWindow.loginFailed", app.getLocale());
        String message = messages.getMainMessage("loginWindow.pleaseContactAdministrator", app.getLocale());

        showNotification(title, message, NotificationType.ERROR);
    }

    protected void showLoginException(String message) {
        String title = messages.getMainMessage("loginWindow.loginFailed", app.getLocale());

        showNotification(title, message, NotificationType.ERROR);

        if (loginByRememberMe) {
            loginByRememberMe = false;

            loginField.removeValueChangeListener(loginChangeListener);
            passwordField.removeValueChangeListener(loginChangeListener);
        }
    }

    public void login() {
        doLogin();

        setRememberMeCookies();
    }

    protected void setRememberMeCookies() {
        if (connection.isAuthenticated()) {
            if (webConfig.getRememberMeEnabled()) {
                if (Boolean.TRUE.equals(rememberMeCheckBox.getValue())) {
                    if (!loginByRememberMe) {
                        app.addCookie(COOKIE_REMEMBER_ME, Boolean.TRUE.toString());

                        String login = loginField.getValue();

                        String encodedLogin = URLEncodeUtils.encodeUtf8(login);

                        app.addCookie(COOKIE_LOGIN, StringEscapeUtils.escapeJava(encodedLogin));

                        UserSession session = connection.getSession();
                        if (session == null) {
                            throw new IllegalStateException("Unable to get session after login");
                        }

                        User user = session.getUser();

                        String rememberMeToken = userManagementService.generateRememberMeToken(user.getId());

                        app.addCookie(COOKIE_PASSWORD, rememberMeToken);
                    }
                } else {
                    app.removeCookie(COOKIE_REMEMBER_ME);
                    app.removeCookie(COOKIE_LOGIN);
                    app.removeCookie(COOKIE_PASSWORD);
                }
            }
        }
    }

    protected void doLogin() {
        String login = loginField.getValue();
        String password = passwordField.getValue() != null ? passwordField.getValue() : "";

        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            showNotification(messages.getMainMessage("loginWindow.emptyLoginOrPassword"), NotificationType.WARNING);
            return;
        }

        try {
            Locale selectedLocale = localesSelect.getValue();
            app.setLocale(selectedLocale);

            if (loginByRememberMe && webConfig.getRememberMeEnabled()) {
                doLogin(new RememberMeCredentials(login, password, selectedLocale));
            } else {
                doLogin(new LoginPasswordCredentials(login, password, selectedLocale));
            }

            // locale could be set on the server
            if (connection.getSession() != null) {
                Locale loggedInLocale = connection.getSession().getLocale();

                if (globalConfig.getLocaleSelectVisible()) {
                    app.addCookie(App.COOKIE_LOCALE, loggedInLocale.toLanguageTag());
                }
            }
        } catch (InternalAuthenticationException e) {
            log.error("Internal error during login", e);

            showUnhandledExceptionOnLogin(e);
        } catch (LoginException e) {
            log.info("Login failed: {}", e.toString());

            String message = StringUtils.abbreviate(e.getMessage(), 1000);
            showLoginException(message);
        } catch (Exception e) {
            log.warn("Unable to login", e);

            showUnhandledExceptionOnLogin(e);
        }
    }

    protected void doLogin(Credentials credentials) throws LoginException {
        if (credentials instanceof AbstractClientCredentials) {
            ((AbstractClientCredentials) credentials).setOverrideLocale(localesSelect.isVisibleRecursive());
        }
        connection.login(credentials);
    }
}