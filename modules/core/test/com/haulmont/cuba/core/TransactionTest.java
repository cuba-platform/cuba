/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TransactionTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private static final String TEST_EXCEPTION_MSG = "test exception";

    @Before
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");
    }

    @Test
    public void testNoTransaction() {
        try {
            EntityManager em = cont.persistence().getEntityManager();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    @Test
    public void testCommit() {
        UUID id;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Server server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setRunning(false);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testCommitRetaining() {
        UUID id;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setRunning(false);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testRollback() {
        try {
            __testRollback();
            fail();
        } catch (Exception e) {
            assertEquals(TEST_EXCEPTION_MSG, e.getMessage());
        }
    }

    private void __testRollback() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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

    @Test
    public void testRollbackAndCatch() {
        try {
            __testRollbackAndCatch();
            fail();
        } catch (Exception e) {
            assertEquals(TEST_EXCEPTION_MSG, e.getMessage());
        }
    }

    private void __testRollbackAndCatch() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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

    @Test
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
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            server = em.find(Server.class, id);
            assertEquals(id, server.getId());
            server.setRunning(false);

            throwException();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testNestedRollback() {
        try {
            Transaction tx = cont.persistence().createTransaction();
            try {

                Transaction tx1 = cont.persistence().getTransaction();
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

    @Test
    public void testSuspend() {
        Transaction tx = cont.persistence().getTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = cont.persistence().createTransaction();
            try {
                EntityManager em1 = cont.persistence().getEntityManager();
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

    @Test
    public void testSuspendRollback() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Server server = new Server();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            Transaction tx1 = cont.persistence().createTransaction();
            try {
                EntityManager em1 = cont.persistence().getEntityManager();
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

    @Test
    public void testRunInTransaction() throws Exception {
        UUID id = cont.persistence().callInTransaction(em -> {
            assertNotNull(em);
            Server server = new Server();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);
            return server.getId();
        });

        cont.persistence().runInTransaction(em -> {
            Server server = em.find(Server.class, id);
            assertNotNull(server);
            assertEquals(id, server.getId());
            server.setRunning(false);
        });

    }

    private void throwException() {
        throw new RuntimeException(TEST_EXCEPTION_MSG);
    }
}
