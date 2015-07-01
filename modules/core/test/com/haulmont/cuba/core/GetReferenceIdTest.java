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
public class GetReferenceIdTest extends CubaTestCase {

    public void test() throws Exception {
        // todo EL
//        User user = null;
//
//        Transaction tx = persistence.createTransaction();
//        try {
//            EntityManager em = persistence.getEntityManager();
//
//            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
//            q.setView(
//                    new View(User.class, false)
//                            .addProperty("login")
//                            .addProperty("userRoles", new View(UserRole.class)
//                                    .addProperty("role", new View(Role.class)
//                                            .addProperty("name")))
//            );
//            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
//            List<User> list = q.getResultList();
//            if (!list.isEmpty()) {
//                user = list.get(0);
//
//                UUID id = persistence.getTools().getReferenceId(user, "group");
//                System.out.println(id);
//            }
//
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//        user = reserialize(user);
//        assertNotNull(user);
//        assertNotNull(user.getUserRoles());
//        user.getUserRoles().size();
    }

    // Pre-6.0 test
    public void testLoadReferencedEntity() throws Exception {
        // todo EL
//        User user = null;
//
//        Transaction tx = persistence.createTransaction();
//        try {
//            EntityManager em = persistence.getEntityManager();
//
//            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
//            q.setView(
//                    new View(User.class, false)
//                            .addProperty("login")
//            );
//            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
//            List<User> list = q.getResultList();
//            if (!list.isEmpty()) {
//                user = list.get(0);
//
//                UUID id = persistence.getTools().getReferenceId(user, "group");
//                System.out.println(id);
//            }
//
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//
//        try {
//            persistence.getTools().getReferenceId(user, "group");
//            fail();
//        } catch (Exception e) {
//            // ok
//        }
//
//        tx = persistence.createTransaction();
//        try {
//            EntityManager em = persistence.getEntityManager();
//
//            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
//            q.setView(
//                    new View(User.class, false)
//                            .addProperty("login")
//                            .addProperty("group",
//                                    new View(Group.class, false).addProperty("id")
//                            )
//            );
//            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
//            List<User> list = q.getResultList();
//            if (!list.isEmpty()) {
//                user = list.get(0);
//
//                UUID id = persistence.getTools().getReferenceId(user, "group");
//                System.out.println(id);
//            }
//
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//
//        /*
//         * test Persistence.getReferenceId() when field value is NULL
//         * (ticket XXX)
//         */
//        // create user without group
//        User userWithoutGroup = new User();
//        userWithoutGroup.setLogin("ForeverAlone");
//
//        // save to DB
//        tx = persistence.createTransaction();
//        try {
//            EntityManager em = persistence.getEntityManager();
//            em.persist(userWithoutGroup);
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//
//        // test method
//        try {
//            tx = persistence.createTransaction();
//            try {
//                EntityManager em = persistence.getEntityManager();
//                User reloadedUser = em.find(User.class, userWithoutGroup.getId(),
//                        new View(User.class).addProperty("login"));
//
//                UUID groupId = persistence.getTools().getReferenceId(reloadedUser, "group");
//
//                assertNull(groupId);
//
//                tx.commit();
//            } finally {
//                tx.end();
//            }
//        } catch (IllegalArgumentException e) {
//            fail(e.getMessage());
//        }
    }
}
