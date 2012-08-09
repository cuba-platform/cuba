/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Config;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrBuilder;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Supports configuration parameters framework functionality.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(ConfigStorageAPI.NAME)
public class ConfigStorage extends ManagementBean implements ConfigStorageMBean, ConfigStorageAPI {

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
    public String printDbProperties() {
        return printDbProperties(null);
    }

    @Override
    public String printDbProperties(String prefix) {
        try {
            login();
            loadCache();
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, String> entry : cache.entrySet()) {
                if (prefix == null || entry.getKey().startsWith(prefix)) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    @Override
    public String getDbPropertyJmx(String name) {
        try {
            login();
            String value = getDbProperty(name);
            return name + "=" + value;
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    @Override
    public String setDbPropertyJmx(String name, String value) {
        try {
            login();
            setDbProperty(name, value);
            return "Property " + name + " set to " + value;
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    @Override
    public String removeDbPropertyJmx(String name) {
        Transaction tx = persistence.createTransaction();
        try {
            login();
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("delete from sys$Config c where c.name = ?1");
            query.setParameter(1, name);
            query.executeUpdate();
            tx.commit();
            clearCache();
            return "Property " + name + " removed";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
            logout();
        }
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
    public String printAppProperties() {
        return printAppProperties(null);
    }

    @Override
    public String printAppProperties(String prefix) {
        List<String> list = new ArrayList<String>();
        for (String name : AppContext.getPropertyNames()) {
            if (prefix == null || name.startsWith(prefix)) {
                list.add(name + "=" + AppContext.getProperty(name));
            }
        }
        Collections.sort(list);
        return new StrBuilder().appendWithSeparators(list, "\n").toString();
    }

    @Override
    public String getAppProperty(String name) {
        return name + "=" + AppContext.getProperty(name);
    }

    @Override
    public String setAppProperty(String name, String value) {
        AppContext.setProperty(name, value);
        return "Property " + name + " set to " + value;
    }

    @Override
    public Map<String, String> getDbProperties() {
        loadCache();
        return new HashMap<String, String>(cache);
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
                        cache.put(config.getName(), config.getValue());
                    }
                    cacheLoaded = true;
                }
            }
        }
    }

    @Override
    public void setDbProperty(String name, String value) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Config instance = getConfigInstance(name);
            if (value != null) {
                if (instance == null) {
                    instance = new Config();
                    instance.setName(name);
                    instance.setValue(value);
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
