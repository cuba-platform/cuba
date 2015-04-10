/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui.client.action;

import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.communication.JSONSerializer;
import com.vaadin.client.metadata.Type;
import elemental.json.*;
import elemental.json.JsonValue;

/**
 * @author artamonov
 * @version $Id$
 */
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