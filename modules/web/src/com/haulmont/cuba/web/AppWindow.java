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
import com.haulmont.cuba.web.toolkit.ui.MenuBar;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.WindowManager;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.event.ItemClickEvent;

import java.util.Locale;
import java.util.List;
import java.util.Collections;

import org.dom4j.Element;

public class AppWindow extends Window
{
    private Connection connection;

    private ExpandLayout rootLayout;
    private MenuBar menuBar;
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

//        Button navBtn = new Button(Messages.getString("navBtn"),
//                new Button.ClickListener() {
//                    public void buttonClick(Button.ClickEvent event) {
//                        Navigator navigator = new Navigator(AppWindow.this);
//                        addWindow(navigator);
//                    }
//                }
//        );
//        navBtn.setStyleName(Button.STYLE_LINK);
//        titleLayout.addComponent(navBtn);

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

//        titleLayout.expand(navBtn);

        rootLayout.addComponent(titleLayout);

        menuBar = new MenuBar();
        initMenuBar();
        rootLayout.addComponent(menuBar);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        rootLayout.addComponent(tabSheet);
        rootLayout.expand(tabSheet);
    }

    private void initMenuBar() {
        List<MenuItem> rootItems = App.getInstance().getMenuConfig().getRootItems();
        for (MenuItem menuItem : rootItems) {
            createMenuItem(menuItem, null);
        }
        menuBar.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                MenuItem menuItem = (MenuItem) event.getItemId();

                final String caption = menuItem.getCaption();

                final Element element = menuItem.getDescriptor();
                final String template = element.attributeValue("template");

                if (template != null) {
                    App.getInstance().getScreenManager().openWindow(
                            template,
                            WindowManager.OpenType.NEW_TAB,
                            Collections.singletonMap("caption", caption));
                } else {
                    final String className = element.attributeValue("class");
                    if (className != null) {
                        try {
                            App.getInstance().getScreenManager().openWindow(
                                    Class.forName(className),
                                    WindowManager.OpenType.NEW_TAB,
                                    Collections.singletonMap("caption", caption));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void createMenuItem(MenuItem menuItem, MenuItem parenItem) {
        menuBar.addItem(menuItem);
        if (parenItem != null) {
            menuBar.setParent(menuItem, parenItem);
        }
        if (menuItem.getChildren().size() == 0) {
            menuBar.setChildrenAllowed(menuItem, false);
        }
        else {
            menuBar.setChildrenAllowed(menuItem, true);
            for (MenuItem item : menuItem.getChildren()) {
                createMenuItem(item, menuItem);
            }
        }
    }
}
