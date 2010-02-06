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
import com.haulmont.cuba.core.sys.JtaTransaction;

import javax.transaction.TransactionManager;
import javax.transaction.SystemException;
import javax.naming.NamingException;
import java.util.List;
import java.util.UUID;

public class TransactionTest extends CubaTestCase
{
    private static final String TEST_EXCEPTION_MSG = "test exception";

    public void testCommit() {
        UUID id;
        Transaction tx = Locator.createTransaction();
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

        tx = Locator.createTransaction();
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
        Transaction tx = Locator.createTransaction();
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
        Transaction tx = Locator.createTransaction();
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
        Transaction tx = Locator.createTransaction();
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
        Transaction tx = Locator.createTransaction();
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

    public void testRollbackAndContinue() {
        Transaction tx = Locator.createTransaction();
        try {
            try {
                TransactionManager tm = (TransactionManager) Locator.getJndiContext().lookup("java:/TransactionManager");
                tm.setRollbackOnly();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        } finally {
            tx.end();
        }

        tx = Locator.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNestedRollback() {
        try {
            Transaction tx = Locator.createTransaction();
            try {

                Transaction tx1 = Locator.getTransaction();
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
        Transaction tx = Locator.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = Locator.createTransaction();
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
        Transaction tx = Locator.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = Locator.createTransaction();
            try {
                EntityManager em1 = PersistenceProvider.getEntityManager();
                assertTrue(em != em1);
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
