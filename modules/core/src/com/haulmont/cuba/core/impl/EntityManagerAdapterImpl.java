/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:56:32
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.haulmont.cuba.core.EntityManagerAdapter;
import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.QueryAdapter;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.DeleteDeferred;

public class EntityManagerAdapterImpl implements EntityManagerAdapter
{
    private Log log = LogFactory.getLog(EntityManagerAdapterImpl.class);

    private OpenJPAEntityManager jpaEm;

    EntityManagerAdapterImpl(OpenJPAEntityManager jpaEntityManager) {
        this.jpaEm = jpaEntityManager;
    }

    public void persist(BaseEntity entity) {
        jpaEm.persist(entity);
    }

    public <T extends BaseEntity> T merge(T entity) {
        return jpaEm.merge(entity);
    }

    public void remove(BaseEntity entity) {
        if (entity instanceof DeleteDeferred) {
            ((DeleteDeferred) entity).setDeleteTs(TimeProvider.currentTimestamp());
            ((DeleteDeferred) entity).setDeletedBy(SecurityProvider.currentUserLogin());
        }
        else {
            jpaEm.remove(entity);
        }
    }

    public <T extends BaseEntity> T find(Class<T> clazz, Object key) {
        return jpaEm.find(clazz, key);
    }

    public QueryAdapter createQuery(String qlStr) {
        log.debug("Creating JPQL query: " + qlStr);
        return new QueryAdapterImpl(jpaEm.createQuery(qlStr));
    }

    public void flush() {
        jpaEm.flush();
    }

    public void close() {
        jpaEm.close();
    }
}
