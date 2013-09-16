/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling of entities before they have been deleted or
 * marked as deleted in DB.<br>
 */
public interface BeforeDeleteEntityListener<T extends BaseEntity>
{
    /**
     * Executes before the object has been deleted or marked as deleted in DB.<br>
     * @param entity deleted entity
     */
    void onBeforeDelete(T entity);
}
