/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityStateTest extends CubaTestCase {

    private UUID userId;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        if (userId != null)
            deleteRecord("SEC_USER", userId);
        super.tearDown();
    }

    public void testTransactions() throws Exception {
        User user;
        Group group;

        // create and persist

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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
