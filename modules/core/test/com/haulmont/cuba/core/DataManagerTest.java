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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.TemporalType;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class DataManagerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected DataManager dataManager;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);

        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");
    }

    @Test
    public void test() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext<Server> loadContext = LoadContext.create(Server.class).setId(id);

        server = dataManager.load(loadContext);
        assertEquals("localhost", server.getName());

        server.setName("krivopustov");
        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));
    }

    @Test
    public void testLoad() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext<Server> loadContext = LoadContext.create(Server.class).setId(id);

        server = dataManager.load(loadContext);
        assertEquals("localhost", server.getName());
    }

    @Test
    public void testLoadList() {
        Server server = new Server();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext<Server> loadContext = LoadContext.create(Server.class);
        loadContext.setQueryString("select s from " + PersistenceHelper.getEntityName(Server.class) + " s");

        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testLoadListById() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext<Server> loadContext = LoadContext.create(Server.class).setId(id);

        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() == 1);
    }

    @Test
    public void testAssociatedResult() throws Exception {
        LoadContext<Group> loadContext = LoadContext.create(Group.class);
        loadContext.setQueryString("select u.group from sec$User u where u.id = :userId")
                .setParameter("userId", UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

        List<Group> groups = dataManager.loadList(loadContext);
        assertEquals(1, groups.size());
    }

    @Test
    public void testLoadListCaseInsensitive() {
        Server server = new Server();
        server.setName("LocalHost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext<Server> loadContext = LoadContext.create(Server.class);
        loadContext.setQueryString("select s from sys$Server s where s.name like :name")
                .setParameter("name", "(?i)%loc%host%");

        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testLoadListCaseInsensitiveLower() {
        Server server = new Server();
        server.setName("LocalHost");
        server.setRunning(true);

        DataManager dataManager = AppBeans.get(DataManager.NAME);
        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext<Server> loadContext = LoadContext.create(Server.class);
        loadContext.setQueryString("select s from sys$Server s where s.name like :name")
                .setParameter("name", "(?i)%localhost%");

        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testUnexistingQueryParameters() throws Exception {
        LoadContext<User> loadContext = LoadContext.create(User.class).setQuery(
                LoadContext.createQuery("select u from sec$User u where u.login = :login").setParameter("name", "admin"));

        try {
            dataManager.loadList(loadContext);
            fail("DataService must throw exception for nonexistent parameters");
        } catch (Exception e) {
            // ok
        }

        loadContext = LoadContext.create(User.class).setQuery(
                LoadContext.createQuery("select u from sec$User u where u.login = :login").setParameter("login", "admin"));
        List<User> list = dataManager.loadList(loadContext);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetCount() throws Exception {
        LoadContext<User> loadContext = LoadContext.create(User.class).setQuery(
                LoadContext.createQuery("select u from sec$User u where u.login = :login").setParameter("login", "admin"));

        long count = dataManager.getCount(loadContext);
        assertEquals(1, count);

        loadContext.getQuery().setParameter("login", "cc1aa09f-c5d5-4bd1-896c-cb774d2e2898");
        count = dataManager.getCount(loadContext);
        assertEquals(0, count);
    }

    @Test
    public void testTemporalType() throws Exception {
        Date nextYear = DateUtils.addYears(AppBeans.get(TimeSource.class).currentTimestamp(), 1);
        LoadContext<User> loadContext = LoadContext.create(User.class).setQuery(
                LoadContext.createQuery("select u from sec$User u where u.createTs = :ts")
                        .setParameter("ts", nextYear, TemporalType.DATE));

        List<User> users = dataManager.loadList(loadContext);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testExtendedLoadContext() throws Exception {
        LoadContext<User> loadContext = new MyLoadContext<>(User.class, "test").setQuery(
                LoadContext.createQuery("select u from sec$User u where u.login = :login").setParameter("login", "admin"));

        long count = dataManager.getCount(loadContext);
        assertEquals(1, count);

    }

    @Test
    public void testReloadWithDynamicAttributes() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext<Server> loadContext = LoadContext.create(Server.class).setId(id).setLoadDynamicAttributes(true);
        server = dataManager.load(loadContext);
        assertNotNull(server.getDynamicAttributes());
        server = dataManager.reload(server, View.LOCAL);
        assertNotNull(server.getDynamicAttributes());

        loadContext = LoadContext.create(Server.class).setId(id).setLoadDynamicAttributes(false);
        server = dataManager.load(loadContext);
        assertNull(server.getDynamicAttributes());

        loadContext = LoadContext.create(Server.class).setLoadDynamicAttributes(true);
        loadContext.setQueryString("select s from sys$Server s where s.id = :id")
                .setParameter("id", id);
        List<Server> resultList = dataManager.loadList(loadContext);
        assertNotNull(resultList.get(0).getDynamicAttributes());
    }

    @Test
    public void testDataManagerLoadOneRecord() {
        Server server1 = new Server();
        server1.setName("app1");
        server1.setRunning(false);

        Server server2 = new Server();
        server2.setName("app2");
        server2.setRunning(false);

        dataManager.commit(new CommitContext(server1, server2));

        LoadContext<Server> lc = new LoadContext<>(Server.class);
        lc.setQueryString("select s from sys$Server s order by s.name")
                .setMaxResults(1);

        Server latest = dataManager.load(lc);
        assertEquals(server1, latest);
    }

    @Test
    public void testNonEntityResults() throws Exception {
        // fails on aggregates
        LoadContext context = LoadContext.create(Server.class).setQuery(LoadContext.createQuery("select count(s) from sys$Server s"));
        try {
            dataManager.load(context);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof DevelopmentException);
        }

        // fails on single attributes
        context = LoadContext.create(Server.class).setQuery(LoadContext.createQuery("select s.name from sys$Server s"));
        try {
            dataManager.load(context);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof DevelopmentException);
        }
    }

    public static class MyLoadContext<E extends Entity> extends LoadContext<E> {

        private String info;

        public MyLoadContext() {
        }

        public MyLoadContext(Class<E> javaClass, String info) {
            super(javaClass);
            this.info = info;
        }

        @Override
        public MyLoadContext<?> copy() {
            MyLoadContext<?> copy = (MyLoadContext) super.copy();
            copy.info = info;
            return copy;
        }
    }
}