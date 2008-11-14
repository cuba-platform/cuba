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

import com.haulmont.cuba.core.intf.BasicService;
import com.haulmont.cuba.core.intf.BasicInvocationContext;
import com.haulmont.cuba.core.intf.BasicServiceRemote;
import com.haulmont.cuba.core.entity.Server;

import java.util.UUID;
import java.util.List;

public class BasicServiceTest extends CubaTestCase
{
    public void test() {
        BasicService bs = Locator.lookupLocal(BasicService.JNDI_NAME);

        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setAddress("127.0.0.1");
        server.setRunning(true);

        bs.create(server);

        server = bs.get(new BasicInvocationContext().setEntityClass(Server.class).setId(id));
        assertEquals("localhost", server.getName());

        server.setName("krivopustov");
        bs.update(server);
    }

    public void testRemoteWithException() {
        BasicServiceRemote bs = Locator.lookupRemote(BasicService.JNDI_NAME);

        Object id = "some key";
        try {
            bs.get(new BasicInvocationContext().setEntityClass(Server.class).setId(id));
            fail();
        } catch (Exception e) {
            System.out.println("Done");
        }

    }

    public void testLoad() {
        BasicService bs = Locator.lookupLocal(BasicService.JNDI_NAME);

        Server server = new Server();
        UUID id = server.getId();
        server.setName("localhost");
        server.setAddress("127.0.0.1");
        server.setRunning(true);

        bs.create(server);

        server = bs.load(new BasicInvocationContext().setEntityClass(Server.class).setId(id));
        assertEquals("localhost", server.getName());
    }

    public void testLoadList() {
        BasicService bs = Locator.lookupLocal(BasicService.JNDI_NAME);

        Server server = new Server();
        server.setName("localhost");
        server.setAddress("127.0.0.1");
        server.setRunning(true);

        bs.create(server);

        BasicInvocationContext ctx = new BasicInvocationContext()
                .setEntityClass(Server.class)
                .setQueryString("select s from " + PersistenceProvider.getEntityName(Server.class) + " s");
        List<Server> list = bs.loadList(ctx);
        assertTrue(list.size() > 0);
    }
}
