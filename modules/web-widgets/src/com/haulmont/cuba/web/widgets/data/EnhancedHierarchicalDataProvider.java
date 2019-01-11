/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.web.widgets.data;

/**
 * Interface defining methods for enhancing {@link com.vaadin.data.provider.HierarchicalDataProvider} behavior.
 *
 * @param <T> data type
 */
public interface EnhancedHierarchicalDataProvider<T> {

    /**
     * Returns the hierarchy level of an item.
     *
     * @param item the item to get level
     * @return the level of the given item
     */
    int getLevel(T item);
}
