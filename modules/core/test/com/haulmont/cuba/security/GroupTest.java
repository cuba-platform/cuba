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
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.GroupHierarchy;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class GroupTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID rootId, group1Id, group2Id, group3Id;

    private void createGroups() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Group root = new Group();
            rootId = root.getId();
            root.setName("root");
            em.persist(root);
            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            root = em.find(Group.class, rootId);
            Group group1 = new Group();
            group1Id = group1.getId();
            group1.setName("group1");
            group1.setParent(root);
            em.persist(group1);
            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            group1 = em.find(Group.class, group1Id);
            Group group2 = new Group();
            group2Id = group2.getId();
            group2.setName("group2");
            group2.setParent(group1);
            em.persist(group2);
            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            root = em.find(Group.class, rootId);
            Group group3 = new Group();
            group3Id = group3.getId();
            group3.setName("group3");
            group3.setParent(root);
            em.persist(group3);
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testNew() {
        createGroups();

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Group group2 = em.find(Group.class, group2Id);
            for (GroupHierarchy hierarchy : group2.getHierarchyList()) {
                assertEquals(group2, hierarchy.getGroup());
                if (hierarchy.getLevel() == 0)
                    assertEquals(rootId, hierarchy.getParent().getId());
                else if (hierarchy.getLevel() == 1)
                    assertEquals(group1Id, hierarchy.getParent().getId());
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testUpdate() {
        createGroups();

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Group group1 = em.find(Group.class, group1Id);
            Group group3 = em.find(Group.class, group3Id);
            group1.setParent(group3);
            tx.commitRetaining();

            em = cont.persistence().getEntityManager();

            group1 = em.find(Group.class, group1Id);
            for (GroupHierarchy hierarchy : group1.getHierarchyList()) {
                assertEquals(group1, hierarchy.getGroup());
                if (hierarchy.getLevel() == 0)
                    assertEquals(rootId, hierarchy.getParent().getId());
                else if (hierarchy.getLevel() == 1)
                    assertEquals(group3Id, hierarchy.getParent().getId());
            }

            Group group2 = em.find(Group.class, group2Id);
            for (GroupHierarchy hierarchy : group2.getHierarchyList()) {
                assertEquals(group2, hierarchy.getGroup());
                if (hierarchy.getLevel() == 0)
                    assertEquals(rootId, hierarchy.getParent().getId());
                else if (hierarchy.getLevel() == 1)
                    assertEquals(group3Id, hierarchy.getParent().getId());
                else if (hierarchy.getLevel() == 2)
                    assertEquals(group1Id, hierarchy.getParent().getId());
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
