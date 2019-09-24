/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.security.entity.User;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EntitySetTest {

    @Test
    public void test() {
        Server server1 = new Server(); Server server2 = new Server();
        Set<Server> set = new HashSet<>();
        set.add(server1); set.add(server2);

        EntitySet entitySet = EntitySet.of(set);

        assertSame(server1, entitySet.optional(Server.class, server1.getId()).orElse(null));
        assertFalse(entitySet.optional(Server.class, UUID.randomUUID()).isPresent());
        assertFalse(entitySet.optional(User.class, server1.getId()).isPresent());

        assertSame(server1, entitySet.get(Server.class, server1.getId()));

        try {
            entitySet.get(Server.class, UUID.randomUUID());
            fail();
        } catch (IllegalArgumentException e) {
            // ok
        }

        assertSame(server1, entitySet.optional(server1).orElse(null));
        assertSame(server1, entitySet.get(server1));
    }

    @Test
    public void testOfCollection() {
        Server server1 = new Server(); Server server2 = new Server();
        List<Server> list = Arrays.asList(server1, server2);

        EntitySet entitySet = EntitySet.of(list);
        assertSame(server1, entitySet.optional(Server.class, server1.getId()).orElse(null));
    }
}