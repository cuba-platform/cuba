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
 * @author krivopustov
 * @version $Id$
 */
public interface DataManager {

    String NAME = "cuba_DataManager";

    /**
     * Load a single entity instance.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link com.haulmont.cuba.core.global.LoadContext}.</p>
     * @param context   {@link com.haulmont.cuba.core.global.LoadContext} object, defining what and how to load
     * @return          the loaded detached object, or null if not found
     */
    @Nullable
    <A extends Entity> A load(LoadContext context);

    /**
     * Load collection of entity instances.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link LoadContext}.</p>
     * @param context   {@link LoadContext} object, defining what and how to load
     * @return          a list of detached instances, or empty list if nothing found
     */
    <A extends Entity> List<A> loadList(LoadContext context);

    /**
     * Reload the entity instance from database with the view specified.
     * @param entity        reloading instance
     * @param viewName      view name
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <A extends Entity> A reload(A entity, String viewName);

    /**
     * Reload the entity instance from database with the view specified.
     * @param entity        reloading instance
     * @param view          view object
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <A extends Entity> A reload(A entity, View view);

    /**
     * Reload the entity instance from database with the view specified. Loading instance class may differ from original
     * instance if we want to load an ancestor or a descendant.
     * @param entity        reloading instance
     * @param view          view object
     * @param metaClass     desired MetaClass, if null - original entity's metaclass is used
     * @return              reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass);

    /**
     * Reload the entity instance from database with the view specified. Loading instance class may differ from original
     * instance if we want to load an ancestor or a descendant.
     * @param entity                    reloading instance
     * @param view                      view object
     * @param metaClass                 desired MetaClass, if null - original entity's metaclass is used
     * @param useSecurityConstraints    whether to apply security constraints when loading the instnace
     * @return                          reloaded instance
     * @throws EntityAccessException if the entity cannot be reloaded because it was deleted or access restrictions has been changed
     */
    <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass, boolean useSecurityConstraints);

    /**
     * Commit a collection of new or detached entity instances to the database.
     * @param context   {@link com.haulmont.cuba.core.global.CommitContext} object, containing committing entities and other information
     * @return          set of committed instances
     */
    Set<Entity> commit(CommitContext context);

    /**
     * Commit the entity to the database.
     * @param entity    entity instance
     * @param view      view object, affects returning committed instance
     * @return          committed instance
     */
    <A extends Entity> A commit(A entity, @Nullable View view);

    /**
     * Commit the entity to the database.
     * @param entity    entity instance
     * @return          committed instance
     */
    <A extends Entity> A commit(A entity);

    /**
     * Remove the entity instance from the database.
     * @param entity    entity instance
     */
    void remove(Entity entity);}
