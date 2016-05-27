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
package com.haulmont.cuba.web.toolkit.ui.client.action;

import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.communication.JSONSerializer;
import com.vaadin.client.metadata.Type;
import elemental.json.*;
import elemental.json.JsonValue;

@SuppressWarnings("UnusedDeclaration")
public class ClientActionSerializer implements JSONSerializer<ClientAction> {

    @Override
    public ClientAction deserialize(Type type, JsonValue jsonValue, ApplicationConnection connection) {
        ClientAction clientAction = new ClientAction();
        JsonObject clientActionObject = (JsonObject) jsonValue;
        clientAction.setCaption(clientActionObject.get("caption").asString());
        clientAction.setActionId(clientActionObject.get("actionId").asString());
        return clientAction;
    }

    @Override
    public JsonValue serialize(ClientAction value, ApplicationConnection connection) {
        // serialization to json not needed for this type
        return Json.createObject();
    }
}