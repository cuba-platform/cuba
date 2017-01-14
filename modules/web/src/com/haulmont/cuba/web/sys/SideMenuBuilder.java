/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.sys;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.SideMenu;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.components.KeyCombination.getShortcutModifiers;

/**
 * Side menu builder.
 */
public class SideMenuBuilder {
    private final Logger log = LoggerFactory.getLogger(SideMenuBuilder.class);

    protected UserSession session;

    protected SideMenu menu;

    protected Window window;

    protected MenuConfig menuConfig = AppBeans.get(MenuConfig.NAME);

    protected UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);

    // call MenuBuilder after attaching menubar to UI
    public SideMenuBuilder(SideMenu menu) {
        this.session = uss.getUserSession();
        this.menu = menu;
        this.window = ComponentsHelper.getWindowImplementation(menu);
    }

    public void build() {
        if (window == null) {
            throw new IllegalStateException("SideMenu is not belong to Window");
        }

        List<MenuItem> rootItems = menuConfig.getRootItems();
        for (MenuItem menuItem : rootItems) {
            if (menuItem.isPermitted(session)) {
                createMenuBarItem(menu, menuItem);
            }
        }
        removeExtraSeparators(menu);
    }

    protected void removeExtraSeparators(SideMenu menuBar) {
        for (SideMenu.MenuItem item : new ArrayList<>(menuBar.getMenuItems())) {
            removeExtraSeparators(item);
            if (isMenuItemEmpty(item))
                menuBar.removeMenuItem(item);
        }
    }

    protected void removeExtraSeparators(SideMenu.MenuItem item) {
        if (!item.hasChildren())
            return;

        boolean done;
        do {
            done = true;
            if (item.hasChildren()) {
                List<SideMenu.MenuItem> children = new ArrayList<>(item.getChildren());
                for (int i = 0; i < children.size(); i++) {
                    SideMenu.MenuItem child = children.get(i);
                    removeExtraSeparators(child);
                    if (isMenuItemEmpty(child) && (i == 0 || i == children.size() - 1 || isMenuItemEmpty(children.get(i + 1)))) {
                        item.removeChildItem(child);
                        done = false;
                    }
                }
            }
        } while (!done);
    }

    protected void createMenuBarItem(SideMenu menuBar, MenuItem item) {
        if (item.isPermitted(session)) {
            SideMenu.MenuItem menuItem = menuBar.createMenuItem(item.getId(),
                    MenuConfig.getMenuItemCaption(item.getId()), null, createMenuBarCommand(item));

            createSubMenu(menuItem, item, session);
            assignTestId(menuItem, item);
            assignStyleName(menuItem, item);
            assignIcon(menuItem, item);
            assignDescription(menuItem, item);
            assignShortcut(menuItem, item);

            if (!isMenuItemEmpty(menuItem)) {
                menuBar.addMenuItem(menuItem);
            }
        }
    }

    protected void createSubMenu(SideMenu.MenuItem vItem, MenuItem parentItem, UserSession session) {
        if (parentItem.isPermitted(session) && !parentItem.getChildren().isEmpty()) {
            for (MenuItem child : parentItem.getChildren()) {
                if (child.isSeparator()) {
                    continue;
                }

                if (child.getChildren().isEmpty()) {
                    if (child.isPermitted(session)) {
                        SideMenu.MenuItem menuItem = menu.createMenuItem(child.getId(),
                                MenuConfig.getMenuItemCaption(child.getId()), null, createMenuBarCommand(child));

                        assignTestId(menuItem, child);
                        assignDescription(menuItem, child);
                        assignIcon(menuItem, child);
                        assignStyleName(menuItem, child);
                        assignShortcut(menuItem, child);

                        vItem.addChildItem(menuItem);
                    }
                } else {
                    if (child.isPermitted(session)) {
                        SideMenu.MenuItem menuItem = menu.createMenuItem(child.getId());
                        menuItem.setCaption(MenuConfig.getMenuItemCaption(child.getId()));

                        createSubMenu(menuItem, child, session);
                        assignTestId(menuItem, child);
                        assignDescription(menuItem, child);
                        assignIcon(menuItem, child);
                        assignStyleName(menuItem, child);
                        assignShortcut(menuItem, child);

                        if (!isMenuItemEmpty(menuItem)) {
                            vItem.addChildItem(menuItem);
                        }
                    }
                }
            }
        }
    }

    protected Consumer<SideMenu.MenuItem> createMenuBarCommand(final MenuItem item) {
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

        return event -> {
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
        };
    }

    protected boolean isMenuItemEmpty(SideMenu.MenuItem menuItem) {
        return !menuItem.hasChildren() && menuItem.getCommand() == null;
    }

    protected void assignTestId(SideMenu.MenuItem menuItem, MenuItem conf) {
        if (menu.getId() != null && !conf.isSeparator()) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            String id = testIdManager.normalize(conf.getId());
            String testId = menu.getId() + "_" + id;
            testIdManager.reserveId(testId);

            menuItem.setTestId(testId);
        }
    }

    protected void assignStyleName(SideMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getStylename() != null) {
            menuItem.setStyleName(conf.getStylename());
        }
    }

    protected void assignDescription(SideMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getDescription() != null) {
            menuItem.setDescription(conf.getDescription());
        }
    }

    protected void assignIcon(SideMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getIcon() != null) {
            menuItem.setIcon(conf.getIcon());
        }
    }

    protected void assignShortcut(SideMenu.MenuItem menuItem, MenuItem item) {
        KeyCombination itemShortcut = item.getShortcut();
        if (itemShortcut != null) {
            ShortcutListener shortcut = new SideMenuShortcutListener(menuItem, item);
            AbstractComponent windowImpl = window.unwrap(AbstractComponent.class);
            windowImpl.addShortcutListener(shortcut);

            if (Strings.isNullOrEmpty(menuItem.getBadgeText())) {
                menuItem.setDescription(itemShortcut.format());
            }
        }
    }

    protected static class SideMenuShortcutListener extends ShortcutListener {
        protected SideMenu.MenuItem menuItem;

        public SideMenuShortcutListener(SideMenu.MenuItem menuItem, MenuItem item) {
            super("shortcut_" + item.getId(),
                    item.getShortcut().getKey().getCode(),
                    getShortcutModifiers(item.getShortcut().getModifiers()));
            this.menuItem = menuItem;
        }

        @Override
        public void handleAction(Object sender, Object target) {
            Consumer<SideMenu.MenuItem> command = menuItem.getCommand();
            if (command != null) {
                command.accept(menuItem);
            }
        }
    }
}