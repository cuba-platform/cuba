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

import java.util.List;
import java.util.UUID;

public class TransactionTest extends CubaTestCase
{
    private static final String TEST_EXCEPTION_MSG = "test exception";

    public void testNoTransaction() {
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    public void testCommit() {
        UUID id;
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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

        tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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

    public void testNestedRollback() {
        try {
            Transaction tx = PersistenceProvider.createTransaction();
            try {

                Transaction tx1 = PersistenceProvider.getTransaction();
                try {
                    throwException();
                    fail();
                    tx1.commit();
                } catch (RuntimeException e) {
                    assertEquals(TEST_EXCEPTION_MSG, e.getMessage());
                } finally {
                    tx1.end();
                }

                tx.commit();
                fail();
            } finally {
                tx.end();
            }
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testSuspend() {
        Transaction tx = PersistenceProvider.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = PersistenceProvider.createTransaction();
            try {
                EntityManager em1 = PersistenceProvider.getEntityManager();
                assertTrue(em != em1);

                Query query = em1.createQuery("select s from core$Server s");
                List list = query.getResultList();
                assertNotNull(list);

                tx1.commit();
            } finally {
                tx1.end();
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testSuspendRollback() {
        Transaction tx = PersistenceProvider.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = PersistenceProvider.createTransaction();
            try {
                EntityManager em1 = PersistenceProvider.getEntityManager();
                assertTrue(em != em1);
                Server server1 = em1.find(Server.class, server.getId());
                assertNull(server1);
                throwException();
                tx1.commit();
            } catch (Exception e) {
                //
            } finally {
                tx1.end();
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void throwException() {
        throw new RuntimeException(TEST_EXCEPTION_MSG);
    }
}
