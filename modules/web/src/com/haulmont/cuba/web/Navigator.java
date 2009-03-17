/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:51:21
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.ApplicationProperties;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.security.global.UserSession;
import com.itmill.toolkit.event.ItemClickEvent;
import com.itmill.toolkit.ui.Tree;
import com.itmill.toolkit.ui.Window;

import java.util.Collections;
import java.util.List;

public class Navigator extends Window
{
    private Tree tree;
    private Window parentWindow;

    public Navigator(Window parentWindow) {
        super(MessageProvider.getMessage(Navigator.class, "navigator.caption"));
        if (parentWindow == null) {
            throw new IllegalArgumentException("parentWindow must not be null");
        }
        this.parentWindow = parentWindow;
        initUI();
    }

    private void initUI() {
        tree = new Tree();

        final MenuConfig menuConfig = ApplicationProperties.getInstance().getMenuConfig();
        List<MenuItem> rootItems = menuConfig.getRootItems();
        for (MenuItem menuItem : rootItems) {
            createTreeItem(menuItem, null);
        }
        for (MenuItem rootItem : rootItems) {
            tree.expandItemsRecursively(rootItem);
        }
        tree.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                MenuItem menuItem = (MenuItem) event.getItemId();
                String caption = menuItem.getCaption();
                final com.haulmont.cuba.gui.config.WindowConfig windowConfig = ApplicationProperties.getInstance().getWindowConfig();
                WindowInfo windowInfo = windowConfig.getWindowInfo(menuItem.getId());
                App.getInstance().getWindowManager().openWindow(
                        windowInfo,
                            WindowManager.OpenType.NEW_TAB,
                            Collections.<String, Object>singletonMap("caption", caption)
                );
                parentWindow.removeWindow(Navigator.this);
            }
        });

        addComponent(tree);
    }

    private void createTreeItem(MenuItem menuItem, MenuItem parenItem) {
        final Connection connection = App.getInstance().getConnection();
        if (!connection.isConnected()) return;
        final UserSession session = connection.getSession();
        if (menuItem.isPermitted(session)) {
             tree.addItem(menuItem);
             if (parenItem != null) {
                 tree.setParent(menuItem, parenItem);
             }
             if (menuItem.getChildren().size() == 0) {
                 tree.setChildrenAllowed(menuItem, false);
             } else {
                 tree.setChildrenAllowed(menuItem, true);
                 for (MenuItem item : menuItem.getChildren()) {
                     createTreeItem(item, menuItem);
                 }
             }
        }
    }
}
