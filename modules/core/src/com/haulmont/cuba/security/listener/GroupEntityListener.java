/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 14:44:41
 *
 * $Id$
 */
package com.haulmont.cuba.security.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Utils;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.GroupHierarchy;
import org.apache.openjpa.enhance.PersistenceCapable;

import java.util.ArrayList;
import java.util.List;

public class GroupEntityListener implements 
            BeforeInsertEntityListener<Group>,
            BeforeUpdateEntityListener<Group>
{
    public void onBeforeInsert(Group entity) {
        createNewHierarchy(entity, entity.getParent());
    }

    private void createNewHierarchy(Group entity, Group parent) {
        if (parent == null)
            return;
        if (((PersistenceCapable) parent).pcIsNew())
            throw new IllegalStateException("Unable to create GroupHierarchy. Commit parent group first.");

        EntityManager em = PersistenceProvider.getEntityManager();

        if (entity.getHierarchyList() == null) {
            entity.setHierarchyList(new ArrayList<GroupHierarchy>());
        }
        else {
            entity.getHierarchyList().clear();
        }

        parent = em.find(Group.class, parent.getId()); // refresh parent in case of detached

        int level = 0;
        for (GroupHierarchy hierarchy : parent.getHierarchyList()) {
            GroupHierarchy h = new GroupHierarchy();
            h.setGroup(entity);
            h.setParent(hierarchy.getParent());
            h.setLevel(level++);
            em.persist(h);
            entity.getHierarchyList().add(h);
        }
        GroupHierarchy h = new GroupHierarchy();
        h.setGroup(entity);
        h.setParent(parent);
        h.setLevel(level);
        em.persist(h);
        entity.getHierarchyList().add(h);
    }

    public void onBeforeUpdate(Group entity) {
        if (!PersistenceProvider.getDirtyFields(entity).contains("parent"))
            return;

        EntityManager em = PersistenceProvider.getEntityManager();

        for (GroupHierarchy oldHierarchy : entity.getHierarchyList()) {
            em.remove(oldHierarchy);
        }
        createNewHierarchy(entity, entity.getParent());

        Query q = em.createQuery(
                "select h from sec$GroupHierarchy h join fetch h.group " +
                "where h.parent.id = ?1");
        q.setParameter(1, entity);
        List<GroupHierarchy> list = q.getResultList();
        for (GroupHierarchy hierarchy : list) {
            Group dependentGroup = hierarchy.getGroup();
            for (GroupHierarchy depHierarchy : dependentGroup.getHierarchyList()) {
                em.remove(depHierarchy);
            }
            em.remove(hierarchy);
            createNewHierarchy(dependentGroup, dependentGroup.getParent());
        }
    }
}
