/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.TestDetachAttachListener;
import com.haulmont.cuba.core.listener.TestListener;
import com.haulmont.cuba.core.listener.TestListenerBean;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class EntityListenerTest extends CubaTestCase {

    private TestListenerBean listenerBean;
    private TestDetachAttachListener detachAttachListener;

    private EntityListenerManager entityListenerManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from SYS_SERVER");

        listenerBean = AppBeans.get("cuba_TestListenerBean");
        listenerBean.events.clear();

        detachAttachListener = AppBeans.get("cuba_TestDetachAttachListener");
        detachAttachListener.events.clear();

        TestListener.events.clear();

        entityListenerManager = AppBeans.get(EntityListenerManager.class);
        entityListenerManager.addListener(Server.class, TestListener.class);
        entityListenerManager.addListener(Server.class, "cuba_TestListenerBean");
        entityListenerManager.addListener(Server.class, "cuba_TestDetachAttachListener");
        entityListenerManager.addListener(Server.class, "cuba_TestListenerUsingEntityManager");
    }

    @Override
    public void tearDown() throws Exception {
        entityListenerManager.removeListener(Server.class, "cuba_TestListenerUsingEntityManager");
        entityListenerManager.removeListener(Server.class, "cuba_TestDetachAttachListener");
        entityListenerManager.removeListener(Server.class, "cuba_TestListenerBean");
        entityListenerManager.removeListener(Server.class, TestListener.class);
        super.tearDown();
    }

    public void test() {
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

            assertEquals(2, TestListener.events.size());
            assertTrue(CollectionUtils.isEqualCollection(
                    Arrays.asList("onAfterInsert: " + id, "onAfterInsert: " + id1),
                    TestListener.events
            ));
            TestListener.events.clear();

            assertEquals(2, listenerBean.events.size());
            assertTrue(CollectionUtils.isEqualCollection(
                    Arrays.asList("onAfterInsert: " + id, "onAfterInsert: " + id1),
                    listenerBean.events
            ));
            listenerBean.events.clear();

            em = persistence.getEntityManager();
            server = em.find(Server.class, id);
            server.setName(server.getName() + " - " + new Date());

            tx.commitRetaining();

            assertEquals(1, TestListener.events.size());
            assertEquals("onAfterUpdate: " + id, TestListener.events.get(0));
            TestListener.events.clear();

            assertEquals(1, listenerBean.events.size());
            assertEquals("onAfterUpdate: " + id, listenerBean.events.get(0));
            listenerBean.events.clear();

            em = persistence.getEntityManager();
            server = em.find(Server.class, id1);
            em.remove(server);

            tx.commit();

            assertEquals(1, TestListener.events.size());
            assertEquals("onAfterDelete: " + id1, TestListener.events.get(0));

            assertEquals(1, listenerBean.events.size());
            assertEquals("onAfterDelete: " + id1, listenerBean.events.get(0));

        } finally {
            tx.end();
        }
    }

    public void testDetachAttch() throws Exception {
        UUID id;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);

            tx.commitRetaining();

            assertEquals(1, detachAttachListener.events.size());
            assertEquals("onBeforeDetach: " + id, detachAttachListener.events.get(0));
            detachAttachListener.events.clear();

            server.setName("somehost");
            em = persistence.getEntityManager();
            em.merge(server);

            assertEquals(1, detachAttachListener.events.size());
            assertEquals("onBeforeAttach: " + id, detachAttachListener.events.get(0));
            detachAttachListener.events.clear();

            tx.commit();

            assertEquals(1, detachAttachListener.events.size());
            assertEquals("onBeforeDetach: " + id, detachAttachListener.events.get(0));
            detachAttachListener.events.clear();
        } finally {
            tx.end();
        }
    }

    public void testEntityManager() throws Exception {
        Server server;
        UUID serverId;
        Transaction tx = persistence.createTransaction();
        try {
            // create

            server = new Server();
            server.setName("server1");
            serverId = server.getId();
            persistence.getEntityManager().persist(server);

            tx.commitRetaining();

            assertNotNull(server.getData());
            UUID relatedId = UUID.fromString(server.getData());
            FileDescriptor related = persistence.getEntityManager().find(FileDescriptor.class, relatedId);
            assertNotNull(related);
            assertEquals("Related", related.getName());

            tx.commitRetaining();

            // update

            server = persistence.getEntityManager().find(Server.class, serverId);
            assertNotNull(server);
            server.setName("server1 updated");

            tx.commitRetaining();

            related = persistence.getEntityManager().find(FileDescriptor.class, relatedId);
            assertNotNull(related);
            assertEquals("Related updated", related.getName());

            tx.commitRetaining();

            // remove

            server = persistence.getEntityManager().find(Server.class, serverId);
            assertNotNull(server);
            persistence.getEntityManager().remove(server);

            tx.commitRetaining();

            related = persistence.getEntityManager().find(FileDescriptor.class, relatedId);
            assertNull(related);

            tx.commit();
        } finally {
            tx.end();
        }

    }
}
