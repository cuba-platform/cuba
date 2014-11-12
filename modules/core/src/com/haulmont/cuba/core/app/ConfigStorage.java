/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Config;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Supports configuration parameters framework functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ConfigStorageAPI.NAME)
public class ConfigStorage implements ConfigStorageAPI {

    @Inject
    private Persistence persistence;

    private ClusterManagerAPI clusterManager;

    private Map<String, String> cache = new ConcurrentHashMap<String, String>();
    
    private volatile boolean cacheLoaded;

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
        cache.clear();
        cacheLoaded = false;
    }

    @Override
    public Map<String, String> getDbProperties() {
        loadCache();
        return new HashMap<>(cache);
    }

    @Override
    public String getDbProperty(String name) {
        loadCache();
        return cache.get(name);
    }

    private void loadCache() {
        if (!cacheLoaded) {
            List<Config> list;
            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
                String s = "select c from sys$Config c";
                Query query = em.createQuery(s);
                list = query.getResultList();
                tx.commit();
            } finally {
                tx.end();
            }
            synchronized (this) {
                if (!cacheLoaded) {
                    cache.clear();
                    for (Config config : list) {
                        cache.put(config.getName(), config.getValue() == null ? null : config.getValue().trim());
                    }
                    cacheLoaded = true;
                }
            }
        }
    }

    @Override
    public void setDbProperty(String name, String value) {
        Preconditions.checkNotNullArgument(name, "name is null");
        Transaction tx = persistence.createTransaction();
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
