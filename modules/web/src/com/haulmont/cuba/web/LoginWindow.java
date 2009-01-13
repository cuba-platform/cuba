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
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.resource.Messages;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.itmill.toolkit.Application;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.ui.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class LoginWindow extends Window implements ApplicationContext.TransactionListener
{
    private Connection connection;

    private TextField loginField;
    private TextField passwdField;

    public LoginWindow(App app, Connection connection) {
        super("CUBA Login");
        this.connection = connection;

        app.getContext().addTransactionListener(this);

        OrderedLayout layout = new FormLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        Label label = new Label("Welcome to CUBA!");
        layout.addComponent(label);

        loginField = new TextField("Login Name");
        layout.addComponent(loginField);
        loginField.focus();

        passwdField = new TextField("Password");
        passwdField.setSecret(true);
        layout.addComponent(passwdField);

        initFields();

        Button okButton = new Button("Submit", new SubmitListener());
        layout.addComponent(okButton);

        setLayout(layout);
    }

    private void initFields() {
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

    protected class SubmitListener implements Button.ClickListener
    {
        public void buttonClick(Button.ClickEvent event) {
            String login = (String) loginField.getValue();
            try {
                if (ActiveDirectoryHelper.useActiveDirectory()) {
                    ActiveDirectoryHelper.authenticate(login, (String) passwdField.getValue());
                    connection.loginActiveDirectory(login);
                }
                else {
                    String passwd = DigestUtils.md5Hex((String) passwdField.getValue());
                    connection.login(login, passwd);
                }
                open(new ExternalResource(App.getInstance().getURL()));
            } catch (LoginException e) {
                showNotification(Messages.getString("loginWindow.loginFailed"), e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
            }
        }
    }
}
