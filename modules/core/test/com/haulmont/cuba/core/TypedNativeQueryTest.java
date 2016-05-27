/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypedNativeQueryTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID groupId, userId;

    @After
    public void tearDown() throws Exception {
        if (userId != null) {
            cont.deleteRecord("SEC_USER", userId);
        }
        if (groupId != null) {
            cont.deleteRecord("SEC_GROUP", groupId);
        }
    }

    /*
     * Test that entity which is loaded by native typed query, is MANAGED,
     * by changing loaded entity attribute.
     */
    @Test
    public void testTypedNativeQueryByChangingAttribute() {
        Group group = new Group();
        groupId = group.getId();
        group.setName("Old Name");
        Transaction tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager().persist(group);
            tx.commit();
        } finally {
            tx.end();
        }

        // load with native query, change attribute
        String nativeQuery = "select ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, NAME from SEC_GROUP where ID = ?";
        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

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
        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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
    @Test
    public void testTypedNativeQueryByPersistingAnotherEntity() {
        Group group = new Group();
        groupId = group.getId();
        group.setName("Old Name");
        Transaction tx = cont.persistence().createTransaction();
        try {
            cont.persistence().getEntityManager().persist(group);
            tx.commit();
        } finally {
            tx.end();
        }

        String nativeQuery = "select ID, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, VERSION, NAME from SEC_GROUP where ID = ?";
        tx = cont.persistence().createTransaction();
        Group g;
        try {
            EntityManager em = cont.persistence().getEntityManager();

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

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            em.persist(user);
            tx.commit();
        } finally {
            tx.end();
        }
        // gets persisted without error
    }
}