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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import com.haulmont.cuba.web.auth.DomainAliasesResolver;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    protected LoginService loginService;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected DomainAliasesResolver domainAliasesResolver;

    @Inject
    protected CubaAuthProvider cubaAuthProvider;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected Embedded logoImage;

    @Inject
    protected TextField loginField;

    @Inject
    protected CheckBox rememberMeCheckBox;

    @Inject
    protected Label rememberMeSpacer;

    @Inject
    protected PasswordField passwordField;

    @Inject
    protected Label localesSelectLabel;

    @Inject
    protected LookupField localesSelect;

    protected boolean loginByRememberMe = false;

    protected ValueChangeListener loginChangeListener;

    protected Boolean bruteForceProtectionEnabled;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        loginField.requestFocus();

        initLogoImage();

        initDefaultCredentials();

        initLocales();

        initRememberMe();
    }

    protected void initLocales() {
        Map<String, Locale> locales = globalConfig.getAvailableLocales();

        localesSelect.setOptionsMap(locales);
        localesSelect.setValue(App.getInstance().getLocale());

        boolean localeSelectVisible = globalConfig.getLocaleSelectVisible();
        localesSelect.setVisible(localeSelectVisible);
        localesSelectLabel.setVisible(localeSelectVisible);

        localesSelect.addValueChangeListener(e -> {
            Locale selectedLocale = (Locale) e.getValue();

            App app = App.getInstance();
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
        String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", userSessionSource.getLocale());
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

        App app = App.getInstance();

        String rememberMeCookie = app.getCookieValue(COOKIE_REMEMBER_ME);
        if (Boolean.parseBoolean(rememberMeCookie)) {
            String login;
            String encodedLogin = app.getCookieValue(COOKIE_LOGIN) != null ? app.getCookieValue(COOKIE_LOGIN) : "";
            try {
                login = URLDecoder.decode(encodedLogin, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                login = encodedLogin;
            }

            String rememberMeToken = app.getCookieValue(COOKIE_PASSWORD) != null ? app.getCookieValue(COOKIE_PASSWORD) : "";
            if (app.getConnection().checkRememberMe(login, rememberMeToken)) {
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

        App app = App.getInstance();

        if (webAuthConfig.getExternalAuthentication()) {
            loginField.setValue(app.getPrincipal() == null ? "" : app.getPrincipal().getName());
            passwordField.setValue("");
        } else {
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
    }

    /**
     * Convert userName to db form
     * In database users stores in form DOMAIN&#92;userName
     *
     * @param login Login string
     * @return login in form DOMAIN&#92;userName
     */
    protected String convertLoginString(String login) {
        int slashPos = login.indexOf("\\");
        if (slashPos >= 0) {
            String domainAlias = login.substring(0, slashPos);
            String domain = domainAliasesResolver.getDomainName(domainAlias).toUpperCase();
            String userName = login.substring(slashPos + 1);
            login = domain + "\\" + userName;
        } else {
            int atSignPos = login.indexOf("@");
            if (atSignPos >= 0) {
                String domainAlias = login.substring(atSignPos + 1);
                String domain = domainAliasesResolver.getDomainName(domainAlias).toUpperCase();
                String userName = login.substring(0, atSignPos);
                login = domain + "\\" + userName;
            }
        }
        return login;
    }

    protected void showUnhandledExceptionOnLogin(Exception e) {
        String title = messages.getMainMessage("loginWindow.loginFailed", userSessionSource.getLocale());
        String message = messages.getMainMessage("loginWindow.pleaseContactAdministrator", userSessionSource.getLocale());

        showNotification(title, message, NotificationType.ERROR);
    }

    protected void showLoginException(String message){
        String title = messages.getMainMessage("loginWindow.loginFailed", userSessionSource.getLocale());

        showNotification(title, message, NotificationType.ERROR);

        if (loginByRememberMe) {
            loginByRememberMe = false;

            loginField.removeValueChangeListener(loginChangeListener);
            passwordField.removeValueChangeListener(loginChangeListener);
        }
    }

    public void login() {
        doLogin();

        App app = App.getInstance();
        Connection connection = app.getConnection();

        if (connection.isConnected()) {
            if (webConfig.getRememberMeEnabled()) {
                if (Boolean.TRUE.equals(rememberMeCheckBox.getValue())) {
                    if (!loginByRememberMe) {
                        app.addCookie(COOKIE_REMEMBER_ME, Boolean.TRUE.toString());

                        String login = loginField.getValue();

                        String encodedLogin;
                        try {
                            encodedLogin = URLEncoder.encode(login, StandardCharsets.UTF_8.name());
                        } catch (UnsupportedEncodingException e) {
                            encodedLogin = login;
                        }

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
        }

        App app = App.getInstance();
        if (!bruteForceProtectionCheck(login, app.getClientAddress())) {
            return;
        }

        try {
            Connection connection = app.getConnection();

            Locale selectedLocale = localesSelect.getValue();
            app.setLocale(selectedLocale);

            if (loginByRememberMe && webConfig.getRememberMeEnabled()) {
                doLoginByRememberMe(login, password, selectedLocale);
            } else if (webAuthConfig.getExternalAuthentication()) {
                // try to login as externally authenticated user, fallback to regular authentication
                // we use resolved locale for error messages
                if (authenticateExternally(login, password, selectedLocale)) {
                    login = convertLoginString(login);

                    ((ExternallyAuthenticatedConnection) connection).loginAfterExternalAuthentication(login, selectedLocale);
                } else {
                    doLogin(login, passwordEncryption.getPlainHash(password), selectedLocale);
                }
            } else {
                doLogin(login, passwordEncryption.getPlainHash(password), selectedLocale);
            }
            // locale could be set on the server
            if (connection.getSession() != null) {
                Locale loggedInLocale = userSessionSource.getLocale();

                if (globalConfig.getLocaleSelectVisible()) {
                    app.addCookie(App.COOKIE_LOCALE, loggedInLocale.toLanguageTag());
                }
            }
        } catch (LoginException e) {
            log.info("Login failed: {}", e.toString());

            String message = StringUtils.abbreviate(e.getMessage(), 1000);
            String bruteForceMsg = registerUnsuccessfulLoginAttempt(login, app.getClientAddress());
            if (!Strings.isNullOrEmpty(bruteForceMsg))  {
                message = bruteForceMsg;
            }
            showLoginException(message);
        } catch (Exception e) {
            log.warn("Unable to login", e);

            showUnhandledExceptionOnLogin(e);
        }
    }

    protected void doLogin(String login, String password, Locale locale) throws LoginException {
        App app = App.getInstance();

        app.getConnection().login(login, password, locale);
    }

    protected void doLoginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        App app = App.getInstance();

        app.getConnection().loginByRememberMe(login, rememberMeToken, locale);
    }

    protected boolean authenticateExternally(String login, String passwordValue, Locale locale) {
        try {
            cubaAuthProvider.authenticate(login, passwordValue, locale);
        } catch (Exception e) {
            log.debug("External authentication failed", e);
            return false;
        }
        return true;
    }

    protected boolean isBruteForceProtectionEnabled() {
        if (bruteForceProtectionEnabled == null) {
            bruteForceProtectionEnabled = loginService.isBruteForceProtectionEnabled();
        }
        return bruteForceProtectionEnabled;
    }

    protected boolean bruteForceProtectionCheck(String login, String ipAddress) {
        if (isBruteForceProtectionEnabled()) {
            if (loginService.loginAttemptsLeft(login, ipAddress) <= 0) {
                String title = messages.getMainMessage("loginWindow.loginFailed");
                String message = messages.formatMainMessage(
                        "loginWindow.loginAttemptsNumberExceeded",
                        loginService.getBruteForceBlockIntervalSec());

                showNotification(title, message, NotificationType.ERROR_HTML);

                log.info("Blocked user login attempt: login={}, ip={}", login, ipAddress);

                return false;
            }
        }
        return true;
    }

    @Nullable
    protected String registerUnsuccessfulLoginAttempt(String login, String ipAddress) {
        String message = null;
        if (isBruteForceProtectionEnabled()) {
            int loginAttemptsLeft = loginService.registerUnsuccessfulLogin(login, ipAddress);
            if (loginAttemptsLeft > 0) {
                message = messages.formatMainMessage(
                        "loginWindow.loginFailedAttemptsLeft",
                        loginAttemptsLeft);
            } else {
                message = messages.formatMainMessage(
                        "loginWindow.loginAttemptsNumberExceeded",
                        loginService.getBruteForceBlockIntervalSec());
            }
        }
        return message;
    }
}