/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;


import java.util.function.Consumer;

/**
 * The ShortcutAction is triggered when the user presses a given key combination.
 */
public class ShortcutAction {
    protected final KeyCombination shortcut;
    protected final Consumer<ShortcutTriggeredEvent> handler;

    public ShortcutAction(String shortcut, Consumer<ShortcutTriggeredEvent> handler) {
        this(KeyCombination.create(shortcut), handler);
    }

    public ShortcutAction(KeyCombination shortcut, Consumer<ShortcutTriggeredEvent> handler) {
        this.shortcut = shortcut;
        this.handler = handler;
    }

    /**
     * @return the key combination that the shortcut reacts to
     */
    public KeyCombination getShortcutCombination() {
        return shortcut;
    }

    /**
     * @return the handler invoked when the shortcut is triggered
     */
    public Consumer<ShortcutTriggeredEvent> getHandler() {
        return handler;
    }
}