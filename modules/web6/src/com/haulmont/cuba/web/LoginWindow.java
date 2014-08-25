/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.ActiveDirectoryConnection;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import com.haulmont.cuba.web.auth.DomainAliasesResolver;
import com.haulmont.cuba.web.sys.CubaApplicationContext;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;

/**
 * Standard login window.
 * <p/>
 * Specific application should inherit from this class and create appropriate
 * instance in {@link DefaultApp#createLoginWindow()} method
 *
 * @author krivopustov
 * @version $Id$
 */
public class LoginWindow extends Window implements Action.Handler {

    protected Log log = LogFactory.getLog(getClass());

    public static final String COOKIE_LOGIN = "rememberMe.Login";
    public static final String COOKIE_PASSWORD = "rememberMe.Password";
    public static final String COOKIE_REMEMBER_ME = "rememberMe";

    protected Connection connection;

    protected TextField loginField;
    protected PasswordField passwordField;
    protected AbstractSelect localesSelect;

    protected Locale resolvedLocale;
    protected Map<String, Locale> locales;

    protected GlobalConfig globalConfig;
    protected WebConfig webConfig;

    protected CheckBox rememberMe;
    protected boolean loginByRememberMe = false;
    protected Property.ValueChangeListener loginChangeListener;

    protected Button okButton;

    protected Messages messages;
    protected Configuration configuration;
    protected PasswordEncryption passwordEncryption;

    protected UserManagementService userManagementService;

    public LoginWindow(App app, Connection connection) {
        log.trace("Creating " + this);

        configuration = AppBeans.get(Configuration.NAME);
        messages = AppBeans.get(Messages.NAME);
        passwordEncryption = AppBeans.get(PasswordEncryption.NAME);
        userManagementService = AppBeans.get(UserManagementService.NAME);

        globalConfig = configuration.getConfig(GlobalConfig.class);
        webConfig = configuration.getConfig(WebConfig.class);
        locales = globalConfig.getAvailableLocales();

        resolvedLocale = resolveLocale(app);

        setCaption(messages.getMessage(getMessagesPack(), "loginWindow.caption", resolvedLocale));
        this.connection = connection;

        loginField = new TextField();
        passwordField = new PasswordField();
        localesSelect = new NativeSelect();
        okButton = new Button();

        // make fields immediate to resync fast in case of login is already performed from another UI (i.e. browser tab)
        loginField.setImmediate(true);
        passwordField.setImmediate(true);
        localesSelect.setImmediate(true);

        if (app.isCookiesEnabled() && webConfig.getRememberMeEnabled()) {
            rememberMe = new CheckBox();
        }

        initUI(app);

        if (app.isTestMode()) {
            loginField.setDebugId("loginField");
            passwordField.setDebugId("pwdField");
            localesSelect.setDebugId("localesField");
            if (okButton != null) {
                okButton.setDebugId("loginSubmitButton");
            }

            loginField.setCubaId("loginField");
            passwordField.setCubaId("passwordField");
            localesSelect.setCubaId("localesField");
            okButton.setCubaId("loginSubmitButton");

            if (rememberMe != null) {
                rememberMe.setCubaId("rememberMeCheckBox");
            }
        }

        addActionHandler(this);

        setPositionX(100);
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

    protected void initStandartUI(App app, int formWidth, int formHeight, int fieldWidth, boolean localesSelectVisible) {

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setStyleName("mainLayout");

        Form form = new Form(new FormLayout());
        form.setStyleName("loginForm");
        form.setWidth("-1px");
        form.setHeight("-1px");
        FormLayout formLayout = (FormLayout) form.getLayout();
        formLayout.setSpacing(true);
        formLayout.setWidth("-1px");

        HorizontalLayout welcomeLayout = new HorizontalLayout();
        welcomeLayout.setStyleName("login-form-caption");
        welcomeLayout.setWidth("-1px");
        welcomeLayout.setHeight("-1px");
        welcomeLayout.setSpacing(true);

        String welcomeMsg = messages.getMessage(getMessagesPack(), "loginWindow.welcomeLabel", resolvedLocale);
        Label label = new Label(welcomeMsg.replace("\n", "<br/>"));
        label.setContentMode(Label.CONTENT_XHTML);
        label.setWidth("-1px");
        label.setStyleName("login-caption");

        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setStyleName("loginBottom");
        centerLayout.setMargin(true, false, false, false);
        centerLayout.setSpacing(false);
        centerLayout.setWidth(formWidth + "px");
        centerLayout.setHeight(formHeight + "px");

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setStyleName("login-title");
        titleLayout.setSpacing(true);

        Embedded logoImage = getLogoImage();
        if (logoImage != null) {
            titleLayout.addComponent(logoImage);
            titleLayout.setComponentAlignment(logoImage, Alignment.MIDDLE_LEFT);
        }
        if (!StringUtils.isBlank((String) label.getValue())) {
            titleLayout.addComponent(label);
            titleLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        }

        centerLayout.addComponent(titleLayout);
        centerLayout.setComponentAlignment(titleLayout, Alignment.MIDDLE_CENTER);

        centerLayout.addComponent(form);
        centerLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        loginField.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.loginField", resolvedLocale));
        form.addField("loginField", loginField);
        loginField.setWidth(fieldWidth + "px");
        loginField.setStyleName("login-field");
        formLayout.setComponentAlignment(loginField, Alignment.MIDDLE_CENTER);

        passwordField.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.passwordField", resolvedLocale));
        passwordField.setWidth(fieldWidth + "px");
        passwordField.setStyleName("password-field");
        form.addField("passwordField", passwordField);
        formLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);

        if (localesSelectVisible) {
            localesSelect.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.localesSelect", resolvedLocale));
            localesSelect.setWidth(fieldWidth + "px");
            localesSelect.setNullSelectionAllowed(false);
            formLayout.addComponent(localesSelect);
            formLayout.setComponentAlignment(localesSelect, Alignment.MIDDLE_CENTER);
        }

        if (rememberMe != null) {
            rememberMe.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.rememberMe", resolvedLocale));
            rememberMe.setStyleName("rememberMe");
            form.addField("rememberMe", rememberMe);
            formLayout.setComponentAlignment(rememberMe, Alignment.MIDDLE_CENTER);
        }

        okButton.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.okButton", resolvedLocale));
        okButton.addListener(new SubmitListener());
        okButton.setStyleName("submit-login-btn");
        okButton.setIcon(new VersionedThemeResource("app/images/login-button.png"));
        form.addField("button", okButton);
        formLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);

        mainLayout.addComponent(centerLayout);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);

        initFields(app);
        loginField.focus();

        setContent(mainLayout);
    }

    @Nullable
    protected Embedded getLogoImage() {
        final String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", resolvedLocale);
        if ("loginWindow.logoImage".equals(loginLogoImagePath))
            return null;

        return new Embedded(null, new VersionedThemeResource(loginLogoImagePath));
    }

    protected void initUI(App app) {
        initStandartUI(app, 310, -1, 125, configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible());
    }

    protected void initRememberMe(final App app) {
        if (!app.isCookiesEnabled())
            return;

        String rememberMeCookie = app.getCookieValue(COOKIE_REMEMBER_ME);
        if (Boolean.parseBoolean(rememberMeCookie)) {
            String login;
            String encodedLogin = app.getCookieValue(COOKIE_LOGIN) != null ? app.getCookieValue(COOKIE_LOGIN) : "";
            try {
                login = URLDecoder.decode(encodedLogin, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                login = encodedLogin;
            }

            String rememberMeToken = app.getCookieValue(COOKIE_PASSWORD) != null ? app.getCookieValue(COOKIE_PASSWORD) : "";
            if (connection.checkRememberMe(login, rememberMeToken)) {
                rememberMe.setValue(true);
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

            loginField.addListener(loginChangeListener);
            passwordField.addListener(loginChangeListener);
        } else {
            rememberMe.setValue(false);
            loginChangeListener = null;
        }
    }

    protected void initFields(App app) {
        String currentLocale = messages.getTools().localeToString(resolvedLocale);
        String selected = null;
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            localesSelect.addItem(entry.getKey());
            if (messages.getTools().localeToString(entry.getValue()).equals(currentLocale)) {
                selected = entry.getKey();
            }
        }
        if (selected == null) {
            selected = locales.keySet().iterator().next();
        }
        localesSelect.setValue(selected);

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            loginField.setValue(app.getUser() == null ? "" : ((Principal) app.getUser()).getName());
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

        if (webConfig.getRememberMeEnabled()) {
            initRememberMe(app);
        }
    }

    public class SubmitListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            doLogin();
        }
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        final Action[] actions = new Action[1];
        actions[0] = new ShortcutAction("Default key",
                ShortcutAction.KeyCode.ENTER, null);
        return actions;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (sender == this) {
            doLogin();
        }
    }

    protected void login() {
        String login = (String) loginField.getValue();
        String password = passwordField.getValue() != null ? (String) passwordField.getValue() : "";

        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            String message = messages.getMessage(getMessagesPack(), "loginWindow.emptyLoginOrPassword", resolvedLocale);
            showNotification(message, Notification.TYPE_WARNING_MESSAGE);
            return;
        }

        try {
            Locale locale = getUserLocale();
            App.getInstance().setLocale(locale);

            if (loginByRememberMe && rememberMe != null) {
                loginByRememberMe(login, password, locale);
            } else if (ActiveDirectoryHelper.useActiveDirectory()) {
                // try to login as AD user, fallback to regular authentication
                // we use resolved locale for error messages
                if (loginActiveDirectory(login, password, resolvedLocale)) {
                    login = convertLoginString(login);

                    ((ActiveDirectoryConnection) connection).loginActiveDirectory(login, locale);
                } else {
                    login(login, passwordEncryption.getPlainHash(password), locale);
                }
            } else {
                login(login, passwordEncryption.getPlainHash(password), locale);
            }
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());

            String message = messages.getMessage(getMessagesPack(), "loginWindow.loginFailed", resolvedLocale);
            showNotification(
                    ComponentsHelper.preprocessHtmlMessage(message),
                    e.getMessage(), Notification.TYPE_ERROR_MESSAGE);

            if (loginByRememberMe) {
                loginByRememberMe = false;
                loginField.removeListener(loginChangeListener);
                passwordField.removeListener(loginChangeListener);
                loginChangeListener = null;
            }
        }
    }

    protected boolean loginActiveDirectory(String login, String passwordValue, Locale locale) {
        CubaAuthProvider authProvider = AppBeans.get(CubaAuthProvider.NAME);
        try {
            authProvider.authenticate(login, passwordValue, locale);
        } catch (LoginException e) {
            log.debug("Login to AD failed: " + e.toString());
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
            if (rememberMe != null) {
                App app = App.getInstance();
                if (Boolean.TRUE.equals(rememberMe.getValue())) {
                    if (!loginByRememberMe) {
                        app.addCookie(COOKIE_REMEMBER_ME, Boolean.TRUE.toString());

                        String login = (String) loginField.getValue();

                        String encodedLogin;
                        try {
                            encodedLogin = URLEncoder.encode(login, "UTF-8");
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

            if (webConfig.getUseSessionFixationProtection()) {
                CubaApplicationContext context = (CubaApplicationContext) App.getInstance().getContext();
                context.reinitializeSession();

                context.getHttpSession().setMaxInactiveInterval(webConfig.getHttpSessionExpirationTimeoutSec());
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