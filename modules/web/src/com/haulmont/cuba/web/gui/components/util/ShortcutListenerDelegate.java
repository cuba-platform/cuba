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

package com.haulmont.cuba.web.gui.components.util;

import com.vaadin.event.ShortcutListener;

import javax.annotation.Nullable;

/**
 * Convenient ShortcutListener subclass that accepts lambda handler.
 */
public final class ShortcutListenerDelegate extends ShortcutListener {

    private ShortcutListenerHandler handler;

    public ShortcutListenerDelegate(String caption, int keyCode, @Nullable int[] modifierKeys) {
        super(caption, keyCode, modifierKeys);
    }

    @Override
    public void handleAction(Object sender, Object target) {
        handler.handle(sender, target);
    }

    public ShortcutListenerDelegate withHandler(ShortcutListenerHandler handler) {
        this.handler = handler;
        return this;
    }
}