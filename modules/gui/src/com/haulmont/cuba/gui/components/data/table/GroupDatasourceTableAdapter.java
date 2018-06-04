/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.components.data.table;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.GroupTableSource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class GroupDatasourceTableAdapter<E extends Entity<K>, K>
        extends SortableCollectionDatasourceTableAdapter<E, K>
        implements GroupTableSource<E> {

    @SuppressWarnings("unchecked")
    public GroupDatasourceTableAdapter(GroupDatasource<E, K> datasource) {
        super((CollectionDatasource.Sortable<E, K>) datasource);
    }

    @SuppressWarnings("unchecked")
    public GroupDatasource<E, K> getGroupDatasource() {
        return (GroupDatasource<E, K>) datasource;
    }

    @Override
    public void groupBy(Object[] properties) {
        getGroupDatasource().groupBy(properties);
    }

    @Override
    public List<GroupInfo> rootGroups() {
        return getGroupDatasource().rootGroups();
    }

    @Override
    public boolean hasChildren(GroupInfo groupId) {
        return getGroupDatasource().hasChildren(groupId);
    }

    @Override
    public List<GroupInfo> getChildren(GroupInfo groupId) {
        return getGroupDatasource().getChildren(groupId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getOwnChildItems(GroupInfo groupId) {
        return getGroupDatasource().getOwnChildItems(groupId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getChildItems(GroupInfo groupId) {
        return getGroupDatasource().getChildItems(groupId);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public GroupInfo getParentGroup(E entity) {
        return getGroupDatasource().getParentGroup(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GroupInfo> getGroupPath(E entity) {
        return getGroupDatasource().getGroupPath(entity);
    }

    @Override
    public Object getGroupProperty(GroupInfo groupId) {
        return getGroupDatasource().getGroupProperty(groupId);
    }

    @Override
    public Object getGroupPropertyValue(GroupInfo groupId) {
        return getGroupDatasource().getGroupPropertyValue(groupId);
    }

    @Override
    public Collection<?> getGroupItemIds(GroupInfo groupId) {
        return getGroupDatasource().getGroupItemIds(groupId);
    }

    @Override
    public int getGroupItemsCount(GroupInfo groupId) {
        return getGroupDatasource().getGroupItemsCount(groupId);
    }

    @Override
    public boolean hasGroups() {
        return getGroupDatasource().hasGroups();
    }

    @Override
    public Collection<?> getGroupProperties() {
        return getGroupDatasource().getGroupProperties();
    }

    @Override
    public boolean containsGroup(GroupInfo groupId) {
        return getGroupDatasource().containsGroup(groupId);
    }
}