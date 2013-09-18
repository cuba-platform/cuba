/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class TypedNativeQueryTest extends CubaTestCase {

    private UUID groupId, userId;

    protected void tearDown() throws Exception {
        if (userId != null) {
            deleteRecord("SEC_USER", userId);
        }
        if (groupId != null) {
            deleteRecord("SEC_GROUP", groupId);
        }

        super.tearDown();
    }

    /*
     * Test that entity which is loaded by native typed query, is MANAGED,
     * by changing loaded entity attribute.
     */
    public void testTypedNativeQueryByChangingAttribute() {
        Group group = new Group();
        groupId = group.getId();
        group.setName("Old Name");
        Transaction tx = persistence.createTransaction();
        try {
            persistence.getEntityManager().persist(group);
            tx.commit();
        } finally {
            tx.end();
        }

        // load with native query, change attribute
        String nativeQuery = "select ID, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, NAME from SEC_GROUP where ID = ?";
        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<Group> q = em.createNativeQuery(nativeQuery, Group.class);
            q.setParameter(1, group.getId().toString());

            Group g = q.getResultList().get(0);

            g.setName("New Name");
            tx.commit();
        } finally {
            tx.end();
        }

        // load again, check
        Group g2;
        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            g2 = em.find(Group.class, group.getId());

            assertNotNull(g2);
            assertEquals("New Name", g2.getName());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    /*
     * Test that entity which is loaded by native typed query,
     * is MANAGED, by persisting another entity linked to it.
     */
    public void testTypedNativeQueryByPersistingAnotherEntity() {
        Group group = new Group();
        groupId = group.getId();
        group.setName("Old Name");
        Transaction tx = persistence.createTransaction();
        try {
            persistence.getEntityManager().persist(group);
            tx.commit();
        } finally {
            tx.end();
        }

        String nativeQuery = "select ID, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, VERSION, NAME from SEC_GROUP where ID = ?";
        tx = persistence.createTransaction();
        Group g;
        try {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<Group> q = em.createNativeQuery(nativeQuery, Group.class);
            q.setParameter(1, group.getId().toString());
            g = q.getResultList().get(0);
            tx.commit();
        } finally {
            tx.end();
        }

        User user = new User();
        userId = user.getId();
        user.setLogin("typednativesqlquery");
        user.setGroup(g);
        user.setName("Test");

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.persist(user);
            tx.commit();
        } finally {
            tx.end();
        }
        // gets persisted without error
    }

}
