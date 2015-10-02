/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import com.haulmont.cuba.web.auth.DomainAliasesResolver;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Standard login window.
 * <p/>
 * To use a specific implementation override {@link App#createLoginWindow(AppUI)} method.
 *
 * @author krivopustov
 * @version $Id$
 */
public class LoginWindow extends UIView {

    public static final String COOKIE_LOGIN = "rememberMe.Login";
    public static final String COOKIE_PASSWORD = "rememberMe.Password";
    public static final String COOKIE_REMEMBER_ME = "rememberMe";

    protected static final Logger log = LoggerFactory.getLogger(LoginWindow.class);

    protected final App app;
    protected final AppUI ui;
    protected final Connection connection;

    protected TextField loginField;
    protected PasswordField passwordField;
    protected ComboBox localesSelect;

    protected Locale resolvedLocale;
    protected Map<String, Locale> locales;

    protected GlobalConfig globalConfig;
    protected WebConfig webConfig;

    protected CheckBox rememberMeCheckBox;
    protected boolean rememberMeAllowed = false;
    protected boolean loginByRememberMe = false;

    protected Property.ValueChangeListener loginChangeListener;

    protected Button okButton;

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    public LoginWindow(AppUI ui) {
        log.trace("Creating " + this);
        this.ui = ui;

        globalConfig = configuration.getConfig(GlobalConfig.class);
        webConfig = configuration.getConfig(WebConfig.class);
        locales = globalConfig.getAvailableLocales();

        app = ui.getApp();

        resolvedLocale = resolveLocale(app);

        connection = app.getConnection();

        loginField = new TextField();
        passwordField = new PasswordField();
        localesSelect = new ComboBox();
        localesSelect.setTextInputAllowed(false);

        // make fields immediate to resync fast in case of login is already performed from another UI (i.e. browser tab)
        loginField.setImmediate(true);
        passwordField.setImmediate(true);
        localesSelect.setImmediate(true);

        okButton = new CubaButton();

        rememberMeAllowed = webConfig.getRememberMeEnabled();

        if (rememberMeAllowed) {
            rememberMeCheckBox = new CubaCheckBox();
        }

        setSizeFull();

        // load theme from cookies if it is changed by user in settings dialog
        applyUserTheme();

        initUI();

        if (ui.isTestMode()) {
            loginField.setCubaId("loginField");
            passwordField.setCubaId("passwordField");
            localesSelect.setCubaId("localesField");
            okButton.setCubaId("loginSubmitButton");

            if (rememberMeCheckBox != null) {
                rememberMeCheckBox.setCubaId("rememberMeCheckBox");
            }

            TestIdManager testIdManager = ui.getTestIdManager();

            loginField.setId(testIdManager.reserveId("loginField"));
            passwordField.setId(testIdManager.reserveId("passwordField"));
            localesSelect.setId(testIdManager.reserveId("localesField"));

            okButton.setId(testIdManager.reserveId("loginSubmitButton"));
            if (rememberMeCheckBox != null) {
                rememberMeCheckBox.setId(testIdManager.reserveId("rememberMeCheckBox"));
            }
        }

        addShortcutListener(new ShortcutListener("Default key", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                doLogin();
            }
        });
    }

    protected void applyUserTheme() {
        String uiTheme = ui.getTheme();
        String userAppTheme = app.getCookieValue(App.APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName());

        if (userAppTheme != null) {
            if (!StringUtils.equals(userAppTheme, uiTheme)) {
                // check theme support
                ThemeConstantsRepository themeRepository = AppBeans.get(ThemeConstantsRepository.NAME);
                Set<String> supportedThemes = themeRepository.getAvailableThemes();
                if (supportedThemes.contains(userAppTheme)) {
                    app.applyTheme(userAppTheme);
                    ui.setTheme(userAppTheme);
                }
            }
        }
    }

    protected Locale resolveLocale(App app) {
        for (Locale locale : locales.values()) {
            if (locale.equals(app.getLocale())) {
                return locale;
            }
        }

        // if not found and application locale contains country, try to match by language only
        if (!StringUtils.isEmpty(app.getLocale().getCountry())) {
            Locale appLocale = Locale.forLanguageTag(app.getLocale().getLanguage());
            for (Locale locale : locales.values()) {
                if (Locale.forLanguageTag(locale.getLanguage()).equals(appLocale)) {
                    return locale;
                }
            }
        }
        // return first locale set in the cuba.availableLocales app property
        return locales.values().iterator().next();
    }

    protected void initStandartUI(int formWidth, int formHeight, int fieldWidth, boolean localesSelectVisible) {
        setStyleName("cuba-login-main-layout");

        VerticalLayout centerLayout = createCenterLayout(formWidth, formHeight, fieldWidth, localesSelectVisible);

        addComponent(centerLayout);
        setSizeFull();
        setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);

        initFields();
        loginField.focus();
    }

    protected VerticalLayout createCenterLayout(int formWidth, int formHeight, int fieldWidth, boolean localesSelectVisible) {
        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setStyleName("cuba-login-bottom");
        centerLayout.setWidth(formWidth + "px");
        centerLayout.setHeight(formHeight + "px");

        HorizontalLayout titleLayout = createTitleLayout();
        centerLayout.addComponent(titleLayout);
        centerLayout.setComponentAlignment(titleLayout, Alignment.MIDDLE_CENTER);

        FormLayout loginFormLayout = createLoginFormLayout(fieldWidth, localesSelectVisible);
        centerLayout.addComponent(loginFormLayout);
        centerLayout.setComponentAlignment(loginFormLayout, Alignment.MIDDLE_CENTER);
        return centerLayout;
    }

    protected HorizontalLayout createTitleLayout() {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setStyleName("cuba-login-title");
        titleLayout.setSpacing(true);

        Image logoImage = getLogoImage();
        if (logoImage != null) {
            logoImage.setStyleName("cuba-login-icon");
            titleLayout.addComponent(logoImage);
            titleLayout.setComponentAlignment(logoImage, Alignment.MIDDLE_LEFT);
        }

        String welcomeMsg = messages.getMainMessage("loginWindow.welcomeLabel", resolvedLocale);
        Label label = new Label(welcomeMsg.replace("\n", "<br/>"));
        label.setContentMode(ContentMode.HTML);
        label.setWidthUndefined();
        label.setStyleName("cuba-login-caption");

        if (!StringUtils.isBlank(label.getValue())) {
            titleLayout.addComponent(label);
            titleLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        }
        return titleLayout;
    }

    protected FormLayout createLoginFormLayout(int fieldWidth, boolean localesSelectVisible) {
        FormLayout loginFormLayout = new FormLayout();
        loginFormLayout.setStyleName("cuba-login-form");
        loginFormLayout.setSpacing(true);
        loginFormLayout.setSizeUndefined();


        loginField.setCaption(messages.getMainMessage("loginWindow.loginField", resolvedLocale));
        loginFormLayout.addComponent(loginField);
        loginField.setWidth(fieldWidth + "px");
        loginField.setStyleName("username-field");
        loginFormLayout.setComponentAlignment(loginField, Alignment.MIDDLE_CENTER);

        passwordField.setCaption(messages.getMainMessage("loginWindow.passwordField", resolvedLocale));
        passwordField.setWidth(fieldWidth + "px");
        passwordField.setStyleName("password-field");
        loginFormLayout.addComponent(passwordField);
        loginFormLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);

        if (localesSelectVisible) {
            localesSelect.setCaption(messages.getMainMessage("loginWindow.localesSelect", resolvedLocale));
            localesSelect.setWidth(fieldWidth + "px");
            localesSelect.setNullSelectionAllowed(false);
            loginFormLayout.addComponent(localesSelect);
            loginFormLayout.setComponentAlignment(localesSelect, Alignment.MIDDLE_CENTER);
        }

        if (rememberMeAllowed) {
            rememberMeCheckBox.setCaption(messages.getMainMessage("loginWindow.rememberMe", resolvedLocale));
            rememberMeCheckBox.setStyleName("remember-me");
            loginFormLayout.addComponent(rememberMeCheckBox);
            loginFormLayout.setComponentAlignment(rememberMeCheckBox, Alignment.MIDDLE_CENTER);
        }

        okButton.setCaption(messages.getMainMessage("loginWindow.okButton", resolvedLocale));
        okButton.addClickListener(new SubmitListener());
        okButton.setStyleName("cuba-login-submit");
        okButton.setIcon(WebComponentsHelper.getIcon("app/images/login-button.png"));

        loginFormLayout.addComponent(okButton);
        loginFormLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
        return loginFormLayout;
    }

    @Nullable
    protected Image getLogoImage() {
        final String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", resolvedLocale);
        if (StringUtils.isBlank(loginLogoImagePath) || "loginWindow.logoImage".equals(loginLogoImagePath))
            return null;

        return new Image(null, new VersionedThemeResource(loginLogoImagePath));
    }

    protected void initUI() {
        boolean localeSelectVisible = globalConfig.getLocaleSelectVisible();

        ThemeConstants theme = app.getThemeConstants();
        int formWidth = theme.getInt("cuba.web.LoginWindow.form.width");
        int formHeight = theme.getInt("cuba.web.LoginWindow.form.height");
        int fieldWidth = theme.getInt("cuba.web.LoginWindow.field.width");

        initStandartUI(formWidth, formHeight, fieldWidth, localeSelectVisible);
    }

    protected void initRememberMe() {
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
            if (connection.checkRememberMe(login, rememberMeToken)) {
                rememberMeCheckBox.setValue(true);
                loginField.setValue(login);

                passwordField.setValue(rememberMeToken);
                loginByRememberMe = true;
            }

            loginChangeListener = new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    loginByRememberMe = false;
                }
            };

            loginField.addValueChangeListener(loginChangeListener);
            passwordField.addValueChangeListener(loginChangeListener);
        } else {
            rememberMeCheckBox.setValue(false);
            loginChangeListener = null;
        }
    }

    protected void initFields() {
        String currLocale = messages.getTools().localeToString(resolvedLocale);
        String selected = null;
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            localesSelect.addItem(entry.getKey());
            if (messages.getTools().localeToString(entry.getValue()).equals(currLocale)) {
                selected = entry.getKey();
            }
        }
        if (selected == null) {
            selected = locales.keySet().iterator().next();
        }
        localesSelect.setValue(selected);

        if (configuration.getConfig(WebAuthConfig.class).getExternalAuthentication()) {
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

        if (rememberMeAllowed) {
            initRememberMe();
        }
    }

    @Override
    public String getTitle() {
        return messages.getMainMessage("loginWindow.caption", resolvedLocale);
    }

    public class SubmitListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            doLogin();
        }
    }

    protected void login() {
        String login = loginField.getValue();
        String password = passwordField.getValue() != null ? passwordField.getValue() : "";

        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            String message = messages.getMainMessage("loginWindow.emptyLoginOrPassword", resolvedLocale);
            new Notification(message, Type.WARNING_MESSAGE).show(ui.getPage());
            return;
        }

        try {
            Locale locale = getUserLocale();
            app.setLocale(locale);

            PasswordEncryption passwordEncryption = AppBeans.get(PasswordEncryption.NAME);

            if (loginByRememberMe && rememberMeAllowed) {
                loginByRememberMe(login, password, locale);
            } else if (configuration.getConfig(WebAuthConfig.class).getExternalAuthentication()) {
                // try to login as externally authenticated user, fallback to regular authentication
                // we use resolved locale for error messages
                if (authenticateExternally(login, password, resolvedLocale)) {
                    login = convertLoginString(login);

                    ((ExternallyAuthenticatedConnection) connection).loginAfterExternalAuthentication(login, locale);
                } else {
                    login(login, passwordEncryption.getPlainHash(password), locale);
                }
            } else {
                login(login, passwordEncryption.getPlainHash(password), locale);
            }
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());

            String title = messages.getMainMessage("loginWindow.loginFailed", resolvedLocale);
            String message = StringUtils.abbreviate(e.getMessage(), 1000);
            new Notification(title, message, Type.ERROR_MESSAGE, true).show(ui.getPage());

            if (loginByRememberMe) {
                loginByRememberMe = false;
                loginField.removeValueChangeListener(loginChangeListener);
                passwordField.removeValueChangeListener(loginChangeListener);
                loginChangeListener = null;
            }
        } catch (Exception e) {
            log.warn("Unable to login", e);

            String title = messages.getMainMessage("loginWindow.loginFailed", resolvedLocale);
            String message = messages.getMainMessage("loginWindow.pleaseContactAdministrator", resolvedLocale);
            new Notification(title, message, Type.ERROR_MESSAGE, true).show(ui.getPage());
        }
    }

    protected boolean authenticateExternally(String login, String passwordValue, Locale locale) {
        CubaAuthProvider authProvider = AppBeans.get(CubaAuthProvider.NAME);
        try {
            authProvider.authenticate(login, passwordValue, locale);
        } catch (Exception e) {
            log.debug("External authentication failed", e);
            return false;
        }
        return true;
    }

    /**
     * Convert userName to db form
     * In database users stores in form DOMAIN&#92;userName
     *
     * @param login Login string
     * @return login in form DOMAIN&#92;userName
     */
    private String convertLoginString(String login) {
        DomainAliasesResolver aliasesResolver = AppBeans.get(DomainAliasesResolver.NAME);
        int slashPos = login.indexOf("\\");
        if (slashPos >= 0) {
            String domainAlias = login.substring(0, slashPos);
            String domain = aliasesResolver.getDomainName(domainAlias).toUpperCase();
            String userName = login.substring(slashPos + 1);
            login = domain + "\\" + userName;
        } else {
            int atSignPos = login.indexOf("@");
            if (atSignPos >= 0) {
                String domainAlias = login.substring(atSignPos + 1);
                String domain = aliasesResolver.getDomainName(domainAlias).toUpperCase();
                String userName = login.substring(0, atSignPos);
                login = domain + "\\" + userName;
            }
        }
        return login;
    }

    protected void login(String login, String password, Locale locale) throws LoginException {
        connection.login(login, password, locale);
    }

    protected void loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        connection.loginByRememberMe(login, rememberMeToken, locale);
    }

    protected void doLogin() {
        login();

        if (connection.isConnected()) {
            if (rememberMeAllowed) {
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

                        UserManagementService userManagementService = AppBeans.get(UserManagementService.NAME);
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

    protected Locale getUserLocale() {
        String lang = (String) localesSelect.getValue();
        return locales.get(lang);
    }

    protected String getMessagesPack() {
        return AppConfig.getMessagesPack();
    }
}