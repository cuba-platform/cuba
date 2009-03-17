/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 16:10:25
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.LoggedEntity;
import com.haulmont.cuba.security.entity.LoggedAttribute;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EntityLog implements EntityLogMBean
{
    private Map<String, Set<String>> entities;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void create() {
    }

    public void start() {
    }

    public EntityLog getImplementation() {
        return this;
    }

    public void invalidateCache() {
        lock.writeLock().lock();
        try {
            entities = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Set<String> getLoggedAttributes(String entity) {
        lock.readLock().lock();
        try {
            if (entities == null) {
                // upgrade lock
                lock.readLock().unlock();
                lock.writeLock().lock();

                entities = new HashMap<String, Set<String>>();
                loadEntities();

                // downgrade lock
                lock.writeLock().unlock();
                lock.readLock().lock();
            }
            Set<String> attributes = entities.get(entity);
            return attributes == null ? null : Collections.unmodifiableSet(attributes);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void loadEntities() {
        Transaction tx = Locator.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query q = em.createQuery("select e from core$LoggedEntity e join fetch e.attributes");
            List<LoggedEntity> list = q.getResultList();
            for (LoggedEntity loggedEntity : list) {
                Set<String> attributes = new HashSet<String>();
                for (LoggedAttribute loggedAttribute : loggedEntity.getAttributes()) {
                    attributes.add(loggedAttribute.getName());
                }
                entities.put(loggedEntity.getEntity(), attributes);
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }
}
