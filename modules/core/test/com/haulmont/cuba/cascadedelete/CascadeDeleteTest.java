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
 */

package com.haulmont.cuba.cascadedelete;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.testmodel.cascadedelete.CascadeEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CascadeDeleteTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected CascadeEntity root, first, second, third;

    @Before
    public void setUp() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Metadata metadata = cont.metadata();

            root =  metadata.create(CascadeEntity.class);
            root.setName("root");
            em.persist(root);

            first =  metadata.create(CascadeEntity.class);
            first.setName("first");
            first.setFather(root);
            em.persist(first);

            second =  metadata.create(CascadeEntity.class);
            second.setName("second");
            second.setFather(first);
            em.persist(second);

            third =  metadata.create(CascadeEntity.class);
            third.setName("third");
            third.setFather(second);
            em.persist(third);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord(third, second, first, root);
    }

    @Test
    public void testRemoveCascade() throws Exception {
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            CascadeEntity loadedRoot = em.find(CascadeEntity.class, root.getId());
            em.find(CascadeEntity.class, first.getId());
            em.remove(loadedRoot);
            tx.commit();
        }

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            List r = em.createQuery("select e from test$CascadeEntity e").getResultList();
            assertEquals(0, r.size());
            tx.commit();
        }
    }

    @Test
    public void testEntityListenerOnCascadeDelete() throws Exception {
        EntityListenerManager entityListenerManager = AppBeans.get(EntityListenerManager.class);
        entityListenerManager.addListener(CascadeEntity.class, DeleteCascadeEntityListener.class);
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            CascadeEntity loadedSecond = em.find(CascadeEntity.class, second.getId());
            em.find(CascadeEntity.class, third.getId());
            em.remove(loadedSecond);
            tx.commit();
        }
        entityListenerManager.removeListener(CascadeEntity.class, DeleteCascadeEntityListener.class);
        assertEquals(2, DeleteCascadeEntityListener.deletedEvents.size());
        assertTrue(DeleteCascadeEntityListener.deletedEvents.contains(second.getId().toString()));
        assertTrue(DeleteCascadeEntityListener.deletedEvents.contains(third.getId().toString()));
    }

    @Test
    public void testEntityListenerOnUpdate() throws Exception {
        EntityListenerManager entityListenerManager = AppBeans.get(EntityListenerManager.class);
        entityListenerManager.addListener(CascadeEntity.class, UpdateCascadeEntityListener.class);
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            CascadeEntity loadedThird =  em.find(CascadeEntity.class, third.getId());
            CascadeEntity loadedSecond = em.find(CascadeEntity.class, second.getId());
            loadedThird.setName("third#1");
            tx.commit();
        }
        entityListenerManager.removeListener(CascadeEntity.class, UpdateCascadeEntityListener.class);
        assertEquals(2, UpdateCascadeEntityListener.updatedEvents.size());
        assertTrue(UpdateCascadeEntityListener.updatedEvents.contains(second.getId().toString()));
        assertTrue(UpdateCascadeEntityListener.updatedEvents.contains(third.getId().toString()));
    }
}
