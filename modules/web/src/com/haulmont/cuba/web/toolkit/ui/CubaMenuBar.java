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

    protected Map<MenuItem, String> shortcuts = null;
    protected Map<MenuItem, String> testIds = null;
    protected Map<MenuItem, String> cubaIds = null;

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
        if (shortcuts == null) {
            shortcuts = new HashMap<>();
        }

        if (shortcuts.containsKey(item)) {
            shortcuts.remove(item);
        }
        shortcuts.put(item, str);
    }

    public void clearShortcut(MenuItem item) {
        if (shortcuts != null) {
            shortcuts.remove(item);
        }
    }

    public void setTestId(MenuItem item, String id) {
        if (testIds == null) {
            testIds = new HashMap<>();
        }
        testIds.put(item, id);
    }

    public void setCubaId(MenuItem item, String id) {
        if (cubaIds == null) {
            cubaIds = new HashMap<>();
        }
        cubaIds.put(item, id);
    }

    @Override
    protected void paintAdditionalItemParams(PaintTarget target, MenuItem item) throws PaintException {
        if (shortcuts != null && shortcuts.containsKey(item)) {
            String shortcut = shortcuts.get(item);
            if (shortcut != null) {
                target.addAttribute("shortcut", shortcut);
            }
        }
        if (testIds != null && testIds.containsKey(item)) {
            String testIdValue = testIds.get(item);
            if (testIdValue != null) {
                target.addAttribute("tid", testIdValue);
            }
        }
        if (cubaIds != null && cubaIds.containsKey(item)) {
            String idValue = cubaIds.get(item);
            if (idValue != null) {
                target.addAttribute("cid", idValue);
            }
        }
    }
}