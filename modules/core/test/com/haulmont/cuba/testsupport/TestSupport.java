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

package com.haulmont.cuba.testsupport;

import org.junit.Assert;

import java.io.Serializable;

import static com.haulmont.cuba.core.sys.serialization.SerializationSupport.deserialize;
import static com.haulmont.cuba.core.sys.serialization.SerializationSupport.serialize;
import java.util.UUID;

/**
 */
public class TestSupport {

    public static final UUID ADMIN_USER_ID = UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

    public static final UUID COMPANY_GROUP_ID = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");

    public static final UUID ADMIN_ROLE_ID = UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93");

    public static <T> T reserialize(Serializable object) throws Exception {
        if (object == null)
            return null;

        return (T) deserialize(serialize(object));
    }

    public static void assertFail(Runnable runnable) {
        try {
            runnable.run();
            Assert.fail();
        } catch (Exception ignored) {
        }
    }
}