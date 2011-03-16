/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.12.2008 19:01:40
 *
 * $Id$
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling of entities after they have been updated in DB.<br>
 * Any updates through the EntityManager are impossible!
 */
public interface AfterUpdateEntityListener<T extends BaseEntity>
{
    /**
     * Executes after the object has been updated in DB.<br>
     * @param entity updated entity
     */
    void onAfterUpdate(T entity);
}
