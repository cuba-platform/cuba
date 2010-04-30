/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 10.11.2009 18:18:27
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;
import java.util.List;

/**
 * CollectionDatasource which supports a grouping of items by the list of properties
 * @param <T> type of entity
 * @param <K> type of entity ID
 */
public interface GroupDatasource<T extends Entity<K>, K> extends CollectionDatasource<T, K> {
    /**
     * Perform grouping
     * @param properties the list of properties for a grouping
     */
    void groupBy(Object[] properties);

    List<GroupInfo> rootGroups();

    boolean hasChildren(GroupInfo groupId);

    List<GroupInfo> getChildren(GroupInfo groupId);

    Object getGroupProperty(GroupInfo groupId);

    Object getGroupPropertyValue(GroupInfo groupId);

    Collection<K> getGroupItemIds(GroupInfo groupId);

    int getGroupItemsCount(GroupInfo groupId);

    boolean hasGroups();

    Collection<?> getGroupProperties();

    boolean containsGroup(GroupInfo groupId);
}
