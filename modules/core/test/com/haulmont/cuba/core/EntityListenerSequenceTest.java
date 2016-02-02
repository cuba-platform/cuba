/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.TestListener;
import com.haulmont.cuba.core.listener.TestListenerAllEvents;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Konstantin Krivopustov
 */
public class EntityListenerSequenceTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private EntityListenerManager entityListenerManager;

    @Before
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");

        entityListenerManager = AppBeans.get(EntityListenerManager.class);
        entityListenerManager.addListener(Server.class, TestListenerAllEvents.class);
    }

    @After
    public void tearDown() throws Exception {
        entityListenerManager.removeListener(Server.class, TestListenerAllEvents.class);
    }

    @Test
    public void test() throws Exception {
        Server server = new Server();

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            server.setName("localhost");
            server.setRunning(true);
            em.persist(server);
            tx.commit();
        }
        assertEquals(Arrays.asList("onBeforeInsert: " + server.getId(), "onAfterInsert: " + server.getId()), TestListenerAllEvents.events);
        TestListenerAllEvents.events.clear();

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            server.setName("changed");
            server = em.merge(server);
            tx.commit();
        }
        assertEquals(Arrays.asList("onBeforeUpdate: " + server.getId(), "onAfterUpdate: " + server.getId()), TestListenerAllEvents.events);
        TestListenerAllEvents.events.clear();

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            em.remove(server);
            tx.commit();
        }
        assertEquals(Arrays.asList("onBeforeDelete: " + server.getId(), "onAfterDelete: " + server.getId()), TestListenerAllEvents.events);
        TestListenerAllEvents.events.clear();
    }
}
