/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2008 18:35:16
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.BaseEntity;

public interface EntityManagerAdapter
{
    void persist(BaseEntity entity);

    <T extends BaseEntity> T merge(T entity);

    void remove(BaseEntity entity);

    <T extends BaseEntity> T find(Class<T> clazz, Object key);

    QueryAdapter createQuery(String qlStr);

    void flush();

    void close();
}
