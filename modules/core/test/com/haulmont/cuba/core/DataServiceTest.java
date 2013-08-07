/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 14:25:43
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.security.entity.Group;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DataServiceTest extends CubaTestCase {
    
    protected DataService dataService;

    public void setUp() throws Exception {
        super.setUp();
        dataService = AppBeans.get(DataService.class);
    }

    public void test() {
        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataService.commit(new CommitContext(Collections.<Entity>singleton(server)));

        final LoadContext loadContext = new LoadContext(Server.class);
        loadContext.setId(id);

        server = dataService.load(loadContext);
        assertEquals("localhost", server.getName());

        server.setName("krivopustov");
        dataService.commit(new CommitContext(Collections.<Entity>singleton(server)));
    }

    public void testLoad() {
        DataService dataService = Locator.lookup(DataService.NAME);

        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataService.commit(new CommitContext(Collections.<Entity>singleton(server)));

        final LoadContext loadContext = new LoadContext(Server.class);
        loadContext.setId(id);

        server = dataService.load(loadContext);
        assertEquals("localhost", server.getName());
    }

    public void testLoadList() {
        DataService dataService = Locator.lookup(DataService.NAME);

        Server server = new Server();
        server.setName("localhost");
        server.setRunning(true);

        dataService.commit(new CommitContext(Collections.<Entity>singleton(server)));

        final LoadContext loadContext =
                new LoadContext(Server.class);
        loadContext.setQueryString("select s from " + PersistenceHelper.getEntityName(Server.class) + " s");
        
        List<Server> list = dataService.loadList(loadContext);
        assertTrue(list.size() > 0);
    }

    public void testLoadListById() {
        DataService dataService = Locator.lookup(DataService.NAME);

        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setRunning(true);

        dataService.commit(new CommitContext(Collections.<Entity>singleton(server)));

        LoadContext loadContext = new LoadContext(Server.class);
        loadContext.setId(id);

        List<Server> list = dataService.loadList(loadContext);
        assertTrue(list.size() == 1);
    }

    public void testAssociatedResult() throws Exception {
        LoadContext loadContext = new LoadContext(Group.class);
        loadContext.setQueryString("select u.group from sec$User u where u.id = :userId")
                .setParameter("userId", UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

        // here should be a warning or something
        List<Server> list = dataService.loadList(loadContext);
        assertTrue(list.size() == 1);
    }
}
