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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
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
        final MenuCommand command = new MenuCommand(App.getInstance().getMainFrame().getWindowManager(), item, windowInfo);
        jMenuItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        command.execute();
                    }
                }
        );
    }

    private void createSubMenu(JMenu jMenu, MenuItem item) {
        List<MenuItem> itemChildren = new LinkedList<>(item.getChildren());
        CollectionUtils.filter(itemChildren, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return ((MenuItem) object).isPermitted(userSession);
            }
        });

        MenuItem prevItem = null;
        for (MenuItem child : itemChildren) {
            if (child.getChildren().isEmpty()) {
                if (child.isSeparator()) {
                    // skip first and last separator
                    if (child != itemChildren.get(0) &&
                            child != itemChildren.get(itemChildren.size() - 1)) {
                        // skip separator after separator
                        if (prevItem == null || !prevItem.isSeparator())
                            jMenu.addSeparator();
                    }
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
            prevItem = child;
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