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
import com.haulmont.cuba.core.sys.ConfigWorker;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class ConfigStorage extends ManagementBean implements ConfigStorageMBean
{
    public void create() {
    }

    public void start() {
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
            String value = getConfigWorker().getProperty(name);
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
            getConfigWorker().setProperty(name, value);
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
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
            logout();
        }
    }

    private ConfigWorker getConfigWorker() {
        ConfigWorker configWorker = Locator.lookupLocal(ConfigWorker.JNDI_NAME);
        return configWorker;
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
}
