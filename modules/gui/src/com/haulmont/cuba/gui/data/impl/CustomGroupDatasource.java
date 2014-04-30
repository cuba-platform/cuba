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
 * @author gorodnov
 * @version $Id$
 */
public class CustomGroupDatasource<T extends Entity<K>, K> 
        extends CustomCollectionDatasource<T, K>
        implements GroupDatasource<T, K> {

    protected GroupDelegate<T,K> groupDelegate = new GroupDelegate<T, K>(this) {
        @Override
        protected void doSort(SortInfo<MetaPropertyPath>[] sortInfo) {
            CustomGroupDatasource.super.doSort();
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
}
