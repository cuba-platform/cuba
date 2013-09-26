/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling entities before they have been inserted into DB.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface BeforeInsertEntityListener<T extends BaseEntity> {

    /**
     * Executes before the object has been inserted into DB.
     *
     * @param entity updated entity
     */
    void onBeforeInsert(T entity);
}
