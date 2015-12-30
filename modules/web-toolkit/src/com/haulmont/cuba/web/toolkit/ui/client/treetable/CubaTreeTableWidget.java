/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.treetable;

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
import com.haulmont.cuba.web.toolkit.ui.client.table.TableCellClickListener;
import com.vaadin.client.*;
import com.vaadin.client.ui.*;

import java.util.Set;

import static com.haulmont.cuba.web.toolkit.ui.client.Tools.isAnyModifierKeyPressed;
import static com.haulmont.cuba.web.toolkit.ui.client.table.CubaScrollTableWidget.CUBA_TABLE_CLICKABLE_CELL_STYLE;
import static com.haulmont.cuba.web.toolkit.ui.client.table.CubaScrollTableWidget.CUBA_TABLE_CLICKABLE_TEXT_STYLE;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTreeTableWidget extends VTreeTable implements ShortcutActionHandler.ShortcutActionHandlerOwner, HasEnabled {

    protected static final String WIDGET_CELL_CLASSNAME = "widget-container";

    protected boolean textSelectionEnabled = false;
    protected boolean contextMenuEnabled = true;

    protected VOverlay presentationsEditorPopup;
    protected VOverlay customContextMenuPopup;

    protected ShortcutActionHandler shortcutHandler;

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

    protected CubaTreeTableWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);

        hideColumnControlAfterClick = false;
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
        // CAUTION Do not use multiselect mode SIMPLE for touch devices, it may be laptop with touch screen
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
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
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

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        this.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
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
        return new CubaTreeTableTableHead();
    }

    public void setPresentationsMenu(Widget presentationsMenu) {
        if (this.presentationsMenu != presentationsMenu) {
            Style presentationsIconStyle = ((CubaTreeTableTableHead) tHead).presentationsEditIcon.getElement().getStyle();
            if (presentationsMenu == null) {
                presentationsIconStyle.setDisplay(Style.Display.NONE);
            } else {
                presentationsIconStyle.setDisplay(Style.Display.BLOCK);
            }
        }
        this.presentationsMenu = presentationsMenu;
    }

    public void updateTextSelection() {
        Tools.textSelectionEnable(scrollBody.getElement(), textSelectionEnabled);
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
        return new TableAggregationRow(new AggregatableTable() {
            @Override
            public TableHead getHead() {
                return tHead;
            }

            @Override
            public String getStylePrimaryName() {
                return CubaTreeTableWidget.this.getStylePrimaryName();
            }

            @Override
            public String[] getVisibleColOrder() {
                return visibleColOrder;
            }

            @Override
            public String getColKeyByIndex(int index) {
                return CubaTreeTableWidget.this.getColKeyByIndex(index);
            }

            @Override
            public int getColWidth(String colKey) {
                return CubaTreeTableWidget.this.getColWidth(colKey);
            }

            @Override
            public void setColWidth(int colIndex, int w, boolean isDefinedWidth) {
                CubaTreeTableWidget.this.setColWidth(colIndex, w, isDefinedWidth);
            }

            @Override
            public boolean isTextSelectionEnabled() {
                return textSelectionEnabled;
            }
        });
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

    protected class CubaTreeTableTableHead extends TableHead {

        protected final SimplePanel presentationsEditIcon = GWT.create(SimplePanel.class);

        public CubaTreeTableTableHead() {
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
                presentationsEditorPopup.setOwner(CubaTreeTableWidget.this);
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
            return new CubaTreeTableHeaderCell(cid, caption);
        }
    }

    protected class CubaTreeTableHeaderCell extends HeaderCell {

        protected int sortClickCounter = 0;

        public CubaTreeTableHeaderCell(String colId, String headerText) {
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
                                setRowFocus(CubaTreeTableRow.this);

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
                            // found <span class="cuba-table-clickable-cell">

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
        }
    }

    protected void showContextMenuPopup(int left, int top) {
        if (customContextMenu instanceof HasWidgets) {
            if (!((HasWidgets) customContextMenu).iterator().hasNext()) {
                // there are no actions to show
                return;
            }
        }

        customContextMenuPopup = Tools.createCubaContextMenu();
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