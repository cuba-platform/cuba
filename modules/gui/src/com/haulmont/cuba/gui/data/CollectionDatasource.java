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
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.core.global.filter.QueryFilter;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Datasource containing a collection of entity instances.
 *
 * @param <T> type of entity
 * @param <K> type of entity ID
 *
 * @see #setQuery(String)
 *
 */
public interface CollectionDatasource<T extends Entity<K>, K> extends Datasource<T> {

    /**
     * @return item by ID, can be null
     */
    @Nullable
    T getItem(K id);

    /**
     * @return item by ID. Throws exception if not found.
     */
    T getItemNN(K id);

    /**
     * @return  all item IDs
     */
    Collection<K> getItemIds();

    /**
     * In standard implementations this is a wrapper method around {@link #getItemIds()} and {@link #getItem(Object)}.
     * Use it only if you really need the collection of items. Otherwise use {@link #getItemIds()} or {@link #size()}
     * directly.
     *
     * @return collection of all items
     */
    Collection<T> getItems();

    /**
     * @return size of the underlying collection.
     */
    int size();

    /**
     * Add an item to the collection. The datasource becomes modified.
     */
    void addItem(T item);

    /**
     * Remove an item from the collection. The datasource becomes modified.
     */
    void removeItem(T item);

    /**
     * Exclude an item from the collection. The datasource "modified" state doesn't change.
     */
    void excludeItem(T item);

    /**
     * Include an item into the collection. The datasource "modified" state doesn't change.
     */
    void includeItem(T item);

    /**
     * Clear the underlying collection. The datasource "modified" state doesn't change.
     */
    void clear();

    /**
     * Revert the datasource to its initial state before data modification.
     */
    void revert();

    /**
     * Update an item in the collection if it is already there. The datasource becomes modified.
     */
    void modifyItem(T item);

    /**
     * Update an item in the collection if it is already there. Does not affect the "modified" state.
     */
    void updateItem(T item);

    /**
     * Suspend invocation of <code>collectionChanged</code> method of registered {@link CollectionChangeListener}s.
     * It makes sense in case of massive updates of the datasource by {@link #addItem(com.haulmont.cuba.core.entity.Entity)}
     * or similar methods.
     * After that, <code>collectionChanged</code> will be invoked once on {@link #resumeListeners()} call.
     * <p/>Usage example:
     * <pre>
     * ds.suspendListeners();
       try {
           for (Object item : items) {
               ds.addItem(item);
           }
       } finally {
           ds.resumeListeners();
       }
     * </pre>
     */
    void suspendListeners();

    /**
     * Resume invocation of <code>collectionChanged</code> method of registered {@link CollectionChangeListener}s
     * after calling {@link #suspendListeners()}.
     * It will call <code>collectionChanged</code> just once, doesn't matter how many updates were issued
     * since the previous {@link #suspendListeners()} call.
     * <p/>This method should be called in <code>finally</code> section.
     */
    void resumeListeners();

    /**
     * @return true if this datasource is in Soft Delete mode.
     */
    boolean isSoftDeletion();

    /**
     * Switch on/off Soft Deletion.
     */
    void setSoftDeletion(boolean softDeletion);

    /**
     * @return true if the underlying collection contains an item with the specified ID
     */
    boolean containsItem(K itemId);

    /**
     * @return Query string which is used to load data. Implementation-dependent (JPQL, Groovy, etc.).
     * Can be null.
     */
    String getQuery();

    /**
     * @return load context that can be used to load the same data as contained in the datasource.
     * Can be null.
     */
    LoadContext getCompiledLoadContext();

    /**
     * @return Current query filter. Can be null.
     */
    QueryFilter getQueryFilter();

    /**
     * Set query string which is used to load data. Implementation-dependent (JPQL, Groovy, etc.).
     * <p/> The query may use the following parameters, distinguished by prefix:
     * <ul>
     * <li><code>ds$</code> - current item in the specified datasource
     * <li><code>component$</code> - value of the specified UI component
     * <li><code>param$</code> - value of parameter passed to the window when opening it
     * <li><code>session$</code> - <code>userId</code> represents current or substituted user ID,
     * <code>userLogin</code> represents current or substituted user login in lower case,
     * any other string represents a user session attribute with this name
     * <li><code>custom$</code> - value of parameter passed to the {@link #refresh(java.util.Map)} method 
     * </ul>
     */
    void setQuery(String query);

    /**
     * Set query string and associated filter which is used to load data. 
     * Query is implementation-dependent (JPQL, Groovy, etc.).
     * <br>See {@link #setQuery(String)} for the list of supported query parameters.
     */
    void setQuery(String query, QueryFilter filter);

    /**
     * Set query filter which is used to load data. Query remains the same.
     * Query is implementation-dependent (JPQL, Groovy, etc.).
     * <br>See {@link #setQuery(String)} for the list of supported query parameters.
     */
    void setQueryFilter(QueryFilter filter);

    /**
     * @return Max number of rows. 0 in case of no limits.
     */
    int getMaxResults();

    /**
     * Set max number of rows. 0 in case of no limits.
     * <p/> Implementations may or may not take this parameter into account.
     */
    void setMaxResults(int maxResults);

    /**
     * Refresh datasource passing specified parameters to the query.
     * <p>These parameters may be referenced in the query text by "custom$" prefix.</p>
     * @param parameters    parameters map
     */
    void refresh(Map<String, Object> parameters);

    /**
     * Whether to refresh datasource on changing value of a component which it depends on
     * (through <code>component$</code> parameter)
     */
    boolean getRefreshOnComponentValueChange();

    /**
     * Whether to refresh datasource on changing value of a component which it depends on
     * (through <code>component$</code> parameter)
     */
    void setRefreshOnComponentValueChange(boolean refresh);

    /**
     * CollectionDatasource which underlying collection is ordered.
     * Supports predictable navigation between items.
     * @param <T> type of entity
     * @param <K> type of entity ID
     */
    interface Ordered<T extends Entity<K>, K> extends CollectionDatasource<T, K> {
        @Nullable
        K firstItemId();
        @Nullable
        K lastItemId();

        @Nullable
        K nextItemId(K itemId);
        @Nullable
        K prevItemId(K itemId);

        boolean isFirstId(K itemId);
        boolean isLastId(K itemId);
    }

    /**
     * Ordered CollectionDatasource supporting order change.
     * @param <T> type of entity
     * @param <K> type of entity ID
     */
    interface Sortable<T extends Entity<K>, K> extends Ordered<T, K> {

        /** Sort order */
        enum Order {
            ASC,
            DESC
        }

        /** How to sort */
        class SortInfo<P> {
            P propertyPath;
            Order order;

            public P getPropertyPath() {
                return propertyPath;
            }

            public void setPropertyPath(P propertyPath) {
                this.propertyPath = propertyPath;
            }

            public Order getOrder() {
                return order;
            }

            public void setOrder(Order order) {
                this.order = order;
            }
        }

        /**
         * Perform sorting
         */
        void sort(SortInfo[] sortInfos);

        /**
         * Remove sort infos
         */
        void resetSortOrder();
    }

    /**
     * CollectionDatasource which supports data aggregation.
     * @param <T> type of entity
     * @param <K> type of entity ID
     */
    interface Aggregatable<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        /**
         * Perform aggregation
         */
        Map<AggregationInfo, String> aggregate(AggregationInfo[] aggregationInfos, Collection<K> itemIds);
    }

    /**
     * CollectionDatasource with lazy loading.
     * @param <T> type of entity
     * @param <K> type of entity ID
     */
    interface Lazy<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        boolean isCompletelyLoaded();
    }

    /**
     * CollectionDatasource that supports counting records in database and loading by pages.
     * @param <T> type of entity
     * @param <K> type of entity ID
     */
    interface SupportsPaging<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        /** Returns count of records in datatabase for the current query and filter */
        int getCount();

        /**  */
        int getFirstResult();
        void setFirstResult(int startPosition);
    }

    /**
     * CollectionDatasource that supports defferred refresh.
     * When it is in suspended state, it doesn't actually refresh on refreshIfNotSuspended() calls, but refreshes
     * once after switch to not supended.
     */
    interface Suspendable<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        boolean isSuspended();
        void setSuspended(boolean suspended);

        void refreshIfNotSuspended();
    }

    /**
     * CollectionDatasource that supports applying filter to previously selected data.
     */
    interface SupportsApplyToSelected<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        void pinQuery();
        void unpinLastQuery();
        void unpinAllQuery();
    }

    /**
     * Mode of loading data from database: {@link #ALWAYS}, {@link #NEVER}
     */
    enum RefreshMode {
        /**
         * Datasource will load data for each refresh
         */
        ALWAYS,
        /**
         * Datasource will never load data from database
         */
        NEVER
    }

    interface SupportsRefreshMode<T extends Entity<K>, K> extends CollectionDatasource<T,K> {
        RefreshMode getRefreshMode();
        void setRefreshMode(RefreshMode refreshMode);
    }

    /** Operation which caused the datasource change. */
    enum Operation {
        REFRESH,
        CLEAR,
        ADD,
        REMOVE,
        UPDATE
    }

    class CollectionChangeEvent<T extends Entity<K>, K> {
        private final CollectionDatasource ds;
        private final Operation operation;
        private final List<T> items;

        public CollectionChangeEvent(CollectionDatasource ds, Operation operation, List<T> items) {
            this.ds = ds;
            this.operation = operation;
            this.items = items;
        }

        /**
         * @return datasource
         */
        public CollectionDatasource<T, K> getDs() {
            return ds;
        }

        /**
         * @return items which used in operation, in case of {@link Operation#REFRESH} or {@link Operation#CLEAR}
         * equals {@link java.util.Collections#emptyList()}
         */
        public List<T> getItems() {
            return items;
        }

        /**
         * @return operation which caused the datasource change
         */
        public Operation getOperation() {
            return operation;
        }
    }

    interface CollectionChangeListener<T extends Entity<K>, K> {
        /**
         * Enclosed collection changed.
         */
        void collectionChanged(CollectionChangeEvent<T, K> e);
    }

    void addCollectionChangeListener(CollectionChangeListener<T, K> listener);
    void removeCollectionChangeListener(CollectionChangeListener<T, K> listener);
}