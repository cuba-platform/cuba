/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 09.06.2010 10:46:32
 * $Id$
 */
package com.haulmont.cuba.web.toolkit;

import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.MenuBar;

public class MenuShortcutAction extends ShortcutListener {
    private static final long serialVersionUID = -5416777300893219886L;

    private MenuBar.MenuItem menuItem;

    public MenuShortcutAction(MenuBar.MenuItem menuItem, String caption, int kc, int... m) {
        super(caption, kc, m);
        this.menuItem = menuItem;
    }

    public MenuShortcutAction(MenuBar.MenuItem menuItem, String caption, com.haulmont.cuba.gui.components.ShortcutAction.KeyCombination key) {
        this(menuItem, caption, key.getKey().getCode(), getShortcutModifiers(key.getModifiers()));
    }

    @Override
    public void handleAction(Object sender, Object target) {
        menuItem.getCommand().menuSelected(menuItem);
    }

    public static int[] getShortcutModifiers(com.haulmont.cuba.gui.components.ShortcutAction.Modifier[] modifiers) {
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
