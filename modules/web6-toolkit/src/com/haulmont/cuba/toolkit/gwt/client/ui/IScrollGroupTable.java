/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.toolkit.gwt.client.Tools;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.IScrollTable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IScrollGroupTable extends IScrollTable {

    protected Set<String> groupColumns;

    @Override
    public void updateFromUIDL(UIDL uidl) {
        if (uidl.hasVariable("groupColumns")) {
            groupColumns = uidl.getStringArrayVariableAsSet("groupColumns");
        } else {
            groupColumns = null;
        }

        super.updateFromUIDL(uidl);
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
            if (groupColumns.size() == 0) groupColumns = null;
        }
    }

    private boolean isGroupColumn(String cid) {
        return groupColumns != null && groupColumns.contains(cid);
    }

    @Override
    protected boolean updateImmediate() {
        return true; //all changes will be process immediate
    }

    @Override
    protected IScrollTableHead createHead() {
        return new IScrollGroupTableHead();
    }

    @Override
    protected IScrollGroupTableBody createBody() {
        return new IScrollGroupTableBody();
    }

    @Override
    protected AggregationRow createAggregationRow(UIDL uidl) {
        return new GroupTableAggregationRow();
    }

    public class IScrollGroupTableHead extends IScrollTableHead {
        public IScrollGroupTableHead() {
            super();
            availableCells.put("-1", new GroupDividerHeaderCell());
        }

        @Override
        public void clear() {
            super.clear();
            availableCells.put("-1", new GroupDividerHeaderCell());
        }

        @Override
        public void updateCellsFromUIDL(UIDL uidl) {
            Iterator it = uidl.getChildIterator();
            HashSet<String> updated = new HashSet<String>();
            updated.add("-1"); // Group columns devider column key
            updated.add("0"); // Row headers column key
            while (it.hasNext()) {
                final UIDL col = (UIDL) it.next();
                final String cid = col.getStringAttribute("cid");
                updated.add(cid);

                String caption = buildCaptionHtmlSnippet(col);
                HeaderCell c = getHeaderCell(cid);
                if (c == null) {
                    c = createHeaderCell(cid, col);
                    availableCells.put(cid, c);
                    if (initializedAndAttached) {
                        // we will need a column width recalculation
                        initializedAndAttached = false;
                        initialContentReceived = false;
                        isNewBody = true;
                    }
                } else {
                    c.setText(caption);
                }

                if (col.hasAttribute("sortable")) {
                    c.setSortable(true);
                    if (cid.equals(sortColumn)) {
                        c.setSorted(true);
                    } else {
                        c.setSorted(false);
                    }
                }
                if (col.hasAttribute("align")) {
                    c.setAlign(col.getStringAttribute("align").charAt(0));
                }
                if (col.hasAttribute("width")) {
                    final String width = col.getStringAttribute("width");
                    c.setWidth(Integer.parseInt(width));
                } else if (recalcWidths) {
                    c.setWidth(-1);
                }
            }
            // check for orphaned header cells
            for (Iterator cellsIterator = availableCells.keySet().iterator(); cellsIterator.hasNext();) {
                final String cid = (String) cellsIterator.next();
                if (!updated.contains(cid)) {
                    removeCell(cid);
                    cellsIterator.remove();
                }
            }
        }
    }

    @Override
    protected void updateHeader(String[] colIds) {
        if (colIds == null) {
            return;
        }

        int visibleCols = colIds.length + 1; //allow group divider column
        int colIndex = 0;
        if (showRowHeaders) {
            tHead.enableColumn("0", colIndex);
            visibleCols++;
            visibleColOrder = new String[visibleCols];
            visibleColOrder[colIndex] = "0";
            colIndex++;
        } else {
            visibleColOrder = new String[visibleCols];
            tHead.removeCell("0");
        }

        updateHeaderColumns(colIds, colIndex);

        tHead.setVisible(showColHeaders);
        setContainerHeight();
    }

    @Override
    protected void updateHeaderColumns(String[] colIds, int colIndex) {
        boolean dividerPainted = false;
        int i;

        //clear old header cells
        clearOrphanedCells(colIds);

        //show updated header cells
        for (i = 0; i < colIds.length; i++) {
            final String cid = colIds[i];
            if (!isGroupColumn(cid) && !dividerPainted) {
                //paint group columns divider
                visibleColOrder[colIndex] = "-1";
                tHead.enableColumn("-1", colIndex++);
                dividerPainted = true;
            }
            visibleColOrder[colIndex] = cid;
            tHead.enableColumn(cid, colIndex);
            colIndex++;
        }
        if (!dividerPainted) {
            visibleColOrder[colIndex] = "-1";
            tHead.enableColumn("-1", colIndex);
        }
    }

    @Override
    protected void reOrderColumn(String columnKey, int newIndex) {

        final int oldIndex = getColIndexByKey(columnKey);

        // Change header order
        tHead.moveCell(oldIndex, newIndex);

        // Change body order
        tBody.moveCol(oldIndex, newIndex);

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

        final int groupDividerIndex = getColIndexByKey("-1");
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

        if (!"-1".equals(oldKeyOnNewIndex)) {
            // add back hidden rows,
            for (final String aColumnOrder : columnOrder) {
                if (aColumnOrder.equals(oldKeyOnNewIndex)) {
                    break; // break loop at target
                }
                if (isCollapsedColumn(aColumnOrder)) {
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
        int index = showRowHeaders ? 1 : 0;
        boolean dividerPainted = false;
        for (final String cid : newOrder) {
            if (!isGroupColumn(cid) && !dividerPainted) {
                //paint group columns divider
                visibleColOrder[index++] = "-1";
                dividerPainted = true;
            }
            if (!isCollapsedColumn(cid)) {
                visibleColOrder[index++] = cid;
            }
        }

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
        client.updateVariable(paintableId, "columnorder", columnOrder, updateImmediate());
    }

    protected class GroupDividerHeaderCell extends HeaderCell {
        public GroupDividerHeaderCell() {
            super("-1", null);
            setWidth(10);
            DOM.setElementProperty(td, "className", CLASSNAME
                    + "-group-div");
        }

        @Override
        public void onBrowserEvent(Event event) {
            //do nothing
        }

        @Override
        protected void handleCaptionEvent(Event event) {
            //do nothing
        }

        @Override
        protected void onResizeEvent(Event event) {
            //do nothing
        }
    }

    public class IScrollGroupTableBody extends IScrollTableBody {
        @Override
        protected IScrollTableRow createRowInstance(UIDL uidl) {
            if ("gr".equals(uidl.getTag())) {
                return new IScrollGroupTableGroupRow(uidl, aligns); //creates a group row
            } else {
                return new IScrollGroupTableRow(uidl, aligns);
            }
        }

        @Override
        protected IScrollTableRow createRow(UIDL uidl) {
            IScrollTableRow row = createRowInstance(uidl);
            for (int i = 0; i < visibleColOrder.length; i++) {
                setRowColWidth(row, i, IScrollGroupTable.this.getColWidth(getColKeyByIndex(i)));
            }
            return row;
        }

        @Override
        protected void addRowBeforeFirstRendered(IScrollTableRow row) {
            super.addRowBeforeFirstRendered(row);
            if (row instanceof IScrollGroupTableGroupRow
                    && ((IScrollGroupTableGroupRow) row).isExpanded()) {
                row.addStyleName("v-expanded");
            }
        }

        @Override
        protected void addRow(IScrollTableRow row) {
            super.addRow(row);
            if (row instanceof IScrollGroupTableGroupRow
                    && ((IScrollGroupTableGroupRow) row).isExpanded()) {
                row.addStyleName("v-expanded");
            }
        }

        @Override
        protected void applyAlternatingRowColor(IScrollTableRow row, String style) {
            if (row instanceof IScrollGroupTableGroupRow) {
                row.addStyleName(CLASSNAME + "-group-row");
            } else {
                super.applyAlternatingRowColor(row, style);
            }
        }

        @Override
        public int getColWidth(int i) {
            if (initDone) {
                Widget row = null;
                for (Widget w : renderedRows) {
                    if (!(w instanceof IScrollGroupTableGroupRow)) {
                        row = w;
                        break;
                    }
                }
                if (row != null) {
                    final Element e = DOM.getChild(row.getElement(), i);
                    return DOM.getElementPropertyInt(e, "offsetWidth");
                }
            }
            return 0;
        }

        @Override
        public void setColWidth(int colIndex, int w) {
            for (final Widget row : renderedRows) {
                setRowColWidth(row, colIndex, w);
            }
        }

        private void setRowColWidth(Widget row, int colIndex, int w) {
            if (row instanceof IScrollGroupTableGroupRow) {
                IScrollGroupTableGroupRow groupRow = (IScrollGroupTableGroupRow) row;
                if (groupRow.hasCells || colIndex < groupRow.getColIndex()) {
                    applyWidth(row.getElement(), colIndex, w);
                } else {
                    addWidth(row.getElement(), colIndex, w);
                }
            } else {
                applyWidth(row.getElement(), colIndex, w);
            }
        }

        private void applyWidth(Element tr, int colIndex, int w) {
            Element td = DOM.getChild(tr, colIndex);
            if (DOM.getChildCount(td) > 0) {
                setWidthDependsOnStyle(DOM.getFirstChild(td), w);
            }
            DOM.setStyleAttribute(td, "width", w + "px");
        }

        private void addWidth(Element tr, int colIndex, int w) {
            int newWidth = w;
            w -= DOM.getElementPropertyInt(tr, "_cellWidth" + colIndex);
            w += DOM.getElementPropertyInt(tr, "_cellWidth");
            DOM.setElementPropertyInt(tr, "_cellWidth" + colIndex, newWidth);
            DOM.setElementPropertyInt(tr, "_cellWidth", w);

            Element td = DOM.getChild(tr, DOM.getChildCount(tr) - 1);
            setWidthDependsOnStyle(DOM.getFirstChild(td), w);
            DOM.setStyleAttribute(td, "width", w + "px");
        }

        public class IScrollGroupTableRow extends IScrollTableRow {
            public IScrollGroupTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void addCells(UIDL uidl, int colIndex) {
                Iterator it = uidl.getChildIterator();
                while (colIndex < visibleColOrder.length) {
                    String columnId = visibleColOrder[colIndex];

                    String description = null;
                    if (uidl.hasAttribute("descr-" + columnId)) {
                        description = uidl.getStringAttribute("descr-"
                                + columnId);
                    }

                    if ("-1".equals(columnId)) { //paint cell for columns group divider
                        addCell("", aligns[colIndex], "", colIndex, false, description);
                    } else if (it.hasNext()) {
                        Object cell = it.next();
                        String style = "";
                        if (uidl.hasAttribute("style-" + columnId)) {
                            style = uidl.getStringAttribute("style-" + columnId);
                        }

                        if (cell instanceof String) {
                            addCell(cell.toString(), aligns[colIndex], style, colIndex, false, description);

                        } else {
                            Paintable cellContent = client
                                    .getPaintable((UIDL) cell);
                            addCell((Widget) cellContent, aligns[colIndex], style, colIndex, description);
                            paintComponent(cellContent, (UIDL) cell);
                        }
                    }
                    colIndex++;
                }
            }
        }

        public class IScrollGroupTableGroupRow extends IScrollGroupTableRow {

            private String colKey;
            private String groupKey;
            private boolean expanded;

            private int colIndex = -1;

            private boolean hasCells = false;

            public IScrollGroupTableGroupRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void addCells(UIDL uidl, int colIndex) {
                colKey = uidl.getStringAttribute("colKey");
                groupKey = uidl.getStringAttribute("groupKey");
                expanded = uidl.hasAttribute("expanded") && uidl.getBooleanAttribute("expanded");

                while (colIndex < visibleColOrder.length && !visibleColOrder[colIndex].equals(colKey)) { //draw empty cells
                    Element td = DOM.createTD();
                    Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
                    DOM.appendChild(getElement(), td);
                    colIndex++;
                }

                if (colIndex < visibleColOrder.length) { //paint "+" and group caption
                    this.colIndex = colIndex;
                    addCell(uidl.getStringAttribute("caption"), aligns[colIndex], "", colIndex++, false, true);

                    if (uidl.getChildCount() > 0) {
                        Iterator it = uidl.getChildIterator();
                        while (colIndex < visibleColOrder.length) {
                            String columnId = visibleColOrder[colIndex];
                            String description = null;
                            if (uidl.hasAttribute("descr-" + columnId)) {
                                description = uidl.getStringAttribute("descr-"
                                        + columnId);
                            }

                            if ("-1".equals(columnId)) {//paint cell for columns group divider
                                addCell("", aligns[colIndex], "", colIndex, false, description);
                            } else if (it.hasNext()) {
                                final Object cell = it.next();
                                if (cell instanceof String) {
                                    addCell((String) cell, aligns[colIndex], "", colIndex, false, description);
                                } else {
                                    Paintable p = client.getPaintable((UIDL) cell);
                                    addCell((Widget) p, aligns[colIndex], "", colIndex, description);
                                    paintComponent(p, (UIDL) cell);
                                }
                            }
                            colIndex++;
                        }
                        hasCells = true;
                    } else {
                        Element td = DOM.getChild(getElement(), DOM.getChildCount(getElement()) - 1);
                        DOM.setElementAttribute(td, "colSpan", String.valueOf(visibleColOrder.length - this.colIndex));
                        hasCells = false;
                    }
                } else {
                    throw new RuntimeException("Unexpected error");
                }
            }

            public void addCell(String text, char align, String style, int col,
                                boolean textIsHTML, boolean paintGroup) {
                // String only content is optimized by not using Label widget
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();

                Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
                Tools.setStylePrimaryName(container, CLASSNAME + "-cell-content");
                if (allowMultiStingCells) {
                    Tools.addStyleName(td, CLASSNAME + "-cell-wrap");
                }
                if (style != null && !style.equals("")) {
                    Tools.addStyleDependentName(td, style);
                    Tools.addStyleDependentName(container, style);
                }

                Element contentDiv = container;
                if (paintGroup) {
                    //create "+" cell
                    Element groupDiv = DOM.createDiv();
                    DOM.setInnerHTML(groupDiv, "&nbsp;");
                    Tools.setStyleName(groupDiv, CLASSNAME + "-group-cell");
                    DOM.appendChild(container, groupDiv);

                    contentDiv = DOM.createDiv();
                    Tools.setStyleName(contentDiv, CLASSNAME + "-float");
                    DOM.appendChild(container, contentDiv);
                }

                setCellText(contentDiv, text, textIsHTML);
                setCellAlignment(contentDiv, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);

                tableCells.add(td);

                Tools.textSelectionEnable(td, textSelectionEnabled);
            }

            @Override
            protected void moveCol(int oldIndex, int newIndex) {
                //do nothing, columns reordering will be process on the server
            }

            @Override
            public void onBrowserEvent(Event event) {
                final Element targetElement = DOM.eventGetTarget(event);
                switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        if (BrowserInfo.get().getWebkitVersion() > 0 && DOM.getElementPropertyBoolean(targetElement, "__cell")) {
                            bodyContainer.setFocus(true);
                        }
                        handleClickEvent(event);
                        handleRowClick(event);
                        break;
                    default:
                        break;
                }
            }

            @Override
            protected void handleRowClick(Event event) {
                if (isExpanded()) {
                    client.updateVariable(paintableId, "collapse", getGroupKey(), true);
                } else {
                    client.updateVariable(paintableId, "expand", getGroupKey(), true);
                }
                DOM.eventCancelBubble(event, true);
            }

            @Override
            protected void handleClickEvent(Event event) {
                //do nothing
            }

            @Override
            public void showContextMenu(Event event) {
                //do nothing
            }

            public String getColKey() {
                return colKey;
            }

            public String getGroupKey() {
                return groupKey;
            }

            public int getColIndex() {
                return colIndex;
            }

            public boolean isExpanded() {
                return expanded;
            }

            public boolean hasCells() {
                return hasCells;
            }
        }
    }

    private class GroupTableAggregationRow extends AggregationRow {
        @Override
        protected void paintRow(UIDL uidl) {
            Iterator it = uidl.getChildIterator();
            for (int colIndex = 0; colIndex < visibleColOrder.length; colIndex++) {
                String columnId = visibleColOrder[colIndex];
                if ("-1".equals(columnId)) {
                    addCell("", aligns[colIndex], "");
                } else if (it.hasNext()) {
                    String style = "";
                    if (uidl.hasAttribute("style-" + columnId)) {
                        style = uidl.getStringAttribute("style-" + columnId);
                    }
                    final Object cell = it.next();
                    if (cell instanceof String) {
                        addCell((String) cell, aligns[colIndex], style);
                    } else {
                        Paintable p = client.getPaintable((UIDL) cell);
                        addCell((Widget) p, aligns[colIndex], style);
                        p.updateFromUIDL((UIDL) cell, client);
                    }

                    int colWidth;
                    if ((colWidth = getColWidth(getColKeyByIndex(colIndex))) > -1) {
                        setColWidth(colIndex, colWidth);
                    }
                }
            }
        }
    }

}
