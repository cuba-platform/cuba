/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.data.util.AggregationContainerOrderedWrapper;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.gwt.client.ui.IScrollTable;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import java.io.Serializable;
import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
@SuppressWarnings("serial")
@ClientWidget(IScrollTable.class)
public class Table extends com.vaadin.ui.Table implements AggregationContainer {

    protected LinkedList<Object> editableColumns = null;
    protected boolean storeColWidth = false;
    protected LinkedList<ShortcutListener> shortcutListeners = new LinkedList<>();

    protected PagingMode pagingMode = PagingMode.SCROLLING;

    protected int currentPage = 1;
    protected int pagesCount = -1;

    protected PagingProvider pagingProvider = null;

    protected boolean aggregatable = false;

    protected boolean enableCancelSorting = true;

    private boolean textSelectionEnabled;

    private boolean showTotalAggregation = true;

    protected ActionManager shortcutsManager = new ActionManager();

    private List<CollapseListener> columnCollapseListeners = new ArrayList<>();

    public interface CollapseListener {
        void columnCollapsed(Object columnId, boolean collapsed);
    }

    public enum PagingMode {
        PAGE,
        SCROLLING
    }

    public Table() {
    }

    public Table(String caption) {
        super(caption);
    }

    public Table(String caption, Container dataSource) {
        super(caption, dataSource);
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        if (newDataSource != null
                && !Container.Ordered.class.isInstance(newDataSource)
                && AggregationContainer.class.isInstance(newDataSource)) {
            newDataSource = new AggregationContainerOrderedWrapper(newDataSource);
        }

        super.setContainerDataSource(newDataSource);
    }

    public Object[] getEditableColumns() {
        if (editableColumns == null) {
            return null;
        }
        return editableColumns.toArray();
    }

    public void setEditableColumns(Object[] editableColumns) {
        if (editableColumns == null) {
            throw new NullPointerException("You cannot set null as editable columns");
        }

        if (this.editableColumns == null) {
            this.editableColumns = new LinkedList<Object>();
        } else {
            this.editableColumns.clear();
        }

        final Collection properties = getContainerPropertyIds();
        for (final Object editableColumn : editableColumns) {
            if (editableColumn == null) {
                throw new NullPointerException("Ids must be non-nulls");
            } else if (!properties.contains(editableColumn)
                    || columnGenerators.containsKey(editableColumn)) {
                throw new IllegalArgumentException(
                        "Ids must exist in the Container and it must be not a generated column, incorrect id: "
                                + editableColumn);
            }
            this.editableColumns.add(editableColumn);
        }

        resetPageBuffer();
        refreshRenderedCells();
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        if (!isSelectable() && variables.containsKey("selected")) {
            // Not-selectable is a special case, AbstractSelect does not support
            // TODO could be optimized.
            variables = new HashMap<>(variables);
            variables.remove("selected");
        }

        /*
         * The AbstractSelect cannot handle the multiselection properly, instead
         * we handle it ourself
         */
        else if (isSelectable() && isMultiSelect()
                && variables.containsKey("selected")
                /*&& multiSelectMode == MultiSelectMode.DEFAULT*/) {
            handleSelectedItems(variables);
            variables = new HashMap<>(variables);
            variables.remove("selected");
        }
        super.changeVariables(source, variables);
        if (variables.containsKey("enterPressed")) {
            fireShortcutListeners();
        }

        // Actions
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }

    private void fireShortcutListeners() {
        for (ShortcutListener listener : shortcutListeners) {
            listener.handleAction(Table.this, null);
        }
    }

    /**
     * Gets items ids from a range of key values
     */
    protected Set<Object> getItemIdsInRange(Object itemId, final int length) {
        HashSet<Object> ids = new HashSet<>();
        for (int i = 0; i < length; i++) {
            assert itemId != null; // should not be null unless client-server
                                   // are out of sync
            ids.add(itemId);
            itemId = nextItemId(itemId);
        }
        return ids;
    }

    /**
     * Handles selection if selection is a multiselection
     *
     * @param variables
     *            The variables
     */
    private void handleSelectedItems(Map<String, Object> variables) {
        final String[] ka = (String[]) variables.get("selected");
        final String[] ranges = (String[]) variables.get("selectedRanges");

        Set<Object> renderedItemIds = getCurrentlyRenderedItemIds();

        HashSet<Object> newValue = new LinkedHashSet<>(
                (Collection<Object>) getValue());

        if (variables.containsKey("clearSelections")) {
            // the client side has instructed to swipe all previous selections
            newValue.clear();
        } else {
            /*
             * first clear all selections that are currently rendered rows (the
             * ones that the client side counterpart is aware of)
             */
            newValue.removeAll(renderedItemIds);
        }

        /*
         * Then add (possibly some of them back) rows that are currently
         * selected on the client side (the ones that the client side is aware
         * of).
         */
        for (int i = 0; i < ka.length; i++) {
            // key to id
            final Object id = itemIdMapper.get(ka[i]);
            if (!isNullSelectionAllowed()
                    && (id == null || id == getNullSelectionItemId())) {
                // skip empty selection if nullselection is not allowed
                requestRepaint();
            } else if (id != null && containsId(id)) {
                newValue.add(id);
            }
        }

        if (!isNullSelectionAllowed() && newValue.size() < 1) {
            // empty selection not allowed, keep old value
            requestRepaint();
            return;
        }

        /* Add range items aka shift clicked multiselection areas */
        if (ranges != null) {
            for (String range : ranges) {
                String[] split = range.split("-");
                Object startItemId = itemIdMapper.get(split[0]);
                int length = Integer.valueOf(split[1]);
                newValue.addAll(getItemIdsInRange(startItemId, length));
            }
        }

        setValue(newValue, true);

    }

    private Set<Object> getCurrentlyRenderedItemIds() {
        HashSet<Object> ids = new HashSet<>();
        if (pageBuffer != null) {
            for (int i = 0; i < pageBuffer[CELL_ITEMID].length; i++) {
                ids.add(pageBuffer[CELL_ITEMID][i]);
            }
        }
        return ids;
    }

    @Override
    protected boolean changeVariables(Map<String, Object> variables) {

        boolean clientNeedsContentRefresh = false;

        if (variables.containsKey("colwidth")) {
            try {
                final ColumnWidth colWidth = ColumnWidth.deSerialize((String) variables.get("colwidth"));
                final Object id = columnIdMap.get(colWidth.getColId());
                setColumnWidth(id, colWidth.getWidth());
            } catch (Exception e) {
                //ignore
            }
        }

        if (variables.containsKey("curpage")) {
            currentPage = ((Integer) variables.get("curpage")).intValue();
            clientNeedsContentRefresh = true;
        }

        if (variables.containsKey("pagelength")) {
            setPageLength(((Integer) variables.get("pagelength")).intValue());
            clientNeedsContentRefresh = true;
        }

        return clientNeedsContentRefresh;
    }

    @Override
    public void addGeneratedColumn(Object id, ColumnGenerator generatedColumn) {
        if (generatedColumn == null) {
            throw new IllegalArgumentException(
                    "Can not add null as a GeneratedColumn");
        }
        if (columnGenerators.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Can not add the same GeneratedColumn twice, id:" + id);
        } else {
            columnGenerators.put(id, generatedColumn);
            /*
             * add to visible column list unless already there (overriding
             * column from DS)
             */
            if (!visibleColumns.contains(id)) {
                visibleColumns.add(id);
            }

            if (editableColumns != null) {
                editableColumns.remove(id);
            }

            resetPageBuffer();
            refreshRenderedCells();
        }
    }

    @Override
    protected Object getPropertyValue(Object rowId, Object colId, Property property) {
        if (isColumnEditable(colId) && fieldFactory != null) {
            final Field f = fieldFactory.createField(getContainerDataSource(),
                    rowId, colId, this);
            if (f != null) {
                f.setPropertyDataSource(property);
                return f;
            }
        }

        return formatPropertyValue(rowId, colId, property);
    }

    protected boolean isColumnEditable(Object columnId) {
        return isEditable() &&
                editableColumns != null && editableColumns.contains(columnId);
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        if (editableColumns != null) {
            editableColumns.remove(propertyId);
        }
        if (isAggregatable() && items instanceof AggregationContainer) {
            removeContainerPropertyAggregation(propertyId);
        }

        boolean removed = super.removeContainerProperty(propertyId);

        if (removed)
            this.resetPageBuffer();

        return removed;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        // The tab ordering number
        if (getTabIndex() > 0) {
            target.addAttribute("tabindex", getTabIndex());
        }

        if (getDragMode() != TableDragMode.NONE) {
            target.addAttribute("dragmode", getDragMode().ordinal());
        }

        // Initialize temps
        final Object[] colids = getVisibleColumns();
        final int cols = colids.length;
        final int first = getCurrentPageFirstItemIndex();
        int total = size();
        final int pagelen = getPageLength();
        final int colHeadMode = getColumnHeaderMode();
        final boolean colheads = colHeadMode != COLUMN_HEADER_MODE_HIDDEN;
        final boolean rowheads = getRowHeaderMode() != ROW_HEADER_MODE_HIDDEN;
        final Object[][] cells = getVisibleCells();
        int rows;
        if (reqRowsToPaint >= 0) {
            rows = reqRowsToPaint;
        } else {
            rows = cells[0].length;
            if (alwaysRecalculateColumnWidths) {
                // TODO experimental feature for now: tell the client to
                // recalculate column widths.
                // We'll only do this for paints that do not originate from
                // table scroll/cache requests (i.e when reqRowsToPaint<0)
                target.addAttribute("recalcWidths", true);
            }
        }

        if (!isNullSelectionAllowed() && getNullSelectionItemId() != null
                && containsId(getNullSelectionItemId())) {
            total--;
            rows--;
        }

        // selection support
        LinkedList<String> selectedKeys = new LinkedList<>();
        if (isMultiSelect()) {
            // only paint selections that are currently visible in the client
            HashSet sel = new HashSet((Set) getValue());
            Collection vids = getVisibleItemIds();
            for (Iterator it = vids.iterator(); it.hasNext();) {
                Object id = it.next();
                if (sel.contains(id)) {
                    selectedKeys.add(itemIdMapper.key(id));
                }
            }
        } else {
            Object value = getValue();
            if (value == null) {
                value = getNullSelectionItemId();
            }
            if (value != null) {
                selectedKeys.add(itemIdMapper.key(value));
            }
        }

        // Table attributes
        if (isSelectable()) {
            target.addAttribute("selectmode", (isMultiSelect() ? "multi"
                    : "single"));
        } else {
            target.addAttribute("selectmode", "none");
        }

        if (clickListenerCount > 0) {
            target.addAttribute("listenClicks", true);
        }

        target.addAttribute("cols", cols);
        target.addAttribute("rows", rows);

        target.addAttribute("firstrow",
                (reqFirstRowToPaint >= 0 ? reqFirstRowToPaint
                        : firstToBeRenderedInClient));
        target.addAttribute("totalrows", total);
        if (pagelen > 0 && !allowMultiStringCells) {
            target.addAttribute("pagelength", pagelen);
        }
        if (colheads) {
            target.addAttribute("colheaders", true);
        }
        if (rowheads) {
            target.addAttribute("rowheaders", true);
        }
        if (allowMultiStringCells) {
            target.addAttribute("multistring", true);
        }

        if (isSelectable() && !isMultiSelect() && !isNullSelectionAllowed()) {
            target.addAttribute("nullSelectionDisallowed", true);
        }

        if (isStoreColWidth()) {
            target.addAttribute("storeColWidth", true);
        }

        if (isTextSelectionEnabled()) {
            target.addAttribute("textSelection", true);
        }

        if (pagingMode == PagingMode.PAGE) {
            target.addAttribute("pagescount", pagesCount);
            target.addAttribute("curpage", currentPage);
            paintPaging(target);
        }
        target.addAttribute("pagingMode", pagingMode.name());

        // Visible column order
        final ArrayList visibleColOrder = new ArrayList();
        for (final Iterator it = visibleColumns.iterator(); it.hasNext();) {
            final Object columnId = it.next();
            if (!isColumnCollapsed(columnId)) {
                visibleColOrder.add(columnIdMap.key(columnId));
            }
        }
        target.addAttribute("vcolorder", visibleColOrder.toArray());

        if (reqFirstRowToPaint == -1 && items instanceof AggregationContainer && isAggregatable()
                && !((AggregationContainer) items).getAggregationPropertyIds().isEmpty() && isShowTotalAggregation()) {
            paintAggregationRow(target, ((AggregationContainer) items).aggregate(new Context(items.getItemIds())));
        }

        // Rows
        final Set<Action> actionSet = new LinkedHashSet<>();
        final boolean selectable = isSelectable();
        final boolean[] iscomponent = new boolean[visibleColumns.size()];
        int iscomponentIndex = 0;
        for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                && iscomponentIndex < iscomponent.length;) {
            final Object columnId = it.next();
            if (columnGenerators.containsKey(columnId)) {
                iscomponent[iscomponentIndex++] = true;
            } else {
                final Class colType = getType(columnId);
                iscomponent[iscomponentIndex++] = colType != null
                        && Component.class.isAssignableFrom(colType);
            }
        }
        target.startTag("rows");
        // cells array contains all that are supposed to be visible on client,
        // but we'll start from the one requested by client
        int start = 0;
        if (reqFirstRowToPaint != -1 && firstToBeRenderedInClient != -1) {
            start = reqFirstRowToPaint - firstToBeRenderedInClient;
        }
        int end = cells[0].length;
        if (reqRowsToPaint != -1) {
            end = start + reqRowsToPaint;
        }
        // sanity check
        if (lastToBeRenderedInClient != -1 && lastToBeRenderedInClient < end) {
            end = lastToBeRenderedInClient + 1;
        }
        if (start > cells[CELL_ITEMID].length || start < 0) {
            start = 0;
        }
        // trying to fix ArrayIndexOutOfBoundsException
        if (end > cells[CELL_ITEMID].length) {
            end = cells[CELL_ITEMID].length;
        }

        for (int i = start; i < end; i++) {
            final Object itemId = cells[CELL_ITEMID][i];

            if (!isNullSelectionAllowed() && getNullSelectionItemId() != null
                    && itemId == getNullSelectionItemId()) {
                // Remove null selection item if null selection is not allowed
                continue;
            }

            target.startTag("tr");

            // tr attributes
            if (rowheads) {
                if (cells[CELL_ICON][i] != null) {
                    target.addAttribute("icon", (Resource) cells[CELL_ICON][i]);
                }
                if (cells[CELL_HEADER][i] != null) {
                    target.addAttribute("caption",
                            (String) cells[CELL_HEADER][i]);
                }
            }
            target.addAttribute("key", Integer.parseInt(cells[CELL_KEY][i]
                    .toString()));
            //TODO GP http://dev.vaadin.com/ticket/3520
//            if (actionHandlers != null || isSelectable()) {
                if (isSelected(itemId)) {
                    target.addAttribute("selected", true);
                }
//            }

            // Actions
            paintRowActions(target, actionSet, itemId);

            paintCellStyleGenerator(target, itemId);

            // cells
            int currentColumn = 0;
            for (final Iterator it = visibleColumns.iterator(); it.hasNext(); currentColumn++) {
                final Object columnId = it.next();
                paintCell(target, itemId, columnId, cells[CELL_FIRSTCOL + currentColumn][i],
                        iscomponent[currentColumn]);
            }

            target.endTag("tr");
        }
        target.endTag("rows");

        // The select variable is only enabled if selectable
        if (selectable && selectedKeys.size() > 0) {
            target.addVariable(this, "selected", (String[]) selectedKeys
                    .toArray(new String[selectedKeys.size()]));
        }

        // The cursors are only shown on pageable table
        if (first != 0 || getPageLength() > 0) {
            target.addVariable(this, "firstvisible", first);
        }

        // Sorting
        if (getContainerDataSource() instanceof Container.Sortable) {
            target.addVariable(this, "sortcolumn", columnIdMap
                    .key(getSortContainerPropertyId()));
            target.addVariable(this, "sortascending", isSortAscending());
        }

        if (isEnableCancelSorting()){
            target.addVariable(this, "enableCancelSorting", true);
        }

        // Resets and paints "to be painted next" variables. Also reset
        // pageBuffer
        reqFirstRowToPaint = -1;
        reqRowsToPaint = -1;
        containerChangeToBeRendered = false;
        target.addVariable(this, "reqrows", reqRowsToPaint);
        target.addVariable(this, "reqfirstrow", reqFirstRowToPaint);

        // Actions
        paintActions(target, actionSet);

        if (isColumnReorderingAllowed()) {
            final String[] colorder = new String[visibleColumns.size()];
            int i = 0;
            for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                    && i < colorder.length;) {
                colorder[i++] = columnIdMap.key(it.next());
            }
            target.addVariable(this, "columnorder", colorder);
        }

        paintCollapsedColumns(target);

        paintVisibleColumns(target, colheads);

        if (getDropHandler() != null) {
            getDropHandler().getAcceptCriterion().paint(target);
        }
    }

    protected void paintActions(PaintTarget target, Set<Action> actionSet) throws PaintException {
        if (!actionSet.isEmpty()) {
            target.addVariable(this, "action", "");
            target.startTag("actions");

            for (final Action a : actionSet) {
                target.startTag("action");
                if (a.getCaption() != null) {
                    target.addAttribute("caption", a.getCaption());
                }
                if (a.getIcon() != null) {
                    target.addAttribute("icon", a.getIcon());
                }
                target.addAttribute("key", actionMapper.key(a));
                target.endTag("action");
            }

            target.endTag("actions");
        }
        // paint shortcut actions
        shortcutsManager.paintActions(null, target);
    }

    protected void paintCollapsedColumns(PaintTarget target) throws PaintException {
        if (isColumnCollapsingAllowed()) {
            String[] collapsedkeys;
            if (collapsedColumns == null || collapsedColumns.isEmpty()) {
                collapsedkeys = new String[0];
            } else {
                collapsedkeys = new String[collapsedColumns.size()];

                int nextColumn = 0;
                for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                        && nextColumn < collapsedkeys.length;) {
                    final Object columnId = it.next();
                    if (isColumnCollapsed(columnId)) {
                        collapsedkeys[nextColumn++] = columnIdMap.key(columnId);
                    }
                }
            }
            target.addVariable(this, "collapsedcolumns", collapsedkeys);
        }
    }

    protected void paintVisibleColumns(PaintTarget target, boolean colheads)
            throws PaintException {

        final Collection sortables = getSortableContainerPropertyIds();

        target.startTag("visiblecolumns");
        for (final Object columnId : visibleColumns) {
            if (columnId != null) {
                target.startTag("column");
                target.addAttribute("cid", columnIdMap.key(columnId));
                final String head = getColumnHeader(columnId);
                target.addAttribute("caption", (head != null ? head : ""));
                if (isColumnCollapsed(columnId)) {
                    target.addAttribute("collapsed", true);
                }
                if (colheads) {
                    if (getColumnIcon(columnId) != null) {
                        target.addAttribute("icon", getColumnIcon(columnId));
                    }
                    if (sortables.contains(columnId)) {
                        target.addAttribute("sortable", true);
                    }
                }
                if (!ALIGN_LEFT.equals(getColumnAlignment(columnId))) {
                    target.addAttribute("align", getColumnAlignment(columnId));
                }
                if (getColumnWidth(columnId) > -1) {
                    target.addAttribute("width", String
                            .valueOf(getColumnWidth(columnId)));
                }

                target.endTag("column");
            }
        }
        target.endTag("visiblecolumns");
    }

    protected void paintRowActions(PaintTarget target, Set actionSet, Object itemId) {
        if (actionHandlers != null) {
            final ArrayList keys = new ArrayList();
            for (Action.Handler actionHandler : actionHandlers) {
                final Action[] aa = (actionHandler).getActions(itemId, this);
                if (aa != null) {
                    for (Action anAa : aa) {
                        final String key = actionMapper.key(anAa);
                        actionSet.add(anAa);
                        keys.add(key);
                    }
                }
            }
            target.addAttribute("al", keys.toArray());
        }
    }

    protected void paintCellStyleGenerator(PaintTarget target, Object itemId) throws PaintException {
        /*
        * For each row, if a cellStyleGenerator is specified, get the
    * specific style for the cell, using null as propertyId. If there
    * is any, add it to the target.
    */
        if (cellStyleGenerator != null) {
            String rowStyle = cellStyleGenerator.getStyle(itemId, null);
            if (rowStyle != null && !rowStyle.equals("")) {
                target.addAttribute("rowstyle", rowStyle);
            }
        }
    }

    protected void paintCell(PaintTarget target, Object itemId, Object columnId,
                             Object value, boolean component
    ) throws PaintException {
        if (columnId == null || isColumnCollapsed(columnId)) {
            return;
        }
        /*
        * For each cell, if a cellStyleGenerator is specified, get the
        * specific style for the cell. If there is any, add it to the
        * target.
        */
        if (cellStyleGenerator != null) {
            String cellStyle = cellStyleGenerator.getStyle(itemId,
                    columnId);
            if (cellStyle != null && !cellStyle.equals("")) {
                target.addAttribute("style-"
                        + columnIdMap.key(columnId), cellStyle);
            }
        }
        if ((component || isColumnEditable(columnId))
                && Component.class.isInstance(value)) {
            final Component c = (Component) value;
            if (c == null) {
                target.addText("");
            } else {
                c.requestRepaint();
                c.paint(target);
            }
        } else {
            target.addText((String) value);
        }
    }

    protected void paintAggregationRow(PaintTarget target, Map<Object, Object> aggregations) throws PaintException {
        target.startTag("arow");
        for (final Object columnId : visibleColumns) {
            if (columnId == null || isColumnCollapsed(columnId)) {
                continue;
            }

            if (cellStyleGenerator != null) {
                String cellStyle = cellStyleGenerator.getStyle(null, columnId);
                if (cellStyle != null && !cellStyle.equals("")) {
                    target.addAttribute("style-"
                            + columnIdMap.key(columnId), cellStyle + "-ag");
                }
            }

            Object value = aggregations.get(columnId);
            if (Component.class.isInstance(value)) {
                Component c = (Component) value;
                if (c == null) {
                    target.addText("");
                } else {
                    c.paint(target);
                }
            } else {
                target.addText((String) value);
            }
        }
        target.endTag("arow");
    }

    @Override
    public Object addItem(Object[] cells, Object itemId) throws UnsupportedOperationException {
        itemId = super.addItem(cells, itemId);

        resetPageBuffer();
        refreshRenderedCells();

        return itemId;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (event.getProperty().isReadOnly()) {
            super.valueChange(event);
        }
    }

    @Override
    protected void refreshRenderedCells() {
        if (getParent() == null) {
            return;
        }

        if (isContentRefreshesEnabled) {

            HashSet oldListenedProperties = listenedProperties;
            HashSet oldVisibleComponents = visibleComponents;

            // initialize the listener collections
            listenedProperties = new HashSet<>();
            visibleComponents = new HashSet<>();

            // Collects the basic facts about the table page
            final Object[] colids = getVisibleColumns();
            final int cols = colids.length;
            int rows, totalRows;
            rows = totalRows = size();
            int pagelen;
            if (allowMultiStringCells) {
                pagelen = totalRows;
            } else {
                pagelen = getPageLength();
            }

            int firstIndex = pagingMode == PagingMode.PAGE
                    ? currentPageFirstItemIndex() : getCurrentPageFirstItemIndex();
            if (rows > 0 && firstIndex >= 0) {
                rows -= firstIndex;
            }

            if (pagelen > 0 && pagelen < rows) {
                rows = pagelen;
            }

            if (pagingMode == PagingMode.SCROLLING) {
                // If "to be painted next" variables are set, use them
                if (lastToBeRenderedInClient - firstToBeRenderedInClient > 0) {
                    rows = lastToBeRenderedInClient - firstToBeRenderedInClient + 1;
                }
                if (firstToBeRenderedInClient >= 0) {
                    if (firstToBeRenderedInClient < totalRows) {
                        firstIndex = firstToBeRenderedInClient;
                    } else {
                        firstIndex = totalRows - 1;
                    }
                } else {
                    // initial load
                    firstToBeRenderedInClient = firstIndex;
                }
                if (totalRows > 0) {
                    if (rows + firstIndex > totalRows) {
                        rows = totalRows - firstIndex;
                    }
                } else {
                    rows = 0;
                }
            }

            Object[][] cells = new Object[cols + CELL_FIRSTCOL][rows];
            if (rows == 0) {
                pageBuffer = cells;
                unregisterPropertiesAndComponents(oldListenedProperties,
                        oldVisibleComponents);
                return;
            }

            if (pagingMode == PagingMode.PAGE) {
                pagesCount = totalRows % pagelen == 0
                        ? totalRows / pagelen
                        : totalRows / pagelen + 1;
                if (currentPage > pagesCount) currentPage = pagesCount;
            }

            Object id;
            // Gets the first item id
            if (items instanceof Container.Indexed) {
                id = ((Container.Indexed) items).getIdByIndex(firstIndex);
            } else {
                id = ((Container.Ordered) items).firstItemId();
                for (int i = 0; i < firstIndex; i++) {
                    id = ((Container.Ordered) items).nextItemId(id);
                }
            }

            final int headmode = getRowHeaderMode();
            final boolean[] iscomponent = new boolean[cols];
            for (int i = 0; i < cols; i++) {
                iscomponent[i] = columnGenerators.containsKey(colids[i])
                        || Component.class.isAssignableFrom(getType(colids[i]));
            }
            int firstIndexNotInCache;
            if (pageBuffer != null && pageBuffer[CELL_ITEMID].length > 0) {
                firstIndexNotInCache = pageBufferFirstIndex
                        + pageBuffer[CELL_ITEMID].length;
            } else {
                firstIndexNotInCache = -1;
            }

            // Creates the page contents
            int filledRows = 0;
            for (int i = 0; i < rows && id != null; i++) {
                cells[CELL_ITEMID][i] = id;
                cells[CELL_KEY][i] = itemIdMapper.key(id);
                if (headmode != ROW_HEADER_MODE_HIDDEN) {
                    switch (headmode) {
                    case ROW_HEADER_MODE_INDEX:
                        cells[CELL_HEADER][i] = String.valueOf(i + firstIndex
                                + 1);
                        break;
                    default:
                        cells[CELL_HEADER][i] = getItemCaption(id);
                    }
                    cells[CELL_ICON][i] = getItemIcon(id);
                }

                if (cols > 0) {
                    for (int j = 0; j < cols; j++) {
                        if (isColumnCollapsed(colids[j])) {
                            continue;
                        }
                        Property p = null;
                        Object value = "";
                        boolean isGenerated = columnGenerators
                                .containsKey(colids[j]);

                        if (!isGenerated) {
                            p = getContainerProperty(id, colids[j]);
                        }

                        // check in current pageBuffer already has row
                        int index = firstIndex + i;
                        if (p != null || isGenerated) {
                            if (p instanceof Property.ValueChangeNotifier) {
                                if (oldListenedProperties == null
                                        || !oldListenedProperties.contains(p)) {
                                    ((Property.ValueChangeNotifier) p)
                                            .addListener(this);
                                }
                                listenedProperties.add(p);
                            }
                            if (index < firstIndexNotInCache
                                    && index >= pageBufferFirstIndex) {
                                // we have data already in our cache,
                                // recycle it instead of fetching it via
                                // getValue/getPropertyValue
                                int indexInOldBuffer = index
                                        - pageBufferFirstIndex;
                                value = pageBuffer[CELL_FIRSTCOL + j][indexInOldBuffer];
                            } else {
                                if (isGenerated) {
                                    ColumnGenerator cg = columnGenerators.get(colids[j]);
                                    value = cg.generateCell(this, id, colids[j]);
                                } else if (iscomponent[j]) {
                                    value = p.getValue();
                                } else if (p != null) {
                                    value = getPropertyValue(id, colids[j], p);
                                } else {
                                    value = getPropertyValue(id, colids[j],
                                            null);
                                }
                            }
                        }

                        if (value instanceof Component) {
                            if (oldVisibleComponents == null
                                    || !oldVisibleComponents.contains(value)) {
                                ((Component) value).setParent(this);
                            }
                            visibleComponents.add((Component) value);
                        }
                        cells[CELL_FIRSTCOL + j][i] = value;
                    }
                }

                id = ((Container.Ordered) items).nextItemId(id);

                filledRows++;
            }

            // Assures that all the rows of the cell-buffer are valid
            if (filledRows != cells[0].length) {
                final Object[][] temp = new Object[cells.length][filledRows];
                for (int i = 0; i < cells.length; i++) {
                    for (int j = 0; j < filledRows; j++) {
                        temp[i][j] = cells[i][j];
                    }
                }
                cells = temp;
            }

            pageBufferFirstIndex = firstIndex;

            // Saves the results to internal buffer
            pageBuffer = cells;

            unregisterPropertiesAndComponents(oldListenedProperties,
                    oldVisibleComponents);

            requestRepaint();
        }
    }

    protected void paintPaging(PaintTarget target) throws PaintException {
        if (pagingProvider != null) {
            target.startTag("paging");
            if (pagingProvider.firstCaption() != null) {
                target.addAttribute("fc", pagingProvider.firstCaption());
            }
            if (pagingProvider.prevCaption() != null) {
                target.addAttribute("pc", pagingProvider.prevCaption());
            }
            if (pagingProvider.nextCaption() != null) {
                target.addAttribute("nc", pagingProvider.nextCaption());
            }
            if (pagingProvider.lastCaption() != null) {
                target.addAttribute("lc", pagingProvider.lastCaption());
            }
            if (pagingProvider.showPageLengthSelector() && pagingProvider.pageLengths() != null) {
                final int[] arr = pagingProvider.pageLengths();
                Arrays.sort(arr);
                Integer[] lengths = new Integer[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    lengths[i] = arr[i];
                }
                target.addAttribute("lengths", lengths);

                target.addAttribute("sc", pagingProvider.pageLengthSelectorCaption());
            }
            target.endTag("paging");
        }
    }

    @Override
    public void containerItemSetChange(Container.ItemSetChangeEvent event) {
        super.containerItemSetChange(event);

        Object value = getValue();
        if (value != null) {
            if (isMultiSelect()) {
                if (Collection.class.isAssignableFrom(value.getClass())) {
                    Set<Object> newValue = new HashSet<Object>();
                    Set<Object> oldValue = new HashSet<Object>((Collection) value);
                    for (final Object v : oldValue) {
                        if (items.containsId(v)) {
                            newValue.add(v);
                        }
                    }
                    if (oldValue.isEmpty() && newValue.isEmpty())
                        return;
                    setValue(newValue);
                }
            } else {
                if (!items.containsId(value)) {
                    setValue(null);
                }
            }
        }
    }

    @Override
    public Map<Object, Object> aggregate(Context context) {
        if (items instanceof AggregationContainer && isAggregatable()) {
            return ((AggregationContainer) items).aggregate(context);
        }
        throw new IllegalStateException("Table container is not AggregationContainer: " + items.getClass());
    }

    public Map<Object, Object> aggregate() {
        return aggregate(new Context(getItemIds()));
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
    public void addShortcutListener(ShortcutListener listener) {
        if (listener.getKeyCode() != 13 || !(listener.getModifiers() == null || listener.getModifiers().length > 0)) {
            shortcutsManager.addAction(listener);
        } else
            shortcutListeners.add(listener);
    }

    @Override
    public void removeShortcutListener(ShortcutListener listener){
        shortcutListeners.remove(listener);
        shortcutsManager.removeAction(listener);
    }

    public boolean isAggregatable() {
        return aggregatable;
    }

    public void setAggregatable(boolean aggregatable) {
        if (this.aggregatable != aggregatable) {
            this.aggregatable = aggregatable;
            requestRepaint();
        }
    }

    public boolean isEnableCancelSorting() {
        return enableCancelSorting;
    }

    public void setEnableCancelSorting(boolean enableCancelSorting) {
        this.enableCancelSorting = enableCancelSorting;
    }

    public boolean isStoreColWidth() {
        return storeColWidth;
    }

    public void setStoreColWidth(boolean storeColWidth) {
        this.storeColWidth = storeColWidth;
    }

    protected int currentPageFirstItemIndex() {
        return (currentPage - 1) * getPageLength();
    }

    public PagingMode getPagingMode() {
        return pagingMode;
    }

    public void setPagingMode(PagingMode pagingMode) {
        this.pagingMode = pagingMode;
        requestRepaint();
    }

    public PagingProvider getPagingProvider() {
        return pagingProvider;
    }

    public void setPagingProvider(PagingProvider pagingProvider) {
        this.pagingProvider = pagingProvider;
        requestRepaint();
    }

    public boolean isTextSelectionEnabled() {
        return textSelectionEnabled;
    }

    public void setTextSelectionEnabled(boolean textSelectionEnabled) {
        this.textSelectionEnabled = textSelectionEnabled;
        requestRepaint();
    }

    public boolean isShowTotalAggregation() {
        return showTotalAggregation;
    }

    public void setShowTotalAggregation(boolean showTotalAggregation) {
        this.showTotalAggregation = showTotalAggregation;
        requestRepaint();
    }

    public void addColumnCollapseListener(CollapseListener listener) {
        columnCollapseListeners.add(listener);
    }

    public void removeColumnCollapseListener(CollapseListener listener) {
        columnCollapseListeners.remove(listener);
    }

    @Override
    public void setColumnCollapsed(Object propertyId, boolean collapsed) throws IllegalStateException {
        if (collapsedColumns.contains(propertyId) != collapsed) {
            for (CollapseListener listener : columnCollapseListeners) {
                listener.columnCollapsed(propertyId, collapsed);
            }
        }
        super.setColumnCollapsed(propertyId, collapsed);
    }

    public interface PagingProvider extends Serializable {
        String firstCaption();
        String prevCaption();
        String nextCaption();
        String lastCaption();

        String pageLengthSelectorCaption();
        boolean showPageLengthSelector();
        int[] pageLengths();
    }
}
