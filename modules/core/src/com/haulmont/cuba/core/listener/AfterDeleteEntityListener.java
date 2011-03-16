/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 11:31:43
 *
 * $Id$
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling of entities after they have been deleted or
 * marked as deleted in DB.<br>
 * Any updates through the EntityManager are impossible!
 */
public interface AfterDeleteEntityListener<T extends BaseEntity>
{
    /**
     * Executes after the object has been deleted or marked as deleted in DB.<br>
     * @param entity deleted entity
     */
    void onAfterDelete(T entity);
}
