/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling of entities before they have been updated in DB.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface BeforeUpdateEntityListener<T extends BaseEntity> {

    /**
     * Executes before the object has been updated in DB.
     *
     * @param entity updated entity
     */
    void onBeforeUpdate(T entity);
}
