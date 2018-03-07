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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.cuba.core.sys.serialization.Serialization;
import com.haulmont.cuba.core.sys.serialization.StandardSerialization;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 *
 */
public class ObservableListTest {

    @Test
    public void testSerialization() throws Exception {
        Serialization serialization = new StandardSerialization();
        
        User user1 = new User();
        user1.setLogin("u1");
        user1.setName("User 1");
        user1.setUserRoles(new ObservableList<>(new ArrayList<>(), () -> {}));

        Role role1 = new Role();
        role1.setName("Role 1");

        UserRole user1Role1 = new UserRole();
        user1Role1.setUser(user1);
        user1Role1.setRole(role1);

        user1.getUserRoles().add(user1Role1);

        User deserializedUser = (User) serialization.deserialize(serialization.serialize(user1));
        assertTrue(deserializedUser.getUserRoles() instanceof ArrayList);
    }
}