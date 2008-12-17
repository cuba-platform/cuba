/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
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
 * Defines the contract for handling of entities before they have been updated in DB.<br>
 */
public interface BeforeUpdateEntityListener<T extends BaseEntity>
{
    /**
     * Executes before the object has been updated in DB.<br>
     * @param entity updated entity
     * @param changedProperties names of properties changed since last load.<br>
     * If the entity was detached, then changedProperties contains all properties of the entity.<br>
     * The array is sorted, so Arrays.binarySearch() can be applied
     */
    void onBeforeUpdate(T entity);
}
