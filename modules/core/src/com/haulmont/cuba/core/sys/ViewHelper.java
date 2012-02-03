/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 13:01:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.persistence.FetchPlan;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ViewHelper
{
    private static Log log = LogFactory.getLog(ViewHelper.class);

    private static Map<View, Set<FetchPlanField>> fetchPlans = new ConcurrentHashMap<View, Set<FetchPlanField>>();

    public static void setView(FetchPlan fetchPlan, View view) {
        if (fetchPlan == null)
            throw new IllegalArgumentException("FetchPlan is null");

        fetchPlan.clearFetchGroups();

        if (view != null) {
            fetchPlan.removeFetchGroup(FetchPlan.GROUP_DEFAULT);
            fetchPlan.setExtendedPathLookup(true);

            Set<FetchPlanField> fetchPlanFields;
            if (!StringUtils.isEmpty(view.getName())) {
                fetchPlanFields = fetchPlans.get(view);
                if (fetchPlanFields == null) {
                    fetchPlanFields = new HashSet<FetchPlanField>();
                    processView(view, fetchPlanFields);
                    fetchPlans.put(view, fetchPlanFields);
                }
            } else {
                // Don't cache unnamed views, because they are usually created programmatically and may be different
                // each time
                fetchPlanFields = new HashSet<FetchPlanField>();
                processView(view, fetchPlanFields);
            }
            for (FetchPlanField field : fetchPlanFields) {
                fetchPlan.addField(field.entityClass, field.property);
            }
        } else {
            fetchPlan.addFetchGroup(FetchPlan.GROUP_DEFAULT);
        }
    }

    public static void addView(FetchPlan fetchPlan, View view) {
        if (fetchPlan == null)
            throw new IllegalArgumentException("FetchPlan is null");
        if (view == null)
            throw new IllegalArgumentException("View is null");

        Set<FetchPlanField> fetchPlanFields = fetchPlans.get(view);
        if (fetchPlanFields == null) {
            fetchPlanFields = new HashSet<FetchPlanField>();
            processView(view, fetchPlanFields);
            fetchPlans.put(view, fetchPlanFields);
        }
        for (FetchPlanField field : fetchPlanFields) {
            fetchPlan.addField(field.entityClass, field.property);
        }
    }

    public static View intersectViews(View first, View second) {
        if (first == null)
            throw new IllegalArgumentException("View is null");
        if (second == null)
            throw new IllegalArgumentException("View is null");

        View resultView = new View(first.getEntityClass());

        Collection<ViewProperty> firstProps = first.getProperties();

        for (ViewProperty firstProperty : firstProps) {
            if (second.containsProperty(firstProperty.getName())) {
                View resultPropView = null;
                ViewProperty secondProperty = second.getProperty(firstProperty.getName());
                if ((firstProperty.getView() != null) && (secondProperty.getView() != null)) {
                    resultPropView = intersectViews(firstProperty.getView(), secondProperty.getView());
                }
                resultView.addProperty(firstProperty.getName(), resultPropView);
            }
        }

        return resultView;
    }

    private static void processView(View view, Set<FetchPlanField> fetchPlanFields) {
        if (view.isIncludeSystemProperties()) {
            includeSystemProperties(view, fetchPlanFields);
        }

        for (ViewProperty property : view.getProperties()) {
            if (property.isLazy())
                continue;

            FetchPlanField field = new FetchPlanField(
                    getRealClass(view.getEntityClass(), property.getName()), property.getName());
            fetchPlanFields.add(field);
            if (property.getView() != null) {
                processView(property.getView(), fetchPlanFields);
            }
        }
    }

    private static void includeSystemProperties(View view, Set<FetchPlanField> fetchPlanFields) {
        Class<? extends BaseEntity> entityClass = view.getEntityClass();
        if (BaseEntity.class.isAssignableFrom(entityClass)) {
            for (String property : BaseEntity.PROPERTIES) {
                fetchPlanFields.add(new FetchPlanField(getRealClass(entityClass, property), property));
            }
        }
        if (Updatable.class.isAssignableFrom(entityClass)) {
            for (String property : Updatable.PROPERTIES) {
                fetchPlanFields.add(new FetchPlanField(getRealClass(entityClass, property), property));
            }
        }
        if (SoftDelete.class.isAssignableFrom(entityClass)) {
            for (String property : SoftDelete.PROPERTIES) {
                fetchPlanFields.add(new FetchPlanField(getRealClass(entityClass, property), property));
            }
        }
    }

    /**
     * This is the workaround for OpenJPA's inability to create correct fetch plan for entities
     * inherited form other entities (not directly from a MappedSuperclass). Here we do the following:
     * <ul>
     * <li>If the property is declared in an entity, return this entity class</li>
     * <li>If the property is declared in a MappedSuperclass, return a recent entity class down to the hierarchy</li>
     * </ul>
     * @param entityClass   entity class for which a fetch plan is being created
     * @param property      entity property name to include in the fetch plan
     * @return              a class in the hierarchy (see conditions above)
     */
    @SuppressWarnings("unchecked")
    private static Class getRealClass(Class<? extends BaseEntity> entityClass, String property) {
        if (hasDeclaredField(entityClass, property))
            return entityClass;

        List<Class> superclasses = ClassUtils.getAllSuperclasses(entityClass);
        for (int i = 0; i < superclasses.size(); i++) {
            Class superclass = superclasses.get(i);
            if (hasDeclaredField(superclass, property)) {
                // If the class declaring the field is an entity, return it
                if (superclass.isAnnotationPresent(javax.persistence.Entity.class))
                    return superclass;
                // Else search for a recent entity down to the hierarchy
                for (int j = i - 1; j >= 0; j--) {
                    superclass = superclasses.get(j);
                    if (superclass.isAnnotationPresent(javax.persistence.Entity.class))
                        return superclass;
                }
            }
        }
        return entityClass;
    }

    private static boolean hasDeclaredField(Class<? extends BaseEntity> entityClass, String name) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name))
                return true;
        }
        return false;
    }

    public static void fetchInstance(Instance instance, View view) {
        if (PersistenceHelper.isDetached(instance))
            throw new IllegalArgumentException("Can not fetch detached entity. Merge first.");
        __fetchInstance(instance, view, new HashMap<Instance, Set<View>>());
    }

    private static void __fetchInstance(Instance instance, View view, Map<Instance, Set<View>> visited) {
        Set<View> views = visited.get(instance);

        if (views == null) {
            views = new HashSet<View>();
            visited.put(instance, views);
        } else if (views.contains(view)) {
            return;
        }

        views.add(view);

        if (log.isTraceEnabled()) log.trace("Fetching instance " + instance);
        for (ViewProperty property : view.getProperties()) {
            if (log.isTraceEnabled()) log.trace("Fetching property " + property.getName());
            Object value = instance.getValue(property.getName());
            View propertyView = property.getView();
            if (value != null && propertyView != null) {
                if (value instanceof Collection) {
                    for (Object item : ((Collection) value)) {
                        if (item instanceof Instance)
                            __fetchInstance((Instance) item, propertyView, visited);
                    }
                } else if (value instanceof Instance) {
                    if (PersistenceHelper.isDetached(value)) {
                        log.trace("Object " + value + " is detached, loading it");
                        EntityManager em = PersistenceProvider.getEntityManager();
                        Entity entity = (Entity) value;
                        value = em.find(entity.getClass(), entity.getId());
                        if (value == null) {
                            // the instance is most probably deleted
                            continue;
                        }
                        instance.setValue(property.getName(), value);
                    }
                    __fetchInstance((Instance) value, propertyView, visited);
                }
            }
        }
    }

    public static boolean hasLazyProperties(View view) {
        for (ViewProperty property : view.getProperties()) {
            if (property.isLazy())
                return true;
            if (property.getView() != null) {
                if (hasLazyProperties(property.getView()))
                    return true;
            }
        }
        return false;
    }

    private static class FetchPlanField {
        private final Class entityClass;
        private final String property;

        private FetchPlanField(Class entityClass, String property) {
            this.entityClass = entityClass;
            this.property = property;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FetchPlanField that = (FetchPlanField) o;

            if (!entityClass.equals(that.entityClass)) return false;
            if (!property.equals(that.property)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = entityClass.hashCode();
            result = 31 * result + property.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return entityClass.getName() + "." + property;
        }
    }
}
