/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityManagerTest extends CubaTestCase {

    private UUID userId;
    private UUID user2Id;
    private UUID groupId;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        userId = UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("login" + userId);
            user.setPassword("000");
            user.setGroup(group);
            em.persist(user);

            group = new Group();
            groupId = group.getId();
            group.setName("testGroup1");
            em.persist(group);

            user = new User();
            user2Id = user.getId();
            user.setName("testUser2");
            user.setLogin("login" + user2Id);
            user.setPassword("222");
            user.setGroup(group);
            em.persist(user);

            tx.commit();
        } finally {
            tx.end();
        }

        metadata.getViewRepository().getView(User.class, View.MINIMAL);
    }

    @Override
    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId);
        deleteRecord("SEC_USER", user2Id);
        deleteRecord("SEC_GROUP", groupId);
        super.tearDown();
    }

    // Commented in EL: EntityManager has no setView() method anymore
//    public void testViewPropagation() throws Exception {
//
//        View view = new View(User.class, false)
//                .addProperty("name")
//                .addProperty("login")
//                .addProperty("group", new View(Group.class)
//                        .addProperty("name"));
//
//        User user;
//
//        Transaction tx = persistence.createTransaction();
//        try {
//            EntityManager em = persistence.getEntityManager();
//            em.setView(view);
//
//            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id = ?1", User.class);
//            query.setParameter(1, userId);
//            user = query.getSingleResult();
//
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//
//        assertNull(user.getCreatedBy());
//        assertNotNull(user.getGroup());
//    }

    public void testFind() throws Exception {

        View view = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class)
                        .addProperty("name"));

        User user;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user = em.find(User.class, userId, view);

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);

        assertNotNull(user);
        try {
            user.getCreatedBy();
            fail();
        } catch (Exception ignored) {
        }
        assertNotNull(user.getGroup());
    }

    public void testMerge() throws Exception {
        UUID newUserId = UUID.randomUUID();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            User user = new User();
            user.setId(newUserId);
            user.setName("testMerge");
            user.setLogin("testMerge");
            user.setPassword("testMerge");
            user.setGroup(em.getReference(Group.class, groupId));
            user = em.merge(user);
            User userFromPersistentContext = em.find(User.class, newUserId);
            assertEquals(user, userFromPersistentContext);
            tx.commit();
        } finally {
            tx.end();
            deleteRecord("SEC_USER", newUserId);
        }
    }

    public void testFindSeparateViews() throws Exception {

        View view1 = new View(User.class, false)
                .addProperty("name")
                .addProperty("login");

        View view2 = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class)
                        .addProperty("name"));

        User user1, user2;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user1 = em.find(User.class, userId, view1);
            user2 = em.find(User.class, user2Id, view2);

            tx.commit();
        } finally {
            tx.end();
        }
        user1 = reserialize(user1);
        user2 = reserialize(user2);

        assertNotNull(user1);
        try {
            user1.getCreatedBy();
            fail();
        } catch (Exception ignored) {
        }
        try {
            user1.getGroup();
            fail();
        } catch (Exception ignored) {
        }
        assertNotNull(user2);

        try {
            user2.getCreatedBy();
            fail();
        } catch (Exception ignored) {
        }
        try {
            user2.getCreatedBy();
            fail();
        } catch (Exception ignored) {
        }
        assertNotNull(user2.getGroup());
    }

    public void testFindCombinedView() throws Exception {

        View view1 = new View(User.class, false)
                .addProperty("name")
                .addProperty("login");

        View view2 = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class)
                        .addProperty("name"));

        User user1;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user1 = em.find(User.class, userId, view1, view2);

            tx.commit();
        } finally {
            tx.end();
        }
        user1 = reserialize(user1);

        assertNotNull(user1);
        try {
            user1.getCreatedBy();
            fail();
        } catch (Exception ignored) {
        }
        assertNotNull(user1.getGroup());
    }

    public void testQueryView() throws Exception {
        View view = new View(User.class, "testQueryView", false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class, false)
                        .addProperty("name"));

        ((AbstractViewRepository) metadata.getViewRepository()).storeView(
                metadata.getSession().getClassNN(User.class), view);

        User user;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id = ?1", User.class);
            query.setParameter(1, userId);
            query.setViewName("testQueryView");
            user = query.getSingleResult();

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);

        try {
            user.getCreatedBy();
            fail();
        } catch (Exception ignored) {
        }
        assertNotNull(user.getGroup());
    }

    public void testReload() throws Exception {
        User originalUser, reloadedUser;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            originalUser = em.find(User.class, userId, "user.browse");
            tx.commit();
        } finally {
            tx.end();
        }
        assertNotNull(originalUser);
        assertFalse(PersistenceHelper.isLoaded(originalUser, "userRoles"));

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            reloadedUser = em.reload(originalUser, "user.edit");
            tx.commit();
        } finally {
            tx.end();
        }
        assertNotNull(reloadedUser);
        assertTrue(PersistenceHelper.isLoaded(reloadedUser, "userRoles"));
        assertTrue(originalUser != reloadedUser);
    }

    public void testReloadSameTx() throws Exception {
        User originalUser, reloadedUser;

        View view = new View(User.class, false)
                .addProperty("name");

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            originalUser = em.find(User.class, userId, view);
            assertNotNull(originalUser);
            reloadedUser = em.reload(originalUser, "user.edit");

            tx.commit();
        } finally {
            tx.end();
        }
        assertTrue(originalUser == reloadedUser);
        reloadedUser = reserialize(reloadedUser);
        assertNotNull(reloadedUser);
        assertNotNull(reloadedUser.getLogin());
        assertTrue(PersistenceHelper.isLoaded(reloadedUser, "userRoles"));
    }
}
