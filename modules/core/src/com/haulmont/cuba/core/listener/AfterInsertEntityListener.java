/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 15:23:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling entities after they have been inserted into DB.<br>
 * Any updates through the EntityManager are impossible!
 */
public interface AfterInsertEntityListener<T extends BaseEntity>
{
    /**
     * Executes after the object has been inserted into DB.<br>
     * @param entity updated entity
     */
    void onAfterInsert(T entity);
}
