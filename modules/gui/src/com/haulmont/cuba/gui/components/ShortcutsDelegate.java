/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles active shortcuts for actions holder (Table or Tree)
 *
 * @param <T> type of shortcut descriptor
 *
 * @author artamonov
 * @version $Id$
 */
public abstract class ShortcutsDelegate<T> {

    private Map<String, T> shortcuts = new HashMap<>();

    private boolean allowEnterShortcut = true;

    public boolean isAllowEnterShortcut() {
        return allowEnterShortcut;
    }

    public void setAllowEnterShortcut(boolean allowEnterShortcut) {
        this.allowEnterShortcut = allowEnterShortcut;
    }

    public void addAction(Action oldAction, Action newAction) {
        KeyCombination newShortcut = newAction.getShortcut();
        if (newShortcut != null) {
            if (oldAction != null) {
                KeyCombination oldShortcut = oldAction.getShortcut();
                if (newShortcut.equals(oldShortcut)) {
                    removeShortcut(oldAction);
                } else if (oldShortcut != null) {
                    removeShortcut(oldAction);
                    // find and assign alternative
                    addAlternativeShortcut(oldShortcut);
                }
            }

            addShortcut(newAction.getId(), newShortcut);
        } else {
            if (oldAction != null) {
                KeyCombination oldShortcut = oldAction.getShortcut();
                if (oldShortcut != null) {
                    removeShortcut(oldAction);
                    // find and assign alternative
                    addAlternativeShortcut(oldShortcut);
                }
            }
        }
    }

    public void removeAction(Action action) {
        if (action != null) {
            removeShortcut(action);
            // find and assign alternative
            if (action.getShortcut() != null) {
                addAlternativeShortcut(action.getShortcut());
            }
        }
    }

    private void addAlternativeShortcut(KeyCombination kc) {
        Action alternativeAction = null;
        for (Action action : getActions()) {
            // find last action with same shortcut
            if (kc.equals(action.getShortcut())) {
                alternativeAction = action;
            }
        }
        if (alternativeAction != null) {
            addShortcut(alternativeAction.getId(), alternativeAction.getShortcut());
        }
    }

    private void removeShortcut(Action action) {
        String actionId = action.getId();
        if (shortcuts.containsKey(actionId)) {
            detachShortcut(action, shortcuts.remove(actionId));
        }
    }

    private void addShortcut(final String actionId, KeyCombination keyCombination) {
        if (!allowEnterShortcut
                && (keyCombination.getModifiers() == null || keyCombination.getModifiers().length == 0)
                && keyCombination.getKey() == KeyCombination.Key.ENTER) {
            return;
        }

        for (Action oldAction : getActions()) {
            if (keyCombination.equals(oldAction.getShortcut())) {
                removeShortcut(oldAction);
            }
        }

        shortcuts.put(actionId, attachShortcut(actionId, keyCombination));
    }

    protected abstract T attachShortcut(final String actionId, KeyCombination keyCombination);

    protected abstract void detachShortcut(Action action, T shortcutDescriptor);

    protected abstract Collection<Action> getActions();
}