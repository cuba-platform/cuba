/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit;

import com.haulmont.cuba.gui.components.KeyCombination;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.MenuBar;

/**
 * @author budarov
 * @version $Id$
 */
public class MenuShortcutAction extends ShortcutListener {

    private static final long serialVersionUID = -5416777300893219886L;

    private MenuBar.MenuItem menuItem;

    public MenuShortcutAction(MenuBar.MenuItem menuItem, String caption, int kc, int... m) {
        super(caption, kc, m);
        this.menuItem = menuItem;
    }

    public MenuShortcutAction(MenuBar.MenuItem menuItem, String caption, KeyCombination key) {
        this(menuItem, caption, key.getKey().getCode(), getShortcutModifiers(key.getModifiers()));
    }

    @Override
    public void handleAction(Object sender, Object target) {
        menuItem.getCommand().menuSelected(menuItem);
    }

    public static int[] getShortcutModifiers(KeyCombination.Modifier[] modifiers) {
        if (modifiers == null) {
            return null;
        }
        int[] res = new int[modifiers.length];
        for (int i = 0; i < modifiers.length; i++) {
            res[i] = modifiers[i].getCode();
        }
        return res;
    }
}