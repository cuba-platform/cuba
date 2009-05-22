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

import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Locator;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import javax.sql.DataSource;
import javax.naming.NamingException;

public class PersistenceConfig implements PersistenceConfigMBean, PersistenceConfigAPI
{
    private String datasourceName;
    private Element persistenceUnitElement;
    private boolean metadataLoaded;
    private Set<String> softDeleteTables = new HashSet<String>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

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

    public void initDbMetadata() {
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
            ResultSet rs = metaData.getColumns(null, null, null, "delete_ts");
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

    public boolean isDeleteDeferredFor(String table) {
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
