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
import com.vaadin.ui.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

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
    protected Locale loc;

    protected Button okButton;

    public LoginWindow(App app, Connection connection) {
        super();
        loc = app.getLocale();

        setCaption(MessageProvider.getMessage(getMessagesPack(), "loginWindow.caption", loc));
        this.connection = connection;
        app.getContext().addTransactionListener(this);

        loginField = new TextField();
        passwdField = new TextField();

        initUI();

        addActionHandler(this);
    }

    protected void initUI() {
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

        initFields();
        loginField.focus();

        okButton = new Button(MessageProvider.getMessage(getMessagesPack(), "loginWindow.okButton", loc),
                new SubmitListener());
        layout.addComponent(okButton);

        setLayout(layout);
        setTheme("saneco");
    }

    protected void initFields() {
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
                connection.login(login, passwd);
            }
            open(new ExternalResource(App.getInstance().getURL()));
        } catch (LoginException e) {
            showNotification(MessageProvider.getMessage(getMessagesPack(), "loginWindow.loginFailed", loc), e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
        }
    }

    protected String getMessagesPack() {
        return "com.haulmont.cuba.web";
    }
}
