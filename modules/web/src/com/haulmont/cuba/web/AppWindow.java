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
    private OrderedLayout currentLayout;

    public AppWindow(App app) {
        super();
        setCaption(getAppCaption());
        app.setMainWindow(this);

        currentLayout = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);
        initWelcomeLayout();
        addComponent(currentLayout);
    }

    protected String getAppCaption() {
        return "Cuba Application";
    }

    public App getApp() {
        return (App) getApplication();
    }

    protected void initWelcomeLayout() {
        Label label = new Label("Hello from Cuba!");
        currentLayout.addComponent(label);

        LoginDialog dialog = new LoginDialog(this, getApp().getConnection());
        dialog.show();
    }

    protected void initMainLayout() {
        Label label = new Label("Logged in as " + getApp().getConnection().getSession().getName());
        currentLayout.addComponent(label);

        Button logoutBtn = new Button("Logout",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        getApp().getConnection().logout();
                    }
                }
        );
        currentLayout.addComponent(logoutBtn);
    }

    public void connectionStateChanged(Connection connection) {
        if (currentLayout != null)
            removeComponent(currentLayout);
        currentLayout = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);

        if (connection.isConnected()) {
            initMainLayout();
        }
        else {
            initWelcomeLayout();
        }

        addComponent(currentLayout);
    }
}
