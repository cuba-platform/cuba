/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.MenuShortcutAction;
import com.haulmont.cuba.web.toolkit.ui.CubaMenuBar;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Main menu builder.
 *
 */
public class MenuBuilder {

    private Logger log = LoggerFactory.getLogger(MenuBuilder.class);

    protected UserSession session;

    protected CubaMenuBar menuBar;

    protected Window.TopLevelWindow topLevelWindow;

    protected MenuConfig menuConfig = AppBeans.get(MenuConfig.NAME);

    protected UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);

    // call MenuBuilder after attaching menubar to UI
    public MenuBuilder(AppMenu menu) {
        this.session = uss.getUserSession();
        this.menuBar = (CubaMenuBar) WebComponentsHelper.unwrap(menu);
        this.topLevelWindow = ((AppUI) menuBar.getUI()).getTopLevelWindow();
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

    protected void removeExtraSeparators(MenuBar menuBar) {
        for (MenuBar.MenuItem item : new ArrayList<>(menuBar.getItems())) {
            removeExtraSeparators(item);
            if (isMenuItemEmpty(item))
                menuBar.removeItem(item);
        }
    }

    protected void removeExtraSeparators(MenuBar.MenuItem item) {
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

    protected void createMenuBarItem(MenuBar menuBar, MenuItem item) {
        if (item.isPermitted(session)) {
            MenuBar.MenuItem menuItem = menuBar.addItem(MenuConfig.getMenuItemCaption(item.getId()), createMenuBarCommand(item));
            assignShortcut(menuItem, item);
            createSubMenu(menuItem, item, session);
            assignTestId(menuItem, item);
            assignStyleName(menuItem, item);
            assignDescription(menuItem, item);
            if (isMenuItemEmpty(menuItem)) {
                menuBar.removeItem(menuItem);
            }
        }
    }

    protected void createSubMenu(MenuBar.MenuItem vItem, MenuItem item, UserSession session) {
        if (item.isPermitted(session) && !item.getChildren().isEmpty()) {
            for (MenuItem child : item.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    if (child.isPermitted(session)) {
                        MenuBar.MenuItem menuItem = (child.isSeparator()) ? vItem.addSeparator() : vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), createMenuBarCommand(child));
                        assignShortcut(menuItem, child);
                        assignTestId(menuItem, child);
                        assignDescription(menuItem, child);
                        assignStyleName(menuItem, child);
                    }
                } else {
                    if (child.isPermitted(session)) {
                        MenuBar.MenuItem menuItem = vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), null);
                        assignShortcut(menuItem, child);
                        createSubMenu(menuItem, child, session);
                        assignTestId(menuItem, child);
                        assignDescription(menuItem, child);
                        assignStyleName(menuItem, child);
                        if (isMenuItemEmpty(menuItem)) {
                            vItem.removeChild(menuItem);
                        }
                    }
                }
            }
        }
    }

    protected MenuBar.Command createMenuBarCommand(final MenuItem item) {
        if (!item.getChildren().isEmpty() || item.isMenu())     //check item is menu
            return null;

        WindowInfo windowInfo = null;
        final WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        try {
            windowInfo = windowConfig.getWindowInfo(item.getId());
        } catch (NoSuchScreenException e) {
            log.error("Invalid screen ID for menu item: " + item.getId());
        }

        final MenuCommand command;
        if (windowInfo != null) {
            command = new MenuCommand(item, windowInfo);
        } else {
            command = null;
        }

        return new com.vaadin.ui.MenuBar.Command() {
            @Override
            public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                if (command != null) {
                    command.execute();
                } else {
                    if (item.getParent() != null) {
                        throw new DevelopmentException("Invalid screen ID for menu item: " + item.getId(),
                                "Parent menu ID", item.getParent().getId());
                    } else {
                        throw new DevelopmentException("Invalid screen ID for menu item: " + item.getId());
                    }
                }
            }
        };
    }

    protected boolean isMenuItemEmpty(MenuBar.MenuItem menuItem) {
        return !menuItem.hasChildren() && menuItem.getCommand() == null;
    }

    protected void assignShortcut(MenuBar.MenuItem menuItem, MenuItem item) {
        if (item.getShortcut() != null && menuItem.getCommand() != null) {
            MenuShortcutAction shortcut = new MenuShortcutAction(menuItem, "shortcut_" + item.getId(), item.getShortcut());
            topLevelWindow.unwrap(AbstractComponent.class).addShortcutListener(shortcut);
            menuBar.setShortcut(menuItem, item.getShortcut());
        }
    }

    protected void assignTestId(MenuBar.MenuItem menuItem, MenuItem conf) {
        if (menuBar.getId() != null && menuBar.getCubaId() != null && !conf.isSeparator()) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            String id = testIdManager.normalize(conf.getId());
            String testId = menuBar.getId() + "_" + id;
            testIdManager.reserveId(testId);

            menuBar.setTestId(menuItem, testId);
            menuBar.setCubaId(menuItem, conf.getId());
        }
    }

    protected void assignStyleName(MenuBar.MenuItem menuItem, MenuItem conf) {
        if (conf.getStylename() != null) {
            menuItem.setStyleName("ms " + conf.getStylename());
        }
    }

    protected void assignDescription(MenuBar.MenuItem menuItem, MenuItem conf) {
        if (conf.getDescription() != null) {
            menuItem.setDescription(conf.getDescription());
        }
    }
}