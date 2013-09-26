/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling entities after they have been inserted into DB.<br>
 * Any updates through the EntityManager are impossible!
 *
 * @author krivopustov
 * @version $Id$
 */
public interface AfterInsertEntityListener<T extends BaseEntity> {

    /**
     * Executes after the object has been inserted into DB.
     *
     * @param entity updated entity
     */
    void onAfterInsert(T entity);
}
