/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 10.01.2010 20:50:40
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

public class GroupDatasourceImpl<T extends Entity<K>, K>
        extends CollectionDatasourceImpl<T, K>
        implements GroupDatasource<T, K>
{

    protected Object[] groupProperties = null;

    protected Map<GroupInfo, GroupInfo> parent;

    protected Map<GroupInfo, List<GroupInfo>> children;

    protected List<GroupInfo> roots;

    protected Map<GroupInfo, List<K>> groupItems;

    private boolean inGrouping;

    public GroupDatasourceImpl(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, String viewName
    ) {
        super(context, dataservice, id, metaClass, viewName);
    }

    public GroupDatasourceImpl(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, String viewName,
            boolean softDeletion
    ) {
        super(context, dataservice, id, metaClass, viewName, softDeletion);
    }

    public void groupBy(Object[] properties) {
        if (inGrouping) {
            return;
        }

        inGrouping = true;

        try {
            if (properties == null) {
                throw new NullPointerException("Group properties cannot be NULL");
            }

            //check datasource state
            if (!State.VALID.equals(state)) {
                refresh();
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
        }
    }

    protected void doGroup() {
        final Collection<K> itemIds = getItemIds();

        roots = new LinkedList<GroupInfo>();
        parent = new LinkedHashMap<GroupInfo, GroupInfo>();
        children = new HashMap<GroupInfo, List<GroupInfo>>();
        groupItems = new HashMap<GroupInfo, List<K>>();

        for (final K id : itemIds) {
            final T item = getItem(id);
            if (item != null)
            {
                GroupInfo<MetaPropertyPath> itemGroup;

                final LinkedMap itemValues = new LinkedMap();

                itemGroup = processItemGroupping(0, null, roots, item, itemValues);

                if (itemGroup == null) {
                    throw new IllegalStateException("Item group cannot be NULL");
                }

                List<K> groupItemIds = groupItems.get(itemGroup);
                if (groupItemIds == null) {
                    groupItemIds = new LinkedList<K>();
                    groupItems.put(itemGroup, groupItemIds);
                }
                groupItemIds.add(id);
            }
        }
    }

    private GroupInfo<MetaPropertyPath> processItemGroupping(
            int propertyIndex,
            GroupInfo parent,
            List<GroupInfo> children,
            T item,
            final LinkedMap itemValues
    ) {
        final Object property = groupProperties[propertyIndex++];

        itemValues.put(property, getItemValue((MetaPropertyPath) property, item.getId()));

        GroupInfo<MetaPropertyPath> itemGroup = new GroupInfo<MetaPropertyPath>(itemValues);

        if (!this.parent.containsKey(itemGroup)) {
            this.parent.put(itemGroup, parent);
        }

        if (!children.contains(itemGroup)) {
            children.add(itemGroup);
        }

        List<GroupInfo> groupChildren = this.children.get(itemGroup);
        if (groupChildren == null) {
            groupChildren = new LinkedList<GroupInfo>();
            this.children.put(itemGroup, groupChildren);
        }

        if (propertyIndex < groupProperties.length) {
            itemGroup = processItemGroupping(propertyIndex, itemGroup, groupChildren, item, itemValues);
        }

        return itemGroup;
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

    public Collection<K> getGroupItemIds(GroupInfo groupId) {
        if (containsGroup(groupId)) {
            List<K> itemIds;
            if ((itemIds = groupItems.get(groupId)) == null) {
                itemIds = new LinkedList<K>();
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
}
