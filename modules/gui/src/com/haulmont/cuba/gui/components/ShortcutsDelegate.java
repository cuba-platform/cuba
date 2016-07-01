/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles active shortcuts for actions holder (Table or Tree)
 *
 * @param <T> type of shortcut descriptor
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

    public void addAction(@Nullable Action oldAction, Action newAction) {
        KeyCombination newShortcut = newAction.getShortcutCombination();
        if (newShortcut != null) {
            if (oldAction != null) {
                KeyCombination oldShortcut = oldAction.getShortcutCombination();
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
                KeyCombination oldShortcut = oldAction.getShortcutCombination();
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
            if (action.getShortcutCombination() != null) {
                addAlternativeShortcut(action.getShortcutCombination());
            }
        }
    }

    private void addAlternativeShortcut(KeyCombination kc) {
        Action alternativeAction = null;
        for (Action action : getActions()) {
            // find last action with same shortcut
            if (kc.equals(action.getShortcutCombination())) {
                alternativeAction = action;
            }
        }
        if (alternativeAction != null) {
            addShortcut(alternativeAction.getId(), alternativeAction.getShortcutCombination());
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
            if (keyCombination.equals(oldAction.getShortcutCombination())) {
                removeShortcut(oldAction);
            }
        }

        shortcuts.put(actionId, attachShortcut(actionId, keyCombination));
    }

    protected abstract T attachShortcut(final String actionId, KeyCombination keyCombination);

    protected abstract void detachShortcut(Action action, T shortcutDescriptor);

    protected abstract Collection<Action> getActions();
}