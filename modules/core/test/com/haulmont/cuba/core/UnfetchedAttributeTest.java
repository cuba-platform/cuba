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

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 */
public class UnfetchedAttributeTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;
    
    private Group group;

    @Before
    public void setUp() throws Exception {
        group = new Group();
        group.setName("Some group");
        Transaction tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager().persist(group);
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testGet() throws Exception {
        User user = null;

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setView(
                    new View(User.class, false)
                            .addProperty("login")
                            .addProperty("userRoles", new View(UserRole.class)
                                    .addProperty("role", new View(Role.class)
                                            .addProperty("name")))
            );
            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);

                try {
                    user.getGroup();
                    fail();
                } catch (IllegalStateException ignored) {
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);
        assertNotNull(user);
        assertNotNull(user.getUserRoles());
        user.getUserRoles().size();
    }

    @Test
    public void testSet() throws Exception {
        User user = null;

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setView(
                    new View(User.class, false)
                            .addProperty("login")
                            .addProperty("userRoles", new View(UserRole.class)
                                    .addProperty("role", new View(Role.class)
                                            .addProperty("name")))
            );
            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);
                try {
                    user.setGroup(group);
                    fail();
                } catch (IllegalStateException ignored) {
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);
        assertNotNull(user);
        assertNotNull(user.getUserRoles());
        user.getUserRoles().size();
    }
}
