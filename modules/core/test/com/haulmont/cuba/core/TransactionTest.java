/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.11.2008 17:53:55
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Server;

import java.util.UUID;

public class TransactionTest extends CubaTestCase
{
    private static final String TEST_EXCEPTION_MSG = "test exception";

    public void testCommit() {
        UUID id;
        TransactionAdapter tx = Locator.createTransaction();
        try {
            EntityManagerAdapter em = PersistenceProvider.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = Locator.createTransaction();
        try {
            EntityManagerAdapter em = PersistenceProvider.getEntityManager();
            Server server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setAddress("222");

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testCommitRetaining() {
        UUID id;
        TransactionAdapter tx = Locator.createTransaction();
        try {
            EntityManagerAdapter em = PersistenceProvider.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setAddress("222");

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testRollback() {
        try {
            __testRollback();
            fail();
        } catch (Exception e) {
            assertEquals(TEST_EXCEPTION_MSG, e.getMessage());
        }
    }

    private void __testRollback() {
        TransactionAdapter tx = Locator.createTransaction();
        try {
            EntityManagerAdapter em = PersistenceProvider.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);
            throwException();
            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testRollbackAndCatch() {
        try {
            __testRollbackAndCatch();
            fail();
        } catch (Exception e) {
            assertEquals(TEST_EXCEPTION_MSG, e.getMessage());
        }
    }

    private void __testRollbackAndCatch() {
        TransactionAdapter tx = Locator.createTransaction();
        try {
            EntityManagerAdapter em = PersistenceProvider.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);
            throwException();
            tx.commit();
        } catch (RuntimeException e) {
            System.out.println("Caught exception: " + e.getMessage());
            throw e;
        } finally {
            tx.end();
        }
    }

    public void testCommitRetainingAndRollback() {
        try {
            __testCommitRetainingAndRollback();
            fail();
        } catch (Exception e) {
            assertEquals(TEST_EXCEPTION_MSG, e.getMessage());
        }
    }

    private void __testCommitRetainingAndRollback() {
        UUID id;
        TransactionAdapter tx = Locator.createTransaction();
        try {
            EntityManagerAdapter em = PersistenceProvider.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setAddress("222");

            throwException();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void throwException() {
        throw new RuntimeException(TEST_EXCEPTION_MSG);
    }
}
