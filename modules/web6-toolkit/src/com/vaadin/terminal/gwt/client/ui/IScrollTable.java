/*
 * Copyright 2008 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * IScrollTable
 * <p/>
 * IScrollTable is a FlowPanel having two widgets in it: * TableHead component *
 * ScrollPanel
 * <p/>
 * TableHead contains table's header and widgets + logic for resizing,
 * reordering and hiding columns.
 * <p/>
 * ScrollPanel contains IScrollTableBody object which handles content. To save
 * some bandwidth and to improve clients responsiveness with loads of data, in
 * IScrollTableBody all rows are not necessary rendered. There are "spacers" in
 * IScrollTableBody to use the exact same space as non-rendered rows would use.
 * This way we can use seamlessly traditional scrollbars and scrolling to fetch
 * more rows instead of "paging".
 * <p/>
 * In IScrollTable we listen to scroll events. On horizontal scrolling we also
 * update TableHeads scroll position which has its scrollbars hidden. On
 * vertical scroll events we will check if we are reaching the end of area where
 * we have rows rendered and
 * <p/>
 * TODO implement unregistering for child components in Cells
 */
public class IScrollTable extends com.haulmont.cuba.toolkit.gwt.client.ui.Table {

    /**
     * multiple of pagelength which component will cache when requesting more
     * rows
     */
    protected static final double CACHE_RATE = 2;
    /**
     * fraction of pageLenght which can be scrolled without making new request
     */
    protected static final double CACHE_REACT_RATE = 1.5;

    protected final RowRequestHandler rowRequestHandler;

    protected int firstRowInViewPort = 0;
    protected int lastRequestedFirstvisible = 0; // to detect "serverside scroll"
    protected int firstvisible = 0;

    protected int rows; //received rows count
    protected int firstrow; //an index of the first row whitch will be rendered

    /*
     * When scrolling and selecting at the same time, the selections are not in
     * sync with the server while retrieving new rows (until key is released).
     */
    private HashSet<Object> unSyncedselectionsBeforeRowFetch;

    public IScrollTable() {
        super();
        bodyContainer.addScrollHandler(this);
        rowRequestHandler = new RowRequestHandler();
    }

    @Override
    public void updateFromUIDL(UIDL uidl) {

        // we may have pending cache row fetch, cancel it. See #2136
        rowRequestHandler.cancel();

        firstrow = uidl.getIntAttribute("firstrow");
        rows = uidl.getIntAttribute("rows");

        firstvisible = uidl.hasVariable("firstvisible") ? uidl
                .getIntVariable("firstvisible") : 0;
        if (firstvisible != lastRequestedFirstvisible && tBody != null) {
            // received 'surprising' firstvisible from server: scroll there
            firstRowInViewPort = firstvisible;
            bodyContainer.setScrollPosition(firstvisible * tBody.getRowHeight());
        }

        super.updateFromUIDL(uidl);

        if (uidl.hasVariable("selected")) {
            final Set<String> selectedKeys = uidl
                    .getStringArrayVariableAsSet("selected");
            if (tBody != null) {
                for (Object aTBody : tBody) {
                    /*
                     * Make the focus reflect to the server side state unless we
                     * are currently selecting multiple rows with keyboard.
                     */
                    IScrollTableBody.IScrollTableRow row = (IScrollTableBody.IScrollTableRow) aTBody;
                    boolean selected = selectedKeys.contains(row.getKey());
                    if (!selected
                            && unSyncedselectionsBeforeRowFetch != null
                            && unSyncedselectionsBeforeRowFetch.contains(row
                            .getKey())) {
                        selected = true;
                    }
                    if (selected != row.isSelected()) {
                        row.toggleSelection();
                    }
                }
            }
        }
        unSyncedselectionsBeforeRowFetch = null;

        hideScrollPositionAnnotation();
        purgeUnregistryBag();
    }

    @Override
    protected void focusRowFromBody() {
        if (selectedRowKeys.size() == 1) {
            // try to focus a row currently selected and in viewport
            String selectedRowKey = selectedRowKeys.iterator().next();
            if (selectedRowKey != null) {
                IScrollTableBody.IScrollTableRow renderedRow = (IScrollTableBody.IScrollTableRow) getRenderedRowByKey(selectedRowKey);
                if (renderedRow == null || !renderedRow.isInViewPort()) {
                    setRowFocus(getBody().getRowByRowIndex(firstRowInViewPort));
                } else {
                    setRowFocus(renderedRow);
                }
            }
        } else {
            // multiselect mode
            setRowFocus(getBody().getRowByRowIndex(firstRowInViewPort));
        }
    }

    @Override
    protected IScrollTableBody createBody() {
        return new IScrollTableBody();
    }

    @Override
    protected IScrollTableHead createHead() {
        return new IScrollTableHead();
    }

    @Override
    protected boolean updateImmediate() {
        return true;
    }

    @Override
    protected void updateBody(UIDL rowData) {
        if (!recalcWidths && initializedAndAttached) {
            updateBody(rowData, firstrow, rows);
        } else {
            if (tBody != null) {
                tBody.removeFromParent();
                lazyUnregistryBag.add(tBody);
            }
            tBody = createBody();
            ((IScrollTableBody) tBody).renderInitialRows(rowData, firstrow, rows);
            bodyContainer.add(tBody);
            initialContentReceived = true;
            if (isAttached()) {
                sizeInit();
            }
            tBody.restoreRowVisibility();
        }
    }

    @Override
    protected void sizeInit() {
        super.sizeInit();

        initializedAndAttached = false;

        if (firstvisible > 0) {
            // Deferred due some Firefox oddities. IE & Safari could survive
            // without
            Scheduler.get().scheduleDeferred(new Command() {
                @Override
                public void execute() {
                    bodyContainer.setScrollPosition(firstvisible
                            * tBody.getRowHeight());
                    firstRowInViewPort = firstvisible;
                }
            });
        }

        if (enabled) {
            // Do we need cache rows
            final IScrollTable.IScrollTableBody body = getBody();
            if (body.getLastRendered() + 1 < firstRowInViewPort + pageLength
                    + CACHE_REACT_RATE * pageLength) {
                if (totalRows - 1 > body.getLastRendered()) {
                    // fetch cache rows
                    rowRequestHandler.setReqFirstRow(body.getLastRendered() + 1);
                    rowRequestHandler.setReqRows((int) (pageLength * CACHE_RATE));
                    rowRequestHandler.deferRowFetch(1);
                }
            }
        }

        initializedAndAttached = true;
    }

    /**
     * @param uidl     which contains row data
     * @param firstRow first row in data set
     * @param rows  amount of rows in data set
     */
    private void updateBody(UIDL uidl, int firstRow, int rows) {
        final IScrollTable.IScrollTableBody tBody = getBody();
        if (uidl == null || rows < 1) {
            // container is empty, remove possibly existing rows
            if (firstRow < 0) {
                while (tBody.getLastRendered() > tBody.firstRendered) {
                    tBody.unlinkRow(false);
                }
                tBody.unlinkRow(false);
            }
            return;
        }

        tBody.renderRows(uidl, firstRow, rows);

        final int optimalFirstRow = (int) (firstRowInViewPort - pageLength
                * CACHE_RATE);
        boolean cont = true;
        while (cont && tBody.getLastRendered() > optimalFirstRow
                && tBody.getFirstRendered() < optimalFirstRow) {
            // client.console.log("removing row from start");
            cont = tBody.unlinkRow(true);
        }
        final int optimalLastRow = (int) (firstRowInViewPort + pageLength + pageLength
                * CACHE_RATE);
        cont = true;
        while (cont && tBody.getLastRendered() > optimalLastRow) {
            // client.console.log("removing row from the end");
            cont = tBody.unlinkRow(false);
        }
        tBody.fixSpacers();
        tBody.restoreRowVisibility();
    }

    @Override
    protected void onDetach() {
        rowRequestHandler.cancel();
        super.onDetach();
        // ensure that scrollPosElement will be detached
        if (scrollPositionElement != null) {
            final Element parent = DOM.getParent(scrollPositionElement);
            if (parent != null) {
                DOM.removeChild(parent, scrollPositionElement);
            }
        }
    }

    @Override
    public void onScroll(ScrollEvent event) {
        VConsole.log("Scroll event processing");

        int scrollLeft = bodyContainer.getElement().getScrollLeft();
        int scrollTop = bodyContainer.getScrollPosition();
        if (!initializedAndAttached) {
            return;
        }
        if (!enabled) {
            bodyContainer.setScrollPosition(firstRowInViewPort
                    * tBody.getRowHeight());
            return;
        }

        rowRequestHandler.cancel();

        runWebkitOverflowAutoFix();

        if (focusedRow != null)
            focusedRow.onScroll();

        // fix headers horizontal scrolling
        tHead.setHorizontalScrollPosition(scrollLeft);

        if (aggregationRow != null) {
            aggregationRow.setHorizontalScrollPosition(scrollLeft);
        }

        firstRowInViewPort = (int) Math.ceil(scrollTop
                / (double) tBody.getRowHeight());
        // ApplicationConnection.getConsole().log(
        // "At scrolltop: " + scrollTop + " At row " + firstRowInViewPort);

        int postLimit = (int) (firstRowInViewPort + pageLength + pageLength
                * CACHE_REACT_RATE);
        if (postLimit > totalRows - 1) {
            postLimit = totalRows - 1;
        }
        int preLimit = (int) (firstRowInViewPort - pageLength
                * CACHE_REACT_RATE);
        if (preLimit < 0) {
            preLimit = 0;
        }
        final IScrollTable.IScrollTableBody body = getBody();
        final int lastRendered = body.getLastRendered();
        final int firstRendered = body.getFirstRendered();

        if (postLimit <= lastRendered && preLimit >= firstRendered) {
            // remember which firstvisible we requested, in case the server has
            // a differing opinion
            lastRequestedFirstvisible = firstRowInViewPort;
            client.updateVariable(paintableId, "firstvisible",
                    firstRowInViewPort, false);
            return; // scrolled withing "non-react area"
        }

        if (firstRowInViewPort - pageLength * CACHE_RATE > lastRendered
                || firstRowInViewPort + pageLength + pageLength * CACHE_RATE < firstRendered) {
            // need a totally new set
            // ApplicationConnection.getConsole().log(
            // "Table: need a totally new set");
            rowRequestHandler
                    .setReqFirstRow((int) (firstRowInViewPort - pageLength
                            * CACHE_RATE));
            int last = firstRowInViewPort + (int) CACHE_RATE * pageLength
                    + pageLength;
            if (last > totalRows) {
                last = totalRows - 1;
            }
            rowRequestHandler.setReqRows(last
                    - rowRequestHandler.getReqFirstRow() + 1);
            rowRequestHandler.deferRowFetch();
            return;
        }
        if (preLimit < firstRendered) {
            // need some rows to the beginning of the rendered area
            // ApplicationConnection
            // .getConsole()
            // .log(
            // "Table: need some rows to the beginning of the rendered area");
            rowRequestHandler
                    .setReqFirstRow((int) (firstRowInViewPort - pageLength
                            * CACHE_RATE));
            rowRequestHandler.setReqRows(firstRendered
                    - rowRequestHandler.getReqFirstRow());
            rowRequestHandler.deferRowFetch();

            return;
        }
        if (postLimit > lastRendered) {
            // need some rows to the end of the rendered area
            // ApplicationConnection.getConsole().log(
            // "need some rows to the end of the rendered area");
            rowRequestHandler.setReqFirstRow(lastRendered + 1);
            rowRequestHandler.setReqRows((int) ((firstRowInViewPort
                    + pageLength + pageLength * CACHE_RATE) - lastRendered));
            rowRequestHandler.deferRowFetch();
        }
    }

    private void announceScrollPosition() {
        if (scrollPositionElement == null) {
            scrollPositionElement = DOM.createDiv();
            DOM.setElementProperty(scrollPositionElement, "className",
                    "v-table-scrollposition");
            DOM.appendChild(getElement(), scrollPositionElement);
        }

        DOM.setStyleAttribute(scrollPositionElement, "position", "absolute");
        DOM.setStyleAttribute(scrollPositionElement, "marginLeft", (DOM
                .getElementPropertyInt(getElement(), "offsetWidth") / 2 - 80)
                + "px");
        DOM.setStyleAttribute(scrollPositionElement, "marginTop", -(DOM
                .getElementPropertyInt(getElement(), "offsetHeight") - 2)
                + "px");

        // indexes go from 1-totalRows, as rowheaders in index-mode indicate
        int last = (firstRowInViewPort + (bodyContainer.getOffsetHeight() / tBody
                .getRowHeight()));
        if (last > totalRows) {
            last = totalRows;
        }
        DOM.setInnerHTML(scrollPositionElement, "<span>"
                + (firstRowInViewPort + 1) + " &ndash; " + last + "..."
                + "</span>");
        DOM.setStyleAttribute(scrollPositionElement, "display", "block");
    }

    private void hideScrollPositionAnnotation() {
        if (scrollPositionElement != null) {
            DOM.setStyleAttribute(scrollPositionElement, "display", "none");
        }
    }

    protected class RowRequestHandler extends Timer {

        private int reqFirstRow = 0;
        private int reqRows = 0;

        public void deferRowFetch() {
            deferRowFetch(250);
        }

        public void deferRowFetch(int msec) {
            if (reqRows > 0 && reqFirstRow < totalRows) {
                schedule(msec);

                // tell scroll position to user if currently "visible" rows are
                // not rendered
                final IScrollTable.IScrollTableBody body = getBody();
                if ((firstRowInViewPort + pageLength > body.getLastRendered())
                        || (firstRowInViewPort < body.getFirstRendered())) {
                    announceScrollPosition();
                } else {
                    hideScrollPositionAnnotation();
                }
            }
        }

        public void setReqFirstRow(int reqFirstRow) {
            if (reqFirstRow < 0) {
                reqFirstRow = 0;
            } else if (reqFirstRow >= totalRows) {
                reqFirstRow = totalRows - 1;
            }
            this.reqFirstRow = reqFirstRow;
        }

        public void setReqRows(int reqRows) {
            this.reqRows = reqRows;
        }

        @Override
        public void run() {
            if (client.hasActiveRequest()) {
                // if client connection is busy, don't bother loading it more
                schedule(250);
                ApplicationConnection.getConsole().log(
                        "Table: AC is busy, deferring cache row fetch..");

            } else {
                ApplicationConnection.getConsole().log(
                        "Getting " + reqRows + " rows from " + reqFirstRow);

                final IScrollTable.IScrollTableBody body = getBody();
                int firstToBeRendered = body.firstRendered;
                if (reqFirstRow < firstToBeRendered) {
                    firstToBeRendered = reqFirstRow;
                } else if (firstRowInViewPort - (int) (CACHE_RATE * pageLength) > firstToBeRendered) {
                    firstToBeRendered = firstRowInViewPort
                            - (int) (CACHE_RATE * pageLength);
                    if (firstToBeRendered < 0) {
                        firstToBeRendered = 0;
                    }
                }

                int lastToBeRendered = body.lastRendered;

                if (reqFirstRow + reqRows - 1 > lastToBeRendered) {
                    lastToBeRendered = reqFirstRow + reqRows - 1;
                } else if (firstRowInViewPort + pageLength + pageLength
                        * CACHE_RATE < lastToBeRendered) {
                    lastToBeRendered = (firstRowInViewPort + pageLength + (int) (pageLength * CACHE_RATE));
                    if (lastToBeRendered >= totalRows) {
                        lastToBeRendered = totalRows - 1;
                    }
                    // due Safari 3.1 bug (see #2607), verify reqrows, original
                    // problem unknown, but this should catch the issue
                    if (reqFirstRow + reqRows - 1 > lastToBeRendered) {
                        reqRows = lastToBeRendered - reqFirstRow;
                    }
                }

                client.updateVariable(paintableId, "firstToBeRendered",
                        firstToBeRendered, false);

                client.updateVariable(paintableId, "lastToBeRendered",
                        lastToBeRendered, false);
                // remember which firstvisible we requested, in case the server
                // has
                // a differing opinion
                lastRequestedFirstvisible = firstRowInViewPort;
                client.updateVariable(paintableId, "firstvisible",
                        firstRowInViewPort, false);
                client.updateVariable(paintableId, "reqfirstrow", reqFirstRow,
                        false);
                client.updateVariable(paintableId, "reqrows", reqRows, true);

                if (selectionChanged) {
                    unSyncedselectionsBeforeRowFetch = new HashSet<Object>(
                            selectedRowKeys);
                }
            }
        }

        public int getReqFirstRow() {
            return reqFirstRow;
        }

        public int getReqRows() {
            return reqRows;
        }

        /**
         * Sends request to refresh content at this position.
         */
        public void refreshContent() {
            int first = (int) (firstRowInViewPort - pageLength * CACHE_RATE);
            int reqRows = (int) (2 * pageLength * CACHE_RATE + pageLength);
            if (first < 0) {
                reqRows = reqRows + first;
                first = 0;
            }
            setReqFirstRow(first);
            setReqRows(reqRows);
            run();
        }
    }

    public class IScrollHeaderCell extends HeaderCell {
        public IScrollHeaderCell(String colId, UIDL uidl) {
            super(colId, uidl);
        }

        @Override
        protected void handleCaptionEvent(Event event) {
            super.handleCaptionEvent(event);
            if (DOM.eventGetType(event) == Event.ONMOUSEUP) {
                if (!moved && sortable) {
                    firstvisible = 0;
                    rowRequestHandler.setReqFirstRow(0);
                    rowRequestHandler.setReqRows((int) (2 * pageLength
                            * CACHE_RATE + pageLength));
                    rowRequestHandler.deferRowFetch();
                }
            }
        }
    }

    public class IScrollTableHead extends TableHead {
        class ColumnAction extends VisibleColumnAction {
            ColumnAction(String colKey) {
                super(colKey);
            }

            @Override
            public void execute() {
                super.execute();
                // let rowRequestHandler determine proper rows
                rowRequestHandler.refreshContent();
            }
        }

        @Override
        protected HeaderCell createHeaderCell(String cid, UIDL uidl) {
            return new IScrollHeaderCell(cid, uidl);
        }

        @Override
        protected VisibleColumnAction createColumnAction(String key) {
            return new ColumnAction(key);
        }
    }

    /**
     * This Panel can only contain IScrollTableRow type of widgets. This
     * "simulates" very large table, keeping spacers which take room of
     * unrendered rows.
     */
    public class IScrollTableBody extends ITableBody {

        Element preSpacer = DOM.createDiv();
        Element postSpacer = DOM.createDiv();

        protected int firstRendered;

        protected int lastRendered;

        public IScrollTableBody() {
            super();
            DOM.setElementProperty(table, "className", CLASSNAME + "-table");
            DOM.setElementProperty(preSpacer, "className", CLASSNAME
                    + "-row-spacer");
            DOM.setElementProperty(postSpacer, "className", CLASSNAME
                    + "-row-spacer");

            DOM.appendChild(table, tBody);
            DOM.appendChild(container, preSpacer);
            DOM.appendChild(container, table);
            DOM.appendChild(container, postSpacer);
        }

        @Override
        public int getAvailableWidth() {
            return DOM.getElementPropertyInt(preSpacer, "offsetWidth");
        }

        public void renderInitialRows(UIDL rowData, int firstIndex, int rows) {
            firstRendered = firstIndex;
            lastRendered = firstIndex + rows - 1;
            final Iterator it = rowData.getChildIterator();
            aligns = tHead.getColumnAlignments();
            while (it.hasNext()) {
                final IScrollTableRow row = createRowInstance((UIDL) it.next());
                addRow(row);
            }
            if (isAttached()) {
                fixSpacers();
            }
        }

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
                final IScrollTableRow[] rowArray = createRowsArray(rows);
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
            if (reactLastRow >= totalRows) {
                reactLastRow = totalRows - 1;
            }
            if (lastRendered < reactLastRow) {
                // get some cache rows below visible area
                rowRequestHandler.setReqFirstRow(lastRendered + 1);
                rowRequestHandler.setReqRows(reactLastRow - lastRendered);
                rowRequestHandler.deferRowFetch(1);
            } else if (IScrollTable.this.getBody().getFirstRendered() > reactFirstRow) {
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
        protected IScrollTableRow createRow(UIDL uidl) {
            final IScrollTableRow row = createRowInstance(uidl);
            final int cells = DOM.getChildCount(row.getElement());
            for (int i = 0; i < cells; i++) {
                final Element cell = DOM.getChild(row.getElement(), i);
                final int w = IScrollTable.this
                        .getColWidth(getColKeyByIndex(i));
                setWidthDependsOnStyle(DOM.getFirstChild(cell), w);
                DOM.setStyleAttribute(cell, "width", w + "px");
            }
            return row;
        }

        @Override
        protected IScrollTableRow createRowInstance(UIDL uidl) {
            return new IScrollTableRow(uidl, aligns);
        }

        protected IScrollTableRow[] createRowsArray(int rows) {
            return new IScrollTableRow[rows];
        }

        protected void addRowBeforeFirstRendered(IScrollTableRow row) {
            row.setIndex(firstRendered - 1);
            IScrollTableRow first = null;
            if (renderedRows.size() > 0) {
                first = (IScrollTableRow) renderedRows.get(0);
            }
            if (first != null && first.getStyleName().indexOf("-odd") == -1) {
                applyAlternatingRowColor(row, "-row-odd");
            } else {
                applyAlternatingRowColor(row, "-row");
            }
            if (row.isSelected()) {
                row.addStyleName("v-selected");
            }
            tBody.insertBefore(row.getElement(),
                    tBody.getFirstChild());
            adopt(row);
            renderedRows.add(0, row);
        }

        protected void addRow(IScrollTableRow row) {
            row.setIndex(firstRendered + renderedRows.size());
            IScrollTableRow last = null;
            if (renderedRows.size() > 0) {
                last = (IScrollTableRow) renderedRows
                        .get(renderedRows.size() - 1);
            }
            if (last != null && last.getStyleName().indexOf("-odd") == -1) {
                applyAlternatingRowColor(row, "-row-odd");
            } else {
                applyAlternatingRowColor(row, "-row");
            }
            if (row.isSelected()) {
                row.addStyleName("v-selected");
                focusedRow = row;
            }
            tBody.appendChild(row.getElement());
            adopt(row);
            renderedRows.add(row);
        }

        protected void applyAlternatingRowColor(IScrollTableRow row, String style) {
            row.addStyleName(CLASSNAME + style);
        }

        /**
         * @param fromBeginning Remove all from beginning
         * @return false if couldn't remove row
         */
        public boolean unlinkRow(boolean fromBeginning) {
            if (lastRendered - firstRendered < 0) {
                return false;
            }
            int index;
            if (fromBeginning) {
                index = 0;
                firstRendered++;
            } else {
                index = renderedRows.size() - 1;
                lastRendered--;
            }
            final IScrollTableRow toBeRemoved = (IScrollTableRow) renderedRows.get(index);
            lazyUnregistryBag.add(toBeRemoved);
            DOM.removeChild(tBody, toBeRemoved.getElement());
            orphan(toBeRemoved);
            renderedRows.remove(index);
            fixSpacers();
            return true;
        }

        @Override
        public void setContainerHeight() {
            fixSpacers();
            super.setContainerHeight();
        }

        protected void fixSpacers() {
            int prepx = getRowHeight() * firstRendered;
            if (prepx < 0) {
                prepx = 0;
            }
            DOM.setStyleAttribute(preSpacer, "height", prepx + "px");
            int postpx = getRowHeight() * (totalRows - 1 - lastRendered);
            if (postpx < 0) {
                postpx = 0;
            }
            DOM.setStyleAttribute(postSpacer, "height", postpx + "px");
        }

        public int getLastRendered() {
            return lastRendered;
        }

        public int getFirstRendered() {
            return firstRendered;
        }

        public class IScrollTableRow extends ITableRow {

            protected IScrollTableRow() {
                super();
            }

            public IScrollTableRow(int rowKey) {
                super(rowKey);
            }

            public IScrollTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            /**
             * Detects whether row is visible in tables viewport.
             *
             * @return
             */
            public boolean isInViewPort() {
                int absoluteTop = getAbsoluteTop();
                int scrollPosition = bodyContainer.getScrollPosition();
                if (absoluteTop < scrollPosition) {
                    return false;
                }
                int maxVisible = scrollPosition
                        + bodyContainer.getOffsetHeight() - getOffsetHeight();
                if (absoluteTop > maxVisible) {
                    return false;
                }
                return true;
            }
        }
    }

    /*
     * Overridden due Table might not survive of visibility change (scroll pos
     * lost). Example ITabPanel just set contained components invisible and back
     * when changing tabs.
     */
    @Override
    public void setVisible(boolean visible) {
        if (isVisible() != visible) {
            super.setVisible(visible);
            if (initializedAndAttached) {
                if (visible) {
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            bodyContainer.setScrollPosition(firstRowInViewPort
                                    * tBody.getRowHeight());
                        }
                    });
                }
            }
        }
    }

    @Override
    public IScrollTableBody getBody() {
        return (IScrollTableBody) super.getBody();
    }

    @Override
    protected boolean handleNavigationPageDownKey( boolean ctrl, boolean shift ) {
        if (isSelectable()) {
            /*
             * If selectable we plagiate MSW behaviour: first scroll to the
             * end of current view. If at the end, scroll down one page
             * length and keep the selected row in the bottom part of
             * visible area.
             */
            if (!isFocusAtTheEndOfTable()) {
                ITableBody.ITableRow lastVisibleRowInViewPort = getBody()
                        .getRowByRowIndex(firstRowInViewPort
                                + getFullyVisibleRowCount() - 1);
                if (lastVisibleRowInViewPort != null
                        && lastVisibleRowInViewPort != focusedRow) {
                    // focused row is not at the end of the table, move
                    // focus and select the last visible row
                    setRowFocus(lastVisibleRowInViewPort);
                    selectFocusedRow(ctrl, shift);
                    sendSelectedRows();
                } else {
                    int indexOfToBeFocused = focusedRow.getIndex()
                            + getFullyVisibleRowCount();
                    if (indexOfToBeFocused >= totalRows) {
                        indexOfToBeFocused = totalRows - 1;
                    }
                    ITableBody.ITableRow toBeFocusedRow = getBody()
                            .getRowByRowIndex(indexOfToBeFocused);

                    if (toBeFocusedRow != null) {
                        /*
                         * if the next focused row is rendered
                         */
                        setRowFocus(toBeFocusedRow);
                        selectFocusedRow(ctrl, shift);
                        // TODO needs scrollintoview ?
                        sendSelectedRows();
                    } else {
                        // scroll down by pixels and return, to wait for
                        // new rows, then select the last item in the
                        // viewport
                        selectLastItemInNextRender = true;
                        multiselectPending = shift;
                        scrollByPagelenght(1);
                    }
                }
            }
        } else {
            /* No selections, go page down by scrolling */
            scrollByPagelenght(1);
        }
        return true;
    }

    @Override
    protected boolean handleNavigationPageUpKey(boolean ctrl, boolean shift) {
        if (isSelectable()) {
            /*
             * If selectable we plagiate MSW behaviour: first scroll to the
             * end of current view. If at the end, scroll down one page
             * length and keep the selected row in the bottom part of
             * visible area.
             */
            if (!isFocusAtTheBeginningOfTable()) {
                ITableBody.ITableRow firstVisibleRowInViewPort = getBody()
                        .getRowByRowIndex(firstRowInViewPort);
                if (firstVisibleRowInViewPort != null
                        && firstVisibleRowInViewPort != focusedRow) {
                    // focus is not at the beginning of the table, move
                    // focus and select the first visible row
                    setRowFocus(firstVisibleRowInViewPort);
                    selectFocusedRow(ctrl, shift);
                    sendSelectedRows();
                } else {
                    int indexOfToBeFocused = focusedRow.getIndex()
                            - getFullyVisibleRowCount();
                    if (indexOfToBeFocused < 0) {
                        indexOfToBeFocused = 0;
                    }
                    ITableBody.ITableRow toBeFocusedRow = getBody()
                            .getRowByRowIndex(indexOfToBeFocused);

                    if (toBeFocusedRow != null) { // if the next focused row
                                                  // is rendered
                        setRowFocus(toBeFocusedRow);
                        selectFocusedRow(ctrl, shift);
                        // TODO needs scrollintoview ?
                        sendSelectedRows();
                    } else {
                        // unless waiting for the next rowset already
                        // scroll down by pixels and return, to wait for
                        // new rows, then select the last item in the
                        // viewport
                        selectFirstItemInNextRender = true;
                        multiselectPending = shift;
                        scrollByPagelenght(-1);
                    }
                }
            }
        } else {
            /* No selections, go page up by scrolling */
            scrollByPagelenght(-1);
        }

        return true;

    }

    @Override
    protected boolean handleNavigationEndKey(boolean ctrl, boolean shift) {
        bodyContainer.setScrollPosition(getBody().getOffsetHeight());
        if (isSelectable()) {
            final int lastRendered = getBody().getLastRendered();
            if (lastRendered + 1 == totalRows) {
                ITableBody.ITableRow rowByRowIndex = tBody
                        .getRowByRowIndex(lastRendered);
                if (focusedRow != rowByRowIndex) {
                    setRowFocus(rowByRowIndex);
                    selectFocusedRow(ctrl, shift);
                    sendSelectedRows();
                }
            } else {
                if (ctrl) {
                    focusLastItemInNextRender = true;
                } else {
                    selectLastItemInNextRender = true;
                    multiselectPending = shift;
                }
            }
        }
        return true;
    }
}