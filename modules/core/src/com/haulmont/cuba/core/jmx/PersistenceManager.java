/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.PersistenceConfig;
import com.haulmont.cuba.core.app.PersistenceManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.EntityStatistics;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.QueryParser;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.core.sys.DBNotInitializedException;
import com.haulmont.cuba.core.sys.DbUpdater;
import com.haulmont.cuba.security.app.Authenticated;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.persistence.Table;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_PersistenceManagerMBean")
public class PersistenceManager implements PersistenceManagerMBean {

    protected static Log log = LogFactory.getLog(PersistenceManager.class);

    @Inject
    protected PersistenceManagerAPI persistenceManager;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DbUpdater dbUpdater;

    @Inject
    protected PersistenceSecurity security;

    protected PersistenceConfig persistenceConfig;

    protected ServerConfig serverConfig;

    @Inject
    public void setConfiguration(Configuration configuration) {
        persistenceConfig = configuration.getConfig(PersistenceConfig.class);
        serverConfig = configuration.getConfig(ServerConfig.class);
    }

    @Override
    public int getDefaultLookupScreenThreshold() {
        return persistenceConfig.getDefaultLookupScreenThreshold();
    }

    @Authenticated
    @Override
    public void setDefaultLookupScreenThreshold(int value) {
        persistenceConfig.setDefaultLookupScreenThreshold(value);
    }

    @Override
    public int getDefaultLazyCollectionThreshold() {
        return persistenceConfig.getDefaultLazyCollectionThreshold();
    }

    @Authenticated
    @Override
    public void setDefaultLazyCollectionThreshold(int value) {
        persistenceConfig.setDefaultLazyCollectionThreshold(value);
    }

    @Override
    public int getDefaultFetchUI() {
        return persistenceConfig.getDefaultFetchUI();
    }

    @Authenticated
    @Override
    public void setDefaultFetchUI(int value) {
        persistenceConfig.setDefaultFetchUI(value);
    }

    @Override
    public int getDefaultMaxFetchUI() {
        return persistenceConfig.getDefaultMaxFetchUI();
    }

    @Authenticated
    @Override
    public void setDefaultMaxFetchUI(int value) {
        persistenceConfig.setDefaultMaxFetchUI(value);
    }

    @Override
    public String printSoftDeleteTables() {
        StringBuilder sb = new StringBuilder();
        for (String table : persistenceManager.getSoftDeleteTables()) {
            sb.append(table).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String printViewRepositoryDump() {
        return new ViewRepositoryInfo(metadata).dump();
    }

    @Override
    public String printViewRepositoryDumpHtml() {
        return new ViewRepositoryInfo(metadata).dumpHtml();
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
                File dbDir = new File(serverConfig.getDbDir());
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

    @Authenticated
    @Override
    public String jpqlExecuteUpdate(String queryString, boolean softDeletion) {
        try {
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
        }
    }

    @Override
    public synchronized String flushStatisticsCache() {
        try {
            persistenceManager.flushStatisticsCache();
            return "Done";
        } catch (Exception e) {
            log.error("flushStatisticsCache error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Authenticated
    @Override
    public String refreshStatistics(String entityName) {
        if (StringUtils.isBlank(entityName))
            return "Pass an entity name (MetaClass name, e.g. sec$User) or 'all' to refresh statistics for all entities.\n" +
                    "Be careful, it can take very long time.";

        try {
            log.info("Refreshing statistics for " + entityName);
            if ("all".equals(entityName)) {
                for (MetaClass metaClass : metadata.getSession().getClasses()) {
                    Class javaClass = metaClass.getJavaClass();
                    Table annotation = (Table) javaClass.getAnnotation(Table.class);
                    if (annotation != null) {
                        persistenceManager.refreshStatisticsForEntity(metaClass.getName());
                    }
                }
            } else {
                MetaClass metaClass = metadata.getSession().getClass(entityName);
                if (metaClass == null)
                    return "MetaClass not found: " + entityName;
                Class javaClass = metaClass.getJavaClass();
                Table annotation = (Table) javaClass.getAnnotation(Table.class);
                if (annotation != null) {
                    persistenceManager.refreshStatisticsForEntity(metaClass.getName());
                }
            }
            return "Done";
        } catch (Exception e) {
            log.error("refreshStatistics error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String showStatistics(String entityName) {
        try {
            Map<String, EntityStatistics> statistics = persistenceManager.getEntityStatistics();
            if (StringUtils.isBlank(entityName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Displaying statistics for all entities.\n");
                sb.append("To show a particular entity only, pass its name in the method parameter.\n\n");

                for (String name : statistics.keySet()) {
                    sb.append(statistics.get(name)).append("\n");
                }
                return sb.toString();
            } else {
                EntityStatistics es = statistics.get(entityName);
                return es == null ? "No such entity" : es.toString();
            }
        } catch (Exception e) {
            log.error("showStatistics error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Authenticated
    @Override
    public synchronized String enterStatistics(String name, Long instanceCount, Integer fetchUI, Integer maxFetchUI,
                                               Integer lazyCollectionThreshold, Integer lookupScreenThreshold) {
        if (StringUtils.isBlank(name))
            return "Entity name is required";
        try {
            EntityStatistics es = persistenceManager.enterStatistics(
                    name, instanceCount, fetchUI, maxFetchUI, lazyCollectionThreshold, lookupScreenThreshold);

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
        }
    }

    @Authenticated
    @Override
    public String deleteStatistics(String name) {
        if (StringUtils.isBlank(name))
            return "Entity name is required";
        try {
            persistenceManager.deleteStatistics(name);
            return "Entity statistics for " + name + " has been deleted";
        } catch (Exception e) {
            log.error("deleteStatistics error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
