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

import com.haulmont.cuba.web.log.LogWindow;
import com.haulmont.cuba.web.resource.Messages;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.terminal.ExternalResource;

import java.util.Locale;

public class AppWindow extends Window
{
    private Connection connection;

    private ExpandLayout rootLayout;
    private TabSheet tabSheet;

    public AppWindow(Connection connection) {
        super();
        this.connection = connection;
        setCaption(getAppCaption());

        rootLayout = new ExpandLayout(OrderedLayout.ORIENTATION_VERTICAL);
        initLayout();
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

    protected void initLayout() {
        rootLayout.setMargin(true);
        rootLayout.setSpacing(true);

        ExpandLayout titleLayout = new ExpandLayout(ExpandLayout.ORIENTATION_HORIZONTAL);
        titleLayout.setSpacing(true);
        titleLayout.setHeight(-1);

        Button navBtn = new Button(Messages.getString("navBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        Navigator navigator = new Navigator(AppWindow.this);
                        addWindow(navigator);
                    }
                }
        );
        navBtn.setStyleName(Button.STYLE_LINK);
        titleLayout.addComponent(navBtn);

        Label label = new Label(String.format(Messages.getString("loggedInLabel"),
                connection.getSession().getName(), connection.getSession().getProfile()));
        titleLayout.addComponent(label);

        Button profileBtn = new Button(Messages.getString("profileBtn"),
                new Button.ClickListener()
                {
                    public void buttonClick(Button.ClickEvent event) {
                        ChangeProfileWindow window = new ChangeProfileWindow();
                        window.center();
                        addWindow(window);
                    }
                }
        );
        profileBtn.setStyleName(Button.STYLE_LINK);
        titleLayout.addComponent(profileBtn);

        Button logoutBtn = new Button(Messages.getString("logoutBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        connection.logout();
                        open(new ExternalResource(App.getInstance().getURL()));
                    }
                }
        );
        logoutBtn.setStyleName(Button.STYLE_LINK);
        titleLayout.addComponent(logoutBtn);

        Button viewLogBtn = new Button(Messages.getString("viewLogBtn"),
                new Button.ClickListener()
                {
                    public void buttonClick(Button.ClickEvent event) {
                        LogWindow logWindow = new LogWindow();
                        addWindow(logWindow);
                    }
                }
        );
        viewLogBtn.setStyleName(Button.STYLE_LINK);
        titleLayout.addComponent(viewLogBtn);

        titleLayout.expand(navBtn);

        rootLayout.addComponent(titleLayout);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        rootLayout.addComponent(tabSheet);
        rootLayout.expand(tabSheet);
    }

}
