/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.12.2008 19:00:25
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.entity.Profile;
import com.itmill.toolkit.ui.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class LoginDialog
{
    private AppWindow appWindow;

    private Connection connection;

    private Window loginWin;
    private TextField loginField;
    private TextField passwdField;
    private Select profileSelect;
    private Button clearButton;

    private boolean authenticated;

    public LoginDialog(AppWindow appWindow, Connection connection) {
        this.appWindow = appWindow;
        this.connection = connection;
    }

    public void show() {
        loginWin = new Window("Login");
        loginWin.setModal(true);
        loginWin.addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                appWindow.addWindow(loginWin);
            }
        });

        OrderedLayout windowLayout = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);
        windowLayout.setSpacing(true);

        OrderedLayout layout = new FormLayout();
        layout.setSpacing(true);

        loginField = new TextField("Login Name");
        layout.addComponent(loginField);
        loginField.focus();

        passwdField = new TextField("Password"); // TODO KK: implement password field with client-side hashing
        passwdField.setSecret(true);
        layout.addComponent(passwdField);

        initFields();

        profileSelect = new Select("Profile");
        profileSelect.setNullSelectionAllowed(false);
        profileSelect.setVisible(false);
        layout.addComponent(profileSelect);

        windowLayout.addComponent(layout);


        OrderedLayout btnLayout = new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL);
        btnLayout.setSpacing(true);

        Button okButton = new Button("Submit", new SubmitListener());
        btnLayout.addComponent(okButton);

        clearButton = new Button("Clear", new ClearListener());
        clearButton.setVisible(false);
        btnLayout.addComponent(clearButton);

        windowLayout.addComponent(btnLayout);

        loginWin.addComponent(windowLayout);

        appWindow.addWindow(loginWin);
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
            if (!authenticated) {
                List<Profile> profiles = connection.authenticate(login, passwd);
                if (profiles.isEmpty()) {
                    throw new RuntimeException("No available profile");
                }
                else if (profiles.size() == 1) {
                    login(profiles.get(0).getName());
                }
                else {
                    authenticated = true;
                    loginField.setEnabled(false);
                    passwdField.setEnabled(false);
                    profileSelect.setVisible(true);
                    clearButton.setVisible(true);

                    profileSelect.removeAllItems();
                    for (Profile profile : profiles) {
                        profileSelect.addItem(profile.getName());
                    }

                    profileSelect.select("Default");
                    profileSelect.focus();
                }
            }
            else {
                String profileName = (String) profileSelect.getValue();
                login(profileName);
            }
        }

        private void login(String profileName) {
            String login = (String) loginField.getValue();
            String passwd = DigestUtils.md5Hex((String) passwdField.getValue());

            connection.login(login, passwd, profileName);

            appWindow.removeWindow(loginWin);
        }
    }

    private class ClearListener implements Button.ClickListener
    {
        public void buttonClick(Button.ClickEvent event) {
            if (authenticated) {
                loginField.setEnabled(true);
                passwdField.setEnabled(true);
                profileSelect.setVisible(false);
                clearButton.setVisible(false);
                initFields();
                loginField.focus();

                authenticated = false;
            }
        }
    }

}
