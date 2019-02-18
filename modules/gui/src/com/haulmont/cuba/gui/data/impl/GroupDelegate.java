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

import com.google.common.collect.ImmutableList;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class GroupDelegate<T extends Entity<K>, K> {

    protected Object[] groupProperties = null;

    protected Map<GroupInfo, GroupInfo> parents;

    protected Map<GroupInfo, List<GroupInfo>> children;

    protected List<GroupInfo> roots;

    protected Map<GroupInfo, List<K>> groupItems;
    // reversed relations from groupItems
    protected Map<K, GroupInfo> itemGroups;

    protected boolean isGrouping;

    protected CollectionDatasource<T, K> datasource;
    protected CollectionDatasource.SortDelegate<T, K> sortDelegate;
    protected GroupDatasource.GroupSortDelegate groupSortDelegate;

    protected GroupDelegate(CollectionDatasource<T, K> datasource,
                            CollectionDatasource.SortDelegate<T, K> sortDelegate,
                            GroupDatasource.GroupSortDelegate groupSortDelegate) {
        this.datasource = datasource;
        this.sortDelegate = sortDelegate;
        this.groupSortDelegate = groupSortDelegate;
    }

    public void groupBy(Object[] properties, CollectionDatasource.Sortable.SortInfo<MetaPropertyPath>[] sortInfos) {
        if (isGrouping) {
            return;
        }
        isGrouping = true;
        try {
            if (properties != null) {
                CollectionDsHelper.autoRefreshInvalid(datasource, true);

                groupProperties = properties;

                if (!ArrayUtils.isEmpty(groupProperties)) {
                    if (datasource.getState() == Datasource.State.VALID) {
                        doGroup();
                    } else if (datasource instanceof PropertyDatasource) {
                        datasource.addStateChangeListener(new Datasource.StateChangeListener<T>() {
                            @Override
                            public void stateChanged(Datasource.StateChangeEvent<T> e) {
                                if (e.getState() == Datasource.State.VALID) {
                                    doGroup();
                                    datasource.removeStateChangeListener(this);
                                }
                            }
                        });
                    }
                } else {
                    roots = null;
                    parents = null;
                    children = null;
                    groupItems = null;
                    itemGroups = null;
                }
            }
        } finally {
            isGrouping = false;

            if (!ArrayUtils.isEmpty(sortInfos)) {
                if (hasGroups()) {
                    doGroupSort(sortInfos);
                } else {
                    doSort(sortInfos);
                }
            }
        }
    }

    protected void doGroup() {
        roots = new LinkedList<>();
        parents = new LinkedHashMap<>();
        children = new HashMap<>();
        groupItems = new HashMap<>();
        itemGroups = new HashMap<>();

        Collection<K> itemIds = datasource.getItemIds();
        for (K id : itemIds) {
            T item = datasource.getItem(id);
            GroupInfo<MetaPropertyPath> groupInfo = groupItems(0, null, roots, item, new LinkedMap<>());

            if (groupInfo == null) {
                throw new IllegalStateException("Item group cannot be NULL");
            }

            List<K> itemsIds = groupItems.computeIfAbsent(groupInfo, k -> new ArrayList<>());
            itemsIds.add(id);
        }
    }

    protected GroupInfo<MetaPropertyPath> groupItems(int propertyIndex, GroupInfo parent, List<GroupInfo> children,
                                                     T item, LinkedMap<MetaPropertyPath, Object> groupValues) {
        MetaPropertyPath property = (MetaPropertyPath) groupProperties[propertyIndex++];
        Object itemValue = getValueByProperty(item, property);
        groupValues.put(property, itemValue);

        GroupInfo<MetaPropertyPath> groupInfo = new GroupInfo<>(groupValues);
        itemGroups.put(item.getId(), groupInfo);

        if (!parents.containsKey(groupInfo)) {
            parents.put(groupInfo, parent);
        }

        if (!children.contains(groupInfo)) {
            children.add(groupInfo);
        }

        List<GroupInfo> groupChildren =
                this.children.computeIfAbsent(groupInfo, k -> new ArrayList<>());

        if (propertyIndex < groupProperties.length) {
            groupInfo = groupItems(propertyIndex, groupInfo, groupChildren, item, groupValues);
        }

        return groupInfo;
    }

    protected abstract void doSort(CollectionDatasource.Sortable.SortInfo<MetaPropertyPath>[] sortInfo);

    protected void doGroupSort(CollectionDatasource.Sortable.SortInfo<MetaPropertyPath>[] sortInfo) {
        if (hasGroups()) {
            MetaPropertyPath propertyPath = sortInfo[0].getPropertyPath();

            int index = ArrayUtils.indexOf(groupProperties, propertyPath);
            if (index > -1) {
                if (index == 0) { // Sort roots
                    groupSortDelegate.sortGroups(roots, sortInfo);
                } else {
                    Object parentProperty = groupProperties[index - 1];
                    for (Map.Entry<GroupInfo, List<GroupInfo>> entry : children.entrySet()) {
                        Object property = entry.getKey().getProperty();
                        if (property.equals(parentProperty)) {
                            groupSortDelegate.sortGroups(entry.getValue(), sortInfo);
                        }
                    }
                }
            } else {
                Set<GroupInfo> groups = parents.keySet();
                for (GroupInfo groupInfo : groups) {
                    List<K> items = groupItems.get(groupInfo);
                    if (items != null) {
                        List<T> entities = items.stream()
                                .map(item -> datasource.getItem(item))
                                .collect(Collectors.toList());

                        sortDelegate.sort(entities, sortInfo);

                        items.clear();
                        for (T entity : entities) {
                            items.add(entity.getId());
                        }
                    }
                }
            }
        }
    }

    public List<GroupInfo> rootGroups() {
        if (hasGroups()) {
            return Collections.unmodifiableList(roots);
        }
        return Collections.emptyList();
    }

    public boolean hasChildren(GroupInfo group) {
        boolean groupExists = containsGroup(group);
        List<GroupInfo> groupChildren = this.children.get(group);
        return groupExists && CollectionUtils.isNotEmpty(groupChildren);
    }

    public List<GroupInfo> getChildren(GroupInfo groupId) {
        if (hasChildren(groupId)) {
            return Collections.unmodifiableList(children.get(groupId));
        }
        return Collections.emptyList();
    }

    // return collection as is
    public List<GroupInfo> getChildrenInternal(GroupInfo groupId) {
        if (hasChildren(groupId)) {
            return children.get(groupId);
        }
        return Collections.emptyList();
    }

    public Object getGroupProperty(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            return groupId.getProperty();
        }
        return null;
    }

    public Object getGroupPropertyValue(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            return groupId.getValue();
        }
        return null;
    }

    public List<K> getGroupItemIds(GroupInfo group) {
        if (containsGroup(group)) {
            List<K> itemIds;
            if ((itemIds = groupItems.get(group)) == null) {
                itemIds = new ArrayList<>();
                List<GroupInfo> children = getChildrenInternal(group);
                for (GroupInfo child : children) {
                    itemIds.addAll(getGroupItemIds(child));
                }
            }
            return ImmutableList.copyOf(itemIds);
        }
        return Collections.emptyList();
    }

    public int getGroupItemsCount(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            List<K> itemIds;
            if ((itemIds = groupItems.get(groupId)) == null) {
                int count = 0;
                List<GroupInfo> children = getChildrenInternal(groupId);
                for (GroupInfo child : children) {
                    count += getGroupItemsCount(child);
                }
                return count;
            } else {
                return itemIds.size();
            }
        }
        return 0;
    }

    public boolean hasGroups() {
        return roots != null;
    }

    public Collection<?> getGroupProperties() {
        if (groupProperties == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(groupProperties);
    }

    public boolean containsGroup(GroupInfo groupInfo) {
        return hasGroups() && parents.keySet().contains(groupInfo);
    }

    protected Object getValueByProperty(T item, MetaPropertyPath property) {
        Preconditions.checkNotNullArgument(item);

        if (property.getMetaProperties().length == 1) {
            return item.getValue(property.getMetaProperty().getName());
        } else {
            return item.getValueEx(property);
        }
    }

    public List<T> getOwnChildItems(GroupInfo groupId) {
        if (groupItems == null) {
            return Collections.emptyList();
        }

        List<K> idsList = groupItems.get(groupId);
        if (containsGroup(groupId) && CollectionUtils.isNotEmpty(idsList)) {
            return idsList.stream()
                    .map(id -> datasource.getItem(id))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<T> getChildItems(GroupInfo groupId) {
        if (groupItems == null) {
            return Collections.emptyList();
        }

        if (containsGroup(groupId)) {
            List<T> entities = new ArrayList<>();

            // if current group contains other groups
            if (hasChildren(groupId)) {
                List<GroupInfo> children = getChildrenInternal(groupId);
                for (GroupInfo childGroup : children) {
                    entities.addAll(getChildItems(childGroup));
                }
            }

            for (K id : groupItems.getOrDefault(groupId, Collections.emptyList())) {
                T item = datasource.getItem(id);
                entities.add(item);
            }

            return entities;
        }
        return Collections.emptyList();
    }

    public GroupInfo getParentGroup(T entity) {
        K id = entity.getId();
        if (!datasource.containsItem(id)) {
            throw new IllegalArgumentException("Datasource doesn't contain passed entity");
        }

        if (itemGroups == null) {
            return null;
        }
        return itemGroups.get(entity.getId());
    }

    public List<GroupInfo> getGroupPath(T entity) {
        K id = entity.getId();
        if (!datasource.containsItem(id)) {
            throw new IllegalArgumentException("Datasource doesn't contain passed entity");
        }

        if (itemGroups == null) {
            return Collections.emptyList();
        }

        GroupInfo groupInfo = itemGroups.get(entity.getId());
        if (groupInfo == null) {
            return Collections.emptyList();
        }
        LinkedList<GroupInfo> parentGroups = new LinkedList<>();
        parentGroups.add(groupInfo);

        GroupInfo parent = parents.get(groupInfo);
        while (parent != null) {
            parentGroups.addFirst(parent);
            parent = parents.get(parent);
        }

        return parentGroups;
    }

    public void setSortDelegate(CollectionDatasource.SortDelegate<T, K> sortDelegate) {
        this.sortDelegate = sortDelegate;
    }

    public void setGroupSortDelegate(GroupDatasource.GroupSortDelegate groupSortDelegate) {
        this.groupSortDelegate = groupSortDelegate;
    }
}