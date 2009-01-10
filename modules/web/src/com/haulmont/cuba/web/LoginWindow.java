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

import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.terminal.ExternalResource;
import com.haulmont.cuba.security.global.LoginException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginWindow extends Window
{
    private Connection connection;

    private TextField loginField;
    private TextField passwdField;

    public LoginWindow(Connection connection) {
        super("CUBA Login");
        this.connection = connection;

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
        String defaultUser = System.getProperty("cuba.LoginDialog.defaultUser");
        if (!StringUtils.isBlank(defaultUser))
            loginField.setValue(defaultUser);
        else
            loginField.setValue(null);

        String defaultPassw = System.getProperty("cuba.LoginDialog.defaultPassword");
        if (!StringUtils.isBlank(defaultPassw))
            passwdField.setValue(defaultPassw);
        else
            passwdField.setValue(null);
    }

    private class SubmitListener implements Button.ClickListener
    {
        public void buttonClick(Button.ClickEvent event) {
            String login = (String) loginField.getValue();
            String passwd = DigestUtils.md5Hex((String) passwdField.getValue());
            try {
                connection.login(login, passwd);
                open(new ExternalResource(App.getInstance().getURL()));
            } catch (LoginException e) {
                showNotification(e.getMessage());
            }
        }
    }
}
