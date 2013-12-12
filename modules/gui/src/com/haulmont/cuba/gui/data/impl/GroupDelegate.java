/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Instance;

import java.util.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.collections.map.LinkedMap;

/**
 * @author gorodnov
 * @version $Id$
 */
public abstract class GroupDelegate<T extends Entity<K>, K> {

    protected Object[] groupProperties = null;

    protected Map<GroupInfo, GroupInfo> parent;

    protected Map<GroupInfo, List<GroupInfo>> children;

    protected List<GroupInfo> roots;

    protected Map<GroupInfo, List<K>> groupItems;

    protected boolean inGrouping;

    protected CollectionDatasource<T, K> datasource;

    protected GroupDelegate(CollectionDatasource<T, K> datasource) {
        this.datasource = datasource;
    }

    public void groupBy(Object[] properties, CollectionDatasource.Sortable.SortInfo<MetaPropertyPath>[] sortInfos) {
        if (inGrouping) {
            return;
        }

        inGrouping = true;

        try {
            if (properties == null) {
                throw new NullPointerException("Group properties cannot be NULL");
            }

            //check a datasource state and refresh a datasource if it needed
            if (!Datasource.State.VALID.equals(datasource.getState())) {
                datasource.refresh();
            }

            groupProperties = properties;

            if (!ArrayUtils.isEmpty(groupProperties)) {
                doGroup();
            } else {
                roots = null;
                parent = null;
                children = null;
                groupItems = null;
            }
        } finally {
            inGrouping = false;

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
        final Collection<K> itemIds = datasource.getItemIds();

        roots = new LinkedList<>();
        parent = new LinkedHashMap<>();
        children = new HashMap<>();
        groupItems = new HashMap<>();

        for (final K id : itemIds) {
            final T item = datasource.getItem(id);
            if (item != null) {
                GroupInfo<MetaPropertyPath> itemGroup;

                final LinkedMap itemValues = new LinkedMap();

                itemGroup = processItemGroupping(0, null, roots, item, itemValues);

                if (itemGroup == null) {
                    throw new IllegalStateException("Item group cannot be NULL");
                }

                List<K> groupItemIds = groupItems.get(itemGroup);
                if (groupItemIds == null) {
                    groupItemIds = new LinkedList<>();
                    groupItems.put(itemGroup, groupItemIds);
                }
                groupItemIds.add(id);
            }
        }
    }

    protected GroupInfo<MetaPropertyPath> processItemGroupping(
            int propertyIndex,
            GroupInfo parent,
            List<GroupInfo> children,
            T item,
            final LinkedMap itemValues
    ) {
        final Object property = groupProperties[propertyIndex++];

        itemValues.put(property, getItemValue((MetaPropertyPath) property, item.getId()));

        GroupInfo<MetaPropertyPath> itemGroup = new GroupInfo<>(itemValues);

        if (!this.parent.containsKey(itemGroup)) {
            this.parent.put(itemGroup, parent);
        }

        if (!children.contains(itemGroup)) {
            children.add(itemGroup);
        }

        List<GroupInfo> groupChildren = this.children.get(itemGroup);
        if (groupChildren == null) {
            groupChildren = new LinkedList<>();
            this.children.put(itemGroup, groupChildren);
        }

        if (propertyIndex < groupProperties.length) {
            itemGroup = processItemGroupping(propertyIndex, itemGroup, groupChildren, item, itemValues);
        }

        return itemGroup;
    }

    protected abstract void doSort(CollectionDatasource.Sortable.SortInfo<MetaPropertyPath>[] sortInfo);

    protected void doGroupSort(CollectionDatasource.Sortable.SortInfo<MetaPropertyPath>[] sortInfo) {
        if (hasGroups()) {
            final MetaPropertyPath propertyPath = sortInfo[0].getPropertyPath();
            final boolean asc = CollectionDatasource.Sortable.Order.ASC.equals(sortInfo[0].getOrder());

            final int index = Arrays.asList(groupProperties).indexOf(propertyPath);
            if (index > -1) {
                if (index == 0) { // Sort roots
                    Collections.sort(roots, new GroupInfoComparator(asc));
                } else {
                    final Object parentProperty = groupProperties[index - 1];
                    for (final Map.Entry<GroupInfo, List<GroupInfo>> entry : children.entrySet()) {
                        if (entry.getKey().getProperty().equals(parentProperty)) {
                            Collections.sort(entry.getValue(), new GroupInfoComparator(asc));
                        }
                    }
                }
            } else {
                final Set<GroupInfo> groups = parent.keySet();
                for (final GroupInfo groupInfo : groups) {
                    if (groupItems.get(groupInfo) != null) {
                        Collections.sort(groupItems.get(groupInfo),
                                new EntityByIdComparator<>(propertyPath, datasource, asc));
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

    public boolean hasChildren(GroupInfo groupId) {
        List<GroupInfo> children;
        return containsGroup(groupId) && (children = this.children.get(groupId)) != null
                && !children.isEmpty();
    }

    public List<GroupInfo> getChildren(GroupInfo groupId) {
        if (hasChildren(groupId)) {
            return Collections.unmodifiableList(children.get(groupId));
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

    public List<K> getGroupItemIds(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            List<K> itemIds;
            if ((itemIds = groupItems.get(groupId)) == null) {
                itemIds = new LinkedList<>();
                final List<GroupInfo> children = getChildren(groupId);
                for (final GroupInfo child : children) {
                    itemIds.addAll(getGroupItemIds(child));
                }
            }
            return Collections.unmodifiableList(itemIds);
        }
        return Collections.emptyList();
    }

    public int getGroupItemsCount(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            List<K> itemIds;
            if ((itemIds = groupItems.get(groupId)) == null) {
                int count = 0;
                final List<GroupInfo> children = getChildren(groupId);
                for (final GroupInfo child : children) {
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
        return Arrays.asList(groupProperties);
    }

    public boolean containsGroup(GroupInfo groupId) {
        return hasGroups() && parent.keySet().contains(groupId);
    }

    protected Object getItemValue(MetaPropertyPath property, K itemId) {
        Instance instance = datasource.getItem(itemId);
        if (instance == null) {
            throw new IllegalStateException("Unable to get instance for groouping");
        }

        if (property.getMetaProperties().length == 1) {
            return instance.getValue(property.getMetaProperty().getName());
        } else {
            return instance.getValueEx(property.toString());
        }
    }
}