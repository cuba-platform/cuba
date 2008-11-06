/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 20:50:16
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Server;

public class PersistenceTest extends CubaTestCase
{
    public void test() {
        EntityManagerAdapter em = Locator.getEntityManager();
        assertNotNull(em);
        Server server = new Server();
        server.setName("localhost");
        server.setAddress("127.0.0.1");
        server.setRunning(true);
        em.persist(server);
    }
}
