/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.security.global.UserSession;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MenuBuilder {

    private UserSession userSession;
    private JMenuBar menuBar;
    private MenuConfig menuConfig;

    public MenuBuilder(UserSession userSession, JMenuBar menuBar) {
        this.userSession = userSession;
        this.menuBar = menuBar;
        menuConfig = AppBeans.get(MenuConfig.class);
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
        if (!item.getChildren().isEmpty()) {
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

            assignShortcut(jMenu, item);
            menuBar.add(jMenu);
            createSubMenu(jMenu, item);
        } else {
            JMenuItem jMenuItem = new JMenuItem(caption);
            assignShortcut(jMenuItem, item);
            assignCommand(jMenuItem, item);
            menuBar.add(jMenuItem);
        }
    }

    private void assignCommand(final JMenuItem jMenuItem, MenuItem item) {
        WindowInfo windowInfo;
        try {
            windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo(item.getId());
        } catch (NoSuchScreenException e) {
            return;
        }
        final MenuCommand command = new MenuCommand(App.getInstance().getWindowManager(), item, windowInfo);
        jMenuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        command.execute();
                    }
                }
        );
    }

    private void createSubMenu(JMenu jMenu, MenuItem item) {
        for (MenuItem child : item.getChildren()) {
            if (child.isPermitted(userSession)) {
                if (child.getChildren().isEmpty()) {
                    if (child.isSeparator()) {
                        jMenu.addSeparator();
                    } else {
                        JMenuItem jMenuItem = new JMenuItem(MenuConfig.getMenuItemCaption(child.getId()));
                        assignCommand(jMenuItem, child);
                        assignShortcut(jMenuItem, child);
                        jMenu.add(jMenuItem);
                    }
                } else {
                    JMenu jChildMenu = new JMenu(MenuConfig.getMenuItemCaption(child.getId()));
                    assignShortcut(jChildMenu, child);
                    createSubMenu(jChildMenu, child);
                    if (!isMenuEmpty(jChildMenu)) {
                        jMenu.add(jChildMenu);
                    }
                }
            }
        }
    }

    private boolean isMenuEmpty(JMenu jMenu) {
        return jMenu.getSubElements().length == 0;
    }

    private void assignShortcut(JMenuItem jMenuItem, MenuItem item) {
        if (item.getShortcut() != null) {
            ShortcutAction.Key key = item.getShortcut().getKey();
            ShortcutAction.Modifier[] modifiers = item.getShortcut().getModifiers();
            ShortcutAction.KeyCombination combo = new ShortcutAction.KeyCombination(key, modifiers);
            jMenuItem.setAccelerator(DesktopComponentsHelper.convertKeyCombination(combo));
        }
    }
}
