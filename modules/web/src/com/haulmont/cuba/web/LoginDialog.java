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

import com.itmill.toolkit.ui.*;

public class LoginDialog
{
    private Window mainWindow;

    private Window loginWin;

    public LoginDialog(Window mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void show() {
        loginWin = new Window("Login");
        loginWin.setModal(true);
        loginWin.addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                mainWindow.addWindow(loginWin);
            }
        });

        OrderedLayout form = new FormLayout();
        form.setSpacing(true);

        TextField loginField = new TextField("Login Name");
        form.addComponent(loginField);

        TextField passwdField = new TextField("Password");
        passwdField.setSecret(true);
        form.addComponent(passwdField);

        Button okButton = new Button("Submit",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        mainWindow.removeWindow(loginWin);
                    }
                }
        );
        form.addComponent(okButton);

        loginWin.addComponent(form);

        mainWindow.addWindow(loginWin);
    }
}
