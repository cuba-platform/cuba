/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.grouptable;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.aggregation.TableAggregationRow;
import com.haulmont.cuba.web.toolkit.ui.client.table.CubaScrollTableWidget;
import com.vaadin.client.*;
import com.vaadin.client.ui.VScrollTable;
import com.vaadin.shared.ui.table.TableConstants;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaGroupTableWidget extends CubaScrollTableWidget {

    public static final String CLASSNAME = "cuba-grouptable";
    public static final String GROUP_DIVIDER_COLUMN_KEY = "-1";

    protected Set<String> groupColumns;

    public CubaGroupTableWidget() {
        addStyleName("cuba-grouptable");
    }

    public void updateGroupColumns(Set<String> groupColumns) {
        this.groupColumns = groupColumns;
    }

    @Override
    protected void setColWidth(int colIndex, int w, boolean isDefinedWidth) {
        if (GROUP_DIVIDER_COLUMN_KEY.equals(visibleColOrder[colIndex])) {
            w = 0;
            isDefinedWidth = true;
        }

        super.setColWidth(colIndex, w, isDefinedWidth);
    }

    private void addGroupColumn(String colKey) {
        if (groupColumns == null) {
            groupColumns = new HashSet<String>();
        }
        groupColumns.add(colKey);
    }

    private void removeGroupColumn(String colKey) {
        if (groupColumns != null) {
            groupColumns.remove(colKey);
            if (groupColumns.size() == 0)
                groupColumns = null;
        }
    }

    private boolean isGroupColumn(String cid) {
        return groupColumns != null && groupColumns.contains(cid);
    }

    @Override
    protected int getVisibleColsCount(String[] strings) {
        return strings.length + 1;
    }

    @Override
    protected void updateHeaderColumns(String[] strings, int colIndex) {
        boolean dividerPainted = false;

        int i;
        for (i = 0; i < strings.length; i++) {
            final String cid = strings[i];
            if (!isGroupColumn(cid) && !dividerPainted) {
                //paint group columns divider
                visibleColOrder[colIndex] = GROUP_DIVIDER_COLUMN_KEY;
                tHead.enableColumn(GROUP_DIVIDER_COLUMN_KEY, colIndex++);
                dividerPainted = true;
            }
            visibleColOrder[colIndex] = cid;
            tHead.enableColumn(cid, colIndex);
            colIndex++;
        }

        if (!dividerPainted) {
            visibleColOrder[colIndex] = GROUP_DIVIDER_COLUMN_KEY;
            tHead.enableColumn(GROUP_DIVIDER_COLUMN_KEY, colIndex);
        }
    }

    @Override
    protected void updateFooterColumns(String[] strings, int colIndex) {
        boolean dividerPainted = false;

        int i;
        for (i = 0; i < strings.length; i++) {
            final String cid = strings[i];
            if (!isGroupColumn(cid) && !dividerPainted) {
                //paint group columns divider
                tFoot.enableColumn(GROUP_DIVIDER_COLUMN_KEY, colIndex++);
                dividerPainted = true;
            }
            tFoot.enableColumn(cid, colIndex);
            colIndex++;
        }
        if (!dividerPainted) {
            tFoot.enableColumn(GROUP_DIVIDER_COLUMN_KEY, colIndex);
        }
    }

    @Override
    protected void reOrderColumn(String columnKey, int newIndex) {
        // CAUTION This method copied from VScrollTable
        // Added grouping support

        final int oldIndex = getColIndexByKey(columnKey);

        // Change header order
        tHead.moveCell(oldIndex, newIndex);

        // Change body order
        scrollBody.moveCol(oldIndex, newIndex);

        // Change footer order
        tFoot.moveCell(oldIndex, newIndex);

        /*
         * Build new columnOrder and update it to server Note that columnOrder
         * also contains collapsed columns so we cannot directly build it from
         * cells vector Loop the old columnOrder and append in order to new
         * array unless on moved columnKey. On new index also put the moved key
         * i == index on columnOrder, j == index on newOrder
         */

        // Grouping support
        final int groupDividerIndex = getColIndexByKey(GROUP_DIVIDER_COLUMN_KEY);
        if (isGroupColumn(columnKey)) {
            if (newIndex > 0 && newIndex >= groupDividerIndex) {
                removeGroupColumn(columnKey);
                newIndex--;
            }
        } else {
            if (newIndex <= groupDividerIndex) {
                addGroupColumn(columnKey);
            }
            if (newIndex > 0 && newIndex > groupDividerIndex) {
                newIndex--;
            }
        }

        final String oldKeyOnNewIndex = visibleColOrder[newIndex];
        if (showRowHeaders) {
            newIndex--; // columnOrder don't have rowHeader
        }

        if (!GROUP_DIVIDER_COLUMN_KEY.equals(oldKeyOnNewIndex)) {
            for (int i = 0; i < columnOrder.length; i++) {
                if (columnOrder[i].equals(oldKeyOnNewIndex)) {
                    break; // break loop at target
                }
                if (isCollapsedColumn(columnOrder[i])) {
                    newIndex++;
                }
            }
        }

        // finally we can build the new columnOrder for server
        final String[] newOrder = new String[columnOrder.length];
        for (int i = 0, j = 0; j < newOrder.length; i++) {
            if (j == newIndex) {
                newOrder[j] = columnKey;
                j++;
            }
            if (i == columnOrder.length) {
                break;
            }
            if (columnOrder[i].equals(columnKey)) {
                continue;
            }
            newOrder[j] = columnOrder[i];
            j++;
        }
        columnOrder = newOrder;
        // also update visibleColumnOrder
        int colIndex = showRowHeaders ? 1 : 0;
        boolean dividerPainted = false;
        for (int j = 0; j < newOrder.length; j++) {
            final String cid = newOrder[j];
            if (!isGroupColumn(cid) && !dividerPainted) {
                //paint group columns divider
                visibleColOrder[colIndex++] = GROUP_DIVIDER_COLUMN_KEY;
                dividerPainted = true;
            }
            if (!isCollapsedColumn(cid)) {
                visibleColOrder[colIndex++] = cid;
            }
        }

        // Grouping support
        if (groupColumns != null) {
            // collect new grouped columns
            final String[] groupedColumns = new String[groupColumns.size()];
            for (int i = 0, j = 0; i < columnOrder.length; i++) {
                final String colKey = columnOrder[i];
                if (isGroupColumn(colKey)) {
                    groupedColumns[j++] = colKey;
                }
                if (j == groupedColumns.length) {
                    break;
                }
            }
            client.updateVariable(paintableId, "groupedcolumns", groupedColumns, false);
        }

        // CAUTION we use immediate update of column order
        client.updateVariable(paintableId, "columnorder", columnOrder, true);
        if (client.hasEventListeners(this,
                TableConstants.COLUMN_REORDER_EVENT_ID)) {
            client.sendPendingVariableChanges();
        }
    }

    @Override
    protected boolean checkColumnForUpdateWidth(HeaderCell cell) {
        return !GROUP_DIVIDER_COLUMN_KEY.equals(cell.getColKey());
    }

    @Override
    protected VScrollTableBody createScrollBody() {
        return new CubaGroupTableBody();
    }

    @Override
    protected TableHead createTableHead() {
        return new GroupTableHead();
    }

    @Override
    protected TableFooter createTableFooter() {
        return new GroupTableFooter();
    }

    @Override
    protected TableAggregationRow createAggregationRow() {
        return new TableAggregationRow(getAggregatableTable()) {
            @Override
            protected boolean addSpecificCell(String columnId, int colIndex) {
                if (GROUP_DIVIDER_COLUMN_KEY.equals(columnId)) {
                    addCell("", aligns[colIndex], "", false);
                    return true;
                }

                return super.addSpecificCell(columnId, colIndex);
            }
        };
    }

    @Override
    protected boolean handleNavigation(int keycode, boolean ctrl, boolean shift) {
        if (focusedRow instanceof CubaGroupTableBody.CubaGroupTableGroupRow) {
            CubaGroupTableBody.CubaGroupTableGroupRow groupRow = (CubaGroupTableBody.CubaGroupTableGroupRow) focusedRow;

            if (keycode == getNavigationLeftKey()) {
                if (groupRow.expanded) {
                    client.updateVariable(paintableId, "collapse", groupRow.getGroupKey(), true);
                }
                return true;
            }
            if (keycode == getNavigationRightKey()) {
                if (!groupRow.expanded) {
                    client.updateVariable(paintableId, "expand", groupRow.getGroupKey(), true);
                }
                return true;
            }
        }
        return super.handleNavigation(keycode, ctrl, shift);
    }

    public CubaGroupTableBody.CubaGroupTableGroupRow getRenderedGroupRowByKey(String key) {
        if (scrollBody != null) {
            final Iterator<Widget> it = scrollBody.iterator();
            CubaGroupTableBody.CubaGroupTableGroupRow row;
            while (it.hasNext()) {
                Widget widget = it.next();
                if (widget instanceof CubaGroupTableBody.CubaGroupTableGroupRow) {
                    row = (CubaGroupTableBody.CubaGroupTableGroupRow) widget;
                    if (row.getGroupKey().equals(key)) {
                        return row;
                    }
                }
            }
        }
        return null;
    }

    protected class GroupTableHead extends CubaScrollTableHead {
        public GroupTableHead() {
            availableCells.put(GROUP_DIVIDER_COLUMN_KEY, new GroupDividerHeaderCell());
        }

        @Override
        public void clear() {
            super.clear();

            availableCells.put(GROUP_DIVIDER_COLUMN_KEY, new GroupDividerHeaderCell());
        }

        @Override
        protected void fillAdditionalUpdatedCells(HashSet<String> updated) {
            updated.add(GROUP_DIVIDER_COLUMN_KEY);
        }
    }

    protected class GroupTableFooter extends TableFooter {
        public GroupTableFooter() {
            availableCells.put(GROUP_DIVIDER_COLUMN_KEY, new GroupDividerFooterCell());
        }

        @Override
        public void clear() {
            super.clear();
            availableCells.put(GROUP_DIVIDER_COLUMN_KEY, new GroupDividerFooterCell());
        }

        @Override
        protected void fillAdditionalUpdatedCells(HashSet<String> updated) {
            updated.add(GROUP_DIVIDER_COLUMN_KEY);
        }
    }

    protected class CubaGroupTableBody extends CubaScrollTableBody {

        @Override
        protected VScrollTableRow createRow(UIDL uidl, char[] aligns2) {
            if (uidl.hasAttribute("groupKey")) {
                return new CubaGroupTableGroupRow(uidl, aligns2); //creates a group row
            } else
                return new CubaGroupTableRow(uidl, aligns2);
        }

        protected class CubaGroupTableRow extends CubaScrollTableRow {
            protected TableCellElement groupDividerCell;

            public CubaGroupTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected boolean addSpecificCell(Object columnId, int colIndex, char[] aligns) {
                if (GROUP_DIVIDER_COLUMN_KEY.equals(columnId)) {
                    addDividerCell(aligns[colIndex]);
                    return true;
                }
                return false;
            }

            public void addDividerCell(char align) {
                // String only content is optimized by not using Label widget
                final TableCellElement td = DOM.createTD().cast();
                this.groupDividerCell = td;
                initCellWithText("", align, "", false, false, null, td);
                td.addClassName(CLASSNAME + "-group-divider");
            }

            @Override
            protected void updateStyleNames(String primaryStyleName) {
                super.updateStyleNames(primaryStyleName);

                if (groupDividerCell != null) {
                    groupDividerCell.addClassName(CLASSNAME + "-group-divider");
                }
            }
        }

        protected class CubaGroupTableGroupRow extends CubaGroupTableRow {

            protected Integer groupColIndex;
            protected String groupKey;
            protected boolean expanded;
            protected Boolean hasCells;

            public CubaGroupTableGroupRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
                selectable = false;
            }

            @Override
            protected void addCellsFromUIDL(UIDL uidl, char[] aligns, int colIndex, int visibleColumnIndex) {
                this.groupKey = uidl.getStringAttribute("groupKey");
                this.expanded = uidl.hasAttribute("expanded") && uidl.getBooleanAttribute("expanded");

                if (expanded) {
                    addStyleName("v-expanded");
                }

                int currentColIndex = colIndex;
                if (currentColIndex < visibleColOrder.length) {
                    String colKey = uidl.getStringAttribute("colKey");
                    while (currentColIndex < visibleColOrder.length && !visibleColOrder[currentColIndex].equals(colKey)) {
                        //draw empty cells
                        Element td = DOM.createTD();

                        final TableCellElement tdCell = td.cast();
                        initCellWithText("", ALIGN_LEFT, "", false, true, null, tdCell);

                        td.setClassName(CubaGroupTableWidget.this.getStylePrimaryName() + "-cell-content");
                        td.addClassName(CubaGroupTableWidget.this.getStylePrimaryName() + "-cell-stub");
                        DOM.appendChild(getElement(), td);

                        currentColIndex++;
                    }
                } else {
                    throw new ArrayIndexOutOfBoundsException("Group rendering error");
                }

                //paint "+" and group caption
                this.groupColIndex = currentColIndex;

                addGroupCell(uidl.getStringAttribute("groupCaption"));

                currentColIndex++;

                if (uidl.getChildCount() > 0) {
                    Iterator cells = uidl.getChildIterator();
                    while (currentColIndex < visibleColOrder.length) {
                        String columnId = visibleColOrder[currentColIndex];

                        if (GROUP_DIVIDER_COLUMN_KEY.equals(columnId)) { //paint cell for columns group divider
                            addDividerCell(aligns[currentColIndex]);
                        } else if (cells.hasNext()) {
                            final Object cell = cells.next();

                            String style = "";
                            if (uidl.hasAttribute("style-" + columnId)) {
                                style = uidl.getStringAttribute("style-" + columnId);
                            }

                            String description = null;
                            if (uidl.hasAttribute("descr-" + columnId)) {
                                description = uidl.getStringAttribute("descr-"
                                        + columnId);
                            }

                            boolean sorted = tHead.getHeaderCell(currentColIndex).isSorted();
                            if (cell instanceof String) {
                                addCell(uidl, cell.toString(), aligns[currentColIndex], style,
                                        isRenderHtmlInCells(), sorted, description);
                            } else {
                                final ComponentConnector cellContent = client
                                        .getPaintable((UIDL) cell);

                                addCell(uidl, cellContent.getWidget(), aligns[currentColIndex],
                                        style, sorted, description);
                            }
                        }

                        currentColIndex++;
                    }
                    hasCells = true;
                } else {
                    TableCellElement td = getElement().getLastChild().cast();
                    int colSpan = visibleColOrder.length - groupColIndex;
                    td.setColSpan(colSpan);
                    hasCells = false;
                }
            }

            @Override
            protected void setCellWidth(int cellIx, int w) {
                if (hasCells) {
                    super.setCellWidth(cellIx, w);
                } else {
                    calcAndSetWidthForSpannedCell();
                }
            }

            @Override
            protected void initCellWidths() {
                if (!hasCells) {
                    setSpannedRowWidthAfterDOMFullyInited();
                } else {
                    super.initCellWidths();
                }
            }

            private void setSpannedRowWidthAfterDOMFullyInited() {
                // Defer setting width on spanned columns to make sure that
                // they are added to the DOM before trying to calculate
                // widths.
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        calcAndSetWidthForSpannedCell();
                    }
                });
            }

            private void calcAndSetWidthForSpannedCell() {
                final int cells = tHead.getVisibleCellCount();
                for (int i = 0; i < groupColIndex; i++) {
                    int w = CubaGroupTableWidget.this.getColWidth(getColKeyByIndex(i));
                    if (w < 0) {
                        w = 0;
                    }
                    super.setCellWidth(i, w);
                }

                Element tr = getElement();

                int totalSpannedWidth = 0;
                for (int i = groupColIndex; i < cells; i++) {

                    int headerWidth = tHead.getHeaderCell(i).getOffsetWidth();
                    totalSpannedWidth += headerWidth;
                }

                Element td = DOM.getChild(tr, DOM.getChildCount(tr) - 1);

                Style wrapperStyle = td.getFirstChildElement().getStyle();
                WidgetUtil.setWidthExcludingPaddingAndBorder(td, totalSpannedWidth, 13, false);

                int wrapperWidth = totalSpannedWidth;
                if (BrowserInfo.get().isWebkit()
                        || BrowserInfo.get().isOpera10()) {
                            /*
                             * Some versions of Webkit and Opera ignore the width
                             * definition of zero width table cells. Instead, use 1px
                             * and compensate with a negative margin.
                             */
                    if (totalSpannedWidth == 0) {
                        wrapperWidth = 1;
                        wrapperStyle.setMarginRight(-1, Style.Unit.PX);
                    } else {
                        wrapperStyle.clearMarginRight();
                    }
                }
                wrapperStyle.setPropertyPx("width", wrapperWidth);
            }

            protected void addGroupCell(String text) {
                // String only content is optimized by not using Label widget
                Element tdElement = DOM.createTD();
                final TableCellElement td = tdElement.cast();
                initCellWithText(text, ALIGN_LEFT, "", false, true, null, td);

                // Enchance DOM for table cell
                Element container = (Element) td.getChild(0);
                String containerInnerHTML = container.getInnerHTML();

                container.setInnerHTML("");

                Element groupDiv = DOM.createDiv();
                groupDiv.setInnerHTML("&nbsp;");

                groupDiv.setClassName(CLASSNAME + "-group-cell-expander");
                DOM.appendChild(container, groupDiv);

                Element contentDiv = DOM.createDiv();
                contentDiv.setInnerHTML(containerInnerHTML);

                contentDiv.setClassName(CLASSNAME + "-float");
                DOM.appendChild(container, contentDiv);
            }

            @Override
            public void onBrowserEvent(Event event) {
                final Element targetElement = DOM.eventGetTarget(event);
                switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        if (BrowserInfo.get().getWebkitVersion() > 0
                                && targetElement.getPropertyBoolean("__cell")) {
                            scrollBodyPanel.setFocus(true);
                        }
                        setRowFocus(this);

                        if (!isSingleSelectMode()) {
                            deselectAll();
                            sendSelectedRows(false);
                        }

                        handleRowClick(event);
                        break;
                    default:
                        break;
                }
            }

            protected void handleRowClick(Event event) {
                if (expanded) {
                    client.updateVariable(paintableId, "collapse", getGroupKey(), true);
                } else {
                    client.updateVariable(paintableId, "expand", getGroupKey(), true);
                }

                DOM.eventCancelBubble(event, true);
            }

            @Override
            protected boolean handleClickEvent(Event event, com.google.gwt.dom.client.Element targetTdOrTr,
                                               boolean immediate) {
                return false;
            }

            @Override
            public void showContextMenu(Event event) {
                //do nothing
            }

            public String getGroupKey() {
                return groupKey;
            }

            public boolean isExpanded() {
                return expanded;
            }
        }

        @Override
        public void moveCol(int oldIndex, int newIndex) {
            //do nothing, columns reordering will be process on the server
        }

        @Override
        public int getAvailableWidth() {
            // fix for Halo based themes #PL-4570
            // it does not set real width of group column to divider cell element

            int availableWidth = super.getAvailableWidth();
            HeaderCell headerCell = tHead.getHeaderCell(GROUP_DIVIDER_COLUMN_KEY);
            int groupCellWidth = headerCell.getOffsetWidth();
            Style headCellStyle = headerCell.getElement().getStyle();

            if (!(groupCellWidth + "px").equals(headCellStyle.getWidth())) {
                if (availableWidth - groupCellWidth > 0) {
                    availableWidth -= groupCellWidth;
                }
            }

            return availableWidth;
        }

        @Override
        public boolean isGeneratedRow(Widget row) {
            return row instanceof CubaGroupTableGroupRow;
        }
    }

    protected class GroupDividerHeaderCell extends CubaScrollTableHeaderCell {
        public GroupDividerHeaderCell() {
            super(GROUP_DIVIDER_COLUMN_KEY, null);
            addStyleName(CLASSNAME + "-group-divider-header");

            setText("&nbsp;");
        }

        @Override
        protected void updateStyleNames(String primaryStyleName) {
            super.updateStyleNames(primaryStyleName);
            addStyleName(CLASSNAME + "-group-divider-header");
        }

        @Override
        public void onBrowserEvent(Event event) {
            //do nothing
        }

        @Override
        protected void handleCaptionEvent(Event event) {
            //do nothing
        }
    }

    protected class GroupDividerFooterCell extends FooterCell {

        public GroupDividerFooterCell() {
            super(GROUP_DIVIDER_COLUMN_KEY, null);
            addStyleName(CLASSNAME + "-group-divider-footer");
        }

        @Override
        protected void updateStyleNames(String primaryStyleName) {
            super.updateStyleNames(primaryStyleName);
            addStyleName(CLASSNAME + "-group-divider-footer");
        }

        @Override
        public void onBrowserEvent(Event event) {
            //do nothing
        }

        @Override
        protected void handleCaptionEvent(Event event) {
            //do nothing
        }
    }
}