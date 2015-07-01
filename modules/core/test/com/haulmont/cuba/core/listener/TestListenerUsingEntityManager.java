/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.Server;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_TestListenerUsingEntityManager")
public class TestListenerUsingEntityManager
        implements BeforeInsertEntityListener<Server>, BeforeUpdateEntityListener<Server>, BeforeDeleteEntityListener<Server> {

    @Inject
    private Persistence persistence;

    @Override
    public void onBeforeInsert(Server entity) {
        EntityManager em = persistence.getEntityManager();

        FileDescriptor related = new FileDescriptor();
        related.setName("Related");
        System.out.println(">>>>> persist related: " + related.getId());
        em.persist(related);

        entity.setData(related.getId().toString());
    }

    @Override
    public void onBeforeUpdate(Server entity) {
        EntityManager em = persistence.getEntityManager();

        UUID relatedId = UUID.fromString(entity.getData());
        FileDescriptor related = em.find(FileDescriptor.class, relatedId);
        if (related != null) {
            related.setName("Related updated");
            System.out.println(">>>>> update related: " + relatedId);
        } else
            throw new RuntimeException("Related not found: " + relatedId);
    }

    @Override
    public void onBeforeDelete(Server entity) {
        EntityManager em = persistence.getEntityManager();

        UUID relatedId = UUID.fromString(entity.getData());
        FileDescriptor related = em.find(FileDescriptor.class, relatedId);
        if (related != null) {
            System.out.println(">>>>> remove related: " + relatedId);
            em.remove(related);
        } else
            throw new RuntimeException("Related entity not found" + relatedId);
    }
}
