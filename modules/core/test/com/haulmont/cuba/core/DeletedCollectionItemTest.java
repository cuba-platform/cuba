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
import com.haulmont.cuba.security.entity.Subject;
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
    private UUID subject1Id;
    private UUID subject2Id;

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
            profile1.setGroup(group);
            em.persist(profile1);

            Profile profile2 = new Profile();
            profile2Id = profile2.getId();
            profile2.setName("testProfile2");
            profile2.setGroup(group);
            em.persist(profile2);

            Subject subject1 = new Subject();
            subject1Id = subject1.getId();
            subject1.setUser(user);
            subject1.setProfile(profile1);
            em.persist(subject1);

            Subject subject2 = new Subject();
            subject2Id = subject2.getId();
            subject2.setUser(user);
            subject2.setProfile(profile2);
            em.persist(subject2);

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

            Query q = em.createNativeQuery("delete from SEC_SUBJECT where ID = ? or ID = ?");
            q.setParameter(1, subject1Id.toString());
            q.setParameter(2, subject2Id.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_PROFILE where ID = ? or ID = ?");
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
                            .addProperty("subjects",
                                    new View(Subject.class, "testView")
                                            .addProperty("profile")
                            )
            );
            User user = em.find(User.class, userId);

            Set<Subject> subjects = user.getSubjects();
            assertEquals(2, subjects.size());
            for (Subject subject : subjects) {
                System.out.println(subject.getProfile().getName());
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
                            .addProperty("subjects",
                                    new View(Subject.class, "testView")
                                            .addProperty("profile")
                            )
            );
            User user = em.find(User.class, userId);

            Set<Subject> subjects = user.getSubjects();
            assertEquals(2, subjects.size());
            for (Subject subject : subjects) {
                System.out.println(subject.getProfile().getName());
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

            Set<Subject> subjects = user.getSubjects();
            assertEquals(2, subjects.size());
            for (Subject subject : subjects) {
                System.out.println(subject.getProfile().getName());
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

            Query q = em.createQuery("select u from sec$User u join fetch u.subjects where u.id = ?1");
            q.setParameter(1, userId);
            User user = (User) q.getSingleResult();

            Set<Subject> subjects = user.getSubjects();
            assertEquals(2, subjects.size());
            for (Subject subject : subjects) {
                System.out.println(subject.getProfile().getName());
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
