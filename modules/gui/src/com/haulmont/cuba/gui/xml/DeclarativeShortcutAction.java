/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ShortcutAction;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DeclarativeShortcutAction extends DeclarativeAction implements ShortcutAction {

    private KeyCombination combination;

    public DeclarativeShortcutAction(String id, String caption, String icon, String methodName,
                                     String shortcut, Component.ActionsHolder holder)
    {
        super(id, caption, icon, methodName, holder);
        combination = ShortcutAction.KeyCombination.create(shortcut);
    }

    @Override
    public KeyCombination getKeyCombination() {
        return combination;
    }
}
