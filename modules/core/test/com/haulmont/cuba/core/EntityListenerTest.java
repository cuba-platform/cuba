/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.12.2008 17:03:51
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Server;

import java.util.UUID;

public class EntityListenerTest extends CubaTestCase
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

            Server server1 = new Server();
            server1.setName("localhost");
            server1.setAddress("127.0.0.1");
            server1.setRunning(true);
            em.persist(server1);

            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            server = em.find(Server.class, id);
            server.setAddress("192.168.1.1");

            tx.commitRetaining();
        } catch (Exception e) {
            tx.end();
        }


    }


}
