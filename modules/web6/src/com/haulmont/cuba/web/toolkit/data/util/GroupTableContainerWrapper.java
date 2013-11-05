/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.data.util;

import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.data.GroupTableContainer;
import com.haulmont.cuba.web.toolkit.data.TableContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.ContainerOrderedWrapper;

import java.util.Collection;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public class GroupTableContainerWrapper extends ContainerOrderedWrapper
        implements GroupTableContainer, AggregationContainer {

    private boolean isGroupTableContainer;

    private GroupTableContainer groupTableContainer;
    private Container container;

    public GroupTableContainerWrapper(Container toBeWrapped) {
        super(toBeWrapped);

        isGroupTableContainer = toBeWrapped instanceof GroupTableContainer;
        if (isGroupTableContainer)
            groupTableContainer = (GroupTableContainer) toBeWrapped;
        container = toBeWrapped;
    }

    @Override
    public void groupBy(final Object[] properties) {
        if (isGroupTableContainer) {
            groupTableContainer.groupBy(properties);
        } else {
            throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                    + container.getClass());
        }
    }

    @Override
    public boolean isGroup(Object id) {
        return isGroupTableContainer && groupTableContainer.isGroup(id);
    }

    @Override
    public Collection<?> rootGroups() {
        if (isGroupTableContainer) {
            return groupTableContainer.rootGroups();
        } else {
            throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                    + container.getClass());
        }
    }

    @Override
    public boolean hasChildren(Object id) {
        return isGroupTableContainer && groupTableContainer.hasChildren(id);
    }

    @Override
    public Collection<?> getChildren(Object id) {
        if (isGroupTableContainer) {
            return groupTableContainer.getChildren(id);
        } else {
            throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                    + container.getClass());
        }
    }

    @Override
    public boolean hasGroups() {
        return isGroupTableContainer && groupTableContainer.hasGroups();
    }

    @Override
    public Object getGroupProperty(Object itemId) {
        if (isGroupTableContainer) {
            return groupTableContainer.getGroupProperty(itemId);
        }
        throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                + container.getClass());
    }

    @Override
    public Object getGroupPropertyValue(Object itemId) {
        if (isGroupTableContainer) {
            return groupTableContainer.getGroupPropertyValue(itemId);
        }
        throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                + container.getClass());
    }

    @Override
    public Collection<?> getGroupItemIds(Object itemId) {
        if (isGroupTableContainer) {
            return groupTableContainer.getGroupItemIds(itemId);
        }
        throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                + container.getClass());
    }

    @Override
    public int getGroupItemsCount(Object itemId) {
        if (isGroupTableContainer) {
            return groupTableContainer.getGroupItemsCount(itemId);
        }
        throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                + container.getClass());
    }

    @Override
    public Collection<?> getGroupProperties() {
        if (isGroupTableContainer) {
            return groupTableContainer.getGroupProperties();
        }
        throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                + container.getClass());
    }

    @Override
    public void expand(Object id) {
        if (isGroupTableContainer) {
            groupTableContainer.expand(id);
        } else {
            throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                    + container.getClass());
        }
    }

    @Override
    public boolean isExpanded(Object id) {
        return isGroupTableContainer && groupTableContainer.isExpanded(id);
    }

    @Override
    public void expandAll() {
        if (isGroupTableContainer) {
            groupTableContainer.expandAll();
        } else {
            throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                    + container.getClass());
        }
    }

    @Override
    public void collapseAll() {
        if (isGroupTableContainer) {
            groupTableContainer.collapseAll();
        } else {
            throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                    + container.getClass());
        }
    }

    @Override
    public void collapse(Object id) {
        if (isGroupTableContainer) {
            groupTableContainer.collapse(id);
        } else {
            throw new IllegalStateException("Wrapped container is not GroupTableContainer:"
                    + container.getClass());
        }
    }

    @Override
    public Collection getAggregationPropertyIds() {
        if (container instanceof AggregationContainer) {
            return ((AggregationContainer) container).getAggregationPropertyIds();
        }
        throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                + container.getClass());
    }

    @Override
    public Type getContainerPropertyAggregation(Object propertyId) {
        if (container instanceof AggregationContainer) {
            return ((AggregationContainer) container).getContainerPropertyAggregation(propertyId);
        }
        throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                + container.getClass());
    }

    @Override
    public void addContainerPropertyAggregation(Object propertyId, Type type) {
        if (container instanceof AggregationContainer) {
            ((AggregationContainer) container).addContainerPropertyAggregation(propertyId, type);
        } else {
            throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                    + container.getClass());
        }
    }

    @Override
    public void removeContainerPropertyAggregation(Object propertyId) {
        if (container instanceof AggregationContainer) {
            ((AggregationContainer) container).removeContainerPropertyAggregation(propertyId);
        } else {
            throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                    + container.getClass());
        }
    }

    @Override
    public Map<Object, Object> aggregate(Context context) {
        if (container instanceof AggregationContainer) {
            return ((AggregationContainer) container).aggregate(context);
        }
        throw new IllegalStateException("Wrapped container is not AggregationContainer: "
                + container.getClass());
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        if (container instanceof Sortable) {
            ((Sortable) container).sort(propertyId, ascending);
        } else {
            throw new IllegalStateException("Wrapped container is not Sortable: "
                    + container.getClass());
        }
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        if (container instanceof Sortable) {
            return ((Sortable) container).getSortableContainerPropertyIds();
        }
        throw new IllegalStateException("Wrapped container is not Sortable: "
                + container.getClass());
    }

    @Override
    public void resetSortOrder() {
        if (container instanceof TableContainer) {
            ((TableContainer) container).resetSortOrder();
        }
    }
}
