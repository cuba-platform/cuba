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

    public AppWindow(WebApplication app) {
        super("Cuba Application");
        app.setMainWindow(this);

        showWelcomeLayout();
    }

    private WebApplication getApp() {
        return (WebApplication) getApplication();
    }

    public void showWelcomeLayout() {
        if (currentLayout != null)
            removeComponent(currentLayout);

        currentLayout = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);

        Label label = new Label("Hello from Cuba!");
        currentLayout.addComponent(label);

        addComponent(currentLayout);

        LoginDialog dialog = new LoginDialog(this, getApp().getConnection());
        dialog.show();
    }

    public void showMainLayout() {
        if (currentLayout != null)
            removeComponent(currentLayout);

        currentLayout = new OrderedLayout(OrderedLayout.ORIENTATION_HORIZONTAL);

        Label label = new Label("Logged in");
        currentLayout.addComponent(label);

        Button logoutBtn = new Button("Logout",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        getApp().getConnection().logout();
                    }
                }
        );
        currentLayout.addComponent(logoutBtn);

        addComponent(currentLayout);
    }

    public void connectionStateChanged(Connection connection) {
        if (connection.isConnected()) {
            showMainLayout();
        }
        else {
            showWelcomeLayout();
        }
    }
}
