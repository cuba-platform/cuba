/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.web.gui.data.PropertyValueStringify;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.data.GroupTableContainer;
import com.haulmont.cuba.web.toolkit.data.util.GroupTableContainerWrapper;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class CubaGroupTable extends CubaTable implements GroupTableContainer {

    protected KeyMapper groupIdMap = new KeyMapper();

    protected GroupPropertyValueFormatter groupPropertyValueFormatter;

    protected boolean fixedGrouping = false;

    protected boolean requestColumnReorderingAllowed = true;

    @Override
    public void setContainerDataSource(Container newDataSource) {
        if (newDataSource == null) {
            newDataSource = new IndexedContainer();
        }

        super.setContainerDataSource(new GroupTableContainerWrapper(newDataSource));
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        if (property instanceof PropertyValueStringify)
            return ((PropertyValueStringify) property).getFormattedValue();

        return super.formatPropertyValue(rowId, colId, property);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (hasGroups()) {
            final Collection groupProperties = getGroupProperties();
            final String[] groupColumns = new String[groupProperties.size()];

            int index = 0;
            for (final Object groupColumnId : groupProperties) {
                groupColumns[index++] = columnIdMap.key(groupColumnId);
            }
            target.addVariable(this, "groupColumns", groupColumns);
        }
    }

    @Override
    public boolean isColumnReorderingAllowed() {
        return requestColumnReorderingAllowed && super.isColumnReorderingAllowed();
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        Object[] newGroupProperties = null;

        if (variables.containsKey("columnorder") && !variables.containsKey("groupedcolumns")) {
            newGroupProperties = new Object[0];
        } else if (variables.containsKey("groupedcolumns")) {
            focus();

            final Object[] ids = (Object[]) variables.get("groupedcolumns");
            final Object[] groupProperties = new Object[ids.length];
            for (int i = 0; i < ids.length; i++) {
                groupProperties[i] = columnIdMap.get(ids[i].toString());
            }
            newGroupProperties = groupProperties;
            // Deny group by generated columns
            if (!columnGenerators.isEmpty()) {
                List<Object> notGeneratedProperties = new ArrayList<>();
                for (Object id : newGroupProperties) {
                    // todo support grouping by generated columns with Printable
                    if (!columnGenerators.containsKey(id) || (id instanceof MetaPropertyPath)) {
                        notGeneratedProperties.add(id);
                    }
                }
                newGroupProperties = notGeneratedProperties.toArray();
            }
        }

        if (variables.containsKey("collapsedcolumns")) {
            final Object[] ids = (Object[]) variables
                    .get("collapsedcolumns");
            Set<Object> idSet = new HashSet<>();
            for (Object id : ids) {
                idSet.add(columnIdMap.get(id.toString()));
            }

            boolean needToRegroup = false;
            final List<Object> groupProperties = new ArrayList<>(getGroupProperties());
            for (int index = 0; index < groupProperties.size(); index++) {
                final Object propertyId = groupProperties.get(index);
                if (idSet.contains(propertyId)) {
                    groupProperties.subList(index, groupProperties.size()).clear();
                    needToRegroup = true;
                    break;
                }
            }
            if (needToRegroup) {
                newGroupProperties = groupProperties.toArray();
            }
        }

        if (fixedGrouping && isGroupsChanged(newGroupProperties)) {
            requestColumnReorderingAllowed = false;
            markAsDirty();
        }

        super.changeVariables(source, variables);

        if (!fixedGrouping && newGroupProperties != null && isGroupsChanged(newGroupProperties)) {
            groupBy(newGroupProperties, true);
        }

        requestColumnReorderingAllowed = true;
    }

    protected boolean isGroupsChanged(Object[] newGroupProperties) {
        Collection<?> oldGroupProperties = getGroupProperties();
        if (newGroupProperties == null && oldGroupProperties == null)
            return false;

        if (newGroupProperties == null)
            return true;

        if (oldGroupProperties == null)
            return true;

        if (oldGroupProperties.size() != newGroupProperties.length)
            return true;

        int i = 0;
        for (Object oldGroupProperty : oldGroupProperties) {
            if (!ObjectUtils.equals(oldGroupProperty, newGroupProperties[i]))
                return true;
            i++;
        }

        return false;
    }

    @Override
    protected boolean changeVariables(Map<String, Object> variables) {
        boolean clientNeedsContentRefresh = super.changeVariables(variables);

        boolean needsResetPageBuffer = false;

        if (variables.containsKey("expand")) {
            focus();

            Object groupId = groupIdMap.get((String) variables.get("expand"));
            expand(groupId, false);
            clientNeedsContentRefresh = true;
            needsResetPageBuffer = true;
        }

        if (variables.containsKey("collapse")) {
            focus();

            Object groupId = groupIdMap.get((String) variables.get("collapse"));
            collapse(groupId, false);
            clientNeedsContentRefresh = true;
            needsResetPageBuffer = true;
        }

        if (needsResetPageBuffer) {
            resetPageBuffer();
        }

        return clientNeedsContentRefresh;
    }

    @Override
    protected boolean isCellPaintingNeeded(Object itemId, Object columnId) {
        return !isGroup(itemId);
    }

    @Override
    protected void paintRowAttributes(PaintTarget target, Object itemId) throws PaintException {
        super.paintRowAttributes(target, itemId);

        boolean hasAggregation = items instanceof AggregationContainer && isAggregatable()
                && !((AggregationContainer) items).getAggregationPropertyIds().isEmpty();

        boolean hasGroups = hasGroups();
        if (hasGroups) {
            if (isGroup(itemId)) {
                target.addAttribute("colKey", columnIdMap.key(getGroupProperty(itemId)));
                target.addAttribute("groupKey", groupIdMap.key(itemId));
                if (isExpanded(itemId))
                    target.addAttribute("expanded", true);

                final Object propertyValue = getGroupPropertyValue(itemId);
                target.addAttribute("groupCaption", formatGroupPropertyValue(itemId, propertyValue));

                if (hasAggregation) {
                    paintGroupAggregation(target, itemId,
                            ((AggregationContainer) items).aggregate(new GroupAggregationContext(this, itemId)));
                }
            }
        }
    }

    @Override
    protected Collection<?> getAggregationItemIds() {
        if (hasGroups()) {
            List itemIds = new LinkedList();
            for (final Object groupId : rootGroups()) {
                itemIds.addAll(getGroupItemIds(groupId));
            }
            return itemIds;
        } else {
            return items.getItemIds();
        }
    }

    protected void paintGroupAggregation(PaintTarget target, Object groupId, Map<Object, Object> aggregations)
            throws PaintException {
        boolean paintGroupProperty = false;

        final Collection groupProperties = getGroupProperties();
        final Object groupProperty = getGroupProperty(groupId);

        for (final Object columnId : visibleColumns) {
            if (columnId == null || isColumnCollapsed(columnId)) {
                continue;
            }

            if (groupProperties.contains(columnId) && !paintGroupProperty) {
                if (columnId.equals(groupProperty)) {
                    paintGroupProperty = true;
                }
                continue;
            }

            if (getCellStyleGenerator() != null) {
                String cellStyle = getCellStyleGenerator().getStyle(this, null, columnId);
                if (cellStyle != null && !cellStyle.equals("")) {
                    target.addAttribute("style-" + columnIdMap.key(columnId), cellStyle + "-ag");
                }
            }

            String value = (String) aggregations.get(columnId);
            if (value != null) {
                target.addText(value);
            } else {
                target.addText("");
            }
        }
    }

    @Override
    protected LinkedHashSet<Object> getItemIdsInRange(Object startItemId, final int length) {
        Set<Object> rootIds = super.getItemIdsInRange(startItemId, length);
        LinkedHashSet<Object> ids = new LinkedHashSet<>();
        for (Object itemId: rootIds) {
            if (itemId instanceof GroupInfo) {
                if (!isExpanded(itemId)) {
                    Collection<?> itemIds = getGroupItemIds(itemId);
                    ids.addAll(itemIds);
                    expand(itemId, true);
                }

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

    @Override
    protected boolean isColumnNeedsToRefreshRendered(Object colId) {
        final GroupTableContainer items = (GroupTableContainer) this.items;
        final boolean groupped = items.hasGroups();

        return !groupped || !getGroupProperties().contains(colId);
    }

    @Override
    protected boolean isItemNeedsToRefreshRendered(Object itemId) {
        final GroupTableContainer items = (GroupTableContainer) this.items;
        final boolean groupped = items.hasGroups();

        return !groupped || !items.isGroup(itemId);
    }

    protected String formatGroupPropertyValue(Object groupId, Object groupValue) {
        return groupPropertyValueFormatter != null
                ? groupPropertyValueFormatter.format(groupId, groupValue)
                : (groupValue == null ? "" : groupValue.toString());
    }

    protected void expand(Object id, boolean rerender) {
        final int pageIndex = getCurrentPageFirstItemIndex();
        ((GroupTableContainer) items).expand(id);
        setCurrentPageFirstItemIndex(pageIndex, false);
        if (rerender) {
            resetPageBuffer();
            refreshRenderedCells();
            markAsDirty();
        }
    }

    protected void collapse(Object id, boolean rerender) {
        final int pageIndex = getCurrentPageFirstItemIndex();
        ((GroupTableContainer) items).collapse(id);
        setCurrentPageFirstItemIndex(pageIndex, false);
        if (rerender) {
            resetPageBuffer();
            refreshRenderedCells();
            markAsDirty();
        }
    }

    protected void groupBy(Object[] properties, boolean rerender) {
        GroupTableContainer groupTableContainer = (GroupTableContainer) items;
        if (groupTableContainer.getGroupProperties().isEmpty() && properties.length == 0) {
            // no need to regroup and refreshRenderedCells
            return;
        }

        groupTableContainer.groupBy(properties);
        if (rerender) {
            resetPageBuffer();
            refreshRenderedCells();
            markAsDirty();
        }
    }

    @Override
    public Collection<?> getGroupProperties() {
        Collection<?> groupProperties = ((GroupTableContainer) items).getGroupProperties();
        // Deny group by generated columns
        if (!columnGenerators.isEmpty()) {
            List<Object> notGeneratedGroupProps = new ArrayList<>();
            for (Object id : groupProperties) {
                if (!columnGenerators.containsKey(id) || (id instanceof MetaPropertyPath))
                    notGeneratedGroupProps.add(id);
            }
            return notGeneratedGroupProps;
        } else
            return groupProperties;
    }

    @Override
    public void expandAll() {
        final int pageIndex = getCurrentPageFirstItemIndex();
        ((GroupTableContainer) items).expandAll();
        setCurrentPageFirstItemIndex(pageIndex, false);
        resetPageBuffer();
        refreshRenderedCells();
        markAsDirty();
    }

    @Override
    public void expand(Object id) {
        expand(id, true);
    }

    @Override
    public void collapseAll() {
        final int pageIndex = getCurrentPageFirstItemIndex();
        ((GroupTableContainer) items).collapseAll();
        setCurrentPageFirstItemIndex(pageIndex, false);
        resetPageBuffer();
        refreshRenderedCells();
        markAsDirty();
    }

    @Override
    public void collapse(Object id) {
        collapse(id, true);
    }

    @Override
    public boolean hasGroups() {
        return ((GroupTableContainer) items).hasGroups();
    }

    @Override
    public void groupBy(Object[] properties) {
        groupBy(properties, true);
    }

    @Override
    public boolean isGroup(Object itemId) {
        return ((GroupTableContainer) items).isGroup(itemId);
    }

    @Override
    public Collection<?> rootGroups() {
        return ((GroupTableContainer) items).rootGroups();
    }

    @Override
    public boolean hasChildren(Object id) {
        return ((GroupTableContainer) items).hasChildren(id);
    }

    @Override
    public Collection<?> getChildren(Object id) {
        return ((GroupTableContainer) items).getChildren(id);
    }

    @Override
    public Object getGroupProperty(Object itemId) {
        return ((GroupTableContainer) items).getGroupProperty(itemId);
    }

    @Override
    public Object getGroupPropertyValue(Object itemId) {
        return ((GroupTableContainer) items).getGroupPropertyValue(itemId);
    }

    @Override
    public Collection<?> getGroupItemIds(Object itemId) {
        return ((GroupTableContainer) items).getGroupItemIds(itemId);
    }

    @Override
    public int getGroupItemsCount(Object itemId) {
        return ((GroupTableContainer) items).getGroupItemsCount(itemId);
    }

    @Override
    public boolean isExpanded(Object id) {
        return ((GroupTableContainer) items).isExpanded(id);
    }

    public boolean isFixedGrouping() {
        return fixedGrouping;
    }

    public void setFixedGrouping(boolean fixedGrouping) {
        this.fixedGrouping = fixedGrouping;
        markAsDirty();
    }

    public GroupPropertyValueFormatter getGroupPropertyValueFormatter() {
        return groupPropertyValueFormatter;
    }

    public void setGroupPropertyValueFormatter(GroupPropertyValueFormatter groupPropertyValueFormatter) {
        this.groupPropertyValueFormatter = groupPropertyValueFormatter;
    }

    public interface GroupPropertyValueFormatter {
        String format(Object groupId, @Nullable Object value);
    }

    public static class GroupAggregationContext extends Context {
        private Object groupId;

        public GroupAggregationContext(GroupTableContainer datasource, Object groupId) {
            super(datasource.getGroupItemIds(groupId));
            this.groupId = groupId;
        }

        public Object getGroupId() {
            return groupId;
        }
    }
}