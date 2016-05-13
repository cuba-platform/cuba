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
 */

package com.haulmont.cuba.web.toolkit.ui.client.profiler;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.communication.JSONSerializer;
import com.vaadin.client.metadata.Type;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

@SuppressWarnings("UnusedDeclaration")
public class ScreenProfilerClientEventSerializer implements JSONSerializer<ScreenProfilerClientEvent> {

    @Override
    public ScreenProfilerClientEvent deserialize(Type type, JsonValue jsonValue, ApplicationConnection connection) {
        JsonObject profilerEventObject = (JsonObject) jsonValue;
        ScreenProfilerClientEvent profilerEvent = new ScreenProfilerClientEvent();
        profilerEvent.setProfilerMarker(profilerEventObject.getString("profilerMarker"));
        profilerEvent.setClientTime((int) profilerEventObject.getNumber("clientTime"));
        profilerEvent.setServerTime((int) profilerEventObject.getNumber("serverTime"));
        profilerEvent.setNetworkTime((int) profilerEventObject.getNumber("networkTime"));
        profilerEvent.setEventTs((long) profilerEventObject.getNumber("eventTs"));
        return profilerEvent;
    }

    @Override
    public JsonValue serialize(ScreenProfilerClientEvent value, ApplicationConnection connection) {
        JsonObject profilerEventObject = Json.createObject();
        profilerEventObject.put("profilerMarker", value.getProfilerMarker());
        profilerEventObject.put("clientTime", value.getClientTime());
        profilerEventObject.put("serverTime", value.getServerTime());
        profilerEventObject.put("networkTime", value.getNetworkTime());
        profilerEventObject.put("eventTs", value.getEventTs());
        return profilerEventObject;
    }
}
