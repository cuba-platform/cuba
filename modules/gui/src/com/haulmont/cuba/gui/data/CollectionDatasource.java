/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:06:58
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.gui.components.AggregationInfo;

import java.util.Collection;
import java.util.Map;

/**
 * Datasource containing a collection of entity instances.
 * <br>Usually loaded by query defined in XML descriptor, see {@link #setQuery(String)} for the
 * list of possible query parameters.
 * @param <T> type of entity
 * @param <K> type of entity ID
 */
public interface CollectionDatasource<T extends Entity<K>, K> extends Datasource<T> {

    /** Mode of fetching data from database: {@link #ALL}, {@link #LAZY} */
    enum FetchMode {
        /** Datasource will load all data at once */
        ALL,
        /** Datasource will try to load data by chunks per UI component request */
        LAZY
    }

    /** Get item by ID */
    T getItem(K key);

    /** Get item ID */
    K getItemId(T item);

    /** Get all item IDs */
    Collection<K> getItemIds();

    /**
     * Size of the underlying collection. For {@link FetchMode#LAZY} datasource it is not equal
     * to the size of currently loaded data. 
     */
    int size();

    /** Add item to the collection. The datasource becomes modified. */
    void addItem(T item) throws UnsupportedOperationException;

    /** Remove item to the collection. The datasource becomes modified. */
    void removeItem(T item) throws UnsupportedOperationException;

    /** Updates item in the collection if it is already there. The datasource becomes modified. */
    void modifyItem(T item);

    /** Updates item in the collection if it is already there. Does not affect the "modified" state */
    void updateItem(T item);

    /** True if this datasource supports soft deletion functionality. Corresponds to EntityManager.setSoftDeletion(boolean) */
    boolean isSoftDeletion();

    /** Switch on/off soft deletion functionality. Corresponds to EntityManager.setSoftDeletion(boolean) */
    void setSoftDeletion(boolean softDeletion);

    /** True if the underlying collection contains an item with the specified ID */
    boolean containsItem(K itemId);

    /** Query string which is used to load data. Implementation-dependent (JPQL, Groovy, etc.). */
    String getQuery();

    /** Query filter associated with {@link #getQuery()} */
    QueryFilter getQueryFilter();

    /**
     * Set query string which is used to load data. Implementation-dependent (JPQL, Groovy, etc.).
     * <br>The query may use the following parameters, distiguished by prefix:
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
     * Max number of rows. 0 in case of no limits.<br>
     * Implementations may or may not take this parameter into account.
     */
    int getMaxResults();

    /**
     * Set max number of rows. 0 in case of no limits.<br>
     * Implementations may or may not take this parameter into account.
     */
    void setMaxResults(int maxResults);

    /**
     * Refresh datasource passing specified parameters to the query.
     * These parameters may be referenced in the query text by "custom$" prefix.
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
        K firstItemId();
        K lastItemId();

        K nextItemId(K itemId);
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

        /** Perform sort */
        void sort(SortInfo[] sortInfos);
    }

    /**
     * CollectionDatasource which supports an aggregation of data
     * @param <T> type of entity
     * @param <K> type of entity ID
     */
    interface Aggregatable<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        /**
         * Perform aggregation
         */
        Map<Object, String> aggregate(AggregationInfo[] aggregationInfos, Collection<K> itemIds);
    }

    /**
     * CollectionDatasource with lazy loading
     * @param <T> type of entity
     * @param <K> type of entity ID
     */
    interface Lazy<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        boolean isCompletelyLoaded();
    }

    interface Suspendable<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

        boolean isSuspended();
        void setSuspended(boolean suspended);

        void refreshIfNotSuspended();
    }
}
