/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
