/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 09.06.2010 14:46:51
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

@SuppressWarnings("serial")
public class MenuBar extends com.vaadin.ui.MenuBar {

    private Map<MenuItem, String> shortcuts;
    private boolean vertical;

    private BiMap<MenuItem, String> debugIds;

    public MenuBar() {
        shortcuts = new HashMap<MenuItem, String>();
    }

    public void setShortcut(MenuItem item, ShortcutAction.KeyCombination shortcut) {
        setShortcut(item, makeCaption(shortcut));
    }

    private String makeCaption(ShortcutAction.KeyCombination shortcut) {
        StringBuilder sb = new StringBuilder();
        if (shortcut.getModifiers() != null) {
            for (ShortcutAction.Modifier mod: shortcut.getModifiers()) {
                sb.append(MessageProvider.getMessage(getClass(), "shortcut." + mod.name()))
                  .append("+");
            }
        }
        sb.append(MessageProvider.getMessage(getClass(), "shortcut." + shortcut.getKey().name()));
        return sb.toString();
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
        Stack<Iterator<MenuItem>> iteratorStack = new Stack<Iterator<MenuItem>>();

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
