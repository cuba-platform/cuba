/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:56:32
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.entity.DeleteDeferred;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.View;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.kernel.Broker;
import org.apache.openjpa.kernel.OpCallbacks;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.persistence.AutoDetachType;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;

import java.util.*;

public class EntityManagerImpl implements EntityManager
{
    public interface CloseListener
    {
        void onClose();
    }

    private OpenJPAEntityManager delegate;

    private boolean closed;
    private Set<CloseListener> closeListeners = new HashSet<CloseListener>();

    private boolean deleteDeferred = true;

    private OpCallbacksImpl dummyOpCallbacks = new OpCallbacksImpl();

    EntityManagerImpl(OpenJPAEntityManager jpaEntityManager) {
        delegate = jpaEntityManager;
        // Set AutoDetachType to none to prevent automatic detach after transaction rollback 
        delegate.setAutoDetach(EnumSet.noneOf(AutoDetachType.class));
    }

    public boolean isDeleteDeferred() {
        return deleteDeferred;
    }

    public void setDeleteDeferred(boolean deleteDeferred) {
        if (deleteDeferred != this.deleteDeferred) {
            // clear SQL queries cache
            OpenJPAConfiguration conf = ((OpenJPAEntityManagerFactorySPI) delegate.getEntityManagerFactory()).getConfiguration();
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

    public void persist(Entity entity) {
        delegate.persist(entity);
    }

    public <T extends Entity> T merge(T entity) {
        return delegate.merge(entity);
    }

    public void remove(Entity entity) {
        if (entity instanceof DeleteDeferred) {
            ((DeleteDeferred) entity).setDeleteTs(TimeProvider.currentTimestamp());
            ((DeleteDeferred) entity).setDeletedBy(SecurityProvider.currentUserSession().getUser().getLogin());
        }
        else {
            delegate.remove(entity);
        }
    }

    public <T extends Entity> T find(Class<T> clazz, Object key) {
        return delegate.find(clazz, key);
    }

    public <T extends Entity> T getReference(Class<T> clazz, Object key) {
        return delegate.getReference(clazz, key);
    }

    public Query createQuery() {
        return new QueryImpl(delegate, false);
    }

    public Query createQuery(String qlStr) {
        QueryImpl query = new QueryImpl(delegate, false);
        query.setQueryString(qlStr);
        return query;
    }

    public Query createNativeQuery() {
        return new QueryImpl(delegate, true);
    }

    public Query createNativeQuery(String sql) {
        QueryImpl query = new QueryImpl(delegate, true);
        query.setQueryString(sql);
        return query;
    }

    public void setView(View view) {
        ViewHelper.setView(delegate.getFetchPlan(), view);
    }

    public void flush() {
        delegate.flush();
    }

    public void close() {
        delegate.close();
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

    public Collection getManagedObjects() {
        return delegate.getManagedObjects();
    }

    public void detachAll() {
        Broker broker = ((org.apache.openjpa.persistence.EntityManagerImpl) delegate).getBroker();
        broker.detachAll(dummyOpCallbacks);
    }

    private static class OpCallbacksImpl implements OpCallbacks
    {
        public int processArgument(int op, Object arg, OpenJPAStateManager sm) {
            return ACT_RUN | ACT_CASCADE;
        }
    }
}
