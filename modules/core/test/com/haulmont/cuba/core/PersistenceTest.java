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

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = em.find(Server.class, id);
            assertEquals(id, server.getId());

            em.remove(server);
            
            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void raiseException() {
        throw new RuntimeException("test_ex");
    }
}
