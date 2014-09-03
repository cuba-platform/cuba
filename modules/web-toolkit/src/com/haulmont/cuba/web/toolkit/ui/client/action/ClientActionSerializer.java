/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui.client.action;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.communication.JSONSerializer;
import com.vaadin.client.metadata.Type;

/**
 * @author artamonov
 * @version $Id$
 */
public class ClientActionSerializer implements JSONSerializer<ClientAction> {
    @Override
    public ClientAction deserialize(Type type, JSONValue jsonValue, ApplicationConnection connection) {
        ClientAction clientAction = new ClientAction();
        JSONObject clientActionObject = jsonValue.isObject();
        clientAction.setCaption(clientActionObject.get("caption").isString().stringValue());
        clientAction.setActionId(clientActionObject.get("actionId").isString().stringValue());
        return clientAction;
    }

    @Override
    public JSONValue serialize(ClientAction value, ApplicationConnection connection) {
        // serialization to json not needed for this type
        return new JSONObject();
    }
}