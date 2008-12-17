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
 * Defines the contract for handling of entities before they have been marked as deleted in DB.<br>
 */
public interface BeforeDeleteEntityListener<T extends BaseEntity>
{
    /**
     * Executes before the object has been marked as deleted in DB.<br>
     * @param entity deleted entity
     */
    void onBeforeDelete(T entity);
}
