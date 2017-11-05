/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.listener.*;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class EntityListenerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;

    private TestListenerBean listenerBean;
    private TestDetachAttachListener detachAttachListener;

    private EntityListenerManager entityListenerManager;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();
        metadata = cont.metadata();

        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");

        listenerBean = AppBeans.get("cuba_TestListenerBean");
        listenerBean.events.clear();

        detachAttachListener = AppBeans.get("cuba_TestDetachAttachListener");
        detachAttachListener.events.clear();

        TestListener.events.clear();
        TestUserEntityListener.events.clear();

        entityListenerManager = AppBeans.get(EntityListenerManager.class);
        entityListenerManager.addListener(User.class, TestUserEntityListener.class);
        entityListenerManager.addListener(Server.class, TestListener.class);
        entityListenerManager.addListener(Server.class, "cuba_TestListenerBean");
        entityListenerManager.addListener(Server.class, "cuba_TestDetachAttachListener");
        entityListenerManager.addListener(Server.class, "cuba_TestListenerUsingEntityManager");
    }

    @After
    public void tearDown() throws Exception {
        entityListenerManager.removeListener(Server.class, "cuba_TestListenerUsingEntityManager");
        entityListenerManager.removeListener(Server.class, "cuba_TestDetachAttachListener");
        entityListenerManager.removeListener(Server.class, "cuba_TestListenerBean");
        entityListenerManager.removeListener(Server.class, TestListener.class);
        entityListenerManager.removeListener(User.class, TestUserEntityListener.class);
    }

    @Test
    public void test() {
        UUID id, id1;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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

            em = cont.persistence().getEntityManager();
            server = em.find(Server.class, id);
            server.setName(server.getName() + " - " + new Date());

            tx.commitRetaining();

            assertEquals(1, TestListener.events.size());
            assertEquals("onAfterUpdate: " + id, TestListener.events.get(0));
            TestListener.events.clear();

            assertEquals(1, listenerBean.events.size());
            assertEquals("onAfterUpdate: " + id, listenerBean.events.get(0));
            listenerBean.events.clear();

            em = cont.persistence().getEntityManager();
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

    @Test
    public void testDetachAttch() throws Exception {
        UUID id;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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
            em = cont.persistence().getEntityManager();
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

    @Test
    public void testEntityManager() throws Exception {
        Server server;
        UUID serverId;
        Transaction tx = cont.persistence().createTransaction();
        try {
            // create

            server = new Server();
            server.setName("server1");
            serverId = server.getId();
            cont.persistence().getEntityManager().persist(server);

            tx.commitRetaining();

            assertNotNull(server.getData());
            UUID relatedId = UUID.fromString(server.getData());
            FileDescriptor related = cont.persistence().getEntityManager().find(FileDescriptor.class, relatedId);
            assertNotNull(related);
            assertEquals("Related", related.getName());

            tx.commitRetaining();

            // update

            server = cont.persistence().getEntityManager().find(Server.class, serverId);
            assertNotNull(server);
            server.setName("server1 updated");

            tx.commitRetaining();

            related = cont.persistence().getEntityManager().find(FileDescriptor.class, relatedId);
            assertNotNull(related);
            assertEquals("Related updated", related.getName());

            tx.commitRetaining();

            // remove

            server = cont.persistence().getEntityManager().find(Server.class, serverId);
            assertNotNull(server);
            cont.persistence().getEntityManager().remove(server);

            tx.commitRetaining();

            related = cont.persistence().getEntityManager().find(FileDescriptor.class, relatedId);
            assertNull(related);

            tx.commit();
        } finally {
            tx.end();
        }

    }

    @Test
    public void testSequenceForSoftDeletedEntity() throws Exception {
        User user;
        try (Transaction tx = persistence.createTransaction()) {
            user = metadata.create(User.class);
            user.setGroup(persistence.getEntityManager().find(Group.class, TestSupport.COMPANY_GROUP_ID));
            user.setLogin("user-" + user.getId());
            persistence.getEntityManager().persist(user);
            tx.commit();
        }
        assertEquals(2, TestUserEntityListener.events.size());
        assertTrue(TestUserEntityListener.events.get(0).contains("onBeforeInsert"));
        assertTrue(TestUserEntityListener.events.get(1).contains("onAfterInsert"));
        TestUserEntityListener.events.clear();

        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            u.setName(u.getLogin());
            tx.commit();
        }
        assertEquals(2, TestUserEntityListener.events.size());
        assertTrue(TestUserEntityListener.events.get(0).contains("onBeforeUpdate"));
        assertTrue(TestUserEntityListener.events.get(1).contains("onAfterUpdate"));
        TestUserEntityListener.events.clear();

        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            persistence.getEntityManager().remove(u);
            tx.commit();
        }
        assertEquals(2, TestUserEntityListener.events.size());
        assertTrue(TestUserEntityListener.events.get(0).contains("onBeforeDelete"));
        assertTrue(TestUserEntityListener.events.get(1).contains("onAfterDelete"));
    }

    @Test
    public void testExceptionInListener() throws Exception {
        Server server;
        try (Transaction tx = persistence.createTransaction()) {
            server = metadata.create(Server.class);
            server.setName("localhost");
            persistence.getEntityManager().persist(server);
            tx.commit();
        }
        entityListenerManager.addListener(Server.class, TestListenerThrowing.class);
        try {
            try (Transaction tx = persistence.createTransaction()) {
                Server s = persistence.getEntityManager().find(Server.class, server.getId());
                s.setName("changed");
                tx.commit();
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalStateException);
                assertEquals("test exception", e.getMessage());
            }
        } finally {
            entityListenerManager.removeListener(Server.class, TestListenerThrowing.class);
            cont.deleteRecord(server);
        }
    }
}
