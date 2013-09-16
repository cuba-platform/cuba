/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

import java.util.Collection;
import java.util.List;

/**
 * @author Gorodnov
 * @version $Id$
 */
public class GroupDatasourceImpl<T extends Entity<K>, K>
        extends CollectionDatasourceImpl<T, K>
        implements GroupDatasource<T, K> {

    protected GroupDelegate<T,K> groupDelegate = new GroupDelegate<T, K>(this) {
        @Override
        protected void doSort(SortInfo<MetaPropertyPath>[] sortInfo) {
            GroupDatasourceImpl.super.doSort();
        }
    };

    @Override
    public void groupBy(Object[] properties) {
        groupDelegate.groupBy(properties, sortInfos);
    }

    @Override
    protected void doSort() {
        if (hasGroups()) {
            groupDelegate.doGroupSort(sortInfos);
        } else {
            super.doSort();
        }
    }

    @Override
    public List<GroupInfo> rootGroups() {
        return groupDelegate.rootGroups();
    }

    @Override
    public boolean hasChildren(GroupInfo groupId) {
        return groupDelegate.hasChildren(groupId);
    }

    @Override
    public List<GroupInfo> getChildren(GroupInfo groupId) {
        return groupDelegate.getChildren(groupId);
    }

    @Override
    public Object getGroupProperty(GroupInfo groupId) {
        return groupDelegate.getGroupProperty(groupId);
    }

    @Override
    public Object getGroupPropertyValue(GroupInfo groupId) {
        return groupDelegate.getGroupPropertyValue(groupId);
    }

    @Override
    public Collection<K> getGroupItemIds(GroupInfo groupId) {
        return groupDelegate.getGroupItemIds(groupId);
    }

    @Override
    public int getGroupItemsCount(GroupInfo groupId) {
        return groupDelegate.getGroupItemsCount(groupId);
    }

    @Override
    public boolean hasGroups() {
        return groupDelegate.hasGroups();
    }

    @Override
    public Collection<?> getGroupProperties() {
        return groupDelegate.getGroupProperties();
    }

    @Override
    public boolean containsGroup(GroupInfo groupId) {
        return groupDelegate.containsGroup(groupId);
    }

    @Override
    public K nextItemId(K itemId) {
        if (!groupDelegate.rootGroups().isEmpty()) {
            // Works in bounds of the current top-level group
            for (GroupInfo rootGroup : groupDelegate.rootGroups()) {
                List<K> groupItemIds = groupDelegate.getGroupItemIds(rootGroup);
                for (int i = 0; i < groupItemIds.size(); i++) {
                    if (groupItemIds.get(i).equals(itemId) && i < groupItemIds.size() - 1) {
                        return groupItemIds.get(i + 1);
                    }
                }
            }
            return null;
        } else
            return super.nextItemId(itemId);
    }

    @Override
    public K prevItemId(K itemId) {
        if (!groupDelegate.rootGroups().isEmpty()) {
            for (GroupInfo rootGroup : groupDelegate.rootGroups()) {
                // Works in bounds of the current top-level group
                List<K> groupItemIds = groupDelegate.getGroupItemIds(rootGroup);
                for (int i = 0; i < groupItemIds.size(); i++) {
                    if (groupItemIds.get(i).equals(itemId) && i > 0) {
                        return groupItemIds.get(i - 1);
                    }
                }
            }
            return null;
        } else
            return super.prevItemId(itemId);
    }

    @Override
    public K firstItemId() {
        List<GroupInfo> rootGroups = groupDelegate.rootGroups();
        if (!rootGroups.isEmpty()) {
            List<K> groupItemIds = groupDelegate.getGroupItemIds(rootGroups.get(0));
            if (!groupItemIds.isEmpty())
                return groupItemIds.get(0);
            else
                return null;
        } else {
            return super.firstItemId();
        }
    }

    @Override
    public K lastItemId() {
        List<GroupInfo> rootGroups = groupDelegate.rootGroups();
        if (!rootGroups.isEmpty()) {
            List<K> groupItemIds = groupDelegate.getGroupItemIds(rootGroups.get(rootGroups.size() - 1));
            if (!groupItemIds.isEmpty())
                return groupItemIds.get(groupItemIds.size() - 1);
            else
                return null;
        } else {
            return super.lastItemId();
        }
    }
}
