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

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.ListComponent;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ListAction extends BaseAction implements Action.HasTarget {

    protected ListComponent target;

    public ListAction(String id) {
        super(id);
    }

    public ListAction(String id, @Nullable String shortcut) {
        super(id, shortcut);
    }

    @Override
    public ListComponent getTarget() {
        return target;
    }

    @Override
    public void setTarget(ListComponent target) {
        this.target = target;
    }

    /**
     * Set caption using fluent API method.
     *
     * @param caption caption
     * @return current instance of action
     */
    public ListAction withCaption(String caption) {
        this.caption = caption;
        return this;
    }

    /**
     * Set description using fluent API method.
     *
     * @param description description
     * @return current instance of action
     */
    public ListAction withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set icon using fluent API method.
     *
     * @param icon icon
     * @return current instance of action
     */
    public ListAction withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Set shortcut using fluent API method.
     *
     * @param shortcut shortcut
     * @return current instance of action
     */
    public ListAction withShortcut(String shortcut) {
        if (shortcut != null) {
            this.shortcut = KeyCombination.create(shortcut);
        }
        return this;
    }

    /**
     * Set action performed event handler using fluent API method. Can be used instead of subclassing BaseAction class.
     *
     * @param handler action performed handler
     * @return current instance of action
     */
    public ListAction withHandler(Consumer<ActionPerformedEvent> handler) {
        getEventHub().subscribe(ActionPerformedEvent.class, handler);

        return this;
    }

    /**
     * Set whether this action is primary using fluent API method. Can be used instead of subclassing BaseAction class.
     *
     * @param primary primary
     * @return current instance of action
     */
    public ListAction withPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }
}