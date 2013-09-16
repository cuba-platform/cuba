/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

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
    }

    @Override
    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId);
        deleteRecord("SEC_USER", user2Id);
        deleteRecord("SEC_GROUP", groupId);
        super.tearDown();
    }

    public void testViewPropagation() throws Exception {

        View view = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class)
                        .addProperty("name"));

        User user;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.setView(view);

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id = ?1", User.class);
            query.setParameter(1, userId);
            user = query.getSingleResult();

            tx.commit();
        } finally {
            tx.end();
        }

        assertNull(user.getCreatedBy());
        assertNotNull(user.getGroup());
    }

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
        assertNotNull(user);
        assertNull(user.getCreatedBy());
        assertNotNull(user.getGroup());
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
            em.setView(view1);
            user1 = em.find(User.class, userId);
            user2 = em.find(User.class, user2Id, view2);

            tx.commit();
        } finally {
            tx.end();
        }
        assertNotNull(user1);
        assertNull(user1.getCreatedBy());
        assertNull(user1.getGroup());
        assertNotNull(user2);
        assertNull(user2.getCreatedBy());
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
        assertNotNull(user1);
        assertNull(user1.getCreatedBy());
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

        assertNull(user.getCreatedBy());
        assertNotNull(user.getGroup());
    }

    public void testReload() throws Exception {
        final User originalUser = persistence.createTransaction().execute(new Transaction.Callable<User>() {
            @Override
            public User call(EntityManager em) {
                return em.find(User.class, userId, "user.browse");
            }
        });

        assertNotNull(originalUser);
        assertNull(originalUser.getUserRoles());

        User reloadedUser = persistence.createTransaction().execute(new Transaction.Callable<User>() {
            @Override
            public User call(EntityManager em) {
                return em.reload(originalUser, "user.edit");
            }
        });
        assertNotNull(reloadedUser);
        assertNotNull(reloadedUser.getUserRoles());
        assertTrue(originalUser != reloadedUser);
    }

    public void testReloadSameTx() {
        User originalUser;
        User reloadedUser;

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
        assertNotNull(reloadedUser);
        assertNotNull(reloadedUser.getLogin());
        assertNotNull(reloadedUser.getUserRoles());
        assertTrue(originalUser == reloadedUser);
    }
}
