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

import java.util.Collection;
import java.util.Map;

public interface CollectionDatasource<T extends Entity, K> extends Datasource<T> {
    enum FetchMode {
        ALL,
        LAZY
    }

    T getItem(K key);
    K getItemId(T item);

    Collection<K> getItemIds();
    int size();

    void addItem(T item) throws UnsupportedOperationException;
    void removeItem(T item) throws UnsupportedOperationException;
    void updateItem(T item);

    boolean containsItem(K itemId);

    interface Ordered<T extends Entity, K> extends CollectionDatasource<T, K> {
        K firstItemId();
        K lastItemId();

        K nextItemId(K itemId);
        K prevItemId(K itemId);

        boolean isFirstId(K itemId);
        boolean isLastId(K itemId);
    }

    interface Sortable<T extends Entity, K> extends Ordered<T, K>{
        enum Order {
            ASC,
            DESC
        }

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

        void sort(SortInfo[] sortInfos);
    }

    String getQuery();
    void setQuery(String query);

    void refresh(Map<String, Object> parameters);
}
