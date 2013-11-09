/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.toolkit.MenuShortcutAction;
import com.haulmont.cuba.web.toolkit.ui.CubaMenuBar;
import com.vaadin.ui.MenuBar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Main menu builder.
 *
 * @author krivopustov
 * @version $Id$
 */
public class MenuBuilder {

    private Log log = LogFactory.getLog(MenuBuilder.class);

    protected AppWindow appWindow;

    protected UserSession session;

    protected CubaMenuBar menuBar;

    protected MenuConfig menuConfig = AppBeans.get(MenuConfig.class);

    public MenuBuilder(AppWindow appWindow, UserSession session, CubaMenuBar menuBar) {
        this.appWindow = appWindow;
        this.session = session;
        this.menuBar = menuBar;
    }

    public void build() {
        List<MenuItem> rootItems = menuConfig.getRootItems();
        for (MenuItem menuItem : rootItems) {
            if (menuItem.isPermitted(session)) {
                createMenuBarItem(menuBar, menuItem);
            }
        }
        removeExtraSeparators(menuBar);
    }

    private void removeExtraSeparators(MenuBar menuBar) {
        for (MenuBar.MenuItem item : new ArrayList<>(menuBar.getItems())) {
            removeExtraSeparators(item);
            if (isMenuItemEmpty(item))
                menuBar.removeItem(item);
        }
    }

    private void removeExtraSeparators(MenuBar.MenuItem item) {
        if (!item.hasChildren())
            return;

        boolean done;
        do {
            done = true;
            if (item.hasChildren()) {
                List<MenuBar.MenuItem> children = new ArrayList<>(item.getChildren());
                for (int i = 0; i < children.size(); i++) {
                    MenuBar.MenuItem child = children.get(i);
                    removeExtraSeparators(child);
                    if (isMenuItemEmpty(child) && (i == 0 || i == children.size() - 1 || isMenuItemEmpty(children.get(i + 1)))) {
                        item.removeChild(child);
                        done = false;
                    }
                }
            }
        } while (!done);
    }

    private void createMenuBarItem(MenuBar menuBar, MenuItem item) {
        if (item.isPermitted(session)) {
            MenuBar.MenuItem menuItem = menuBar.addItem(MenuConfig.getMenuItemCaption(item.getId()), createMenuBarCommand(item));
            assignShortcut(menuItem, item);
            createSubMenu(menuItem, item, session);
            assignDebugIds(menuItem, item);
            if (isMenuItemEmpty(menuItem)) {
                menuBar.removeItem(menuItem);
            }
        }
    }

    private void createSubMenu(MenuBar.MenuItem vItem, MenuItem item, UserSession session) {
        if (item.isPermitted(session) && !item.getChildren().isEmpty()) {
            for (MenuItem child : item.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    if (child.isPermitted(session)) {
                        MenuBar.MenuItem menuItem = (child.isSeparator()) ? vItem.addSeparator() : vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), createMenuBarCommand(child));
                        assignShortcut(menuItem, child);
                        assignDebugIds(menuItem, child);
                    }
                } else {
                    if (child.isPermitted(session)) {
                        MenuBar.MenuItem menuItem = vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), null);
                        assignShortcut(menuItem, child);
                        createSubMenu(menuItem, child, session);
                        assignDebugIds(menuItem, child);
                        if (isMenuItemEmpty(menuItem)) {
                            vItem.removeChild(menuItem);
                        }
                    }
                }
            }
        }
    }

    private MenuBar.Command createMenuBarCommand(final MenuItem item) {
        if (!item.getChildren().isEmpty())     //check item is menu
            return null;

        WindowInfo windowInfo = null;
        final WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
        try {
            windowInfo = windowConfig.getWindowInfo(item.getId());
        } catch (NoSuchScreenException e) {
            log.error("Invalid screen ID for menu item: " + item.getId());
        }

        final MenuCommand command;
        if (windowInfo != null) {
            command = new MenuCommand(appWindow.getWindowManager(), item, windowInfo);
        } else {
            command = null;
        }

        return new com.vaadin.ui.MenuBar.Command() {
            @Override
            public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                if (command != null) {
                    command.execute();
                } else if (item.getParent() != null) {
                    throw new DevelopmentException("Invalid screen ID for menu item: " + item.getId(),
                            "Parent menu ID", item.getParent().getId());
                }
            }
        };
    }

    private boolean isMenuItemEmpty(MenuBar.MenuItem menuItem) {
        return !menuItem.hasChildren() && menuItem.getCommand() == null;
    }

    protected void assignShortcut(MenuBar.MenuItem menuItem, MenuItem item) {
        if (item.getShortcut() != null) {
            MenuShortcutAction shortcut = new MenuShortcutAction(menuItem, "shortcut_" + item.getId(), item.getShortcut());
            appWindow.addAction(shortcut);
            menuBar.setShortcut(menuItem, item.getShortcut());
        }
    }

    protected void assignDebugIds(MenuBar.MenuItem menuItem, MenuItem conf) {
        if (menuBar.getId() != null && !conf.isSeparator()) {
//            vaadin7
//            menuBar.setId(menuItem, menuBar.getDebugId() + ":" + conf.getId());
        }
    }
}
