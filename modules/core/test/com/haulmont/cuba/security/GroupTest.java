/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 15:47:39
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.GroupHierarchy;

import java.util.UUID;

public class GroupTest extends CubaTestCase
{
    private UUID rootId, group1Id, group2Id, group3Id;

    private void createGroups() {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Group root = new Group();
            rootId = root.getId();
            root.setName("root");
            em.persist(root);
            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            root = em.find(Group.class, rootId);
            Group group1 = new Group();
            group1Id = group1.getId();
            group1.setName("group1");
            group1.setParent(root);
            em.persist(group1);
            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            group1 = em.find(Group.class, group1Id);
            Group group2 = new Group();
            group2Id = group2.getId();
            group2.setName("group2");
            group2.setParent(group1);
            em.persist(group2);
            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
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

    public void testNew() {
        createGroups();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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

    public void testUpdate() {
        createGroups();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Group group1 = em.find(Group.class, group1Id);
            Group group3 = em.find(Group.class, group3Id);
            group1.setParent(group3);
            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();

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
