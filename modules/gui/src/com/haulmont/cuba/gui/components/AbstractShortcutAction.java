/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * <p>$Id$</p>
 *
 * @author Nikolay Gorodnov
 */
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
