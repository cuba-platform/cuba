/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
    /** Perform grouping by the list of properties */
    void groupBy(Object[] properties);

    /** Returns the list of root groups */
    List<GroupInfo> rootGroups();

    /** Indicates that group has nested groups */
    boolean hasChildren(GroupInfo groupId);

    /** Returns the list of nested groups */
    List<GroupInfo> getChildren(GroupInfo groupId);

    /** Returns a group property */
    Object getGroupProperty(GroupInfo groupId);

    /** Returns a group property value */
    Object getGroupPropertyValue(GroupInfo groupId);

    /** Returns item ids that are contained in the selected group */
    Collection<K> getGroupItemIds(GroupInfo groupId);

    /** Returns a count of items that are contained in the selected group */
    int getGroupItemsCount(GroupInfo groupId);

    /** Indicated that a datasource has groups */
    boolean hasGroups();

    /** Returns group properties */
    Collection<?> getGroupProperties();

    /** Indicates that a group is contained in the groups tree */
    boolean containsGroup(GroupInfo groupId);
}
