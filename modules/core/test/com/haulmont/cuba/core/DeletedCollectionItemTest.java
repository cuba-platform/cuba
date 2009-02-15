/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.12.2008 16:57:38
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.core.global.View;

import java.util.UUID;
import java.util.Set;

public class DeletedCollectionItemTest extends CubaTestCase
{
    private UUID groupId;
    private UUID userId;
    private UUID role2Id;
    private UUID userRole1Id;
    private UUID userRole2Id;

    protected void setUp() throws Exception {
        super.setUp();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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
            role2.setName("role2");
            em.persist(role2);

            UserRole userRole2 = new UserRole();
            userRole2Id = userRole2.getId();
            userRole2.setUser(user);
            userRole2.setRole(role2);
            em.persist(userRole2);
            
            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();

            UserRole ur = em.find(UserRole.class, userRole2Id);
            em.remove(ur);

            Role r = em.find(Role.class, role2Id);
            em.remove(r);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void tearDown() throws Exception {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q;

            q = em.createNativeQuery("delete from SEC_USER_ROLE where ID = ? or ID = ?");
            q.setParameter(1, userRole1Id.toString());
            q.setParameter(2, userRole2Id.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_ROLE where ID = ?");
            q.setParameter(1, role2Id.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_USER where ID = ?");
            q.setParameter(1, userId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_GROUP where ID = ?");
            q.setParameter(1, groupId.toString());
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
        super.tearDown();
    }

    public void testNormalMode() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Role role = em.find(Role.class, role2Id);
            assertNull(role);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testCleanupMode() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.setDeleteDeferred(false);

            Role role = em.find(Role.class, role2Id);
            assertNotNull(role);
            assertTrue(role.isDeleted());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testOneToMany() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(
                    new View(User.class, "testView")
                            .addProperty("name")
                            .addProperty("login")
                            .addProperty("userRoles",
                                new View(UserRole.class, "testView")
                                    .addProperty("role",
                                        new View(Role.class, "testView")
                                            .addProperty("name")))
            );
            User user = em.find(User.class, userId);

            Set<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testOneToMany_CleanupMode() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.setDeleteDeferred(false);

            em.setView(
                    new View(User.class, "testView")
                            .addProperty("name")
                            .addProperty("login")
                            .addProperty("userRoles",
                                new View(UserRole.class, "testView")
                                    .addProperty("role",
                                        new View(Role.class, "testView")
                                            .addProperty("name")))
            );
            User user = em.find(User.class, userId);

            Set<UserRole> userRoles = user.getUserRoles();
            assertEquals(2, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testOneToMany_Query() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setParameter(1, userId);
            User user = (User) q.getSingleResult();

            Set<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testOneToMany_JoinFetchQuery() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createQuery("select u from sec$User u join fetch u.userRoles where u.id = ?1");
            q.setParameter(1, userId);
            User user = (User) q.getSingleResult();

            Set<UserRole> userRoles = user.getUserRoles();
            assertEquals(1, userRoles.size());
            for (UserRole ur : userRoles) {
                assertNotNull(ur.getRole());
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

//    public void testManyToOne() {
//        Transaction tx = Locator.createTransaction();
//        try {
//            EntityManager em = PersistenceProvider.getEntityManager();
//
//            em.setView(
//                    new View(Profile.class, "testView")
//                            .addProperty("name")
//                            .addProperty("group",
//                                    new View(Group.class, "testView")
//                                        .addProperty("name")
//                            )
//            );
//            Profile profile = em.find(Profile.class, profile1Id);
//            assertNotNull(profile.getGroup());
//            assertTrue(profile.getGroup().isDeleted());
//
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//    }
}
