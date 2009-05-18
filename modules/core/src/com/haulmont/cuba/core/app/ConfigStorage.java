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
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigStorage extends ManagementBean implements ConfigStorageMBean, ConfigStorageAPI
{
    private Log log = LogFactory.getLog(ConfigStorage.class);

    private Map<String, String> cache = new ConcurrentHashMap<String, String>();

    private String nullValue = new String();

    public ConfigStorageAPI getAPI() {
        return this;
    }

    public void start() {
        log.debug("start");
        loadSystemProperties();
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
                sb.append(config.getName()).append("=").append(config.getValue()).append("<br>");
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

    public String loadSystemProperties() {
        String confUrl = System.getProperty("jboss.server.config.url");
        try {
            StringBuilder sb = new StringBuilder();

            String fileName = URI.create(confUrl).getPath() + "system.properties";
            File file = new File(fileName);
            if (file.exists()) {
                InputStream is = new FileInputStream(fileName);
                Properties props;
                try {
                    props = new Properties();
                    props.load(is);
                } finally {
                    is.close();
                }

                for (Map.Entry<Object, Object> entry : props.entrySet()) {
                    if ("".equals(entry.getValue())) {
                        System.getProperties().remove(entry.getKey());
                    }
                    else {
                        System.getProperties().put(entry.getKey(), entry.getValue());
                    }
                }
                sb.append("Properties from ").append(fileName).append(" loaded succesfully\n\n");
            }
            else {
                sb.append("File ").append(fileName).append(" not found\n\n");
            }

            List<String> strings = new ArrayList<String>(System.getProperties().size());
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
                strings.add(entry.getKey().toString() + "=" + entry.getValue().toString());
            }
            Collections.sort(strings);
            sb.append("Current system properties:\n\n");
            for (String s : strings) {
                sb.append(StringEscapeUtils.escapeHtml(s)).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    public String getConfigProperty(String name) {
        String value = cache.get(name);
        if (value == nullValue)
            return null;
        else if (value != null)
            return value;

        Transaction tx = Locator.getTransaction();
        try {
            Config instance = getConfigInstance(name);
            value = instance == null ? null : instance.getValue();
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
        if (value == null)
            throw new IllegalArgumentException("Value can not be null");
        Transaction tx = Locator.getTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Config instance = getConfigInstance(name);
            if (instance == null) {
                instance = new Config();
                instance.setName(name);
                instance.setValue(value);
                em.persist(instance);
            }
            else {
                instance.setValue(value);
            }
            tx.commit();
        } finally {
            tx.end();
        }
        cache.put(name, value);
    }

    private Config getConfigInstance(String name) {
        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery("select c from core$Config c where c.name = ?1");
        query.setParameter(1, name);
        List<Config> list = query.getResultList();
        if (list.isEmpty())
            return null;
        else
            return list.get(0);
    }
}
