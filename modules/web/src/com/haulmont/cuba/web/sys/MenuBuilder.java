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

import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.config.MenuCommand;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.toolkit.MenuShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Main menu builder.
 */
@Component(MenuBuilder.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MenuBuilder {
    public static final String NAME = "cuba_AppMenuBuilder";

    private final Logger log = LoggerFactory.getLogger(MenuBuilder.class);

    @Inject
    protected UserSession session;

    @Inject
    protected MenuConfig menuConfig;

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected Scripting scripting;

    protected AppMenu appMenu;

    public MenuBuilder() {
    }

    public void build(AppMenu appMenu) {
        this.appMenu = appMenu;

        Window window = ComponentsHelper.getWindowImplementation(appMenu);
        if (window == null) {
            throw new IllegalStateException("AppMenu is not belong to Window");
        }

        List<MenuItem> rootItems = menuConfig.getRootItems();
        for (MenuItem menuItem : rootItems) {
            if (menuItem.isPermitted(session)) {
                createMenuBarItem(window, menuItem);
            }
        }
        removeExtraSeparators();
    }

    protected void removeExtraSeparators() {
        for (AppMenu.MenuItem item : new ArrayList<>(appMenu.getMenuItems())) {
            removeExtraSeparators(item);
            if (isMenuItemEmpty(item))
                appMenu.removeMenuItem(item);
        }
    }

    protected void removeExtraSeparators(AppMenu.MenuItem item) {
        if (!item.hasChildren())
            return;

        boolean done;
        do {
            done = true;
            if (item.hasChildren()) {
                List<AppMenu.MenuItem> children = new ArrayList<>(item.getChildren());
                for (int i = 0; i < children.size(); i++) {
                    AppMenu.MenuItem child = children.get(i);
                    removeExtraSeparators(child);
                    if (isMenuItemEmpty(child) && (i == 0 || i == children.size() - 1 || isMenuItemEmpty(children.get(i + 1)))) {
                        item.removeChildItem(child);
                        done = false;
                    }
                }
            }
        } while (!done);
    }

    protected void createMenuBarItem(Window webWindow, MenuItem item) {
        if (item.isPermitted(session)) {
            AppMenu.MenuItem menuItem = appMenu.createMenuItem(item.getId(), menuConfig.getItemCaption(item.getId()),
                    null, createMenuBarCommand(item));

            assignShortcut(webWindow, menuItem, item);
            assignStyleName(menuItem, item);
            assignIcon(menuItem, item);
            assignDescription(menuItem, item);

            createSubMenu(webWindow, menuItem, item, session);

            if (!isMenuItemEmpty(menuItem)) {
                appMenu.addMenuItem(menuItem);
            }
        }
    }

    protected void createSubMenu(Window webWindow, AppMenu.MenuItem vItem, MenuItem item, UserSession session) {
        if (item.isPermitted(session) && !item.getChildren().isEmpty()) {
            for (MenuItem child : item.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    if (child.isPermitted(session)) {
                        if (child.isSeparator()) {
                            vItem.addChildItem(appMenu.createSeparator());
                            continue;
                        }

                        AppMenu.MenuItem menuItem = appMenu.createMenuItem(child.getId(),
                                menuConfig.getItemCaption(child.getId()), null, createMenuBarCommand(child));

                        assignShortcut(webWindow, menuItem, child);
                        assignDescription(menuItem, child);
                        assignIcon(menuItem, child);
                        assignStyleName(menuItem, child);

                        vItem.addChildItem(menuItem);
                    }
                } else {
                    if (child.isPermitted(session)) {
                        AppMenu.MenuItem menuItem = appMenu.createMenuItem(child.getId(),
                                menuConfig.getItemCaption(child.getId()), null, null);

                        assignShortcut(webWindow, menuItem, child);
                        assignDescription(menuItem, child);
                        assignIcon(menuItem, child);
                        assignStyleName(menuItem, child);

                        createSubMenu(webWindow, menuItem, child, session);

                        if (!isMenuItemEmpty(menuItem)) {
                            vItem.addChildItem(menuItem);
                        }
                    }
                }
            }
        }
    }

    protected Consumer<AppMenu.MenuItem> createMenuBarCommand(final MenuItem item) {
        if (CollectionUtils.isNotEmpty(item.getChildren()) || item.isMenu())     //check item is menu
            return null;

        MenuCommand menuCommand = new MenuCommand(item);

        return menuItem ->
                menuCommand.execute();
    }

    protected boolean isMenuItemEmpty(AppMenu.MenuItem menuItem) {
        return !menuItem.hasChildren() && menuItem.getCommand() == null;
    }

    protected void assignShortcut(Window webWindow, AppMenu.MenuItem menuItem, MenuItem item) {
        KeyCombination itemShortcut = item.getShortcut();
        if (itemShortcut != null) {
            ShortcutListener shortcut = new MenuShortcutAction(menuItem, "shortcut_" + item.getId(), item.getShortcut());

            AbstractComponent windowImpl = webWindow.unwrap(AbstractComponent.class);
            windowImpl.addShortcutListener(shortcut);

            appMenu.setMenuItemShortcutCaption(menuItem, itemShortcut.format());
        }
    }

    protected void assignStyleName(AppMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getStylename() != null) {
            menuItem.setStyleName("ms " + conf.getStylename());
        }
    }

    protected void assignDescription(AppMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getDescription() != null) {
            menuItem.setDescription(conf.getDescription());
        }
    }

    protected void assignIcon(AppMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getIcon() != null) {
            menuItem.setIcon(conf.getIcon());
        }
    }
}