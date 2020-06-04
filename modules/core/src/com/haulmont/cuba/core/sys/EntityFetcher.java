/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;

/**
 * Fetches entities by views by accessing reference attributes.
 */
@Component(EntityFetcher.NAME)
public class EntityFetcher {

    public static final String NAME = "cuba_EntityFetcher";

    private static final Logger log = LoggerFactory.getLogger(EntityFetcher.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected Persistence persistence;

    @Inject
    protected EntityStates entityStates;

    /**
     * Fetch instance by view object.
     */
    public void fetch(Entity instance, View view) {
        if (view == null)
            return;
        fetch(instance, view, new HashMap<>(), false);
    }

    /**
     * Fetch instance by view name.
     */
    public void fetch(Entity instance, String viewName) {
        if (viewName == null)
            return;
        View view = viewRepository.getView(instance.getClass(), viewName);
        fetch(instance, view, new HashMap<>(), false);
    }

    /**
     * Fetch instance by view object.
     *
     * @param optimizeForDetached if true, detached objects encountered in the graph will be first checked whether all
     *                            required attributes are already loaded, and reloaded only when needed.
     *                            If the argument is false, all detached objects are reloaded anyway.
     */
    public void fetch(Entity instance, View view, boolean optimizeForDetached) {
        if (view == null)
            return;
        fetch(instance, view, new HashMap<>(), optimizeForDetached);
    }

    /**
     * Fetch instance by view name.
     *
     * @param optimizeForDetached if true, detached objects encountered in the graph will be first checked whether all
     *                            required attributes are already loaded, and reloaded only when needed.
     *                            If the argument is false, all detached objects are reloaded anyway.
     */
    public void fetch(Entity instance, String viewName, boolean optimizeForDetached) {
        if (viewName == null)
            return;
        View view = viewRepository.getView(instance.getClass(), viewName);
        fetch(instance, view, new HashMap<>(), optimizeForDetached);
    }

    @SuppressWarnings("unchecked")
    protected void fetch(Entity entity, View view, Map<Instance, Set<View>> visited, boolean optimizeForDetached) {
        Set<View> views = visited.get(entity);
        if (views == null) {
            views = new HashSet<>();
            visited.put(entity, views);
        } else if (views.contains(view)) {
            return;
        }
        views.add(view);

        if (log.isTraceEnabled()) log.trace("Fetching instance " + entity);

        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        for (ViewProperty property : view.getProperties()) {
            MetaProperty metaProperty = metaClass.getPropertyNN(property.getName());
            if (!metaProperty.getRange().isClass() && !metadata.getTools().isLazyFetchedLocalAttribute(metaProperty)
                    || metadata.getTools().isNotPersistent(metaClass, metaProperty))
                continue;

            if (log.isTraceEnabled()) log.trace("Fetching property " + property.getName());

            Object value = entity.getValue(property.getName());
            View propertyView = property.getView();
            if (value != null && propertyView != null) {
                if (value instanceof Collection) {
                    for (Object item : new ArrayList(((Collection) value))) {
                        if (item instanceof Entity) {
                            Entity e = (Entity) item;
                            if (entityStates.isDetached(e)) {
                                fetchReloaded(e, propertyView, visited, optimizeForDetached, managed -> {
                                    if (value instanceof List) {
                                        List list = (List) value;
                                        list.set(list.indexOf(e), managed);
                                    } else {
                                        Collection collection = (Collection) value;
                                        collection.remove(e);
                                        collection.add(managed);
                                    }
                                });
                            } else {
                                fetch((Entity) item, propertyView, visited, optimizeForDetached);
                            }
                        }
                    }
                } else if (value instanceof Entity) {
                    Entity e = (Entity) value;
                    if (!metaProperty.isReadOnly() && entityStates.isDetached(e) && !(e instanceof EmbeddableEntity)) {
                        fetchReloaded(e, propertyView, visited, optimizeForDetached, managed -> {
                                    entity.setValue(property.getName(), managed);
                        });
                    } else {
                        fetch(e, propertyView, visited, optimizeForDetached);
                    }
                }
            }
        }
    }

    protected void fetchReloaded(Entity entity, View view, Map<Instance, Set<View>> visited, boolean optimizeForDetached,
                                 Consumer<Entity> managedEntityConsumer) {
        if (!optimizeForDetached || needReloading(entity, view)) {
            if (log.isTraceEnabled()) {
                log.trace("Object " + entity + " is detached, loading it");
            }
            String storeName = metadata.getTools().getStoreName(entity.getMetaClass());
            if (storeName != null) {
                try (Transaction tx = persistence.getTransaction(storeName)) {
                    EntityManager em = persistence.getEntityManager(storeName);
                    //noinspection unchecked
                    Entity managed = em.find(entity.getClass(), entity.getId());
                    if (managed != null) { // the instance here can be null if it has been deleted
                        managedEntityConsumer.accept(managed);
                        fetch(managed, view, visited, optimizeForDetached);
                    }
                    tx.commit();
                }
            }
        }
    }

    protected boolean needReloading(Entity entity, View view) {
        return !entityStates.isLoadedWithView(entity, view);
    }
}
