/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Supports configuration parameters framework functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ConfigStorageAPI.NAME)
public class ConfigStorage implements ConfigStorageAPI {

    @Inject
    protected Persistence persistence;

    protected ClusterManagerAPI clusterManager;

    protected Map<String, String> cache;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();
    protected Lock readLock = lock.readLock();
    protected Lock writeLock = lock.writeLock();

    private Logger log = LoggerFactory.getLogger(ConfigStorage.class);

    private static class InvalidateCacheMsg implements Serializable {
        private static final long serialVersionUID = -3116358584797500962L;
    }
    
    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        clusterManager.addListener(InvalidateCacheMsg.class, new ClusterListenerAdapter<InvalidateCacheMsg>() {
            @Override
            public void receive(InvalidateCacheMsg message) {
                internalClearCache();
            }
        });
    }
    
    @Override
    public void clearCache() {
        internalClearCache();
        clusterManager.send(new InvalidateCacheMsg());
    }

    private void internalClearCache() {
        writeLock.lock();
        try {
            cache = null;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Map<String, String> getDbProperties() {
        readLock.lock();
        try {
            loadCache();
            return new HashMap<>(cache);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String getDbProperty(String name) {
        readLock.lock();
        try {
            loadCache();
            return cache.get(name);
        } finally {
            readLock.unlock();
        }
    }

    protected void loadCache() {
        if (cache == null) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (cache == null) {
                    log.info("Loading DB-stored app properties cache");
                    // Don't use transactions here because of loop possibility from EntityLog
                    QueryRunner queryRunner = new QueryRunner(persistence.getDataSource());
                    try {
                        cache = queryRunner.query("select NAME, VALUE from SYS_CONFIG",
                                new ResultSetHandler<Map<String, String>>() {
                                    @Override
                                    public Map<String, String> handle(ResultSet rs) throws SQLException {
                                        HashMap<String, String> map = new HashMap<>();
                                        while (rs.next()) {
                                            map.put(rs.getString(1), rs.getString(2));
                                        }
                                        return map;
                                    }
                                });
                    } catch (SQLException e) {
                        throw new RuntimeException("Error loading DB-stored app properties cache", e);
                    }
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    @Override
    public void setDbProperty(String name, String value) {
        Preconditions.checkNotNullArgument(name, "name is null");
        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Config instance = getConfigInstance(name);
            if (value != null) {
                if (instance == null) {
                    instance = new Config();
                    instance.setName(name.trim());
                    instance.setValue(value.trim());
                    em.persist(instance);
                } else {
                    instance.setValue(value);
                }
            } else {
                if (instance != null)
                    em.remove(instance);
            }
            tx.commit();
        } finally {
            tx.end();
        }
        clearCache();
    }

    private Config getConfigInstance(String name) {
        EntityManager em = persistence.getEntityManager();
        Query query = em.createQuery("select c from sys$Config c where c.name = ?1");
        query.setParameter(1, name);
        query.setView(null);
        List<Config> list = query.getResultList();
        if (list.isEmpty())
            return null;
        else
            return list.get(0);
    }
}
