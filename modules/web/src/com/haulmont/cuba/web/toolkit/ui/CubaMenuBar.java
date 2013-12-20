/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.KeyCombination;
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
    protected final Map<MenuItem, String> testIds = new HashMap<>();

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

    public void setTestId(MenuItem item, String id) {
        testIds.put(item, id);
    }

    @Override
    protected void paintAdditionalItemParams(PaintTarget target, MenuItem item) throws PaintException {
        if (shortcuts.containsKey(item)) {
            String shortcut = shortcuts.get(item);
            if (shortcut != null) {
                target.addAttribute("shortcut", shortcut);
            }
        }
        if (testIds.containsKey(item)) {
            target.addAttribute("testId", testIds.get(item));
        }
    }
}