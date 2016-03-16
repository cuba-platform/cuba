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

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;

/**
 */
public class EntityStateTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID userId;

    @After
    public void tearDown() throws Exception {
        if (userId != null)
            cont.deleteRecord("SEC_USER", userId);
    }

    @Test
    public void testTransactions() throws Exception {
        User user;
        Group group;

        // create and persist

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            user = new User();
            assertTrue(user.__new());
            assertFalse(user.__managed());
            assertFalse(user.__detached());

            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            assertTrue(user.__new());
            assertTrue(user.__managed());
            assertFalse(user.__detached());

            tx.commit();
        } finally {
            tx.end();
        }

        assertFalse(user.__new());
        assertFalse(user.__managed());
        assertTrue(user.__detached());

        // load from DB

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            // find
            user = em.find(User.class, userId);
            assertNotNull(user);

            assertFalse(user.__new());
            assertTrue(user.__managed());
            assertFalse(user.__detached());

            group = user.getGroup();
            assertNotNull(group);

            assertFalse(group.__new());
            assertTrue(group.__managed());
            assertFalse(group.__detached());

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            // query
            Query query = em.createQuery("select u from sec$User u where u.id = ?1").setParameter(1, userId);
            user = (User) query.getFirstResult();
            assertNotNull(user);

            assertFalse(user.__new());
            assertTrue(user.__managed());
            assertFalse(user.__detached());

            group = user.getGroup();
            assertNotNull(group);

            assertFalse(group.__new());
            assertTrue(group.__managed());
            assertFalse(group.__detached());

            tx.commit();
        } finally {
            tx.end();
        }

        assertFalse(user.__new());
        assertFalse(user.__managed());
        assertTrue(user.__detached());

        assertFalse(group.__new());
        assertFalse(group.__managed());
        assertTrue(group.__detached());

        user.setName("changed name");

        // merge changed

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.merge(user);

            assertFalse(user.__new());
            assertTrue(user.__managed());
            assertFalse(user.__detached());

            tx.commit();
        } finally {
            tx.end();
        }

        assertFalse(user.__new());
        assertFalse(user.__managed());
        assertTrue(user.__detached());
    }

    @Test
    public void testSerialization() throws Exception {
        User user;
        Group group;

        // serialize new

        user = new User();
        assertTrue(user.__new());
        assertFalse(user.__managed());
        assertFalse(user.__detached());

        user = reserialize(user);

        assertTrue(user.__new());
        assertFalse(user.__managed());
        assertFalse(user.__detached());

        // serialize managed

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId);
            assertNotNull(user);

            assertFalse(user.__new());
            assertTrue(user.__managed());
            assertFalse(user.__detached());

            group = user.getGroup();
            assertNotNull(group);

            assertFalse(group.__new());
            assertTrue(group.__managed());
            assertFalse(group.__detached());

            user = reserialize(user);

            assertFalse(user.__new());
            assertFalse(user.__managed());
            assertTrue(user.__detached());

            tx.commit();
        } finally {
            tx.end();
        }

        user.setName("changed name");

        // merge changed and serialize

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.merge(user);

            assertFalse(user.__new());
            assertTrue(user.__managed());
            assertFalse(user.__detached());

            tx.commit();
        } finally {
            tx.end();
        }

        assertFalse(user.__new());
        assertFalse(user.__managed());
        assertTrue(user.__detached());

        user = reserialize(user);

        assertFalse(user.__new());
        assertFalse(user.__managed());
        assertTrue(user.__detached());
    }
}
