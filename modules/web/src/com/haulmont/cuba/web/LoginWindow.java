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
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.vaadin.Application;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * Login window.
 * <p>
 * Specific application should inherit from this class and create appropriate
 * instance in {@link App#createLoginWindow()} method
 */
public class LoginWindow extends Window
        implements ApplicationContext.TransactionListener,
        Action.Handler, Action.Container
{
    private Connection connection;

    protected TextField loginField;
    protected TextField passwdField;
    protected NativeSelect localesSelect;
    protected Locale loc;
    protected Map<String, Locale> locales;

    protected Button okButton;

    public LoginWindow(App app, Connection connection) {
        super();
        loc = app.getLocale();
        locales = ConfigProvider.getConfig(WebConfig.class).getAvailableLocales();

        setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.caption", loc));
        this.connection = connection;
        app.getContext().addTransactionListener(this);

        loginField = new TextField();
        passwdField = new TextField();
        localesSelect = new NativeSelect();

        initUI(app);

        addActionHandler(this);
    }

    protected void initUI(App app) {
        FormLayout layout = new FormLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        Label label = new Label(MessageProvider.getMessage(getMessagesPack(), "loginWindow.welcomeLabel", loc));
        layout.addComponent(label);

        loginField.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.loginField", loc));
        layout.addComponent(loginField);
        loginField.focus();

        passwdField.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.passwordField", loc));
        passwdField.setSecret(true);
        layout.addComponent(passwdField);

        localesSelect.setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.localesSelect", loc));
        localesSelect.setNullSelectionAllowed(false);
        layout.addComponent(localesSelect);

        initFields();
        loginField.focus();

        okButton = new NativeButton(MessageProvider.getMessage(getMessagesPack(), "loginWindow.okButton", loc),
                new SubmitListener());
        layout.addComponent(okButton);

        Layout userHintLayout = createUserHint(app);
        if (userHintLayout != null) {
            final VerticalLayout wrapLayout = new VerticalLayout();
            wrapLayout.setSpacing(true);
            wrapLayout.addComponent(layout);
            wrapLayout.addComponent(userHintLayout);
            setContent(wrapLayout);
        } else {
            setContent(layout);
        }

        setTheme("saneco");
    }

    protected void initFields() {
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
            passwdField.setValue("");
        }
        else {
            WebConfig config = ConfigProvider.getConfig(WebConfig.class);

            String defaultUser = config.getLoginDialogDefaultUser();
            if (!StringUtils.isBlank(defaultUser))
                loginField.setValue(defaultUser);
            else
                loginField.setValue("");

            String defaultPassw = config.getLoginDialogDefaultPassword();
            if (!StringUtils.isBlank(defaultPassw))
                passwdField.setValue(defaultPassw);
            else
                passwdField.setValue("");
        }
    }

    public void transactionStart(Application application, Object transactionData) {
        HttpServletRequest request = (HttpServletRequest) transactionData;
        if (request.getUserPrincipal() != null
                && ActiveDirectoryHelper.useActiveDirectory()
                && loginField.getValue() == null)
        {
            loginField.setValue(request.getUserPrincipal().getName());
        }
    }

    public void transactionEnd(Application application, Object transactionData) {
    }

    public class SubmitListener implements Button.ClickListener
    {
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
        login();
    }

    protected void login() {
        String login = (String) loginField.getValue();
        try {
            if (ActiveDirectoryHelper.useActiveDirectory()) {
                ActiveDirectoryHelper.authenticate(login, (String) passwdField.getValue(), loc);
                connection.loginActiveDirectory(login);
            }
            else {
                String passwd = DigestUtils.md5Hex((String) passwdField.getValue());
                String lang = (String) localesSelect.getValue();
                Locale locale = locales.get(lang);
                App.getInstance().setLocale(locale);
                connection.login(login, passwd, locale);
            }
            open(new ExternalResource(App.getInstance().getURL()));
        } catch (LoginException e) {
            showNotification(MessageProvider.getMessage(getMessagesPack(), "loginWindow.loginFailed", loc), e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
        }
    }

    protected Layout createUserHint(App app) {
        boolean enableChromeFrame = ConfigProvider.getConfig(WebConfig.class).getUseChromeFramePlugin();
        WebApplicationContext context = (WebApplicationContext) app.getContext();
        WebBrowser browser = context.getBrowser();

        if (enableChromeFrame && browser.getBrowserApplication() != null)
        {
            final Browser browserInfo = Browser.getBrowserInfo(browser.getBrowserApplication());
            if (browserInfo.isIE() && ! browserInfo.isChromeFrame()) {
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
        return "com.haulmont.cuba.web";
    }
}
