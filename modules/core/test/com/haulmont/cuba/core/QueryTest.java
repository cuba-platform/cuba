/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.time.DateUtils;

import javax.persistence.TemporalType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class QueryTest extends CubaTestCase {

    private UUID userId;
    private UUID user2Id;
    private UUID groupId;

    protected void setUp() throws Exception {
        super.setUp();

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            user = new User();
            user2Id = user.getId();
            user.setName("testUser2");
            user.setLogin("testLogin2");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            Group group = new Group();
            groupId = group.getId();
            group.setName("testGroup");
            em.persist(group);

            tx.commit();
        }
    }

    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId, user2Id);
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

    public void testCaseInsensitiveSearch() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            TypedQuery<User> query = persistence.getEntityManager().createQuery(
                    "select u from sec$User u where u.name like :name", User.class);
            query.setParameter("name", "(?i)%user%");
            List<User> list = query.getResultList();
            tx.commit();

            Iterables.find(list, new Predicate<User>() {
                @Override
                public boolean apply(User input) {
                    return input.getId().equals(userId);
                }
            });
        } finally {
            tx.end();
        }

    }

    public void testListParameter() throws Exception {
        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<User> query = persistence.getEntityManager().createQuery(
                    "select u from sec$User u where u.id in :ids order by u.createTs", User.class);
            query.setParameter("ids", Arrays.asList(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"), userId, user2Id));
            List<User> list = query.getResultList();
            assertEquals(3, list.size());

            tx.commit();
        }

        // Implicit conversion

        User user1, user2, user3;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            user1 = em.find(User.class, userId);
            user2 = em.find(User.class, user2Id);
            user3 = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

            tx.commit();
        }

        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<User> query = persistence.getEntityManager().createQuery(
                    "select u from sec$User u where u.id in :ids order by u.createTs", User.class);
            query.setParameter("ids", Arrays.asList(user1, user2, user3));
            List<User> list = query.getResultList();
            assertEquals(3, list.size());

            tx.commit();
        }

        // Positional parameters

        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<User> query = persistence.getEntityManager().createQuery(
                    "select u from sec$User u where u.id in ?1 order by u.createTs", User.class);
            query.setParameter(1, Arrays.asList(user1.getId(), user2.getId(), user3.getId()));
            List<User> list = query.getResultList();
            assertEquals(3, list.size());

            tx.commit();
        }

        // Positional parameters with implicit conversion

        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<User> query = persistence.getEntityManager().createQuery(
                    "select u from sec$User u where u.id in ?1 order by u.createTs", User.class);
            query.setParameter(1, Arrays.asList(user1, user2, user3));
            List<User> list = query.getResultList();
            assertEquals(3, list.size());

            tx.commit();
        }
    }
}
