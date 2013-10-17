/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.haulmont.cuba.web.sys.auth.DomainAliasesResolver;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
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
import java.security.Principal;
import java.util.Locale;
import java.util.Map;

/**
 * Login window.
 * <p/>
 * Specific application should inherit from this class and create appropriate
 * instance in {@link DefaultApp#createLoginWindow()} method
 *
 * @author krivopustov
 * @version $Id$
 */
@SuppressWarnings("serial")
public class LoginWindow extends Window implements Action.Handler {

    public static final String COOKIE_LOGIN = "rememberMe.Login";
    public static final String COOKIE_PASSWORD = "rememberMe.Password";
    public static final String COOKIE_REMEMBER_ME = "rememberMe";

    protected Log log = LogFactory.getLog(getClass());

    // must be 8 symbols
    private static final String PASSWORD_KEY = "25tuThUw";

    private static final char[] DOMAIN_SEPARATORS = new char[]{'\\', '@'};

    protected Connection connection;

    protected TextField loginField;
    protected PasswordField passwordField;
    protected AbstractSelect localesSelect;
    protected Locale loc;
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

    public LoginWindow(App app, Connection connection) {
        super();
        configuration = AppBeans.get(Configuration.NAME);
        messages = AppBeans.get(Messages.NAME);
        passwordEncryption = AppBeans.get(PasswordEncryption.NAME);

        globalConfig = configuration.getConfig(GlobalConfig.class);
        webConfig = configuration.getConfig(WebConfig.class);
        locales = globalConfig.getAvailableLocales();

        loc = resolveLocale(app);

        setCaption(messages.getMessage(getMessagesPack(), "loginWindow.caption", loc));
        this.connection = connection;

        loginField = new TextField();
        passwordField = new PasswordField();
        localesSelect = new NativeSelect();
        okButton = new Button();

        if (app.isCookiesEnabled()) {
            if (!ActiveDirectoryHelper.useActiveDirectory() ||
                    !ActiveDirectoryHelper.activeDirectorySupportedBySession()) {
                rememberMe = new CheckBox();
            }
        }

        initUI(app);

        if (globalConfig.getTestMode()) {
            WebWindowManager windowManager = app.getWindowManager();
            windowManager.setDebugId(loginField, "loginField");
            windowManager.setDebugId(passwordField, "pwdField");
            windowManager.setDebugId(localesSelect, "localesField");
            if (okButton != null) {
                windowManager.setDebugId(okButton, "loginSubmitButton");
            }
        }

        addActionHandler(this);

        setPositionX(100);
    }

    protected Locale resolveLocale(App app) {
        Locale appLocale = messages.getTools().useLocaleLanguageOnly() ?
                Locale.forLanguageTag(app.getLocale().getLanguage()) : app.getLocale();

        for (Locale locale : locales.values()) {
            if (locale.equals(appLocale)) {
                return locale;
            }
        }
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

        Label label = new Label(messages.getMessage(getMessagesPack(), "loginWindow.welcomeLabel", loc));
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

        loginField.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.loginField", loc));
        form.addField("loginField", loginField);
        loginField.setWidth(fieldWidth + "px");
        loginField.setStyleName("login-field");
        formLayout.setComponentAlignment(loginField, Alignment.MIDDLE_CENTER);

        passwordField.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.passwordField", loc));
        passwordField.setWidth(fieldWidth + "px");
        passwordField.setStyleName("password-field");
        form.addField("passwordField", passwordField);
        formLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);

        if (localesSelectVisible) {
            localesSelect.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.localesSelect", loc));
            localesSelect.setWidth(fieldWidth + "px");
            localesSelect.setNullSelectionAllowed(false);
            formLayout.addComponent(localesSelect);
            formLayout.setComponentAlignment(localesSelect, Alignment.MIDDLE_CENTER);
        }

        if (rememberMe != null) {
            rememberMe.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.rememberMe", loc));
            rememberMe.setStyleName("rememberMe");
            form.addField("rememberMe", rememberMe);
            formLayout.setComponentAlignment(rememberMe, Alignment.MIDDLE_CENTER);
        }

        okButton.setCaption(messages.getMessage(getMessagesPack(), "loginWindow.okButton", loc));
        okButton.addListener(new SubmitListener());
        okButton.setStyleName("submit-login-btn");
        okButton.setIcon(new ThemeResource("images/tick.png"));
        form.addField("button", okButton);
        formLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);

        mainLayout.addComponent(centerLayout);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);

        initFields(app);
        loginField.focus();

        Layout userHintLayout = createUserHint(app);
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
    protected Embedded getLogoImage() {
        final String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", loc);
        if ("loginWindow.logoImage".equals(loginLogoImagePath))
            return null;

        return new Embedded(null, new ThemeResource(loginLogoImagePath));
    }

    protected void initUI(App app) {
        initStandartUI(app, 310, -1, 125, configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible());
    }

    protected void initRememberMe(final App app) {
        if (!app.isCookiesEnabled())
            return;

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

            loginField.addListener(loginChangeListener);
            passwordField.addListener(loginChangeListener);
        } else {
            rememberMe.setValue(false);
            loginChangeListener = null;
        }
    }

    protected void initFields(App app) {
        String currLocale = messages.getTools().localeToString(loc);
        String selected = null;
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            localesSelect.addItem(entry.getKey());
            if (messages.getTools().localeToString(entry.getValue()).equals(currLocale))
                selected = entry.getKey();
        }
        if (selected == null)
            selected = locales.keySet().iterator().next();
        localesSelect.setValue(selected);

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            loginField.setValue(app.getUser() == null ? "" : ((Principal) app.getUser()).getName());
            passwordField.setValue("");

            if (!ActiveDirectoryHelper.activeDirectorySupportedBySession())
                initRememberMe(app);
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
        try {
            // Login with AD if domain specified
            if (ActiveDirectoryHelper.useActiveDirectory() && StringUtils.containsAny(login, DOMAIN_SEPARATORS)) {
                Locale locale = getUserLocale();
                App.getInstance().setLocale(locale);
                String password = (String) passwordField.getValue();
                if (loginByRememberMe && StringUtils.isNotEmpty(password))
                    password = decryptPassword(password);

                ActiveDirectoryHelper.getAuthProvider().authenticate(login, password, loc);
                login = convertLoginString(login);

                ((ActiveDirectoryConnection) connection).loginActiveDirectory(login, locale);
            } else {
                String value = passwordField.getValue() != null ? (String) passwordField.getValue() : "";
                String passwd = loginByRememberMe ? value : passwordEncryption.getPlainHash(value);
                Locale locale = getUserLocale();
                App.getInstance().setLocale(locale);
                login(login, passwd, locale);
            }
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            // todo Fix notification about exception while AD Auth
            showNotification(
                    messages.getMessage(getMessagesPack(), "loginWindow.loginFailed", loc),
                    e.getMessage(), Notification.TYPE_ERROR_MESSAGE);

            if (loginByRememberMe) {
                loginByRememberMe = false;
                loginField.removeListener(loginChangeListener);
                passwordField.removeListener(loginChangeListener);
                loginChangeListener = null;
            }
        } catch (Exception e) {
            handleException(e);
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

    protected void handleException(Exception e) {
        if (e instanceof RuntimeException)
            throw (RuntimeException) e;
        else
            throw new RuntimeException(e);
    }

    protected void doLogin() {
        login();
        if (rememberMe != null) {
            App app = App.getInstance();
            if (Boolean.TRUE.equals(rememberMe.getValue())) {
                if (!loginByRememberMe) {
                    app.addCookie(COOKIE_REMEMBER_ME, String.valueOf(rememberMe));

                    String login = (String) loginField.getValue();
                    String password = passwordField.getValue() != null ? (String) passwordField.getValue() : "";

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
    }

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

    // if decrypt password is impossible returns encrypted password
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

    protected Layout createUserHint(App app) {
        boolean enableChromeFrame = webConfig.getUseChromeFramePlugin();
        WebApplicationContext context = (WebApplicationContext) app.getContext();
        WebBrowser browser = context.getBrowser();

        if (enableChromeFrame && browser.getBrowserApplication() != null) {
            final Browser browserInfo = Browser.getBrowserInfo(browser.getBrowserApplication());
            if (browserInfo.isIE() && !browserInfo.isChromeFrame()) {
                final Layout layout = new VerticalLayout();
                layout.setStyleName("loginUserHint");
                layout.addComponent(new Label(messages.getMessage(getMessagesPack(), "chromeframe.hint", loc),
                        Label.CONTENT_XHTML));
                return layout;
            }
        }
        return null;
    }

    protected String getMessagesPack() {
        return AppConfig.getMessagesPack();
    }
}
