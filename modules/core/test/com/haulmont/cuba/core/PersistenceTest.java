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
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.List;
import java.util.UUID;

public class PersistenceTest extends CubaTestCase
{
    public void test() {
        UUID id;
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            assertNotNull(em);
            Server server = new Server();
            id = server.getId();
            server.setName("localhost");
            server.setAddress("127.0.0.1");
            server.setRunning(true);
            em.persist(server);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = em.find(Server.class, id);
            assertEquals(id, server.getId());

            server.setAddress("222");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Server server = em.find(Server.class, id);
            assertEquals(id, server.getId());

            em.remove(server);
            
            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void raiseException() {
        throw new RuntimeException("test_ex");
    }

    public void testLoadReferencedEntity() throws Exception {
        User user = null;

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(
                    new View(User.class, false)
                            .addProperty("login")
            );
            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);

                UUID id = PersistenceProvider.getReferenceId(user, "group");
                System.out.println(id);
            }

            tx.commit();
        } finally {
            tx.end();
        }

        try {
            PersistenceProvider.getReferenceId(user, "group");
            fail();
        } catch (Exception e) {
            // ok
        }

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(
                    new View(User.class, false)
                            .addProperty("login")
                            .addProperty("group",
                                    new View(Group.class, false).addProperty("id")
                            )
            );
            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);

                UUID id = PersistenceProvider.getReferenceId(user, "group");
                System.out.println(id);
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
