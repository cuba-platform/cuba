/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.auth.ActiveDirectoryConnection;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.auth.DomainAliasesResolver;
import com.haulmont.cuba.web.sys.Browser;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

/**
 * Standard login window.
 * <p/>
 * To use a specific implementation override {@link App#createLoginWindow(AppUI)} method.
 *
 * @author krivopustov
 * @version $Id$
 */
public class LoginWindow extends UIView implements Action.Handler {

    protected Log log = LogFactory.getLog(getClass());

    public static final String COOKIE_LOGIN = "rememberMe.Login";
    public static final String COOKIE_PASSWORD = "rememberMe.Password";
    public static final String COOKIE_REMEMBER_ME = "rememberMe";

    private static final char[] DOMAIN_SEPARATORS = new char[]{'\\', '@'};

    /**
     * This key is used to encrypt password in cookie to support "remember me" in AD auth.
     * Must be of 8 symbols.
     */
    private static final String PASSWORD_KEY = "25tuThUw";

    protected Connection connection;

    protected final AppUI ui;

    protected final App app;

    protected VerticalLayout mainLayout;

    protected TextField loginField;
    protected PasswordField passwordField;
    protected AbstractSelect localesSelect;

    protected Locale resolvedLocale;
    protected Map<String, Locale> locales;

    protected GlobalConfig globalConfig;
    protected WebConfig webConfig;

    protected boolean rememberMeAllowed = false;
    protected CheckBox rememberMe;
    protected boolean loginByRememberMe = false;

    protected Property.ValueChangeListener loginChangeListener;

    protected Button okButton;

    protected Messages messages;
    protected Configuration configuration;
    protected PasswordEncryption passwordEncryption;

    /**
     * @deprecated Use {@link #LoginWindow(AppUI)}. In next minor release will be removed
     */
    @Deprecated
    public LoginWindow(App app, Connection connection) {
        this(app.getAppUI());
    }

    public LoginWindow(AppUI ui) {
        log.trace("Creating " + this);
        this.ui = ui;

        configuration = AppBeans.get(Configuration.NAME);
        messages = AppBeans.get(Messages.NAME);
        passwordEncryption = AppBeans.get(PasswordEncryption.NAME);

        globalConfig = configuration.getConfig(GlobalConfig.class);
        webConfig = configuration.getConfig(WebConfig.class);
        locales = globalConfig.getAvailableLocales();

        app = ui.getApp();

        resolvedLocale = resolveLocale(app);

        connection = app.getConnection();

        loginField = new TextField();
        passwordField = new PasswordField();
        localesSelect = new NativeSelect();
        // make fields immediate to resync fast in case of login is already performed from another UI (i.e. browser tab)
        loginField.setImmediate(true);
        passwordField.setImmediate(true);
        localesSelect.setImmediate(true);

        okButton = new Button();

        rememberMeAllowed = !ActiveDirectoryHelper.useActiveDirectory() ||
                !ActiveDirectoryHelper.activeDirectorySupportedBySession();

        if (rememberMeAllowed) {
            rememberMe = new CubaCheckBox();
        }

        setSizeFull();
        setBaseStyle("cuba-login");

        initUI();

        if (ui.isTestMode()) {
            TestIdManager testIdManager = ui.getTestIdManager();

            loginField.setCubaId("loginField");
            passwordField.setCubaId("passwordField");
            localesSelect.setCubaId("localesField");
            okButton.setCubaId("loginSubmitButton");

            if (rememberMe != null) {
                rememberMe.setCubaId("rememberMeCheckBox");
            }

            loginField.setId(testIdManager.reserveId("loginField"));
            passwordField.setId(testIdManager.reserveId("passwordField"));
            localesSelect.setId(testIdManager.reserveId("localesField"));

            okButton.setId(testIdManager.reserveId("loginSubmitButton"));
            if (rememberMe != null) {
                rememberMe.setId(ui.getTestIdManager().reserveId("rememberMeCheckBox"));
            }
        }

        addActionHandler(this);
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
        mainLayout = new VerticalLayout();
        mainLayout.setStyleName(getStyle("main-layout"));

        FormLayout loginFormLayout = new FormLayout();
        Panel form = new Panel(loginFormLayout);
        form.setStyleName(getStyle("form"));
        form.setWidth(Sizeable.SIZE_UNDEFINED,  Unit.PIXELS);
        form.setHeight(Sizeable.SIZE_UNDEFINED,  Unit.PIXELS);

        loginFormLayout.setSpacing(true);
        loginFormLayout.setWidth(Sizeable.SIZE_UNDEFINED,  Unit.PIXELS);

        HorizontalLayout welcomeLayout = new HorizontalLayout();
        welcomeLayout.setStyleName(getStyle("form-caption"));
        welcomeLayout.setWidth(Sizeable.SIZE_UNDEFINED,  Unit.PIXELS);
        welcomeLayout.setHeight(Sizeable.SIZE_UNDEFINED,  Unit.PIXELS);
        welcomeLayout.setSpacing(true);

        String welcomeMsg = messages.getMessage(getMessagesPack(), "loginWindow.welcomeLabel", resolvedLocale);
        Label label = new Label(welcomeMsg.replace("\n", "<br/>"));
        label.setContentMode(ContentMode.HTML);
        label.setWidth(Sizeable.SIZE_UNDEFINED,  Unit.PIXELS);
        label.setStyleName(getStyle("caption"));

        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setStyleName(getStyle("bottom"));
        centerLayout.setSpacing(false);
        centerLayout.setWidth(formWidth + "px");
        centerLayout.setHeight(formHeight + "px");

        centerLayout.setHeight(formHeight + "px");

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setStyleName(getStyle("title"));
        titleLayout.setSpacing(true);

        Image logoImage = getLogoImage();
        if (logoImage != null) {
            titleLayout.addComponent(logoImage);
            titleLayout.setComponentAlignment(logoImage, Alignment.MIDDLE_LEFT);
        }
        if (!StringUtils.isBlank(label.getValue())) {
            titleLayout.addComponent(label);
            titleLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        }

        centerLayout.addComponent(titleLayout);
        centerLayout.setComponentAlignment(titleLayout, Alignment.MIDDLE_CENTER);

        centerLayout.addComponent(form);
        centerLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        loginField.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.loginField", resolvedLocale));
        loginFormLayout.addComponent(loginField);
        loginField.setWidth(fieldWidth + "px");
        loginField.setStyleName(getStyle("username-field"));
        loginFormLayout.setComponentAlignment(loginField, Alignment.MIDDLE_CENTER);

        passwordField.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.passwordField", resolvedLocale));
        passwordField.setWidth(fieldWidth + "px");
        passwordField.setStyleName(getStyle("password-field"));
        loginFormLayout.addComponent(passwordField);
        loginFormLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);

        if (localesSelectVisible) {
            localesSelect.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.localesSelect", resolvedLocale));
            localesSelect.setWidth(fieldWidth + "px");
            localesSelect.setNullSelectionAllowed(false);
            loginFormLayout.addComponent(localesSelect);
            loginFormLayout.setComponentAlignment(localesSelect, Alignment.MIDDLE_CENTER);
        }

        if (rememberMeAllowed) {
            rememberMe.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.rememberMe", resolvedLocale));
            rememberMe.setStyleName(getStyle("remember-me"));
            loginFormLayout.addComponent(rememberMe);
            loginFormLayout.setComponentAlignment(rememberMe, Alignment.MIDDLE_CENTER);
        }

        okButton.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.okButton", resolvedLocale));
        okButton.addClickListener(new SubmitListener());
        okButton.setStyleName(getStyle("submit"));
        okButton.setIcon(new VersionedThemeResource("app/images/login-button.png"));

        loginFormLayout.addComponent(okButton);
        loginFormLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);

        mainLayout.addComponent(centerLayout);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);

        initFields();
        loginField.focus();

        Layout userHintLayout = createUserHint();
        if (userHintLayout != null) {
            VerticalLayout wrapLayout = new VerticalLayout();
            wrapLayout.setSpacing(true);
            wrapLayout.addComponent(mainLayout);
            wrapLayout.addComponent(userHintLayout);
            setContent(wrapLayout);
        } else {
            setContent(mainLayout);
        }
    }

    @Nullable
    protected Image getLogoImage() {
        final String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", resolvedLocale);
        if (StringUtils.isBlank(loginLogoImagePath) || "loginWindow.logoImage".equals(loginLogoImagePath))
            return null;

        return new Image(null, new VersionedThemeResource(loginLogoImagePath));
    }

    protected void initUI() {
        initStandartUI(310, -1, 125, configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible());
    }

    protected void initRememberMe() {
        App app = App.getInstance();
        String rememberMeCookie = app.getCookieValue(COOKIE_REMEMBER_ME);
        if (Boolean.parseBoolean(rememberMeCookie)) {
            rememberMe.setValue(true);

            String login;
            String encodedLogin = app.getCookieValue(COOKIE_LOGIN) != null ? app.getCookieValue(COOKIE_LOGIN) : "";
            try {
                login = URLDecoder.decode(encodedLogin, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                login = encodedLogin;
            }

            loginField.setValue(login);
            passwordField.setValue(app.getCookieValue(COOKIE_PASSWORD) != null ? app.getCookieValue(COOKIE_PASSWORD) : "");
            loginByRememberMe = true;

            loginChangeListener = new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    loginByRememberMe = false;
                }
            };

            loginField.addValueChangeListener(loginChangeListener);
            passwordField.addValueChangeListener(loginChangeListener);
        } else {
            rememberMe.setValue(false);
            loginChangeListener = null;
        }
    }

    protected void initFields() {
        String currLocale = messages.getTools().localeToString(resolvedLocale);
        String selected = null;
        App app = App.getInstance();
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            localesSelect.addItem(entry.getKey());
            if (messages.getTools().localeToString(entry.getValue()).equals(currLocale))
                selected = entry.getKey();
        }
        if (selected == null)
            selected = locales.keySet().iterator().next();
        localesSelect.setValue(selected);

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            loginField.setValue(app.getPrincipal() == null ? "" : app.getPrincipal().getName());
            passwordField.setValue("");

            if (!ActiveDirectoryHelper.activeDirectorySupportedBySession())
                initRememberMe();
        } else {
            String defaultUser = webConfig.getLoginDialogDefaultUser();
            if (!StringUtils.isBlank(defaultUser) && !"<disabled>".equals(defaultUser))
                loginField.setValue(defaultUser);
            else
                loginField.setValue("");

            String defaultPassw = webConfig.getLoginDialogDefaultPassword();
            if (!StringUtils.isBlank(defaultPassw) && !"<disabled>".equals(defaultPassw))
                passwordField.setValue(defaultPassw);
            else
                passwordField.setValue("");

            initRememberMe();
        }
    }

    @Override
    public String getTitle() {
        return messages.getMessage(getMessagesPack(), "loginWindow.caption", resolvedLocale);
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
        String login = loginField.getValue();
        try {
            // Login with AD if domain specified
            if (ActiveDirectoryHelper.useActiveDirectory() && StringUtils.containsAny(login, DOMAIN_SEPARATORS)) {
                Locale locale = getUserLocale();
                App.getInstance().setLocale(locale);

                String password = passwordField.getValue();
                if (loginByRememberMe && StringUtils.isNotEmpty(password))
                    password = decryptPassword(password);

                ActiveDirectoryHelper.getAuthProvider().authenticate(login, password, resolvedLocale);
                login = convertLoginString(login);

                ((ActiveDirectoryConnection) connection).loginActiveDirectory(login, locale);
            } else {
                Locale locale = getUserLocale();
                App.getInstance().setLocale(locale);

                String value = passwordField.getValue() != null ? passwordField.getValue() : "";
                String passwd = loginByRememberMe ? value : passwordEncryption.getPlainHash(value);

                login(login, passwd, locale);
            }
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());

            String message = messages.getMessage(getMessagesPack(), "loginWindow.loginFailed", resolvedLocale);
            new Notification(
                    ComponentsHelper.preprocessHtmlMessage(message),
                    StringUtils.abbreviate(e.getMessage(), 1000), Notification.Type.ERROR_MESSAGE, true)
            .show(getUI().getPage());

            if (loginByRememberMe) {
                loginByRememberMe = false;
                loginField.removeValueChangeListener(loginChangeListener);
                passwordField.removeValueChangeListener(loginChangeListener);
                loginChangeListener = null;
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
            String domainAlias = login.substring(atSignPos + 1);
            String domain = aliasesResolver.getDomainName(domainAlias).toUpperCase();
            String userName = login.substring(0, atSignPos);
            login = domain + "\\" + userName;
        }
        return login;
    }

    protected void login(String login, String passwd, Locale locale) throws LoginException {
        connection.login(login, passwd, locale);
    }

    protected void doLogin() {
        login();
        if (rememberMeAllowed) {
            App app = App.getInstance();
            if (Boolean.TRUE.equals(rememberMe.getValue())) {
                if (!loginByRememberMe) {
                    app.addCookie(COOKIE_REMEMBER_ME, String.valueOf(rememberMe.getValue()));

                    String login = loginField.getValue();
                    String password = passwordField.getValue() != null ? passwordField.getValue() : "";

                    String encodedLogin;
                    try {
                        encodedLogin = URLEncoder.encode(login, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        encodedLogin = login;
                    }

                    app.addCookie(COOKIE_LOGIN, StringEscapeUtils.escapeJava(encodedLogin));
                    if (!ActiveDirectoryHelper.useActiveDirectory())
                        app.addCookie(COOKIE_PASSWORD, passwordEncryption.getPlainHash(password));
                    else {
                        if (StringUtils.isNotEmpty(password))
                            app.addCookie(COOKIE_PASSWORD, encryptPassword(password));
                    }
                }
            } else {
                app.removeCookie(COOKIE_REMEMBER_ME);
                app.removeCookie(COOKIE_LOGIN);
                app.removeCookie(COOKIE_PASSWORD);
            }
        }

        if (webConfig.getUseSessionFixationProtection()) {
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
        }
    }

    /**
     * Encrypt password to store in cookie for "remember me". <br/>
     * Used only for AD auth.
     *
     * @param password  plain password
     * @return          encrypted password
     */
    protected String encryptPassword(String password) {
        SecretKeySpec key = new SecretKeySpec(PASSWORD_KEY.getBytes(), "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(PASSWORD_KEY.getBytes());
        String result;
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            result = new String(Hex.encodeHex(cipher.doFinal(password.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return result;
    }

    /**
     * Decrypt the password stored in cookie. <br/>
     * Used only for AD auth.
     *
     * @param password  encrypted password
     * @return          plain password, or input string if decryption fails
     */
    protected String decryptPassword(String password) {
        SecretKeySpec key = new SecretKeySpec(PASSWORD_KEY.getBytes(), "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(PASSWORD_KEY.getBytes());
        String result;
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            result = new String(cipher.doFinal(Hex.decodeHex(password.toCharArray())));
        } catch (Exception e) {
            return password;
        }
        return result;
    }

    protected Locale getUserLocale() {
        String lang = (String) localesSelect.getValue();
        return locales.get(lang);
    }

    protected Layout createUserHint() {
        boolean enableChromeFrame = webConfig.getUseChromeFramePlugin();
        WebBrowser browser = UI.getCurrent().getPage().getWebBrowser();

        if (enableChromeFrame && browser.getBrowserApplication() != null) {
            final Browser browserInfo = Browser.getBrowserInfo(browser.getBrowserApplication());
            if (browserInfo.isIE() && !browserInfo.isChromeFrame()) {
                final Layout layout = new VerticalLayout();
                layout.setStyleName(getStyle("user-hint"));
                layout.addComponent(new Label(messages.getMessage(getMessagesPack(), "chromeframe.hint", resolvedLocale),
                        ContentMode.HTML));
                return layout;
            }
        }
        return null;
    }

    protected String getMessagesPack() {
        return AppConfig.getMessagesPack();
    }
}