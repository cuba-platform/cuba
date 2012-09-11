/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.EntityStatistics;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.DBNotInitializedException;
import com.haulmont.cuba.core.sys.DbUpdater;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.io.File;
import java.net.URI;
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
public class PersistenceManager extends ManagementBean implements PersistenceManagerMBean, PersistenceManagerAPI {

    private static Log log = LogFactory.getLog(PersistenceManager.class);

    private boolean metadataLoaded;

    private Set<String> softDeleteTables = new HashSet<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Map<String, EntityStatistics> statisticsCache;

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Inject
    private DbUpdater dbUpdater;

    @Inject
    private PersistenceSecurity security;

    private PersistenceConfig config;

    private Configuration configuration;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        config = configuration.getConfig(PersistenceConfig.class);
    }

    private void initDbMetadata() {
        log.info("Initializing DB metadata");
        DataSource datasource = persistence.getDataSource();
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, null, persistence.getDbDialect().getDeleteTsColumn());
            lock.writeLock().lock();
            try {
                softDeleteTables.clear();
                while (rs.next()) {
                    String table = rs.getString("TABLE_NAME");
                    if (table != null)
                        softDeleteTables.add(table.toLowerCase());
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
    public int getDefaultLookupScreenThreshold() {
        return config.getDefaultLookupScreenThreshold();
    }

    @Override
    public void setDefaultLookupScreenThreshold(int value) {
        config.setDefaultLookupScreenThreshold(value);
    }

    @Override
    public int getDefaultLazyCollectionThreshold() {
        return config.getDefaultLazyCollectionThreshold();
    }

    @Override
    public void setDefaultLazyCollectionThreshold(int value) {
        config.setDefaultLazyCollectionThreshold(value);
    }

    @Override
    public int getDefaultFetchUI() {
        return config.getDefaultFetchUI();
    }

    @Override
    public void setDefaultFetchUI(int value) {
        config.setDefaultFetchUI(value);
    }

    @Override
    public int getDefaultMaxFetchUI() {
        return config.getDefaultMaxFetchUI();
    }

    @Override
    public void setDefaultMaxFetchUI(int value) {
        config.setDefaultMaxFetchUI(value);
    }

    @Override
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

    @Override
    public String updateDatabase(String token) {
        if (!"update".equals(token))
            return "Pass 'update' in the method parameter if you really want to update database.";
        try {
            dbUpdater.updateDatabase();
            return "Updated";
        } catch (Throwable e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String findUpdateDatabaseScripts() {
        try {
            List<String> list = dbUpdater.findUpdateDatabaseScripts();
            if (!list.isEmpty()) {
                String dbDirName = configuration.getConfig(ServerConfig.class).getDbDir();
                File dbDir = new File(dbDirName);
                URI dbDirUri = dbDir.toURI();

                String indent = "\t";
                StrBuilder sb = new StrBuilder();
                sb.append(dbDir.getPath().replace('\\', '/') + "\n");
                for (String path : list) {
                    URI file = new File(path).toURI();
                    sb.append(indent + dbDirUri.relativize(file).getPath() + "\n");
                }

                return sb.toString();
            } else
                return "No updates available";
        } catch (DBNotInitializedException e) {
            return e.getMessage();
        } catch (Throwable e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String jpqlLoadList(String queryString) {
        try {
            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
                Query query = em.createQuery(queryString);
                QueryParser parser = QueryTransformerFactory.createParser(queryString);
                Set<String> paramNames = parser.getParamNames();
                for (String paramName : paramNames) {
                    security.setQueryParam(query, paramName);
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

    @Override
    public String jpqlExecuteUpdate(String queryString, boolean softDeletion) {
        try {
            login();
            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
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

    private synchronized Map<String, EntityStatistics> getStatisticsCache() {
        if (statisticsCache == null) {
            statisticsCache = new ConcurrentHashMap<>();
            internalLoadStatisticsCache();
        }
        return statisticsCache;
    }

    private void internalLoadStatisticsCache() {
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
    public synchronized String flushStatisticsCache() {
        try {
            statisticsCache = null;
            return "Done";
        } catch (Exception e) {
            log.error("flushStatisticsCache error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String refreshStatistics(String entityName) {
        if (StringUtils.isBlank(entityName))
            return "Pass an entity name (MetaClass name, e.g. sec$User) or 'all' to refresh statistics for all entities.\n" +
                    "Be careful, it can take very long time.";

        try {
            log.info("Refreshing statistics for " + entityName);
            login();
            if ("all".equals(entityName)) {
                for (MetaClass metaClass : metadata.getSession().getClasses()) {
                    Class javaClass = metaClass.getJavaClass();
                    Table annotation = (Table) javaClass.getAnnotation(Table.class);
                    if (annotation != null) {
                        refreshStatisticsForEntity(metaClass.getName());
                    }
                }
            } else {
                MetaClass metaClass = metadata.getSession().getClass(entityName);
                if (metaClass == null)
                    return "MetaClass not found: " + entityName;
                Class javaClass = metaClass.getJavaClass();
                Table annotation = (Table) javaClass.getAnnotation(Table.class);
                if (annotation != null) {
                    refreshStatisticsForEntity(metaClass.getName());
                }
            }
            return "Done";
        } catch (Exception e) {
            log.error("refreshStatistics error", e);
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    @Override
    public String showStatistics(String entityName) {
        try {
            if (StringUtils.isBlank(entityName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Displaying statistics for all entities.\n");
                sb.append("To show a particular entity only, pass its name in the method parameter.\n\n");

                List<String> names = new ArrayList(getStatisticsCache().keySet());
                Collections.sort(names);

                for (String name : names) {
                    sb.append(getStatisticsCache().get(name)).append("\n");
                }
                return sb.toString();
            } else {
                EntityStatistics es = getStatisticsCache().get(entityName);
                return es == null ? "No such entity" : es.toString();
            }
        } catch (Exception e) {
            log.error("showStatistics error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public synchronized String enterStatistics(String name, Long instanceCount, Integer fetchUI, Integer maxFetchUI,
                                               Integer lazyCollectionThreshold, Integer lookupScreenThreshold) {
        if (StringUtils.isBlank(name))
            return "Entity name is required";
        try {
            login();
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

            statisticsCache = null;

            StringBuilder sb = new StringBuilder("Statistics for ").append(name).append(" changed:\n");
            sb.append("instanceCount=").append(es.getInstanceCount()).append("\n");
            sb.append("fetchUI=").append(es.getFetchUI()).append("\n");
            sb.append("maxFetchUI=").append(es.getMaxFetchUI()).append("\n");
            sb.append("lazyCollectionThreshold=").append(es.getLazyCollectionThreshold()).append("\n");
            sb.append("lookupScreenThreshold=").append(es.getLookupScreenThreshold()).append("\n");
            return sb.toString();
        } catch (Exception e) {
            log.error("enterStatistics error", e);
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    @Override
    public String deleteStatistics(String name) {
        if (StringUtils.isBlank(name))
            return "Entity name is required";
        try {
            login();
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

            statisticsCache = null;

            return "Entity statistics for " + name + " has been deleted";
        } catch (Exception e) {
            log.error("deleteStatistics error", e);
            return ExceptionUtils.getStackTrace(e);
        } finally {
            logout();
        }
    }

    private void refreshStatisticsForEntity(String name) {
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

    private EntityStatistics getEntityStatisticsInstance(String name, EntityManager em) {
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
