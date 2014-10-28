/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Server;

import java.util.List;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TransactionTest extends CubaTestCase {

    private static final String TEST_EXCEPTION_MSG = "test exception";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from SYS_SERVER");
    }

    public void testNoTransaction() {
        try {
            EntityManager em = persistence.getEntityManager();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    public void testCommit() {
        UUID id;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Server server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setRunning(false);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testCommitRetaining() {
        UUID id;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            tx.commitRetaining();

            em = persistence.getEntityManager();
            server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setRunning(false);

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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            server.setName("localhost");
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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            server.setName("localhost");
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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            tx.commitRetaining();

            em = persistence.getEntityManager();
            server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setRunning(false);

            throwException();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNestedRollback() {
        try {
            Transaction tx = persistence.createTransaction();
            try {

                Transaction tx1 = persistence.getTransaction();
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
        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = persistence.createTransaction();
            try {
                EntityManager em1 = persistence.getEntityManager();
                assertTrue(em != em1);

                Query query = em1.createQuery("select s from sys$Server s");
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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = persistence.createTransaction();
            try {
                EntityManager em1 = persistence.getEntityManager();
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
