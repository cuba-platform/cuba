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

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.QueryParser;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.core.sys.DbUpdater;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.sql.DataSource;
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
 * This MBean is intended for reading on startup and caching database metadata information,
 * mainly to support soft delete functionality;
 */
@ManagedBean(PersistenceConfigAPI.NAME)
public class PersistenceConfig extends ManagementBean implements PersistenceConfigMBean, PersistenceConfigAPI
{
    private static Log log = LogFactory.getLog(PersistenceConfig.class);
    private boolean metadataLoaded;
    private Set<String> softDeleteTables = new HashSet<String>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    @Inject
    private DbUpdater dbUpdater;

    private void initDbMetadata() {
        log.info("Initializing DB metadata");
        DataSource datasource = Locator.getDataSource();
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

    public String updateDatabase() {
        try {
            dbUpdater.updateDatabase();
            return "Updated";
        } catch (Throwable e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    public String findUpdateDatabaseScripts() {
        try {
            List<String> list = dbUpdater.findUpdateDatabaseScripts();
            StrBuilder sb = new StrBuilder();
            sb.appendWithSeparators(list, "\n");
            return sb.toString();
        } catch (Throwable e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    public String jpqlLoadList(String queryString) {
        try {
            Transaction tx = Locator.createTransaction();
            try {
                EntityManager em = PersistenceProvider.getEntityManager();
                Query query = em.createQuery(queryString);
                QueryParser parser = QueryTransformerFactory.createParser(queryString);
                Set<String> paramNames = parser.getParamNames();
                for (String paramName : paramNames) {
                    SecurityProvider.setQueryParam(query, paramName);
                }
                List resultList = query.getResultList();
                tx.commit();

                StrBuilder sb = new StrBuilder();
                sb.appendWithSeparators(resultList, "\n");
                return sb.toString();
            } finally {
                tx.end();
            }
        } catch (Throwable e) {
            log.error("jpqlLoadList error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    public String jpqlExecuteUpdate(String queryString, boolean softDeletion) {
        try {
            login();
            Transaction tx = Locator.createTransaction();
            try {
                EntityManager em = PersistenceProvider.getEntityManager();
                em.setSoftDeletion(softDeletion);
                Query query = em.createQuery(queryString);
                int count = query.executeUpdate();
                tx.commit();

                return "Done: " + count + " entities affected, softDeletion=" + softDeletion;
            } finally {
                tx.end();
            }
        } catch (Throwable e) {
            log.error("jpqlExecuteUpdate error", e);
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }
}
