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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.gui.components.data.TreeTableItems;
import com.haulmont.cuba.web.gui.components.table.TableDataContainer;
import com.haulmont.cuba.web.gui.components.table.TableItemsEventsDelegate;
import com.haulmont.cuba.web.gui.components.table.TreeTableDataContainer;
import com.haulmont.cuba.web.widgets.CubaTreeTable;
import com.haulmont.cuba.web.widgets.data.AggregationContainer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WebTreeTable<E extends Entity> extends WebAbstractTable<CubaTreeTable, E> implements TreeTable<E> {

    public WebTreeTable() {
        component = createComponent();
    }

    protected CubaTreeTable createComponent() {
        return new CubaTreeTable();
    }

    @Override
    public void setRowHeaderMode(RowHeaderMode rowHeaderMode) {
        // Row Header mode for TreeTable ignored
    }

    @Override
    public void setItems(TableItems<E> tableItems) {
        if (tableItems != null &&
                !(tableItems instanceof TreeTableItems)) {
            throw new IllegalArgumentException("TreeTable supports only TreeTableItems data binding");
        }

        super.setItems(tableItems);
    }

    @Override
    protected TableDataContainer<E> createTableDataContainer(TableItems<E> tableItems) {
        return new AggregatableTreeTableDataContainer<>((TreeTableItems<E>) tableItems, this);
    }

    protected class AggregatableTreeTableDataContainer<I> extends TreeTableDataContainer<I>
            implements AggregationContainer {

        protected List<Object> aggregationProperties = null;

        public AggregatableTreeTableDataContainer(TreeTableItems<I> tableDataSource,
                                                  TableItemsEventsDelegate<I> dataEventsDelegate) {
            super(tableDataSource, dataEventsDelegate);
        }

        @Override
        public Collection getAggregationPropertyIds() {
            if (aggregationProperties != null) {
                return Collections.unmodifiableList(aggregationProperties);
            }
            return Collections.emptyList();
        }

        @Override
        public void addContainerPropertyAggregation(Object propertyId, Type type) {
            if (aggregationProperties == null) {
                aggregationProperties = new ArrayList<>();
            } else if (aggregationProperties.contains(propertyId)) {
                throw new IllegalStateException(String.format("Aggregation property %s already exists", propertyId));
            }
            aggregationProperties.add(propertyId);
        }

        @Override
        public void removeContainerPropertyAggregation(Object propertyId) {
            if (aggregationProperties != null) {
                aggregationProperties.remove(propertyId);
                if (aggregationProperties.isEmpty()) {
                    aggregationProperties = null;
                }
            }
        }

        @Override
        public Map<Object, Object> aggregate(Context context) {
            return __aggregate(this, context);
        }
    }

    protected TreeTableItems<E> getTreeTableSource() {
        return ((TreeTableItems) getItems());
    }

    @Override
    public void setIconProvider(Function<? super E, String> iconProvider) {
        this.iconProvider = iconProvider;
        // do not change row header mode
        component.refreshRowCache();
    }

    @Override
    public void expandAll() {
        TreeTableItems<E> treeTableSource = getTreeTableSource();
        if (treeTableSource != null) {
            Object nullParentItemId = new Object();

            Map<Object, Object> parentsMapping = getParentsMapping(treeTableSource, nullParentItemId);

            Tree<Object> itemIdsTree = toItemIdsTree(parentsMapping, nullParentItemId);

            List<Object> preOrder = toContainerPreOrder(itemIdsTree);
            List<Object> openItems = getItemIdsWithChildren(parentsMapping, nullParentItemId);
            List<Object> collapsedItemIds = getCollapsedItemIds();

            component.expandAllHierarchical(collapsedItemIds, preOrder, openItems);
        }
    }

    protected Map<Object, Object> getParentsMapping(TreeTableItems<E> tableSource, Object nullParentItemId) {
        Map<Object, Object> parentsMapping = new LinkedHashMap<>();

        Collection<?> itemIds = tableSource.getItemIds();

        for (Object itemId : itemIds) {
            Object parentId = tableSource.getParent(itemId);

            if (itemIds.contains(parentId)) {
                parentsMapping.put(itemId, parentId);
            } else {
                parentsMapping.put(itemId, nullParentItemId);
            }
        }

        return parentsMapping;
    }

    protected List<Object> getItemIdsWithChildren(Map<Object, Object> parentsMapping, Object nullParentItemId) {
        Set<Object> parents = new LinkedHashSet<>(parentsMapping.values());
        parents.remove(nullParentItemId);
        return new ArrayList<>(parents);
    }

    protected Tree<Object> toItemIdsTree(Map<Object, Object> parentsMapping, Object nullParentItemId) {
        Map<Object, Node<Object>> nodeMapping = new LinkedHashMap<>();

        for (Object itemId : parentsMapping.keySet()) {
            Node<Object> node = new Node<>(itemId);
            nodeMapping.put(itemId, node);
        }

        List<Node<Object>> roots = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : parentsMapping.entrySet()) {
            Object itemId = entry.getKey();
            Object parentId = entry.getValue();

            Node<Object> itemNode = nodeMapping.get(itemId);

            if (parentId == nullParentItemId) {
                roots.add(itemNode);
            } else {
                Node<Object> parentNode = nodeMapping.get(parentId);
                parentNode.addChild(itemNode);
            }
        }

        return new Tree<>(roots);
    }

    protected List<Object> toContainerPreOrder(Tree<Object> itemIdsTree) {
        List<Node<Object>> nodes = itemIdsTree.toList();
        if (nodes.isEmpty()) {
            return Collections.emptyList();
        }

        return nodes.stream()
                .map(objectNode -> objectNode.data)
                .collect(Collectors.toList());
    }

    protected List<Object> getCollapsedItemIds() {
        TreeTableItems<E> treeTableSource = getTreeTableSource();
        if (treeTableSource == null) {
            return Collections.emptyList();
        }

        Collection<?> itemIds = treeTableSource.getItemIds();
        return itemIds.stream()
                .filter(itemId -> component.isCollapsed(itemId))
                .collect(Collectors.toList());
    }

    @Override
    public void expand(Object itemId) {
        if (component.containsId(itemId)) {
            component.expandItemWithParents(itemId);
        }
    }

    @Override
    public void collapseAll() {
        component.collapseAllHierarchical();
    }

    @Override
    public void collapse(Object itemId) {
        if (component.containsId(itemId)) {
            component.collapseItemRecursively(itemId);
        }
    }

    @Override
    public void expandUpTo(int level) {
        component.expandUpTo(level);
    }

    @Override
    public int getLevel(Object itemId) {
        return component.getLevel(itemId);
    }

    @Override
    public boolean isExpanded(Object itemId) {
        if (component.containsId(itemId)) {
            return !component.isCollapsed(itemId);
        }
        return false;
    }
}