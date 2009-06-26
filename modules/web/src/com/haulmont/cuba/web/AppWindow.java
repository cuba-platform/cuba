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
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.web.log.LogWindow;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.chile.core.model.MetaClass;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.terminal.Sizeable;
import com.itmill.toolkit.ui.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AppWindow extends Window {
    protected Connection connection;

    protected MenuBar menuBar;
    private TabSheet tabSheet;

    private VerticalLayout rootLayout;
    private HorizontalLayout titlePageLayout;
    private HorizontalLayout menuBarLayout;
    private HorizontalLayout emptyLayout;
    private VerticalLayout tabbedPaneLayout;

    public AppWindow(Connection connection) {
        super();

        this.connection = connection;
        setCaption(getAppCaption());

        rootLayout = createLayout();
        initLayout();
        setLayout(rootLayout);
        postInitLayout();
    }

/*
    private void setMarginSpacing(Layout layout, boolean margin, boolean spacing)
    {
         layout.setMargin(false);
         layout.setSpacing(true);
         layout.setSizeFull();
    }
*/

    protected VerticalLayout createLayout() {
             final VerticalLayout layout = new VerticalLayout();

             layout.setMargin(false);
             layout.setSpacing(false);
             layout.setSizeFull();

             // Title Pane
             //HorizontalLayout titlePane = createTitlePane();
//             titlePane.setStyleName("headtitle-layout-style");

             titlePageLayout = createTitlePane();
             titlePageLayout.setMargin(false);
             titlePageLayout.setSpacing(false);
//             vTitlePageLayout.setSizeFull();

             //vTitlePageLayout.addComponent(titlePane);
             layout.addComponent(titlePageLayout);

             // Menu
             menuBarLayout = createMenuBarLayout();
//             menuLayout.setStyleName("menu-layout-style");
//             menuBar = createMenuBar();
//             menuBarLayout.addComponent(menuBar);
             menuBarLayout.setMargin(false);
             menuBarLayout.setSpacing(false);
//             vMenuBarLayout.setSizeFull();

             layout.addComponent(menuBarLayout);

             emptyLayout = new HorizontalLayout();
             emptyLayout.setMargin(false);
             emptyLayout.setSpacing(false);
             emptyLayout.setSizeFull();

             layout.addComponent(emptyLayout);

             // Windows
             tabbedPaneLayout = new VerticalLayout();
             tabbedPaneLayout.setMargin(true);
             tabbedPaneLayout.setSpacing(true);
//             tabbedPaneLayout.setSizeFull();
//             TabbedPaneLayout.setStyleName("content-layout-style");

             tabSheet = new TabSheet();
             tabSheet.setSizeFull();

             tabbedPaneLayout.setSizeFull();

             tabbedPaneLayout.addComponent(tabSheet);
             tabbedPaneLayout.setExpandRatio(tabSheet, 1);

             layout.addComponent(tabbedPaneLayout);
             layout.setExpandRatio(tabbedPaneLayout, 1);

             return layout;
         }

    protected String getAppCaption() {
             return MessageProvider.getMessage(getClass(), "application.caption", Locale.getDefault());
         }

    public TabSheet getTabSheet() {
             return tabSheet;
         }

    public MenuBar getMenuBar()
    {
        return menuBar;
    }

    public VerticalLayout getRootLayout()
    {
        return rootLayout;
    }

    public HorizontalLayout getTitlePageLayout()
    {
        return titlePageLayout;
    }

    public HorizontalLayout getMenuBarLayout()
    {
        return menuBarLayout;
    }

    public HorizontalLayout getEmptyLayout()
    {
        return emptyLayout;
    }
    public VerticalLayout getTabbedPaneLayout() {
        return tabbedPaneLayout;
    }

    protected void initLayout() {
    }

    protected void postInitLayout() {
    }

    protected HorizontalLayout createMenuBarLayout()
    {
        menuBarLayout = new HorizontalLayout();
        menuBar = createMenuBar();
        menuBarLayout.addComponent(menuBar);
        
        return menuBarLayout;
    }

    protected MenuBar createMenuBar() {
        menuBar = new MenuBar();

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
                        String url = ActiveDirectoryHelper.useActiveDirectory() ? "login" : "";
                        open(new ExternalResource(App.getInstance().getURL() + url));
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
            if (!menuItem.hasChildren()) {
                menuBar.removeItem(menuItem);
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

                final String id = windowInfo.getId();
                if (id.endsWith(".create") || id.endsWith(".edit")) {
                    final String[] strings = id.split("[.]");
                    if (strings.length != 2) throw new UnsupportedOperationException();

                    final String metaClassName = strings[0];
                    final MetaClass metaClass = MetadataProvider.getSession().getClass(metaClassName);
                    if (metaClass == null) throw new IllegalStateException(String.format("Can't find metaClass %s", metaClassName));

                    Object newItem;
                    try {
                        newItem = metaClass.createInstance();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }

                    App.getInstance().getWindowManager().openEditor(
                            windowInfo,
                            newItem,
                            WindowManager.OpenType.NEW_TAB,
                            Collections.<String, Object>singletonMap("caption", caption)
                    );
                } else {
                    App.getInstance().getWindowManager().openWindow(
                            windowInfo,
                            WindowManager.OpenType.NEW_TAB,
                            Collections.<String, Object>singletonMap("caption", caption)
                    );
                }
            }
        };
    }
}
