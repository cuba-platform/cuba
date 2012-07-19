/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling entities right before they are detached from an EntityManager
 * on transaction commit.
 */
public interface BeforeDetachEntityListener<T extends BaseEntity>
{
    /**
     * Executes before the object is detached from an EntityManager on transaction commit.
     * @param entity        detached entity
     * @param entityManager current EntityManager
     */
    void onBeforeDetach(T entity, EntityManager entityManager);
}
