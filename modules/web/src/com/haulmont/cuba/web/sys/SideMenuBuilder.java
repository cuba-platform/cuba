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
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.SideMenu;
import com.haulmont.cuba.gui.config.MenuCommand;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.components.KeyCombination.getShortcutModifiers;

/**
 * Side menu builder.
 */
@Component(SideMenuBuilder.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SideMenuBuilder {
    public static final String NAME = "cuba_SideMenuBuilder";

    @Inject
    protected UserSession session;

    @Inject
    protected MenuConfig menuConfig;

    @Inject
    protected MessageTools messageTools;

    public SideMenuBuilder() {
    }

    public void build(SideMenu menu) {
        build(menu, menuConfig.getRootItems());
    }

    protected void build(SideMenu menu, List<MenuItem> rootItems) {
        Window window = ComponentsHelper.getWindowImplementation(menu);

        if (window == null) {
            throw new IllegalStateException("SideMenu is not belong to Window");
        }

        for (MenuItem menuItem : rootItems) {
            // AppMenu does not support separators
            if (menuItem.isPermitted(session)
                    && !menuItem.isSeparator()) {
                createMenuBarItem(window, menu, menuItem);
            }
        }
        removeExtraSeparators(menu);
    }

    protected void removeExtraSeparators(SideMenu menuBar) {
        List<SideMenu.MenuItem> menuItems = menuBar.getMenuItems();
        for (SideMenu.MenuItem item : menuItems.toArray(new SideMenu.MenuItem[0])) {
            removeExtraSeparators(item);
            if (isMenuItemEmpty(item)) {
                menuBar.removeMenuItem(item);
            }
        }
    }

    protected void removeExtraSeparators(SideMenu.MenuItem item) {
        if (!item.hasChildren())
            return;

        // SideMenu does not support separator elements
        if (item.hasChildren()) {
            SideMenu.MenuItem[] menuItems =
                    item.getChildren().toArray(new SideMenu.MenuItem[0]);

            for (SideMenu.MenuItem child : menuItems) {
                removeExtraSeparators(child);
                if (isMenuItemEmpty(child)) {
                    item.removeChildItem(child);
                }
            }
        }
    }

    protected void createMenuBarItem(Window webWindow, SideMenu menu, MenuItem item) {
        if (item.isPermitted(session)) {
            SideMenu.MenuItem menuItem = menu.createMenuItem(item.getId(),
                    menuConfig.getItemCaption(item.getId()), null, createMenuBarCommand(item));

            createSubMenu(webWindow, menu, menuItem, item, session);
            assignStyleName(menuItem, item);
            assignIcon(menuItem, item);
            assignDescription(menuItem, item);
            assignExpanded(menuItem, item);
            assignShortcut(webWindow, menuItem, item);

            if (!isMenuItemEmpty(menuItem)) {
                menu.addMenuItem(menuItem);
            }
        }
    }

    protected void createSubMenu(Window webWindow, SideMenu menu, SideMenu.MenuItem vItem,
                                 MenuItem parentItem, UserSession session) {
        if (parentItem.isPermitted(session)) {
            for (MenuItem child : parentItem.getChildren()) {
                if (child.isSeparator()) {
                    continue;
                }

                if (child.isPermitted(session)) {
                    SideMenu.MenuItem menuItem = menu.createMenuItem(child.getId(),
                            menuConfig.getItemCaption(child.getId()));

                    assignDescription(menuItem, child);
                    assignIcon(menuItem, child);
                    assignStyleName(menuItem, child);

                    if (child.getChildren().isEmpty()) {
                        menuItem.setCommand(createMenuBarCommand(child));

                        assignShortcut(webWindow, menuItem, child);

                        vItem.addChildItem(menuItem);
                    } else {
                        createSubMenu(webWindow, menu, menuItem, child, session);

                        assignExpanded(menuItem, child);

                        if (!isMenuItemEmpty(menuItem)) {
                            vItem.addChildItem(menuItem);
                        }
                    }
                }
            }
        }
    }

    protected void assignExpanded(SideMenu.MenuItem menuItem, MenuItem item) {
        menuItem.setExpanded(item.isExpanded());
    }

    protected Consumer<SideMenu.MenuItem> createMenuBarCommand(final MenuItem item) {
        if (!item.getChildren().isEmpty() || item.isMenu())     //check item is menu
            return null;

        return createMenuCommandExecutor(item);
    }

    protected Consumer<SideMenu.MenuItem> createMenuCommandExecutor(MenuItem item) {
        return new MenuCommandExecutor(item);
    }

    protected boolean isMenuItemEmpty(SideMenu.MenuItem menuItem) {
        return !menuItem.hasChildren() && menuItem.getCommand() == null;
    }

    protected void assignStyleName(SideMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getStylename() != null) {
            menuItem.setStyleName(conf.getStylename());
        }
    }

    protected void assignDescription(SideMenu.MenuItem menuItem, MenuItem conf) {
        String description = conf.getDescription();
        if (StringUtils.isNotEmpty(description)) {
            menuItem.setDescription(messageTools.loadString(description));
        }
    }

    protected void assignIcon(SideMenu.MenuItem menuItem, MenuItem conf) {
        if (conf.getIcon() != null) {
            menuItem.setIcon(conf.getIcon());
        }
    }

    protected void assignShortcut(Window webWindow, SideMenu.MenuItem menuItem, MenuItem item) {
        KeyCombination itemShortcut = item.getShortcut();
        if (itemShortcut != null) {
            ShortcutListener shortcut = new SideMenuShortcutListener(menuItem, item);

            AbstractComponent windowImpl = webWindow.unwrap(AbstractComponent.class);
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

    public static class MenuCommandExecutor implements Consumer<SideMenu.MenuItem> {
        private final MenuItem item;

        public MenuCommandExecutor(MenuItem item) {
            this.item = item;
        }

        @Override
        public void accept(SideMenu.MenuItem menuItem) {
            MenuCommand command = new MenuCommand(item);
            command.execute();
        }
    }
}