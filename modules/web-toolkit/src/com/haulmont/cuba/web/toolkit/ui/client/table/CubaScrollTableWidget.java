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

package com.haulmont.cuba.web.toolkit.ui.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.haulmont.cuba.web.toolkit.ui.client.aggregation.AggregatableTable;
import com.haulmont.cuba.web.toolkit.ui.client.aggregation.TableAggregationRow;
import com.vaadin.client.*;
import com.vaadin.client.Focusable;
import com.vaadin.client.ui.*;

import java.util.Set;

import static com.haulmont.cuba.web.toolkit.ui.client.Tools.isAnyModifierKeyPressed;

public class CubaScrollTableWidget extends VScrollTable implements ShortcutActionHandler.ShortcutActionHandlerOwner, HasEnabled {

    public static final String CUBA_TABLE_CLICKABLE_CELL_STYLE = "cuba-table-clickable-cell";
    public static final String CUBA_TABLE_CLICKABLE_TEXT_STYLE = "cuba-table-clickable-text";

    protected static final String WIDGET_CELL_CLASSNAME = "widget-container";

    protected ShortcutActionHandler shortcutHandler;

    protected boolean textSelectionEnabled = false;
    protected boolean contextMenuEnabled = true;

    protected VOverlay presentationsEditorPopup;
    protected VOverlay customContextMenuPopup;

    protected Widget presentationsMenu;
    protected Widget customContextMenu;

    protected boolean multiLineCells = false;

    protected TableAggregationRow aggregationRow;

    protected Set<String> clickableColumns;
    protected TableCellClickListener cellClickListener;

    protected VOverlay customPopupOverlay;
    protected Widget customPopupWidget;
    protected boolean customPopupAutoClose = false;
    protected int lastClickClientX;
    protected int lastClickClientY;

    protected CubaScrollTableWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);

        hideColumnControlAfterClick = false;
    }

    @Override
    protected VScrollTableBody.VScrollTableRow getNextRowToFocus(VScrollTableBody.VScrollTableRow currentRow, int offset) {
        // Support select first N rows by Shift+Click #PL-3267
        if (focusedRow == currentRow && !focusedRow.isSelected()) {
            if (currentRow instanceof CubaScrollTableBody.CubaScrollTableRow) {
                CubaScrollTableBody.CubaScrollTableRow row = (CubaScrollTableBody.CubaScrollTableRow) currentRow;
                if (row.isSelectable()) {
                    return focusedRow;
                }
            }
        }

        return super.getNextRowToFocus(currentRow, offset);
    }

    @Override
    protected boolean needToSelectFocused(VScrollTableBody.VScrollTableRow currentRow) {
        // Support select first N rows by Shift+Click #PL-3267
        return currentRow == focusedRow && (!focusedRow.isSelected());
    }

    @Override
    public void scheduleLayoutForChildWidgets() {
        if (scrollBody != null) {
            // Fix for #VAADIN-12970, relayout cell widgets
            // Haulmont API
            ComponentConnector connector = Util.findConnectorFor(this);
            LayoutManager lm = connector.getLayoutManager();

            for (Widget w : scrollBody) {
                HasWidgets row = (HasWidgets) w;
                for (Widget child : row) {
                    ComponentConnector childConnector = Util.findConnectorFor(child);
                    if (childConnector != null && childConnector.getConnectorId() != null) {
                        if (childConnector instanceof ManagedLayout
                                || childConnector instanceof AbstractLayoutConnector) {
                            lm.setNeedsMeasure(childConnector);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        this.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    public void setPresentationsMenu(Widget presentationsMenu) {
        if (this.presentationsMenu != presentationsMenu) {
            Style presentationsIconStyle = ((CubaScrollTableHead) tHead).presentationsEditIcon.getElement().getStyle();
            if (presentationsMenu == null) {
                presentationsIconStyle.setDisplay(Style.Display.NONE);
            } else {
                presentationsIconStyle.setDisplay(Style.Display.BLOCK);
            }
        }
        this.presentationsMenu = presentationsMenu;
    }

    @Override
    protected VScrollTableBody createScrollBody() {
        return new CubaScrollTableBody();
    }

    @Override
    public boolean handleBodyContextMenu(int left, int top) {
        if (contextMenuEnabled) {
            if (customContextMenu == null) {
                return super.handleBodyContextMenu(left, top);
            } else if (enabled && !selectedRowKeys.isEmpty()) {
                showContextMenuPopup(left, top);

                return true;
            }
        }
        return false;
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
    protected void setColWidth(int colIndex, int w, boolean isDefinedWidth) {
        super.setColWidth(colIndex, w, isDefinedWidth);

        if (aggregationRow != null && aggregationRow.isInitialized()) {
            aggregationRow.setCellWidth(colIndex, w);
        }
    }

    @Override
    public int getAdditionalRowsHeight() {
        if (aggregationRow != null) {
            return aggregationRow.getOffsetHeight();
        }
        return 0;
    }

    @Override
    protected TableHead createTableHead() {
        return new CubaScrollTableHead();
    }

    public void updateTextSelection() {
        Tools.textSelectionEnable(scrollBody.getElement(), textSelectionEnabled);
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        if (presentationsEditorPopup != null) {
            presentationsEditorPopup.hide();
        }

        if (customContextMenuPopup != null) {
            customContextMenuPopup.hide();
        }
    }

    protected void updateAggregationRow(UIDL uidl) {
        if (aggregationRow == null) {
            aggregationRow = createAggregationRow();
            insert(aggregationRow, getWidgetIndex(scrollBodyPanel));
        }
        aggregationRow.updateFromUIDL(uidl);
        aggregationRow.setHorizontalScrollPosition(scrollLeft);
    }

    protected TableAggregationRow createAggregationRow() {
        return new TableAggregationRow(getAggregatableTable());
    }

    protected AggregatableTable getAggregatableTable() {
        return new AggregatableTable() {
            @Override
            public TableHead getHead() {
                return tHead;
            }

            @Override
            public String getStylePrimaryName() {
                return CubaScrollTableWidget.this.getStylePrimaryName();
            }

            @Override
            public String[] getVisibleColOrder() {
                return visibleColOrder;
            }

            @Override
            public String getColKeyByIndex(int index) {
                return CubaScrollTableWidget.this.getColKeyByIndex(index);
            }

            @Override
            public int getColWidth(String colKey) {
                return CubaScrollTableWidget.this.getColWidth(colKey);
            }

            @Override
            public void setColWidth(int colIndex, int w, boolean isDefinedWidth) {
                CubaScrollTableWidget.this.setColWidth(colIndex, w, isDefinedWidth);
            }

            @Override
            public boolean isTextSelectionEnabled() {
                return textSelectionEnabled;
            }
        };
    }

    @Override
    public void onScroll(ScrollEvent event) {
        if (isLazyScrollerActive()) {
            return;
        }

        super.onScroll(event);

        if (aggregationRow != null) {
            aggregationRow.setHorizontalScrollPosition(scrollLeft);
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

    protected class CubaScrollTableHead extends TableHead {

        protected final SimplePanel presentationsEditIcon = GWT.create(SimplePanel.class);

        public CubaScrollTableHead() {
            Element iconElement = presentationsEditIcon.getElement();
            iconElement.setClassName("cuba-table-presentations-icon");
            iconElement.getStyle().setDisplay(Style.Display.NONE);

            Element columnSelector = (Element) getElement().getLastChild();
            DOM.insertChild(getElement(), iconElement, DOM.getChildIndex(getElement(), columnSelector));

            DOM.sinkEvents(iconElement, Event.ONCLICK);
        }

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);

            if (event.getEventTarget().cast() == presentationsEditIcon.getElement() && isEnabled()) {
                presentationsEditorPopup = new VOverlay();
                presentationsEditorPopup.setStyleName("cuba-table-presentations-editor");
                presentationsEditorPopup.setOwner(CubaScrollTableWidget.this);
                presentationsEditorPopup.setWidget(presentationsMenu);

                presentationsEditorPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> event) {
                        presentationsEditorPopup = null;
                    }
                });

                presentationsEditorPopup.setAutoHideEnabled(true);
                presentationsEditorPopup.showRelativeTo(presentationsEditIcon);
            }
        }

        @Override
        protected HeaderCell createHeaderCell(String cid, String caption) {
            return new CubaScrollTableHeaderCell(cid, caption);
        }
    }

    protected class CubaScrollTableHeaderCell extends HeaderCell {

        protected int sortClickCounter = 0;

        public CubaScrollTableHeaderCell(String colId, String headerText) {
            super(colId, headerText);
        }

        @Override
        public void setText(String headerText) {
            if ("".equals(headerText) || headerText == null) {
                headerText = "&nbsp;";
            }

            super.setText(headerText);
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
                            sortAscending = true;

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
                rowRequestHandler.setReqRows((int) (2 * pageLength
                        * cache_rate + pageLength));
                rowRequestHandler.deferRowFetch(); // some validation +
                // defer 250ms
                rowRequestHandler.cancel(); // instead of waiting
                rowRequestHandler.run(); // run immediately
            }
        }
    }

    protected class CubaScrollTableBody extends VScrollTableBody {

        protected Widget lastFocusedWidget = null;

        @Override
        protected VScrollTableRow createRow(UIDL uidl, char[] aligns2) {
            if (uidl.hasAttribute("gen_html")) {
                // This is a generated row.
                return new VScrollTableGeneratedRow(uidl, aligns2);
            }
            return new CubaScrollTableRow(uidl, aligns2);
        }

        protected class CubaScrollTableRow extends VScrollTableRow {

            protected String currentColumnKey = null;
            protected boolean selectable = true;

            public CubaScrollTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void initCellWithWidget(final Widget w, char align,
                                              String style, boolean sorted, TableCellElement td) {
                super.initCellWithWidget(w, align, style, sorted, td);

                td.getFirstChildElement().addClassName(WIDGET_CELL_CLASSNAME);

                if (CubaScrollTableWidget.this.isSelectable()) {
                    // Support for #PL-2080
                    recursiveAddFocusHandler(w, w);
                }
            }

            protected void recursiveAddFocusHandler(final Widget w, final Widget topWidget) {
                if (w instanceof HasWidgets) {
                    for (Widget child: (HasWidgets)w) {
                        recursiveAddFocusHandler(child, topWidget);
                    }
                }

                if (w instanceof HasFocusHandlers) {
                    ((HasFocusHandlers) w).addFocusHandler(new FocusHandler() {
                        @Override
                        public void onFocus(FocusEvent event) {
                            if (childWidgets.indexOf(topWidget) < 0) {
                                return;
                            }

                            lastFocusedWidget = w;

                            if (!isSelected()) {
                                deselectAll();

                                toggleSelection();
                                setRowFocus(CubaScrollTableRow.this);

                                sendSelectedRows();
                            }
                        }
                    });
                }
            }

            protected void handleFocusForWidget() {
                if (lastFocusedWidget == null) {
                    return;
                }

                if (isSelected()) {
                    if (lastFocusedWidget instanceof Focusable) {
                        ((Focusable) lastFocusedWidget).focus();
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
                        && !isAnyModifierKeyPressed(event)
                        && isCubaTableClickableCell(event)) {

                    Element eventTarget = event.getEventTarget().cast();
                    Element elementTdOrTr = getElementTdOrTr(eventTarget);

                    int childIndex = DOM.getChildIndex(getElement(), elementTdOrTr);
                    String columnKey = tHead.getHeaderCell(childIndex).getColKey();
                    if (columnKey != null) {
                        WidgetUtil.TextRectangle rect = WidgetUtil.getBoundingClientRect(eventTarget);
                        lastClickClientX = (int) Math.ceil(rect.getLeft());
                        lastClickClientY = (int) Math.ceil(rect.getBottom());

                        if (cellClickListener != null) {
                            cellClickListener.onClick(columnKey, rowKey);

                            event.preventDefault();
                            event.stopPropagation();

                            return;
                        }
                    }
                }

                if (event.getTypeInt() == Event.ONDBLCLICK && isCubaTableClickableCell(event)) {
                        return;
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

            protected boolean isCubaTableClickableCell(Event event) {
                Element eventTarget = event.getEventTarget().cast();
                Element elementTdOrTr = getElementTdOrTr(eventTarget);

                if (elementTdOrTr != null
                        && "td".equalsIgnoreCase(elementTdOrTr.getTagName())
                        && !elementTdOrTr.hasClassName(CUBA_TABLE_CLICKABLE_TEXT_STYLE)) {
                    // found <td>

                    if ("span".equalsIgnoreCase(eventTarget.getTagName())
                            && eventTarget.hasClassName(CUBA_TABLE_CLICKABLE_CELL_STYLE)) {
                        // found <span class="cuba-table-clickable-cell">
                        return true;
                    }
                }
                return false;
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

                final Element tdElement = td.cast();
                Tools.textSelectionEnable(tdElement, textSelectionEnabled);

                if (clickableColumns != null && clickableColumns.contains(currentColumnKey)) {
                    Element wrapperElement = tdElement.getFirstChildElement();
                    final Element clickableSpan = DOM.createSpan().cast();
                    clickableSpan.setClassName(CUBA_TABLE_CLICKABLE_CELL_STYLE);

                    clickableSpan.setInnerText(wrapperElement.getInnerText());

                    wrapperElement.removeAllChildren();
                    DOM.appendChild(wrapperElement, clickableSpan);
                }

                if (multiLineCells) {
                    Style wrapperStyle = tdElement.getFirstChildElement().getStyle();
                    wrapperStyle.setWhiteSpace(Style.WhiteSpace.PRE_LINE);
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
            public void showContextMenu(Event event) {
                if (contextMenuEnabled && enabled && (customContextMenu != null || actionKeys != null)) {
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
                if (customContextMenu != null) {
                    showContextMenuPopup(left, top);
                } else {
                    super.showContextMenu(left, top);
                }
            }

            protected void selectRowForContextMenuActions(Event event) {
                boolean clickEventSent = handleClickEvent(event, getElement(), false);
                if (CubaScrollTableWidget.this.isSelectable()) {
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
            public void toggleSelection() {
                if (selectable) {
                    super.toggleSelection();
                }
            }

            public boolean isSelectable() {
                return selectable;
            }
        }
    }

    protected void showContextMenuPopup(int left, int top) {
        if (customContextMenu instanceof HasWidgets) {
            if (!((HasWidgets) customContextMenu).iterator().hasNext()) {
                // there are no actions to show
                return;
            }
        }

        customContextMenuPopup = Tools.createCubaTableContextMenu();
        customContextMenuPopup.setOwner(this);
        customContextMenuPopup.setWidget(customContextMenu);

        customContextMenuPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                customContextMenuPopup = null;
            }
        });

        Tools.showPopup(customContextMenuPopup, left, top);
    }

    public void showCustomPopup() {
        if (customPopupWidget != null) {
            if (customPopupWidget instanceof HasWidgets) {
                if (!((HasWidgets) customPopupWidget).iterator().hasNext()) {
                    // there are no component to show
                    return;
                }
            }

            customPopupOverlay = Tools.createCubaTablePopup(customPopupAutoClose);
            customPopupOverlay.setOwner(this);
            customPopupOverlay.setWidget(customPopupWidget);

            customPopupOverlay.addCloseHandler(new CloseHandler<PopupPanel>() {
                @Override
                public void onClose(CloseEvent<PopupPanel> event) {
                    customPopupOverlay = null;
                }
            });

            Tools.showPopup(customPopupOverlay, lastClickClientX, lastClickClientY);
        }
    }

    @Override
    protected boolean isColumnCollapsingEnabled() {
        return visibleColOrder.length > 1;
    }
}