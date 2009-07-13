package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.Widget;
import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.RenderSpace;
import com.itmill.toolkit.terminal.gwt.client.Util;
import com.itmill.toolkit.terminal.gwt.client.ui.IScrollTable;
import com.itmill.toolkit.terminal.gwt.client.ui.Table;

import java.util.Iterator;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */
public class IScrollTreeTable
        extends IScrollTable {

    private static int LEVEL_STEP_SIZE = 19;

    @Override
    protected IScrollTableBody createBody() {
        return new IScrollTreeTableBody();
    }

    @Override
    protected void sizeInit() {
        /*
         * We will use browsers table rendering algorithm to find proper column
         * widths. If content and header take less space than available, we will
         * divide extra space relatively to each column which has not width set.
         *
         * Overflow pixels are added to last column.
         */

        Iterator<Widget> headCells = tHead.iterator();
        int i = 0;
        int totalExplicitColumnsWidths = 0;
        int total = 0;

        final int[] widths = new int[tHead.getVisibleCellCount()];

        tHead.enableBrowserIntelligence();
        // first loop: collect natural widths
        while (headCells.hasNext()) {
            final HeaderCell hCell = (HeaderCell) headCells.next();
            int w = hCell.getWidth();
            if (w > 0) {
                // server has defined column width explicitly
                totalExplicitColumnsWidths += w;
            } else {
                final int hw = hCell.getOffsetWidth();
                final int cw = tBody.getColWidth(i);
                w = (hw > cw ? hw : cw) + IScrollTableBody.CELL_EXTRA_WIDTH;
            }
            widths[i] = w;
            total += w;
            i++;
        }

        tHead.disableBrowserIntelligence();

        // fix "natural" height if height not set
/*
        if (height == null || "".equals(height)) {
            bodyContainer.setHeight((tBody.getRowHeight() * pageLength) + "px");
        }
*/
        if (height == null || "".equals(height)) {
            bodyContainer.setHeight((tBody.getRowHeight() * (totalRows<pageLength?( (totalRows<1)?1:totalRows ):pageLength) ) + "px");
            String height = (tBody.getRowHeight() * (totalRows<pageLength?( (totalRows<1)?1:totalRows ):pageLength) ) + "px";
        }

        // fix "natural" width if width not set
        if (width == null || "".equals(width)) {
            //            w += getScrollbarWidth();
            setContentWidth(total);
        }

        int availW = tBody.getAvailableWidth();
        // Hey IE, are you really sure about this?
        availW = tBody.getAvailableWidth() - Util.getNativeScrollbarSize();//todo fix an issue with scroll bar

//        boolean needsReLayout = false;

        if (availW > total || allowMultiStingCells/*fix an issue with the scrollbar appearing*/) {
            // natural size is smaller than available space
            int extraSpace = availW - total;
            int totalWidthR = total - totalExplicitColumnsWidths;
            if (totalWidthR > 0) {
//                needsReLayout = true;

                /*
                 * If the table has a relative width and there is enough space
                 * for a scrollbar we reserve this in the last column
                 */
                int scrollbarWidth = Util.getNativeScrollbarSize();
                if (relativeWidth && totalWidthR >= scrollbarWidth) {
                    scrollbarWidthReserved = scrollbarWidth + 1; //
                    int columnindex = tHead.getVisibleCellCount() - 1;
                    widths[columnindex] += scrollbarWidthReserved;
                    HeaderCell headerCell = tHead.getHeaderCell(columnindex);
                    if (headerCell.getWidth() == -1) {
                        totalWidthR += scrollbarWidthReserved;
                    }
                    extraSpace -= scrollbarWidthReserved;
                    scrollbarWidthReservedInColumn = columnindex;
                }

                calculatedWidth = 0;

                // now we will share this sum relatively to those without
                // explicit width
                headCells = tHead.iterator();
                i = 0;
                HeaderCell hCell;
                while (headCells.hasNext()) {
                    hCell = (HeaderCell) headCells.next();
                    if (hCell.getWidth() == -1) {
                        int w = widths[i];
                        final int newSpace;
                        if (availW > total) {
                            newSpace = extraSpace * w / totalWidthR;
                        } else {
                            newSpace = (int) Math.floor((double) extraSpace * (double) w / (double) totalWidthR);
                        }
                        w += newSpace;
                        widths[i] = w;
                        calculatedWidth += w;
                    } else {
                        calculatedWidth += hCell.getWidth();
                    }
                    i++;
                }
            }
        } else {
            // bodys size will be more than available and scrollbar will appear
        }

        // last loop: set possibly modified values or reset if new tBody
        i = 0;
        headCells = tHead.iterator();
        while (headCells.hasNext()) {
            final HeaderCell hCell = (HeaderCell) headCells.next();
            if (isNewBody || hCell.getWidth() == -1) {
                final int w = widths[i];
                setColWidth(i, w);
            }
            i++;
        }

        isNewBody = false;

        if (firstvisible > 0) {
            // Deferred due some Firefox oddities. IE & Safari could survive
            // without
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    bodyContainer.setScrollPosition(firstvisible
                            * tBody.getRowHeight());
                    firstRowInViewPort = firstvisible;
                }
            });
        }

        if (enabled) {
            // Do we need cache rows
            if (tBody.getLastRendered() + 1 < firstRowInViewPort + pageLength
                    + CACHE_REACT_RATE * pageLength) {
                if (totalRows - 1 > tBody.getLastRendered()) {
                    // fetch cache rows
                    rowRequestHandler
                            .setReqFirstRow(tBody.getLastRendered() + 1);
                    rowRequestHandler
                            .setReqRows((int) (pageLength * CACHE_RATE));
                    rowRequestHandler.deferRowFetch(1);
                }
            }
        }
        initializedAndAttached = true;

//        if (needsReLayout) {
        tBody.reLayoutComponents();
//        }
    }

    public class IScrollTreeTableBody extends IScrollTableBody {

        protected int groupColIndex =
                showRowHeaders ? 1 : 0;

        @Override
        public void renderRows(UIDL rowData, int firstIndex, int rows) {
            // FIXME REVIEW
            aligns = tHead.getColumnAlignments();
            final Iterator it = rowData.getChildIterator();
            if (firstIndex == lastRendered + 1) {
                while (it.hasNext()) {
                    final IScrollTableRow row = createRow((UIDL) it.next());
                    addRow(row);
                    lastRendered++;
                }
                fixSpacers();
            } else if (firstIndex + rows == firstRendered) {
                final IScrollTableRow[] rowArray = new IScrollTreeTableRow[rows];
                int i = rows;
                while (it.hasNext()) {
                    i--;
                    rowArray[i] = createRow((UIDL) it.next());
                }
                for (i = 0; i < rows; i++) {
                    addRowBeforeFirstRendered(rowArray[i]);
                    firstRendered--;
                }
            } else {
                // completely new set of rows
                while (lastRendered + 1 > firstRendered) {
                    unlinkRow(false);
                }
                final IScrollTableRow row = createRow((UIDL) it.next());
                firstRendered = firstIndex;
                lastRendered = firstIndex - 1;
                addRow(row);
                lastRendered++;
                setContainerHeight();
                fixSpacers();
                while (it.hasNext()) {
                    addRow(createRow((UIDL) it.next()));
                    lastRendered++;
                }
                fixSpacers();
            }
            // this may be a new set of rows due content change,
            // ensure we have proper cache rows
            int reactFirstRow = (int) (firstRowInViewPort - pageLength
                    * CACHE_REACT_RATE);
            int reactLastRow = (int) (firstRowInViewPort + pageLength + pageLength
                    * CACHE_REACT_RATE);
            if (reactFirstRow < 0) {
                reactFirstRow = 0;
            }
            if (reactLastRow > totalRows) {
                reactLastRow = totalRows - 1;
            }
            if (lastRendered < reactLastRow) {
                // get some cache rows below visible area
                rowRequestHandler.setReqFirstRow(lastRendered + 1);
                rowRequestHandler.setReqRows(reactLastRow - lastRendered - 1);
                rowRequestHandler.deferRowFetch(1);
            } else if (IScrollTreeTable.this.tBody.getFirstRendered() > reactFirstRow) {
                /*
                 * Branch for fetching cache above visible area.
                 *
                 * If cache needed for both before and after visible area, this
                 * will be rendered after-cache is reveived and rendered. So in
                 * some rare situations table may take two cache visits to
                 * server.
                 */
                rowRequestHandler.setReqFirstRow(reactFirstRow);
                rowRequestHandler.setReqRows(firstRendered - reactFirstRow);
                rowRequestHandler.deferRowFetch(1);
            }
        }

        @Override
        public void renderInitialRows(UIDL rowData, int firstIndex, int rows) {
            firstRendered = firstIndex;
            lastRendered = firstIndex + rows - 1;
            final Iterator it = rowData.getChildIterator();
            aligns = tHead.getColumnAlignments();
            while (it.hasNext()) {
                final IScrollTableRow row = createRowInstance((UIDL) it.next(),
                        aligns);
                addRow(row);
            }
            if (isAttached()) {
                fixSpacers();
            }
        }

        @Override
        protected void addRowBeforeFirstRendered(IScrollTableRow row) {
            super.addRowBeforeFirstRendered(row);
            if (((IScrollTreeTableRow) row).isExpanded()) {
                row.addStyleName("i-expanded");
            }
        }

        @Override
        protected void addRow(IScrollTableRow row) {
            super.addRow(row);
            if (((IScrollTreeTableRow) row).isExpanded()) {
                row.addStyleName("i-expanded");
            }
        }

        protected IScrollTreeTableRow createRowInstance(UIDL uidl, char[] aligns) {
            boolean isCaption = isCaptionRow(uidl);
            final IScrollTreeTableRow row;
            if (isCaption) {
                row = new IScrollTreeTableCaptionRow(uidl, aligns);
            } else {
                row = new IScrollTreeTableRow(uidl, aligns);
            }
            return row;
        }

        @Override
        protected IScrollTableRow createRow(UIDL uidl) {
            final IScrollTreeTableRow row = createRowInstance(uidl, aligns);
            if (!isCaptionRow(uidl)) {
                final int cells = DOM.getChildCount(row.getElement());
                for (int i = 0; i < cells; i++) {
                    final int w = IScrollTreeTable.this
                            .getColWidth(getColKeyByIndex(i));
                    applyCellWidth(row, i, w);
                }
            }
            return row;
        }

        @Override
        public int getColWidth(int i) {
            if (initDone) {
                IScrollTreeTableRow row = null;
                for (Object o : renderedRows) {
                    if (!(o instanceof IScrollTreeTableCaptionRow)) {
                        row = (IScrollTreeTableRow) o;
                        break;
                    }
                }
                if (row != null) {
                    final Element e = DOM.getChild(row.getElement(), i);
                     DOM.getElementPropertyInt(e, "offsetWidth");
                }
            }
            return 0;
        }

        @Override
        public void setColWidth(int colIndex, int w) {
            for (final Object o : renderedRows) {
                if (o instanceof IScrollTreeTableCaptionRow) {
                    if (colIndex < groupColIndex) {
                        applyCellWidth((IScrollTreeTableRow) o, colIndex, w);
                    } else {
                        int rowWidth = scrollbarWidthReserved > 0
                                ? calculatedWidth - scrollbarWidthReserved : calculatedWidth;
                        applyCellWidth((IScrollTreeTableRow) o, groupColIndex, rowWidth);
                    }
                } else {
                    applyCellWidth((IScrollTreeTableRow) o, colIndex, w);
                }
            }
        }

        protected void applyCellWidth(IScrollTreeTableRow row,
                int colIndex, int w)
        {
            final Element cell = DOM.getChild(row.getElement(),
                    colIndex);
            int innerWidth = w;
            if (colIndex == groupColIndex) {
                if (row.hasChildren()) {
                    innerWidth -= (row.getLevel() * LEVEL_STEP_SIZE);
                } else {
                    innerWidth -= ((row.getLevel() + 1) * LEVEL_STEP_SIZE);
                }
            }
            DOM.setStyleAttribute(DOM.getFirstChild(cell), "width",
                    (innerWidth - CELL_CONTENT_PADDING) + "px");
            DOM.setStyleAttribute(cell, "width", w + "px");
        }

        @Override
        protected void applyAlternatingRowColor(IScrollTableRow row,
                    String style)
        {
            if (row instanceof IScrollTreeTableCaptionRow) {
                row.addStyleName(CLASSNAME + "-caption-row");
            } else {
                super.applyAlternatingRowColor(row, style);
            }
        }

        private boolean isCaptionRow(UIDL uidl) {
            return uidl.hasAttribute("rowCaption");
        }

        public class IScrollTreeTableCaptionRow extends IScrollTreeTableRow {
            public IScrollTreeTableCaptionRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void addCells(UIDL uidl, int col) {
                int columnCount = IScrollTreeTable.this.tHead.getVisibleCellCount();

                final Element td = DOM.createTD();
                DOM.setElementAttribute(td, "colSpan", String.valueOf(columnCount));

                final Element container = DOM.createDiv();
                DOM.setElementProperty(container, "className", CLASSNAME + "-caption-row-content");
                if (groupCell != null) {
                    final Element contentDiv = DOM.createDiv();
                    DOM.setStyleAttribute(container, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                            + "px");
                    DOM.setElementProperty(contentDiv, "className", CLASSNAME + "-float");
                    DOM.setInnerText(contentDiv, uidl.getStringAttribute("rowCaption"));
                    DOM.appendChild(container, groupCell);
                    DOM.appendChild(container, contentDiv);
                } else {
                    DOM.setStyleAttribute(container, "marginLeft", (getLevel() + 1) * LEVEL_STEP_SIZE
                            + "px");
                    DOM.setInnerText(container, uidl.getStringAttribute("rowCaption"));
                }

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);
            }

            @Override
            protected void moveCol(int oldIndex, int newIndex) {
            }
        }

        public class IScrollTreeTableRow
                extends IScrollTableBody.IScrollTableRow
        {
            private boolean expanded;
            private int level;

            protected Element groupCell = null;

            public IScrollTreeTableRow(UIDL uidl, char[] aligns) {
                super(uidl.getIntAttribute("key"));

                String rowStyle = uidl.getStringAttribute("rowstyle");
                if (rowStyle != null) {
                    addStyleName(CLASSNAME + "-row-" + rowStyle);
                }

                tHead.getColumnAlignments();
                int col = 0;

                // row header
                if (showRowHeaders) {
                    addCell(buildCaptionHtmlSnippet(uidl), aligns[col], "", col,
                            true);
                    col++;
                }

                if (uidl.hasAttribute("al")) {
                    actionKeys = uidl.getStringArrayAttribute("al");
                }

                if (uidl.hasAttribute("children") && uidl.getIntAttribute("children") > 0) {
                    groupCell = createGroupContainer();
                    if (uidl.hasAttribute("expanded")) {
                        expanded = true;
                    }
                }

                level = uidl.getIntAttribute("level");

                addCells(uidl, col);

                if (uidl.hasAttribute("selected") && !isSelected()) {
                    toggleSelection();
                }
            }

            @Override
            public void addCell(String text, char align, String style, int col, boolean textIsHTML) {
                // String only content is optimized by not using Label widget
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();
                String classNameTd = CLASSNAME + "-cell";
                String className = CLASSNAME + "-cell-content";
                if (allowMultiStingCells) {
                    classNameTd += " " + CLASSNAME + "-cell-wrap";
                }
                String classNameTdExt = null;
                if (style != null && !style.equals("")) {
                    className += " " + CLASSNAME + "-cell-content-" + style;
                    classNameTdExt = CLASSNAME + "-cell-" + style;
                }
                if (classNameTdExt != null) {
                    classNameTd += " " + classNameTdExt;
                }
                DOM.setElementProperty(td, "className", classNameTd);
                DOM.setElementProperty(container, "className", className);

                Element contentDiv = container;

                if (col == groupColIndex) {
                    if (groupCell != null) {
                        contentDiv = DOM.createDiv();

                        DOM.setStyleAttribute(container, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                                + "px");

                        DOM.setElementProperty(contentDiv, "className", CLASSNAME + "-float");
                        DOM.appendChild(container, groupCell);
                        DOM.appendChild(container, contentDiv);
                    } else {
                        DOM.setStyleAttribute(container, "marginLeft", (getLevel() + 1) * LEVEL_STEP_SIZE
                                + "px");
                    }
                }

                setCellContent(contentDiv, text, textIsHTML);
                setCellAlignment(contentDiv, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);
            }

            @Override
            public void addCell(Widget w, char align, String style, int col) {
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();
                String classNameTd = CLASSNAME + "-cell";
                String className = CLASSNAME + "-cell-content";
                if (allowMultiStingCells) {
                    classNameTd += " " + CLASSNAME + "-cell-wrap";
                }
                String classNameTdExt = null;
                if (style != null && !style.equals("")) {
                    className += " " + CLASSNAME + "-cell-content-" + style;
                    classNameTdExt = CLASSNAME + "-cell-" + style;
                }
                if (classNameTdExt != null) {
                    classNameTd += " " + classNameTdExt;
                }
                DOM.setElementProperty(td, "className", classNameTd);
                DOM.setElementProperty(container, "className", className);


                Element contentDiv = container;

                if (col == groupColIndex) {
                    if (groupCell != null) {
                        contentDiv = DOM.createDiv();

                        DOM.setStyleAttribute(groupCell, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                                + "px");

                        DOM.setElementProperty(contentDiv, "className", CLASSNAME + "-float");
                        DOM.appendChild(container, groupCell);
                        DOM.appendChild(container, contentDiv);
                    } else {
                        DOM.setStyleAttribute(container, "marginLeft", (getLevel() + 1) * LEVEL_STEP_SIZE
                                + "px");
                    }
                }

                setCellAlignment(contentDiv, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);

                setCellContent(contentDiv, w, col);
            }

            public boolean isExpanded() {
                return expanded;
            }

            public int getLevel() {
                return level;
            }

            protected Element createGroupContainer() {
                Element groupContainer = DOM.createDiv();
                DOM.setInnerHTML(groupContainer, "&nbsp;");
                DOM.setElementProperty(groupContainer, "className", CLASSNAME + "-group-cell");
                return groupContainer;
            }

            @Override
            public RenderSpace getAllocatedSpace(Widget child) {
                int w = 0;
                int i = getColIndexOf(child);
                HeaderCell headerCell = tHead.getHeaderCell(i);
                if (headerCell != null) {
                    if (initializedAndAttached) {
                        w = headerCell.getWidth() - CELL_CONTENT_PADDING;
                    } else {
                        // header offset width is not absolutely correct value,
                        // but
                        // a best guess (expecting similar content in all
                        // columns ->
                        // if one component is relative width so are others)
                        w = headerCell.getOffsetWidth() - CELL_CONTENT_PADDING;
                    }
                }

                if (i == groupColIndex) {
                    w -= (getLevel() + 1) * LEVEL_STEP_SIZE;
                }

                return new RenderSpace(w, getRowHeight());
            }

            @Override
            public void onBrowserEvent(Event event) {
//                final Element tdOrTr = DOM.getParent(DOM.eventGetTarget(event));
//                Element parentElement = DOM.getParent(tdOrTr);
//                if (getElement() == tdOrTr
//                        || getElement() == parentElement
//                        || (parentElement != null && getElement() == DOM.getParent(parentElement))) {
                    switch (DOM.eventGetType(event)) {
                        case Event.ONCLICK:
                            handleClickEvent(event);
                            handleRowClick(event);
                            break;
                        case Event.ONDBLCLICK:
                            handleClickEvent(event);
                            break;
                        case Event.ONCONTEXTMENU:
                            showContextMenu(event);
                            break;
                        default:
                            break;
                    }
//                }
            }

            public boolean hasChildren() {
                return (groupCell != null);
            }

            protected void handleRowClick(Event event) {
                if (groupCell != null
                        && DOM.eventGetTarget(event) == groupCell) {
                    if (isExpanded()) {
                        client.updateVariable(paintableId, "collapse", getKey(), true);
                    } else {
                        client.updateVariable(paintableId, "expand", getKey(), true);
                    }
                    DOM.eventCancelBubble(event, true);
                } else if (selectMode > Table.SELECT_MODE_NONE) {
                    toggleSelection();
                    // Note: changing the immediateness of this might
                    // require changes to "clickEvent" immediateness
                    // also.
                    client.updateVariable(paintableId, "selected",
                            selectedRowKeys.toArray(), immediate);
                }
            }
        }
    }
}
