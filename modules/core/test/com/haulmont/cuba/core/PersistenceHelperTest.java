/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PersistenceHelperTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");
    }

    @After
    public void tearDown() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");
    }

    @Test
    public void testEntityStates() {
        try {
            PersistenceHelper.isNew(null);
            fail("isNew() should not accept null");
        } catch (Exception e) {
           //
        }
        try {
            PersistenceHelper.isManaged(null);
            fail("isManaged() should not accept null");
        } catch (Exception e) {
            //
        }
        try {
            PersistenceHelper.isDetached(null);
            fail("isDetached() should not accept null");
        } catch (Exception e) {
            //
        }

        UUID id;
        Server server;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            assertNotNull(em);
            server = new Server();

            assertTrue(PersistenceHelper.isNew(server));
            assertFalse(PersistenceHelper.isManaged(server));
            assertFalse(PersistenceHelper.isDetached(server));

            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);
            assertTrue(PersistenceHelper.isNew(server));
            assertTrue(PersistenceHelper.isManaged(server));
            assertFalse(PersistenceHelper.isDetached(server));

            tx.commit();
        } finally {
            tx.end();
        }
        assertFalse(PersistenceHelper.isNew(server));
        assertFalse(PersistenceHelper.isManaged(server));
        assertTrue(PersistenceHelper.isDetached(server));


        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            server = em.find(Server.class, id);
            assertNotNull(server);
            assertEquals(id, server.getId());

            assertFalse(PersistenceHelper.isNew(server));
            assertTrue(PersistenceHelper.isManaged(server));
            assertFalse(PersistenceHelper.isDetached(server));

            server.setRunning(false);

            tx.commit();
        } finally {
            tx.end();
        }
        assertFalse(PersistenceHelper.isNew(server));
        assertFalse(PersistenceHelper.isManaged(server));
        assertTrue(PersistenceHelper.isDetached(server));

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            server = em.merge(server);

            assertFalse(PersistenceHelper.isNew(server));
            assertTrue(PersistenceHelper.isManaged(server));
            assertFalse(PersistenceHelper.isDetached(server));

            tx.commit();
        } finally {
            tx.end();
        }


        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            server = em.find(Server.class, id);
            assertNotNull(server);
            assertEquals(id, server.getId());

            em.remove(server);

            assertFalse(PersistenceHelper.isNew(server));
            assertTrue(PersistenceHelper.isManaged(server));  // is it correct?
            assertFalse(PersistenceHelper.isDetached(server));

            tx.commit();
        } finally {
            tx.end();
        }

        assertFalse(PersistenceHelper.isNew(server));
        assertFalse(PersistenceHelper.isManaged(server));
        assertTrue(PersistenceHelper.isDetached(server)); // is it correct?
    }
}
