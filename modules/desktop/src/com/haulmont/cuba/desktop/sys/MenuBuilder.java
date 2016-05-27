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

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.validation.ValidationAwareActionListener;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.logging.UserActionsLogger;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MenuBuilder {

    private Logger userActionsLog = LoggerFactory.getLogger(UserActionsLogger.class);

    private UserSession userSession;
    private JMenuBar menuBar;
    private MenuConfig menuConfig;

    public MenuBuilder(UserSession userSession, JMenuBar menuBar) {
        this.userSession = userSession;
        this.menuBar = menuBar;
        menuConfig = AppBeans.get(MenuConfig.NAME);
    }

    public void build() {
        List<MenuItem> rootItems = menuConfig.getRootItems();
        for (MenuItem item : rootItems) {
            if (item.isPermitted(userSession)) {
                createMenuBarItem(menuBar, item);
            }
        }
    }

    private void createMenuBarItem(JMenuBar menuBar, MenuItem item) {
        String caption = MenuConfig.getMenuItemCaption(item.getId());
        if (!item.getChildren().isEmpty() || item.isMenu()) {
            final JMenu jMenu = new JMenu(caption);
            jMenu.addMenuListener(new MenuListener() {
                @Override
                public void menuSelected(MenuEvent e) {
                    jMenu.requestFocus();
                }

                @Override
                public void menuDeselected(MenuEvent e) {
                }

                @Override
                public void menuCanceled(MenuEvent e) {
                }
            });
            jMenu.setName(item.getId());
            menuBar.add(jMenu);
            createSubMenu(jMenu, item);
        } else {
            JMenuItem jMenuItem = new JMenuItem(caption);
            jMenuItem.setName(item.getId());
            //todo remove hardcoded border
            jMenuItem.setBorder(BorderFactory.createEmptyBorder(1, 4, 2, 4));
            assignShortcut(jMenuItem, item);
            jMenuItem.setMaximumSize(new Dimension(jMenuItem.getPreferredSize().width,
                    jMenuItem.getMaximumSize().height));
            assignCommand(jMenuItem, item);
            menuBar.add(jMenuItem);
        }
    }

    private void assignCommand(final JMenuItem jMenuItem, MenuItem item) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo;
        try {
            windowInfo = windowConfig.getWindowInfo(item.getId());
        } catch (NoSuchScreenException e) {
            return;
        }

        final MenuCommand command = new MenuCommand(item, windowInfo);
        jMenuItem.addActionListener(new ValidationAwareActionListener() {
            @Override
            public void actionPerformedAfterValidation(ActionEvent e) {
                command.execute();

                StringBuilder menuPath = new StringBuilder();
                formatMenuPath(item, menuPath);
                userActionsLog.trace("Window {} was opened using menu item {}", windowInfo.getId(), menuPath.toString());
            }
        });
    }

    private void createSubMenu(JMenu jMenu, MenuItem item) {
        List<MenuItem> itemChildren = new LinkedList<>(item.getChildren());
        CollectionUtils.filter(itemChildren, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return ((MenuItem) object).isPermitted(userSession);
            }
        });

        List<MenuItemContainer> items = new ArrayList<>();

        // prepare menu items
        for (MenuItem child : itemChildren) {
            if (child.getChildren().isEmpty()) {
                if (child.isSeparator()) {
                    items.add(new MenuItemContainer());
                } else {
                    JMenuItem jMenuItem = new JMenuItem(MenuConfig.getMenuItemCaption(child.getId()));
                    jMenuItem.setName(child.getId());
                    assignCommand(jMenuItem, child);
                    assignShortcut(jMenuItem, child);
                    items.add(new MenuItemContainer(jMenuItem));
                }
            } else {
                JMenu jChildMenu = new JMenu(MenuConfig.getMenuItemCaption(child.getId()));
                createSubMenu(jChildMenu, child);
                if (!isMenuEmpty(jChildMenu)) {
                    items.add(new MenuItemContainer(jChildMenu));
                }
            }
        }

        // remove unnecessary separators
        if (!items.isEmpty()) {
            Iterator<MenuItemContainer> iterator = items.iterator();
            JMenuItem menuItem = getNextMenuItem(iterator);
            boolean useSeparator = false;

            while (menuItem != null) {
                if (useSeparator)
                    jMenu.addSeparator();

                jMenu.add(menuItem);

                useSeparator = false;
                menuItem = null;

                if (iterator.hasNext()) {
                    MenuItemContainer itemContainer = iterator.next();
                    if (!itemContainer.isSeparator())
                        menuItem = itemContainer.getMenuItem();
                    else {
                        menuItem = getNextMenuItem(iterator);
                        useSeparator = true;
                    }
                }
            }
        }
    }

    private JMenuItem getNextMenuItem(Iterator<MenuItemContainer> iterator) {
        JMenuItem item = null;
        while (iterator.hasNext() && item == null) {
            MenuItemContainer cMenuItem = iterator.next();
            if (!cMenuItem.isSeparator())
                item = cMenuItem.getMenuItem();
        }
        return item;
    }

    private static class MenuItemContainer {
        private JMenuItem menuItem = null;

        private MenuItemContainer() {
        }

        public MenuItemContainer(JMenuItem menuItem) {
            this.menuItem = menuItem;
        }

        public JMenuItem getMenuItem() {
            return menuItem;
        }

        public boolean isSeparator() {
            return this.menuItem == null;
        }
    }

    private boolean isMenuEmpty(JMenu jMenu) {
        return jMenu.getSubElements().length == 0;
    }

    private void assignShortcut(JMenuItem jMenuItem, MenuItem item) {
        if (item.getShortcut() != null) {
            KeyCombination.Key key = item.getShortcut().getKey();
            KeyCombination.Modifier[] modifiers = item.getShortcut().getModifiers();
            KeyCombination combo = new KeyCombination(key, modifiers);
            jMenuItem.setAccelerator(DesktopComponentsHelper.convertKeyCombination(combo));
        }
    }

    private void formatMenuPath(MenuItem menuItem, StringBuilder stringBuilder) {
        if (menuItem.getParent() != null) {
            formatMenuPath(menuItem.getParent(), stringBuilder);
        }

        stringBuilder.append(menuItem.getId());
        stringBuilder.append("->");
    }
}