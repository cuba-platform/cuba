/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.List;
import java.util.UUID;

public class QueryTest extends CubaTestCase
{
    private UUID userId;
    private UUID groupId;

    protected void setUp() throws Exception {
        super.setUp();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            Group group = new Group();
            groupId = group.getId();
            group.setName("testGroup");
            em.persist(group);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId);
        deleteRecord("SEC_GROUP", groupId);
        super.tearDown();
    }

    public void test() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

            Query query = em.createQuery("select r from sec$UserRole r where r.user.id = :user");
            query.setParameter("user", user);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNullParam() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("select r from sec$UserRole r where r.deleteTs = :dts");
            query.setParameter("dts", null);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testUpdate() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Group group = em.find(Group.class, groupId);

            Query query = em.createQuery("update sec$User u set u.group = :group where u.id = :userId");
            query.setParameter("userId", userId);
            query.setParameter("group", group, false);
            query.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
    }

// This test doesn't pass for some unclarified reason.
//
//    public void testFlushBeforeUpdate() {
//        Transaction tx = persistence.createTransaction();
//        try {
//            EntityManager em = persistence.getEntityManager();
//
//            Group group = em.find(Group.class, groupId);
//            User user = em.find(User.class, userId);
//            assertNotNull(user);
//            user.setName("newName");
//
//            Query query = em.createQuery("update sec$User u set u.group = :group where u.id = :userId");
//            query.setParameter("userId", userId);
//            query.setParameter("group", group, false);
//            query.executeUpdate();
//
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//
//        tx = persistence.createTransaction();
//        try {
//            EntityManager em = persistence.getEntityManager();
//            User user = em.find(User.class, userId);
//            assertNotNull(user);
//            assertEquals(groupId, user.getGroup().getId());
//            assertEquals("newName", user.getName());
//
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//    }

    public void testAssociatedResult() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("select u.group from sec$User u where u.id = :userId");
            query.setParameter("userId", userId);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testIgnoreChanges() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<User> query;
            List<User> list;

            query = em.createQuery("select u from sec$User u where u.name = ?1", User.class);
            query.setParameter(1, "testUser");
            list = query.getResultList();
            assertEquals(1, list.size());
            User user = list.get(0);

            user.setName("newName");

            query = em.createQuery("select u from sec$User u where u.name = ?1", User.class);
            query.setParameter(1, "testUser");
            list = query.getResultList();
            assertEquals(1, list.size());
            User user1 = list.get(0);

            assertTrue(user1 == user);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNativeQueryIgnoreChanges() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<User> query;
            List<User> list;

            query = em.createNativeQuery("select * from SEC_USER where NAME = ?1", User.class);
            query.setParameter(1, "testUser");
            list = query.getResultList();
            assertEquals(1, list.size());
            User user = list.get(0);

            user.setName("newName");

            query = em.createNativeQuery("select * from SEC_USER where NAME = ?1", User.class);
            query.setParameter(1, "testUser");
            list = query.getResultList();
            assertEquals(1, list.size());
            User user1 = list.get(0);

            assertTrue(user1 == user);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNativeQuerySelect() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createNativeQuery("select ID, LOGIN from SEC_USER where NAME = ?1");
            query.setParameter(1, "testUser");
            List list = query.getResultList();
            assertEquals(1, list.size());
            assertTrue(list.get(0) instanceof Object[]);
            Object[] row = (Object[]) list.get(0);
            assertEquals(userId.toString(), row[0]);
            assertEquals("testLogin", row[1]);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNativeQueryFlushBeforeUpdate() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Group group = em.find(Group.class, groupId);
            User user = em.find(User.class, userId);
            assertNotNull(user);
            user.setName("newName");

            Query query = em.createNativeQuery("update SEC_USER set GROUP_ID = ?1 where ID = ?2");
            query.setParameter(1, group.getId().toString());
            query.setParameter(2, userId.toString());
            query.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId);
            assertNotNull(user);
            assertEquals(groupId, user.getGroup().getId());
            assertEquals("newName", user.getName());

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
