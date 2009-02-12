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
import com.haulmont.cuba.core.global.DataServiceRemote;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BasicServiceTest extends CubaTestCase
{
    public void test() {
        DataService bs = Locator.lookupLocal(DataService.JNDI_NAME);

        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setAddress("127.0.0.1");
        server.setRunning(true);

        bs.commit(new DataServiceRemote.CommitContext<Entity>(Collections.<Entity>singleton(server)));

        final DataServiceRemote.LoadContext loadContext = new DataServiceRemote.LoadContext(Server.class);
        loadContext.setId(id);

        server = bs.load(loadContext);
        assertEquals("localhost", server.getName());

        server.setName("krivopustov");
        bs.commit(new DataServiceRemote.CommitContext<Entity>(Collections.<Entity>singleton(server)));
    }

//    public void testRemoteWithException() {
//        DataServiceRemote bs = Locator.lookupRemote(DataService.JNDI_NAME);
//
//        Object id = "some key";
//        try {
//            bs.get(new BasicInvocationContext().setEntityClass(Server.class).setId(id));
//            fail();
//        } catch (Exception e) {
//            System.out.println("Done");
//        }
//    }

    public void testLoad() {
        DataService bs = Locator.lookupLocal(DataService.JNDI_NAME);

        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setAddress("127.0.0.1");
        server.setRunning(true);

        bs.commit(new DataServiceRemote.CommitContext<Entity>(Collections.<Entity>singleton(server)));

        final DataServiceRemote.LoadContext loadContext = new DataServiceRemote.LoadContext(Server.class);
        loadContext.setId(id);

        server = bs.load(loadContext);
        assertEquals("localhost", server.getName());
    }

    public void testLoadList() {
        DataService bs = Locator.lookupLocal(DataService.JNDI_NAME);

        Server server = new Server();
        server.setName("localhost");
        server.setAddress("127.0.0.1");
        server.setRunning(true);

        bs.commit(new DataServiceRemote.CommitContext<Entity>(Collections.<Entity>singleton(server)));

        final DataServiceRemote.CollectionLoadContext loadContext =
                new DataServiceRemote.CollectionLoadContext(Server.class);
        loadContext.setQueryString("select s from " + PersistenceProvider.getEntityName(Server.class) + " s");
        
        List<Server> list = bs.loadList(loadContext);
        assertTrue(list.size() > 0);
    }
}
