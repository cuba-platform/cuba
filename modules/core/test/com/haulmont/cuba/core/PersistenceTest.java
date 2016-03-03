/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class PersistenceTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID userId;

    @Before
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER", userId);
    }

    private void raiseException() {
        throw new RuntimeException("test_ex");
    }

    @Test
    public void testLoadByCombinedView() throws Exception {
        User user;
        Transaction tx = cont.persistence().createTransaction();
        try {
            // load by single view

            EntityManager em = cont.persistence().getEntityManager();

            user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"),
                    new View(User.class, false).addProperty("login"));

            assertTrue(cont.persistence().getTools().isLoaded(user, "login"));
            assertFalse(cont.persistence().getTools().isLoaded(user, "name"));

            tx.commitRetaining();

            // load by combined view

            em = cont.persistence().getEntityManager();

            user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"),
                    new View(User.class, false).addProperty("login"),
                    new View(User.class, false).addProperty("name")
            );

            assertTrue(cont.persistence().getTools().isLoaded(user, "login"));
            assertTrue(cont.persistence().getTools().isLoaded(user, "name"));

            tx.commitRetaining();

            // load by complex combined view

            em = cont.persistence().getEntityManager();

            user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"),
                    new View(User.class, false).addProperty("login"),
                    new View(User.class, false).addProperty("group", new View(Group.class).addProperty("name"))
            );

            assertTrue(cont.persistence().getTools().isLoaded(user, "login"));
            assertFalse(cont.persistence().getTools().isLoaded(user, "name"));
            assertTrue(cont.persistence().getTools().isLoaded(user, "group"));
            assertTrue(cont.persistence().getTools().isLoaded(user.getGroup(), "name"));

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testMergeNotLoaded() throws Exception {
        User user;
        Group group;

        Transaction tx = cont.persistence().createTransaction();
        try {
            User transientUser = new User();
            transientUser.setId(userId);
            transientUser.setName("testUser1");

            EntityManager em = cont.persistence().getEntityManager();
            em.merge(transientUser);

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId);
            assertNotNull(user);
            group = user.getGroup();
        } finally {
            tx.end();
        }

        assertEquals(userId, user.getId());
        assertEquals("testUser1", user.getName());
        assertEquals("testLogin", user.getLogin());
        assertNotNull(group);
    }

    @Test
    public void testDirtyFields() throws Exception {
        PersistenceTools persistenceTools = cont.persistence().getTools();

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            UUID id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            assertTrue(persistenceTools.isDirty(server, "name", "running"));
            assertNull(persistenceTools.getOldValue(server, "data"));

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            server = em.find(Server.class, id);
            assertNotNull(server);
            server.setData("testData");

            Set<String> dirtyFields = persistenceTools.getDirtyFields(server);
            assertTrue(dirtyFields.contains("data"));
            assertTrue(persistenceTools.isDirty(server, "data"));
            assertNull(persistenceTools.getOldValue(server, "data"));

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            server = em.find(Server.class, id);
            assertNotNull(server);

            server.setData("testData1");
            assertEquals("testData", persistenceTools.getOldValue(server, "data"));

            tx.commit();
        } finally {
            tx.end();
        }
    }

    /**
     * OpenJPA silently ignores setting null in nullable=false attribute.
     */
    @Test
    public void testNonNullAttribute() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            User user = em.find(User.class, userId);
            assertNotNull(user);
            user.setLogin(null);
            user.setName(null);
            tx.commitRetaining();
            fail();

// Old OpenJPA behaviour
//            em = cont.persistence().getEntityManager();
//            user = em.find(User.class, userId);
//            assertNotNull(user);
//            assertNotNull(user.getLogin()); // null was not saved
//            assertNull(user.getName());     // null was saved

            tx.commit();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("NOT NULL check constraint"));
        } finally {
            tx.end();
        }

    }
}
