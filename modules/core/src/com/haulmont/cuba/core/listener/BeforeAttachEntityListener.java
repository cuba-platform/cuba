/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Defines the contract for handling entities right before they are attached to an EntityManager on merge operation.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface BeforeAttachEntityListener<T extends BaseEntity> {

    /**
     * Executes before the object is attached to an EntityManager on merge operation.
     *
     * @param entity        detached entity
     */
    void onBeforeAttach(T entity);
}
