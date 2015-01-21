/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;

import java.util.Map;
import java.util.UUID;

/**
 * Interface provides full text search functionality necessary to generic filter component.
 * Its implementation is in FTS module.
 *
 * @author gorbunkov
 * @version $Id$
 */
public interface FtsFilterHelper {
    String NAME = "cuba_FtsFilterHelper";

    /**
     * Checks whether an entity is indexed by full text search engine
     */
    boolean isEntityIndexed(String entityName);

    /**
     * Performs full text search and stores founded entity ids as {@link com.haulmont.cuba.core.entity.QueryResult}
     * records in database.
     * @param entityName name of entity from datasource. Please note that result will contain not only
     *                   entities with given name but its descendants as well
     * @return search result that contains queryKey and map
     * that holds search result descriptions (see {@link FtsSearchResult})
     */
    FtsSearchResult search(String searchTerm, String entityName);

    /**
     * Creates a filter condition that joins a set of entities stored with given @{code queryKey}
     */
    CustomCondition createFtsCondition(int queryKey);

    /**
     * Class for holding search result.
     * <p>
     *     {@code queryKey} property contains an unique identifier for stored entities set
     * </p>
     * <p>
     *     {@code hitInfos} property is a map. Key is entity id, value is a text that describes
     *     where search term was found in current entity
     * </p>
     */
    class FtsSearchResult {
        private int queryKey;
        private Map<UUID, String> hitInfos;

        public int getQueryKey() {
            return queryKey;
        }

        public void setQueryKey(int queryKey) {
            this.queryKey = queryKey;
        }

        public Map<UUID, String> getHitInfos() {
            return hitInfos;
        }

        public void setHitInfos(Map<UUID, String> hitInfos) {
            this.hitInfos = hitInfos;
        }
    }

}
