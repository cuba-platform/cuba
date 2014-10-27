/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.PersistenceManagerAPI}.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Manages entity statistics and updates database")
public interface PersistenceManagerMBean {

    String getDbmsType();
    String getDbmsVersion();

    int getDefaultLookupScreenThreshold();
    void setDefaultLookupScreenThreshold(int value);

    int getDefaultLazyCollectionThreshold();
    void setDefaultLazyCollectionThreshold(int value);

    int getDefaultFetchUI();
    void setDefaultFetchUI(int value);

    int getDefaultMaxFetchUI();
    void setDefaultMaxFetchUI(int value);

    /**
     * Show list of tables supporting soft deletion.
     * @return  operation result
     */
    @ManagedOperation(description = "Show list of tables supporting soft deletion")
    String printSoftDeleteTables();

    @ManagedOperation(description = "Show list of views with properties from ViewRepository")
    String printViewRepositoryDump();

    @ManagedOperation(description = "Print list of views with properties from ViewRepository as HTML markup")
    String printViewRepositoryDumpHtml();

    /**
     * Start the database update.
     * @param token 'update' string must be passed to avoid accidental invocation
     * @return  operation result
     */
    @ManagedOperation(description = "Start the database update")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "token", description = "Enter 'update' here")})
    String updateDatabase(String token);

    /**
     * Show database update scripts that will be executed on next update.
     * @return  operation result
     */
    @ManagedOperation(description = "Show database update scripts that will be executed on next update")
    String findUpdateDatabaseScripts();

    /**
     * Execute a JPQL query.
     * <p>The query may contain security-related parameters specified in
     * {@link com.haulmont.cuba.core.PersistenceSecurity}. This is a way to test some JPQL code in the real server
     * environment</p>
     * @param queryString   JPQL query string
     * @return              list of loaded entities as string
     */
    @ManagedOperation(description = "Execute a JPQL query")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "queryString",
            description = "May contain security-related parameters: session$userLogin, session$userId, session$userGroupId, session$<SESSION_ATTRIBUTE>")})
    String jpqlLoadList(String queryString);

    /**
     * Execute a JPQL update statement.
     * @param queryString   JPQL update statement
     * @param softDeletion  soft deletion sign
     * @return              number of entity instances affected by update
     */
    @ManagedOperation(description = "Execute a JPQL update statement")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "queryString", description = ""),
            @ManagedOperationParameter(name = "softDeletion", description = "")
    })
    String jpqlExecuteUpdate(String queryString, boolean softDeletion);

    /**
     * Calculate and refresh statistics for the specified entity. This method updates statistics in the database table
     * and in cache.
     * @param entityName    entity name or 'all' to refresh for all entities
     * @return              operation result
     */
    @ManagedOperation(description = "Calculate and refresh statistics for the specified entity")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "entityName",
            description = "MetaClass name, e.g. 'sec$User', or 'all' to refresh for all entities")})
    String refreshStatistics(String entityName);

    /**
     * Show current statistics for the specified entity.
     * @param entityName    entity name or blank to show all entities
     * @return              operation result
     */
    @ManagedOperation(description = "Show current statistics for the specified entity")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "entityName", description = "MetaClass name, e.g. sec$User")})
    String showStatistics(String entityName);

    /**
     * Manually update statistics for an entity.
     */
    @ManagedOperation(description = "Enter statistics for the specified entity")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "entityName", description = "MetaClass name, e.g. sec$User"),
            @ManagedOperationParameter(name = "instanceCount", description = ""),
            @ManagedOperationParameter(name = "fetchUI", description = ""),
            @ManagedOperationParameter(name = "maxFetchUI", description = ""),
            @ManagedOperationParameter(name = "lazyCollectionThreshold", description = ""),
            @ManagedOperationParameter(name = "lookupScreenThreshold", description = "")
    })
    String enterStatistics(String entityName, Long instanceCount, Integer fetchUI, Integer maxFetchUI,
                           Integer lazyCollectionThreshold, Integer lookupScreenThreshold);

    @ManagedOperation(description = "Delete statistics for the specified entity")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "entityName", description = "MetaClass name, e.g. sec$User")
    })
    String deleteStatistics(String entityName);

    /**
     * Flush statistics cache. It will be loaded on a next request.
     * @return  operation result
     */
    @ManagedOperation(description = "Flush statistics cache. It will be reloaded on a next request")
    String flushStatisticsCache();
}
