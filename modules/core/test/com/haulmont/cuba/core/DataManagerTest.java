/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DataManagerTest extends CubaTestCase {
    
    protected DataManager dataManager;

    public void setUp() throws Exception {
        super.setUp();
        dataManager = AppBeans.get(DataManager.class);

        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from SYS_SERVER");
    }

    public void test() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        final LoadContext<Server> loadContext = new LoadContext<>(Server.class);
        loadContext.setId(id);

        server = dataManager.load(loadContext);
        assertEquals("localhost", server.getName());

        server.setName("krivopustov");
        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));
    }

    public void testLoad() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        final LoadContext<Server> loadContext = new LoadContext<>(Server.class);
        loadContext.setId(id);

        server = dataManager.load(loadContext);
        assertEquals("localhost", server.getName());
    }

    public void testLoadList() {
        Server server = new Server();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        final LoadContext loadContext =
                new LoadContext(Server.class);
        loadContext.setQueryString("select s from " + PersistenceHelper.getEntityName(Server.class) + " s");
        
        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() > 0);
    }

    public void testLoadListById() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext loadContext = new LoadContext(Server.class);
        loadContext.setId(id);

        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() == 1);
    }

    public void testAssociatedResult() throws Exception {
        LoadContext loadContext = new LoadContext(Group.class);
        loadContext.setQueryString("select u.group from sec$User u where u.id = :userId")
                .setParameter("userId", UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() == 1);
    }

    public void testLoadListCaseInsensitive() {
        Server server = new Server();
        server.setName("LocalHost");
        server.setRunning(true);

        dataManager.commit(new CommitContext(Collections.<Entity>singleton(server)));

        final LoadContext loadContext =
                new LoadContext(Server.class);
        loadContext.setQueryString("select s from sys$Server s where s.name like :name")
                .setParameter("name", "(?i)%loc%host%");

        List<Server> list = dataManager.loadList(loadContext);
        assertTrue(list.size() > 0);
    }

    public void testUnexistingQueryParameters() throws Exception {
        LoadContext loadContext = new LoadContext(Server.class);
        loadContext.setQueryString("select u from sec$User u where u.login = :login")
                .setParameter("name", "admin");

        try {
            dataManager.loadList(loadContext);
            fail("DataService must throw exception for nonexistent parameters");
        } catch (Exception e) {
            // ok
        }

        loadContext = new LoadContext(Server.class);
        loadContext.setQueryString("select u from sec$User u where u.login = :login")
                .setParameter("login", "admin");
        List<Entity> list = dataManager.loadList(loadContext);
        assertEquals(1, list.size());
    }
}
