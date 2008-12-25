/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:06:58
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import java.util.Collection;

public interface CollectionDatasource<T, K> extends Datasource<T> {
    T getItem(K key);

    Collection<K> getItemIds();
    int size();

    void addItem(T item) throws UnsupportedOperationException;
    void removeItem(T item) throws UnsupportedOperationException;

    interface Sortable<K> {
        enum Order {
            ASC,
            DESC
        }

        class SortInfo {
            Object property;
            Order order;
        }

        Collection<K> getSortedItemIds();
        void sort(SortInfo[] sortInfos);
    }
}
