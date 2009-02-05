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

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.web.resource.Messages;
import com.itmill.toolkit.event.ItemClickEvent;
import com.itmill.toolkit.ui.Tree;
import com.itmill.toolkit.ui.Window;
import org.dom4j.Element;

import java.util.Collections;
import java.util.List;

public class Navigator extends Window
{
    private Tree tree;
    private Window parentWindow;

    public Navigator(Window parentWindow) {
        super(Messages.getString("navigator.caption"));
        if (parentWindow == null) {
            throw new IllegalArgumentException("parentWindow must not be null");
        }
        this.parentWindow = parentWindow;
        initUI();
    }

    private void initUI() {
        tree = new Tree();

        List<MenuItem> rootItems = App.getInstance().getMenuConfig().getRootItems();
        for (MenuItem menuItem : rootItems) {
            createTreeItem(menuItem, null);
        }
        for (MenuItem rootItem : rootItems) {
            tree.expandItemsRecursively(rootItem);
        }
        tree.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                MenuItem menuItem = (MenuItem) event.getItemId();

                final String caption = menuItem.getCaption();

                final Element element = menuItem.getDescriptor();
                final String template = element.attributeValue("template");

                if (template != null) {
                    App.getInstance().getScreenManager().openWindow(
                            template,
                            WindowManager.OpenType.NEW_TAB,
                            Collections.<String, Object>singletonMap("caption", caption));
                    parentWindow.removeWindow(Navigator.this);
                } else {
                    final String className = element.attributeValue("class");
                    if (className != null) {
                        try {
                            App.getInstance().getScreenManager().openWindow(
                                    Class.forName(className),
                                    WindowManager.OpenType.NEW_TAB,
                                    Collections.<String, Object>singletonMap("caption", caption));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        parentWindow.removeWindow(Navigator.this);
                    }
                }
            }
        });

        addComponent(tree);
    }

    private void createTreeItem(MenuItem menuItem, MenuItem parenItem) {
        tree.addItem(menuItem);
        if (parenItem != null) {
            tree.setParent(menuItem, parenItem);
        }
        if (menuItem.getChildren().size() == 0) {
            tree.setChildrenAllowed(menuItem, false);
        }
        else {
            tree.setChildrenAllowed(menuItem, true);
            for (MenuItem item : menuItem.getChildren()) {
                createTreeItem(item, menuItem);
            }
        }
    }
}
