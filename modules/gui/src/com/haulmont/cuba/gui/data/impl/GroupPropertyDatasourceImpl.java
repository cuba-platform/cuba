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
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

import java.util.Collection;
import java.util.List;

public class GroupPropertyDatasourceImpl<T extends Entity<K>, K>
        extends CollectionPropertyDatasourceImpl<T, K>
        implements GroupDatasource<T, K> {

    protected GroupDelegate<T, K> groupDelegate = new GroupDelegate<T, K>(this) {
        @Override
        protected void doSort(SortInfo<MetaPropertyPath>[] sortInfo) {
            GroupPropertyDatasourceImpl.super.doSort();
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
    public List<Entity> getOwnChildItems(GroupInfo groupId) {
        return groupDelegate.getOwnChildItems(groupId);
    }

    @Override
    public List<Entity> getChildItems(GroupInfo groupId) {
        return groupDelegate.getChildItems(groupId);
    }

    @Override
    public GroupInfo getParentGroup(Entity entity) {
        return groupDelegate.getParentGroup(entity);
    }

    @Override
    public List<GroupInfo> getGroupPath(Entity entity) {
        return groupDelegate.getGroupPath(entity);
    }
}