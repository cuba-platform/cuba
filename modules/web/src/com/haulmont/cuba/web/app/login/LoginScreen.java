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

package com.haulmont.cuba.web.app.login;

import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

import static com.haulmont.cuba.web.App.*;

/**
 * Base class for Login screen.
 */
@Route(path = "login", root = true)
@UiDescriptor("login-screen.xml")
@UiController("login")
public class LoginScreen extends Screen {

    private static final Logger log = LoggerFactory.getLogger(LoginScreen.class);

    @Inject
    protected GlobalConfig globalConfig;
    @Inject
    protected WebConfig webConfig;
    @Inject
    protected WebAuthConfig webAuthConfig;

    @Inject
    protected UserManagementService userManagementService;
    @Inject
    protected Messages messages;
    @Inject
    protected Notifications notifications;
    @Inject
    protected Screens screens;

    @Inject
    protected App app;
    @Inject
    protected Connection connection;

    @Inject
    protected Image logoImage;
    @Inject
    protected TextField<String> loginField;
    @Inject
    protected CheckBox rememberMeCheckBox;
    @Inject
    protected PasswordField passwordField;
    @Inject
    protected LookupField<Locale> localesSelect;

    @Subscribe
    protected void onInit(InitEvent event) {
        loginField.focus();

        initPoweredByLink();

        initLogoImage();

        initDefaultCredentials();

        initLocales();

        initRememberMe();

        initRememberMeLocalesBox();
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        doRememberMeLogin();
    }

    protected void initPoweredByLink() {
        Component poweredByLink = getWindow().getComponent("poweredByLink");
        if (poweredByLink != null) {
            poweredByLink.setVisible(webConfig.getLoginDialogPoweredByLinkVisible());
        }
    }

    protected void initLocales() {
        localesSelect.setOptionsMap(globalConfig.getAvailableLocales());
        localesSelect.setValue(app.getLocale());

        boolean localeSelectVisible = globalConfig.getLocaleSelectVisible();
        localesSelect.setVisible(localeSelectVisible);

        // if old layout is used
        Component localesSelectLabel = getWindow().getComponent("localesSelectLabel");
        if (localesSelectLabel != null) {
            localesSelectLabel.setVisible(localeSelectVisible);
        }

        localesSelect.addValueChangeListener(e -> {
            app.setLocale(e.getValue());

            AuthInfo authInfo = new AuthInfo(loginField.getValue(),
                    passwordField.getValue(),
                    rememberMeCheckBox.getValue());

            String screenId = UiControllerUtils.getScreenContext(this)
                    .getWindowInfo()
                    .getId();

            Screen loginScreen = screens.create(screenId, OpenMode.ROOT);

            if (loginScreen instanceof LoginScreen) {
                ((LoginScreen) loginScreen).setAuthInfo(authInfo);
            }

            loginScreen.show();
        });
    }

    protected void initLogoImage() {
        String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", app.getLocale());
        if (StringUtils.isBlank(loginLogoImagePath) || "loginWindow.logoImage".equals(loginLogoImagePath)) {
            logoImage.setVisible(false);
        } else {
            logoImage.setSource(ThemeResource.class).setPath(loginLogoImagePath);
        }
    }

    protected void initRememberMe() {
        if (!webConfig.getRememberMeEnabled()) {
            rememberMeCheckBox.setValue(false);
            rememberMeCheckBox.setVisible(false);
        }
    }

    protected void initRememberMeLocalesBox() {
        Component rememberLocalesBox = getWindow().getComponent("rememberLocalesBox");
        if (rememberLocalesBox != null) {
            rememberLocalesBox.setVisible(rememberMeCheckBox.isVisible() || localesSelect.isVisible());
        }
    }

    protected void initDefaultCredentials() {
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

        notifications.create(Notifications.NotificationType.ERROR)
                .withCaption(title)
                .withDescription(message)
                .show();
    }

    protected void showLoginException(String message) {
        String title = messages.getMainMessage("loginWindow.loginFailed", app.getLocale());

        notifications.create(Notifications.NotificationType.ERROR)
                .withCaption(title)
                .withDescription(message)
                .show();
    }

    public void login() {
        doLogin();

        setRememberMeCookies();
    }

    protected void setRememberMeCookies() {
        if (connection.isAuthenticated() && webConfig.getRememberMeEnabled()) {
            if (Boolean.TRUE.equals(rememberMeCheckBox.getValue())) {
                int rememberMeExpiration = globalConfig.getRememberMeExpirationTimeoutSec();

                app.addCookie(COOKIE_REMEMBER_ME, Boolean.TRUE.toString(), rememberMeExpiration);

                String encodedLogin = URLEncodeUtils.encodeUtf8(loginField.getValue());
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
    }

    protected void resetRememberCookies() {
        app.removeCookie(COOKIE_REMEMBER_ME);
        app.removeCookie(COOKIE_LOGIN);
        app.removeCookie(COOKIE_PASSWORD);
    }

    protected void doLogin() {
        String login = loginField.getValue();
        String password = passwordField.getValue() != null ? passwordField.getValue() : "";

        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption(messages.getMainMessage("loginWindow.emptyLoginOrPassword"))
                    .show();
            return;
        }

        try {
            Locale selectedLocale = localesSelect.getValue();
            app.setLocale(selectedLocale);

            doLogin(new LoginPasswordCredentials(login, password, selectedLocale));

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

    protected void doRememberMeLogin() {
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
            credentials.setOverrideLocale(localesSelect.isVisibleRecursive());
            try {
                connection.login(credentials);
            } catch (LoginException e) {
                log.info("Failed to login with remember me token. Reset corresponding cookies.");
                resetRememberCookies();
            }
        }
    }

    protected void setAuthInfo(AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
        rememberMeCheckBox.setValue(authInfo.getRememberMe());

        localesSelect.focus();
    }

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
