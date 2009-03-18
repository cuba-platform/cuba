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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

public interface EntityManager
{
    void persist(Entity entity);

    <T extends Entity> T merge(T entity);

    void remove(Entity entity);

    <T extends Entity> T find(Class<T> clazz, Object key);

    <T extends Entity> T getReference(Class<T> clazz, Object key);

    Query createQuery();

    Query createQuery(String qlStr);

    Query createNativeQuery();

    Query createNativeQuery(String sql);

    void setView(View view);

    void flush();

    void close();

    boolean isClosed();

    boolean isDeleteDeferred();

    void setDeleteDeferred(boolean deleteDeferred);
}
