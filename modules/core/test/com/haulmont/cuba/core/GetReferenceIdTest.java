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
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class GetReferenceIdTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testWithFetchGroup() throws Exception {
        User user = null;

        // not in a view
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();

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

                PersistenceTools.RefId refId = cont.persistence().getTools().getReferenceId(user, "group");
                assertFalse(refId.isLoaded());
                try {
                    refId.getValue();
                    fail();
                } catch (IllegalStateException e) {
                    // ok
                }
            }
            tx.commit();
        }
        user = reserialize(user);
        assertNotNull(user);
        assertNotNull(user.getUserRoles());
        user.getUserRoles().size();

        // in a view
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();

            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setView(
                    new View(User.class, false)
                            .addProperty("login")
                            .addProperty("group", new View(Group.class)
                                    .addProperty("name"))
                            .addProperty("userRoles", new View(UserRole.class)
                                    .addProperty("role", new View(Role.class)
                                            .addProperty("name")))
            );
            q.setParameter(1, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);

                PersistenceTools.RefId refId = cont.persistence().getTools().getReferenceId(user, "group");
                assertTrue(refId.isLoaded());
                assertEquals(TestSupport.COMPANY_GROUP_ID, refId.getValue());
            }
            tx.commit();
        }
        user = reserialize(user);
        assertNotNull(user);
        assertNotNull(user.getUserRoles());
        user.getUserRoles().size();

    }

    @Test
    public void testWithoutFetchGroup() throws Exception {
        User user = null;

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();

            TypedQuery<User> q = em.createQuery("select u from sec$User u where u.id = ?1", User.class);
            q.setParameter(1, TestSupport.ADMIN_USER_ID);
            List<User> list = q.getResultList();
            if (!list.isEmpty()) {
                user = list.get(0);

                PersistenceTools.RefId refId = cont.persistence().getTools().getReferenceId(user, "group");
                assertTrue(refId.isLoaded());
                assertEquals(TestSupport.COMPANY_GROUP_ID, refId.getValue());
            }
            tx.commit();
        }
        try {
            cont.persistence().getTools().getReferenceId(user, "group");
            fail();
        } catch (IllegalStateException e) {
            // ok
        }
        user = reserialize(user);
        assertNotNull(user);
        try {
            cont.persistence().getTools().getReferenceId(user, "group");
            fail();
        } catch (IllegalStateException e) {
            // ok
        }
    }
}
