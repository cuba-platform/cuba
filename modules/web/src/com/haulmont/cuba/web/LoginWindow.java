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
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Locale;
import java.util.Map;

/**
 * Login window.
 * <p/>
 * Specific application should inherit from this class and create appropriate
 * instance in {@link App#createLoginWindow()} method
 */
public class LoginWindow extends Window
        implements ApplicationContext.TransactionListener,
        Action.Handler, Action.Container {

    public static final String COOKIE_LOGIN = "rememberMe.Login";
    public static final String COOKIE_PASSWORD = "rememberMe.Password";
    public static final String COOKIE_REMEMBER_ME = "rememberMe";

    private Connection connection;

    protected TextField loginField;
    protected TextField passwordField;
    protected AbstractSelect localesSelect;
    protected Locale loc;
    protected Map<String, Locale> locales;

    protected CheckBox rememberMe;
    protected boolean loginByRememberMe = false;
    private Property.ValueChangeListener loginChangeListener;

    protected Button okButton;

    public LoginWindow(App app, Connection connection) {
        super();
        loc = app.getLocale();
        locales = ConfigProvider.getConfig(WebConfig.class).getAvailableLocales();

        setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.caption", loc));
        this.connection = connection;
        app.getContext().addTransactionListener(this);

        loginField = new TextField();
        passwordField = new TextField();
        localesSelect = new NativeSelect();
        okButton = new Button();

        if (!ActiveDirectoryHelper.useActiveDirectory()) {
            rememberMe = new CheckBox();
        }

        initUI(app);

        if (ConfigProvider.getConfig(GlobalConfig.class).getTestMode()) {
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

        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setWidth(formWidth + "px");
        centerLayout.setHeight(formHeight + "px");
        centerLayout.setStyleName("centerLayout");

        Form form = new Form(new FormLayout());
        form.setStyleName("loginForm");
        centerLayout.addComponent(form);
        FormLayout formLayout = (FormLayout) form.getLayout();
        formLayout.setSpacing(true);

        Label label = new Label(MessageProvider.getMessage(getMessagesPack(), "loginWindow.welcomeLabel", loc));
        label.setWidth("-1px");
        label.setStyleName("login-caption");

        Embedded logoImage = getLogoImage(app);

        VerticalLayout wrap = new VerticalLayout();
        wrap.setStyleName("loginBottom");
        wrap.setMargin(false);
        wrap.setWidth(formWidth + "px");
        wrap.setHeight(formHeight + "px");
        if (!StringUtils.isBlank(label.getCaption()))  {
            wrap.addComponent(label);
            wrap.setComponentAlignment(label, Alignment.BOTTOM_CENTER);
        }
        if (logoImage != null) {
            wrap.addComponent(logoImage);
            wrap.setComponentAlignment(logoImage, Alignment.BOTTOM_CENTER);
        }
        wrap.addComponent(form);
        centerLayout.addComponent(wrap);

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

        if (!ActiveDirectoryHelper.useActiveDirectory()) {
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
        setContent(mainLayout);

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

    protected Embedded getLogoImage(App app) {
        String confDirPath = AppContext.getProperty("cuba.confDir");
        String loginLogoImagePath = AppContext.getProperty("cuba.loginLogoImagePath");
        if (confDirPath == null || loginLogoImagePath == null)
            return null;
        File file = new File(confDirPath + loginLogoImagePath);
        if (file.exists())
            return new Embedded(null, new FileResource(file, app));
        return null;
    }


    protected void initUI(App app) {
        initStandartUI(app, 267, 222, 125, true);
    }

    protected void initRememberMe(final App app) {
        if (!app.isCookiesEnabled())
            return;

        String rememberMeCookie = app.getCookieValue(COOKIE_REMEMBER_ME);
        if (Boolean.parseBoolean(rememberMeCookie)) {
            rememberMe.setValue(true);
            loginField.setValue(app.getCookieValue(COOKIE_LOGIN));
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

        rememberMe.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (app.isCookiesEnabled()) {
                    Boolean rememberMe = (Boolean) event.getProperty().getValue();
                    if (rememberMe) {
                        app.addCookie(COOKIE_REMEMBER_ME, String.valueOf(rememberMe));

                        String login = (String) loginField.getValue();
                        String password = (String) passwordField.getValue();

                        app.addCookie(COOKIE_LOGIN, login);
                        app.addCookie(COOKIE_PASSWORD, DigestUtils.md5Hex(password));
                    } else {
                        app.removeCookie(COOKIE_REMEMBER_ME);
                        app.removeCookie(COOKIE_LOGIN);
                        app.removeCookie(COOKIE_PASSWORD);
                    }
                }
            }
        });
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
            loginField.setValue(null);
            passwordField.setValue("");
        } else {
            WebConfig config = ConfigProvider.getConfig(WebConfig.class);

            String defaultUser = config.getLoginDialogDefaultUser();
            if (!StringUtils.isBlank(defaultUser))
                loginField.setValue(defaultUser);
            else
                loginField.setValue("");

            String defaultPassw = config.getLoginDialogDefaultPassword();
            if (!StringUtils.isBlank(defaultPassw))
                passwordField.setValue(defaultPassw);
            else
                passwordField.setValue("");

            initRememberMe(app);
        }
    }

    public void transactionStart(Application application, Object transactionData) {
        HttpServletRequest request = (HttpServletRequest) transactionData;
        if (request.getUserPrincipal() != null
                && ActiveDirectoryHelper.useActiveDirectory()
                && loginField.getValue() == null) {
            loginField.setValue(request.getUserPrincipal().getName());
        }
    }

    public void transactionEnd(Application application, Object transactionData) {
    }

    public class SubmitListener implements Button.ClickListener {
        public void buttonClick(Button.ClickEvent event) {
            login();
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
            login();
        }
    }

    protected void login() {
        String login = (String) loginField.getValue();
        try {
            if (ActiveDirectoryHelper.useActiveDirectory()) {
                ActiveDirectoryHelper.authenticate(login, (String) passwordField.getValue(), loc);
                connection.loginActiveDirectory(login);
            } else {
                String passwd = loginByRememberMe
                        ? (String) passwordField.getValue()
                        : DigestUtils.md5Hex((String) passwordField.getValue());
                Locale locale = getUserLocale();
                App.getInstance().setLocale(locale);
                connection.login(login, passwd, locale);
            }
            open(new ExternalResource(App.getInstance().getMainWindow().getURL()));
        } catch (LoginException e) {
            showNotification(MessageProvider.getMessage(getMessagesPack(), "loginWindow.loginFailed", loc), e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
            if (loginByRememberMe) {
                loginByRememberMe = false;
                loginField.removeListener(loginChangeListener);
                passwordField.removeListener(loginChangeListener);
                loginChangeListener = null;
            }
        }
    }

    protected Locale getUserLocale() {
        String lang = (String) localesSelect.getValue();
        return locales.get(lang);
    }

    protected Layout createUserHint(App app) {
        boolean enableChromeFrame = ConfigProvider.getConfig(WebConfig.class).getUseChromeFramePlugin();
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
        return AppConfig.getInstance().getMessagesPack();
    }
}
