/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * CollectionDatasource supporting hierarchy of items.
 * @param <T> type of entity
 * @param <K> type of entity ID
 */
public interface HierarchicalDatasource<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

    /** Property of entity which forms the hierarchy */
    String getHierarchyPropertyName();

    /** Set property of entity which forms the hierarchy */
    void setHierarchyPropertyName(String parentPropertyName);

    /** Identifiers of all root items */
    Collection<K> getRootItemIds();

    /** Get parent ID for item with the given ID */
    K getParent(K itemId);

    /** Get children IDs for item with the given ID */
    Collection<K> getChildren(K itemId);

    /** True if item with the given ID is a root item */
    boolean isRoot(K itemId);

    /** True if item with the given ID has children */
    boolean hasChildren(K itemId);
}