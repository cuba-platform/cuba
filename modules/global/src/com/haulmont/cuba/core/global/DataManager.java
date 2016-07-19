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
 *
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Central interface to provide CRUD functionality. Can be used on both middle and client tiers.
 * <p>
 * In case of {@code RdbmsStore}, works with non-managed (new or detached) entities, always starts and commits new
 * transactions.
 * <p>
 * When used on the client tier - always applies security restrictions. When used on the middleware - does not apply
 * security restrictions by default. If you want to apply security, get {@link #secure()} instance or set the
 * {@code cuba.dataManagerChecksSecurityOnMiddleware} application property to use it by default.
 *
 */
public interface DataManager {

    String NAME = "cuba_DataManager";

    /**
     * Loads a single entity instance.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link com.haulmont.cuba.core.global.LoadContext}.</p>
     * @param context   {@link com.haulmont.cuba.core.global.LoadContext} object, defining what and how to load
     * @return          the loaded detached object, or null if not found
     */
    @Nullable
    <E extends Entity> E load(LoadContext<E> context);

    /**
     * Loads collection of entity instances.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link LoadContext}.</p>
     * @param context   {@link LoadContext} object, defining what and how to load
     * @return          a list of detached instances, or empty list if nothing found
     */
    <E extends Entity> List<E> loadList(LoadContext<E> context);

    /**
     * Returns the number of entity instances for the given query passed in the {@link LoadContext}.
     * @param context   defines the query
     * @return          number of instances in the data store
     */
    long getCount(LoadContext<? extends Entity> context);

    /**
     * Reloads the entity instance from data store with the view specified.
     * @param entity        reloading instance
     * @param viewName      view name
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <E extends Entity> E reload(E entity, String viewName);

    /**
     * Reloads the entity instance from data store with the view specified.
     * @param entity        reloading instance
     * @param view          view object
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <E extends Entity> E reload(E entity, View view);

    /**
     * Reloads the entity instance from data store with the view specified. Loading instance class may differ from original
     * instance if we want to load an ancestor or a descendant.
     * @param entity        reloading instance
     * @param view          view object
     * @param metaClass     desired MetaClass, if null - original entity's metaclass is used
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass);

    /**
     * Reloads the entity instance from data store with the view specified. Loading instance class may differ from original
     * instance if we want to load an ancestor or a descendant.
     * @param entity                    reloading instance
     * @param view                      view object
     * @param metaClass                 desired MetaClass, if null - original entity's metaclass is used
     * @param loadDynamicAttributes     whether to load dynamic attributes for the entity
     * @return                          reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass, boolean loadDynamicAttributes);

    /**
     * Commits a collection of new or detached entity instances to the data store.
     * @param context   {@link com.haulmont.cuba.core.global.CommitContext} object, containing committing entities and other information
     * @return          set of committed instances
     */
    Set<Entity> commit(CommitContext context);

    /**
     * Commits the entity to the data store.
     * @param entity    entity instance
     * @param view      view object, affects the returned committed instance
     * @return          committed instance
     */
    <E extends Entity> E commit(E entity, @Nullable View view);

    /**
     * Commits the entity to the data store.
     * @param entity    entity instance
     * @param viewName  view name, affects the returned committed instance
     * @return          committed instance
     */
    <E extends Entity> E commit(E entity, @Nullable String viewName);

    /**
     * Commits the entity to the data store.
     * @param entity    entity instance
     * @return          committed instance
     */
    <E extends Entity> E commit(E entity);

    /**
     * Removes the entity instance from the data store.
     * @param entity    entity instance
     */
    void remove(Entity entity);

    /**
     * Returns the DataManager implementation that is guaranteed to apply security restrictions.
     * <p>By default, DataManager does not apply security when used on the middleware. Use this method if you want
     * to run the same code both on the client and middle tier. For example:
     * <pre>
     *     AppBeans.get(DataManager.class).secure().load(context);
     * </pre>
     */
    DataManager secure();
}
