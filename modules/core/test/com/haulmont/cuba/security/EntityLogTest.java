/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.app.EntityLogAPI;
import com.haulmont.cuba.security.entity.*;

import java.util.List;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityLogTest extends CubaTestCase {

    private UUID leId;
    private UUID laId;

    private UUID userId;

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
            leId = le.getId();
            le.setName("sec$User");
            le.setAuto(true);
            em.persist(le);

            LoggedAttribute la = new LoggedAttribute();
            laId = la.getId();
            la.setEntity(le);
            la.setName("email");
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

    protected void tearDown() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query q = em.createNativeQuery("delete from SEC_LOGGED_ATTR where ID = ?");
            q.setParameter(1, laId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_LOGGED_ENTITY where ID = ?");
            q.setParameter(1, leId.toString());
            q.executeUpdate();
            
            q = em.createNativeQuery("delete from SEC_USER where ID = ?");
            q.setParameter(1, userId.toString());
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
        super.tearDown();
    }
}
