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
package com.haulmont.cuba.core;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("IncorrectCreateEntity")
public class NamePatternTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void test() {
        Server server = new Server();
        server.setName("orion");
        server.setRunning(false);

        String instanceName = server.getInstanceName();

        assertEquals(InstanceUtils.getInstanceName(server), instanceName);
        assertEquals("orion", instanceName);
    }

    @Test
    public void roleNamePattern() {
        Role role = new Role();
        role.setLocName("System Role");
        role.setName("system_role");

        String instanceName = cont.metadata().getTools().getInstanceName(role);
        assertEquals("System Role [system_role]", instanceName);
        assertEquals("System Role [system_role]", InstanceUtils.getInstanceName(role));
    }

    @Test
    public void userNamePattern() {
        User user = new User();
        user.setName("System Administrator");
        user.setLogin("systemAdmin");

        String instanceName = cont.metadata().getTools().getInstanceName(user);
        assertEquals("System Administrator [systemAdmin]", instanceName);
        assertEquals("System Administrator [systemAdmin]", InstanceUtils.getInstanceName(user));
    }
}