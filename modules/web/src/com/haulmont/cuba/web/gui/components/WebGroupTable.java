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

import com.google.common.collect.Lists;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenersWrapper;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.gui.data.SortableCollectionDsWrapper;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.data.GroupTableContainer;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupTable;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupTable.GroupAggregationInputValueChangeContext;
import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebGroupTable<E extends Entity> extends WebAbstractTable<CubaGroupTable, E> implements GroupTable<E> {

    protected Map<Table.Column, GroupAggregationCells> groupAggregationCells = null;

    protected boolean rerender = true;
    protected boolean showItemsCountForGroup = true;

    protected GroupCellValueFormatter<E> groupCellValueFormatter;

    public WebGroupTable() {
        component = createGroupTableComponent();
        initComponent(component);

        component.setGroupPropertyValueFormatter(new AggregatableGroupPropertyValueFormatter());
    }

    protected CubaGroupTable createGroupTableComponent() {
        return new CubaGroupTable() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Resource getItemIcon(Object itemId) {
                return WebGroupTable.this.getItemIcon(itemId);
            }

            @Override
            protected boolean changeVariables(Map<String, Object> variables) {
                boolean b = super.changeVariables(variables);
                b = handleSpecificVariables(variables) || b;
                return b;
            }

            @Override
            public void groupBy(Object[] properties) {
                groupBy(properties, rerender);
            }
        };
    }

    @Override
    public GroupDatasource getDatasource() {
        return (GroupDatasource) super.getDatasource();
    }

    @Override
    protected StyleGeneratorAdapter createStyleGenerator() {
        return new StyleGeneratorAdapter(){
            @Override
            public String getStyle(com.vaadin.ui.Table source, Object itemId, Object propertyId) {
                if (!component.getGroupProperties().contains(propertyId)) {
                    return super.getStyle(source, itemId, propertyId);
                }

                if (styleProviders != null) {
                    return getGeneratedCellStyle(itemId, propertyId);
                }
                return null;
            }
        };
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled()) {
            return false;
        }

        super.saveSettings(element);

        Element groupPropertiesElement = element.element("groupProperties");
        if (groupPropertiesElement != null) {
            element.remove(groupPropertiesElement);
        }

        groupPropertiesElement = element.addElement("groupProperties");

        final Collection<?> groupProperties = component.getGroupProperties();
        for (Object groupProperty : groupProperties) {
            if (getNotCollapsedColumns().contains(getColumn(groupProperty.toString()))) {
                final Element groupPropertyElement = groupPropertiesElement.addElement("property");
                groupPropertyElement.addAttribute("id", groupProperty.toString());
            }
        }

        return true;
    }

    @Override
    public void applyColumnSettings(Element element) {
        super.applyColumnSettings(element);

        final Element groupPropertiesElement = element.element("groupProperties");
        if (groupPropertiesElement != null) {
            final List elements = groupPropertiesElement.elements("property");
            final List<MetaPropertyPath> properties = new ArrayList<>(elements.size());
            for (final Object o : elements) {
                String id = ((Element) o).attributeValue("id");
                final MetaPropertyPath property = DynamicAttributesUtils.isDynamicAttribute(id)
                        ? DynamicAttributesUtils.getMetaPropertyPath(datasource.getMetaClass(), id)
                        : datasource.getMetaClass().getPropertyPath(id);

                if (property != null) {
                    properties.add(property);
                } else {
                    LoggerFactory.getLogger(WebGroupTable.class)
                            .warn("Ignored group property '{}'", id);
                }
            }
            groupBy(properties.toArray());
        }
    }

    @Override
    protected CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource,
                                                            Collection<MetaPropertyPath> columns,
                                                            CollectionDsListenersWrapper collectionDsListenersWrapper) {
        return new GroupTableDsWrapper(datasource, columns, collectionDsListenersWrapper);
    }

    @Override
    protected Map<Object, Object> __handleAggregationResults(AggregationContainer.Context context, Map<Object, Object> results) {
        if (context instanceof CubaGroupTable.GroupAggregationContext) {
            CubaGroupTable.GroupAggregationContext groupContext = (CubaGroupTable.GroupAggregationContext) context;

            for (final Map.Entry<Object, Object> entry : results.entrySet()) {
                final Table.Column column = columns.get(entry.getKey());
                GroupAggregationCells cells;
                if ((cells = groupAggregationCells.get(column)) != null) {
                    String value = getFormattedValue(column, entry.getValue());
                    cells.cells.put(groupContext.getGroupId(), value);
                }
            }

            return results;
        } else {
            return super.__handleAggregationResults(context, results);
        }
    }

    protected Object[] getNewColumnOrder(Object[] newGroupProperties) {
        List<Object> allProps = Lists.newArrayList(component.getVisibleColumns()); // mutable list
        List<Object> newGroupProps = Arrays.asList(newGroupProperties);

        allProps.removeAll(newGroupProps);
        allProps.addAll(0, newGroupProps);

        return allProps.toArray();
    }

    protected List<Object> collectPropertiesByColumns(String... columnIds) {
        List<Object> properties = new ArrayList<>();

        for (String columnId : columnIds) {
            Column column = getColumn(columnId);

            if (column == null) {
                throw new IllegalArgumentException("There is no column with the given id: " + columnId);
            }

            properties.add(column.getId());
        }

        return properties;
    }

    protected void validateProperties(Object[] properties) {
        for (Object property : properties) {
            if (!(property instanceof MetaPropertyPath)) {
                throw new IllegalArgumentException("Only MetaPropertyPaths are supported by the \"groupBy\" method.");
            }
        }
    }

    @Override
    public void groupBy(Object[] properties) {
        Preconditions.checkNotNullArgument(properties);
        validateProperties(properties);

        if (uselessGrouping(properties)) {
            return;
        }

        component.groupBy(properties);
        component.setColumnOrder(getNewColumnOrder(properties));
    }

    @Override
    public void groupByColumns(String... columnIds) {
        Preconditions.checkNotNullArgument(columnIds);

        if (uselessGrouping(columnIds)) {
            return;
        }

        groupBy(collectPropertiesByColumns(columnIds).toArray());
    }

    @Override
    public void ungroupByColumns(String... columnIds) {
        Preconditions.checkNotNullArgument(columnIds);

        if (uselessGrouping(columnIds)) {
            return;
        }

        Object[] remainingGroups = CollectionUtils
                .removeAll(component.getGroupProperties(), collectPropertiesByColumns(columnIds))
                .toArray();

        groupBy(remainingGroups);
    }

    @Override
    public void ungroup() {
        groupBy(new Object[]{});
    }

    protected boolean uselessGrouping(Object[] newGroupProperties) {
        return (newGroupProperties == null || newGroupProperties.length == 0) &&
                component.getGroupProperties().isEmpty();
    }

    @Override
    public boolean getColumnGroupAllowed(String columnId) {
        Column column = getColumnNN(columnId);
        return getColumnGroupAllowed(column);
    }

    @Override
    public boolean getColumnGroupAllowed(Column column) {
        checkNotNullArgument(column, "column must be non null");

        return component.getColumnGroupAllowed(column.getId());
    }

    @Override
    public void setColumnGroupAllowed(String columnId, boolean allowed) {
        Column column = getColumnNN(columnId);
        setColumnGroupAllowed(column, allowed);
    }

    @Nonnull
    protected Column getColumnNN(String columnId) {
        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        return column;
    }

    @Override
    public void setColumnGroupAllowed(Column column, boolean allowed) {
        checkNotNullArgument(column, "column must be non null");

        if (column.isGroupAllowed() != allowed) {
            column.setGroupAllowed(allowed);
        }
        component.setColumnGroupAllowed(column.getId(), allowed);
    }

    @Override
    public GroupCellValueFormatter<E> getGroupCellValueFormatter() {
        return groupCellValueFormatter;
    }

    @Override
    public void setGroupCellValueFormatter(GroupCellValueFormatter<E> formatter) {
        this.groupCellValueFormatter = formatter;
    }

    @Override
    public void expandAll() {
        component.expandAll();
    }

    @Override
    public void expand(GroupInfo groupId) {
        component.expand(groupId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void expandPath(Entity item) {
        if (component.hasGroups()) {
            expandGroupsFor((Collection<GroupInfo>) component.rootGroups(), item.getId());
        }
    }

    @SuppressWarnings("unchecked")
    protected void expandGroupsFor(Collection<GroupInfo> groupSlice, Object itemId) {
        for (GroupInfo g: groupSlice) {
            if (component.getGroupItemIds(g).contains(itemId)) {
                component.expand(g);

                if (component.hasChildren(g)) {
                    expandGroupsFor((Collection<GroupInfo>) component.getChildren(g), itemId);
                }
                return;
            }
        }
    }

    @Override
    public void collapseAll() {
        component.collapseAll();
    }

    @Override
    public void collapse(GroupInfo groupId) {
        component.collapse(groupId);
    }

    @Override
    public boolean isExpanded(GroupInfo groupId) {
        return component.isExpanded(groupId);
    }

    @Override
    public boolean isFixedGrouping() {
        return component.isFixedGrouping();
    }

    @Override
    public void setFixedGrouping(boolean fixedGrouping) {
        component.setFixedGrouping(fixedGrouping);
    }

    @Override
    public boolean isShowItemsCountForGroup() {
        return showItemsCountForGroup;
    }

    @Override
    public void setShowItemsCountForGroup(boolean showItemsCountForGroup) {
        this.showItemsCountForGroup = showItemsCountForGroup;
    }

    @Override
    protected String getGeneratedCellStyle(Object itemId, Object propertyId) {
        if (itemId instanceof GroupInfo) {
            List<GroupStyleProvider> groupStyleProviders = null;

            for (StyleProvider styleProvider : styleProviders) {
                if (styleProvider instanceof GroupStyleProvider) {
                    if (groupStyleProviders == null) {
                        groupStyleProviders = new LinkedList<>();
                    }

                    groupStyleProviders.add((GroupStyleProvider) styleProvider);
                }
            }

            if (groupStyleProviders != null) {
                String joinedStyle = null;
                for (GroupStyleProvider groupStyleProvider : groupStyleProviders) {
                    String styleName = groupStyleProvider.getStyleName((GroupInfo) itemId);
                    if (styleName != null) {
                        if (joinedStyle == null) {
                            joinedStyle = styleName;
                        } else {
                            joinedStyle += " " + styleName;
                        }
                    }
                }

                return joinedStyle;
            }
        } else {
            return super.getGeneratedCellStyle(itemId, propertyId);
        }
        return null;
    }

    @Override
    public Map<Object, Object> getAggregationResults(GroupInfo info) {
        return component.aggregate(new CubaGroupTable.GroupAggregationContext(component, info));
    }

    @Override
    public void selectAll() {
        if (isMultiSelect()) {
            if (containerDatasource instanceof WebGroupTable.GroupTableDsWrapper) {
                //noinspection unchecked
                GroupTableDsWrapper containerDatasource = (GroupTableDsWrapper) this.containerDatasource;
                Collection<?> itemIds = containerDatasource.hasGroups()
                        ? containerDatasource.getItemIds(false)
                        : containerDatasource.getItemIds();
                // Filter items that don't exist in the datasource, e.g. GroupInfo
                itemIds = itemIds.stream()
                        .filter(id -> datasource.containsItem(id))
                        .collect(Collectors.toList());

                component.setValue(itemIds);
                return;
            }
        }
        super.selectAll();
    }

    protected class GroupTableDsWrapper extends SortableCollectionDsWrapper
            implements GroupTableContainer, AggregationContainer {

        protected boolean groupDatasource;
        protected List<Object> aggregationProperties = null;

        //Supports items expanding
        protected final Set<GroupInfo> expanded = new HashSet<>();

        protected Set<GroupInfo> expandState = new HashSet<>();

        //Items cache
        protected LinkedList<Object> cachedItemIds;
        protected Object first;
        protected Object last;

        public GroupTableDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties,
                                   CollectionDsListenersWrapper collectionDsListenersWrapper) {
            super(datasource, properties, true, collectionDsListenersWrapper);
            groupDatasource = datasource instanceof GroupDatasource;
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<Object, Column> entry : columns.entrySet()) {
                    if (entry.getKey() instanceof MetaPropertyPath) {
                        properties.add((MetaPropertyPath) entry.getKey());
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, datasource.getMetaClass(), properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                    return new TablePropertyWrapper(item, propertyPath);
                }
            };
        }

        @Override
        public void groupBy(Object[] properties) {
            if (groupDatasource) {
                doGroup(properties);
            }
        }

        protected void doGroup(Object[] properties) {
            saveState();
            ((GroupDatasource) datasource).groupBy(properties);
            restoreState();
            resetCachedItems();

            if (aggregationCells != null) {
                if (hasGroups()) {
                    if (groupAggregationCells == null) {
                        groupAggregationCells = new HashMap<>();
                    } else {
                        groupAggregationCells.clear();
                    }
                    fillGroupAggregationCells(groupAggregationCells);
                } else {
                    groupAggregationCells = null;
                }
            }
        }

        protected void saveState() {
            //save expanding state
            expandState.clear();
            expandState.addAll(expanded);
        }

        protected void restoreState() {
            collapseAll();
            //restore groups expanding
            if (hasGroups()) {
                for (final GroupInfo groupInfo : expandState) {
                    expand(groupInfo);
                }
            }
            expandState.clear();
        }

        protected void fillGroupAggregationCells(Map<Table.Column, GroupAggregationCells> cells) {
            final Collection roots = rootGroups();
            for (final Object rootGroup : roots) {
                __fillGroupAggregationCells(rootGroup, cells);
            }
        }

        protected void __fillGroupAggregationCells(Object groupId, Map<Table.Column, GroupAggregationCells> cells) {
            final Set<Table.Column> aggregatableColumns = aggregationCells.keySet();

            for (final Column column : aggregatableColumns) {
                if (!columns.get(getGroupProperty(groupId)).equals(column)) {
                    GroupAggregationCells groupCells = cells.get(column);
                    if (groupCells == null) {
                        groupCells = new GroupAggregationCells();
                        cells.put(column, groupCells);
                    }
                    groupCells.addCell(groupId, "");
                }
            }

            if (hasChildren(groupId)) {
                final Collection children = getChildren(groupId);
                for (final Object child : children) {
                    __fillGroupAggregationCells(child, cells);
                }
            }
        }

        @Override
        public Collection<?> rootGroups() {
            if (hasGroups()) {
                return ((GroupDatasource) datasource).rootGroups();
            }
            return Collections.emptyList();
        }

        @Override
        public boolean hasChildren(Object id) {
            return isGroup(id) && ((GroupDatasource) datasource).hasChildren((GroupInfo) id);
        }

        @Override
        public Collection<?> getChildren(Object id) {
            if (isGroup(id)) {
                return ((GroupDatasource) datasource).getChildren((GroupInfo) id);
            }
            return Collections.emptyList();
        }

        @Override
        public Collection<?> getGroupItemIds(Object id) {
            if (isGroup(id)) {
                return ((GroupDatasource) datasource).getGroupItemIds((GroupInfo) id);
            }
            return Collections.emptyList();
        }

        @Override
        public int getGroupItemsCount(Object id) {
            if (isGroup(id)) {
                return ((GroupDatasource) datasource).getGroupItemsCount((GroupInfo) id);
            }
            return 0;
        }

        @Override
        public boolean isGroup(Object id) {
            return (id instanceof GroupInfo) && ((GroupDatasource) datasource).containsGroup((GroupInfo) id);
        }

        @Override
        public Object getGroupProperty(Object id) {
            if (isGroup(id)) {
                return ((GroupDatasource) datasource).getGroupProperty((GroupInfo) id);
            }
            return null;
        }

        @Override
        public Object getGroupPropertyValue(Object id) {
            if (isGroup(id)) {
                return ((GroupDatasource) datasource).getGroupPropertyValue((GroupInfo) id);
            }
            return null;
        }

        @Override
        public boolean hasGroups() {
            return groupDatasource && ((GroupDatasource) datasource).hasGroups();
        }

        @Override
        public Collection<?> getGroupProperties() {
            if (hasGroups()) {
                return ((GroupDatasource) datasource).getGroupProperties();
            }
            return Collections.emptyList();
        }

        @Override
        public void expandAll() {
            if (hasGroups()) {
                this.expanded.clear();
                expand(rootGroups());
                resetCachedItems();
            }
        }

        protected void expand(Collection groupIds) {
            for (final Object groupId : groupIds) {
                expanded.add((GroupInfo) groupId);
                if (hasChildren(groupId)) {
                    expand(getChildren(groupId));
                }
            }
        }

        @Override
        public void expand(Object id) {
            if (isGroup(id)) {
                expanded.add((GroupInfo) id);
                resetCachedItems();
            }
        }

        @Override
        public void collapseAll() {
            if (hasGroups()) {
                expanded.clear();
                resetCachedItems();
            }
        }

        @Override
        public void collapse(Object id) {
            if (isGroup(id)) {
                //noinspection RedundantCast
                expanded.remove((GroupInfo) id);
                resetCachedItems();
            }
        }

        @Override
        public boolean isExpanded(Object id) {
            //noinspection RedundantCast
            return isGroup(id) && expanded.contains((GroupInfo) id);
        }

        @Override
        public Collection getAggregationPropertyIds() {
            if (aggregationProperties != null) {
                return Collections.unmodifiableList(aggregationProperties);
            }
            return Collections.emptyList();
        }

        @Override
        public Type getContainerPropertyAggregation(Object propertyId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addContainerPropertyAggregation(Object propertyId, Type type) {
            if (aggregationProperties == null) {
                aggregationProperties = new LinkedList<>();
            } else if (aggregationProperties.contains(propertyId)) {
                throw new IllegalStateException("Such aggregation property is already exists");
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

        @SuppressWarnings("unchecked")
        @Override
        public Map<Object, Object> aggregate(Context context) {
            return __aggregate(this, context);
        }

        @Override
        public Object firstItemId() {
            if (hasGroups()) {
                return first;
            }
            return super.firstItemId();
        }

        @Override
        public Object lastItemId() {
            if (hasGroups()) {
                return last;
            }
            return super.lastItemId();
        }

        @Override
        public Object nextItemId(Object itemId) {
            if (hasGroups()) {
                if (itemId == null) {
                    return null;
                }
                if (isLastId(itemId)) {
                    return null;
                }
                int index = getCachedItemIds().indexOf(itemId);
                return getCachedItemIds().get(index + 1);
            }
            return super.nextItemId(itemId);
        }

        @Override
        public Object prevItemId(Object itemId) {
            if (hasGroups()) {
                if (itemId == null) {
                    return null;
                }

                if (isFirstId(itemId)) {
                    return null;
                }
                int index = getCachedItemIds().indexOf(itemId);
                return getCachedItemIds().get(index - 1);
            }
            return super.prevItemId(itemId);
        }

        @Override
        public boolean isFirstId(Object itemId) {
            if (hasGroups()) {
                return itemId != null && itemId.equals(first);
            }
            return super.isFirstId(itemId);
        }

        @Override
        public boolean isLastId(Object itemId) {
            if (hasGroups()) {
                return itemId != null && itemId.equals(last);
            }
            return super.isLastId(itemId);
        }

        @Override
        public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection getItemIds() {
            if (hasGroups()) {
                return getCachedItemIds();
            } else {
                return super.getItemIds();
            }
        }

        public LinkedList<Object> getItemIds(boolean expandedOnly) {
            final LinkedList<Object> result = new LinkedList<>();
            //noinspection unchecked
            final List<GroupInfo> roots = ((GroupDatasource) datasource).rootGroups();
            for (final GroupInfo root : roots) {
                result.add(root);
                collectItemIds(root, result, expandedOnly);
            }
            return result;
        }

        @Override
        public void sort(Object[] propertyId, boolean[] ascending) {
            resetCachedItems();
            super.sort(propertyId, ascending);
        }

        protected LinkedList getCachedItemIds() {
            if (cachedItemIds == null) {
                cachedItemIds = getItemIds(true);

                if (!cachedItemIds.isEmpty()) {
                    first = cachedItemIds.peekFirst();
                    last = cachedItemIds.peekLast();
                }
            }
            return cachedItemIds;
        }

        protected void collectItemIds(GroupInfo groupId, final List<Object> itemIds, boolean expandedOnly) {
            if (!expandedOnly || expanded.contains(groupId)) {
                if (((GroupDatasource) datasource).hasChildren(groupId)) {
                    @SuppressWarnings("unchecked")
                    final List<GroupInfo> children = ((GroupDatasource) datasource).getChildren(groupId);
                    for (final GroupInfo child : children) {
                        itemIds.add(child);
                        collectItemIds(child, itemIds, expandedOnly);
                    }
                } else {
                    itemIds.addAll(((GroupDatasource) datasource).getGroupItemIds(groupId));
                }
            }
        }

        protected void resetCachedItems() {
            cachedItemIds = null;
            first = null;
            last = null;
        }

        @Override
        public int size() {
            if (hasGroups()) {
                return getItemIds().size();
            }
            return super.size();
        }

        @Override
        protected Datasource.StateChangeListener createStateChangeListener() {
            return new ContainerDatasourceStateChangeListener() {
                @Override
                public void stateChanged(Datasource.StateChangeEvent e) {
                    rerender = false;
                    Collection groupProperties = component.getGroupProperties();
                    component.groupBy(groupProperties.toArray());
                    super.stateChanged(e);
                    rerender = true;
                }
            };
        }

        @Override
        protected CollectionDatasource.CollectionChangeListener createCollectionChangeListener() {
            return new ContainerDatasourceCollectionChangeListener(){
                @Override
                public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
                    Collection groupProperties = component.getGroupProperties();
                    component.groupBy(groupProperties.toArray());
                    super.collectionChanged(e);
                }
            };
        }

        @Override
        public void resetSortOrder() {
            if (datasource instanceof CollectionDatasource.Sortable) {
                ((CollectionDatasource.Sortable) datasource).resetSortOrder();
            }
        }
    }

    @Override
    public void setAggregationDistributionProvider(AggregationDistributionProvider distributionProvider) {
        this.distributionProvider = distributionProvider;

        component.setAggregationDistributionProvider(this::distributeGroupAggregation);
    }

    protected boolean distributeGroupAggregation(AggregationInputValueChangeContext context) {
        if (distributionProvider != null) {
            String value = context.getValue();
            Object columnId = context.getColumnId();
            GroupInfo groupInfo = null;
            try {
                Object parsedValue = getParsedAggregationValue(value, columnId);
                Collection<E> scope = Collections.emptyList();

                if (context.isTotalAggregation()) {
                    //noinspection unchecked
                    scope = getDatasource().getItems();
                } else if (context instanceof GroupAggregationInputValueChangeContext) {
                    Object groupId = ((GroupAggregationInputValueChangeContext) context).getGroupInfo();
                    if (groupId instanceof GroupInfo) {
                        groupInfo = (GroupInfo) groupId;
                        //noinspection unchecked
                        scope = getDatasource().getChildItems(groupInfo);
                    }
                }

                //noinspection unchecked
                GroupAggregationDistributionContext<E> aggregationDistribution =
                        new GroupAggregationDistributionContext(getColumnNN(columnId.toString()),
                                parsedValue, scope, groupInfo, context.isTotalAggregation());
                distributionProvider.onDistribution(aggregationDistribution);
            } catch (ParseException e) {
                showParseErrorNotification();
                return false; // rollback to previous value
            }
        }
        return true;
    }

    protected class AggregatableGroupPropertyValueFormatter extends DefaultGroupPropertyValueFormatter {
        @Override
        public String format(Object groupId, @Nullable Object value) {
            String formattedValue = super.format(groupId, value);

            if (groupCellValueFormatter != null) {
                List<Entity> groupItems = component.getGroupItemIds(groupId).stream()
                    .map(itemId -> ((ItemWrapper) component.getItem(itemId)).getItem())
                    .collect(Collectors.toList());

                GroupCellContext<E> context = new GroupCellContext<>((GroupInfo) groupId, value, formattedValue,
                        (List<E>) groupItems);
                return groupCellValueFormatter.format(context);
            }

            if (showItemsCountForGroup) {
                int count = WebGroupTable.this.component.getGroupItemsCount(groupId);
                return String.format("%s (%d)", formattedValue == null ? "" : formattedValue, count);
            } else {
                return formattedValue == null ? "" : formattedValue;
            }
        }
    }

    protected class DefaultGroupPropertyValueFormatter implements CubaGroupTable.GroupPropertyValueFormatter {

        @SuppressWarnings("unchecked")
        @Override
        public String format(Object groupId, @Nullable Object value) {
            if (value == null) {
                return "";
            }
            final MetaPropertyPath propertyPath = ((GroupInfo<MetaPropertyPath>) groupId).getProperty();
            final Table.Column column = columns.get(propertyPath);
            if (column != null && column.getXmlDescriptor() != null) {
                String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                if (column.getFormatter() != null) {
                    return column.getFormatter().format(value);
                } else if (StringUtils.isNotEmpty(captionProperty)) {
                    Collection<?> children = component.getGroupItemIds(groupId);
                    if (children.isEmpty()) {
                        return null;
                    }

                    Object itemId = children.iterator().next();
                    Instance item = ((ItemWrapper) component.getItem(itemId)).getItem();
                    final Object captionValue = item.getValueEx(captionProperty);
                    return captionValue != null ? String.valueOf(captionValue) : null;
                }
            }

            MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
            return metadataTools.format(value, propertyPath.getMetaProperty());
        }
    }

    protected static class GroupAggregationCells {
        protected Map<Object, String> cells = new HashMap<>();

        public void addCell(Object groupId, String value) {
            cells.put(groupId, value);
        }

        public String getValue(Object groupId) {
            return cells.get(groupId);
        }
    }

    @Override
    public void addColumn(Column column) {
        super.addColumn(column);

        setColumnGroupAllowed(column, column.isGroupAllowed());
    }

    @Override
    protected CollectionDsListenersWrapper createCollectionDsListenersWrapper() {
        return new GroupTableCollectionDsListenersWrapper();
    }

    public class GroupTableCollectionDsListenersWrapper extends TableCollectionDsListenersWrapper {
        @Override
        protected void handleAggregation() {
            super.handleAggregation();

            if (isAggregatable() && aggregationCells != null) {
                if (datasource instanceof GroupDatasource) {
                    GroupDatasource groupDs = ((GroupDatasource) datasource);
                    @SuppressWarnings("unchecked")
                    Collection<GroupInfo> roots = groupDs.rootGroups();
                    for (final GroupInfo root : roots) {
                        component.aggregate(new CubaGroupTable.GroupAggregationContext(component, root));
                    }
                }
            }
        }
    }
}