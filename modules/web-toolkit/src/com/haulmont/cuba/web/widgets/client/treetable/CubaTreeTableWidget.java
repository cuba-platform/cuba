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

package com.haulmont.cuba.web.widgets.client.treetable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.Tools;
import com.haulmont.cuba.web.widgets.client.aggregation.TableAggregationRow;
import com.haulmont.cuba.web.widgets.client.image.CubaImageWidget;
import com.haulmont.cuba.web.widgets.client.tableshared.TableEmptyState;
import com.haulmont.cuba.web.widgets.client.tableshared.TableWidget;
import com.haulmont.cuba.web.widgets.client.tableshared.TableWidgetDelegate;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComputedStyle;
import com.vaadin.client.UIDL;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VEmbedded;
import com.vaadin.v7.client.ui.VLabel;
import com.vaadin.v7.client.ui.VTextField;
import com.vaadin.v7.client.ui.VTreeTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.haulmont.cuba.web.widgets.client.Tools.isAnyModifierKeyPressed;
import static com.haulmont.cuba.web.widgets.client.tableshared.TableWidgetDelegate.*;

public class CubaTreeTableWidget extends VTreeTable implements TableWidget {

    public TableWidgetDelegate _delegate = new TableWidgetDelegate(this, this);

    protected CubaTreeTableWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);

        hideColumnControlAfterClick = false;
    }

    @Override
    public void scheduleLayoutForChildWidgets() {
        _delegate.scheduleLayoutForChildWidgets();
    }

    @Override
    public boolean handleBodyContextMenu(int left, int top) {
        if (_delegate.contextMenuEnabled) {
            if (_delegate.customContextMenu == null) {
                return super.handleBodyContextMenu(left, top);
            } else if (enabled) {
                _delegate.showContextMenuPopup(left, top);

                return true;
            }
        }
        return false;
    }

    @Override
    protected VScrollTableBody createScrollBody() {
        scrollBody = new CubaTreeTableBody();
        return scrollBody;
    }

    @Override
    protected int getDynamicBodyHeight() {
        if (totalRows <= 0) {
            return (int) Math.round(scrollBody.getRowHeight(true));
        }

        return (int) Math.round(totalRows * scrollBody.getRowHeight(true));
    }

    @Override
    public boolean isUseSimpleModeForTouchDevice() {
        return Tools.isUseSimpleMultiselectForTouchDevice();
    }

    @Override
    protected boolean isAllowSingleSelectToggle() {
        return BrowserInfo.get().isTouchDevice() && Tools.isUseSimpleMultiselectForTouchDevice();
    }

    @Override
    public void onFocus(FocusEvent event) {
        super.onFocus(event);

        addStyleDependentName("body-focus");
    }

    @Override
    public void onBlur(BlurEvent event) {
        super.onBlur(event);

        removeStyleDependentName("body-focus");
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && _delegate.shortcutHandler != null) {
            _delegate.shortcutHandler.handleKeyboardEvent(event);
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        if (_delegate.presentationsEditorPopup != null) {
            _delegate.presentationsEditorPopup.hide();
        }

        if (_delegate.customContextMenuPopup != null) {
            _delegate.customContextMenuPopup.hide();
        }
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        _delegate.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return _delegate.shortcutHandler;
    }

    @Override
    protected VScrollTableBody.VScrollTableRow getNextRowToFocus(VScrollTableBody.VScrollTableRow currentRow, int offset) {
        // Support select first N rows by Shift+Click #PL-3267
        if (focusedRow == currentRow && !focusedRow.isSelected()) {
            return focusedRow;
        }

        return super.getNextRowToFocus(currentRow, offset);
    }

    @Override
    protected boolean needToSelectFocused(VScrollTableBody.VScrollTableRow currentRow) {
        // Support select first N rows by Shift+Click #PL-3267
        return currentRow == focusedRow && (!focusedRow.isSelected());
    }

    @Override
    public TableHead getHead() {
        return tHead;
    }

    @Override
    public String[] getVisibleColOrder() {
        return visibleColOrder;
    }

    @Override
    public String getColKeyByIndex(int index) {
        return super.getColKeyByIndex(index);
    }

    @Override
    public int getColWidth(String colKey) {
        return super.getColWidth(colKey);
    }

    @Override
    public void setColWidth(int colIndex, int w, boolean isDefinedWidth) {
        super.setColWidth(colIndex, w, isDefinedWidth);

        if (_delegate.aggregationRow != null && _delegate.aggregationRow.isInitialized()) {
            _delegate.aggregationRow.setCellWidth(colIndex, w);
        }
    }

    @Override
    public boolean isTextSelectionEnabled() {
        return _delegate.textSelectionEnabled;
    }

    @Override
    public List<Widget> getRenderedRows() {
        return ((CubaTreeTableBody) scrollBody).getRenderedRows();
    }

    @Override
    public int getAdditionalRowsHeight() {
        if (_delegate.aggregationRow != null) {
            return _delegate.aggregationRow.getOffsetHeight();
        }
        return 0;
    }

    @Override
    protected TableHead createTableHead() {
        return new CubaTreeTableTableHead();
    }

    public void setPresentationsMenu(Widget presentationsMenu) {
        if (_delegate.presentationsMenu != presentationsMenu) {
            Style presentationsIconStyle = ((CubaTreeTableTableHead) tHead).presentationsEditIcon.getElement().getStyle();
            if (presentationsMenu == null) {
                presentationsIconStyle.setDisplay(Style.Display.NONE);
            } else {
                presentationsIconStyle.setDisplay(Style.Display.BLOCK);
            }
        }
        _delegate.presentationsMenu = presentationsMenu;
    }

    public void updateTextSelection() {
        Tools.textSelectionEnable(scrollBody.getElement(), _delegate.textSelectionEnabled);
    }

    protected void updateAggregationRow(UIDL uidl) {
        if (_delegate.aggregationRow == null) {
            _delegate.aggregationRow = createAggregationRow();
            _delegate.aggregationRow.setTotalAggregationInputHandler(_delegate.totalAggregationInputHandler);
            insert(_delegate.aggregationRow, getWidgetIndex(scrollBodyPanel));
        }
        _delegate.aggregationRow.updateFromUIDL(uidl);
        _delegate.aggregationRow.setHorizontalScrollPosition(scrollLeft);
        triggerLazyColumnAdjustment(true);
    }

    protected void removeAggregationRow() {
        remove(_delegate.aggregationRow);
        _delegate.aggregationRow = null;
    }

    protected void showEmptyState(boolean show) {
        if (show) {
            if (_delegate.tableEmptyState == null) {
                _delegate.tableEmptyState = new TableEmptyState();
            }

            Element emptyState = _delegate.tableEmptyState.getElement();
            if (!scrollBodyPanel.getElement().isOrHasChild(emptyState)) {
                scrollBodyPanel.getElement().appendChild(emptyState);
            }
        } else if (_delegate.tableEmptyState != null) {
            _delegate.tableEmptyState.getElement().removeFromParent();
            _delegate.tableEmptyState = null;
        }
    }

    protected TableAggregationRow createAggregationRow() {
        return new TableAggregationRow(this);
    }

    @Override
    public void onScroll(ScrollEvent event) {
        if (isLazyScrollerActive()) {
            return;
        }

        super.onScroll(event);

        if (_delegate.aggregationRow != null) {
            _delegate.aggregationRow.setHorizontalScrollPosition(scrollLeft);
        }

        if (!enabled) {
            tHead.setHorizontalScrollPosition(scrollLeft);
            tFoot.setHorizontalScrollPosition(scrollLeft);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.isEnabled() != enabled) {
            this.enabled = enabled;
        }
    }

    @Override
    public String getSortDescendingLabel() {
        return _delegate.tableSortDescendingLabel;
    }

    @Override
    public String getSortAscendingLabel() {
        return _delegate.tableSortAscendingLabel;
    }

    @Override
    public String getSortResetLabel() {
        return _delegate.tableSortResetLabel;
    }

    @Override
    public Widget getOwner() {
        return CubaTreeTableWidget.this;
    }

    @Override
    public RowRequestHandler getRowRequestHandler() {
        return rowRequestHandler;
    }

    @Override
    public void forceReassignColumnWidths() {
        int visibleCellCount = tHead.getVisibleCellCount();
        for (int i = 0; i < visibleCellCount; i++) {
            HeaderCell hcell = tHead.getHeaderCell(i);
            reassignHeaderCellWidth(i, hcell, hcell.getMinWidth());
        }
    }

    @Override
    protected void reassignHeaderCellWidth(int colIndex, HeaderCell hcell, int minWidth) {
        _delegate.reassignHeaderCellWidth(colIndex, hcell, minWidth);
    }

    @Override
    public boolean isCustomColumn(int colIndex) {
        return false;
    }

    @Override
    public boolean isGenericRow(Widget rowWidget) {
        return rowWidget instanceof VScrollTableBody.VScrollTableRow;
    }

    protected class CubaTreeTableTableHead extends TableHead {

        protected final SimplePanel presentationsEditIcon = GWT.create(SimplePanel.class);

        public CubaTreeTableTableHead() {
            Element iconElement = presentationsEditIcon.getElement();
            iconElement.setClassName("c-table-prefs-icon");
            iconElement.getStyle().setDisplay(Style.Display.NONE);

            Element columnSelector = (Element) getElement().getLastChild();
            DOM.insertChild(getElement(), iconElement, DOM.getChildIndex(getElement(), columnSelector));

            DOM.sinkEvents(iconElement, Event.ONCLICK);
        }

        @Override
        public Action[] getActions() {
            Action[] tableActions = super.getActions();
            Action[] actions = new Action[tableActions.length + 2];
            actions[0] = new SelectAllAction(tableActions);
            actions[1] = new DeselectAllAction(tableActions);
            System.arraycopy(tableActions, 0, actions, 2, tableActions.length);
            return actions;
        }

        @Override
        protected int getIconsOffsetWidth() {
            Style presentationsIconStyle = presentationsEditIcon.getElement().getStyle();
            if ("none".equals(presentationsIconStyle.getDisplay())) {
                return super.getIconsOffsetWidth();
            }

            ComputedStyle cs = new ComputedStyle(presentationsEditIcon.getElement());
            double right = cs.getDoubleProperty("right");

            return (int) Math.ceil(right + cs.getWidth());
        }

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);

            _delegate.showPresentationEditorPopup(event, presentationsEditIcon);
        }

        @Override
        protected HeaderCell createHeaderCell(String cid, String caption) {
            return new CubaTreeTableHeaderCell(cid, caption);
        }

        @Override
        protected String getCustomHtmlAttributes(TableHead.VisibleColumnAction action) {
            String colKey = action.getColKey();
            HeaderCell headerCell = getHeaderCell(colKey);
            if (headerCell != null) {
                String cubaId = headerCell.getElement().getAttribute("cuba-id");
                if (cubaId != null) {
                    return "cuba-id=\"cc_" + cubaId + "\"";
                }
            }

            return super.getCustomHtmlAttributes(action);
        }


        protected class SelectAllAction extends Action {

            protected Action[] actions;

            public SelectAllAction(Action[] actions) {
                super(CubaTreeTableTableHead.this);
                this.actions = actions;
                setCaption(_delegate.selectAllLabel);
            }

            @Override
            public void execute() {
                if (actions == null || actions.length == 0) {
                    return;
                }

                for (Action action : actions) {
                    if (action instanceof VisibleColumnAction
                            && collapsedColumns.contains(((VisibleColumnAction) action).getColKey())) {
                        action.execute();
                    }
                }
            }

            @Override
            public String getHTML() {
                return "<span id=\"tableSelectAllAction\" class=\"v-off\">" +
                        super.getHTML() +
                        "</span>";
            }
        }

        protected class DeselectAllAction extends Action {

            protected Action[] actions;

            public DeselectAllAction(Action[] actions) {
                super(CubaTreeTableTableHead.this);
                this.actions = actions;
                setCaption(_delegate.deselectAllLabel);
            }

            @Override
            public void execute() {
                if (actions == null || actions.length == 0) {
                    return;
                }

                Action firstAction = actions[0];
                if (firstAction instanceof VisibleColumnAction
                     && collapsedColumns.contains(((VisibleColumnAction) firstAction).getColKey())) {
                        firstAction.execute();
                }

                for (int i = 1; i < actions.length; i++) {
                    Action action = actions[i];
                    if (action instanceof VisibleColumnAction
                    && !collapsedColumns.contains(((VisibleColumnAction) action).getColKey())) {
                        action.execute();
                    }
                }
            }

            @Override
            public String getHTML() {
                return "<span id=\"tableDeselectAllAction\" class=\"v-off\">" +
                        super.getHTML() +
                        "</span>";
            }
        }
    }

    protected class CubaTreeTableHeaderCell extends HeaderCell {

        protected int sortClickCounter = 0;

        public CubaTreeTableHeaderCell(String colId, String headerText) {
            super(colId, headerText);

            Element sortIndicator = td.getChild(1).cast();
            DOM.sinkEvents(sortIndicator, Event.ONCONTEXTMENU | DOM.getEventsSunk(sortIndicator));
            Element captionContainer = td.getChild(2).cast();
            DOM.sinkEvents(captionContainer, Event.ONCONTEXTMENU | DOM.getEventsSunk(captionContainer));
        }

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);

            if (isEnabled() && event.getTypeInt() == Event.ONCONTEXTMENU) {
                if (getStyleName().contains("-header-sortable")) {
                    _delegate.showSortMenu(td, cid);
                }

                event.preventDefault();
                event.stopPropagation();
            }
        }

        @Override
        public void setText(String headerText) {
            if (headerText == null || "".equals(headerText)) {
                super.setText("&nbsp;");
                return;
            }

            TableWidgetDelegate delegate = CubaTreeTableWidget.this._delegate;
            boolean captionAsHtml = delegate.htmlCaptionColumns.contains(cid);
            if (captionAsHtml) {
                captionContainer.setInnerHTML(headerText);
            } else {
                captionContainer.setInnerText(headerText);
            }
        }

        @Override
        protected void sortColumn() {
            // CAUTION copied from superclass
            // Added ability to reset sort order
            boolean reloadDataFromServer = true;

            if (cid.equals(sortColumn)) {
                if (sortAscending) {
                    if (sortClickCounter < 2) {
                        // special case for initial revert sorting instead of reset sort order
                        if (sortClickCounter == 0) {
                            client.updateVariable(paintableId, "sortascending", false, false);
                        } else {
                            reloadDataFromServer = false;
                            sortClickCounter = 0;
                            sortColumn = null;

                            client.updateVariable(paintableId, "resetsortorder", "", true);
                        }
                    } else {
                        client.updateVariable(paintableId, "sortascending", false, false);
                    }
                } else {
                    if (sortClickCounter < 2) {
                        // special case for initial revert sorting instead of reset sort order
                        if (sortClickCounter == 0) {
                            client.updateVariable(paintableId, "sortascending", true, false);
                        } else {
                            reloadDataFromServer = false;
                            sortClickCounter = 0;
                            sortColumn = null;
                            sortAscending = true;

                            client.updateVariable(paintableId, "resetsortorder", "", true);
                        }
                    } else {
                        reloadDataFromServer = false;
                        sortClickCounter = 0;
                        sortColumn = null;
                        sortAscending = true;

                        client.updateVariable(paintableId, "resetsortorder", "", true);
                    }
                }
                sortClickCounter++;
            } else {
                sortClickCounter = 0;

                // set table sorted by this column
                client.updateVariable(paintableId, "sortcolumn", cid, false);
            }

            if (reloadDataFromServer) {
                // get also cache columns at the same request
                scrollBodyPanel.setScrollPosition(0);
                firstvisible = 0;
                rowRequestHandler.setReqFirstRow(0);
                rowRequestHandler.setReqRows((int) (2 * pageLength * cacheRate + pageLength));
                rowRequestHandler.deferRowFetch(); // some validation +
                // defer 250ms
                rowRequestHandler.cancel(); // instead of waiting
                rowRequestHandler.run(); // run immediately
            }
        }
    }

    protected class CubaTreeTableBody extends VTreeTableScrollBody {

        protected Widget lastFocusedWidget = null;

        @Override
        protected VScrollTableRow createRow(UIDL uidl, char[] aligns2) {
            if (uidl.hasAttribute("gen_html")) {
                // This is a generated row.
                return new VTreeTableGeneratedRow(uidl, aligns2);
            }
            return new CubaTreeTableRow(uidl, aligns2);
        }

        protected class CubaTreeTableRow extends VTreeTableRow {

            protected String currentColumnKey = null;

            public CubaTreeTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            public List<Widget> getChildWidgets() {
                return childWidgets;
            }

            @Override
            protected void initCellWithWidget(Widget w, char align,
                                              String style, boolean sorted, TableCellElement td) {
                super.initCellWithWidget(w, align, style, sorted, td);

                td.getFirstChildElement().addClassName(WIDGET_CELL_CLASSNAME);

                if (CubaTreeTableWidget.this.isSelectable()) {
                    // Support for #PL-2080
                    recursiveAddFocusHandler(w, w);
                }
            }

            protected void recursiveAddFocusHandler(final Widget w, final Widget topWidget) {
                if (w instanceof HasWidgets) {
                    for (Widget child : (HasWidgets) w) {
                        recursiveAddFocusHandler(child, topWidget);
                    }
                }

                if (w instanceof HasFocusHandlers) {
                    ((HasFocusHandlers) w).addFocusHandler(e ->
                            handleFocusAndClickEvents(e, topWidget));
                }
            }

            protected void handleFocusAndClickEvents(DomEvent e, Widget topWidget) {
                if (childWidgets.indexOf(topWidget) < 0) {
                    return;
                }

                lastFocusedWidget = ((Widget) e.getSource());

                if (!isSelected()) {
                    deselectAll();

                    toggleSelection();
                    setRowFocus(CubaTreeTableWidget.CubaTreeTableBody.CubaTreeTableRow.this);

                    sendSelectedRows();
                }
            }

            protected void handleFocusForWidget() {
                if (lastFocusedWidget == null) {
                    return;
                }

                if (isSelected()) {
                    if (lastFocusedWidget instanceof com.vaadin.client.Focusable) {
                        ((com.vaadin.client.Focusable) lastFocusedWidget).focus();
                    } else if (lastFocusedWidget instanceof com.google.gwt.user.client.ui.Focusable) {
                        ((com.google.gwt.user.client.ui.Focusable) lastFocusedWidget).setFocus(true);
                    }
                }

                lastFocusedWidget = null;
            }

            @Override
            public void onBrowserEvent(Event event) {
                if (event.getTypeInt() == Event.ONMOUSEDOWN
                        && event.getButton() == NativeEvent.BUTTON_LEFT
                        && !isAnyModifierKeyPressed(event)) {

                    Element eventTarget = event.getEventTarget().cast();
                    Element elementTdOrTr = getElementTdOrTr(eventTarget);

                    if (elementTdOrTr != null
                            && "td".equalsIgnoreCase(elementTdOrTr.getTagName())
                            && !elementTdOrTr.hasClassName(CUBA_TABLE_CLICKABLE_TEXT_STYLE)) {
                        // found <td>

                        if ("span".equalsIgnoreCase(eventTarget.getTagName())
                                && eventTarget.hasClassName(CUBA_TABLE_CLICKABLE_CELL_STYLE)) {
                            // found <span class="c-table-clickable-cell">

                            int childIndex = DOM.getChildIndex(getElement(), elementTdOrTr);
                            String columnKey = tHead.getHeaderCell(childIndex).getColKey();
                            if (columnKey != null) {
                                WidgetUtil.TextRectangle rect = WidgetUtil.getBoundingClientRect(eventTarget);
                                _delegate.lastClickClientX = (int) Math.ceil(rect.getLeft());
                                _delegate.lastClickClientY = (int) Math.ceil(rect.getBottom());

                                if (_delegate.cellClickListener != null) {
                                    _delegate.cellClickListener.onClick(columnKey, rowKey);

                                    event.preventDefault();
                                    event.stopPropagation();

                                    return;
                                }
                            }
                        }
                    }
                }

                super.onBrowserEvent(event);

                if (event.getTypeInt() == Event.ONMOUSEDOWN) {
                    final Element eventTarget = event.getEventTarget().cast();
                    Widget widget = WidgetUtil.findWidget(eventTarget, null);

                    if (widget != this) {
                        if (widget instanceof com.vaadin.client.Focusable || widget instanceof com.google.gwt.user.client.ui.Focusable) {
                            lastFocusedWidget = widget;
                        }
                    }

                    handleFocusForWidget();
                }
            }

            @Override
            protected Element getElementTdOrTr(Element eventTarget) {
                Widget widget = WidgetUtil.findWidget(eventTarget, null);
                Widget targetWidget = widget;

                if (widget != this) {
                    /*
                     * This is a workaround to make Labels, read only TextFields
                     * and Embedded in a Table clickable (see #2688). It is
                     * really not a fix as it does not work with a custom read
                     * only components (not extending VLabel/VEmbedded).
                     */
                    while (widget != null && widget.getParent() != this) {
                        widget = widget.getParent();
                    }

                    if (!(widget instanceof VLabel)
                            && !(widget instanceof VEmbedded)
                            && !(widget instanceof VTextField && ((VTextField) widget).isReadOnly())
                            && !(targetWidget instanceof VLabel)
                            && !(targetWidget instanceof Panel)
                            && !(targetWidget instanceof VEmbedded)
                            && !(widget instanceof CubaImageWidget)
                            && !(targetWidget instanceof VTextField && ((VTextField) targetWidget).isReadOnly())) {
                        return null;
                    }
                }
                return getTdOrTr(eventTarget);
            }

            @Override
            protected void beforeAddCell(String columnKey) {
                currentColumnKey = columnKey;
            }

            @Override
            protected void afterAddCell(String columnKey) {
                currentColumnKey = null;
            }

            @Override
            protected void initCellWithText(String text, char align, String style, boolean textIsHTML,
                                            boolean sorted, String description, TableCellElement td) {
                super.initCellWithText(text, align, style, textIsHTML, sorted, description, td);

                Element tdElement = td.cast();
                Tools.textSelectionEnable(tdElement, _delegate.textSelectionEnabled);

                if (_delegate.clickableColumns != null && _delegate.clickableColumns.contains(currentColumnKey)) {
                    Element wrapperElement = tdElement.getFirstChildElement();
                    final Element clickableSpan = DOM.createSpan().cast();
                    clickableSpan.setClassName(CUBA_TABLE_CLICKABLE_CELL_STYLE);

                    clickableSpan.setInnerText(wrapperElement.getInnerText());

                    wrapperElement.removeAllChildren();
                    DOM.appendChild(wrapperElement, clickableSpan);
                }

                if (_delegate.multiLineCells) {
                    Style wrapperStyle = tdElement.getFirstChildElement().getStyle();
                    wrapperStyle.setWhiteSpace(Style.WhiteSpace.PRE_LINE);
                }
            }

            @Override
            public void showContextMenu(Event event) {
                if (_delegate.contextMenuEnabled && enabled && (_delegate.customContextMenu != null || actionKeys != null)) {
                    // Show context menu if there are registered action handlers
                    int left = WidgetUtil.getTouchOrMouseClientX(event)
                            + Window.getScrollLeft();
                    int top = WidgetUtil.getTouchOrMouseClientY(event)
                            + Window.getScrollTop();

                    selectRowForContextMenuActions(event);

                    showContextMenu(left, top);
                }
            }

            @Override
            public void showContextMenu(int left, int top) {
                if (_delegate.customContextMenu != null) {
                    _delegate.showContextMenuPopup(left, top);
                } else {
                    super.showContextMenu(left, top);
                }
            }

            protected void selectRowForContextMenuActions(Event event) {
                boolean clickEventSent = handleClickEvent(event, getElement(), false);
                if (CubaTreeTableWidget.this.isSelectable()) {
                    boolean currentlyJustThisRowSelected = selectedRowKeys
                            .size() == 1
                            && selectedRowKeys.contains(getKey());

                    boolean selectionChanged = false;
                    if (!isSelected()) {
                        if (!currentlyJustThisRowSelected) {
                            if (isSingleSelectMode()
                                    || isMultiSelectModeDefault()) {
                                deselectAll();
                            }
                            toggleSelection();
                        } else if ((isSingleSelectMode() || isMultiSelectModeSimple())
                                && nullSelectionAllowed) {
                            toggleSelection();
                        }

                        selectionChanged = true;
                    }

                    if (selectionChanged) {
                        selectionRangeStart = this;
                        setRowFocus(this);

                        // Queue value change
                        sendSelectedRows(true);
                    }
                }
                if (immediate || clickEventSent) {
                    client.sendPendingVariableChanges();
                }
            }

            @Override
            protected void updateCellStyleNames(TableCellElement td, String primaryStyleName) {
                Element container = td.getFirstChild().cast();
                boolean isWidget = container.getClassName() != null
                        && container.getClassName().contains(WIDGET_CELL_CLASSNAME);

                super.updateCellStyleNames(td, primaryStyleName);

                if (isWidget) {
                    container.addClassName(WIDGET_CELL_CLASSNAME);
                }
            }

            @Override
            protected boolean hasContextMenuActions() {
                if (_delegate.contextMenuEnabled && _delegate.customContextMenu != null) {
                    return true;
                }

                return super.hasContextMenuActions();
            }
        }

        public List<Widget> getRenderedRows() {
            return renderedRows;
        }
    }

    public void requestFocus(final String itemKey, final String columnKey) {
        _delegate.requestFocus(itemKey, columnKey);
    }

    public void showCustomPopup() {
        _delegate.showCustomPopup();
    }

    @Override
    protected boolean isColumnCollapsingEnabled() {
        return (columnOrder.length - 1) > collapsedColumns.size();
    }

    @Override
    public void updateColumnProperties(UIDL uidl) {
        super.updateColumnProperties(uidl);

        if (uidl.hasAttribute("colcubaids")
                && uidl.hasAttribute("vcolorder")) {
            try {
                String[] vcolorder = uidl.getStringArrayAttribute("vcolorder");
                String[] colcubaids = uidl.getStringArrayAttribute("colcubaids");

                Map<String, HeaderCell> headerCellMap = new HashMap<>();
                for (int i = 0; i < getHead().getVisibleCellCount(); i++) {
                    HeaderCell headerCell = getHead().getHeaderCell(i);
                    if (headerCell.getColKey() != null) {
                        headerCellMap.put(headerCell.getColKey(), headerCell);
                    }
                }

                for (int i = 0; i < vcolorder.length; i++) {
                    String key = vcolorder[i];
                    HeaderCell headerCell = headerCellMap.get(key);

                    if (headerCell != null) {
                        headerCell.getElement().setAttribute("cuba-id", "column_" + colcubaids[i]);
                    }
                }
            } catch (Exception e) {
                Logger.getLogger("CubaTreeTableWidget").log(Level.SEVERE,
                        "Unable to init cuba-ids for columns " + e.getMessage());
            }
        }
    }

    public void updateTableBodyScroll() {
        if (willHaveScrollbars()) {
            scrollBodyPanel.getElement().getStyle().clearOverflowY();
        } else {
            scrollBodyPanel.getElement().getStyle()
                    .setOverflowY(Style.Overflow.HIDDEN);
        }
    }

    @Override
    public void updateBody(UIDL uidl, int firstRow, int reqRows) {
        super.updateBody(uidl, firstRow, reqRows);

        // Have to scroll after row is expanded when table has vertical scroll
        if (BrowserInfo.get().isFirefox()) {
            enableLazyScroller();
        }

        handleUpdateBodyRows();
    }

    @Override
    public void initializeRows(UIDL uidl, UIDL rowData) {
        super.initializeRows(uidl, rowData);

        handleUpdateBodyRows();
    }

    @Override
    public void updateRowsInBody(UIDL partialRowUpdates) {
        super.updateRowsInBody(partialRowUpdates);

        handleUpdateBodyRows();
    }

    protected void handleUpdateBodyRows() {
        _delegate.handleUpdateBody();
    }

    @Override
    public void addAfterBodyUpdateListener(AfterBodyUpdateListener listener) {
        _delegate.addAfterUpdateBodyListener(listener);
    }

    @Override
    public void removeAfterBodyUpdateListener(AfterBodyUpdateListener listener) {
        _delegate.removeAfterUpdateListener(listener);
    }

    // CAUTION: copied from com.vaadin.v7.client.ui.VScrollTable.hasVerticalScrollbar
    @Override
    public boolean hasVerticalScrollbar() {
        return scrollBody.getOffsetHeight() > scrollBodyPanel.getOffsetHeight();
    }
}