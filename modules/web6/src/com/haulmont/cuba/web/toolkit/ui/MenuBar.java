/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author budarov
 * @version $Id$
 */
public class MenuBar extends com.vaadin.ui.MenuBar {

    protected Map<MenuItem, String> shortcuts;
    protected boolean vertical;

    protected BiMap<MenuItem, String> debugIds;

    public MenuBar() {
        shortcuts = new HashMap<>();
    }

    public void setShortcut(MenuItem item, KeyCombination shortcut) {
        setShortcut(item, shortcut.format());
    }

    public void setShortcut(MenuItem item, String str) {
        if (shortcuts.containsKey(item)) {
            shortcuts.remove(item);
        }
        shortcuts.put(item, str);
    }

    public void clearShortcut(MenuItem item) {
        shortcuts.remove(item);
    }

    public void setDebugId(MenuItem item, String id) {
        if (debugIds == null) {
            debugIds = HashBiMap.create();
        }
        debugIds.put(item, id);
    }

    public String getDebugId(MenuItem item) {
        if (debugIds != null) {
            return debugIds.get(item);
        }
        return null;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {

        if (variables.containsKey("clickedDebugId")) {
            MenuItem clickedItem  = debugIds.inverse().get(variables.get("clickedDebugId"));
            if (clickedItem != null && clickedItem.isEnabled()) {
                clickedItem.getCommand().menuSelected(clickedItem);
            }
        }

        super.changeVariables(source, variables);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        // Stack for list iterators
        // Stack<Iterator<MenuItem>> iteratorStack = new Stack<>();

        if (isVertical()) {
            target.addAttribute("vertical", true);
        }

        target.startTag("options");

        if (getSubmenuIcon() != null) {
            target.addAttribute("submenuIcon", getSubmenuIcon());
        }

        if (getWidth() > -1) {
            target.startTag("moreItem");
            target.addAttribute("text", getMoreMenuItem().getText());
            if (getMoreMenuItem().getIcon() != null) {
                target.addAttribute("icon", getMoreMenuItem().getIcon());
            }
            target.endTag("moreItem");
        }

        target.endTag("options");
        target.startTag("items");

        // This generates the tree from the contents of the menu
        for (com.vaadin.ui.MenuBar.MenuItem item : getItems()) {
            paintItem(target, item);
        }

        target.endTag("items");
    }

    private void paintItem(PaintTarget target, com.vaadin.ui.MenuBar.MenuItem item)
            throws PaintException {
        if (!item.isVisible()) {
            return;
        }

        target.startTag("item");

        target.addAttribute("id", item.getId());

        if (item.getStyleName() != null) {
            target.addAttribute("style", item.getStyleName());
        }

        if (item.isSeparator()) {
            target.addAttribute("separator", true);
        } else {
            target.addAttribute("text", item.getText());

            Command command = item.getCommand();
            if (command != null) {
                target.addAttribute("command", true);
            }

            Resource icon = item.getIcon();
            if (icon != null) {
                target.addAttribute("icon", icon);
            }

            if (!item.isEnabled()) {
                target.addAttribute("disabled", true);
            }

            //***************************
            if (shortcuts.containsKey(item)) {
                String shortcut = shortcuts.get(item);
                if (shortcut != null) {
                    target.addAttribute("shortcut", shortcut);
                }
            }
            //***************************

            if (debugIds != null && debugIds.containsKey(item)) {
                target.addAttribute("debugId", debugIds.get(item));
            }

            if (item.hasChildren()) {
                for (com.vaadin.ui.MenuBar.MenuItem child : item.getChildren()) {
                    paintItem(target, child);
                }
            }

        }

        target.endTag("item");
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
        requestRepaint();
    }
}