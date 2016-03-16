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

package com.haulmont.cuba.web.toolkit;

import com.vaadin.event.ConnectorActionManager;
import com.vaadin.server.ClientConnector;

/**
 * Keeps track of the ShortcutListeners added to component, and manages the painting and handling as well. <br/>
 * Paints actions with ShortcutListener to separate 'shortcuts' json tag.
 *
 */
public class ShortcutActionManager extends ConnectorActionManager {

    public ShortcutActionManager(ClientConnector connector) {
        super(connector);
    }

    @Override
    protected String getActionsJsonTag() {
        return "shortcuts";
    }

    @Override
    protected boolean isNeedToAddActionVariable() {
        return false;
    }
}