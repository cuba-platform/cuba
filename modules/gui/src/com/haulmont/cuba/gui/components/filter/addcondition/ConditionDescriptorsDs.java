/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.google.common.base.Strings;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;

import java.util.*;

/**
 * Datasource that allows to filter its content based on a string. Filtering is performed by
 * checking that {@code treeCaption} property contains the string
 *
 * @author gorbunkov
 * @version $Id$
 */
public class ConditionDescriptorsDs extends AbstractTreeDatasource<AbstractConditionDescriptor, UUID> {

    protected String conditionsFilter;

    protected boolean filterApplied = false;

    protected List<UUID> filteredItemsIds = new ArrayList<>();

    protected Tree descriptorsTree;

    @Override
    protected Tree loadTree(Map params) {
        descriptorsTree = (Tree) params.get("descriptorsTree");
        return descriptorsTree;
    }

    /**
     * Sets a filter string, fills internal collection with filtered items and refreshes the datasource
     * @param filter filter string
     */
    public void setFilter(String filter) {
        this.conditionsFilter = filter;
        filterApplied = false;
        filteredItemsIds = new ArrayList<>();
        for (UUID rootId : getRootItemIds()) {
            recursivelyFindSelectedItems(rootId);
        }
        filterApplied = true;
        refresh(Collections.<String, Object>singletonMap("descriptorsTree", descriptorsTree));
    }

    /**
     * Checks whether an item or any of its child passes filter and fills internal collection of filtered items
     *
     * @return true if item or any of its child passes filter
     */
    protected boolean recursivelyFindSelectedItems(UUID itemId) {
        boolean passesFilter = passesFilter(itemId);
        for (UUID childId : getChildren(itemId)) {
            passesFilter |= recursivelyFindSelectedItems(childId);
        }
        if (passesFilter) {
            filteredItemsIds.add(itemId);
        }
        return passesFilter;
    }

    protected boolean passesFilter(UUID itemId) {
        if (Strings.isNullOrEmpty(conditionsFilter)) {
            return true;
        }
        AbstractConditionDescriptor item = getItem(itemId);
        return item != null && item.getTreeCaption().toLowerCase().contains(conditionsFilter.toLowerCase());
    }

    @Override
    public Collection<UUID> getRootItemIds() {
        Collection<UUID> rootItemIds = new ArrayList<>(super.getRootItemIds());
        if (filterApplied)
            rootItemIds.retainAll(filteredItemsIds);
        return rootItemIds;
    }

    @Override
    public Collection<UUID> getChildren(UUID itemId) {
        Collection<UUID> children = new ArrayList<>(super.getChildren(itemId));
        if (filterApplied)
            children.retainAll(filteredItemsIds);
        return children;
    }
}
