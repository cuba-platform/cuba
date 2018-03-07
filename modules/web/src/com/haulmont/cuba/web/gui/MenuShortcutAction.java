/*
 * Copyright (c) 2008-2017 Haulmont.
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
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.vaadin.event.ShortcutListener;

public class MenuShortcutAction extends ShortcutListener {

    private static final long serialVersionUID = -5416777300893219886L;

    protected AppMenu.MenuItem menuItem;

    public MenuShortcutAction(AppMenu.MenuItem menuItem, String caption, int kc, int... m) {
        super(caption, kc, m);
        this.menuItem = menuItem;
    }

    public MenuShortcutAction(AppMenu.MenuItem menuItem, String caption, KeyCombination key) {
        this(menuItem, caption, key.getKey().getCode(), KeyCombination.getShortcutModifiers(key.getModifiers()));
    }

    @Override
    public void handleAction(Object sender, Object target) {
        menuItem.getCommand().accept(menuItem);
    }
}