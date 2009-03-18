/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.03.2009 9:51:45
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.LoggedEntity;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.LoggedAttribute;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.app.EntityLogMBean;
import com.haulmont.cuba.security.app.EntityLog;
import com.haulmont.cuba.security.app.EntityLogAPI;

import java.util.UUID;
import java.util.Set;

public class EntityLogTest extends CubaTestCase
{
    private UUID leId;
    private UUID laId;

    private EntityLogMBean mBean;
    private UUID userId;

    protected void setUp() throws Exception {
        super.setUp();
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createNativeQuery("delete from SEC_ENTITY_LOG");
            q.executeUpdate();

            LoggedEntity le = new LoggedEntity();
            leId = le.getId();
            le.setName(User.class.getName());
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

        mBean = Locator.lookupMBean(EntityLogMBean.class, EntityLogMBean.OBJECT_NAME);
        mBean.invalidateCache();
    }

    public void test() {
//        EntityLogAPI entityLog = mBean.getAPI();
//
//        Set<String> attributes = entityLog.getLoggedAttributes(User.class.getName(), true);
//        assertNotNull(attributes);
//        assertTrue(attributes.contains("email"));

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = em.find(User.class, userId);
            user.setEmail("test-email");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = em.find(User.class, userId);
            user.setName("test-name-1");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = em.find(User.class, userId);
            user.setEmail("test-email-1");

            tx.commit();
        } finally {
            tx.end();
        }

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = em.find(User.class, userId);
            em.remove(user);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void tearDown() throws Exception {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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
