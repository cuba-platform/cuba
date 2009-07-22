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

import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<Key, List> cache = new ConcurrentHashMap<Key, List>();

    private Map<Class<? extends BaseEntity>, Set<String>> dynamicListeners =
            new ConcurrentHashMap<Class<? extends BaseEntity>, Set<String>>();

    private volatile boolean enabled = true;

    public static EntityListenerManager getInstance() {
        if (instance == null) {
            instance = new EntityListenerManager();
        }
        return instance;
    }

    public void addListener(Class<? extends BaseEntity> entityClass, Class<?> listenerClassName) {
        Set<String> set = dynamicListeners.get(entityClass);
        if (set == null) {
            set = new HashSet<String>();
            dynamicListeners.put(entityClass, set);
        }
        set.add(listenerClassName.getName());
    }

    public void fireListener(BaseEntity entity, EntityListenerType type) {
        if (!enabled)
            return;
        
        List listeners = getListener(entity.getClass(), type);
        for (Object listener : listeners) {
            switch (type) {
                case BEFORE_INSERT: {
                    logExecution(type, entity);
                    ((BeforeInsertEntityListener) listener).onBeforeInsert(entity);
                    break;
                }
                case BEFORE_UPDATE: {
                    logExecution(type, entity);
                    ((BeforeUpdateEntityListener) listener).onBeforeUpdate(entity);
                    break;
                }
                case BEFORE_DELETE: {
                    logExecution(type, entity);
                    ((BeforeDeleteEntityListener) listener).onBeforeDelete(entity);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unsupported EntityListenerType: " + type);
            }
        }
    }

    public void enable(boolean enable) {
        this.enabled = enable;
    }

    private void logExecution(EntityListenerType type, BaseEntity entity) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Executing ").append(type).append(" entity listener for ")
                    .append(entity.getClass().getName()).append(" id=").append(entity.getId());
            Set<String> dirty = PersistenceProvider.getDirtyFields(entity);
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

    private List getListener(Class<? extends BaseEntity> entityClass, EntityListenerType type) {
        Key key = new Key(entityClass, type);

        if (!cache.containsKey(key)) {
            List listeners = findListener(entityClass, type);
            cache.put(key, listeners);
            return listeners;
        }
        else {
            List listeners = cache.get(key);
            return listeners;
        }
    }

    private List findListener(Class<? extends BaseEntity> entityClass, EntityListenerType type) {
        log.trace("get listener " + type + " for class " + entityClass.getName());
        List<String> classNames = getDeclaredListeners(entityClass);
        if (classNames.isEmpty()) {
            log.trace("no annotations, exiting");
            return Collections.emptyList();
        }

        List result = new ArrayList();
        for (String className : classNames) {
            try {
                Class aClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                log.trace("listener class found: " + aClass);
                Class[] interfaces = aClass.getInterfaces();
                for (Class intf : interfaces) {
                    if (intf.equals(type.getListenerInterface())) {
                        log.trace("listener implements " + type.getListenerInterface());
                        result.add(aClass.newInstance());
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
        return result;
    }

    private List<String> getDeclaredListeners(Class<? extends BaseEntity> entityClass) {
        List<String> listeners = new ArrayList<String>();

        List<Class> superclasses = ClassUtils.getAllSuperclasses(entityClass);
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
