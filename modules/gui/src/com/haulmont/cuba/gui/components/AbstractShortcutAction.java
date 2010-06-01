/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 31.05.2010 14:37:42
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public abstract class AbstractShortcutAction extends AbstractAction implements ShortcutAction {

    private KeyCombination combination;

    protected AbstractShortcutAction(String id, KeyCombination keyCombination) {
        super(id);
        combination = keyCombination;
    }

    public KeyCombination getKeyCombination() {
        return combination;
    }
}
