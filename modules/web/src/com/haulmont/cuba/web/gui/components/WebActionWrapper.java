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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;

/**
 * {@link Action} adapter for web client.
 *
 */
class WebActionWrapper extends com.vaadin.event.Action {

    private final Action action;

    public WebActionWrapper(Action action) {
        super(""); // don't invoke action.getCaption() here as it may not be properly initialized at the moment
        this.action = action;
    }

    @Override
    public String getCaption() {
        StringBuilder sb = new StringBuilder();
        sb.append(action.getCaption());
        if (action.getShortcut() != null) {
            sb.append(" (").append(action.getShortcut().format()).append(")");
        }
        return sb.toString();
    }
}