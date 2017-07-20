/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.idp;

import com.google.gson.Gson;
import com.haulmont.cuba.security.global.IdpSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class IdpSessionJsonTest {
    @Test
    public void idpSessionToJson() {
        IdpSession session = new IdpSession(UUID.randomUUID().toString().replace("-", ""));

        session.setAttributes(new HashMap<>());
        session.getAttributes().put("demo1", 1);
        session.getAttributes().put("demo2", "test");
        session.getAttributes().put("demo3", 2.2);

        String json = new Gson().toJson(session);
        assertNotNull(json);
    }

    @Test
    public void idpSessionFromJson() {
        String sessionJson = "{\"id\":\"ba8693d910404111b3eac8636192d1ff\"," +
                              "\"attributes\":{\"demo3\":2.2,\"demo1\":1,\"demo2\":\"test\"}}";

        IdpSession session = new Gson().fromJson(sessionJson, IdpSession.class);
        assertNotNull(session);
    }
}