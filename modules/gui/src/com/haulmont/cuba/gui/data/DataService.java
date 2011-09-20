/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:09:14
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

/**
 * GUI interface to provide CRUD functionality. Extends similar middleware interface.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface DataService extends com.haulmont.cuba.core.app.DataService {

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
    <A extends Entity> A reload(A entity, View view, MetaClass metaClass);

    /**
     * Reload the entity instance from database with the view specified. Loading instance class may differ from original
     * instance if we want to load an ancestor or a descendant.
     * @param entity                    reloading instance
     * @param view                      view object
     * @param metaClass                 desired MetaClass, if null - original entity's metaclass is used
     * @param useSecurityConstraints    whether to apply security constraints when loading the instnace
     * @return                          reloaded instance
     */
    <A extends Entity> A reload(A entity, View view, MetaClass metaClass, boolean useSecurityConstraints);

    /**
     * Commit the entity to the database.
     * @param entity    entity instance
     * @param view      view object, affects returning committed instance
     * @return          committed instance
     */
    <A extends Entity> A commit(A entity, View view);

    /**
     * Remove the entity instance from the database.
     * @param entity    entity instance
     */
    void remove(Entity entity);
}
