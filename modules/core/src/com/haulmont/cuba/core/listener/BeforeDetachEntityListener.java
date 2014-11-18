/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling entities right before they are detached from an EntityManager
 * on transaction commit.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface BeforeDetachEntityListener<T extends BaseEntity> {

    /**
     * Executes before the object is detached from an EntityManager on transaction commit.
     *
     * @param entity        entity in managed state
     * @param entityManager current EntityManager
     */
    void onBeforeDetach(T entity, EntityManager entityManager);
}
