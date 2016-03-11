/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Central interface to provide CRUD functionality.
 *
 * <p>Works with non-managed (new or detached) entities, always starts and commits new transactions. Can be used on
 * both middle and client tiers.</p>
 *
 * <p>When used on the client tier - always applies security restrictions.
 * <p>When used on the middleware - does not apply security restrictions by default. If you want to apply security,
 * get {@link #secure()} instance or set the {@code cuba.dataManagerChecksSecurityOnMiddleware} application property
 * to use it by default.
 *
 * @author krivopustov
 * @version $Id$
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
     * @return          number of instances in the database
     */
    long getCount(LoadContext<? extends Entity> context);

    /**
     * Reloads the entity instance from database with the view specified.
     * @param entity        reloading instance
     * @param viewName      view name
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <E extends Entity> E reload(E entity, String viewName);

    /**
     * Reloads the entity instance from database with the view specified.
     * @param entity        reloading instance
     * @param view          view object
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <E extends Entity> E reload(E entity, View view);

    /**
     * Reloads the entity instance from database with the view specified. Loading instance class may differ from original
     * instance if we want to load an ancestor or a descendant.
     * @param entity        reloading instance
     * @param view          view object
     * @param metaClass     desired MetaClass, if null - original entity's metaclass is used
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass);

    /**
     * Reloads the entity instance from database with the view specified. Loading instance class may differ from original
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
     * Commits a collection of new or detached entity instances to the database.
     * @param context   {@link com.haulmont.cuba.core.global.CommitContext} object, containing committing entities and other information
     * @return          set of committed instances
     */
    Set<Entity> commit(CommitContext context);

    /**
     * Commits the entity to the database.
     * @param entity    entity instance
     * @param view      view object, affects the returned committed instance
     * @return          committed instance
     */
    <E extends Entity> E commit(E entity, @Nullable View view);

    /**
     * Commits the entity to the database.
     * @param entity    entity instance
     * @param viewName  view name, affects the returned committed instance
     * @return          committed instance
     */
    <E extends Entity> E commit(E entity, @Nullable String viewName);

    /**
     * Commits the entity to the database.
     * @param entity    entity instance
     * @return          committed instance
     */
    <E extends Entity> E commit(E entity);

    /**
     * Removes the entity instance from the database.
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
