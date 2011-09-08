/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 05.01.2009 11:59:54
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.export.ResourceDataProvider;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.gui.components.WebEmbeddedApplicationResource;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.service.FileTypeResolver;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.InputStream;
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
 */
@SuppressWarnings("serial")
public class LoginWindow extends Window implements Action.Handler {

    public static final String COOKIE_LOGIN = "rememberMe.Login";
    public static final String COOKIE_PASSWORD = "rememberMe.Password";
    public static final String COOKIE_REMEMBER_ME = "rememberMe";

    protected Connection connection;

    protected TextField loginField;
    protected TextField passwordField;
    protected AbstractSelect localesSelect;
    protected Locale loc;
    protected Map<String, Locale> locales;
    protected GlobalConfig globalConfig;
    protected WebConfig webConfig;

    protected CheckBox rememberMe;
    protected boolean loginByRememberMe = false;
    protected Property.ValueChangeListener loginChangeListener;

    protected Button okButton;

    public LoginWindow(App app, Connection connection) {
        super();
        loc = app.getLocale();
        globalConfig = ConfigProvider.getConfig(GlobalConfig.class);
        webConfig = ConfigProvider.getConfig(WebConfig.class);
        locales = globalConfig.getAvailableLocales();

        setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.caption", loc));
        this.connection = connection;

        loginField = new TextField();
        passwordField = new TextField();
        localesSelect = new NativeSelect();
        okButton = new Button();

        if (!ActiveDirectoryHelper.useActiveDirectory() && app.isCookiesEnabled()) {
            rememberMe = new CheckBox();
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

    protected void initStandartUI(App app, int formWidth, int formHeight, int fieldWidth, boolean localesSelectVisible) {

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setStyleName("mainLayout");

        Form form = new Form(new FormLayout());
        form.setStyleName("loginForm");
        form.setWidth("-1px");
        FormLayout formLayout = (FormLayout) form.getLayout();
        formLayout.setSpacing(true);
        formLayout.setWidth("-1px");

        Label label = new Label(MessageProvider.getMessage(getMessagesPack(), "loginWindow.welcomeLabel", loc));
        label.setWidth("-1px");
        label.setStyleName("login-caption");

        Embedded logoImage = getLogoImage(app);

        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setStyleName("loginBottom");
        centerLayout.setMargin(true, false, false, false);
        centerLayout.setSpacing(false);
        centerLayout.setWidth(formWidth + "px");
        centerLayout.setHeight(formHeight + "px");
        if (!StringUtils.isBlank((String) label.getValue()))  {
            centerLayout.addComponent(label);
            centerLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        }
        if (logoImage != null) {
            centerLayout.addComponent(logoImage);
            centerLayout.setComponentAlignment(logoImage, Alignment.MIDDLE_CENTER);
        }
        centerLayout.addComponent(form);
        centerLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        loginField.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.loginField", loc));
        form.addField("loginField", loginField);
        loginField.setWidth(fieldWidth + "px");
        loginField.setStyleName("login-field");
        formLayout.setComponentAlignment(loginField, Alignment.MIDDLE_CENTER);

        passwordField.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.passwordField", loc));
        passwordField.setSecret(true);
        passwordField.setWidth(fieldWidth + "px");
        passwordField.setStyleName("password-field");
        form.addField("passwordField", passwordField);
        formLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);

        if (localesSelectVisible) {
            localesSelect.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.localesSelect", loc));
            localesSelect.setWidth(fieldWidth + "px");
            localesSelect.setNullSelectionAllowed(false);
            formLayout.addComponent(localesSelect);
            formLayout.setComponentAlignment(localesSelect, Alignment.MIDDLE_CENTER);
        }

        if (rememberMe != null) {
            rememberMe.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.rememberMe", loc));
            rememberMe.setStyleName("rememberMe");
            form.addField("rememberMe", rememberMe);
            formLayout.setComponentAlignment(rememberMe, Alignment.MIDDLE_CENTER);
        }

        okButton.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.okButton", loc));
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
    protected Embedded getLogoImage(App app) {
        final String loginLogoImagePath = webConfig.getLoginLogoImagePath();
        if (loginLogoImagePath == null)
            return null;

        ResourceDataProvider dataProvider = new ResourceDataProvider(loginLogoImagePath);
        InputStream stream = dataProvider.provide();
        if (stream != null) {
            IOUtils.closeQuietly(stream);
            WebEmbeddedApplicationResource resource = new WebEmbeddedApplicationResource(
                    dataProvider,
                    "loginLogoImage",
                    FileTypeResolver.getMIMEType(loginLogoImagePath),
                    app
            );
            return new Embedded(null, resource);
        }
        return null;
    }

    protected void initUI(App app) {
        initStandartUI(app, 310, -1, 125, true);
    }

    protected void initRememberMe(final App app) {
        if (!app.isCookiesEnabled())
            return;

        String rememberMeCookie = app.getCookieValue(COOKIE_REMEMBER_ME);
        if (Boolean.parseBoolean(rememberMeCookie)) {
            rememberMe.setValue(true);

            String login;
            String encodedLogin = app.getCookieValue(COOKIE_LOGIN);
            try {
                login = URLDecoder.decode(encodedLogin, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                login = encodedLogin;
            }

            loginField.setValue(login);
            passwordField.setValue(app.getCookieValue(COOKIE_PASSWORD));
            loginByRememberMe = true;

            loginChangeListener = new Property.ValueChangeListener() {
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
        String selected = null;
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            localesSelect.addItem(entry.getKey());
            if (entry.getValue().getLanguage().equals(loc.getLanguage()))
                selected = entry.getKey();
        }
        if (selected == null)
            selected = locales.keySet().iterator().next();
        localesSelect.setValue(selected);

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            loginField.setValue(app.getUser() == null ? null : ((Principal) app.getUser()).getName());
            passwordField.setValue("");
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
        public void buttonClick(Button.ClickEvent event) {
            doLogin();
        }
    }

    public Action[] getActions(Object target, Object sender) {
        final Action[] actions = new Action[1];
        actions[0] = new ShortcutAction("Default key",
                ShortcutAction.KeyCode.ENTER, null);
        return actions;
    }

    public void handleAction(Action action, Object sender, Object target) {
        if (sender == this) {
            doLogin();
        }
    }

    protected void login() {
        String login = (String) loginField.getValue();
        try {
            if (ActiveDirectoryHelper.useActiveDirectory()) {
                Locale locale = getUserLocale();
                App.getInstance().setLocale(locale);
                ActiveDirectoryHelper.authenticate(login, (String) passwordField.getValue(), loc);
                ((ActiveDirectoryConnection) connection).loginActiveDirectory(login, locale);
            } else {
                String passwd = loginByRememberMe
                        ? (String) passwordField.getValue()
                        : DigestUtils.md5Hex((String) passwordField.getValue());
                Locale locale = getUserLocale();
                App.getInstance().setLocale(locale);
                login(login, passwd, locale);
            }
        } catch (LoginException e) {
            showNotification(
                    MessageProvider.getMessage(getMessagesPack(), "loginWindow.loginFailed", loc),
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
                    String password = (String) passwordField.getValue();

                    String encodedLogin;
                    try {
                        encodedLogin = URLEncoder.encode(login, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        encodedLogin = login;
                    }

                    app.addCookie(COOKIE_LOGIN, StringEscapeUtils.escapeJava(encodedLogin));
                    app.addCookie(COOKIE_PASSWORD, DigestUtils.md5Hex(password));
                }
            } else {
                app.removeCookie(COOKIE_REMEMBER_ME);
                app.removeCookie(COOKIE_LOGIN);
                app.removeCookie(COOKIE_PASSWORD);
            }
        }
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
                layout.addComponent(new Label(MessageProvider.getMessage(getMessagesPack(), "chromeframe.hint", loc),
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
