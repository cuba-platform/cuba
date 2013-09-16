/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;

/**
 * Interface defining operations with entities on GUI level.
 *
 * @author abramov
 * @version $Id$
 */
public interface DataSupplier extends DataService {

    /**
     * Create a new entity instance
     * @param metaClass     entity MetaClass
     * @return              created instance
     */
    <A extends Entity> A newInstance(MetaClass metaClass);

    /**
     * Reload the entity instance from database with the view specified.
     * @param entity        reloading instance
     * @param viewName      view name
     * @return              reloaded instance
     */
    <A extends Entity> A reload(A entity, String viewName);

    /**
     * Reload the entity instance from database with the view specified.
     * @param entity        reloading instance
     * @param view          view object
     * @return              reloaded instance
     */
    <A extends Entity> A reload(A entity, View view);

    /**
     * Reload the entity instance from database with the view specified. Loading instance class may differ from original
     * instance if we want to load an ancestor or a descendant.
     * @param entity        reloading instance
     * @param view          view object
     * @param metaClass     desired MetaClass, if null - original entity's metaclass is used
     * @return              reloaded instance
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
     */
    <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass, boolean useSecurityConstraints);

    /**
     * Commit the entity to the database.
     * @param entity    entity instance
     * @param view      view object, affects returning committed instance
     * @return          committed instance
     */
    <A extends Entity> A commit(A entity, @Nullable View view);

    /**
     * Remove the entity instance from the database.
     * @param entity    entity instance
     */
    void remove(Entity entity);
}
