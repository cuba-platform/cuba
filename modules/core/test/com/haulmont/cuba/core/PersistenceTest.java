/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 20:50:16
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Server;

import javax.transaction.*;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.UUID;

public class PersistenceTest extends CubaTestCase
{
    public void test() {
        UUID id;
        beginTran();
        try {
            EntityManagerAdapter em = Locator.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);
            commitTran();
        } catch (Exception e) {
            rollbackTran();
            throw new RuntimeException(e);
        }

        EntityManagerAdapter em = Locator.getEntityManager();
        Server server = em.find(Server.class, id);
        assertEquals(id, server.getId());
    }

    private void beginTran() {
        try {
            getTransactionManager().begin();
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    private void commitTran() {
        try {
            getTransactionManager().commit();
        } catch (RollbackException e) {
            throw new RuntimeException(e);
        } catch (HeuristicMixedException e) {
            throw new RuntimeException(e);
        } catch (HeuristicRollbackException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    private void rollbackTran() {
        TransactionManager tm = getTransactionManager();
        try {
            if (tm.getStatus() == Status.STATUS_ACTIVE)
                tm.rollback();
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    private TransactionManager getTransactionManager() {
        Context ctx = Locator.getJndiContext();
        try {
            return (TransactionManager) ctx.lookup("java:/TransactionManager");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
