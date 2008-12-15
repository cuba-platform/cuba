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

import com.itmill.toolkit.ui.*;
import com.haulmont.cuba.web.Navigator;
import com.haulmont.cuba.web.resource.Messages;

import java.util.Locale;

public class AppWindow extends Window implements ConnectionListener
{
    private ExpandLayout rootLayout;
    private TabSheet tabSheet;

    public AppWindow(App app) {
        super();
        setCaption(getAppCaption());
        app.setMainWindow(this);

        createRootLayout();
        initWelcomeLayout();
        setLayout(rootLayout);
    }

    protected String getAppCaption() {
        return Messages.getString("application.caption", Locale.getDefault());
    }

    protected OrderedLayout getRootLayout() {
        return rootLayout;
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public App getApp() {
        return (App) getApplication();
    }

    protected void initWelcomeLayout() {
        Label label = new Label(Messages.getString("welcomeLabel", Locale.getDefault()));
        rootLayout.addComponent(label);

        LoginDialog dialog = new LoginDialog(this, getApp().getConnection());
        dialog.show();
    }

    protected void initMainLayout() {
        Button navBtn = new Button(Messages.getString("navBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        Navigator navigator = new Navigator(AppWindow.this);
                        addWindow(navigator);
                    }
                }
        );
        navBtn.setStyleName(Button.STYLE_LINK);

        Label label = new Label(String.format(Messages.getString("loggedInLabel"), getApp().getConnection().getSession().getName()));

        Button logoutBtn = new Button(Messages.getString("logoutBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        getApp().getConnection().logout();
                    }
                }
        );
        logoutBtn.setStyleName(Button.STYLE_LINK);

        ExpandLayout titleLayout = new ExpandLayout(ExpandLayout.ORIENTATION_HORIZONTAL);
        titleLayout.setSpacing(true);
        titleLayout.setHeight(-1);
        titleLayout.addComponent(navBtn);
        titleLayout.addComponent(label);
        titleLayout.addComponent(logoutBtn);
        titleLayout.expand(navBtn);

        rootLayout.addComponent(titleLayout);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        rootLayout.addComponent(tabSheet);
        rootLayout.expand(tabSheet);
    }

    public void connectionStateChanged(Connection connection) {
        createRootLayout();

        if (connection.isConnected()) {
            initMainLayout();
        }
        else {
            initWelcomeLayout();
        }

        setLayout(rootLayout);
    }

    private void createRootLayout() {
        rootLayout = new ExpandLayout(OrderedLayout.ORIENTATION_VERTICAL);
        rootLayout.setMargin(true);
        rootLayout.setSpacing(true);
    }
}
