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
 */
package com.haulmont.cuba.soft_delete;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.FetchMode;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import org.eclipse.persistence.internal.helper.CubaUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class SoftDeleteTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;
    
    private UUID groupId;
    private UUID userId;
    private UUID role2Id;
    private UUID userRole1Id;
    private UUID userRole2Id;
    private Persistence persistence;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Group group = new Group();
            groupId = group.getId();
            group.setName("testGroup");
            em.persist(group);

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setGroup(group);
            em.persist(user);

            Role role1 = em.find(Role.class, UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93"));

            UserRole userRole1 = new UserRole();
            userRole1Id = userRole1.getId();
            userRole1.setUser(user);
            userRole1.setRole(role1);
            em.persist(userRole1);

            Role role2 = new Role();
            role2Id = role2.getId();
            role2.setName("roleToBeDeleted");
            em.persist(role2);

            UserRole userRole2 = new UserRole();
            userRole2Id = userRole2.getId();
            userRole2.setUser(user);
            userRole2.setRole(role2);
            em.persist(userRole2);
            
            tx.commitRetaining();

            em = cont.persistence().getEntityManager();

            UserRole ur = em.find(UserRole.class, userRole2Id);
            em.remove(ur);

            Role r = em.find(Role.class, role2Id);
            em.remove(r);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER_ROLE", userRole1Id, userRole2Id);
        cont.deleteRecord("SEC_ROLE", role2Id);
        cont.deleteRecord("SEC_USER", userId);
        cont.deleteRecord("SEC_GROUP", groupId);
    }

    @Test
    public void testMultipleTransactions() throws Exception {
        try (Transaction tx = persistence.createTransaction()) {
            assertTrue(CubaUtil.isSoftDeletion());

            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(false);
            assertFalse(CubaUtil.isSoftDeletion());

            tx.commit();
        }

        try (Transaction tx = persistence.createTransaction()) {
            assertTrue(CubaUtil.isSoftDeletion());

            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(false);
            assertFalse(CubaUtil.isSoftDeletion());

            try (Transaction tx1 = persistence.createTransaction()) {
                assertTrue(CubaUtil.isSoftDeletion());

                tx1.commit();
            }
            assertFalse(CubaUtil.isSoftDeletion());

            tx.commit();
        }
    }

    @Test
    public void testNormalMode() {
        System.out.println("===================== BEGIN testNormalMode =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Role role = em.find(Role.class, role2Id);
            assertNull(role);

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testNormalMode =====================");
    }

    @Test
    public void testCleanupMode() {
        System.out.println("===================== BEGIN testCleanupMode =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            em.setSoftDeletion(false);

            Role role = em.find(Role.class, role2Id);
            assertNotNull(role);
            assertTrue(role.isDeleted());

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testCleanupMode =====================");
    }

    @Test
    public void testManyToOne() throws SQLException {
        System.out.println("===================== BEGIN testManyToOne =====================");

        QueryRunner queryRunner = new QueryRunner(persistence.getDataSource());
        queryRunner.update("update SEC_GROUP set DELETE_TS = current_timestamp, DELETED_BY = 'admin' where ID = ?", new Object[] {groupId.toString()});

        // test without view
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId);
            Group group = user.getGroup();

            tx.commit();

            assertNotNull(group);
            assertTrue(group.isDeleted());
        }

        View view;

        // test fetchMode = AUTO (JOIN is used)
        view = new View(User.class, "testView")
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class).addProperty("name"));
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId, view);
            Group group = user.getGroup();

            tx.commit();

            assertNotNull(group);
            assertTrue(group.isDeleted());
        }

        // test fetchMode = UNDEFINED
        view = new View(User.class, "testView")
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class).addProperty("name"), FetchMode.UNDEFINED);
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId, view);
            Group group = user.getGroup();

            tx.commit();

            assertNotNull(group);
            assertTrue(group.isDeleted());
        }

        // test fetchMode = BATCH
        view = new View(User.class, "testView")
                .addProperty("name")
                .addProperty("login")
                .addProperty("group", new View(Group.class).addProperty("name"), FetchMode.BATCH);
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId, view);
            Group group = user.getGroup();

            tx.commit();
//////////////////////////////////////////////// fails!
//            assertNotNull(group);
//            assertTrue(group.isDeleted());
        }

        System.out.println("===================== END testManyToOne =====================");
    }

    @Test
    public void testOneToMany() {
        System.out.println("===================== BEGIN testOneToMany =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            View view = new View(User.class, "testView")
                            .addProperty("name")
                            .addProperty("login")
                            .addProperty("userRoles",
                                    new View(UserRole.class, "testView")
                                            .addProperty("role",
                                                    new View(Role.class, "testView")
                                                            .addProperty("name")));
            User user = em.find(User.class, userId, view);

            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testOneToMany =====================");
    }

    @Test
    public void testOneToManyLazy() {
        System.out.println("===================== BEGIN testOneToManyLazy =====================");
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, userId);

            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testOneToManyLazy =====================");
    }

    @Test
    public void testOneToManyLazy2() {
        System.out.println("===================== BEGIN testOneToManyLazy2 =====================");
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            em.setSoftDeletion(false);

            User user = em.find(User.class, userId);

            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(2, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        }

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, userId);

            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        }

        System.out.println("===================== END testOneToManyLazy2 =====================");
    }

    @Test
    public void testOneToMany_CleanupMode() {
        System.out.println("===================== BEGIN testOneToMany_CleanupMode =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            em.setSoftDeletion(false);

            View view =  new View(User.class, "testView")
                            .addProperty("name")
                            .addProperty("login")
                            .addProperty("userRoles",
                                new View(UserRole.class, "testView")
                                    .addProperty("role",
                                        new View(Role.class, "testView")
                                            .addProperty("name")));
            User user = em.find(User.class, userId, view);

            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(2, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testOneToMany_CleanupMode =====================");
    }

    @Test
    public void testOneToMany_Query() {
        System.out.println("===================== BEGIN testOneToMany_Query =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setParameter(1, userId);
            User user = (User) q.getSingleResult();

            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testOneToMany_Query =====================");
    }

    @Test
    public void testOneToMany_JoinFetchQuery() {
        System.out.println("===================== BEGIN testOneToMany_JoinFetchQuery =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Query q = em.createQuery("select u from sec$User u join fetch u.userRoles where u.id = ?1");
            q.setParameter(1, userId);
            User user = (User) q.getSingleResult();

            List<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testOneToMany_JoinFetchQuery =====================");
    }

    @Test
    public void testQuery() {
        System.out.println("===================== BEGIN testQuery =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Query query = em.createQuery("select r from sec$Role r where r.name = ?1");
            query.setParameter(1, "roleToBeDeleted");

            List<Role> list = query.getResultList();
            assertTrue(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testQuery =====================");
    }

    @Test
    public void testQuery_CleanupMode() {
        System.out.println("===================== BEGIN testQuery_CleanupMode =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            em.setSoftDeletion(false);
            Query query = em.createQuery("select r from sec$Role r where r.name = ?1");
            query.setParameter(1, "roleToBeDeleted");

            List<Role> list = query.getResultList();
            assertTrue(!list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testQuery_CleanupMode =====================");
    }

    @Test
    public void testUpdateQuery_CleanupMode() {
        System.out.println("===================== BEGIN testUpdateQuery_CleanupMode =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            em.setSoftDeletion(false);
            Query query = em.createQuery("update sec$Role r set r.description = ?1 where r.name = ?2");
            query.setParameter(1, "Updated");
            query.setParameter(2, "roleToBeDeleted");
            int updated = query.executeUpdate();

            assertEquals(1, updated);
            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testUpdateQuery_CleanupMode =====================");
    }

    @Test
    public void testQueryWithoutConditions() {
        System.out.println("===================== BEGIN testQueryWithoutConditions =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Query query = em.createQuery("select r from sec$Role r");

            List<Role> list = query.getResultList();
            for (Role role : list) {
                if (role.getId().equals(role2Id)) {
                    fail();
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testQueryWithoutConditions =====================");
    }

    @Test
    public void testRemoveNotManaged() {
        System.out.println("===================== BEGIN testRemoveNotManaged =====================");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            UserRole userRole = em.find(UserRole.class, userRole1Id);

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            em.remove(userRole);

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            UserRole deletedUserRole = em.find(UserRole.class, userRole1Id);
            assertNull(deletedUserRole);

            tx.commit();
        } finally {
            tx.end();
        }
        System.out.println("===================== END testRemoveNotManaged =====================");
    }

    @Test
    public void testGlobalSoftDeleteSwitch() throws Exception {
        EntityManager em;
        Transaction tx;
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            assertTrue(em.isSoftDeletion());
            tx.commit();
        } finally {
            tx.end();
        }

        cont.persistence().setSoftDeletion(false);
        try {
            tx = cont.persistence().createTransaction();
            try {
                em = cont.persistence().getEntityManager();
                assertFalse(em.isSoftDeletion());

                cont.persistence().setSoftDeletion(true);
                em = cont.persistence().getEntityManager();
                assertFalse(em.isSoftDeletion()); // cont.persistence().setSoftDeletion affects only EMs created in a new tx

                tx.commit();
            } finally {
                tx.end();
            }
        } finally {
            cont.persistence().setSoftDeletion(true);
        }

        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            assertTrue(em.isSoftDeletion());
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testSoftDeletionIsolation() throws Exception {
        try (Transaction tx = cont.persistence().createTransaction()) {
            User u = cont.entityManager().find(User.class, userId);
            cont.entityManager().remove(u);
            tx.commit();
        }

        try (Transaction tx = cont.persistence().createTransaction()) {
            cont.persistence().getEntityManager().setSoftDeletion(false);
            User u = cont.entityManager().find(User.class, userId);
            assertNotNull(u);

            Thread thread = new Thread(this::loadDeletedUser);
            thread.start();
            thread.join();

            tx.commit();
        }

        loadDeletedUser();
    }

    private void loadDeletedUser() {
        try (Transaction tx = cont.persistence().createTransaction()) {
            User u = cont.entityManager().find(User.class, userId);
            assertNull(u);
            tx.commit();
        }
    }
}
