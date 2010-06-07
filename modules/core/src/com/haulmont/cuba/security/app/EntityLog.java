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

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * EntityLog MBean implementation.
 * <p/>
 * Allows to log entity lifecycle events: create, modify, delete.<br>
 * Configured by {@link com.haulmont.cuba.security.entity.LoggedEntity} and {@link com.haulmont.cuba.security.entity.LoggedAttribute} entities.
 * See also {@link com.haulmont.cuba.security.app.EntityLogConfig} configuration parameters.
 */
@ManagedBean(EntityLogAPI.NAME)
public class EntityLog implements EntityLogMBean, EntityLogAPI {

    private Log log = LogFactory.getLog(EntityLog.class);

    private volatile boolean loaded;

    private EntityLogConfig config;

    private Map<String, Set<String>> entitiesManual;
    private Map<String, Set<String>> entitiesAuto;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Inject
    public EntityLog(ConfigProvider configProvider) {
        config = configProvider.doGetConfig(EntityLogConfig.class);
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
            log.debug("Invalidating cache");
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
        log.debug("Loading entities");
        entitiesManual = new HashMap<String, Set<String>>();
        entitiesAuto = new HashMap<String, Set<String>>();
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query q = em.createQuery("select e from sec$LoggedEntity e where e.auto = true or e.manual = true");
//            q.setView(null);
            List<LoggedEntity> list = q.getResultList();
            for (LoggedEntity loggedEntity : list) {
                if (loggedEntity.getName() == null) {
                    throw new IllegalStateException("Unable to initialize EntityLog: empty LoggedEntity.name");
                }
                Set<String> attributes = new HashSet<String>();
                for (LoggedAttribute loggedAttribute : loggedEntity.getAttributes()) {
                    if (loggedAttribute.getName() == null) {
                        throw new IllegalStateException("Unable to initialize EntityLog: empty LoggedAttribute.name");
                    }
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
        log.debug("Loaded: entitiesAuto=" + entitiesAuto.size() + ", entitiesManual=" + entitiesManual.size());
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
                Object value = ((Instance) entity).getValue(attr);
                attribute.setValue(stringify(value));
                attribute.setValueId(getValueId(value));
                attribute.setMessagesPack(MessageUtils.inferMessagePack(attr, (Instance) entity));
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

            EntityLogItem item = null;
            for (String attr : attributes) {
                if (dirty.contains(attr)) {
                    if (item == null) {
                        item = new EntityLogItem();
                        item.setEventTs(ts);
                        item.setUser(user);
                        item.setType(EntityLogItem.Type.MODIFY);
                        item.setEntity(entityName);
                        item.setEntityId((UUID) entity.getId());
                        em.persist(item);
                    }
                    EntityLogAttr attribute = new EntityLogAttr();
                    attribute.setName(attr);
                    Object value = ((Instance) entity).getValue(attr);
                    attribute.setValue(stringify(value));
                    attribute.setValueId(getValueId(value));
                    attribute.setMessagesPack(MessageUtils.inferMessagePack(attr, (Instance) entity));
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

            for (String attr : attributes) {
                EntityLogAttr attribute = new EntityLogAttr();
                attribute.setName(attr);
                Object value = ((Instance) entity).getValue(attr);
                attribute.setValue(stringify(value));
                attribute.setValueId(getValueId(value));
                attribute.setMessagesPack(MessageUtils.inferMessagePack(attr, (Instance) entity));
                attribute.setLogItem(item);
                em.persist(attribute);
            }
        } catch (Exception e) {
            log.warn("Unable to log entity " + entity + ", id=" + entity.getId(), e);
        }
    }

    private UUID getValueId(Object value) {
        if (value instanceof Entity) {
            return ((Entity<UUID>) value).getId();
        } else {
            return null;
        }
    }

    private String stringify(Object value) {
        if (value == null)
            return "";
        else if (value instanceof Instance) {
            return ((Instance) value).getInstanceName();
        } else if (value instanceof Date) {
            return Datatypes.getInstance().get(Date.class).format((Date) value);
        } else {
            return value.toString();
        }
    }
}
