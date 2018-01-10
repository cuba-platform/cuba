/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.EntityStatistics;

import java.util.List;
import java.util.SortedMap;

/**
 * Caches database metadata information and entity statistics.
 *
 */
public interface PersistenceManagerAPI {

    String NAME = "cuba_PersistenceManager";

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
