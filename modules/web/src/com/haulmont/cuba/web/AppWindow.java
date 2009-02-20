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

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.config.ScreenInfo;
import com.haulmont.cuba.web.log.LogWindow;
import com.haulmont.cuba.web.resource.Messages;
import com.haulmont.cuba.web.toolkit.ui.MenuBar;
import com.itmill.toolkit.event.ItemClickEvent;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.terminal.Sizeable;
import com.itmill.toolkit.ui.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AppWindow extends Window
{
    private Connection connection;
    private TabSheet tabSheet;

    public AppWindow(Connection connection) {
        super();

        this.connection = connection;
        setCaption(getAppCaption());

        VerticalLayout rootLayout = createLayout();
        initLayout();
        setLayout(rootLayout);
    }

    protected VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();

        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        // Title Pame
        HorizontalLayout titlePane = createTitlePane();

        final VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.addComponent(titlePane);
        layout.addComponent(titleLayout);

        // Menu & Windows
        final VerticalLayout menuAndTabbedPaneLayout = new VerticalLayout();

        MenuBar menuBar = createMenuBar();
        menuAndTabbedPaneLayout.addComponent(menuBar);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        menuAndTabbedPaneLayout.addComponent(tabSheet);
        menuAndTabbedPaneLayout.setExpandRatio(tabSheet, 1);

        menuAndTabbedPaneLayout.setSizeFull();
        layout.addComponent(menuAndTabbedPaneLayout);
        layout.setExpandRatio(menuAndTabbedPaneLayout, 1);

        return layout;
    }

    protected String getAppCaption() {
        return Messages.getString("application.caption", Locale.getDefault());
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    protected void initLayout() {
    }

    protected MenuBar createMenuBar() {
        final MenuBar menuBar = new MenuBar();

        List<MenuItem> rootItems = App.getInstance().getMenuConfig().getRootItems();
        for (MenuItem menuItem : rootItems) {
            createMenuItem(menuBar, menuItem, null);
        }

        menuBar.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                MenuItem menuItem = (MenuItem) event.getItemId();
                String caption = menuItem.getCaption();
                ScreenInfo screenInfo = App.getInstance().getScreenConfig().getScreenInfo(menuItem.getId());
                App.getInstance().getScreenManager().openWindow(
                            screenInfo,
                            WindowManager.OpenType.NEW_TAB,
                            Collections.<String, Object>singletonMap("caption", caption)
                );
            }
        });

        return menuBar;
    }

    protected HorizontalLayout createTitlePane() {
        HorizontalLayout titleLayout = new HorizontalLayout();

        titleLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        titleLayout.setHeight(20, Sizeable.UNITS_PIXELS); // TODO (abramov) This is a bit tricky

        titleLayout.setSpacing(true);

        Label logoLabel = new Label(Messages.getString("logoLabel"));

        Label loggedInLabel = new Label(String.format(Messages.getString("loggedInLabel"), connection.getSession().getName()));

        Button logoutBtn = new Button(Messages.getString("logoutBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        connection.logout();
                        open(new ExternalResource(App.getInstance().getURL()));
                    }
                }
        );
        logoutBtn.setStyleName(Button.STYLE_LINK);

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

        logoLabel.setSizeFull();
        
        titleLayout.addComponent(logoLabel);
        titleLayout.setExpandRatio(logoLabel, 1);

        titleLayout.addComponent(loggedInLabel);
        titleLayout.addComponent(logoutBtn);
        titleLayout.addComponent(viewLogBtn);

        return titleLayout;
    }

    private void createMenuItem(MenuBar menuBar, MenuItem menuItem, MenuItem parenItem) {
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
                createMenuItem(menuBar, item, menuItem);
            }
        }
    }
}
