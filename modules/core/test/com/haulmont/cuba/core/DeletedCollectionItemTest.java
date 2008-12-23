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
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.core.global.View;

import java.util.UUID;
import java.util.List;
import java.util.Set;

public class DeletedCollectionItemTest extends CubaTestCase
{
    private UUID groupId;
    private UUID userId;
    private UUID profile1Id;
    private UUID profile2Id;

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
            em.persist(user);

            Profile profile1 = new Profile();
            profile1Id = profile1.getId();
            profile1.setName("testProfile1");
            profile1.setUser(user);
            profile1.setGroup(group);
            em.persist(profile1);

            Profile profile2 = new Profile();
            profile2Id = profile2.getId();
            profile2.setName("testProfile2");
            profile2.setUser(user);
            profile2.setGroup(group);
            em.persist(profile2);

            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();

            Profile profile = em.find(Profile.class, profile2Id);
            em.remove(profile);

            Group g = em.find(Group.class, groupId);
            em.remove(g);

            tx.commit();
        } finally {
            tx.end();
        }

    }

    protected void tearDown() throws Exception {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createNativeQuery("delete from SEC_PROFILE where ID = ? or ID = ?");
            q.setParameter(1, profile1Id.toString());
            q.setParameter(2, profile2Id.toString());
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

            Group group = em.find(Group.class, groupId);
            assertNull(group);

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

            Group group = em.find(Group.class, groupId);
            assertNotNull(group);
            assertTrue(group.isDeleted());

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
                            .addProperty("profiles",
                                    new View(Profile.class, "testView")
                                            .addProperty("name")
                            )
            );
            User user = em.find(User.class, userId);

            Set<Profile> profiles = user.getProfiles();
            assertEquals(1, profiles.size());
            for (Profile profile : profiles) {
                System.out.println(profile.getName());
                assertEquals("testProfile1", profile.getName());
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
                            .addProperty("profiles",
                                    new View(Profile.class, "testView")
                                            .addProperty("name")
                            )
            );
            User user = em.find(User.class, userId);

            Set<Profile> profiles = user.getProfiles();
            assertEquals(2, profiles.size());
            for (Profile profile : profiles) {
                System.out.println(profile.getName());
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

            Set<Profile> profiles = user.getProfiles();
            assertEquals(1, profiles.size());
            for (Profile profile : profiles) {
                System.out.println(profile.getName());
                assertEquals("testProfile1", profile.getName());
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

            Query q = em.createQuery("select u from sec$User u join fetch u.profiles where u.id = ?1");
            q.setParameter(1, userId);
            User user = (User) q.getSingleResult();

            Set<Profile> profiles = user.getProfiles();
            assertEquals(1, profiles.size());
            for (Profile profile : profiles) {
                System.out.println(profile.getName());
                assertEquals("testProfile1", profile.getName());
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testManyToOne() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(
                    new View(Profile.class, "testView")
                            .addProperty("name")
                            .addProperty("group",
                                    new View(Group.class, "testView")
                                        .addProperty("name")
                            )
            );
            Profile profile = em.find(Profile.class, profile1Id);
            assertNotNull(profile.getGroup());
            assertTrue(profile.getGroup().isDeleted());

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
