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
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.security.entity.LoggedEntity;
import com.haulmont.cuba.security.entity.LoggedAttribute;
import com.haulmont.cuba.security.entity.EntityLogItem;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.Instance;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

public class EntityLog implements EntityLogMBean, EntityLogAPI
{
    private Log log = LogFactory.getLog(EntityLog.class);

    private volatile boolean loaded;

    private Map<String, Set<String>> entitiesManual;
    private Map<String, Set<String>> entitiesAuto;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void create() {
    }

    public void start() {
    }

    public EntityLogAPI getAPI() {
        return this;
    }

    public void invalidateCache() {
        lock.writeLock().lock();
        try {
            entitiesManual = null;
            entitiesAuto = null;
            loaded = false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Set<String> getLoggedAttributes(String entity, boolean auto) {
        lock.readLock().lock();
        try {
            if (!loaded) {
                // upgrade lock
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    if (!loaded) { // recheck because we unlocked for a while
                        loadEntities();
                        loaded = true;
                    }
                } finally {
                    // downgrade lock
                    lock.writeLock().unlock();
                    lock.readLock().lock();
                }
            }

            Set<String> attributes;
            if (auto)
                attributes = entitiesAuto.get(entity);
            else
                attributes = entitiesManual.get(entity);

            return attributes == null ? null : Collections.unmodifiableSet(attributes);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void loadEntities() {
        entitiesManual = new HashMap<String, Set<String>>();
        entitiesAuto = new HashMap<String, Set<String>>();
        Transaction tx = Locator.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query q = em.createQuery(
                    "select e from sec$LoggedEntity e join fetch e.attributes " +
                    "where e.auto = true or e.manual = true");
            List<LoggedEntity> list = q.getResultList();
            for (LoggedEntity loggedEntity : list) {
                Set<String> attributes = new HashSet<String>();
                for (LoggedAttribute loggedAttribute : loggedEntity.getAttributes()) {
                    attributes.add(loggedAttribute.getName());
                }
                if (loggedEntity.isAuto())
                    entitiesAuto.put(loggedEntity.getName(), attributes);
                if (loggedEntity.isManual())
                    entitiesManual.put(loggedEntity.getName(), attributes);
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void registerCreate(BaseEntity entity) {
        registerCreate(entity, false);
    }

    public void registerCreate(BaseEntity entity, boolean auto) {
        try {
            String entityName = entity.getClass().getName();
            Set<String> attributes = getLoggedAttributes(entityName, auto);
            if (attributes == null) {
                return;
            }
            Date ts = TimeProvider.currentTimestamp();
            EntityManager em = PersistenceProvider.getEntityManager();
            User user = em.getReference(User.class, SecurityProvider.currentUserId());

            for (String attr : attributes) {
                EntityLogItem item = new EntityLogItem();
                item.setEventTs(ts);
                item.setUser(user);
                item.setType(EntityLogItem.Type.CREATE);
                item.setEntity(entityName);
                item.setEntityId((UUID) entity.getId());
                item.setAttribute(attr);
                item.setValue(stringify(((Instance) entity).getValue(attr)));

                em.persist(item);
            }
        } catch (Exception e) {
            log.warn("Unable to log entity " + entity + ", id=" + entity.getId(), e);
        }
    }

    public void registerModify(BaseEntity entity) {
        registerModify(entity, false);
    }

    public void registerModify(BaseEntity entity, boolean auto) {
        try {
            String entityName = entity.getClass().getName();
            Set<String> attributes = getLoggedAttributes(entityName, auto);
            if (attributes == null) {
                return;
            }
            Date ts = TimeProvider.currentTimestamp();
            EntityManager em = PersistenceProvider.getEntityManager();
            User user = em.getReference(User.class, SecurityProvider.currentUserId());
            Set<String> dirty = PersistenceProvider.getDirtyFields(entity);

            for (String attr : attributes) {
                if (dirty.contains(attr)) {
                    EntityLogItem item = new EntityLogItem();
                    item.setEventTs(ts);
                    item.setUser(user);
                    item.setType(EntityLogItem.Type.MODIFY);
                    item.setEntity(entityName);
                    item.setEntityId((UUID) entity.getId());
                    item.setAttribute(attr);
                    item.setValue(stringify(((Instance) entity).getValue(attr)));

                    em.persist(item);
                }
            }
        } catch (Exception e) {
            log.warn("Unable to log entity " + entity + ", id=" + entity.getId(), e);
        }
    }

    public void registerDelete(BaseEntity entity) {
        registerDelete(entity, false);
    }

    public void registerDelete(BaseEntity entity, boolean auto) {
        try {
            String entityName = entity.getClass().getName();
            Set<String> attributes = getLoggedAttributes(entityName, auto);
            if (attributes == null) {
                return;
            }
            Date ts = TimeProvider.currentTimestamp();
            EntityManager em = PersistenceProvider.getEntityManager();
            User user = em.getReference(User.class, SecurityProvider.currentUserId());

            EntityLogItem item = new EntityLogItem();
            item.setEventTs(ts);
            item.setUser(user);
            item.setType(EntityLogItem.Type.DELETE);
            item.setEntity(entityName);
            item.setEntityId((UUID) entity.getId());

            em.persist(item);
        } catch (Exception e) {
            log.warn("Unable to log entity " + entity + ", id=" + entity.getId(), e);
        }
    }

    private String stringify(Object value) {
        if (value == null)
            return "<null>";

        String s;
        Datatype datatype = Datatypes.getInstance().get(value.getClass());
        if (datatype != null) {
            s = datatype.format(value);
        } else {
            s = value.toString();
        }
        return StringUtils.substring(s, 0, EntityLogItem.VALUE_LEN);
    }
}
