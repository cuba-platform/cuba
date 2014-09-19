/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.TestListener;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;

import java.util.Date;
import java.util.UUID;

public class EntityListenerTest extends CubaTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from SYS_SERVER");
    }

    public void test() {
        AppBeans.get(EntityListenerManager.class).addListener(Server.class, TestListener.class);
        AppBeans.get(EntityListenerManager.class).addListener(Server.class, "cuba_TestListenerBean");

        UUID id, id1;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            Server server1 = new Server();
            id1 = server1.getId();
            server1.setName("otherhost");
            server1.setRunning(true);
            em.persist(server1);

            tx.commitRetaining();

            em = persistence.getEntityManager();
            server = em.find(Server.class, id);
            server.setName(server.getName() + " - " + new Date());

            tx.commitRetaining();

            em = persistence.getEntityManager();
            server = em.find(Server.class, id1);
            em.remove(server);

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
