/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.web.toolkit.ui.client.menubar.CubaMenuBarState;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;

import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMenuBar extends com.vaadin.ui.MenuBar {

    protected final Map<MenuItem, String> shortcuts = new HashMap<>();

    @Override
    protected CubaMenuBarState getState() {
        return (CubaMenuBarState) super.getState();
    }

    @Override
    protected CubaMenuBarState getState(boolean markAsDirty) {
        return (CubaMenuBarState) super.getState(markAsDirty);
    }

    public boolean isVertical() {
        return getState(false).vertical;
    }

    public void setVertical(boolean useMoreMenuItem) {
        if (useMoreMenuItem != isVertical()) {
            getState().vertical = useMoreMenuItem;
        }
    }

    public void setShortcut(MenuItem item, ShortcutAction.KeyCombination shortcut) {
        setShortcut(item, makeCaption(shortcut));
    }

    private String makeCaption(ShortcutAction.KeyCombination shortcut) {
        Messages messages = AppBeans.get(Messages.class);

        StringBuilder sb = new StringBuilder();
        if (shortcut.getModifiers() != null) {
            for (ShortcutAction.Modifier mod : shortcut.getModifiers()) {
                sb.append(messages.getMessage(getClass(), "shortcut." + mod.name()))
                        .append("+");
            }
        }
        sb.append(messages.getMessage(getClass(), "shortcut." + shortcut.getKey().name()));
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

    @Override
    protected void paintAdditionalItemParams(PaintTarget target, MenuItem item) throws PaintException {
        if (shortcuts.containsKey(item)) {
            String shortcut = shortcuts.get(item);
            if (shortcut != null) {
                target.addAttribute("shortcut", shortcut);
            }
        }
    }
}