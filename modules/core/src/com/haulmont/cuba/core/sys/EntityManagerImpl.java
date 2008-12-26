/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:56:32
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.DeleteDeferred;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class EntityManagerImpl implements EntityManager
{
    public interface CloseListener
    {
        void onClose();
    }

    private OpenJPAEntityManager jpaEm;

    private boolean closed;
    private Set<CloseListener> closeListeners = new HashSet<CloseListener>();

    private boolean deleteDeferred = true;

    EntityManagerImpl(OpenJPAEntityManager jpaEntityManager) {
        this.jpaEm = jpaEntityManager;
    }

    public boolean isDeleteDeferred() {
        return deleteDeferred;
    }

    public void setDeleteDeferred(boolean deleteDeferred) {
        if (deleteDeferred != this.deleteDeferred) {
            // clear SQL queries cache
            OpenJPAConfiguration conf = ((OpenJPAEntityManagerFactorySPI) jpaEm.getEntityManagerFactory()).getConfiguration();
            if (conf instanceof JDBCConfiguration) {
                Map map = ((JDBCConfiguration) conf).getQuerySQLCacheInstance();
                for (Object val : map.values()) {
                    if (val instanceof Map)
                        ((Map) val).clear();
                }
            }
            this.deleteDeferred = deleteDeferred;
        }
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

    public Query createQuery() {
        return new QueryImpl(jpaEm, false);
    }

    public Query createQuery(String qlStr) {
        QueryImpl query = new QueryImpl(jpaEm, false);
        query.setQueryString(qlStr);
        return query;
    }

    public Query createNativeQuery() {
        return new QueryImpl(jpaEm, true);
    }

    public Query createNativeQuery(String sql) {
        QueryImpl query = new QueryImpl(jpaEm, true);
        query.setQueryString(sql);
        return query;
    }

    public void setView(View view) {
        ViewHelper.setView(jpaEm.getFetchPlan(), view);
    }

    public void flush() {
        jpaEm.flush();
    }

    public void close() {
        jpaEm.close();
        closed = true;
        for (CloseListener listener : closeListeners) {
            listener.onClose();
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public void addCloseListener(CloseListener listener) {
        closeListeners.add(listener);
    }

    public void removeCloseListener(CloseListener listener) {
        closeListeners.remove(listener);
    }
}
