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

package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.entity.diff.EntityDiff;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.*;

import java.util.UUID;

import static org.junit.Assert.*;

/**
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

        assertTrue(PersistenceHelper.isNew(new EntityDiff(null)));

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

    @Test
    public void testCheckLoaded() {
        Server server = new Server();

        cont.persistence().runInTransaction((em) -> {
            em.persist(server);
        });

        View view = new View(Server.class).addProperty("name").addProperty("data")
                .setLoadPartialEntities(true);
        Server reloadedServer = cont.persistence().callInTransaction((em) -> {
            return em.find(Server.class, server.getId(), view);
        });

        PersistenceHelper.checkLoaded(reloadedServer, "name"); // fine

        try {
            PersistenceHelper.checkLoaded(reloadedServer, "data", "running");
            Assert.fail("Must throw exception");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Server.running"));
        }
    }
}
