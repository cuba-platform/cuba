/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.EntityStatistics;

import java.util.List;
import java.util.SortedMap;

/**
 * Caches database metadata information and entity statistics.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface PersistenceManagerAPI {

    String NAME = "cuba_PersistenceManager";

    /**
     * Whether this database table supports soft deletion.
     * @param table table name
     * @return      true if this database table supports soft deletion
     */
    boolean isSoftDeleteFor(String table);

    /**
     * @return all soft delete tables sorted in alphabetical order
     */
    List<String> getSoftDeleteTables();

    /**
     * Checks whether the table provided is a ManyToMany link table.
     * @param table table name
     * @return      true/false
     */
    boolean isManyToManyLinkTable(String table);

    /**
     * Whether to use a lazy collection datasource for this entity, based on current statistics.
     * @param entityName    entity name
     * @return              true if lazy collection datasource should be used for this entity
     */
    boolean useLazyCollection(String entityName);

    /**
     * Whether to use a lookup screen or a dropdown for this entity, based on current statistics.
     * @param entityName    entity name
     * @return              true if lookup screen should be used
     */
    boolean useLookupScreen(String entityName);

    /**
     * Return a limit of rows fetched for UI components in 'normal' conditions
     * @param entityName    entity name
     * @return              maximum number of rows
     */
    int getFetchUI(String entityName);

    /**
     * Return the absolute maximum number of rows that can be fetched for UI components
     * @param entityName    entity name
     * @return              maximum number of rows
     */
    int getMaxFetchUI(String entityName);

    void flushStatisticsCache();

    void refreshStatisticsForEntity(String name);

    void deleteStatistics(String name);

    EntityStatistics enterStatistics(String name, Long instanceCount, Integer fetchUI, Integer maxFetchUI,
                         Integer lazyCollectionThreshold, Integer lookupScreenThreshold);

    SortedMap<String, EntityStatistics> getEntityStatistics();
}
