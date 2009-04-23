/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.12.2008 19:33:33
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.listener;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EntityListenerManager
{
    private static class Key
    {
        private final Class entityClass;
        private final EntityListenerType type;

        public Key(Class entityClass, EntityListenerType type) {
            this.entityClass = entityClass;
            this.type = type;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (!entityClass.equals(key.entityClass)) return false;
            if (type != key.type) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = entityClass.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }
    }

    private static EntityListenerManager instance;

    private Log log = LogFactory.getLog(EntityListenerManager.class);

    private Map<Key, Object> cache = new ConcurrentHashMap<Key, Object>();

    private Object nullListener = new Object();

    public static EntityListenerManager getInstance() {
        if (instance == null) {
            instance = new EntityListenerManager();
        }
        return instance;
    }

    public void fireListener(BaseEntity entity, EntityListenerType type) {
        Object entityListener = getListener(entity.getClass(), type);
        if (entityListener != null) {
            switch (type) {
                case BEFORE_INSERT: {
                    logExecution(type, entity);
                    ((BeforeInsertEntityListener) entityListener).onBeforeInsert(entity);
                    break;
                }
                case BEFORE_UPDATE: {
                    logExecution(type, entity);
                    ((BeforeUpdateEntityListener) entityListener).onBeforeUpdate(entity);
                    break;
                }
                case BEFORE_DELETE: {
                    logExecution(type, entity);
                    ((BeforeDeleteEntityListener) entityListener).onBeforeDelete(entity);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unsupported EntityListenerType: " + type);
            }
        }
    }

    private void logExecution(EntityListenerType type, BaseEntity entity) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Executing ").append(type).append(" entity listener for ")
                    .append(entity.getClass().getName()).append(" id=").append(entity.getId());
            Set<String> dirty = PersistenceHelper.getDirtyFields(entity);
            if (!dirty.isEmpty()) {
                sb.append(", changedProperties: ");
                for (Iterator<String> it = dirty.iterator(); it.hasNext();) {
                    String field = it.next();
                    sb.append(field);
                    if (it.hasNext())
                        sb.append(",");
                }
            }
            log.debug(sb.toString());
        }
    }

    private Object getListener(Class<? extends BaseEntity> entityClass, EntityListenerType type) {
        Key key = new Key(entityClass, type);

        if (!cache.containsKey(key)) {
            Object listener = findListener(entityClass, type);
            cache.put(key, listener != null ? listener : nullListener);
            return listener;
        }
        else {
            Object listener = cache.get(key);
            return listener != nullListener ? listener : null;
        }
    }

    private Object findListener(Class<? extends BaseEntity> entityClass, EntityListenerType type) {
        log.trace("get listener " + type + " for class " + entityClass.getName());
        String[] classNames = getDeclaredListeners(entityClass);
        if (classNames == null) {
            log.trace("no annotations, exiting");
            return null;
        }

        for (String className : classNames) {
            try {
                Class aClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                log.trace("listener class found: " + aClass);
                Class[] interfaces = aClass.getInterfaces();
                for (Class intf : interfaces) {
                    if (intf.equals(type.getListenerInterface())) {
                        log.trace("listener implements " + type.getListenerInterface());
                        return aClass.newInstance();
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find an Entity Listener class", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to instantiate an Entity Listener", e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to instantiate an Entity Listener", e);
            }
        }
        log.trace("no implementors of interface " + type.getListenerInterface() + " found");
        return null;
    }

    private String[] getDeclaredListeners(Class<? extends BaseEntity> entityClass) {
        Listeners annotation = entityClass.getAnnotation(Listeners.class);
        return annotation == null ? null : annotation.value();
    }

}
