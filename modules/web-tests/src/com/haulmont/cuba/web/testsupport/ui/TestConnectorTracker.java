/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.testsupport.ui;

import com.vaadin.server.ClientConnector;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.UI;
import elemental.json.JsonObject;

public class TestConnectorTracker extends ConnectorTracker {
    public TestConnectorTracker(UI uI) {
        super(uI);
    }

    @Override
    public boolean isWritingResponse() {
        return false;
    }

    @Override
    public void registerConnector(ClientConnector connector) {
        // do nothing
    }

    @Override
    public void unregisterConnector(ClientConnector connector) {
        // do nothing
    }

    @Override
    public JsonObject getDiffState(ClientConnector connector) {
        return null;
    }
}