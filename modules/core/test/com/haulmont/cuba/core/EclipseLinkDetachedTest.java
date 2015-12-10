/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import org.eclipse.persistence.internal.queries.EntityFetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Set;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EclipseLinkDetachedTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID userId;
    private UUID userRoleId;

    @Before
    public void setUp() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setPosition("manager");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            UserRole userRole = new UserRole();
            userRoleId = userRole.getId();
            userRole.setUser(user);
            userRole.setRole(em.find(Role.class, UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93")));
            em.persist(userRole);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER_ROLE", userRoleId);
        cont.deleteRecord("SEC_USER", userId);
    }

    @Test
    public void testNotSerialized() throws Exception {
        Transaction tx;
        EntityManager em;
        User user;
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }

        assertEquals("testUser", user.getName());
        assertNotNull(user.getGroup());
        assertEquals(1, user.getUserRoles().size());
    }

    @Test
    public void testSerialized() throws Exception {
        Transaction tx;
        EntityManager em;
        User user;
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }

        user = reserialize(user);

        assertEquals("testUser", user.getName());

        // exception on getting not loaded references
        try {
            assertNotNull(user.getGroup());
            fail();
            assertEquals(1, user.getUserRoles().size());
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testNotSerializedFetchGroup() throws Exception {
        Transaction tx;
        EntityManager em;
        User user;
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            View view = new View(User.class)
                    .addProperty("login");
            user = em.find(User.class, userId, view);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }

        assertEquals("testLogin", user.getLogin());

        // unfetched
        try {
            user.getName();
            fail();
        } catch (IllegalStateException ignored) {
        }
        try {
            user.getGroup();
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testSerializedFetchGroup() throws Exception {
        Transaction tx;
        EntityManager em;
        User user;
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            View view = new View(User.class)
                    .addProperty("login");
            user = em.find(User.class, userId, view);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }

        user = reserialize(user);

        assertEquals("testLogin", user.getLogin());
        // exception on getting not loaded references
        try {
            user.getName();
            fail();
        } catch (Exception ignored) {
        }
        try {
            user.getGroup();
            fail();
        } catch (Exception ignored) {
        }
        try {
            user.getUserRoles().size();
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testSerializedFetchGroupMerge() throws Exception {
        Transaction tx;
        EntityManager em;
        User user;
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            View view = new View(User.class)
                    .addProperty("login");
            user = em.find(User.class, userId, view);
            assertNotNull(user);

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);

        assertEquals("testLogin", user.getLogin());
        // exception on getting not loaded references
        try {
            user.getName();
            fail();
        } catch (Exception ignored) {
        }
        try {
            user.getGroup();
            fail();
        } catch (Exception ignored) {
        }
        try {
            user.getUserRoles().size();
            fail();
        } catch (Exception ignored) {
        }

        user.setLogin("testLogin-1");

        // merge
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            user = em.merge(user);

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);

        assertEquals("testLogin-1", user.getLogin());
        // loaded by mapping rules
        assertEquals("testUser", user.getName());
        // exception on getting not loaded references
        try {
            user.getGroup();
            fail();
        } catch (Exception ignored) {
        }
        try {
            user.getUserRoles().size();
            fail();
        } catch (Exception ignored) {
        }

        // find without view
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);

        assertEquals("testLogin-1", user.getLogin());
        assertEquals("testUser", user.getName());
    }

    @Test
    public void testModifiedFetchGroup() throws Exception {
        Transaction tx;
        EntityManager em;
        User user;
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            View view = new View(User.class)
                    .addProperty("login")
                    .addProperty("position");
            user = em.find(User.class, userId, view);
            assertNotNull(user);

            tx.commit();
        } finally {
            tx.end();
        }
        user.setPosition(""); // restricted attribute
        user = reserialize(user);

        Set<String> attributeNames = ((FetchGroupTracker) user)._persistence_getFetchGroup().getAttributeNames();
        attributeNames.remove("position");
        ((FetchGroupTracker) user)._persistence_setFetchGroup(new EntityFetchGroup(attributeNames));

        user.setLogin("testLogin-1");

        // merge
        tx = cont.persistence().createTransaction();
        try {
            em = cont.persistence().getEntityManager();
            user = em.merge(user);

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);

        assertEquals("testLogin-1", user.getLogin());
        // restricted was not changed
        assertEquals("manager", user.getPosition());
        // loaded by mapping rules
        assertEquals("testUser", user.getName());
    }
}
