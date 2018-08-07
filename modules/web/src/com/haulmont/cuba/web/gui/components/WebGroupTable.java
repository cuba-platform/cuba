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
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.data.GroupTableSource;
import com.haulmont.cuba.gui.components.data.TableSource;
import com.haulmont.cuba.gui.components.data.table.GroupDatasourceTableAdapter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.web.gui.components.table.GroupTableDataContainer;
import com.haulmont.cuba.web.gui.components.table.TableDataContainer;
import com.haulmont.cuba.web.widgets.CubaGroupTable;
import com.haulmont.cuba.web.widgets.CubaGroupTable.GroupAggregationContext;
import com.haulmont.cuba.web.widgets.data.AggregationContainer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.emptyToNull;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@SuppressWarnings("deprecation")
public class WebGroupTable<E extends Entity> extends WebAbstractTable<CubaGroupTable, E> implements GroupTable<E> {

    protected Map<Table.Column, GroupAggregationCells> groupAggregationCells = null;

    protected boolean rerender = true;
    protected boolean showItemsCountForGroup = true;

    protected GroupCellValueFormatter<E> groupCellValueFormatter;

    public WebGroupTable() {
        component = createComponent();
    }

    @Override
    public void setTableSource(TableSource<E> tableSource) {
        if (tableSource != null &&
                !(tableSource instanceof GroupTableSource)) {
            throw new IllegalArgumentException("GroupTable supports only GroupTableSource data binding");
        }

        super.setTableSource(tableSource);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(CollectionDatasource datasource) {
        if (datasource == null) {
            setTableSource(null);
        } else {
            if (!(datasource instanceof GroupDatasource)) {
                throw new IllegalArgumentException("GroupTable supports only GroupDatasource");
            }

            setTableSource(new GroupDatasourceTableAdapter((GroupDatasource) datasource));
        }
    }

    @Override
    protected TableDataContainer<E> createTableDataContainer(TableSource<E> tableSource) {
        return new GroupTableDataContainer<>((GroupTableSource<E>) tableSource, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initComponent(CubaGroupTable component) {
        super.initComponent(component);

        component.setGroupPropertyValueFormatter((groupId, value) ->
                formatAggregatableGroupPropertyValue((GroupInfo<MetaPropertyPath>) groupId, value)
        );
    }

    protected CubaGroupTable createComponent() {
        return new CubaGroupTableExt();
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

        for (Object groupProperty : component.getGroupProperties()) {
            Column<E> column = getColumn(groupProperty.toString());

            if (getNotCollapsedColumns().contains(column)) {
                Element groupPropertyElement = groupPropertiesElement.addElement("property");
                groupPropertyElement.addAttribute("id", groupProperty.toString());
            }
        }

        return true;
    }

    @Override
    public void applyColumnSettings(Element element) {
        super.applyColumnSettings(element);

        Element groupPropertiesElement = element.element("groupProperties");
        if (groupPropertiesElement != null) {
            List elements = groupPropertiesElement.elements("property");
            List<MetaPropertyPath> properties = new ArrayList<>(elements.size());
            for (Object o : elements) {
                String id = ((Element) o).attributeValue("id");
                MetaPropertyPath property = DynamicAttributesUtils.isDynamicAttribute(id)
                        ? dynamicAttributesTools.getMetaPropertyPath(getDatasource().getMetaClass(), id)
                        : getDatasource().getMetaClass().getPropertyPath(id);

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
    protected Map<Object, Object> __handleAggregationResults(AggregationContainer.Context context,
                                                             Map<Object, Object> results) {
        if (context instanceof GroupAggregationContext) {
            GroupAggregationContext groupContext = (GroupAggregationContext) context;

            for (Map.Entry<Object, Object> entry : results.entrySet()) {
                Table.Column column = columns.get(entry.getKey());
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
        List<Object> allProps = Lists.newArrayList(component.getVisibleColumns()); // mutable list required
        List<Object> newGroupProps = Arrays.asList(newGroupProperties);

        allProps.removeAll(newGroupProps);
        allProps.addAll(0, newGroupProps);

        return allProps.toArray();
    }

    protected List<Object> collectPropertiesByColumns(String... columnIds) {
        List<Object> properties = new ArrayList<>(columnIds.length);

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
        checkNotNullArgument(properties);
        validateProperties(properties);

        if (uselessGrouping(properties)) {
            return;
        }

        component.groupBy(properties);
        component.setColumnOrder(getNewColumnOrder(properties));
    }

    @Override
    public void groupByColumns(String... columnIds) {
        checkNotNullArgument(columnIds);

        if (uselessGrouping(columnIds)) {
            return;
        }

        groupBy(collectPropertiesByColumns(columnIds).toArray());
    }

    @Override
    public void ungroupByColumns(String... columnIds) {
        checkNotNullArgument(columnIds);

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
        if (!component.getGroupProperties().contains(propertyId)) {
            return super.getGeneratedCellStyle(itemId, propertyId);
        }

        if (itemId instanceof GroupInfo) {
            GroupInfo groupInfo = (GroupInfo) itemId;

            String joinedStyle = styleProviders.stream()
                    .filter(sp -> sp instanceof GroupStyleProvider)
                    .map(sp -> ((GroupStyleProvider) sp).getStyleName(groupInfo))
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" "));

            return emptyToNull(joinedStyle);
        } else {
            return super.getGeneratedCellStyle(itemId, propertyId);
        }
    }

    @Override
    public Map<Object, Object> getAggregationResults(GroupInfo info) {
        return component.aggregate(new GroupAggregationContext(component, info));
    }

    @Override
    public void selectAll() {
        if (isMultiSelect()) {
            if (getTableSource() instanceof GroupTableSource) {
                GroupTableSource<E> tableSource = (GroupTableSource<E>) getTableSource();
                Collection<?> itemIds = tableSource.hasGroups()
                        ? getAllItemIds(tableSource)
                        : tableSource.getItemIds();
                component.setValue(itemIds);
                return;
            }
        }
        super.selectAll();
    }

    protected LinkedList<Object> getAllItemIds(GroupTableSource<E> tableSource) {
        List<GroupInfo> roots = tableSource.rootGroups();
        final LinkedList<Object> result = new LinkedList<>();
        for (final GroupInfo root : roots) {
            result.add(root);
            collectItemIds(root, result);
        }
        return result;
    }

    protected void collectItemIds(GroupInfo groupId, final List<Object> itemIds) {
        GroupTableSource<E> groupTableSource = (GroupTableSource<E>) getTableSource();
        if (groupTableSource.hasChildren(groupId)) {
            final List<GroupInfo> children = groupTableSource.getChildren(groupId);
            for (final GroupInfo child : children) {
                itemIds.add(child);
                collectItemIds(child, itemIds);
            }
        } else {
            itemIds.addAll(groupTableSource.getGroupItemIds(groupId));
        }
    }

    protected String formatAggregatableGroupPropertyValue(GroupInfo<MetaPropertyPath> groupId, @Nullable Object value) {
        String formattedValue = formatGroupPropertyValue(groupId, value);

        if (groupCellValueFormatter != null) {
            List<Entity> groupItems = component.getGroupItemIds(groupId).stream()
                    .map(itemId -> {
                        TableDataContainer container = (TableDataContainer) component.getContainerDataSource();
                        return (Entity) container.getInternalItem(itemId);
                    })
                    .collect(Collectors.toList());

            @SuppressWarnings("unchecked")
            GroupCellContext<E> context = new GroupCellContext<>(groupId, value, formattedValue, (List<E>) groupItems);
            return groupCellValueFormatter.format(context);
        }

        if (showItemsCountForGroup) {
            int count = this.component.getGroupItemsCount(groupId);
            return String.format("%s (%d)", formattedValue == null ? "" : formattedValue, count);
        } else {
            return formattedValue == null ? "" : formattedValue;
        }
    }

    @SuppressWarnings("unchecked")
    protected String formatGroupPropertyValue(GroupInfo<MetaPropertyPath> groupId, @Nullable Object value) {
        if (value == null) {
            return "";
        }

        MetaPropertyPath propertyPath = groupId.getProperty();
        Table.Column column = columns.get(propertyPath);
        if (column != null && column.getXmlDescriptor() != null) {
            String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty"); // vaadin8 move to Column
            if (column.getFormatter() != null) {
                return column.getFormatter().format(value);
            } else if (StringUtils.isNotEmpty(captionProperty)) {
                Collection<?> children = component.getGroupItemIds(groupId);
                if (children.isEmpty()) {
                    return null;
                }

                Object itemId = children.iterator().next();

                TableDataContainer container = (TableDataContainer) component.getContainerDataSource();

                Instance item = (Instance) container.getInternalItem(itemId);
                Object captionValue = item.getValueEx(captionProperty);
                // vaadin8 use metadataTools format with metaproperty
                return metadataTools.format(captionValue);
            }
        }

        return metadataTools.format(value, propertyPath.getMetaProperty());
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
    public void addColumn(Column<E> column) {
        super.addColumn(column);

        setColumnGroupAllowed(column, column.isGroupAllowed());
    }

    protected class CubaGroupTableExt extends CubaGroupTable {
        @Override
        protected boolean isNonGeneratedProperty(Object id) {
            return (id instanceof MetaPropertyPath);
        }

        @Override
        public void groupBy(Object[] properties) {
            groupBy(properties, rerender);
        }

        @Override
        protected LinkedHashSet<Object> getItemIdsInRange(Object startItemId, int length) {
            Set<Object> rootIds = super.getItemIdsInRange(startItemId, length);
            LinkedHashSet<Object> ids = new LinkedHashSet<>();
            for (Object itemId: rootIds) {
                if (itemId instanceof GroupInfo) {
                    if (!isExpanded(itemId)) {
                        Collection<?> itemIds = getGroupItemIds(itemId);
                        ids.addAll(itemIds);
                        expand(itemId, true);
                    }

                    @SuppressWarnings("unchecked")
                    List<GroupInfo> children = (List<GroupInfo>) getChildren(itemId);
                    for (GroupInfo groupInfo : children) {
                        if (!isExpanded(groupInfo)) {
                            expand(groupInfo, true);
                        }
                    }
                } else {
                    ids.add(itemId);
                }
            }
            return ids;
        }
    }
}