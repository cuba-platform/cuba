/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 12:02:22
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.Window;

public class AppWindow extends Window implements ConnectionListener
{
    private OrderedLayout rootLayout;

    public AppWindow(App app) {
        super();
        setCaption(getAppCaption());
        app.setMainWindow(this);

        rootLayout = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);
        initWelcomeLayout();
        addComponent(rootLayout);
    }

    protected String getAppCaption() {
        return "Cuba Application";
    }

    protected OrderedLayout getRootLayout() {
        return rootLayout;
    }

    public App getApp() {
        return (App) getApplication();
    }

    protected void initWelcomeLayout() {
        Label label = new Label("Hello from Cuba!");
        rootLayout.addComponent(label);

        LoginDialog dialog = new LoginDialog(this, getApp().getConnection());
        dialog.show();
    }

    protected void initMainLayout() {
        Label label = new Label("Logged in as " + getApp().getConnection().getSession().getName());
        rootLayout.addComponent(label);

        Button logoutBtn = new Button("Logout",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        getApp().getConnection().logout();
                    }
                }
        );
        rootLayout.addComponent(logoutBtn);
    }

    public void connectionStateChanged(Connection connection) {
        if (rootLayout != null)
            removeComponent(rootLayout);
        rootLayout = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);

        if (connection.isConnected()) {
            initMainLayout();
        }
        else {
            initWelcomeLayout();
        }

        addComponent(rootLayout);
    }
}
