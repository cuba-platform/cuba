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

import java.util.EventObject;

/**
 * Describes shortcut triggered event.
 * The event contains a data about source component and target component.
 */
public class ShortcutTriggeredEvent extends EventObject {
    private final Component target;

    /**
     * Constructs a shortcut triggered event.
     *
     * @param source the component on which the Event initially occurred
     * @param target the component which was focused when the Event occurred
     * @throws IllegalArgumentException if source is null
     */
    public ShortcutTriggeredEvent(Component source, Component target) {
        super(source);
        this.target = target;
    }

    @Override
    public Component getSource() {
        return (Component) super.getSource();
    }

    /**
     * @return the component which was focused when the Event occurred
     */
    public Component getTarget() {
        return target;
    }
}