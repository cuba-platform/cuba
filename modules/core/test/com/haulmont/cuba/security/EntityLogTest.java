/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.app.EntityLogAPI;
import com.haulmont.cuba.security.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityLogTest extends CubaTestCase {

    private List<UUID> leId = new ArrayList<>();
    private List<UUID> laId = new ArrayList<>();

    private UUID userId;
    private UUID roleId;

    protected void setUp() throws Exception {
        super.setUp();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query q = em.createNativeQuery("delete from SEC_ENTITY_LOG_ATTR");
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_ENTITY_LOG");
            q.executeUpdate();

            LoggedEntity le = new LoggedEntity();
            leId.add(le.getId());
            le.setName("sec$User");
            le.setAuto(true);
            em.persist(le);

            LoggedAttribute la = new LoggedAttribute();
            laId.add(la.getId());
            la.setEntity(le);
            la.setName("email");
            em.persist(la);

            le = new LoggedEntity();
            leId.add(le.getId());
            le.setName("sec$Role");
            le.setAuto(true);
            em.persist(le);

            la = new LoggedAttribute();
            laId.add(la.getId());
            la.setEntity(le);
            la.setName("type");
            em.persist(la);

            tx.commit();
        } finally {
            tx.end();
        }

        EntityLogAPI entityLog = AppBeans.get(EntityLogAPI.NAME);
        entityLog.invalidateCache();
    }

    public void test() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = em.find(User.class, userId);
            user.setEmail("test-email");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = em.find(User.class, userId);
            user.setName("test-name-1");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = em.find(User.class, userId);
            user.setEmail("test-email-1");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = em.find(User.class, userId);
            em.remove(user);

            tx.commit();
        } finally {
            tx.end();
        }

        List<EntityLogItem> items;
        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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

    public void testEnumDisplayValue() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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
        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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

    protected void tearDown() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q;

            for (UUID uuid : laId) {
                q = em.createNativeQuery("delete from SEC_LOGGED_ATTR where ID = ?");
                q.setParameter(1, uuid.toString());
                q.executeUpdate();
            }

            for (UUID uuid : leId) {
                q = em.createNativeQuery("delete from SEC_LOGGED_ENTITY where ID = ?");
                q.setParameter(1, uuid.toString());
                q.executeUpdate();
            }

            if (userId != null) {
                q = em.createNativeQuery("delete from SEC_USER where ID = ?");
                q.setParameter(1, userId.toString());
                q.executeUpdate();
            }

            if (roleId != null) {
                q = em.createNativeQuery("delete from SEC_ROLE where ID = ?");
                q.setParameter(1, roleId.toString());
                q.executeUpdate();
            }

            tx.commit();
        } finally {
            tx.end();
        }
        super.tearDown();
    }
}
