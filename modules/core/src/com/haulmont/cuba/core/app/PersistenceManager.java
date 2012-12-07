/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.EntityStatistics;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.DbUpdater;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class that caches database metadata information and entity statistics. Also delegates some funtionality
 * to {@link DbUpdater}.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(PersistenceManagerAPI.NAME)
public class PersistenceManager implements PersistenceManagerAPI {

    protected static Log log = LogFactory.getLog(PersistenceManager.class);

    protected boolean metadataLoaded;

    protected Set<String> softDeleteTables = new HashSet<>();

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected Map<String, EntityStatistics> statisticsCache;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DbUpdater dbUpdater;

    @Inject
    protected PersistenceSecurity security;

    protected PersistenceConfig config;

    @Inject
    public void setConfiguration(Configuration configuration) {
        config = configuration.getConfig(PersistenceConfig.class);
    }

    protected void initDbMetadata() {
        log.info("Initializing DB metadata");
        DataSource datasource = persistence.getDataSource();
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            lock.writeLock().lock();
            try {
                softDeleteTables.clear();

                ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
                while (tables.next()) {
                    String table = tables.getString("TABLE_NAME");
                    if (table != null) {
                        ResultSet columns = metaData.getColumns(
                                null, null, table, persistence.getDbDialect().getDeleteTsColumn());
                        if (columns.next()) {
                            softDeleteTables.add(table.toLowerCase());
                        }
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {
            }
        }
        metadataLoaded = true;
    }

    @Override
    public boolean isSoftDeleteFor(String table) {
        lock.readLock().lock();
        if (!metadataLoaded) {
            lock.readLock().unlock();
            initDbMetadata();
            lock.readLock().lock();
        }
        try {
            return softDeleteTables.contains(table.toLowerCase());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<String> getSoftDeleteTables() {
        lock.readLock().lock();
        if (!metadataLoaded) {
            lock.readLock().unlock();
            initDbMetadata();
            lock.readLock().lock();
        }
        try {
            ArrayList<String> list = new ArrayList<>(softDeleteTables);
            Collections.sort(list);
            return list;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean useLazyCollection(String entityName) {
        EntityStatistics es = getStatisticsCache().get(entityName);
        if (es == null || es.getInstanceCount() == null)
            return false;
        else {
            int threshold = es.getLazyCollectionThreshold() != null ? es.getLazyCollectionThreshold() : config.getDefaultLazyCollectionThreshold();
            return es.getInstanceCount() > threshold;
        }
    }

    @Override
    public boolean useLookupScreen(String entityName) {
        EntityStatistics es = getStatisticsCache().get(entityName);
        if (es == null || es.getInstanceCount() == null)
            return false;
        else {
            int threshold = es.getLookupScreenThreshold() != null ? es.getLookupScreenThreshold() : config.getDefaultLookupScreenThreshold();
            return es.getInstanceCount() > threshold;
        }
    }

    @Override
    public int getFetchUI(String entityName) {
        EntityStatistics es = getStatisticsCache().get(entityName);
        if (es != null && es.getFetchUI() != null)
            return es.getFetchUI();
        else
            return config.getDefaultFetchUI();
    }

    @Override
    public int getMaxFetchUI(String entityName) {
        EntityStatistics es = getStatisticsCache().get(entityName);
        if (es != null && es.getMaxFetchUI() != null)
            return es.getMaxFetchUI();
        else
            return config.getDefaultMaxFetchUI();
    }

    protected synchronized Map<String, EntityStatistics> getStatisticsCache() {
        if (statisticsCache == null) {
            statisticsCache = new ConcurrentHashMap<>();
            internalLoadStatisticsCache();
        }
        return statisticsCache;
    }

    protected void internalLoadStatisticsCache() {
        log.info("Loading statistics cache");
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q = em.createQuery("select s from sys$EntityStatistics s");
            List<EntityStatistics> list = q.getResultList();
            for (EntityStatistics es : list) {
                statisticsCache.put(es.getName(), es);
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public synchronized void flushStatisticsCache() {
        statisticsCache = null;
    }

    @Override
    public synchronized EntityStatistics enterStatistics(String name, Long instanceCount, Integer fetchUI, Integer maxFetchUI,
                                               Integer lazyCollectionThreshold, Integer lookupScreenThreshold) {
        Transaction tx = persistence.createTransaction();
        EntityStatistics es;
        try {
            EntityManager em = persistence.getEntityManager();

            es = getEntityStatisticsInstance(name, em);

            if (instanceCount != null) {
                es.setInstanceCount(instanceCount);
            }
            if (fetchUI != null) {
                es.setFetchUI(fetchUI);
            }
            if (maxFetchUI != null) {
                es.setMaxFetchUI(maxFetchUI);
            }
            if (lazyCollectionThreshold != null) {
                es.setLazyCollectionThreshold(lazyCollectionThreshold);
            }
            if (lookupScreenThreshold != null) {
                es.setLookupScreenThreshold(lookupScreenThreshold);
            }

            tx.commit();
        } finally {
            tx.end();
        }
        flushStatisticsCache();
        return es;
    }

    @Override
    public SortedMap<String, EntityStatistics> getEntityStatistics() {
        return new TreeMap<>(getStatisticsCache());
    }

    @Override
    public void deleteStatistics(String name) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q = em.createQuery("delete from sys$EntityStatistics s where s.name = ?1");
            q.setParameter(1, name);
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
        flushStatisticsCache();
    }

    public void refreshStatisticsForEntity(String name) {
        log.debug("Refreshing statistics for entity " + name);
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query q = em.createQuery("select count(e) from " + name + " e");
            Long count = (Long) q.getSingleResult();

            EntityStatistics es = getEntityStatisticsInstance(name, em);
            es.setInstanceCount(count);
            getStatisticsCache().put(name, es);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected EntityStatistics getEntityStatisticsInstance(String name, EntityManager em) {
        Query q = em.createQuery("select s from sys$EntityStatistics s where s.name = ?1");
        q.setParameter(1, name);
        List<EntityStatistics> list = q.getResultList();

        EntityStatistics es;
        if (list.isEmpty()) {
            es = new EntityStatistics();
            es.setName(name);
            em.persist(es);
        } else {
            es = list.get(0);
        }
        return es;
    }
}
