/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.jmx.EntityLogMBean;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(EntityLogAPI.NAME)
public class EntityLog implements EntityLogAPI {

    private Log log = LogFactory.getLog(EntityLog.class);

    private volatile boolean loaded;

    private EntityLogConfig config;

    private Map<String, Set<String>> entitiesManual;
    private Map<String, Set<String>> entitiesAuto;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    public EntityLog(Configuration configuration) {
        config = configuration.getConfig(EntityLogConfig.class);
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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
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

    private String getEntityName(BaseEntity entity) {
        MetaClass metaClass = metadata.getSession().getClassNN(entity.getClass());
        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
        return originalMetaClass != null ? originalMetaClass.getName() : metaClass.getName();
    }

    public void registerCreate(BaseEntity entity) {
        if (entity == null)
            return;
        registerCreate(entity, false);
    }

    public void registerCreate(BaseEntity entity, boolean auto) {
        if (entity == null || !isEnabled())
            return;

        try {
            String entityName = getEntityName(entity);
            Set<String> attributes = getLoggedAttributes(entityName, auto);
            if (attributes != null && attributes.contains("*")) {
                attributes = getAllAttributes(entity);
            }
            if (attributes == null) {
                return;
            }
            Date ts = timeSource.currentTimestamp();
            EntityManager em = persistence.getEntityManager();
            User user = em.getReference(User.class, userSessionSource.getUserSession().getUser().getId());

            EntityLogItem item = new EntityLogItem();
            item.setEventTs(ts);
            item.setUser(user);
            item.setType(EntityLogItem.Type.CREATE);
            item.setEntity(entityName);
            item.setEntityId((UUID) entity.getId());

            Properties properties = new Properties();
            for (String attr : attributes) {
                writeAttribute(properties, entity, attr);
            }
            item.setChanges(getChanges(properties));

            em.persist(item);

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
            String entityName = getEntityName(entity);
            Set<String> attributes = getLoggedAttributes(entityName, auto);
            if (attributes != null && attributes.contains("*")) {
                attributes = getAllAttributes(entity);
            }
            if (attributes == null) {
                return;
            }
            Date ts = timeSource.currentTimestamp();
            EntityManager em = persistence.getEntityManager();
            User user = em.getReference(User.class, userSessionSource.getUserSession().getUser().getId());
            Set<String> dirty = persistence.getTools().getDirtyFields(entity);

            Properties properties = new Properties();
            for (String attr : attributes) {
                if (dirty.contains(attr)) {
                    writeAttribute(properties, entity, attr);
                }
            }
            if (!properties.isEmpty()) {
                EntityLogItem item = new EntityLogItem();
                item.setEventTs(ts);
                item.setUser(user);
                item.setType(EntityLogItem.Type.MODIFY);
                item.setEntity(entityName);
                item.setEntityId((UUID) entity.getId());
                item.setChanges(getChanges(properties));
                em.persist(item);
            }

        } catch (Exception e) {
            log.warn("Unable to log entity " + entity + ", id=" + entity.getId(), e);
        }
    }

    private String getChanges(Properties properties) throws IOException {
        StringWriter writer = new StringWriter();
        properties.store(writer, null);
        String changes = writer.toString();
        if (changes.startsWith("#"))
            changes = changes.substring(changes.indexOf("\n") + 1); // cut off comments line
        return changes;
    }

    private void writeAttribute(Properties properties, BaseEntity entity, String attr) {
        Object value = entity.getValue(attr);
        properties.setProperty(attr, stringify(value));

        UUID valueId = getValueId(value);
        if (valueId != null)
            properties.setProperty(attr + EntityLogAttr.VALUE_ID_SUFFIX, valueId.toString());

        String mp = AppBeans.get(MessageTools.class).inferMessagePack(attr, entity);
        if (mp != null)
            properties.setProperty(attr + EntityLogAttr.MP_SUFFIX, mp);
    }

    public void registerDelete(BaseEntity entity) {
        registerDelete(entity, false);
    }

    public void registerDelete(BaseEntity entity, boolean auto) {
        if (!isEnabled())
            return;

        try {
            String entityName = getEntityName(entity);
            Set<String> attributes = getLoggedAttributes(entityName, auto);
            if (attributes != null && attributes.contains("*")) {
                attributes = getAllAttributes(entity);
            }
            if (attributes == null) {
                return;
            }
            Date ts = timeSource.currentTimestamp();
            EntityManager em = persistence.getEntityManager();
            User user = em.getReference(User.class, userSessionSource.getUserSession().getUser().getId());

            EntityLogItem item = new EntityLogItem();
            item.setEventTs(ts);
            item.setUser(user);
            item.setType(EntityLogItem.Type.DELETE);
            item.setEntity(entityName);
            item.setEntityId((UUID) entity.getId());

            Properties properties = new Properties();
            for (String attr : attributes) {
                writeAttribute(properties, entity, attr);
            }
            item.setChanges(getChanges(properties));

            em.persist(item);

        } catch (Exception e) {
            log.warn("Unable to log entity " + entity + ", id=" + entity.getId(), e);
        }
    }

    private Set<String> getAllAttributes(Entity entity) {
        if (entity == null) return null;
        Set<String> attributes = new HashSet<String>();
        for (MetaProperty metaProperty : metadata.getSession().getClassNN(entity.getClass()).getProperties()) {
            attributes.add(metaProperty.getName());
        }
        return attributes;
    }

    private UUID getValueId(Object value) {
        if (value instanceof Entity) {
            return (UUID) ((Entity) value).getId();
        } else {
            return null;
        }
    }

    protected String stringify(Object value) {
        if (value == null)
            return "";
        else if (value instanceof Instance) {
            return ((Instance) value).getInstanceName();
        } else if (value instanceof Date) {
            Datatype datatype = Datatypes.getNN(value.getClass());
            //noinspection unchecked
            return datatype.format(value);
        } else if (value instanceof Iterable) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Object obj : (Iterable) value) {
                sb.append(stringify(obj)).append(",");
            }
            if (sb.length() > 1)
                sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            return sb.toString();
        } else {
            return String.valueOf(value);
        }
    }
}
