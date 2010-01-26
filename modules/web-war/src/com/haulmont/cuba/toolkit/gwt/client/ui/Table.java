/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2009 18:22:05
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.toolkit.gwt.client.ColumnWidth;
import com.haulmont.cuba.toolkit.gwt.client.Tools;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.*;

import java.util.*;

public abstract class Table extends FlowPanel implements com.vaadin.terminal.gwt.client.ui.Table {
    public static final String CLASSNAME = "v-table";

    /**
     * Amount of padding inside one table cell (this is reduced from the
     * "cellContent" element's width). You may override this in your own
     * widgetset.
     */
    public static final int CELL_CONTENT_PADDING = 8;

    public static final char ALIGN_CENTER = 'c';
    public static final char ALIGN_LEFT = 'b';
    public static final char ALIGN_RIGHT = 'e';
    protected int pageLength = 15;

    protected boolean showRowHeaders = false;

    protected String[] columnOrder;

    protected ApplicationConnection client;
    protected String paintableId;

    protected boolean immediate;

    protected int selectMode = com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE;

    protected final Set<Object> selectedRowKeys = new HashSet<Object>();

    protected boolean initializedAndAttached = false;

    protected final TableHead tHead;

    protected final ScrollPanel bodyContainer = new ScrollPanel();

    protected int totalRows;

    protected Set<String> collapsedColumns;

    protected ITableBody tBody;

    protected boolean sortAscending;
    protected String sortColumn;
    protected boolean columnReordering;

    /**
     * This map contains captions and icon urls for actions like: * "33_c" ->
     * "Edit" * "33_i" -> "http://dom.com/edit.png"
     */
    protected final HashMap actionMap = new HashMap();
    protected String[] visibleColOrder;
    protected boolean initialContentReceived = false;
    protected Element scrollPositionElement;
    protected boolean enabled;
    protected boolean showColHeaders;

    /** flag to indicate that table body has changed */
    protected boolean isNewBody = true;

    protected boolean emitClickEvents;

    /*
     * Read from the "recalcWidths" -attribute. When it is true, the table will
     * recalculate the widths for columns - desirable in some cases. For #1983,
     * marked experimental.
     */
    protected boolean recalcWidths = false;

    protected int scrollbarWidthReservedInColumn = -1;
    protected int scrollbarWidthReserved = -1;
    protected boolean relativeWidth = false;

    protected int calculatedWidth = -1;

    protected final ArrayList lazyUnregistryBag = new ArrayList();
    protected String height;
    protected String width = "";

    protected boolean allowMultiStingCells = false;
    protected boolean nullSelectionDisallowed = false;

    protected boolean storeColWidth = false;

    protected AggregationRow aggregationRow = null;

    protected Table() {
        tHead = createHead();
        bodyContainer.setStyleName(CLASSNAME + "-body");
        setStyleName(CLASSNAME);
        add(tHead);
        add(bodyContainer);
    }

    protected abstract ITableBody createBody();

    protected abstract TableHead createHead();

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getStringAttribute("id");

        updateFromUIDL(uidl);

    }

    protected void updateFromUIDL(UIDL uidl) {
        if (uidl.hasAttribute("width")) {
            relativeWidth = uidl.getStringAttribute("width").endsWith("%");
        }
        enabled = !uidl.hasAttribute("disabled");

        immediate = uidl.getBooleanAttribute("immediate");
        emitClickEvents = uidl.getBooleanAttribute("listenClicks");
        final int newTotalRows = uidl.getIntAttribute("totalrows");
        if (newTotalRows != totalRows) {
            if (tBody != null) {
                if (totalRows == 0) {
                    tHead.clear();
                }
                initializedAndAttached = false;
                initialContentReceived = false;
                isNewBody = true;
            }
            totalRows = newTotalRows;
        }

        recalcWidths = uidl.hasAttribute("recalcWidths");

        pageLength = uidl.getIntAttribute("pagelength");
        if (pageLength == 0) {
            pageLength = totalRows;
        }

        showRowHeaders = uidl.getBooleanAttribute("rowheaders");
        showColHeaders = uidl.getBooleanAttribute("colheaders");
        allowMultiStingCells = uidl.getBooleanAttribute("multistring");
        nullSelectionDisallowed = uidl.getBooleanAttribute("nullSelectionDisallowed");
        storeColWidth = uidl.getBooleanAttribute("storeColWidth");

        if (uidl.hasVariable("sortascending")) {
            sortAscending = uidl.getBooleanVariable("sortascending");
            sortColumn = uidl.getStringVariable("sortcolumn");
        }

        if (uidl.hasVariable("selected")) {
            final Set selectedKeys = uidl
                    .getStringArrayVariableAsSet("selected");
            selectedRowKeys.clear();
            for (Object selectedKey : selectedKeys) {
                selectedRowKeys.add(selectedKey);
            }
        }

        if (uidl.hasAttribute("selectmode")) {
            if (uidl.getBooleanAttribute("readonly")) {
                selectMode = com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE;
            } else if (uidl.getStringAttribute("selectmode").equals("multi")) {
                selectMode = com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_MULTI;
            } else if (uidl.getStringAttribute("selectmode").equals("single")) {
                selectMode = com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_SINGLE;
            } else {
                selectMode = com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE;
            }
        }

        if (uidl.hasVariable("columnorder")) {
            columnReordering = true;
            columnOrder = uidl.getStringArrayVariable("columnorder");
        }

        if (uidl.hasVariable("collapsedcolumns")) {
            tHead.setColumnCollapsingAllowed(true);
            collapsedColumns = uidl
                    .getStringArrayVariableAsSet("collapsedcolumns");
        } else {
            tHead.setColumnCollapsingAllowed(false);
        }

        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL c = (UIDL) it.next();
            if (c.getTag().equals("actions")) {
                updateActionMap(c);
            } else if (c.getTag().equals("visiblecolumns")) {
                tHead.updateCellsFromUIDL(c);
            }
        }

        updateHeader(uidl.getStringArrayAttribute("vcolorder"));

        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL c = (UIDL) it.next();
            if (c.getTag().equals("rows")) {
                if (tBody != null) {
                    tBody.aligns = tHead.getColumnAlignments();
                }
                updateBody(c);
            } else if (c.getTag().equals("arow")) {
                updateAggregationRow(c);
            }
        }
    }

    protected abstract void updateBody(UIDL uidl);

    protected abstract boolean updateImmediate();

    protected ITableBody.ITableRow getRenderedRowByKey(String key) {
        final Iterator it = tBody.iterator();
        ITableBody.ITableRow r;
        while (it.hasNext()) {
            r = (ITableBody.ITableRow) it.next();
            if (r.getKey().equals(key)) {
                return r;
            }
        }
        return null;
    }

    private void updateActionMap(UIDL c) {
        final Iterator it = c.getChildIterator();
        while (it.hasNext()) {
            final UIDL action = (UIDL) it.next();
            final String key = action.getStringAttribute("key");
            final String caption = action.getStringAttribute("caption");
            actionMap.put(key + "_c", caption);
            if (action.hasAttribute("icon")) {
                // TODO need some uri handling ??
                actionMap.put(key + "_i", client.translateVaadinUri(action
                        .getStringAttribute("icon")));
            }
        }
    }

    protected void updateAggregationRow(UIDL uidl) {
        if (aggregationRow == null) {
            aggregationRow = createAggregationRow(uidl);
            insert(aggregationRow, getWidgetIndex(bodyContainer));
        }
        aggregationRow.updateFromUIDL(uidl);
    }

    protected AggregationRow createAggregationRow(UIDL uidl) {
        return new AggregationRow();
    }

    public String getActionCaption(String actionKey) {
        return (String) actionMap.get(actionKey + "_c");
    }

    public String getActionIcon(String actionKey) {
        return (String) actionMap.get(actionKey + "_i");
    }

    protected void updateHeader(String[] colIds) {
        if (colIds == null) {
            return;
        }

        int visibleCols = colIds.length;
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

    }

    protected void updateHeaderColumns(String[] colIds, int colIndex) {
        int i;
        for (i = 0; i < colIds.length; i++) {
            final String cid = colIds[i];
            visibleColOrder[colIndex] = cid;
            tHead.enableColumn(cid, colIndex);
            colIndex++;
        }
    }

    /**
     * Gives correct column index for given column key ("cid" in UIDL).
     *
     * @param colKey column key
     * @return column index of visible columns, -1 if column not visible
     */
    protected int getColIndexByKey(String colKey) {
        // return 0 if asked for rowHeaders
        if ("0".equals(colKey)) {
            return 0;
        }
        for (int i = 0; i < visibleColOrder.length; i++) {
            if (visibleColOrder[i].equals(colKey)) {
                return i;
            }
        }
        return -1;
    }

    protected boolean isCollapsedColumn(String colKey) {
        return collapsedColumns != null && collapsedColumns.contains(colKey);
    }

    protected String getColKeyByIndex(int index) {
        return tHead.getHeaderCell(index).getColKey();
    }

    protected void setColWidth(int colIndex, int w) {
        final HeaderCell cell = tHead.getHeaderCell(colIndex);
        cell.setWidth(w);
        tBody.setColWidth(colIndex, w);
        if (aggregationRow != null && aggregationRow.initialized) {
            aggregationRow.setColWidth(colIndex, w);
        }
    }

    protected int getColWidth(String colKey) {
        return tHead.getHeaderCell(colKey).getWidth();
    }

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
        // add back hidden rows,
        for (final String aColumnOrder : columnOrder) {
            if (aColumnOrder.equals(oldKeyOnNewIndex)) {
                break; // break loop at target
            }
            if (isCollapsedColumn(aColumnOrder)) {
                newIndex++;
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
        int i = showRowHeaders ? 1 : 0;
        for (final String cid : newOrder) {
            if (!isCollapsedColumn(cid)) {
                visibleColOrder[i++] = cid;
            }
        }
        client.updateVariable(paintableId, "columnorder", columnOrder, updateImmediate());
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if (initialContentReceived) {
            sizeInit();
        }
    }

    /**
     * Run only once when component is attached and received its initial
     * content. This function : * Syncs headers and bodys "natural widths and
     * saves the values. * Sets proper width and height * Makes deferred request
     * to get some cache rows
     */
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

        final int[] widths = new int[tHead.visibleCells.size()];

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
                w = (hw > cw ? hw : cw) + ITableBody.CELL_EXTRA_WIDTH;
            }
            widths[i] = w;
            total += w;
            i++;
        }

        tHead.disableBrowserIntelligence();

        int scrollbarWidth = Util.getNativeScrollbarSize();

        // fix "natural" width if width not set
        if (width == null || "".equals(width)) {
            setContentWidth(total);
        }

        int availW = tBody.getAvailableWidth();
        // Hey IE, are you really sure about this?
        availW = tBody.getAvailableWidth() - scrollbarWidth;

        boolean needsReLayout = false;

        if (availW > total || allowMultiStingCells/*fix an issue with the scrollbar appearing*/) {
            // natural size is smaller than available space
            int extraSpace = availW - total;
            int totalWidthR = total - totalExplicitColumnsWidths;
            if (totalWidthR > 0) {
                needsReLayout = true;

                /*
                 * If the table has a relative width and there is enough space
                 * for a scrollbar we reserve this in the last column
                 */
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
            calculatedWidth = total;
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

        // fix "natural" height if height not set
        if (height == null || "".equals(height)) {
            int bodyHeight;
            if (!allowMultiStingCells) {
                bodyHeight = tBody.getRowHeight() *
                    (totalRows < pageLength ? ((totalRows < 1) ? 1 : totalRows) : pageLength);
            } else {
                tBody.setContainerHeight();
                bodyHeight = tBody.getContainerHeight();
                if (bodyHeight == 0) {
                    bodyHeight = ITableBody.DEFAULT_ROW_HEIGHT;
                }
            }
            if (total + scrollbarWidth >= availW) {
                bodyHeight = bodyHeight + scrollbarWidth; //fix an issue with a horizontal scrollbar;
            }

            //It should fix an issue with a vertical scrollbar in Chrome
            int h = bodyContainer.getOffsetHeight();
            if (h > bodyHeight) {
                bodyHeight = h;
            }

            bodyContainer.setHeight(bodyHeight + "px");
        }

        if (needsReLayout) {
            tBody.reLayoutComponents();
        }

        isNewBody = false;

        initializedAndAttached = true;
    }

    public class HeaderCell extends Widget {

        private static final int DRAG_WIDGET_WIDTH = 4;

        private static final int MINIMUM_COL_WIDTH = 20;

        Element td = DOM.createTD();

        Element captionContainer = DOM.createDiv();

        Element colResizeWidget = DOM.createDiv();

        Element floatingCopyOfHeaderCell;

        protected boolean sortable = false;
        private final String cid;
        private boolean dragging;

        private int dragStartX;
        private int colIndex;
        private int originalWidth;

        private boolean isResizing;

        private int headerX;

        protected boolean moved;

        private int closestSlot;

        private int width = -1;

        private char align = ALIGN_LEFT;

        public void setSortable(boolean b) {
            sortable = b;
        }

        public HeaderCell(String colId, String headerText) {
            cid = colId;

            DOM.setElementProperty(colResizeWidget, "className", CLASSNAME
                    + "-resizer");
            DOM.setStyleAttribute(colResizeWidget, "width", DRAG_WIDGET_WIDTH
                    + "px");
            DOM.sinkEvents(colResizeWidget, Event.MOUSEEVENTS);

            setText(headerText);

            DOM.appendChild(td, colResizeWidget);

            DOM.setElementProperty(captionContainer, "className", CLASSNAME
                    + "-caption-container");

            // ensure no clipping initially (problem on column additions)
            DOM.setStyleAttribute(captionContainer, "overflow", "visible");

            DOM.sinkEvents(captionContainer, Event.MOUSEEVENTS);

            DOM.appendChild(td, captionContainer);

            DOM.sinkEvents(td, Event.MOUSEEVENTS);

            setElement(td);
        }

        public void setWidth(int w) {
            if (width == -1) {
                // go to default mode, clip content if necessary
                DOM.setStyleAttribute(captionContainer, "overflow", "");
            }
            width = w;
            if (w == -1) {
                DOM.setStyleAttribute(captionContainer, "width", "");
                setWidth("");
            } else {
                DOM.setStyleAttribute(captionContainer, "width", (w
                        - DRAG_WIDGET_WIDTH - 4)
                        + "px");
                setWidth(w + "px");
            }
        }

        public int getWidth() {
            return width;
        }

        public void setText(String headerText) {
            DOM.setInnerHTML(captionContainer, headerText);
        }

        public String getColKey() {
            return cid;
        }

        protected void setSorted(boolean sorted) {
            if (sorted) {
                if (sortAscending) {
                    this.setStyleName(CLASSNAME + "-header-cell-asc");
                } else {
                    this.setStyleName(CLASSNAME + "-header-cell-desc");
                }
            } else {
                this.setStyleName(CLASSNAME + "-header-cell");
            }
        }

        /**
         * Handle column reordering.
         */
        @Override
        public void onBrowserEvent(Event event) {
            if (enabled && event != null) {
                if (isResizing || event.getEventTarget().cast() == colResizeWidget) {
                    onResizeEvent(event);
                } else {
                    handleCaptionEvent(event);
                }
            }
        }

        private void createFloatingCopy() {
            floatingCopyOfHeaderCell = DOM.createDiv();
            DOM.setInnerHTML(floatingCopyOfHeaderCell, DOM.getInnerHTML(td));
            floatingCopyOfHeaderCell = DOM
                    .getChild(floatingCopyOfHeaderCell, 1);
            DOM.setElementProperty(floatingCopyOfHeaderCell, "className",
                    CLASSNAME + "-header-drag");
            updateFloatingCopysPosition(DOM.getAbsoluteLeft(td), DOM
                    .getAbsoluteTop(td));
            DOM.appendChild(RootPanel.get().getElement(),
                    floatingCopyOfHeaderCell);
        }

        private void updateFloatingCopysPosition(int x, int y) {
            x -= DOM.getElementPropertyInt(floatingCopyOfHeaderCell,
                    "offsetWidth") / 2;
            DOM.setStyleAttribute(floatingCopyOfHeaderCell, "left", x + "px");
            if (y > 0) {
                DOM.setStyleAttribute(floatingCopyOfHeaderCell, "top", (y + 7)
                        + "px");
            }
        }

        private void hideFloatingCopy() {
            DOM.removeChild(RootPanel.get().getElement(),
                    floatingCopyOfHeaderCell);
            floatingCopyOfHeaderCell = null;
        }

        protected void handleCaptionEvent(Event event) {
            switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
                ApplicationConnection.getConsole().log(
                        "HeaderCaption: mouse down");
                if (columnReordering) {
                    dragging = true;
                    moved = false;
                    colIndex = getColIndexByKey(cid);
                    DOM.setCapture(getElement());
                    headerX = tHead.getAbsoluteLeft();
                    ApplicationConnection
                            .getConsole()
                            .log(
                                    "HeaderCaption: Caption set to capture mouse events");
                    DOM.eventPreventDefault(event); // prevent selecting text
                }
                break;
            case Event.ONMOUSEUP:
                ApplicationConnection.getConsole()
                        .log("HeaderCaption: mouseUP");
                if (columnReordering) {
                    dragging = false;
                    DOM.releaseCapture(getElement());
                    ApplicationConnection.getConsole().log(
                            "HeaderCaption: Stopped column reordering");
                    if (moved) {
                        hideFloatingCopy();
                        tHead.removeSlotFocus();
                        if (closestSlot != colIndex
                                && closestSlot != (colIndex + 1)) {
                            if (closestSlot > colIndex) {
                                reOrderColumn(cid, closestSlot - 1);
                            } else {
                                reOrderColumn(cid, closestSlot);
                            }
                        }
                    }
                }

                if (!moved) {
                    // mouse event was a click to header -> sort column
                    if (sortable) {
                        if (sortColumn.equals(cid)) {
                            // just toggle order
                            client.updateVariable(paintableId, "sortascending",
                                    !sortAscending, true);
                        } else {
                            // set table scrolled by this column
                            client.updateVariable(paintableId, "sortcolumn",
                                    cid, true);
                        }
                        // get also cache columns at the same request
                        bodyContainer.setScrollPosition(0);
                    }
                    break;
                }
                break;
            case Event.ONMOUSEMOVE:
                if (dragging) {
                    ApplicationConnection.getConsole().log(
                            "HeaderCaption: Dragging column, optimal index...");
                    if (!moved) {
                        createFloatingCopy();
                        moved = true;
                    }
                    final int x = DOM.eventGetClientX(event)
                            + DOM.getElementPropertyInt(tHead.hTableWrapper,
                                    "scrollLeft");
                    int slotX = headerX;
                    closestSlot = colIndex;
                    int closestDistance = -1;
                    int start = 0;
                    if (showRowHeaders) {
                        start++;
                    }
                    final int visibleCellCount = tHead.getVisibleCellCount();
                    for (int i = start; i <= visibleCellCount; i++) {
                        if (i > 0) {
                            final String colKey = getColKeyByIndex(i - 1);
                            slotX += getColWidth(colKey);
                        }
                        final int dist = Math.abs(x - slotX);
                        if (closestDistance == -1 || dist < closestDistance) {
                            closestDistance = dist;
                            closestSlot = i;
                        }
                    }
                    tHead.focusSlot(closestSlot);

                    updateFloatingCopysPosition(DOM.eventGetClientX(event), -1);
                    ApplicationConnection.getConsole().log("" + closestSlot);
                }
                break;
            default:
                break;
            }
        }

        protected void onResizeEvent(Event event) {
            switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
                isResizing = true;
                DOM.setCapture(getElement());
                dragStartX = DOM.eventGetClientX(event);
                colIndex = getColIndexByKey(cid);
                originalWidth = getWidth();
                DOM.eventPreventDefault(event);
                break;
            case Event.ONMOUSEUP:
                isResizing = false;
                DOM.releaseCapture(getElement());
                handleColResize();
                tBody.reLayoutComponents();
                break;
            case Event.ONMOUSEMOVE:
                if (isResizing) {
                    final int deltaX = DOM.eventGetClientX(event) - dragStartX;
                    if (deltaX == 0) {
                        return;
                    }

                    int newWidth = originalWidth + deltaX;
                    if (newWidth < MINIMUM_COL_WIDTH) {
                        newWidth = MINIMUM_COL_WIDTH;
                    }
                    updateCalculatedWidth(colIndex, newWidth);
                    setColWidth(colIndex, newWidth);
                }
                break;
            default:
                break;
            }
        }

        protected void handleColResize() {
            if (storeColWidth) {
                final ColumnWidth colWidth = new ColumnWidth(cid, getColWidth(cid));
                client.updateVariable(paintableId, "colwidth", colWidth.toString(),
                        true);
            }
        }

        private void updateCalculatedWidth(int colIndex, int newColumnWidth) {
            if (calculatedWidth > -1) {
                int newWidth = 0;
                for (int i = 0; i < tHead.getVisibleCellCount(); i++) {
                    if (i == colIndex) {
                        newWidth += newColumnWidth;
                    } else {
                        HeaderCell cell = (HeaderCell) tHead.getVisibleCells().get(i);
                        newWidth += cell.getWidth();
                    }
                }
                calculatedWidth = newWidth;
            }
        }

        public String getCaption() {
            return DOM.getInnerText(captionContainer);
        }

        public boolean isEnabled() {
            return getParent() != null;
        }

        public void setAlign(char c) {
            if (align != c) {
                switch (c) {
                case ALIGN_CENTER:
                    DOM.setStyleAttribute(captionContainer, "textAlign",
                            "center");
                    break;
                case ALIGN_RIGHT:
                    DOM.setStyleAttribute(captionContainer, "textAlign",
                            "right");
                    break;
                default:
                    DOM.setStyleAttribute(captionContainer, "textAlign", "");
                    break;
                }
            }
            align = c;
        }

        public char getAlign() {
            return align;
        }

    }

    /**
     * HeaderCell that is header cell for row headers.
     *
     * Reordering disabled and clicking on it resets sorting.
     */
    public class RowHeadersHeaderCell extends HeaderCell {

        RowHeadersHeaderCell() {
            super("0", "");
        }

        @Override
        protected void handleCaptionEvent(Event event) {
            // NOP: RowHeaders cannot be reordered
            // TODO It'd be nice to reset sorting here
        }
    }

    public class TableHead extends Panel implements ActionOwner {

        private static final int WRAPPER_WIDTH = 9000;

        protected Vector<Widget> visibleCells = new Vector<Widget>();

        protected HashMap<String, HeaderCell> availableCells = new HashMap<String, HeaderCell>();

        Element div = DOM.createDiv();
        Element hTableWrapper = DOM.createDiv();
        Element hTableContainer = DOM.createDiv();
        Element table = DOM.createTable();
        Element headerTableBody = DOM.createTBody();
        Element tr = DOM.createTR();

        private final Element columnSelector = DOM.createDiv();

        private int focusedSlot = -1;

        public TableHead() {
            DOM.setStyleAttribute(hTableWrapper, "overflow", "hidden");
            DOM.setElementProperty(hTableWrapper, "className", CLASSNAME
                    + "-header");

            DOM.setElementProperty(columnSelector, "className", CLASSNAME
                    + "-column-selector");
            DOM.setStyleAttribute(columnSelector, "display", "none");

            DOM.setElementProperty(hTableContainer, "className", CLASSNAME
                    + "-header-container");

            DOM.appendChild(table, headerTableBody);
            DOM.appendChild(headerTableBody, tr);
            DOM.appendChild(hTableContainer, table);
            DOM.appendChild(hTableWrapper, hTableContainer);
            DOM.appendChild(div, hTableWrapper);
            DOM.appendChild(div, columnSelector);
            setElement(div);

            setStyleName(CLASSNAME + "-header-wrap");

            DOM.sinkEvents(columnSelector, Event.ONCLICK);

            availableCells.put("0", new RowHeadersHeaderCell());
        }

        @Override
        public void clear() {
            for (String cid : availableCells.keySet()) {
                removeCell(cid);
            }
            availableCells.clear();
            availableCells.put("0", new RowHeadersHeaderCell());
        }

        public void updateCellsFromUIDL(UIDL uidl) {
            Iterator it = uidl.getChildIterator();
            HashSet<String> updated = new HashSet<String>();
            updated.add("0");
            while (it.hasNext()) {
                final UIDL col = (UIDL) it.next();
                final String cid = col.getStringAttribute("cid");
                updated.add(cid);

                String caption = buildCaptionHtmlSnippet(col);
                HeaderCell c = getHeaderCell(cid);
                if (c == null) {
                    c = createHeaderCell(cid, caption);
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

        protected HeaderCell createHeaderCell(String cid, String caption) {
            return new HeaderCell(cid, caption);
        }

        public void enableColumn(String cid, int index) {
            final HeaderCell c = getHeaderCell(cid);
            if (!c.isEnabled() || getHeaderCell(index) != c) {
                setHeaderCell(index, c);
                if (c.getWidth() == -1) {
                    if (initializedAndAttached) {
                        // column is not drawn before,
                        // we will need a column width recalculation
                        initializedAndAttached = false;
                        initialContentReceived = false;
                        isNewBody = true;
                    }
                }
            }
        }

        public Vector<Widget> getVisibleCells() {
            return visibleCells;
        }

        public int getVisibleCellCount() {
            return visibleCells.size();
        }

        public void setHorizontalScrollPosition(int scrollLeft) {
            DOM.setElementPropertyInt(hTableWrapper, "scrollLeft", scrollLeft);
        }

        public void setColumnCollapsingAllowed(boolean cc) {
            if (cc) {
                DOM.setStyleAttribute(columnSelector, "display", "block");
            } else {
                DOM.setStyleAttribute(columnSelector, "display", "none");
            }
        }

        public void disableBrowserIntelligence() {
            DOM.setStyleAttribute(hTableContainer, "width", WRAPPER_WIDTH
                    + "px");
        }

        public void enableBrowserIntelligence() {
            DOM.setStyleAttribute(hTableContainer, "width", "");
        }

        public void setHeaderCell(int index, HeaderCell cell) {
            if (cell.isEnabled()) {
                // we're moving the cell
                DOM.removeChild(tr, cell.getElement());
                orphan(cell);
            }
            if (index < visibleCells.size()) {
                // insert to right slot
                DOM.insertChild(tr, cell.getElement(), index);
                adopt(cell);
                visibleCells.insertElementAt(cell, index);

            } else if (index == visibleCells.size()) {
                // simply append
                DOM.appendChild(tr, cell.getElement());
                adopt(cell);
                visibleCells.add(cell);
            } else {
                throw new RuntimeException(
                        "Header cells must be appended in order");
            }
        }

        public HeaderCell getHeaderCell(int index) {
            if (index < visibleCells.size()) {
                return (HeaderCell) visibleCells.get(index);
            } else {
                return null;
            }
        }

        /**
         * Get's HeaderCell by it's column Key.
         *
         * Note that this returns HeaderCell even if it is currently collapsed.
         *
         * @param cid
         *            Column key of accessed HeaderCell
         * @return HeaderCell
         */
        public HeaderCell getHeaderCell(String cid) {
            return availableCells.get(cid);
        }

        public void moveCell(int oldIndex, int newIndex) {
            final HeaderCell hCell = getHeaderCell(oldIndex);
            final Element cell = hCell.getElement();

            visibleCells.remove(oldIndex);
            DOM.removeChild(tr, cell);

            DOM.insertChild(tr, cell, newIndex);
            visibleCells.insertElementAt(hCell, newIndex);
        }

        public Iterator<Widget> iterator() {
            return visibleCells.iterator();
        }

        @Override
        public boolean remove(Widget w) {
            if (visibleCells.contains(w)) {
                visibleCells.remove(w);
                orphan(w);
                DOM.removeChild(DOM.getParent(w.getElement()), w.getElement());
                return true;
            }
            return false;
        }

        public void removeCell(String colKey) {
            final HeaderCell c = getHeaderCell(colKey);
            remove(c);
        }

        private void focusSlot(int index) {
            removeSlotFocus();
            if (index > 0) {
                DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(tr,
                        index - 1)), "className", CLASSNAME + "-resizer "
                        + CLASSNAME + "-focus-slot-right");
            } else {
                DOM.setElementProperty(DOM.getFirstChild(DOM
                        .getChild(tr, index)), "className", CLASSNAME
                        + "-resizer " + CLASSNAME + "-focus-slot-left");
            }
            focusedSlot = index;
        }

        private void removeSlotFocus() {
            if (focusedSlot < 0) {
                return;
            }
            if (focusedSlot == 0) {
                DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(tr,
                        focusedSlot)), "className", CLASSNAME + "-resizer");
            } else if (focusedSlot > 0) {
                DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(tr,
                        focusedSlot - 1)), "className", CLASSNAME + "-resizer");
            }
            focusedSlot = -1;
        }

        @Override
        public void onBrowserEvent(Event event) {
            if (enabled) {
                if (event.getEventTarget().cast() == columnSelector) {
                    final int left = DOM.getAbsoluteLeft(columnSelector);
                    final int top = DOM.getAbsoluteTop(columnSelector)
                            + DOM.getElementPropertyInt(columnSelector,
                                    "offsetHeight");
                    client.getContextMenu().showAt(this, left, top);
                }
            }
        }

        protected class VisibleColumnAction extends Action {
            String colKey;
            private boolean collapsed;

            public VisibleColumnAction(String colKey) {
                super(Table.TableHead.this);
                this.colKey = colKey;
                caption = tHead.getHeaderCell(colKey).getCaption();
            }

            @Override
            public void execute() {
                client.getContextMenu().hide();
                // toggle selected column
                if (collapsedColumns.contains(colKey)) {
                    collapsedColumns.remove(colKey);
                } else {
                    tHead.removeCell(colKey);
                    collapsedColumns.add(colKey);
                }

                // update variable to server
                client.updateVariable(paintableId, "collapsedcolumns",
                        collapsedColumns.toArray(), updateImmediate());
            }

            public void setCollapsed(boolean b) {
                collapsed = b;
            }

            /**
             * Override default method to distinguish on/off columns
             */
            @Override
            public String getHTML() {
                final StringBuffer buf = new StringBuffer();
                if (collapsed) {
                    buf.append("<span class=\"v-off\">");
                } else {
                    buf.append("<span class=\"v-on\">");
                }
                buf.append(super.getHTML());
                buf.append("</span>");

                return buf.toString();
            }

        }

        /*
         * Returns columns as Action array for column select popup
         */
        public Action[] getActions() {
            final Object[] cols = getActionColumns();
            final Action[] actions = new Action[cols.length];

            for (int i = 0; i < cols.length; i++) {
                final String cid = (String) cols[i];
                final HeaderCell c = getHeaderCell(cid);
                final VisibleColumnAction a = createColumnAction(c
                        .getColKey());
                a.setCaption(c.getCaption());
                if (!c.isEnabled()) {
                    a.setCollapsed(true);
                }
                actions[i] = a;
            }
            return actions;
        }

        protected Object[] getActionColumns() {
            Object[] cols;
            if (columnReordering) {
                cols = columnOrder;
            } else {
                // if columnReordering is disabled, we need different way to get
                // all available columns
                cols = new Object[visibleColOrder.length
                        + collapsedColumns.size()];
                int i;
                for (i = 0; i < visibleColOrder.length; i++) {
                    cols[i] = visibleColOrder[i];
                }
                for (Object collapsedColumn : collapsedColumns) {
                    cols[i++] = collapsedColumn;
                }
            }
            return cols;
        }

        protected VisibleColumnAction createColumnAction(String key) {
            return new VisibleColumnAction(key);
        }

        public ApplicationConnection getClient() {
            return client;
        }

        public String getPaintableId() {
            return paintableId;
        }

        /*
         * Returns column alignments for visible columns
         */
        public char[] getColumnAlignments() {
            final Iterator it = visibleCells.iterator();
            final char[] aligns = new char[visibleCells.size()];
            int colIndex = 0;
            while (it.hasNext()) {
                aligns[colIndex++] = ((HeaderCell) it.next()).getAlign();
            }
            return aligns;
        }

    }

    protected abstract class ITableBody extends Panel {
        public static final int CELL_EXTRA_WIDTH = 20;

        public static final int DEFAULT_ROW_HEIGHT = 24;

        private int rowHeight = -1;

        protected final List<Widget> renderedRows = new Vector<Widget>();

        protected boolean initDone = false;

        protected Element container = DOM.createDiv();

        protected Element tBody = DOM.createTBody();
        protected Element table = DOM.createTable();

        protected char[] aligns;

        public ITableBody() {
            setElement(container);
            aligns = tHead.getColumnAlignments();
        }

        public abstract int getAvailableWidth();

        protected abstract ITableRow createRowInstance(UIDL uidl);

        protected abstract ITableRow createRow(UIDL uidl);

        @Override
        public boolean remove(Widget w) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void onAttach() {
            super.onAttach();
            setContainerHeight();
        }

        public Iterator iterator() {
            return renderedRows.iterator();
        }

        protected int containerHeight = -1;
        /**
         * Fix container blocks height according to totalRows to avoid
         * "bouncing" when scrolling
         */
        public void setContainerHeight() {
            if (!allowMultiStingCells) {
                containerHeight = totalRows * getRowHeight();
            } else {
                containerHeight = 0;
                for (final Object o : renderedRows) {
                    final ITableRow row = (ITableRow) o;
                    containerHeight += row.getHeight();
                }
            }
            DOM.setStyleAttribute(container, "height", containerHeight + "px");
        }

        public int getContainerHeight() {
            if (containerHeight == -1) {
                setContainerHeight();
            }
            return containerHeight;
        }

        public int getRowHeight() {
            if (initDone) {
                return rowHeight;
            } else {
                if (DOM.getChildCount(tBody) > 0) {
                    ITableRow row = (ITableRow) renderedRows.get(0);
                    rowHeight = row.getHeight();
                } else {
                    return DEFAULT_ROW_HEIGHT;
                }
                initDone = true;
                return rowHeight;
            }
        }

        public int getColWidth(int i) {
            if (initDone) {
                final Element e = DOM.getChild(DOM.getChild(tBody, 0), i);
                return DOM.getElementPropertyInt(e, "offsetWidth");
            } else {
                return 0;
            }
        }

        public void setColWidth(int colIndex, int w) {
            final int rows = DOM.getChildCount(tBody);
            for (int i = 0; i < rows; i++) {
                final Element cell = DOM.getChild(DOM.getChild(tBody, i),
                        colIndex);
                DOM.setStyleAttribute(DOM.getFirstChild(cell), "width",
                        (w - CELL_CONTENT_PADDING) + "px");
                DOM.setStyleAttribute(cell, "width", w + "px");
            }
        }

        public void reLayoutComponents() {
            for (Widget w : this) {
                ITableRow r = (ITableRow) w;
                for (Widget widget : r) {
                    client.handleComponentRelativeSize(widget);
                }
            }
        }

        public void moveCol(int oldIndex, int newIndex) {

            // loop all rows and move given index to its new place
            final Iterator rows = iterator();
            while (rows.hasNext()) {
                ((ITableRow) rows.next()).moveCol(oldIndex, newIndex);
            }

        }

        protected class ITableRow extends Panel implements ActionOwner,
                Container {
            protected Vector childWidgets = new Vector();
            private boolean selected = false;
            private final int rowKey;
            private List<UIDL> pendingComponentPaints;

            protected String[] actionKeys = null;

            protected Map widgetColumns = null;

            protected ITableRow() {
                rowKey = 0;
            }

            protected ITableRow(int rowKey) {
                this.rowKey = rowKey;
                setElement(DOM.createElement("tr"));
                DOM.sinkEvents(getElement(), Event.ONCLICK | Event.ONDBLCLICK
                        | Event.ONCONTEXTMENU);
            }

            public ITableRow(UIDL uidl, char[] aligns) {
                this(uidl.getIntAttribute("key"));

                String rowStyle = uidl.getStringAttribute("rowstyle");
                if (rowStyle != null) {
                    addStyleName(CLASSNAME + "-row-" + rowStyle);
                }

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

                addCells(uidl, col);

                if (uidl.hasAttribute("selected") && !isSelected()) {
                    toggleSelection();
                }
            }

            protected void paintComponent(Paintable p, UIDL uidl) {
                if (isAttached()) {
                    p.updateFromUIDL(uidl, client);
                } else {
                    if (pendingComponentPaints == null) {
                        pendingComponentPaints = new LinkedList<UIDL>();
                    }
                    pendingComponentPaints.add(uidl);
                }
            }

            @Override
            protected void onAttach() {
                super.onAttach();
                if (pendingComponentPaints != null) {
                    for (UIDL uidl : pendingComponentPaints) {
                        Paintable paintable = client.getPaintable(uidl);
                        paintable.updateFromUIDL(uidl, client);
                    }
                }
            }

            public String getKey() {
                return String.valueOf(rowKey);
            }

            protected void addCells(UIDL uidl, int col) {
                final Iterator cells = uidl.getChildIterator();
                while (cells.hasNext()) {
                    final Object cell = cells.next();

                    String columnId = visibleColOrder[col];

                    String style = "";
                    if (uidl.hasAttribute("style-" + columnId)) {
                        style = uidl.getStringAttribute("style-" + columnId);
                    }

                    if (cell instanceof String) {
                        addCell(cell.toString(), aligns[col], style, col, false);

                    } else {
                        final Paintable cellContent = client
                                .getPaintable((UIDL) cell);

                        addCell((Widget) cellContent, aligns[col], style, col);
                        paintComponent(cellContent, (UIDL) cell);
                    }
                    col++;
                }
            }

            public void addCell(String text, char align, String style, int col,
                    boolean textIsHTML) {
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

                setCellText(container, text, textIsHTML);
                setCellAlignment(container, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);
            }

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
                // TODO most components work with this, but not all (e.g.
                // Select)
                // Old comment: make widget cells respect align.
                // text-align:center for IE, margin: auto for others

                setCellAlignment(container, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);

                setCellWidget(container, w, col);
            }

            protected void moveCol(int oldIndex, int newIndex) {
                final Element td = DOM.getChild(getElement(), oldIndex);
                DOM.removeChild(getElement(), td);

                DOM.insertChild(getElement(), td, newIndex);
            }

            public int getHeight() {
                return DOM.getChild(getElement(), 0).getOffsetHeight();
            }

            protected void setCellWidget(Element container, Widget w, int colIndex) {
                // ensure widget not attached to another element (possible tBody
                // change)
                w.removeFromParent();
                DOM.appendChild(container, w.getElement());
                adopt(w);
                childWidgets.add(w);
                if (widgetColumns == null) {
                    widgetColumns = new HashMap();
                }
                widgetColumns.put(w, colIndex);
            }

            public Iterator iterator() {
                return childWidgets.iterator();
            }

            @Override
            public boolean remove(Widget w) {
                if (childWidgets.contains(w)) {
                    orphan(w);
                    DOM.removeChild(DOM.getParent(w.getElement()), w
                            .getElement());
                    childWidgets.remove(w);
                    if (widgetColumns != null) {
                        widgetColumns.remove(w);
                    }
                    return true;
                } else {
                    return false;
                }
            }

            protected void handleClickEvent(Event event) {
                if (emitClickEvents) {
                    boolean dbl = DOM.eventGetType(event) == Event.ONDBLCLICK;
                    final Element tdOrTr = DOM.getParent(DOM
                            .eventGetTarget(event));
                    client.updateVariable(paintableId, "clickedKey", ""
                            + rowKey, false);
                    if (getElement() == tdOrTr.getParentElement()) {
                        int childIndex = DOM
                                .getChildIndex(getElement(), tdOrTr);
                        String colKey = tHead.getHeaderCell(childIndex).getColKey();
                        client.updateVariable(paintableId, "clickedColKey",
                                colKey, false);
                    }
                    MouseEventDetails details = new MouseEventDetails(event);
                    // Note: the 'immediate' logic would need to be more
                    // involved (see #2104), but iscrolltable always sends
                    // select event, even though nullselectionallowed wont let
                    // the change trough. Will need to be updated if that is
                    // changed.
                    client
                            .updateVariable(
                                    paintableId,
                                    "clickEvent",
                                    details.toString(),
                                    (dbl || selectMode > com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE || immediate));
                }
            }

            /*
             * React on click that occur on content cells only
             */
            @Override
            public void onBrowserEvent(Event event) {
//                final Element tdOrTr = DOM.getParent(DOM.eventGetTarget(event));
//                if (getElement() == tdOrTr
//                        || getElement() == tdOrTr.getParentElement()) {
                final Element targetElement = DOM.eventGetTarget(event);
                //todo gorodnov: review this code when we will be use a multi selection
                if (Tools.isCheckbox(targetElement) || Tools.isRadio(targetElement))
                    return;

                switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    handleClickEvent(event);
                    handleRowClick(event);
                    break;
                case Event.ONDBLCLICK:
                    handleClickEvent(event);
                    break;
                case Event.ONCONTEXTMENU:
                    handleRowClick(event);
                    showContextMenu(event);
                    break;
                default:
                    break;
                }
//                }
                super.onBrowserEvent(event);
            }

            protected void handleRowClick(Event event) {
                if (selectMode > com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE) {
                    if (!nullSelectionDisallowed || !isSelected()) {
                        toggleSelection();
                        // Note: changing the immediateness of this might
                        // require changes to "clickEvent" immediateness
                        // also.
                        client.updateVariable(paintableId, "selected",
                                selectedRowKeys.toArray(), immediate);
                    }
                }
            }

            public void showContextMenu(Event event) {
                if (enabled && actionKeys != null && actionKeys.length > 0) {
                    int left = event.getClientX();
                    int top = event.getClientY();
                    top += Window.getScrollTop();
                    left += Window.getScrollLeft();
                    client.getContextMenu().showAt(this, left, top);
                }
                event.cancelBubble(true);
                event.preventDefault();
            }

            public boolean isSelected() {
                return selected;
            }

            public void toggleSelection() {
                selected = !selected;
                if (selected) {
                    if (selectMode == com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_SINGLE) {
                        deselectAll();
                    }
                    selectedRowKeys.add(String.valueOf(rowKey));
                    addStyleName("v-selected");
                } else {
                    selectedRowKeys.remove(String.valueOf(rowKey));
                    removeStyleName("v-selected");
                }
            }

            /*
             * (non-Javadoc)
             *
             * @see
             * com.vaadin.terminal.gwt.client.ui.IActionOwner#getActions
             * ()
             */
            public Action[] getActions() {
                if (actionKeys == null) {
                    return new Action[] {};
                }
                final Action[] actions = new Action[actionKeys.length];
                for (int i = 0; i < actions.length; i++) {
                    final String actionKey = actionKeys[i];
                    final TreeAction a = new TreeAction(this, String
                            .valueOf(rowKey), actionKey);
                    a.setCaption(getActionCaption(actionKey));
                    a.setIconUrl(getActionIcon(actionKey));
                    actions[i] = a;
                }
                return actions;
            }

            public ApplicationConnection getClient() {
                return client;
            }

            public String getPaintableId() {
                return paintableId;
            }

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
                return new RenderSpace(w, getRowHeight());
            }

            protected int getColIndexOf(Widget child) {
                int index = -1;
                if (widgetColumns != null) {
                    Integer i = (Integer) widgetColumns.get(child);
                    if (i != null) {
                        index = i;
                    }
                }
                return index;
            }

            public boolean hasChildComponent(Widget component) {
                return childWidgets.contains(component);
            }

            public void replaceChildComponent(Widget oldComponent,
                    Widget newComponent) {
                com.google.gwt.dom.client.Element parentElement = oldComponent
                        .getElement().getParentElement();
                int index = childWidgets.indexOf(oldComponent);
                oldComponent.removeFromParent();

                parentElement.appendChild(newComponent.getElement());
                childWidgets.insertElementAt(newComponent, index);
                if (widgetColumns == null) {
                    widgetColumns = new HashMap();
                }
                widgetColumns.remove(oldComponent);
                widgetColumns.put(newComponent, index);
                adopt(newComponent);

            }

            public boolean requestLayout(Set<Paintable> children) {
                // row size should never change and system wouldn't event
                // survive as this is a kind of fake paitable
                return true;
            }

            public void updateCaption(Paintable component, UIDL uidl) {
                // NOP, not rendered
            }

            public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
                // Should never be called,
                // Component container interface faked here to get layouts
                // render properly
            }
        }
    }

    public void deselectAll() {
        final Object[] keys = selectedRowKeys.toArray();
        for (Object key : keys) {
            final ITableBody.ITableRow row = getRenderedRowByKey((String) key);
            if (row != null && row.isSelected()) {
                row.toggleSelection();
            }
        }
        // still ensure all selects are removed from (not necessary rendered)
        selectedRowKeys.clear();
    }

    @Override
    public void setWidth(String width) {
        if (this.width.equals(width)) {
            return;
        }

        this.width = width;
        if (width != null && !"".equals(width)) {
            int oldWidth = getOffsetWidth();
            super.setWidth(width);
            int newWidth = getOffsetWidth();

            if (scrollbarWidthReservedInColumn != -1 && oldWidth > newWidth
                    && (oldWidth - newWidth) < scrollbarWidthReserved) {
                int col = scrollbarWidthReservedInColumn;
                String colKey = getColKeyByIndex(col);
                setColWidth(scrollbarWidthReservedInColumn, getColWidth(colKey)
                        - (oldWidth - newWidth));
                scrollbarWidthReservedInColumn = -1;
            }

            int innerPixels = getOffsetWidth() - getBorderWidth();
            if (innerPixels < 0) {
                innerPixels = 0;
            }
            setContentWidth(innerPixels);
        } else {
            super.setWidth("");
        }
    }

    /**
     * helper to set pixel size of head and body part
     *
     * @param pixels content width in pixels
     */
    protected void setContentWidth(int pixels) {
        tHead.setWidth(pixels + "px");
        bodyContainer.setWidth(pixels + "px");
    }

    private int borderWidth = -1;

    /**
     * @return border left + border right
     */
    private int getBorderWidth() {
        if (borderWidth < 0) {
            borderWidth = Util.measureHorizontalPaddingAndBorder(bodyContainer
                    .getElement(), 2);
            if (borderWidth < 0) {
                borderWidth = 0;
            }
        }
        return borderWidth;
    }

    /**
     * Ensures scrollable area is properly sized.
     */
    protected void setContainerHeight() {
        if (height != null && !"".equals(height)) {
            int contentH = getOffsetHeight() - tHead.getOffsetHeight();
            if (aggregationRow != null) {
                contentH -= aggregationRow.getOffsetHeight();
            }
            contentH -= getContentAreaBorderHeight();
            if (contentH < 0) {
                contentH = 0;
            }
            bodyContainer.setHeight(contentH + "px");
        }
    }

    private int contentAreaBorderHeight = -1;

    /**
     * @return border top + border bottom of the scrollable area of table
     */
    protected int getContentAreaBorderHeight() {
        if (contentAreaBorderHeight < 0) {
            DOM.setStyleAttribute(bodyContainer.getElement(), "overflow",
                    "hidden");
            contentAreaBorderHeight = bodyContainer.getOffsetHeight()
                    - bodyContainer.getElement().getPropertyInt("clientHeight");
            DOM.setStyleAttribute(bodyContainer.getElement(), "overflow",
                    "auto");
        }
        return contentAreaBorderHeight;
    }

    @Override
    public void setHeight(String height) {
        this.height = height;
        super.setHeight(height);
        setContainerHeight();
    }

    /**
     * Helper function to build html snippet for column or row headers
     *
     * @param uidl
     *            possibly pwith values caption and icon
     * @return html snippet containing possibly an icon + caption text
     */
    protected String buildCaptionHtmlSnippet(UIDL uidl) {
        String s = uidl.getStringAttribute("caption");
        if (uidl.hasAttribute("icon")) {
            s = "<img src=\""
                    + client.translateVaadinUri(uidl
                            .getStringAttribute("icon"))
                    + "\" alt=\"icon\" class=\"v-icon\">" + s;
        }
        return s;
    }

    protected ITableBody getBody() {
        return tBody;
    }

    /**
     * Unregisters Paintables in "trashed" HasWidgets (IScrollTableBodys or
     * IScrollTableRows). This is done lazily as Table must survive from
     * "subtreecaching" logic.
     */
    protected void purgeUnregistryBag() {
        for (final Object aLazyUnregistryBag : lazyUnregistryBag) {
            client.unregisterChildPaintables((HasWidgets) aLazyUnregistryBag);
        }
        lazyUnregistryBag.clear();
    }

    class ActionButtonsPanel extends SimplePanel {

        private Map<Element, String> buttonKeys = new HashMap<Element, String>();

        private Element buttonsContainer = DOM.createDiv();

        public ActionButtonsPanel() {
            setStyleName(CLASSNAME + "-buttons");
            DOM.setElementProperty(buttonsContainer, "className", CLASSNAME + "-buttons-container");

            DOM.appendChild(getContainerElement(), buttonsContainer);

            DOM.sinkEvents(buttonsContainer, Event.ONCLICK);
        }

        public void updateFromUIDL(UIDL uidl) {
            if (!buttonKeys.isEmpty()) {
                buttonKeys.clear();
                Tools.removeChildren(buttonsContainer);
            }

            for (Iterator it = uidl.getChildIterator(); it.hasNext();) {
                final UIDL buttonUIDL = (UIDL) it.next();
                Element buttonElement;
                if (buttonUIDL.hasAttribute("icon")) {
                    Icon iconButton = new Icon(client);
                    iconButton.setUri(buttonUIDL.getStringAttribute("icon"));
                    buttonElement = iconButton.getElement();
                } else {
                    buttonElement = DOM.createDiv();
                }
                if (buttonUIDL.hasAttribute("caption")) {
                    buttonElement.setTitle(buttonUIDL.getStringAttribute("caption"));
                }
                DOM.setElementProperty(buttonElement, "className", CLASSNAME + "-button");
                final Element wrap = DOM.createDiv();
                DOM.setElementProperty(wrap, "className", CLASSNAME + "-button-wrap");
                DOM.appendChild(wrap, buttonElement);
                DOM.appendChild(buttonsContainer, wrap);

                buttonKeys.put(buttonElement, buttonUIDL.getStringAttribute("key"));
            }
        }

        @Override
        public void onBrowserEvent(Event event) {
            if (event != null) {
                switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        final Element targetElement = DOM.eventGetTarget(event);
                        if (buttonKeys.get(targetElement) != null) {
                            client.updateVariable(paintableId, "actionButton", buttonKeys.get(targetElement),
                                    true);
                        }
                        break;
                }
            }
        }
    }

    protected class AggregationRow extends SimplePanel {

        protected boolean initialized = false;

        protected char[] aligns;
        protected Element tr;

        public AggregationRow() {
            setStyleName(CLASSNAME + "-arow");
        }

        public void updateFromUIDL(UIDL uidl) {
            if (getContainerElement().hasChildNodes()) {
                Tools.removeChildren(getContainerElement());
            }

            aligns = tHead.getColumnAlignments();

            if (uidl.getChildCount() > 0) {
                final Element table = DOM.createTable();
                DOM.setElementAttribute(table, "cellpadding", "0");
                DOM.setElementAttribute(table, "cellspacing", "0");
                final Element tBody = DOM.createTBody();
                tr = DOM.createTR();

                DOM.setElementProperty(tr, "className", CLASSNAME + "-arow-row");

                paintRow(uidl);

                DOM.appendChild(tBody, tr);
                DOM.appendChild(table, tBody);
                DOM.appendChild(getContainerElement(), table);
            }

            initialized = getContainerElement().hasChildNodes();
        }

        protected void paintRow(UIDL uidl) {
            int col = 0;
            for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
                final String cell = (String) it.next();
                String columnId = visibleColOrder[col];
                String style = "";
                if (uidl.hasAttribute("style-" + columnId)) {
                    style = uidl.getStringAttribute("style-" + columnId);
                }
                addCell(cell, aligns[col], style);

                final String colKey = getColKeyByIndex(col);
                int colWidth;
                if ((colWidth = getColWidth(colKey)) > -1) {
                    setColWidth(col, colWidth);
                }

                col++;
            }
        }

        public void clearRow() {
            if (initialized) {
                tr = null;
                Tools.removeChildren(getContainerElement());
            }
            initialized = false;
        }

        public void moveCol(int oldIndex, int newIndex) {
            if (initialized && tr != null) {
                final Element td = DOM.getChild(tr, oldIndex);
                DOM.removeChild(tr, td);

                DOM.insertChild(tr, td, newIndex);
            }
        }

        public void setColWidth(int colIndex, int w) {
            if (initialized && tr != null) {
                final Element cell = DOM.getChild(tr, colIndex);
                DOM.setStyleAttribute(DOM.getFirstChild(cell), "width",
                        (w - CELL_CONTENT_PADDING) + "px");
                DOM.setStyleAttribute(cell, "width", w + "px");
            }
        }

        protected void addCell(String text, char align, String style) {
            final Element td = DOM.createTD();
            final Element container = DOM.createDiv();
            String classNameTd = CLASSNAME + "-cell";
            String className = CLASSNAME + "-cell-content";
            if (style != null && !style.equals("")) {
                classNameTd += " " + CLASSNAME + "-cell-" + style;
                className += " " + CLASSNAME + "-cell-content-" + style;
            }
            DOM.setElementProperty(td, "className", classNameTd);
            DOM.setElementProperty(container, "className", className);

            setCellText(container, text, false);
            setCellAlignment(container, align);

            DOM.appendChild(td, container);
            DOM.appendChild(tr, td);
        }
    }

    public static void setCellText(Element container, String text,
                                  boolean textIsHTML) {
        if (textIsHTML) {
            Tools.setInnerHTML(container, text);
        } else {
            if (text == null || "".equals(text)) {
                text = " ";
            }
            Tools.setInnerText(container, text);
        }
    }

    protected static void setCellAlignment(Element container, char align) {
        if (align != ALIGN_LEFT) {
            switch (align) {
            case ALIGN_CENTER:
                DOM.setStyleAttribute(container, "textAlign", "center");
                break;
            case ALIGN_RIGHT:
            default:
                DOM.setStyleAttribute(container, "textAlign", "right");
                break;
            }
        }
    }


}
