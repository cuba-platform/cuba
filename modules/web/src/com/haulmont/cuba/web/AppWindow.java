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

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.web.log.LogWindow;
import com.haulmont.cuba.security.global.UserSession;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.terminal.Sizeable;
import com.itmill.toolkit.ui.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AppWindow extends Window {
    protected Connection connection;
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
             return MessageProvider.getMessage(getClass(), "application.caption", Locale.getDefault());
         }

    public TabSheet getTabSheet() {
             return tabSheet;
         }

    protected void initLayout() {
         }

    protected MenuBar createMenuBar() {
             final MenuBar menuBar = new MenuBar();

             final MenuConfig menuConfig = AppConfig.getInstance().getMenuConfig();
             List<MenuItem> rootItems = menuConfig.getRootItems();
             for (MenuItem menuItem : rootItems) {
                 createMenuBarItem(menuBar, menuItem);
             }

             return menuBar;
         }

    protected HorizontalLayout createTitlePane() {
        HorizontalLayout titleLayout = new HorizontalLayout();

        titleLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        titleLayout.setHeight(20, Sizeable.UNITS_PIXELS); // TODO (abramov) This is a bit tricky

        titleLayout.setSpacing(true);

        Label logoLabel = new Label(MessageProvider.getMessage(getClass(), "logoLabel"));

        Label loggedInLabel = new Label(String.format(MessageProvider.getMessage(getClass(), "loggedInLabel"),
                connection.getSession().getName()));

        Button logoutBtn = new Button(MessageProvider.getMessage(getClass(), "logoutBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        connection.logout();
                        open(new ExternalResource(App.getInstance().getURL()));
                    }
                }
        );
        logoutBtn.setStyleName(Button.STYLE_LINK);

        Button viewLogBtn = new Button(MessageProvider.getMessage(getClass(), "viewLogBtn"),
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
        titleLayout.setExpandRatio(logoLabel, 2);

        titleLayout.addComponent(loggedInLabel);
        titleLayout.setExpandRatio(loggedInLabel, 1);
        
        titleLayout.addComponent(logoutBtn);
        titleLayout.addComponent(viewLogBtn);

        return titleLayout;
    }

    private void createMenuBarItem(MenuBar menuBar, MenuItem item) {
        if (!connection.isConnected()) return;

        final UserSession session = connection.getSession();
        if (item.isPermitted(session)) {
            MenuBar.MenuItem menuItem = menuBar.addItem(item.getCaption(), null);

            if (!item.getChildren().isEmpty()) {
                for (final MenuItem childItem : item.getChildren()) {
                    createMenuItem(menuItem, childItem);
                }
            }
        }
    }

    private void createMenuItem(MenuBar.MenuItem menuItem, MenuItem item) {
        if (!item.isPermitted(connection.getSession()))
            return;

        menuItem.addItem(item.getCaption(), createMenuBarCommand(item));

        if (!item.getChildren().isEmpty()) {
            for (final MenuItem childItem : item.getChildren()) {
                createMenuItem(menuItem, childItem);
            }
        }
    }

    private MenuBar.Command createMenuBarCommand(final MenuItem item) {
        return new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String caption = item.getCaption();
                final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
                WindowInfo windowInfo = windowConfig.getWindowInfo(item.getId());
                App.getInstance().getWindowManager().openWindow(
                        windowInfo,
                        WindowManager.OpenType.NEW_TAB,
                        Collections.<String, Object>singletonMap("caption", caption)
                );
            }
        };
    }
}
