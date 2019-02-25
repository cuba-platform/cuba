/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.components.data.table;

import com.google.common.collect.ImmutableList;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.components.data.GroupTableItems;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.data.impl.AbstractComparator;
import com.haulmont.cuba.gui.data.impl.GroupInfoComparator;
import com.haulmont.cuba.gui.model.CollectionContainer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ContainerGroupTableItems<E extends Entity<K>, K>
        extends ContainerTableItems<E>
        implements GroupTableItems<E> {

    protected Object[] groupProperties = null;

    protected Map<GroupInfo, GroupInfo> parents;

    protected Map<GroupInfo, List<GroupInfo>> children;

    protected List<GroupInfo> roots;

    protected Map<GroupInfo, List<K>> groupItems;
    // reversed relations from groupItems
    protected Map<K, GroupInfo> itemGroups;

    protected boolean isGrouping;

    protected Object[] sortProperties;
    protected boolean[] sortAscending;

    public ContainerGroupTableItems(CollectionContainer<E> container) {
        super(container);
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        sortProperties = propertyId;
        sortAscending = ascending;
        if (hasGroups()) {
            Sort sort = createSort(propertyId, ascending);
            doGroupSort(sort);
        } else {
            super.sort(propertyId, ascending);
        }
    }

    protected void doGroupSort(Sort sort) {
        if (hasGroups()) {
            Sort.Order order = sort.getOrders().get(0);
            MetaPropertyPath propertyPath = container.getEntityMetaClass().getPropertyPath(order.getProperty());
            if (propertyPath == null)
                throw new IllegalStateException("Cannot create property path from " + order.getProperty());
            boolean asc = order.getDirection().equals(Sort.Direction.ASC);

            int index = ArrayUtils.indexOf(groupProperties, propertyPath);
            if (index > -1) {
                if (index == 0) { // Sort roots
                    roots.sort(new GroupInfoComparator(asc));
                } else {
                    Object parentProperty = groupProperties[index - 1];
                    for (Map.Entry<GroupInfo, List<GroupInfo>> entry : children.entrySet()) {
                        Object property = entry.getKey().getProperty();
                        if (property.equals(parentProperty)) {
                            entry.getValue().sort(new GroupInfoComparator(asc));
                        }
                    }
                }
            } else {
                Set<GroupInfo> groups = parents.keySet();
                for (GroupInfo groupInfo : groups) {
                    List<K> items = groupItems.get(groupInfo);
                    if (items != null) {
                        items.sort(new EntityByIdComparator<>(propertyPath, container, asc));
                    }
                }
            }
        }
    }

    @Override
    public void groupBy(Object[] properties) {
        if (isGrouping) {
            return;
        }
        isGrouping = true;
        try {
            if (properties != null) {
                groupProperties = properties;

                if (!ArrayUtils.isEmpty(groupProperties)) {
                    doGroup();
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

            if (sortProperties != null && sortProperties.length > 0) {
                if (hasGroups()) {
                    doGroupSort(createSort(sortProperties, sortAscending));
                } else {
                    super.sort(sortProperties, sortAscending);
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

        for (E item : container.getItems()) {
            GroupInfo<MetaPropertyPath> groupInfo = groupItems(0, null, roots, item, new LinkedMap<>());

            if (groupInfo == null) {
                throw new IllegalStateException("Item group cannot be NULL");
            }

            List<K> itemsIds = groupItems.computeIfAbsent(groupInfo, k -> new ArrayList<>());
            itemsIds.add(item.getId());
        }
    }

    protected GroupInfo<MetaPropertyPath> groupItems(int propertyIndex, GroupInfo parent, List<GroupInfo> children,
                                                     E item, LinkedMap<MetaPropertyPath, Object> groupValues) {
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

    protected Object getValueByProperty(E item, MetaPropertyPath property) {
        Preconditions.checkNotNullArgument(item);

        return item.getValueEx(property.toString());
    }

    @Override
    public List<GroupInfo> rootGroups() {
        if (hasGroups()) {
            return Collections.unmodifiableList(roots);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean hasChildren(GroupInfo groupId) {
        boolean groupExists = containsGroup(groupId);
        List<GroupInfo> groupChildren = this.children.get(groupId);
        return groupExists && CollectionUtils.isNotEmpty(groupChildren);
    }

    @Override
    public List<GroupInfo> getChildren(GroupInfo groupId) {
        if (hasChildren(groupId)) {
            return Collections.unmodifiableList(children.get(groupId));
        }
        return Collections.emptyList();
    }

    @Override
    public List<E> getOwnChildItems(GroupInfo groupId) {
        if (groupItems == null) {
            return Collections.emptyList();
        }

        List<K> idsList = groupItems.get(groupId);
        if (containsGroup(groupId) && CollectionUtils.isNotEmpty(idsList)) {
            return idsList.stream()
                    .map(id -> container.getItem(id))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<E> getChildItems(GroupInfo groupId) {
        if (groupItems == null) {
            return Collections.emptyList();
        }

        if (containsGroup(groupId)) {
            List<E> entities = new ArrayList<>();

            // if current group contains other groups
            if (hasChildren(groupId)) {
                List<GroupInfo> children = getChildrenInternal(groupId);
                for (GroupInfo childGroup : children) {
                    entities.addAll(getChildItems(childGroup));
                }
            }

            for (K id : groupItems.getOrDefault(groupId, Collections.emptyList())) {
                E item = container.getItem(id);
                entities.add(item);
            }

            return entities;
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

    @Nullable
    @Override
    public GroupInfo getParentGroup(E item) {
        K id = item.getId();
        if (container.getItemOrNull(id) == null) {
            throw new IllegalArgumentException("Datasource doesn't contain passed entity");
        }

        if (itemGroups == null) {
            return null;
        }
        return itemGroups.get(item.getId());
    }

    @Override
    public List<GroupInfo> getGroupPath(E item) {
        K id = item.getId();
        if (container.getItemOrNull(id) == null) {
            throw new IllegalArgumentException("Datasource doesn't contain passed entity");
        }

        if (itemGroups == null) {
            return Collections.emptyList();
        }

        GroupInfo groupInfo = itemGroups.get(item.getId());
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

    @Override
    public Object getGroupProperty(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            return groupId.getProperty();
        }
        return null;
    }

    @Override
    public Object getGroupPropertyValue(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            return groupId.getValue();
        }
        return null;
    }

    @Override
    public Collection<K> getGroupItemIds(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            List<K> itemIds;
            if ((itemIds = groupItems.get(groupId)) == null) {
                itemIds = new ArrayList<>();
                List<GroupInfo> children = getChildrenInternal(groupId);
                for (GroupInfo child : children) {
                    itemIds.addAll(getGroupItemIds(child));
                }
            }
            return ImmutableList.copyOf(itemIds);
        }
        return Collections.emptyList();
    }

    @Override
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

    @Override
    public boolean hasGroups() {
        return roots != null;
    }

    @Override
    public Collection<?> getGroupProperties() {
        if (groupProperties == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(groupProperties);
    }

    @Override
    public boolean containsGroup(GroupInfo groupId) {
        return hasGroups() && parents.keySet().contains(groupId);
    }

    public static class EntityByIdComparator<T extends Entity<K>, K> extends AbstractComparator<K> {
        private MetaPropertyPath propertyPath;
        private MetaProperty property;
        private CollectionContainer<T> container;

        public EntityByIdComparator(MetaPropertyPath propertyPath, CollectionContainer<T> container, boolean asc) {
            super(asc);
            this.propertyPath = propertyPath;
            if (propertyPath.getMetaProperties().length == 1) {
                property = this.propertyPath.getMetaProperty();
            }
            this.container = container;
        }

        @Override
        public int compare(K key1, K key2) {
            Object o1 = getValue(container.getItem(key1));
            Object o2 = getValue(container.getItem(key2));

            return __compare(o1, o2);
        }

        private Object getValue(Instance instance) {
            Object value;
            if (property != null) {
                value = instance.getValue(property.getName());
            } else {
                value = instance.getValueEx(propertyPath);
            }

            if (!(value == null || value instanceof Comparable || value instanceof Instance)) {
                if (value instanceof IdProxy) {
                    value = ((IdProxy) value).get();
                } else {
                    value = value.toString();
                }
            }

            return value;
        }
    }
}
