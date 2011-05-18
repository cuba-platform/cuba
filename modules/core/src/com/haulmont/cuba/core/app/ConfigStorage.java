/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 18:02:20
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.Config;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConfigStorage MBean implementation.
 * <p>
 * This MBean is intended to support configuration parameters functionality.
 * It works with database and caches parameters.
 */
@ManagedBean(ConfigStorageAPI.NAME)
public class ConfigStorage extends ManagementBean implements ConfigStorageMBean, ConfigStorageAPI
{
    private Log log = LogFactory.getLog(ConfigStorageService.class);

    private Map<String, String> cache = new ConcurrentHashMap<String, String>();

    private String nullValue = new String();

    public ConfigStorageAPI getAPI() {
        return this;
    }

    public String printProperties() {
        return printProperties(null);
    }

    public String printProperties(String prefix) {
        Transaction tx = Locator.createTransaction();
        try {
            login();
            StringBuilder sb = new StringBuilder();

            EntityManager em = PersistenceProvider.getEntityManager();
            String s = String.format("select c from core$Config c %s",
                    (prefix == null ? "" : "where c.name like ?1"));
            Query query = em.createQuery(s);
            if (prefix != null) {
                query.setParameter(1, prefix);
            }
            List<Config> list = query.getResultList();
            for (Config config : list) {
                sb.append(config.getName()).append("=").append(config.getValue()).append("\n");
            }
            tx.commit();
            return sb.toString();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
            logout();
        }
    }

    public String getProperty(String name) {
        try {
            login();
            String value = getConfigProperty(name);
            return value;
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    public String setProperty(String name, String value) {
        try {
            login();
            setConfigProperty(name, value);
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    public String removeProperty(String name) {
        Transaction tx = Locator.createTransaction();
        try {
            login();
            EntityManager em = PersistenceProvider.getEntityManager();
            Query query = em.createQuery("delete from core$Config c where c.name = ?1");
            query.setParameter(1, name);
            query.executeUpdate();
            tx.commit();
            cache.remove(name);
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
            logout();
        }
    }

    public void clearCache() {
        cache.clear();
    }

    public String getConfigProperty(String name) {
        String value = cache.get(name);
        if (value == nullValue)
            return null;
        else if (value != null)
            return value;

        Transaction tx = Locator.createTransaction();
        try {
            Config instance = getConfigInstance(name);
            if (instance == null) {
                value = null;
            } else {
                value = instance.getValue();
                if (value == null)
                    throw new IllegalStateException("Config property '" + name + "' value is null");
            }
            tx.commit();
        } finally {
            tx.end();
        }
        if (value != null)
            cache.put(name, value);
        else
            cache.put(name, nullValue);
        return value;
    }

    public void setConfigProperty(String name, String value) {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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
        if (value != null)
            cache.put(name, value);
        else
            cache.remove(name);
    }

    private Config getConfigInstance(String name) {
        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery("select c from core$Config c where c.name = ?1");
        query.setParameter(1, name);
        query.setView(null);
        List<Config> list = query.getResultList();
        if (list.isEmpty())
            return null;
        else
            return list.get(0);
    }
}
