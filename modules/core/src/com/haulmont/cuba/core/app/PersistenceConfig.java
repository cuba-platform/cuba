/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 12:54:58
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * PersistentConfig MBean implementation.
 * <p>
 * This MBean is intended for:
 * <li>read on start and cache database metadata information, mainly to support soft delete functionality;
 * <li>read on start <code>conf/system.properties</code> file and set appropriate Java system properties
 */
public class PersistenceConfig implements PersistenceConfigMBean, PersistenceConfigAPI
{
    private static Log log = LogFactory.getLog(PersistenceConfig.class);
    private String datasourceName;
    private Element persistenceUnitElement;
    private boolean metadataLoaded;
    private Set<String> softDeleteTables = new HashSet<String>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    static {
        __loadSystemProperties();
    }

    public void create() {
        String path = PersistenceProvider.getPersistenceXmlPath();
        InputStream stream = getClass().getResourceAsStream("/" + path);
        if (stream == null)
            throw new IllegalStateException("persistence.xml not found in " + path);

        SAXReader xmlReader = new SAXReader();
        Document doc;
        try {
            doc = xmlReader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element root = doc.getRootElement();
        Element unitElem = null;
        Element dsElem;
        for (Element element : (List <Element>) root.elements("persistence-unit")) {
            if (PersistenceProvider.getPersistenceUnitName().equals(element.attributeValue("name"))) {
                unitElem = element;
            }
        }
        persistenceUnitElement = unitElem;
        if (persistenceUnitElement == null)
            throw new IllegalStateException("Persistence unit " + PersistenceProvider.getPersistenceUnitName() + " not found");
        dsElem = unitElem.element("jta-data-source");
        if (dsElem == null)
            throw new IllegalStateException("No 'jta-data-source' element found for persistence unit " + PersistenceProvider.getPersistenceUnitName());
        datasourceName = dsElem.getText();
    }

    public PersistenceConfigAPI getAPI() {
        return this;
    }

    private static void __loadSystemProperties() {
        String confUrl = System.getProperty("jboss.server.config.url");
        String fileName = URI.create(confUrl).getPath() + "system.properties";
        File file = new File(fileName);
        if (file.exists()) {
            Properties props;
            try {
                InputStream is = new FileInputStream(fileName);
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
            } catch (IOException e) {
                log.error("Unable to load system properties", e);
            }
        }
    }

    public String loadSystemProperties() {
        String confUrl = System.getProperty("jboss.server.config.url");
        try {
            StringBuilder sb = new StringBuilder();
            __loadSystemProperties();
            String fileName = URI.create(confUrl).getPath() + "system.properties";
            File file = new File(fileName);
            if (file.exists()) {
                __loadSystemProperties();
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
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    public void initDbMetadata() {
        log.info("Initializing DB metadata");
        DataSource datasource;
        try {
            datasource = (DataSource) Locator.getJndiContext().lookup(datasourceName);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, null, PersistenceProvider.getDbDialect().getDeleteTsColumn());
            lock.writeLock().lock();
            try {
                softDeleteTables.clear();
                while (rs.next()) {
                    String table = rs.getString("TABLE_NAME");
                    softDeleteTables.add(table);
                }
            } finally {
                lock.writeLock().unlock();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
        metadataLoaded = true;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public String printSoftDeleteTables() {
        lock.readLock().lock();
        if (!metadataLoaded) {
            lock.readLock().unlock();
            initDbMetadata();
            lock.readLock().lock();
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (String table : softDeleteTables) {
                sb.append(table).append("\n");
            }
            return sb.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isSoftDeleteFor(String table) {
        lock.readLock().lock();
        if (!metadataLoaded) {
            lock.readLock().unlock();
            initDbMetadata();
            lock.readLock().lock();
        }
        try {
            return softDeleteTables.contains(table);
        } finally {
            lock.readLock().unlock();
        }
    }
}
