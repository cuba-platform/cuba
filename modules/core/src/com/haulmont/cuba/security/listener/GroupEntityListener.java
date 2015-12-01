/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.GroupHierarchy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_GroupEntityListener")
public class GroupEntityListener implements
        BeforeInsertEntityListener<Group>,
        BeforeUpdateEntityListener<Group> {

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Override
    public void onBeforeInsert(Group entity) {
        createNewHierarchy(entity, entity.getParent());
    }

    protected void createNewHierarchy(Group entity, Group parent) {
        if (parent == null) {
            entity.setHierarchyList(new ArrayList<>());

            return;
        }

        if (!PersistenceHelper.isManaged(parent) && !PersistenceHelper.isDetached(parent))
            throw new IllegalStateException("Unable to create GroupHierarchy. Commit parent group first.");

        EntityManager em = persistence.getEntityManager();

        if (entity.getHierarchyList() == null) {
            entity.setHierarchyList(new ArrayList<>());
        } else {
            entity.getHierarchyList().clear();
        }

        if (PersistenceHelper.isDetached(parent))
            parent = em.find(Group.class, parent.getId()); // refresh parent in case of detached

        int level = 0;
        for (GroupHierarchy hierarchy : parent.getHierarchyList()) {
            GroupHierarchy h = metadata.create(GroupHierarchy.class);
            h.setGroup(entity);
            h.setParent(hierarchy.getParent());
            h.setLevel(level++);
            em.persist(h);
            entity.getHierarchyList().add(h);
        }
        GroupHierarchy h = metadata.create(GroupHierarchy.class);
        h.setGroup(entity);
        h.setParent(parent);
        h.setLevel(level);
        em.persist(h);
        entity.getHierarchyList().add(h);
    }

    @Override
    public void onBeforeUpdate(Group entity) {
        if (!persistence.getTools().getDirtyFields(entity).contains("parent"))
            return;

        EntityManager em = persistence.getEntityManager();

        for (GroupHierarchy oldHierarchy : entity.getHierarchyList()) {
            em.remove(oldHierarchy);
        }
        createNewHierarchy(entity, entity.getParent());

        TypedQuery<GroupHierarchy> q = em.createQuery(
                "select h from sec$GroupHierarchy h join fetch h.group " +
                        "where h.parent.id = ?1", GroupHierarchy.class);
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
