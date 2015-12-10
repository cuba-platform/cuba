/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.app.EntityLogAPI;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityLogTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;
    
    private UUID userId;
    private UUID roleId;

    private EntityLogAPI entityLog;

    @Before
    public void setUp() throws Exception {
        cleanup();

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Query q = em.createNativeQuery("delete from SEC_ENTITY_LOG_ATTR");
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_ENTITY_LOG");
            q.executeUpdate();

            LoggedEntity le = new LoggedEntity();
            le.setName("sec$User");
            le.setAuto(true);
            em.persist(le);

            LoggedAttribute la = new LoggedAttribute();
            la.setEntity(le);
            la.setName("email");
            em.persist(la);

            le = new LoggedEntity();
            le.setName("sec$Role");
            le.setAuto(true);
            em.persist(le);

            la = new LoggedAttribute();
            la.setEntity(le);
            la.setName("type");
            em.persist(la);

            tx.commit();
        } finally {
            tx.end();
        }

        entityLog = AppBeans.get(EntityLogAPI.NAME);
        entityLog.invalidateCache();
    }

    @After
    public void tearDown() throws Exception {
        cleanup();

        if (userId != null)
            cont.deleteRecord("SEC_USER", userId);

        if (roleId != null)
            cont.deleteRecord("SEC_ROLE", roleId);
    }

    private void cleanup() throws SQLException {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SEC_LOGGED_ATTR");
        runner.update("delete from SEC_LOGGED_ENTITY");
    }

    @Test
    public void test() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            User user = new User();
            userId = user.getId();
            user.setGroup(group);
            user.setLogin("test");
            user.setName("test-name");
            em.persist(user);

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, userId);
            user.setEmail("test-email");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, userId);
            user.setName("test-name-1");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, userId);
            user.setEmail("test-email-1");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, userId);
            em.remove(user);

            tx.commit();
        } finally {
            tx.end();
        }

        List<EntityLogItem> items;
        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            TypedQuery<EntityLogItem> query = em.createQuery(
                    "select i from sec$EntityLog i where i.entity = ?1 and i.entityId = ?2", EntityLogItem.class);
            query.setParameter(1, "sec$User");
            query.setParameter(2, userId);
            items = query.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }
        assertNotNull(items);
        assertEquals(4, items.size());
        assertNotNull(items.get(0).getAttributes());
        assertEquals(1, items.get(0).getAttributes().size());
    }

    @Test
    public void testEnumDisplayValue() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Role role = new Role();
            roleId = role.getId();
            role.setName("role1");
            role.setType(RoleType.READONLY);
            em.persist(role);

            tx.commit();
        } finally {
            tx.end();
        }

        List<EntityLogItem> items;
        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            TypedQuery<EntityLogItem> query = em.createQuery(
                    "select i from sec$EntityLog i where i.entity = ?1 and i.entityId = ?2", EntityLogItem.class);
            query.setParameter(1, "sec$Role");
            query.setParameter(2, roleId);
            items = query.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }
        assertNotNull(items);
        assertEquals(1, items.size());
        assertNotNull(items.get(0).getAttributes());
        assertEquals(1, items.get(0).getAttributes().size());

        EntityLogAttr attr = items.get(0).getAttributes().iterator().next();

        Messages messages = AppBeans.get(Messages.NAME);
        assertEquals(messages.getMessage(RoleType.READONLY), attr.getDisplayValue());
    }

    @Test
    public void testMultipleFlush() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            User user = new User();
            userId = user.getId();
            user.setGroup(group);
            user.setLogin("test");
            user.setName("test-name");
            user.setEmail("name@test.com");
            em.persist(user);

            em.flush();

            user.setEmail("changed-name@test.com");

            tx.commit();
        } finally {
            tx.end();
        }

        List<EntityLogItem> items = getEntityLogItems();
        assertNotNull(items);
        assertEquals(1, items.size());

        EntityLogItem item = items.get(0);
        assertEquals(EntityLogItem.Type.CREATE, item.getType());

        EntityLogAttr attr = Iterables.find(item.getAttributes(), new Predicate<EntityLogAttr>() {
            @Override
            public boolean apply(EntityLogAttr attr) {
                return "email".equals(attr.getName());
            }
        });
        assertEquals("changed-name@test.com", attr.getValue());

        ////////

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = em.find(User.class, userId);
            user.setEmail("changed-2@test.com");

            em.flush();

            user.setEmail("changed-3@test.com");

            tx.commit();
        } finally {
            tx.end();
        }

        items = getEntityLogItems();
        assertNotNull(items);
        assertEquals(2, items.size());

        item = items.get(0); // the last because of sorting in query
        assertEquals(EntityLogItem.Type.MODIFY, item.getType());

        attr = Iterables.find(item.getAttributes(), new Predicate<EntityLogAttr>() {
            @Override
            public boolean apply(EntityLogAttr attr) {
                return "email".equals(attr.getName());
            }
        });
        assertEquals("changed-3@test.com", attr.getValue());
    }

    private List<EntityLogItem> getEntityLogItems() {
        Transaction tx;
        List<EntityLogItem> items;
        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            TypedQuery<EntityLogItem> query = em.createQuery(
                    "select i from sec$EntityLog i where i.entity = ?1 and i.entityId = ?2 order by i.eventTs desc", EntityLogItem.class);
            query.setParameter(1, "sec$User");
            query.setParameter(2, userId);
            items = query.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }
        return items;
    }
}
