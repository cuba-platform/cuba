/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import java.util.List;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UnfetchedAttributeTest extends CubaTestCase {

    private Group group;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        group = new Group();
        group.setName("Some group");
        Transaction tx = persistence.createTransaction();
        try {
            persistence.getEntityManager().persist(group);
            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testGet() throws Exception {
        User user = null;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setView(
                    new View(User.class, false)
                            .addProperty("login")
                            .addProperty("userRoles", new View(UserRole.class)
                                    .addProperty("role", new View(Role.class)
                                            .addProperty("name")))
            );
            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);

                try {
                    user.getGroup();
                    fail();
                } catch (IllegalStateException ignored) {
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);
        assertNotNull(user);
        assertNotNull(user.getUserRoles());
        user.getUserRoles().size();
    }

    public void testSet() throws Exception {
        User user = null;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setView(
                    new View(User.class, false)
                            .addProperty("login")
                            .addProperty("userRoles", new View(UserRole.class)
                                    .addProperty("role", new View(Role.class)
                                            .addProperty("name")))
            );
            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);
                try {
                    user.setGroup(group);
                    fail();
                } catch (IllegalStateException ignored) {
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);
        assertNotNull(user);
        assertNotNull(user.getUserRoles());
        user.getUserRoles().size();
    }
}
