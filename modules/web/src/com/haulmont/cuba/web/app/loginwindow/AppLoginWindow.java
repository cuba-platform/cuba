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

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.global.InternalAuthenticationException;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.login.LoginScreen;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.security.LoginScreenAuthDelegate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

/**
 * Legacy base class for a controller of application Login window.
 *
 * @see LoginScreen
 */
public class AppLoginWindow extends AbstractWindow implements Window.TopLevelWindow {

    private static final Logger log = LoggerFactory.getLogger(AppLoginWindow.class);

    protected static final ThreadLocal<LoginScreenAuthDelegate.AuthInfo> authInfoThreadLocal = new ThreadLocal<>();

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected WebAuthConfig webAuthConfig;

    @Inject
    protected App app;

    @Inject
    protected Connection connection;

    @Inject
    protected LoginScreenAuthDelegate authDelegate;

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

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        loginField.focus();

        initPoweredByLink();

        initLogoImage();

        initDefaultCredentials();

        initLocales();

        initRememberMe();

        initRememberMeLocalesBox();
    }

    @Override
    protected void afterShow(AfterShowEvent event) {
        super.afterShow(event);

        doRememberMeLogin();
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

        // if old layout is used
        Component localesSelectLabel = getComponent("localesSelectLabel");
        if (localesSelectLabel != null) {
            localesSelectLabel.setVisible(localeSelectVisible);
        }

        localesSelect.addValueChangeListener(e -> {
            Locale selectedLocale = e.getValue();

            app.setLocale(selectedLocale);

            authInfoThreadLocal.set(new LoginScreenAuthDelegate.AuthInfo(loginField.getValue(), passwordField.getValue(),
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
        Component rememberLocalesBox = getComponent("rememberLocalesBox");
        if (rememberLocalesBox != null) {
            rememberLocalesBox.setVisible(rememberMeCheckBox.isVisible() || localesSelect.isVisible());
        }
    }

    protected void initDefaultCredentials() {
        LoginScreenAuthDelegate.AuthInfo authInfo = authInfoThreadLocal.get();
        if (authInfo != null) {
            loginField.setValue(authInfo.getLogin());
            passwordField.setValue(authInfo.getPassword());
            rememberMeCheckBox.setValue(authInfo.getRememberMe());

            localesSelect.focus();

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
    }

    public void login() {
        doLogin();

        setRememberMeCookies();
    }

    protected void setRememberMeCookies() {
        if (Boolean.TRUE.equals(rememberMeCheckBox.getValue())) {
            authDelegate.setRememberMeCookies(loginField.getValue());
        } else {
            authDelegate.resetRememberCookies();
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
        authDelegate.doLogin(credentials, localesSelect.isVisibleRecursive());
    }

    protected void doRememberMeLogin() {
        authDelegate.doRememberMeLogin(localesSelect.isVisibleRecursive());
    }
}