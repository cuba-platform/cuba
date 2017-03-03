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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;

public class EntityManagerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID userId;
    private UUID user2Id;
    private UUID groupId;

    @Before
    public void setUp() throws Exception {
        userId = UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

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

        cont.metadata().getViewRepository().getView(User.class, View.MINIMAL);
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER", userId);
        cont.deleteRecord("SEC_USER", user2Id);
        cont.deleteRecord("SEC_GROUP", groupId);
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
//        Transaction tx = cont.persistence().createTransaction();
//        try {
//            EntityManager em = cont.persistence().getEntityManager();
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

    @Test
    public void testFind() throws Exception {

        View view = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class)
                        .addProperty("name"));

        User user;

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);

            tx.commit();
        }
        user = reserialize(user);

        assertNotNull(user.getCreatedBy());
        assertNotNull(user.getGroup());
    }

    @Test
    public void testMerge() throws Exception {
        UUID newUserId = UUID.randomUUID();
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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
            cont.deleteRecord("SEC_USER", newUserId);
        }
    }

    @Test
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

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            user1 = em.find(User.class, userId, view1);
            user2 = em.find(User.class, user2Id, view2);

            tx.commit();
        }
        user1 = reserialize(user1);
        user2 = reserialize(user2);

        assertNotNull(user1);
        assertNotNull(user1.getCreatedBy());
        try {
            user1.getGroup();
            fail();
        } catch (Exception ignored) {
        }

        assertNotNull(user2);
        assertNotNull(user2.getCreatedBy());
        assertNotNull(user2.getGroup());
    }

    @Test
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

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            user1 = em.find(User.class, userId, view1, view2);

            tx.commit();
        }
        user1 = reserialize(user1);

        assertNotNull(user1);
        assertNotNull(user1.getCreatedBy());
        assertNotNull(user1.getGroup());
    }

    @Test
    public void testQueryView() throws Exception {
        View view = new View(User.class, "testQueryView", false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class, false)
                        .addProperty("name"));

        ((AbstractViewRepository) cont.metadata().getViewRepository()).storeView(
                cont.metadata().getSession().getClassNN(User.class), view);

        User user;

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();

            TypedQuery<User> query = em.createQuery("select u from sec$User u where u.id = ?1", User.class);
            query.setParameter(1, userId);
            query.setViewName("testQueryView");
            user = query.getSingleResult();

            tx.commit();
        }
        user = reserialize(user);

        assertNotNull(user.getCreatedBy());
        assertNotNull(user.getGroup());
    }

    @Test
    public void testReload() throws Exception {
        User originalUser, reloadedUser;
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            originalUser = em.find(User.class, userId, "user.browse");
            tx.commit();
        }
        assertNotNull(originalUser);
        assertFalse(PersistenceHelper.isLoaded(originalUser, "userRoles"));

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            reloadedUser = em.reload(originalUser, "user.edit");
            tx.commit();
        }
        assertNotNull(reloadedUser);
        assertTrue(PersistenceHelper.isLoaded(reloadedUser, "userRoles"));
        assertTrue(originalUser != reloadedUser);
    }

    @Test
    public void testReloadSameTx() throws Exception {
        User originalUser, reloadedUser;

        View view = new View(User.class, false)
                .addProperty("name");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

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

    @Test
    public void testReloadNN() throws Exception {
        cont.persistence().runInTransaction(em -> {
            User user = em.find(User.class, userId);
            user = em.reloadNN(user, "user.edit");
        });

        cont.persistence().runInTransaction(em -> {
            User user = em.find(User.class, userId);
            em.remove(user);
            try {
                em.reloadNN(user, "user.edit");
                fail();
            } catch (EntityNotFoundException e) {
                System.out.println(e);
                // ok
            }
        });
    }
}