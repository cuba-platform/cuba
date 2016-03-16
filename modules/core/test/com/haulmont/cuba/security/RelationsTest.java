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
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

public class RelationsTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testRole() {
        UUID roleId = createRole();

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Role role = em.find(Role.class, roleId);
            em.remove(role);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public UUID createRole() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

            Role role = new Role();
            role.setName("RelationTest");
            em.persist(role);

            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            em.persist(userRole);

            tx.commit();

            return role.getId();
        } finally {
            tx.end();
        }
    }
}
