/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.grouptable;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.haulmont.cuba.web.toolkit.ui.client.table.CubaScrollTableWidget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.UIDL;
import com.vaadin.client.Util;
import com.vaadin.shared.ui.table.TableConstants;

import java.util.HashSet;
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

        // Change footer order
        tFoot.moveCell(oldIndex, newIndex);

        /*
         * Build new columnOrder and update it to server Note that columnOrder
         * also contains collapsed columns so we cannot directly build it from
         * cells vector Loop the old columnOrder and append in order to new
         * array unless on moved columnKey. On new index also put the moved key
         * i == index on columnOrder, j == index on newOrder
         */
        final String oldKeyOnNewIndex = visibleColOrder[newIndex];
        if (showRowHeaders) {
            newIndex--; // columnOrder don't have rowHeader
        }

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

        @Override
        protected void addRow(VScrollTableRow row) {
            super.addRow(row);
            if (row instanceof CubaGroupTableGroupRow
                    && ((CubaGroupTableGroupRow) row).isExpanded()) {
                row.addStyleName("v-expanded");
            }
        }

        protected class CubaGroupTableRow extends CubaScrollTableRow {
            private TableCellElement groupDividerCell;

            public CubaGroupTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected boolean addSpecificCell(Object columnId, int colIndex) {
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

                if (groupDividerCell != null)
                    groupDividerCell.addClassName(CLASSNAME + "-group-divider");
            }
        }

        protected class CubaGroupTableGroupRow extends CubaGroupTableRow {
            private Integer groupColIndex;
            private String groupKey;
            private boolean expanded;

            public CubaGroupTableGroupRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void initCellWidths() {
                super.initCellWidths();

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        setWidthForSpannedCell();
                    }
                });
            }

            protected void setWidthForSpannedCell() {
                int spanWidth = 0;
                for (int ix = groupColIndex; ix < tHead.getVisibleCellCount(); ix++) {
                    spanWidth += tHead.getHeaderCell(ix).getOffsetWidth();
                }
                Util.setWidthExcludingPaddingAndBorder((Element) getElement().getChild(groupColIndex),
                        spanWidth, 13, false);
            }

            @Override
            protected void addCellsFromUIDL(UIDL uidl, char[] aligns, int colIndex, int visibleColumnIndex) {
                this.groupKey = uidl.getStringAttribute("groupKey");
                this.expanded = uidl.hasAttribute("expanded") && uidl.getBooleanAttribute("expanded");

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

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        setWidthForSpannedCell();
                    }
                });
            }

            @Override
            protected void setCellWidth(int cellIx, int width) {
                if (groupColIndex > cellIx) {
                    super.setCellWidth(cellIx, width);
                } else {
                    setWidthForSpannedCell();
                }
            }

            protected void addGroupCell(String text) {
                // String only content is optimized by not using Label widget
                Element tdElement = DOM.createTD();
                final TableCellElement td = tdElement.cast();
                td.setColSpan(visibleColOrder.length - groupColIndex);
                initCellWithText(text, ALIGN_LEFT, "", false, true, null, td);

                // Enchance DOM for table cell
                Element container = (Element) td.getChild(0);
                String containerInnerHTML = container.getInnerHTML();

                container.setInnerHTML("");

                Element groupDiv = DOM.createDiv();
                groupDiv.setInnerHTML("&nbsp;");

                Tools.setStyleName(groupDiv, CLASSNAME + "-group-cell-expander");
                DOM.appendChild(container, groupDiv);

                Element contentDiv = DOM.createDiv();
                contentDiv.setInnerHTML(containerInnerHTML);
                Tools.setStyleName(contentDiv, CLASSNAME + "-float");
                DOM.appendChild(container, contentDiv);
            }

            @Override
            public void onBrowserEvent(Event event) {
                final Element targetElement = DOM.eventGetTarget(event);
                switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        if (BrowserInfo.get().getWebkitVersion() > 0
                                && DOM.getElementPropertyBoolean(targetElement, "__cell")) {
                            scrollBodyPanel.setFocus(true);
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
    }

    protected class GroupDividerHeaderCell extends CubaScrollTableHeaderCell {
        public GroupDividerHeaderCell() {
            super(GROUP_DIVIDER_COLUMN_KEY, null);
            addStyleName(CLASSNAME + "-group-divider-header");
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