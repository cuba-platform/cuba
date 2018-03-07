/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.haulmont.cuba.web.widgets.client.table.CubaTableClientRpc;
import com.haulmont.cuba.web.widgets.client.table.CubaTableServerRpc;
import com.haulmont.cuba.web.widgets.client.treetable.CubaTreeTableState;
import com.haulmont.cuba.web.widgets.data.AggregationContainer;
import com.haulmont.cuba.web.widgets.data.PropertyValueStringify;
import com.haulmont.cuba.web.widgets.data.TableContainer;
import com.haulmont.cuba.web.widgets.data.TreeTableContainer;
import com.haulmont.cuba.web.widgets.data.util.TreeTableContainerWrapper;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.ContainerOrderedWrapper;
import com.vaadin.v7.data.util.HierarchicalContainer;
import com.vaadin.v7.ui.Field;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class CubaTreeTable extends com.vaadin.v7.ui.TreeTable implements TreeTableContainer, CubaEnhancedTable {

    protected LinkedList<Object> editableColumns = null;

    /**
     * Keeps track of the ShortcutListeners added to this component, and manages the painting and handling as well.
     */
    protected ActionManager shortcutActionManager;

    protected boolean autowirePropertyDsForFields = false;

    protected boolean showTotalAggregation = true;

    protected boolean aggregatable = false;

    protected Set<Object> nonSortableProperties; // lazily initialized Set

    protected Map<Object, CellClickListener> cellClickListeners; // lazily initialized map

    protected Map<Object, String> columnDescriptions; // lazily initialized map

    protected Map<Object, String> aggregationTooltips; // lazily initialized map

    protected AggregationStyle aggregationStyle = AggregationStyle.TOP;
    protected Object focusColumn;
    protected Object focusItem;
    protected Runnable beforePaintListener;

    public CubaTreeTable() {
        //noinspection Convert2Lambda
        registerRpc(new CubaTableServerRpc() {
            @Override
            public void onClick(String columnKey, String rowKey) {
                Object columnId = _columnIdMap().get(columnKey);
                Object itemId = itemIdMapper.get(rowKey);

                if (cellClickListeners != null) {
                    CellClickListener cellClickListener = cellClickListeners.get(columnId);
                    if (cellClickListener != null) {
                        cellClickListener.onClick(itemId, columnId);
                    }
                }
            }
        });
    }

    @Override
    protected CubaTreeTableState getState() {
        return (CubaTreeTableState) super.getState();
    }

    @Override
    protected CubaTreeTableState getState(boolean markAsDirty) {
        return (CubaTreeTableState) super.getState(markAsDirty);
    }

    @Override
    public void setSortResetLabel(String tableSortResetLabel) {
        getState(true).tableSortResetLabel = tableSortResetLabel;
    }

    @Override
    public String getSortResetLabel() {
        return getState().tableSortResetLabel;
    }

    @Override
    public void setSortAscendingLabel(String tableSortAscendingLabel) {
        getState(true).tableSortAscendingLabel = tableSortAscendingLabel;
    }

    @Override
    public String getSortAscendingLabel() {
        return getState().tableSortAscendingLabel;
    }

    @Override
    public void setSortDescendingLabel(String tableSortDescendingLabel) {
        getState(true).tableSortDescendingLabel = tableSortDescendingLabel;
    }

    @Override
    public String getSortDescendingLabel() {
        return getState().tableSortDescendingLabel;
    }

    @Override
    public Component getPresentations() {
        return (Component) getState(false).presentations;
    }

    @Override
    public void setPresentations(Component presentations) {
        getState().presentations = presentations;
    }

    @Override
    public void setContextMenuPopup(Layout contextMenu) {
        getState().contextMenu = contextMenu;
    }

    @Override
    public void hideContextMenuPopup() {
        getRpcProxy(CubaTableClientRpc.class).hideContextMenuPopup();
    }

    @Override
    public void hidePresentationsPopup() {
        getRpcProxy(CubaTableClientRpc.class).hidePresentationsPopup();
    }

    @Override
    public boolean isTextSelectionEnabled() {
        return getState(false).textSelectionEnabled;
    }

    @Override
    public void setTextSelectionEnabled(boolean textSelectionEnabled) {
        if (isTextSelectionEnabled() != textSelectionEnabled) {
            getState(true).textSelectionEnabled = textSelectionEnabled;
        }
    }

    @Override
    public void setMultiLineCells(boolean multiLineCells) {
        if (isMultiLineCells() != multiLineCells) {
            getState(true).multiLineCells = multiLineCells;
        }
    }

    @Override
    public boolean isMultiLineCells() {
        return getState(false).multiLineCells;
    }

    @Override
    public boolean disableContentBufferRefreshing() {
        return disableContentRefreshing();
    }

    @Override
    public void enableContentBufferRefreshing(boolean refreshContent) {
        enableContentRefreshing(refreshContent);
    }

    @Override
    protected Object getPropertyValue(Object rowId, Object colId,
                                      Property property) {
        if (isColumnEditable(colId, isEditable()) && _fieldFactory() != null) {
            final Field<?> f = _fieldFactory().createField(
                    getContainerDataSource(), rowId, colId, this);
            if (f != null) {
                // Remember that we have made this association so we can remove
                // it when the component is removed
                _associatedProperties().put(f, property);
                if (autowirePropertyDsForFields) {
                    bindPropertyToField(rowId, colId, property, f);
                }
                return f;
            }
        }

        return formatPropertyValue(rowId, colId, property);
    }

    @Override
    public boolean isAutowirePropertyDsForFields() {
        return autowirePropertyDsForFields;
    }

    @Override
    public void setAutowirePropertyDsForFields(boolean autowirePropertyDsForFields) {
        this.autowirePropertyDsForFields = autowirePropertyDsForFields;
    }

    @Override
    public boolean isContextMenuEnabled() {
        return getState(false).contextMenuEnabled;
    }

    @Override
    public void setContextMenuEnabled(boolean contextMenuEnabled) {
        if (isContextMenuEnabled() != contextMenuEnabled) {
            getState(true).contextMenuEnabled = contextMenuEnabled;
        }
    }

    @Override
    public boolean isCaption(Object itemId) {
        return items instanceof TreeTableContainer
                && ((TreeTableContainer) items).isCaption(itemId);
    }

    @Override
    public String getCaption(Object itemId) {
        return ((TreeTableContainer) items).getCaption(itemId);
    }

    @Override
    public boolean setCaption(Object itemId, String caption) {
        return ((TreeTableContainer) items).setCaption(itemId, caption);
    }

    @Override
    public int getLevel(Object itemId) {
        return ((TreeTableContainer) items).getLevel(itemId);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {

        if (Page.getCurrent().getWebBrowser().isIE() && variables.containsKey("clickEvent")) {
            focus();
        }

        super.changeVariables(source, variables);

        if (shortcutActionManager != null) {
            shortcutActionManager.handleActions(variables, this);
        }
    }

    @Override
    public Registration addShortcutListener(ShortcutListener shortcut) {
        if (shortcutActionManager == null) {
            shortcutActionManager = new ShortcutActionManager(this);
        }

        shortcutActionManager.addAction(shortcut);
        return () -> getActionManager().removeAction(shortcut);
    }

    @Override
    public void removeShortcutListener(ShortcutListener shortcut) {
        if (shortcutActionManager != null) {
            shortcutActionManager.removeAction(shortcut);
        }
    }

    @Override
    protected void paintActions(PaintTarget target, Set<Action> actionSet) throws PaintException {
        super.paintActions(target, actionSet);

        if (shortcutActionManager != null) {
            shortcutActionManager.paintActions(null, target);
        }
    }

    @Override
    public Object[] getEditableColumns() {
        if (editableColumns == null) {
            return null;
        }
        return editableColumns.toArray();
    }

    @Override
    public void setEditableColumns(Object[] editableColumns) {
        checkNotNull(editableColumns, "You cannot set null as editable columns");

        if (this.editableColumns == null) {
            this.editableColumns = new LinkedList<>();
        } else {
            this.editableColumns.clear();
        }

        final Collection properties = getContainerPropertyIds();
        for (final Object editableColumn : editableColumns) {
            if (editableColumn == null) {
                throw new IllegalStateException("Ids must be non-nulls");
            } else if (!properties.contains(editableColumn)
                    || _columnGenerators().containsKey(editableColumn)) {
                throw new IllegalArgumentException(
                        "Ids must exist in the Container and it must be not a generated column, incorrect id: "
                                + editableColumn);
            }
            this.editableColumns.add(editableColumn);
        }

        refreshRowCache();
    }

    @Override
    protected boolean isColumnEditable(Object columnId, boolean editable) {
        return editable &&
                editableColumns != null && editableColumns.contains(columnId);
    }

    @Override
    public boolean isColumnEditable(Object columnId) {
        return isColumnEditable(columnId, isEditable());
    }

    @Override
    public void addGeneratedColumn(Object id, ColumnGenerator generatedColumn) {
        if (generatedColumn == null) {
            throw new IllegalArgumentException(
                    "Can not add null as a GeneratedColumn");
        }
        if (_columnGenerators().containsKey(id)) {
            throw new IllegalArgumentException(
                    "Can not add the same GeneratedColumn twice, id:" + id);
        } else {
            if (!(generatedColumn instanceof PlainTextGeneratedColumn)) {
                // do not use custom cache row settings for table with generated columns
                if (getCacheRate() != 2) {
                    setCacheRate(2);
                }
            }

            _columnGenerators().put(id, generatedColumn);
            /*
             * add to visible column list unless already there (overriding
             * column from DS)
             */
            if (!_visibleColumns().contains(id)) {
                _visibleColumns().add(id);
            }

            if (editableColumns != null) {
                editableColumns.remove(id);
            }

            refreshRowCache();
        }
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);

        if (editable) {
            if (getCacheRate() != 2) {
                setCacheRate(2);
            }
        }
    }

    @Override
    protected boolean changeVariables(Map<String, Object> variables) {
        boolean clientNeedsContentRefresh = super.changeVariables(variables);

        if (variables.containsKey("resetsortorder")) {
            resetSortOrder();

            markAsDirty();
        }

        return clientNeedsContentRefresh;
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        if (property instanceof PropertyValueStringify) {
            return ((PropertyValueStringify) property).getFormattedValue();
        }

        return super.formatPropertyValue(rowId, colId, property);
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        disableContentRefreshing();
        if (newDataSource == null) {
            newDataSource = new HierarchicalContainer();
        }

        super.setContainerDataSource(new TreeTableContainerWrapper(newDataSource));
    }

    public void expandAll() {
        expandAllItems();
    }

    public void expandItemRecursively(Object id) {
        setCollapsed(id, false);
        for (Object childId: getChildren(id)) {
            expandItemRecursively(childId);
        }
    }

    public void expandItemWithParents(Object id) {
        Object currentId = id;
        while (currentId != null) {
            setCollapsed(currentId, false);

            currentId = getParent(currentId);
        }
    }

    public void collapseItemRecursively(Object id) {
        if (hasChildren(id)) {
            for (Object childId: getChildren(id)) {
                collapseItemRecursively(childId);
            }
        }
        setCollapsed(id, true);
    }

    public void collapseAll() {
        for (Object id : getItemIds()) {
            collapseItemRecursively(id);
        }
    }

    public void expandUpTo(int level) {
        Preconditions.checkArgument(level > 0, "level should be greater than 0");

        List<Object> currentLevelItemIds = new ArrayList<>(getItemIds());

        int i = 0;
        while (i < level && !currentLevelItemIds.isEmpty()) {
            for (Object itemId : new ArrayList<>(currentLevelItemIds)) {
                setExpanded(itemId);
                currentLevelItemIds.remove(itemId);
                currentLevelItemIds.addAll(getChildren(itemId));
            }
            i++;
        }
    }

    public void setExpanded(Object itemId) {
        setCollapsed(itemId, false);
    }

    public boolean isExpanded(Object itemId) {
        return !isCollapsed(itemId);
    }

    @Override
    public void resetSortOrder() {
        sortContainerPropertyId = null;
        sortAscending = true;

        if (items instanceof TableContainer) {
            ((TableContainer) items).resetSortOrder();
        }
    }

    @Override
    public Iterator<Component> iterator() {
        List<Component> additionalConnectors = null;

        CubaTreeTableState tableState = getState(false);
        if (tableState.presentations != null) {
            additionalConnectors = new LinkedList<>();
            additionalConnectors.add((Component) tableState.presentations);
        }
        if (tableState.contextMenu != null) {
            if (additionalConnectors == null) {
                additionalConnectors = new LinkedList<>();
            }
            additionalConnectors.add((Component) tableState.contextMenu);
        }
        if (tableState.customPopup != null) {
            if (additionalConnectors == null) {
                additionalConnectors = new LinkedList<>();
            }
            additionalConnectors.add((Component) tableState.customPopup);
        }

        if (additionalConnectors == null) {
            return super.iterator();
        } else if (_visibleComponents() != null) {
            return Iterables.concat(_visibleComponents(), additionalConnectors).iterator();
        } else {
            return additionalConnectors.iterator();
        }
    }

    @Override
    public void refreshCellStyles() {
        super.refreshRenderedCells();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        if (editableColumns != null) {
            editableColumns.remove(propertyId);
        }

        if (isAggregatable() && items instanceof AggregationContainer) {
            removeContainerPropertyAggregation(propertyId);
        }

        boolean removed = super.removeContainerProperty(propertyId);

        if (removed) {
            resetPageBuffer();
        }

        return removed;
    }

    @Override
    public boolean isAggregatable() {
        return this.aggregatable;
    }

    @Override
    public void setAggregatable(boolean aggregatable) {
        if (this.aggregatable != aggregatable) {
            this.aggregatable = aggregatable;
            markAsDirty();
        }
    }

    @Override
    public AggregationStyle getAggregationStyle() {
        return aggregationStyle;
    }

    @Override
    public void setAggregationStyle(AggregationStyle aggregationStyle) {
        this.aggregationStyle = aggregationStyle;
    }

    @Override
    public boolean isShowTotalAggregation() {
        return showTotalAggregation;
    }

    @Override
    public void setShowTotalAggregation(boolean showTotalAggregation) {
        if (this.showTotalAggregation != showTotalAggregation) {
            this.showTotalAggregation = showTotalAggregation;
            markAsDirty();
        }
    }

    @Override
    public Collection getAggregationPropertyIds() {
        if (items instanceof AggregationContainer) {
            return ((AggregationContainer) items).getAggregationPropertyIds();
        }
        throw new IllegalStateException("Table container is not AggregationContainer: " + items.getClass());
    }

    @Override
    public Type getContainerPropertyAggregation(Object propertyId) {
        if (items instanceof AggregationContainer) {
            return ((AggregationContainer) items).getContainerPropertyAggregation(propertyId);
        }
        throw new IllegalStateException("Table container is not AggregationContainer: " + items.getClass());
    }

    @Override
    public void addContainerPropertyAggregation(Object propertyId, Type type) {
        if (items instanceof AggregationContainer) {
            ((AggregationContainer) items).addContainerPropertyAggregation(propertyId, type);
        } else {
            throw new IllegalStateException("Table container is not AggregationContainer: " + items.getClass());
        }
    }

    @Override
    public void removeContainerPropertyAggregation(Object propertyId) {
        if (items instanceof AggregationContainer) {
            ((AggregationContainer) items).removeContainerPropertyAggregation(propertyId);
        } else {
            throw new IllegalStateException("Table container is not AggregationContainer: " + items.getClass());
        }
    }

    @Override
    public Map<Object, Object> aggregate(Context context) {
        if (items instanceof AggregationContainer && isAggregatable()) {
            return ((AggregationContainer) items).aggregate(context);
        }
        throw new IllegalStateException("Table container is not AggregationContainer: " + items.getClass());
    }

    @Override
    protected void paintAdditionalData(PaintTarget target) throws PaintException {
        if (reqFirstRowToPaint == -1 && items instanceof AggregationContainer && isAggregatable()
                && !((AggregationContainer) items).getAggregationPropertyIds().isEmpty() && isShowTotalAggregation()
                && AggregationStyle.TOP.equals(getAggregationStyle())) {
            paintAggregationRow(target, ((AggregationContainer) items).aggregate(new Context(items.getItemIds())));
        }

        // paint cuba-ids

        if (getCubaId() != null) {
            ArrayList<String> visibleColOrder = new ArrayList<>();
            for (Object columnId : _visibleColumns()) {
                if (!isColumnCollapsed(columnId)) {
                    visibleColOrder.add(columnId.toString());
                }
            }
            target.addAttribute("colcubaids", visibleColOrder.toArray());
        }
    }

    protected void paintAggregationRow(PaintTarget target, Map<Object, Object> aggregations) throws PaintException {
        target.startTag("arow");
        for (final Object columnId : _visibleColumns()) {
            if (columnId == null || isColumnCollapsed(columnId)) {
                continue;
            }

            if (getCellStyleGenerator() != null) {
                String cellStyle = getCellStyleGenerator().getStyle(this, null, columnId);
                if (cellStyle != null && !cellStyle.equals("")) {
                    target.addAttribute("style-"
                            + _columnIdMap().key(columnId), cellStyle + "-ag");
                }
            }

            String value = (String) aggregations.get(columnId);
            target.addText(value);
        }
        target.endTag("arow");
    }

    @Override
    public void setClickListener(Object propertyId, CellClickListener clickListener) {
        if (cellClickListeners == null) {
            cellClickListeners = new HashMap<>();
        }
        cellClickListeners.put(propertyId, clickListener);
    }

    @Override
    public void removeClickListener(Object propertyId) {
        if (cellClickListeners != null) {
            cellClickListeners.remove(propertyId);
        }
    }

    @Override
    public boolean getColumnSortable(Object columnId) {
        return nonSortableProperties == null || !nonSortableProperties.contains(columnId);
    }

    @Override
    public void setColumnSortable(Object columnId, boolean sortable) {
        if (nonSortableProperties == null) {
            nonSortableProperties = new HashSet<>();
        }
        if (sortable) {
            if (nonSortableProperties.remove(columnId)) {
                markAsDirty();
            }
        } else {
            if (nonSortableProperties.add(columnId)) {
                markAsDirty();
            }
        }
    }

    @Override
    public void requestFocus(Object itemId, Object columnId) {
        if (!getItemIds().contains(itemId)) {
            throw new IllegalArgumentException("Item doesn't exists");
        }

        if (!_visibleColumns().contains(columnId)) {
            throw new IllegalArgumentException("Column doesn't exists or not visible");
        }

        focusColumn = columnId;
        focusItem = itemId;
        setPageLength(-1);
        markAsDirty();
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        Collection<?> ids = new ArrayList<>(super.getSortableContainerPropertyIds());
        if (nonSortableProperties != null) {
            ids.removeAll(nonSortableProperties);
        }
        return ids;
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        updateClickableColumnKeys();
        updateColumnDescriptions();
        updateAggregatableTooltips();

        if (AggregationStyle.BOTTOM.equals(getAggregationStyle())) {
            updateFooterAggregation();
        }

        if (focusColumn != null) {
            setCurrentPageFirstItemId(focusItem);
            getRpcProxy(CubaTableClientRpc.class).requestFocus(itemIdMapper.key(focusItem), _columnIdMap().key(focusColumn));

            focusColumn = null;
            focusItem = null;
        }
    }

    protected void updateAggregatableTooltips() {
        if (aggregationTooltips != null) {
            Map<String, String> aggregationTooltipsByKey = new HashMap<>();
            for (Map.Entry<Object, String> columnEntry : aggregationTooltips.entrySet()) {
                aggregationTooltipsByKey.put(_columnIdMap().key(columnEntry.getKey()), columnEntry.getValue());
            }
            getState().aggregationDescriptions = aggregationTooltipsByKey;
        }
    }

    protected void updateFooterAggregation() {
        if (!isFooterVisible()) {
            setFooterVisible(true);
        }
        Map<Object, Object> aggregations = ((AggregationContainer) items).aggregate(new Context(items.getItemIds()));
        for (final Object columnId : _visibleColumns()) {
            if (columnId == null || isColumnCollapsed(columnId) || !aggregations.containsKey(columnId)) {
                continue;
            }

            String value = (String) aggregations.get(columnId);
            setColumnFooter(columnId, value);
        }
    }

    protected void updateClickableColumnKeys() {
        if (cellClickListeners != null) {
            String[] clickableColumnKeys = new String[cellClickListeners.size()];
            int i = 0;
            for (Object columnId : cellClickListeners.keySet()) {
                clickableColumnKeys[i] = _columnIdMap().key(columnId);
                i++;
            }

            getState().clickableColumnKeys = clickableColumnKeys;
        }
    }

    @Override
    public void showCustomPopup(Component popupComponent) {
        if (getState().customPopup != null) {
            ((AbstractComponent) getState().customPopup).setParent(null);
        }

        getState().customPopup = popupComponent;
        getRpcProxy(CubaTableClientRpc.class).showCustomPopup();

        popupComponent.setParent(this);
    }

    @Override
    public boolean getCustomPopupAutoClose() {
        return getState(false).customPopupAutoClose;
    }

    @Override
    public void setCustomPopupAutoClose(boolean popupAutoClose) {
        if (getState(false).customPopupAutoClose != popupAutoClose) {
            getState().customPopupAutoClose = popupAutoClose;
        }
    }

    @Override
    public void setColumnDescription(Object columnId, String description) {
        if (description != null) {
            if (columnDescriptions == null) {
                columnDescriptions = new HashMap<>();
            }
            if (!Objects.equals(columnDescriptions.get(columnId), description)) {
                markAsDirty();
            }
            columnDescriptions.put(columnId, description);
        } else if (columnDescriptions != null) {
            if (columnDescriptions.remove(columnId) != null) {
                markAsDirty();
            }
        }
    }

    @Override
    public String getAggregationDescription(Object columnId) {
        if (aggregationTooltips != null) {
            return aggregationTooltips.get(columnId);
        }
        return null;
    }

    @Override
    public void setAggregationDescription(Object columnId, String tooltip) {
        if (tooltip != null) {
            if (aggregationTooltips == null) {
                aggregationTooltips = new HashMap<>();
            }
            if (!Objects.equals(aggregationTooltips.get(columnId), tooltip)) {
                markAsDirty();
            }
            aggregationTooltips.put(columnId, tooltip);
        } else if (aggregationTooltips != null) {
            if (aggregationTooltips.containsKey(columnId)) {
                markAsDirty();
            }
            aggregationTooltips.remove(columnId);
        }
    }

    @Override
    public String getColumnDescription(Object columnId) {
        if (columnDescriptions != null) {
            return columnDescriptions.get(columnId);
        }
        return null;
    }

    protected void updateColumnDescriptions() {
        if (columnDescriptions != null) {
            Map<String, String> columnDescriptionsByKey = new HashMap<>();
            for (Map.Entry<Object, String> columnEntry : columnDescriptions.entrySet()) {
                columnDescriptionsByKey.put(_columnIdMap().key(columnEntry.getKey()), columnEntry.getValue());
            }
            getState().columnDescriptions = columnDescriptionsByKey;
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (height < 0) {
            if (getCacheRate() != 2) {
                setCacheRate(2);
            }
            if (getPageLength() != 15) {
                setPageLength(15);
            }
        }
    }

    @Override
    protected Container createOrderedWrapper(Container newDataSource) {
        ContainerOrderedWrapper wrapper = new ContainerOrderedWrapper(newDataSource);
        wrapper.setResetOnItemSetChange(true);
        return wrapper;
    }

    @Override
    public void setBeforePaintListener(Runnable beforePaintListener) {
        this.beforePaintListener = beforePaintListener;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (beforePaintListener != null) {
            beforePaintListener.run();
        }

        super.paintContent(target);
    }

    public void expandAllHierarchical(List<Object> collapsedItemIds, List<Object> preOrder, List<Object> openItems) {
        if (getContainerStrategy() instanceof HierarchicalStrategy) {
            HierarchicalStrategy hierarchicalStrategy = (HierarchicalStrategy) getContainerStrategy();
            hierarchicalStrategy.setOpenItems(openItems);
            hierarchicalStrategy.setPreOrder(preOrder);

            for (Object itemId : collapsedItemIds) {
                fireExpandEvent(itemId);
            }

            setCurrentPageFirstItemIndex(getCurrentPageFirstItemIndex(), false);

            refreshRowCache();
        } else {
            expandAll();
        }
    }

    public void collapseAllHierarchical() {
        if (getContainerStrategy() instanceof HierarchicalStrategy) {
            HierarchicalStrategy hierarchicalStrategy = (HierarchicalStrategy) getContainerStrategy();
            Set<Object> openItems = hierarchicalStrategy.getOpenItems();

            hierarchicalStrategy.setOpenItems(Collections.emptyList());
            hierarchicalStrategy.setPreOrder(null);
            // trigger preorder update
            hierarchicalStrategy.getItemIds();

            for (Object itemId : openItems) {
                fireCollapseEvent(itemId);
            }

            setCurrentPageFirstItemIndex(getCurrentPageFirstItemIndex(), false);

            refreshRowCache();
        } else {
            collapseAll();
        }
    }
}