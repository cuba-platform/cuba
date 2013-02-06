/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.01.2009 9:12:24
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.List;
import java.util.UUID;

public class QueryTest extends CubaTestCase
{
    private UUID userId;
    private UUID groupId;

    protected void setUp() throws Exception {
        super.setUp();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            Group group = new Group();
            groupId = group.getId();
            group.setName("testGroup");
            em.persist(group);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void tearDown() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query q;

            q = em.createNativeQuery("delete from SEC_USER where ID = ?");
            q.setParameter(1, userId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_GROUP where ID = ?");
            q.setParameter(1, groupId.toString());
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
        super.tearDown();
    }

    public void test() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

            Query query = em.createQuery("select r from sec$UserRole r where r.user.id = :user");
            query.setParameter("user", user);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testNullParam() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("select r from sec$UserRole r where r.deleteTs = :dts");
            query.setParameter("dts", null);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testUpdate() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Group group = em.find(Group.class, groupId);

            Query query = em.createQuery("update sec$User u set u.group = :group where u.id = :userId");
            query.setParameter("userId", userId);
            query.setParameter("group", group, false);
            query.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testAssociatedResult() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createQuery("select u.group from sec$User u where u.id = :userId");
            query.setParameter("userId", userId);
            List list = query.getResultList();

            assertFalse(list.isEmpty());

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
