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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EntityLog implements EntityLogMBean, EntityLogAPI {
    private Log log = LogFactory.getLog(EntityLog.class);

    private volatile boolean loaded;

    private EntityLogConfig config = ConfigProvider.getConfig(EntityLogConfig.class);

    private Map<String, Set<String>> entitiesManual;
    private Map<String, Set<String>> entitiesAuto;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public EntityLogAPI getAPI() {
        return this;
    }

    public synchronized boolean isEnabled() {
        return config.getEnabled();
    }

    public synchronized void setEnabled(boolean enabled) {
        if (enabled != config.getEnabled()) {
            config.setEnabled(enabled);
        }
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

    private Set<String> getLoggedAttributes(String entity, boolean auto) {
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
                    "select distinct e from sec$LoggedEntity e join fetch e.attributes " +
                            "where e.auto = true or e.manual = true");
            List<LoggedEntity> list = q.getResultList();
            for (LoggedEntity loggedEntity : list) {
                Set<String> attributes = new HashSet<String>();
                for (LoggedAttribute loggedAttribute : loggedEntity.getAttributes()) {
                    attributes.add(loggedAttribute.getName());
                }
                if (BooleanUtils.isTrue(loggedEntity.getAuto()))
                    entitiesAuto.put(loggedEntity.getName(), attributes);
                if (BooleanUtils.isTrue(loggedEntity.getManual()))
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
        if (!isEnabled())
            return;

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
            item.setType(EntityLogItem.Type.CREATE);
            item.setEntity(entityName);
            item.setEntityId((UUID) entity.getId());
            em.persist(item);

            for (String attr : attributes) {
                EntityLogAttr attribute = new EntityLogAttr();
                attribute.setName(attr);
                attribute.setValue(stringify(((Instance) entity).getValue(attr)));
                attribute.setLogItem(item);
                em.persist(attribute);
            }
        } catch (Exception e) {
            log.warn("Unable to log entity " + entity + ", id=" + entity.getId(), e);
        }
    }

    public void registerModify(BaseEntity entity) {
        registerModify(entity, false);
    }

    public void registerModify(BaseEntity entity, boolean auto) {
        if (!isEnabled())
            return;

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

            EntityLogItem item = new EntityLogItem();
            item.setEventTs(ts);
            item.setUser(user);
            item.setType(EntityLogItem.Type.MODIFY);
            item.setEntity(entityName);
            item.setEntityId((UUID) entity.getId());
            em.persist(item);

            for (String attr : attributes) {
                if (dirty.contains(attr)) {
                    EntityLogAttr attribute = new EntityLogAttr();
                    attribute.setName(attr);
                    attribute.setValue(stringify(((Instance) entity).getValue(attr)));
                    attribute.setLogItem(item);
                    em.persist(attribute);
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
        if (!isEnabled())
            return;

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
            return "";

        String s;
        Datatype datatype = Datatypes.getInstance().get(value.getClass());
        if (datatype != null) {
            s = datatype.format(value);
        } else if (value.getClass().isEnum()) {
            String nameKey = value.getClass().getSimpleName() + "." + value.toString();
            s = MessageProvider.getMessage(value.getClass(), nameKey);
        } else {
            s = value.toString();
        }
        return StringUtils.substring(s, 0, EntityLogItem.VALUE_LEN);
    }
}
