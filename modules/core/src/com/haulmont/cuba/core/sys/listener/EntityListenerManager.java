/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.listener;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.*;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This bean allows to register and fire entity listeners.
 * <p>Usually entity listeners are registered declaratively with {@code @Listeners} annotation on entity class.
 * Methods {@link #addListener(Class, Class)} and {@link #addListener(Class, String)} allow to add listeners dynamically,
 * e.g. to an entity from a base project.
 *
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_EntityListenerManager")
public class EntityListenerManager {

    protected static class Key {
        private final Class entityClass;
        private final EntityListenerType type;

        public Key(Class entityClass, EntityListenerType type) {
            this.entityClass = entityClass;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (!entityClass.equals(key.entityClass)) return false;
            if (type != key.type) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            result = entityClass.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }
    }

    private Logger log = LoggerFactory.getLogger(EntityListenerManager.class);

    @Inject
    protected Persistence persistence;

    protected Map<Key, List> cache = new ConcurrentHashMap<>();

    protected Map<Class<? extends BaseEntity>, Set<String>> dynamicListeners = new ConcurrentHashMap<>();

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected volatile boolean enabled = true;

    /**
     * Register an entity listener by its class. The listener instance will be instatiated as a plain object.
     *
     * @param entityClass   entity
     * @param listenerClass listener class
     */
    public void addListener(Class<? extends BaseEntity> entityClass, Class<?> listenerClass) {
        lock.writeLock().lock();
        try {
            Set<String> set = dynamicListeners.get(entityClass);
            if (set == null) {
                set = new HashSet<>();
                dynamicListeners.put(entityClass, set);
            }
            set.add(listenerClass.getName());

            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Unregister an entity listener.
     *
     * @param entityClass   entity
     * @param listenerClass listener class
     */
    public void removeListener(Class<? extends BaseEntity> entityClass, Class<?> listenerClass) {
        lock.writeLock().lock();
        try {
            Set<String> set = dynamicListeners.get(entityClass);
            if (set != null) {
                set.remove(listenerClass.getName());
            }

            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Register an entity listener which is a ManagedBean.
     *
     * @param entityClass      entity
     * @param listenerBeanName listener bean name
     */
    public void addListener(Class<? extends BaseEntity> entityClass, String listenerBeanName) {
        lock.writeLock().lock();
        try {
            Set<String> set = dynamicListeners.get(entityClass);
            if (set == null) {
                set = new HashSet<>();
                dynamicListeners.put(entityClass, set);
            }
            set.add(listenerBeanName);

            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Unregister an entity listener.
     *
     * @param entityClass      entity
     * @param listenerBeanName listener bean name
     */
    public void removeListener(Class<? extends BaseEntity> entityClass, String listenerBeanName) {
        lock.writeLock().lock();
        try {
            Set<String> set = dynamicListeners.get(entityClass);
            if (set != null) {
                set.remove(listenerBeanName);
            }

            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public void fireListener(BaseEntity entity, EntityListenerType type) {
        if (!enabled)
            return;

        List listeners = getListener(entity.getClass(), type);

        boolean saved = false;
        if (AppContext.getSecurityContext() != null) { // can be null before login when detaching entities
            saved = AppContext.getSecurityContext().isAuthorizationRequired();
            AppContext.getSecurityContext().setAuthorizationRequired(false);
        }
        try {
            for (Object listener : listeners) {
                switch (type) {
                    case BEFORE_DETACH:
                        logExecution(type, entity);
                        ((BeforeDetachEntityListener) listener).onBeforeDetach(entity, persistence.getEntityManager());
                        break;
                    case BEFORE_ATTACH:
                        logExecution(type, entity);
                        ((BeforeAttachEntityListener) listener).onBeforeAttach(entity);
                        break;
                    case BEFORE_INSERT:
                        logExecution(type, entity);
                        ((BeforeInsertEntityListener) listener).onBeforeInsert(entity);
                        break;
                    case AFTER_INSERT:
                        logExecution(type, entity);
                        ((AfterInsertEntityListener) listener).onAfterInsert(entity);
                        break;
                    case BEFORE_UPDATE:
                        logExecution(type, entity);
                        ((BeforeUpdateEntityListener) listener).onBeforeUpdate(entity);
                        break;
                    case AFTER_UPDATE:
                        logExecution(type, entity);
                        ((AfterUpdateEntityListener) listener).onAfterUpdate(entity);
                        break;
                    case BEFORE_DELETE:
                        logExecution(type, entity);
                        ((BeforeDeleteEntityListener) listener).onBeforeDelete(entity);
                        break;
                    case AFTER_DELETE:
                        logExecution(type, entity);
                        ((AfterDeleteEntityListener) listener).onAfterDelete(entity);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unsupported EntityListenerType: " + type);
                }
            }
        } finally {
            if (AppContext.getSecurityContext() != null) {
                AppContext.getSecurityContext().setAuthorizationRequired(saved);
            }
        }
    }

    public void enable(boolean enable) {
        this.enabled = enable;
    }

    protected void logExecution(EntityListenerType type, BaseEntity entity) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Executing ").append(type).append(" entity listener for ")
                    .append(entity.getClass().getName()).append(" id=").append(entity.getId());
            if (type != EntityListenerType.BEFORE_DETACH && type != EntityListenerType.BEFORE_ATTACH) {
                Set<String> dirty = persistence.getTools().getDirtyFields(entity);
                if (!dirty.isEmpty()) {
                    sb.append(", changedProperties: ");
                    for (Iterator<String> it = dirty.iterator(); it.hasNext(); ) {
                        String field = it.next();
                        sb.append(field);
                        if (it.hasNext())
                            sb.append(",");
                    }
                }
            }
            log.debug(sb.toString());
        }
    }

    protected List<?> getListener(Class<? extends BaseEntity> entityClass, EntityListenerType type) {
        Key key = new Key(entityClass, type);

        lock.readLock().lock();
        try {
            if (!cache.containsKey(key)) {
                List listeners = findListener(entityClass, type);
                cache.put(key, listeners);
                return listeners;
            } else {
                return cache.get(key);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    protected List<?> findListener(Class<? extends BaseEntity> entityClass, EntityListenerType type) {
        log.trace("get listener " + type + " for class " + entityClass.getName());
        List<String> names = getDeclaredListeners(entityClass);
        if (names.isEmpty()) {
            log.trace("no annotations, exiting");
            return Collections.emptyList();
        }

        List<Object> result = new ArrayList<>();
        for (String name : names) {
            if (AppBeans.containsBean(name)) {
                Object bean = AppBeans.get(name);
                log.trace("listener bean found: " + bean);
                List<Class> interfaces = ClassUtils.getAllInterfaces(bean.getClass());
                for (Class intf : interfaces) {
                    if (intf.equals(type.getListenerInterface())) {
                        log.trace("listener implements " + type.getListenerInterface());
                        result.add(bean);
                    }
                }
            } else {
                try {
                    Class aClass = Thread.currentThread().getContextClassLoader().loadClass(name);
                    log.trace("listener class found: " + aClass);
                    List<Class> interfaces = ClassUtils.getAllInterfaces(aClass);
                    for (Class intf : interfaces) {
                        if (intf.equals(type.getListenerInterface())) {
                            log.trace("listener implements " + type.getListenerInterface());
                            result.add(aClass.newInstance());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unable to create entity listener " + name + " for " + entityClass.getName()
                            + " because there is no bean or class with such name");
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException("Unable to instantiate an Entity Listener", e);
                }
            }
        }
        return result;
    }

    protected List<String> getDeclaredListeners(Class<? extends BaseEntity> entityClass) {
        List<String> listeners = new ArrayList<>();

        List<Class> superclasses = ClassUtils.getAllSuperclasses(entityClass);
        Collections.reverse(superclasses);
        for (Class superclass : superclasses) {
            Set<String> set = dynamicListeners.get(superclass);
            if (set != null) {
                listeners.addAll(set);
            }

            Listeners annotation = (Listeners) superclass.getAnnotation(Listeners.class);
            if (annotation != null) {
                listeners.addAll(Arrays.asList(annotation.value()));
            }
        }

        Set<String> set = dynamicListeners.get(entityClass);
        if (set != null) {
            listeners.addAll(set);
        }

        Listeners annotation = entityClass.getAnnotation(Listeners.class);
        if (annotation != null) {
            listeners.addAll(Arrays.asList(annotation.value()));
        }

        return listeners;
    }
}