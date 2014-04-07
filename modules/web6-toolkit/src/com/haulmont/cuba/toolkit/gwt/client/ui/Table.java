/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.toolkit.gwt.client.ColumnWidth;
import com.haulmont.cuba.toolkit.gwt.client.Tools;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.Focusable;
import com.vaadin.terminal.gwt.client.ui.*;
import com.vaadin.terminal.gwt.client.ui.dd.VDragAndDropManager;
import com.vaadin.terminal.gwt.client.ui.dd.VDragEvent;
import com.vaadin.terminal.gwt.client.ui.dd.VTransferable;
import org.vaadin.hene.popupbutton.widgetset.client.ui.VPopupButton;

import java.util.*;

public abstract class Table
        extends
            FlowPanel
        implements
            com.vaadin.terminal.gwt.client.ui.Table, ShortcutActionHandler.ShortcutActionHandlerOwner,
            ScrollHandler, FocusHandler, BlurHandler, Focusable {

    public static final String CLASSNAME = "v-table";
    public static final String CLASSNAME_SELECTION_FOCUS = CLASSNAME + "-focus";

    public static final char ALIGN_CENTER = 'c';
    public static final char ALIGN_LEFT = 'b';
    public static final char ALIGN_RIGHT = 'e';
    private static final int CHARCODE_SPACE = 32;
    protected int pageLength = 15;

    protected String id;

    protected ShortcutActionHandler shortcutHandler;

    protected boolean showRowHeaders = false;

    protected String[] columnOrder;

    protected ApplicationConnection client;
    protected String paintableId;

    protected boolean immediate;

    protected int selectMode = com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE;

    protected final Set<String> selectedRowKeys = new HashSet<String>();

    protected boolean initializedAndAttached = false;

    protected final TableHead tHead;

    protected final FocusableScrollPanel bodyContainer = new FocusableScrollPanel(); // [6.6]

    // For fix bug overflow:auto in webkit
    protected Widget parentOverflowContainer;

    protected final int TABLE_MAX_WIDTH = 30000;

    protected int sortClickCounter = 0;

    //[6.6]
    private KeyPressHandler navKeyPressHandler = new KeyPressHandler() {

        @Override
        public void onKeyPress(KeyPressEvent keyPressEvent) {
            // This is used for Firefox only, since Firefox auto-repeat
            // works correctly only if we use a key press handler, other
            // browsers handle it correctly when using a key down handler
            if (!BrowserInfo.get().isGecko()) {
                return;
            }

            NativeEvent event = keyPressEvent.getNativeEvent();
            if (!enabled) {
                // Cancel default keyboard events on a disabled Table
                // (prevents scrolling)
                event.preventDefault();
            } else if (hasFocus) {
                // Key code in Firefox/onKeyPress is present only for
                // special keys, otherwise 0 is returned
                int keyCode = event.getKeyCode();
                if (keyCode == 0 && event.getCharCode() == ' ') {
                    // Provide a keyCode for space to be compatible with
                    // FireFox keypress event
                    keyCode = CHARCODE_SPACE;
                }

                if (handleNavigation(keyCode,
                        event.getCtrlKey() || event.getMetaKey(),
                        event.getShiftKey())) {
                    event.preventDefault();
                }

                startScrollingVelocityTimer();
            }
        }
    };

    private KeyUpHandler navKeyUpHandler = new KeyUpHandler() {

        @Override
        public void onKeyUp(KeyUpEvent keyUpEvent) {
            NativeEvent event = keyUpEvent.getNativeEvent();
            int keyCode = event.getKeyCode();

            if (!isFocusable()) {
                cancelScrollingVelocityTimer();
            } else if (isNavigationKey(keyCode)) {
                if (keyCode == getNavigationDownKey()
                        || keyCode == getNavigationUpKey()) {
                    /*
                     * in multiselect mode the server may still have value from
                     * previous page. Clear it unless doing multiselection or
                     * just moving focus.
                     */
                    if (!event.getShiftKey() && !event.getCtrlKey()) {
                        instructServerToForgotPreviousSelections();
                    }
                    sendSelectedRows();
                }
                cancelScrollingVelocityTimer();
                navKeyDown = false;
            } else if (KeyCodes.KEY_ENTER == keyCode && !keyUpEvent.isAnyModifierKeyDown()) {
                event.stopPropagation();
                client.updateVariable(paintableId, "enterPressed", "", immediate);
            }
        }
    };

    private KeyDownHandler navKeyDownHandler = new KeyDownHandler() {

        @Override
        public void onKeyDown(KeyDownEvent keyDownEvent) {
            NativeEvent event = keyDownEvent.getNativeEvent();
            // This is not used for Firefox
            if (BrowserInfo.get().isGecko()) {
                return;
            }

            if (!enabled) {
                // Cancel default keyboard events on a disabled Table
                // (prevents scrolling)
                event.preventDefault();
            } else if (hasFocus) {
                if (handleNavigation(event.getKeyCode(), event.getCtrlKey()
                        || event.getMetaKey(), event.getShiftKey())) {
                    navKeyDown = true;
                    event.preventDefault();
                }

                startScrollingVelocityTimer();
            }
        }
    };

    protected int totalRows;

    protected Set<String> collapsedColumns;

    protected ITableBody tBody;

    protected boolean sortAscending;
    protected String sortColumn;
    protected boolean enableCancelSorting = false;
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

    /**
     * flag to indicate that table body has changed
     */
    protected boolean isNewBody = true;

    protected boolean emitClickEvents;

    /*
     * Read from the "recalcWidths" -attribute. When it is true, the table will
     * recalculate the widths for columns - desirable in some cases. For #1983,
     * marked experimental.
     */
    protected boolean recalcWidths = false;

    protected String scrollbarWidthReservedInColumnKey = null;

    protected int scrollbarWidthReserved = -1;
    protected boolean relativeWidth = false;

    protected int calculatedWidth = -1;

    protected final Set<Widget> lazyUnregistryBag = new HashSet<Widget>();
    protected String height;
    protected String width = "";

    protected boolean allowMultiStingCells = false;
    protected boolean nullSelectionDisallowed = false;

    protected boolean storeColWidth = false;

    protected AggregationRow aggregationRow = null;

    //Key down navigation
    protected boolean navigation = true;
    protected String selectedKey;

//    disabled due to #PL-3049
//    protected int focusWidgetIndex = -1;

    private boolean hasFocus = false;
    private int dragmode;

    private int scrollLeft;
    private int scrollTop;
    private boolean navKeyDown;
    protected boolean multiselectPending;

    protected boolean textSelectionEnabled;

    // Custom paddings in all cells is broken
    // Used only one padding value for all cells
    // See Platform #1112
    protected RenderInformation.Size defaultPaddings = null;
    protected Map<String, RenderInformation.Size> stylePaddingBorders = new HashMap<String, RenderInformation.Size>();

    /*
     * The speed (in pixels) which the scrolling scrolls vertically/horizontally
     */
    private int scrollingVelocity = 10;

    private Timer scrollingVelocityTimer = null;

    /**
     * Represents a select range of rows
     */
    private class SelectionRange {
        private ITableBody.ITableRow startRow;
        private int length;

        /**
         * Constuctor.
         */
        @SuppressWarnings({"JavaDoc"})
        public SelectionRange(ITableBody.ITableRow row1, ITableBody.ITableRow row2) {
            ITableBody.ITableRow endRow;
            if (row2.isBefore(row1)) {
                startRow = row2;
                endRow = row1;
            } else {
                startRow = row1;
                endRow = row2;
            }
            length = endRow.getIndex() - startRow.getIndex() + 1;
        }

        public SelectionRange(ITableBody.ITableRow row, int length) {
            startRow = row;
            this.length = length;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */

        @Override
        public String toString() {
            return startRow.getKey() + "-" + length;
        }

        private boolean inRange(ITableBody.ITableRow row) {
            return row.getIndex() >= startRow.getIndex()
                    && row.getIndex() < startRow.getIndex() + length;
        }

        public Collection<SelectionRange> split(ITableBody.ITableRow row) {
            assert row.isAttached();
            ArrayList<SelectionRange> ranges = new ArrayList<SelectionRange>(2);

            int endOfFirstRange = row.getIndex() - 1;
            if (!(endOfFirstRange - startRow.getIndex() < 0)) {
                // create range of first part unless its length is < 1
                ITableBody.ITableRow endOfRange = tBody
                        .getRowByRowIndex(endOfFirstRange);
                ranges.add(new SelectionRange(startRow, endOfRange));
            }
            int startOfSecondRange = row.getIndex() + 1;
            if (!(getEndIndex() - startOfSecondRange < 0)) {
                // create range of second part unless its length is < 1
                ITableBody.ITableRow startOfRange = tBody
                        .getRowByRowIndex(startOfSecondRange);
                ranges.add(new SelectionRange(startOfRange, getEndIndex()
                        - startOfSecondRange + 1));
            }
            return ranges;
        }

        private int getEndIndex() {
            return startRow.getIndex() + length - 1;
        }
    }

    private final HashSet<SelectionRange> selectedRowRanges = new HashSet<SelectionRange>();

    /*
     * These are used when jumping between pages when pressing Home and End
     */
    protected boolean selectLastItemInNextRender = false;
    protected boolean selectFirstItemInNextRender = false;
    protected boolean focusFirstItemInNextRender = false;
    protected boolean focusLastItemInNextRender = false;

    /*
     * The currently focused row
     */
    protected ITableBody.ITableRow focusedRow;

    /*
     * Helper to store selection range start in when using the keyboard
     */
    private ITableBody.ITableRow selectionRangeStart;

    private static final int MULTISELECT_MODE_DEFAULT = 0;
    private int multiselectmode;

    private TouchScrollDelegate touchScrollDelegate;

    /*
     * Flag for notifying when the selection has changed and should be sent to
     * the server
     */
    protected boolean selectionChanged = false;

    protected Table() {
        tHead = createHead();
        bodyContainer.setStyleName(CLASSNAME + "-body");
        bodyContainer.addFocusHandler(this);
        bodyContainer.addBlurHandler(this);
        bodyContainer.addScrollHandler(this);

        /*
         * Firefox auto-repeat works correctly only if we use a key press
         * handler, other browsers handle it correctly when using a key down
         * handler
         */
        if (BrowserInfo.get().isGecko()) {
            bodyContainer.addKeyPressHandler(navKeyPressHandler);
        } else {
            bodyContainer.addKeyDownHandler(navKeyDownHandler);
        }
        bodyContainer.addKeyUpHandler(navKeyUpHandler);

        bodyContainer.sinkEvents(Event.TOUCHEVENTS);
        bodyContainer.addDomHandler(new TouchStartHandler() {
            @Override
            public void onTouchStart(TouchStartEvent event) {
                getTouchScrollDelegate().onTouchStart(event);
            }
        }, TouchStartEvent.getType());

        bodyContainer.sinkEvents(Event.ONCONTEXTMENU);

        bodyContainer.addDomHandler(new ContextMenuHandler() {
            @Override
            public void onContextMenu(ContextMenuEvent event) {
                //handleBodyContextMenu(event); //todo [6.6]
            }
        }, ContextMenuEvent.getType());

        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);

        setStyleName(CLASSNAME);
        add(tHead);
        add(bodyContainer);
    }

    protected TouchScrollDelegate getTouchScrollDelegate() {
        if (touchScrollDelegate == null) {
            touchScrollDelegate = new TouchScrollDelegate(
                    bodyContainer.getElement());
        }
        return touchScrollDelegate;

    }

    @Override
    public void onScroll(ScrollEvent event) {
        int scrollLeft = bodyContainer.getElement().getScrollLeft();
        tHead.setHorizontalScrollPosition(scrollLeft);
        if (aggregationRow != null) {
            aggregationRow.setHorizontalScrollPosition(scrollLeft);
        }
    }

    protected abstract ITableBody createBody();

    protected abstract TableHead createHead();

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        id = uidl.getId();
        paintableId = uidl.getStringAttribute("id");

        updateFromUIDL(uidl);

        // We may have actions attached to this panel
        if (uidl.getChildCount() > 1) {
            final int cnt = uidl.getChildCount();
            for (int i = 1; i < cnt; i++) {
                UIDL childUidl = uidl.getChildUIDL(i);
                if (childUidl.getTag().equals("actions")) {
                    if (shortcutHandler == null) {
                        shortcutHandler = new ShortcutActionHandler(id, client);
                    }
                    shortcutHandler.updateActionMap(childUidl);
                }
            }
        }

        if (BrowserInfo.get().getWebkitVersion() > 0) {
            if (parentOverflowContainer == null) {
                Widget w = this;
                Container container;
                while ((container = Util.getLayout(w)) != null) {
                    w = (Widget) container;
                    if ((w instanceof VTabsheet) || (w instanceof VWindow) || (w instanceof VGroupBox)) {
                        parentOverflowContainer = w;
                        break;
                    }
                }
            }
        }

        runWebkitOverflowAutoFix();
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
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

        if (uidl.hasAttribute("pagelength")) {
            pageLength = uidl.getIntAttribute("pagelength");
        } else {
            pageLength = 0;
        }
        if (pageLength == 0) pageLength = totalRows;

        showRowHeaders = uidl.getBooleanAttribute("rowheaders");
        showColHeaders = uidl.getBooleanAttribute("colheaders");
        allowMultiStingCells = uidl.getBooleanAttribute("multistring");
        nullSelectionDisallowed = uidl.getBooleanAttribute("nullSelectionDisallowed");
        storeColWidth = uidl.getBooleanAttribute("storeColWidth");

        textSelectionEnabled = uidl.getBooleanAttribute("textSelection");

        if (uidl.hasVariable("sortascending")) {
            sortAscending = uidl.getBooleanVariable("sortascending");
            sortColumn = uidl.getStringVariable("sortcolumn");
        }

        if (uidl.hasVariable("enableCancelSorting")){
            enableCancelSorting = uidl.getBooleanVariable("enableCancelSorting");
        }

        if (uidl.hasVariable("selected")) {
            final Set<String> selectedKeys = uidl
                    .getStringArrayVariableAsSet("selected");
            selectedRowKeys.clear();
            for (final String selectedKey : selectedKeys) {
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

        multiselectmode = MULTISELECT_MODE_DEFAULT;

        if (uidl.hasVariable("columnorder")) {
            columnReordering = true;
            columnOrder = uidl.getStringArrayVariable("columnorder");
        }

        if (uidl.hasVariable("collapsedcolumns")) {
            tHead.addStyleName("collapsing-allowed");
            tHead.setColumnCollapsingAllowed(true);
            collapsedColumns = uidl
                    .getStringArrayVariableAsSet("collapsedcolumns");
        } else {
            tHead.removeStyleName("collapsing-allowed");
            tHead.setColumnCollapsingAllowed(false);
        }

        if (uidl.hasVariable("presentations")) {
            tHead.setPresentationsAllow(uidl.getBooleanVariable("presentations"));
        } else {
            tHead.setPresentationsAllow(false);
        }

        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL c = (UIDL) it.next();
            if (c.getTag().equals("actions")) {
                updateActionMap(c);
            } else if (c.getTag().equals("visiblecolumns")) {
                tHead.updateCellsFromUIDL(c);
            } else if (c.getTag().equals("presentations")) {
                tHead.updatePresentationsPopup(c);
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

        dragmode = uidl.hasAttribute("dragmode") ? uidl.getIntAttribute("dragmode") : 0;
        if (BrowserInfo.get().isIE()) {
            if (dragmode > 0) {
                getElement().setPropertyJSO("onselectstart",
                        getPreventTextSelectionIEHack());
            } else {
                getElement().setPropertyJSO("onselectstart", null);
            }
        }
    }

    protected abstract void focusRowFromBody();

    protected abstract void updateBody(UIDL uidl);

    protected abstract boolean updateImmediate();

    protected void initCellStylePaddingBorders(String style) {
        if (!stylePaddingBorders.containsKey(style)) {
            stylePaddingBorders.put(style, RenderInformation.Size.UNDEFINED);
        }
    }

    protected void setWidthDependsOnStyle(Element el, int w) {
        RenderInformation.Size paddingBorders = getElementPaddingBorders(el);
        if (paddingBorders != null && (w - paddingBorders.getWidth() > 0)) {
            DOM.setStyleAttribute(el, "width", (w - paddingBorders.getWidth()) + "px");
        } else {
            DOM.setStyleAttribute(el, "width", w + "px");
        }
    }

    protected RenderInformation.Size getElementPaddingBorders(Element el) {
        if (defaultPaddings != null)
            return defaultPaddings;

        String[] styles = Tools.getStyleNames(el);
        String style = styles[styles.length - 1];
        RenderInformation.Size paddingBorders = stylePaddingBorders.get(style);
        if (paddingBorders != null && isAttached()) {
            if (paddingBorders == RenderInformation.Size.UNDEFINED) {
                paddingBorders = Tools.definePaddingBorders(el);
                if (defaultPaddings == null)
                    defaultPaddings = paddingBorders;
                stylePaddingBorders.put(style, paddingBorders);
            }
            return paddingBorders;
        }
        return null;
    }

    protected boolean isSelectable() {
        return selectMode > com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE;
    }

    /**
     * Selects a row where the current selection head is
     *
     * @param ctrlSelect
     *            Is the selection a ctrl+selection
     * @param shiftSelect
     *            Is the selection a shift+selection
     */
    protected void selectFocusedRow(boolean ctrlSelect, boolean shiftSelect) {
        if (focusedRow != null) {
            // Arrows moves the selection and clears previous selections
            if (isSelectable() && !ctrlSelect && !shiftSelect) {
                deselectAll();
                focusedRow.processRowSelection();
                selectionRangeStart = focusedRow;
            }

            // Ctrl+arrows moves selection head
            else if (isSelectable() && ctrlSelect && !shiftSelect) {
                selectionRangeStart = focusedRow;
                // No selection, only selection head is moved
            }

            // Shift+arrows selection selects a range
            else if (selectMode == SELECT_MODE_MULTI && !ctrlSelect
                    && shiftSelect) {
                focusedRow.toggleShiftSelection(shiftSelect);
            }
        }
    }

    /**
     * Sends the selection to the server if changed since the last update/visit.
     */
    protected void sendSelectedRows() {
        // Don't send anything if selection has not changed
        if (!selectionChanged) {
            return;
        }

        // Reset selection changed flag
        selectionChanged = false;

        // Note: changing the immediateness of this might require changes to
        // "clickEvent" immediateness also.
        if (multiselectmode == MULTISELECT_MODE_DEFAULT) {
            // Convert ranges to a set of strings
            Set<String> ranges = new HashSet<String>();
            for (SelectionRange range : selectedRowRanges) {
                ranges.add(range.toString());
            }

            // Send the selected row ranges
            client.updateVariable(paintableId, "selectedRanges",
                    ranges.toArray(new String[selectedRowRanges.size()]), false);

            // clean selectedRowKeys so that they don't contain excess values
            for (Iterator<String> iterator = selectedRowKeys.iterator(); iterator
                    .hasNext();) {
                String key = iterator.next();
                ITableBody.ITableRow renderedRowByKey = getRenderedRowByKey(key);
                if (renderedRowByKey != null) {
                    for (SelectionRange range : selectedRowRanges) {
                        if (range.inRange(renderedRowByKey)) {
                            iterator.remove();
                        }
                    }
                } else {
                    // orphaned selected key, must be in a range, ignore
                    iterator.remove();
                }

            }
        }

        // Send the selected rows
        client.updateVariable(paintableId, "selected",
                selectedRowKeys.toArray(new String[selectedRowKeys.size()]),
                immediate);

    }

    /**
     * Moves the focus one step down
     *
     * @return Returns true if succeeded
     */
    private boolean moveFocusDown() {
        return moveFocusDown(0);
    }

    /**
     * Moves the focus down by 1+offset rows
     *
     * @param offset Offset
     * @return Returns true if succeeded, else false if the selection could not
     *         be move downwards
     */
    private boolean moveFocusDown(int offset) {
        if (isSelectable()) {
            if (focusedRow == null && bodyContainer.iterator().hasNext()) {
                // FIXME should focus first visible from top, not first rendered
                // ??
                return setRowFocus((ITableBody.ITableRow) bodyContainer.iterator()
                        .next());
            } else {
                ITableBody.ITableRow next = getNextRow(focusedRow, offset);
                if (next != null) {
                    return setRowFocus(next);
                }
            }
        }

        return false;
    }

    /**
     * Moves the selection one step up
     *
     * @return Returns true if succeeded
     */
    private boolean moveFocusUp() {
        return moveFocusUp(0);
    }

    /**
     * Moves the focus row upwards
     *
     * @param offset Offset
     * @return Returns true if succeeded, else false if the selection could not
     *         be move upwards
     *
     */
    private boolean moveFocusUp(int offset) {
        if (isSelectable()) {
            if (focusedRow == null && bodyContainer.iterator().hasNext()) {
                // FIXME logic is exactly the same as in moveFocusDown, should
                // be the opposite??
                return setRowFocus((ITableBody.ITableRow) bodyContainer.iterator()
                        .next());
            } else {
                ITableBody.ITableRow prev = getPreviousRow(focusedRow, offset);
                if (prev != null) {
                    return setRowFocus(prev);
                } else {
                    VConsole.log("no previous available");
                }
            }
        }

        return false;
    }

    /**
     * Get the key that moves the selection head upwards. By default it is the
     * up arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationUpKey() {
        return KeyCodes.KEY_UP;
    }

    /**
     * Get the key that moves the selection head downwards. By default it is the
     * down arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationDownKey() {
        return KeyCodes.KEY_DOWN;
    }

    /**
     * Get the key that scrolls to the left in the table. By default it is the
     * left arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationLeftKey() {
        return KeyCodes.KEY_LEFT;
    }

    /**
     * Get the key that scroll to the right on the table. By default it is the
     * right arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationRightKey() {
        return KeyCodes.KEY_RIGHT;
    }

    /**
     * Get the key that selects an item in the table. By default it is the space
     * bar key but by overriding this you can change the key to whatever you
     * want.
     *
     * @return
     */
    protected int getNavigationSelectKey() {
        return CHARCODE_SPACE;
    }

    /**
     * Get the key the moves the selection one page up in the table. By default
     * this is the Page Up key but by overriding this you can change the key to
     * whatever you want.
     *
     * @return
     */
    protected int getNavigationPageUpKey() {
        return KeyCodes.KEY_PAGEUP;
    }

    /**
     * Get the key the moves the selection one page down in the table. By
     * default this is the Page Down key but by overriding this you can change
     * the key to whatever you want.
     *
     * @return
     */
    protected int getNavigationPageDownKey() {
        return KeyCodes.KEY_PAGEDOWN;
    }

    /**
     * Get the key the moves the selection to the beginning of the table. By
     * default this is the Home key but by overriding this you can change the
     * key to whatever you want.
     *
     * @return
     */
    protected int getNavigationStartKey() {
        return KeyCodes.KEY_HOME;
    }

    /**
     * Get the key the moves the selection to the end of the table. By default
     * this is the End key but by overriding this you can change the key to
     * whatever you want.
     *
     * @return
     */
    protected int getNavigationEndKey() {
        return KeyCodes.KEY_END;
    }

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

    /**
     * Returns the next row to the given row
     *
     * @param row
     *            The row to calculate from
     *
     * @param offset Offset
     * @return The next row or null if no row exists
     */
    private ITableBody.ITableRow getNextRow(ITableBody.ITableRow row, int offset) {
        final Iterator<Widget> it = tBody.iterator();
        ITableBody.ITableRow r = null;
        while (it.hasNext()) {
            r = (ITableBody.ITableRow) it.next();
            if (r == row) {
                r = null;
                while (offset >= 0 && it.hasNext()) {
                    r = (ITableBody.ITableRow) it.next();
                    offset--;
                }
                return r;
            }
        }

        return null;
    }

    /**
     * Returns the previous row from the given row
     *
     * @param row
     *            The row to calculate from
     * @param offset Offset
     * @return The previous row or null if no row exists
     */
    private ITableBody.ITableRow getPreviousRow(ITableBody.ITableRow row, int offset) {
        final Iterator<Widget> it = tBody.iterator();
        final Iterator<Widget> offsetIt = tBody.iterator();
        ITableBody.ITableRow r = null;
        ITableBody.ITableRow prev = null;
        while (it.hasNext()) {
            r = (ITableBody.ITableRow) it.next();
            if (offset < 0) {
                prev = (ITableBody.ITableRow) offsetIt.next();
            }
            if (r == row) {
                return prev;
            }
            offset--;
        }

        return null;
    }

    private void updateActionMap(UIDL c) {
        final Iterator it = c.getChildIterator();
        while (it.hasNext()) {
            final UIDL action = (UIDL) it.next();
            final String key = action.getStringAttribute("key");
            final String caption = action.getStringAttribute("caption");
            // if it is not shortcut action
            if (!action.hasAttribute("kc")) {
                actionMap.put(key + "_c", caption);
                if (action.hasAttribute("icon")) {
                    // TODO need some uri handling ??
                    actionMap.put(key + "_i", client.translateVaadinUri(action.getStringAttribute("icon")));
                }
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

        //clear old header cells
        clearOrphanedCells(colIds);

        //show updated header cells
        for (i = 0; i < colIds.length; i++) {
            final String cid = colIds[i];
            visibleColOrder[colIndex] = cid;
            tHead.enableColumn(cid, colIndex);
            colIndex++;
        }
    }

    protected void clearOrphanedCells(String[] colIds) {
        Vector<Widget> cells = new Vector<Widget>(tHead.visibleCells);
        for (final Object o : cells) {
            final HeaderCell cell = (HeaderCell) o;
            boolean columnExist = false;
            for (String newColId : colIds) {
                if (cell.getColKey() == newColId) {
                    columnExist = true;
                    break;
                }
            }
            if (!columnExist) {
                tHead.removeCell(cell.getColKey());
            }
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
        HeaderCell headerCell = tHead.getHeaderCell(index);
        if (headerCell == null)
            return null;
        return headerCell.getColKey();
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

        enableBrowserIntelligence();
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

        disableBrowserIntelligence();

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
                    HeaderCell headerCell = tHead.getHeaderCell(columnindex);
                    if (headerCell.getWidth() == -1) {
                        widths[columnindex] += scrollbarWidthReserved;
                        totalWidthR += scrollbarWidthReserved;
                    }
                    extraSpace -= scrollbarWidthReserved;

                    scrollbarWidthReservedInColumnKey = headerCell.getColKey();
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

    public void disableBrowserIntelligence() {
        tHead.disableBrowserIntelligence();
    }

    public void enableBrowserIntelligence() {
        tHead.enableBrowserIntelligence();
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

        private String icon;

        public void setSortable(boolean b) {
            sortable = b;
        }

        public HeaderCell(String colId, UIDL uidl) {
            cid = colId;

            DOM.setElementProperty(colResizeWidget, "className", CLASSNAME
                    + "-resizer");
            DOM.setStyleAttribute(colResizeWidget, "width", DRAG_WIDGET_WIDTH
                    + "px");
            DOM.sinkEvents(colResizeWidget, Event.MOUSEEVENTS);

            if (uidl != null) {
                setText(buildCaptionHtmlSnippet(uidl));
                icon = client.translateVaadinUri(uidl.getStringAttribute("icon"));
            } else {
                setText("");
            }

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

        public String getIcon() {
            return icon;
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
                int width = w - DRAG_WIDGET_WIDTH - 4;
                DOM.setStyleAttribute(captionContainer, "width", (width > 0 ? width + "px" : ""));
                setWidth(w + "px");
            }
        }

        public int getWidth() {
            return width;
        }

        public void setText(String headerText) {
            headerText = SafeHtmlUtils.htmlEscape(headerText != null ? headerText : "")
                    .replaceAll("\n", "<br/>")
                    .replaceAll("\t", "&#09;")
                    .replaceAll(" ", "&nbsp;");
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
                    //Fixes #415
                    int scrollLeft = bodyContainer.getElement().getScrollLeft();
                    tHead.setHorizontalScrollPosition(scrollLeft);
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
                    VConsole.log(
                            "HeaderCaption: mouse down");
                    if (columnReordering && event.getButton() == Event.BUTTON_LEFT) {
                        dragging = true;
                        moved = false;
                        colIndex = getColIndexByKey(cid);
                        DOM.setCapture(getElement());
                        headerX = tHead.getAbsoluteLeft();
                        VConsole.log(
                                "HeaderCaption: Caption set to capture mouse events");
                        DOM.eventPreventDefault(event); // prevent selecting text
                    }
                    break;
                case Event.ONMOUSEUP:
                    VConsole.log("HeaderCaption: mouseUP");
                    if (columnReordering) {
                        dragging = false;
                        DOM.releaseCapture(getElement());
                        VConsole.log(
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

                    if (!moved && event.getButton() == Event.BUTTON_LEFT) {
                        // mouse event was a click to header -> sort column
                        if (sortable) {
                            sortColumn();
                        }
                        break;
                    }
                    break;
                case Event.ONMOUSEMOVE:
                    if (dragging) {
                        VConsole.log(
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
                        VConsole.log("" + closestSlot);
                    }
                    break;
                default:
                    break;
            }
        }

        protected void sortColumn() {
            boolean reloadDataFromServer = true;

            if (cid.equals(sortColumn)) {
                if (sortColumn == null) {
                    // anyway sort ascending
                    client.updateVariable(paintableId, "sortascending", !sortAscending, false);
                } else if (sortAscending) {
                    if (sortClickCounter < 2) {
                        // special case for initial revert sorting instead of reset sort order
                        if (sortClickCounter == 0) {
                            client.updateVariable(paintableId, "sortascending", !sortAscending, false);
                        } else {
                            reloadDataFromServer = false;
                            sortClickCounter = 0;
                            sortColumn = null;
                            sortAscending = true;

                            client.updateVariable(paintableId, "resetsortorder", "", true);
                        }
                    } else {
                        client.updateVariable(paintableId, "sortascending", !sortAscending, false);
                    }
                } else {
                    if (sortClickCounter < 2) {
                        // special case for initial revert sorting instead of reset sort order
                        if (sortClickCounter == 0) {
                            client.updateVariable(paintableId, "sortascending", !sortAscending, false);
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
                bodyContainer.setScrollPosition(0);
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
     * <p/>
     * Reordering disabled and clicking on it resets sorting.
     */
    public class RowHeadersHeaderCell extends HeaderCell {

        RowHeadersHeaderCell() {
            super("0", null);
        }

        @Override
        protected void handleCaptionEvent(Event event) {
            // NOP: RowHeaders cannot be reordered
            // TODO It'd be nice to reset sorting here
        }
    }

    public class TableHead extends Panel implements ActionOwner {

        protected Vector<Widget> visibleCells = new Vector<Widget>();

        protected HashMap<String, HeaderCell> availableCells = new HashMap<String, HeaderCell>();

        Element div = DOM.createDiv();
        Element hTableWrapper = DOM.createDiv();
        Element hTableContainer = DOM.createDiv();
        Element table = DOM.createTable();
        Element headerTableBody = DOM.createTBody();
        Element tr = DOM.createTR();

        private final Element columnSelector = DOM.createDiv();
        private Element presentationSelector = DOM.createDiv();

        private Element clickedSelector;

        private PopupContainer popup;

        private int focusedSlot = -1;

        public TableHead() {
            DOM.setStyleAttribute(hTableWrapper, "overflow", "hidden");
            DOM.setElementProperty(hTableWrapper, "className", CLASSNAME
                    + "-header");

            DOM.setElementProperty(presentationSelector, "className", CLASSNAME
                    + "-pres-selector");
            DOM.setStyleAttribute(presentationSelector, "display", "none");

            DOM.setElementProperty(columnSelector, "className", CLASSNAME
                    + "-column-selector");
            DOM.setStyleAttribute(columnSelector, "display", "none");

            DOM.setElementProperty(hTableContainer, "className", CLASSNAME
                    + "-header-container");

            DOM.setElementProperty(table, "className", CLASSNAME
                    + "-header-internal");

            DOM.setInnerHTML(presentationSelector, "<div></div>");
            DOM.setInnerHTML(columnSelector, "<div></div>");

            DOM.appendChild(table, headerTableBody);
            DOM.appendChild(headerTableBody, tr);
            DOM.appendChild(hTableContainer, table);
            DOM.appendChild(hTableWrapper, hTableContainer);
            DOM.appendChild(div, hTableWrapper);
            DOM.appendChild(div, presentationSelector);
            DOM.appendChild(div, columnSelector);
            setElement(div);

            setStyleName(CLASSNAME + "-header-wrap");

            DOM.sinkEvents(columnSelector, Event.ONCLICK);
            DOM.sinkEvents(presentationSelector, Event.ONCLICK);

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

        protected HeaderCell createHeaderCell(String cid, UIDL uidl) {
            return new HeaderCell(cid, uidl);
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

        public void setPresentationsAllow(boolean b) {
            if (b) {
                DOM.setStyleAttribute(presentationSelector, "display", "block");
            } else {
                DOM.setStyleAttribute(presentationSelector, "display", "none");
            }
        }

        public void disableBrowserIntelligence() {
            DOM.setStyleAttribute(hTableContainer, "width", TABLE_MAX_WIDTH + "px");
        }

        public void enableBrowserIntelligence() {
            DOM.setStyleAttribute(hTableContainer, "width", "");
        }

        public void setHeaderCell(int index, HeaderCell cell) {
            if (cell.isEnabled()) {
                // we're moving the cell
                visibleCells.remove(cell);
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
         * <p/>
         * Note that this returns HeaderCell even if it is currently collapsed.
         *
         * @param cid Column key of accessed HeaderCell
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

        @Override
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
                try {
                    final EventTarget target = event.getEventTarget();
                    if (target.cast() == columnSelector || DOM.getParent(target.<Element>cast()) == columnSelector) {
                        final int left = DOM.getAbsoluteLeft(columnSelector);
                        final int top = DOM.getAbsoluteTop(columnSelector)
                                + DOM.getElementPropertyInt(columnSelector,
                                "offsetHeight");

                        clickedSelector = columnSelector;

                        client.getContextMenu().addStyleName("v-tableColumnSelector");
                        client.getContextMenu().showAt(this, left, top);
                    } else if (target.cast() == presentationSelector || DOM.getParent(target.<Element>cast()) == presentationSelector) {
                        final int left = DOM.getAbsoluteLeft(presentationSelector);
                        final int top = DOM.getAbsoluteTop(presentationSelector)
                                + DOM.getElementPropertyInt(presentationSelector,
                                "offsetHeight");

                        clickedSelector = presentationSelector;

                        popup.showAt(left, top);
                    }
                } finally {
                    clickedSelector = null;
                }
            }
        }

        protected void updatePresentationsPopup(UIDL uidl) {
            if (popup == null) {
                popup = new PopupContainer();
            }
            popup.updateFromUIDL(uidl, client);
        }

        protected class VisibleColumnAction extends Action {
            String colKey;
            private boolean collapsed;
            private boolean enabled = true;

            public VisibleColumnAction(String colKey) {
                super(Table.TableHead.this);
                this.colKey = colKey;
                caption = tHead.getHeaderCell(colKey).getCaption();
                iconUrl = tHead.getHeaderCell(colKey).getIcon();
            }

            @Override
            public void execute() {
                if (enabled) {
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
            }

            public void setCollapsed(boolean b) {
                collapsed = b;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
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
        @Override
        public Action[] getActions() {
            if (clickedSelector == columnSelector) {
                return getColumnSelectionActions();
            } else {
                return new Action[0];
            }
        }

        private Action[] getColumnSelectionActions() {
            final Object[] cols = getActionColumns();
            final Action[] actions = new Action[cols.length];

            // Indicates whether there is only one displayed column
            boolean oneColumnLeft = (collapsedColumns.size() >= (cols.length - 1));

            for (int i = 0; i < cols.length; i++) {
                final String cid = (String) cols[i];
                final HeaderCell c = getHeaderCell(cid);
                final VisibleColumnAction a = createColumnAction(c.getColKey());
                a.setCaption(c.getCaption());
                if (!c.isEnabled()) {
                    a.setCollapsed(true);
                }
                else if (oneColumnLeft){
                    //Disable ability to collapse if it's the last not collapsed column
                    a.setEnabled(false);
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

        @Override
        public ApplicationConnection getClient() {
            return client;
        }

        @Override
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

    public interface PaintListener {

        void afterPaint(Widget w);
    }

    public abstract class ITableBody extends Panel {
        public static final int CELL_EXTRA_WIDTH = 20;

        public static final int DEFAULT_ROW_HEIGHT = 24;

        private int rowHeight = -1;

        protected final List<Widget> renderedRows = new Vector<Widget>();

        protected boolean initDone = false;

        protected Element container = DOM.createDiv();

        protected Element tBody = DOM.createTBody();
        protected Element table = DOM.createTable();

        protected char[] aligns;

        protected int firstRendered;

        protected Widget lastFocusedWidget = null;

        protected Map<Widget, PaintListener> pendingPaintListeners = new HashMap<Widget, PaintListener>();

        public ITableBody() {
            setElement(container);
            aligns = tHead.getColumnAlignments();
        }

        public ITableRow getRowByRowIndex(int indexInTable) {
            int internalIndex = indexInTable - firstRendered;
            if (internalIndex >= 0 && internalIndex < renderedRows.size()) {
                return (ITableRow) renderedRows.get(internalIndex);
            } else {
                return null;
            }
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

        @Override
        public Iterator<Widget> iterator() {
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
                setWidthDependsOnStyle(DOM.getFirstChild(cell), w);
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

        /**
         * Restore row visibility which is set to "none" when the row is
         * rendered (due a performance optimization).
         */
        public void restoreRowVisibility() {
            for (Widget row : renderedRows) {
                row.getElement().getStyle().setProperty("visibility", "");
            }
        }

        /**
         * Ensure the component has a focus.
         *
         * TODO the current implementation simply always calls focus for the
         * component. In case the Table at some point implements focus/blur
         * listeners, this method needs to be evolved to conditionally call
         * focus only if not currently focused.
         */
        protected void ensureFocus() {
            if (!hasFocus) {
                bodyContainer.setFocus(true);
            }
        }

        public class ITableRow extends Panel implements ActionOwner,
                Container {
            private static final int TOUCHSCROLL_TIMEOUT = 70;
            private static final int DRAGMODE_MULTIROW = 2;
            protected Vector<Widget> childWidgets = new Vector<Widget>();
            private boolean selected = false;
            private final int rowKey;
            private Map<Paintable, UIDL> pendingComponentPaints;

            protected String[] actionKeys = null;

            protected Map<Widget, Integer> widgetColumns = null;

            private boolean mDown;
            private final TableRowElement rowElement;
            private int index;
            private Event touchStart;
            private static final int TOUCH_CONTEXT_MENU_TIMEOUT = 500;
            private Timer contextTouchTimeout;
            private int touchStartY;
            private int touchStartX;

            protected List<Element> tableCells = new ArrayList<Element>();

            protected ITableRow() {
                this(0);
            }

            protected ITableRow(int rowKey) {
                this.rowKey = rowKey;
                rowElement = Document.get().createTRElement();
                setElement(rowElement);
                DOM.sinkEvents(getElement(), Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONCLICK
                        | Event.TOUCHEVENTS | Event.ONDBLCLICK
                        | Event.ONCONTEXTMENU  | VTooltip.TOOLTIP_EVENTS);
            }

            public ITableRow(UIDL uidl, char[] aligns) {
                this(uidl.getIntAttribute("key"));

                /*
                 * Rendering the rows as hidden improves Firefox and Safari
                 * performance drastically.
                 */
                getElement().getStyle().setProperty("visibility", "hidden");

                String rowStyle = uidl.getStringAttribute("rowstyle");
                if (rowStyle != null) {
                    addStyleName(CLASSNAME + "-row-" + rowStyle);
                }

                String rowDescription = uidl.getStringAttribute("rowdescr");
                if (rowDescription != null && !rowDescription.equals("")) {
                    TooltipInfo tooltipInfo = new TooltipInfo(rowDescription);
                    client.registerTooltip(Table.this, rowElement, tooltipInfo);
                } else {
                    client.registerTooltip(Table.this, rowElement, null);
                }

                int col = 0;

                // row header
                if (showRowHeaders) {
                    addCell(buildCaptionHtmlSnippet(uidl), aligns[col], "", col,
                            true, null);
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

            /**
             * Makes a check based on indexes whether the row is before the
             * compared row.
             *
             * @param row1
             * @return true if this rows index is smaller than in the row1
             */
            public boolean isBefore(ITableRow row1) {
                return getIndex() < row1.getIndex();
            }

            /**
             * Sets the index of the row in the whole table. Currently used just
             * to set even/odd classname
             *
             * @param indexInWholeTable
             */
            public void setIndex(int indexInWholeTable) {
                index = indexInWholeTable;
            }

            public int getIndex() {
                return index;
            }

            protected void paintComponent(Paintable p, UIDL uidl) {
                if (isAttached()) {
                    p.updateFromUIDL(uidl, client);

                    if (pendingPaintListeners.containsKey(p)) {
                        PaintListener paintListener = pendingPaintListeners.remove(p);
                        paintListener.afterPaint((Widget) p);
                    }
                } else {
                    if (pendingComponentPaints == null) {
                        pendingComponentPaints = new LinkedHashMap<Paintable, UIDL>();
                    }
                    pendingComponentPaints.put(p, uidl);
                }
            }

            @Override
            protected void onAttach() {
                super.onAttach();
                if (pendingComponentPaints != null) {
                    for (final Map.Entry<Paintable, UIDL> entry : pendingComponentPaints.entrySet()) {
                        Paintable pendingWidget = entry.getKey();
                        pendingWidget.updateFromUIDL(entry.getValue(), client);

                        if (pendingPaintListeners.containsKey(pendingWidget)) {
                            PaintListener paintListener = pendingPaintListeners.remove(pendingWidget);
                            paintListener.afterPaint((Widget) pendingWidget);
                        }
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

                    String description = null;
                    if (uidl.hasAttribute("descr-" + columnId)) {
                        description = uidl.getStringAttribute("descr-"
                                + columnId);
                    }

                    if (cell instanceof String) {
                        addCell(cell.toString(), aligns[col], style, col, false, description);
                    } else {
                        Paintable cellContent = client.getPaintable((UIDL) cell);
                        addCell((Widget) cellContent, aligns[col], style, col, description);
                        paintComponent(cellContent, (UIDL) cell);
                    }
                    col++;
                }
            }

            public void addCell(String text, char align, String style, int col,
                                boolean textIsHTML, String description) {
                // String only content is optimized by not using Label widget
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();

                //We should apply paddings and borders only to <div/> element, not to <td> element of cell
                Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
                initCellStylePaddingBorders(Tools.setStylePrimaryName(container, CLASSNAME + "-cell-content"));
                if (allowMultiStingCells) {
                    Tools.addStyleName(td, CLASSNAME + "-cell-wrap");
                }
                if (style != null && !style.equals("")) {
                    //We should apply paddings and borders only to <div/> element, not to <td> element of cell
                    Tools.addStyleDependentName(td, style);
                    initCellStylePaddingBorders(Tools.addStyleDependentName(container, style));
                }

                setCellText(container, text, textIsHTML);
                setCellAlignment(container, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);

                if (BrowserInfo.get().getWebkitVersion() > 0) {
                    DOM.setElementPropertyBoolean(td, "__cell", true);
                    DOM.setElementPropertyBoolean(container, "__cell", true);
                }

                tableCells.add(td);

                Tools.textSelectionEnable(td, textSelectionEnabled);

                setTooltip(td, description);
            }

            protected void setTooltip(Element td, String description) {
                if (description != null && !description.equals("")) {
                    TooltipInfo info = new TooltipInfo(description);
                    client.registerTooltip(Table.this, td, info);
                } else {
                    client.registerTooltip(Table.this, td, null);
                }
            }

            public void removeCells() {
                for (Element td : tableCells)
                    Tools.removeElementWithEvents(td);

                tableCells.clear();
            }

            public void addCell(Widget w, char align, String style, int col, String description) {
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();

                //We should apply paddings and borders only to <div/> element, not to <td> element of cell
                Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
                initCellStylePaddingBorders(Tools.setStylePrimaryName(container, CLASSNAME + "-cell-content"));
                if (allowMultiStingCells) {
                    Tools.addStyleName(td, CLASSNAME + "-cell-wrap");
                }
                if (style != null && !style.equals("")) {
                    //We should apply paddings and borders only to <div/> element, not to <td> element of cell
                    Tools.addStyleDependentName(td, style);
                    initCellStylePaddingBorders(Tools.addStyleDependentName(container, style));
                }
                // TODO most components work with this, but not all (e.g.
                // Select)
                // Old comment: make widget cells respect align.
                // text-align:center for IE, margin: auto for others

                setCellAlignment(container, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);

                if (BrowserInfo.get().getWebkitVersion() > 0) {
                    DOM.setElementPropertyBoolean(td, "__cell", true);
                    DOM.setElementPropertyBoolean(container, "__cell", true);
                }

                setCellWidget(container, w, col);
                setTooltip(td, description);
            }

            protected void moveCol(int oldIndex, int newIndex) {
                final Element td = DOM.getChild(getElement(), oldIndex);
                DOM.removeChild(getElement(), td);

                DOM.insertChild(getElement(), td, newIndex);
            }

            public int getHeight() {
                return DOM.getChild(getElement(), 0).getOffsetHeight();
            }

            protected void setCellWidget(Element container, final Widget w, int colIndex) {
                // ensure widget not attached to another element (possible tBody
                // change)
                w.removeFromParent();
                DOM.appendChild(container, w.getElement());
                adopt(w);
                childWidgets.add(w);
                if (widgetColumns == null) {
                    widgetColumns = new HashMap<Widget, Integer>();
                }
                widgetColumns.put(w, colIndex);

                // Support for #PL-2080
                pendingPaintListeners.put(w, new PaintListener() {
                    @Override
                    public void afterPaint(Widget w) {
                        recursiveAddFocusHandler(w, w);
                    }
                });
            }

            protected void recursiveAddFocusHandler(final Widget w, final Widget topWidget) {
                if (w instanceof HasWidgets) {
                    for (Widget child: (HasWidgets)w) {
                        recursiveAddFocusHandler(child, topWidget);
                    }
                } else if (w instanceof HasFocusHandlers) {
                    ((HasFocusHandlers) w).addFocusHandler(new FocusHandler() {
                        @Override
                        public void onFocus(FocusEvent event) {
                            if (childWidgets.indexOf(topWidget) < 0) {
                                return;
                            }

                            lastFocusedWidget = w;

                            VConsole.log("onFocus: Focus widget in column: " + childWidgets.indexOf(topWidget));

                            if (!isSelected()) {
                                deselectAll();

                                toggleSelection();
                                setRowFocus(ITableRow.this);

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

                VConsole.log("Handle focus for Table");

                if (isSelected()) {
                    if (lastFocusedWidget instanceof Focusable) {
                        ((Focusable) lastFocusedWidget).focus();

                        VConsole.log("onSelect: Focus table cell widget");
                    } else if (lastFocusedWidget instanceof com.google.gwt.user.client.ui.Focusable) {
                        ((com.google.gwt.user.client.ui.Focusable) lastFocusedWidget).setFocus(true);

                        VConsole.log("onSelect: Focus GWT table cell widget");
                    }
                }

                lastFocusedWidget = null;
            }

            @Override
            public Iterator<Widget> iterator() {
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

                    boolean imm = true;
                    if (immediate && event.getButton() == Event.BUTTON_LEFT
                            && !dbl && isSelectable() && !isSelected()) {
                        /*
                         * A left click when the table is selectable and in
                         * immediate mode on a row that is not currently
                         * selected will cause a selection event to be fired
                         * after this click event. By making the click event
                         * non-immediate we avoid sending two separate messages
                         * to the server.
                         */
                        imm = false;
                    }

                    client
                            .updateVariable(paintableId, "clickEvent",
                                    details.toString(), imm);
                }
            }

            private void handleTooltips(final Event event, Element target){
                if(target.hasTagName("TD")){
                    if(client.getTooltipTitleInfo(Table.this, target) != null){
                        // Cell has description, use it
                        client.handleTooltipEvent(event, Table.this, target);
                    } else {
                    // Cell might have row description, use row description
                        client.handleTooltipEvent(event, Table.this, target.getParentElement());
                    }
                } else {
                // Table row (tr)
                    client.handleTooltipEvent(event, Table.this, target);
                }
            }

            /*
             * React on click that occur on content cells only
             */

            @Override
            public void onBrowserEvent(Event event) {
                final Element targetElement = DOM.eventGetTarget(event);

                Widget targetWidget = Util.findWidget(targetElement, null);
                int eventType = DOM.eventGetType(event);
                if (targetWidget != this) {
                    /*
                     * This is a workaround to make Labels, read only TextFields
                     * and Embedded in a Table clickable (see #2688). It is
                     * really not a fix as it does not work with a custom read
                     * only components (not extending VLabel/VEmbedded).
                     */
                    while (targetWidget != null && targetWidget.getParent() != this) {
                        targetWidget = targetWidget.getParent();
                    }

                    if (!(targetWidget instanceof VLabel)
                            && !(targetWidget instanceof VButton &&
                                (Event.ONMOUSEOVER == eventType ||
                                Event.ONMOUSEOUT == eventType ||
                                Event.ONMOUSEMOVE == eventType))
                            && !(targetWidget instanceof VEmbedded)
                            && !(targetWidget instanceof VTextField && ((VTextField) targetWidget)
                            .isReadOnly())) {
                        return;
                    }
                }

                final int type = event.getTypeInt();
                final Element targetTdOrTr = getEventTargetTdOrTr(event);
                if (Tools.isCheckbox(targetElement) || Tools.isRadio(targetElement))
                    return;
                boolean targetCellOrRowFound = targetTdOrTr != null;

                if(targetCellOrRowFound){
                    handleTooltips(event, targetTdOrTr);
                }

                switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        handleClickEvent(event);
                        if (isSelectable()) {
                            // Ctrl+Shift click
                            if ((event.getCtrlKey() || event.getMetaKey())
                                    && event.getShiftKey()
                                    && selectMode == SELECT_MODE_MULTI
                                    && multiselectmode == MULTISELECT_MODE_DEFAULT) {
                                toggleShiftSelection(false);
                                setRowFocus(this);

                                // Ctrl click
                            } else if ((event.getCtrlKey() || event
                                    .getMetaKey())
                                    && selectMode == SELECT_MODE_MULTI
                                    && multiselectmode == MULTISELECT_MODE_DEFAULT) {
                                boolean wasSelected = isSelected();
                                toggleSelection();
                                setRowFocus(this);
                                /*
                                * next possible range select must start on
                                * this row
                                */
                                selectionRangeStart = this;
                                if (wasSelected) {
                                    removeRowFromUnsentSelectionRanges(this);
                                }

                                // Ctrl click (Single selection)
                            } else if ((event.getCtrlKey() || event
                                    .getMetaKey()
                                    && selectMode == SELECT_MODE_SINGLE)) {
                                if (!isSelected()
                                        || (isSelected() && !nullSelectionDisallowed)) {

                                    if (!isSelected()) {
                                        deselectAll();
                                    }

                                    toggleSelection();
                                    setRowFocus(this);
                                }

                                // Shift click
                            } else if (event.getShiftKey()
                                    && selectMode == SELECT_MODE_MULTI
                                    && multiselectmode == MULTISELECT_MODE_DEFAULT) {
                                toggleShiftSelection(true);

                                // click
                            } else {
                                rowClick();
                            }

                            // Remove IE text selection hack
                            if (BrowserInfo.get().isIE()) {
                                ((Element) event.getEventTarget().cast())
                                        .setPropertyJSO("onselectstart",
                                                null);
                            }
                            handleRowClick(event);
                        }

                        if (BrowserInfo.get().getWebkitVersion() > 0
                                && childWidgets.isEmpty()
                                && DOM.getElementPropertyBoolean(targetElement, "__cell")) {
                            bodyContainer.setFocus(true);
                            VConsole.log("Chrome: setted focus to panel");
                        }

                        break;
                    case Event.ONTOUCHEND:
                    case Event.ONTOUCHCANCEL:
                        if (touchStart != null) {
                            /*
                             * Touch has not been handled as neither context or
                             * drag start, handle it as a click.
                             */
                            Util.simulateClickFromTouchEvent(touchStart, this);
                            touchStart = null;
                        }
                        if (contextTouchTimeout != null) {
                            contextTouchTimeout.cancel();
                        }
                        break;
                    case Event.ONTOUCHMOVE:
                        if (isSignificantMove(event)) {
                            /*
                             * TODO figure out scroll delegate don't eat events
                             * if row is selected. Null check for active
                             * delegate is as a workaround.
                             */
                            if (dragmode != 0
                                    && touchStart != null
                                    && (TouchScrollDelegate
                                            .getActiveScrollDelegate() == null)) {
                                startRowDrag(touchStart, type, targetTdOrTr);
                            }
                            if (contextTouchTimeout != null) {
                                contextTouchTimeout.cancel();
                            }
                            /*
                             * Avoid clicks and drags by clearing touch start
                             * flag.
                             */
                            touchStart = null;
                        }

                        break;
                    case Event.ONTOUCHSTART:
                        touchStart = event;
                        Touch touch = event.getChangedTouches().get(0);
                        // save position to fields, touches in events are same
                        // isntance during the operation.
                        touchStartX = touch.getClientX();
                        touchStartY = touch.getClientY();
                        /*
                         * Prevent simulated mouse events.
                         */
                        touchStart.preventDefault();
                        if (dragmode != 0 || actionKeys != null) {
                            new Timer() {
                                @Override
                                public void run() {
                                    TouchScrollDelegate activeScrollDelegate = TouchScrollDelegate
                                            .getActiveScrollDelegate();
                                    if (activeScrollDelegate != null
                                            && !activeScrollDelegate.isMoved()) {
                                        /*
                                         * scrolling hasn't started. Cancel
                                         * scrolling and let row handle this as
                                         * drag start or context menu.
                                         */
                                        activeScrollDelegate.stopScrolling();
                                    } else {
                                        /*
                                         * Scrolled or scrolling, clear touch
                                         * start to indicate that row shouldn't
                                         * handle touch move/end events.
                                         */
                                        touchStart = null;
                                    }
                                }
                            }.schedule(TOUCHSCROLL_TIMEOUT);

                            if (contextTouchTimeout == null
                                    && actionKeys != null) {
                                contextTouchTimeout = new Timer() {
                                    @Override
                                    public void run() {
                                        if (touchStart != null) {
                                            showContextMenu(touchStart);
                                            touchStart = null;
                                        }
                                    }
                                };
                            }
                            contextTouchTimeout.cancel();
                            contextTouchTimeout
                                    .schedule(TOUCH_CONTEXT_MENU_TIMEOUT);
                        }
                        break;
                    case Event.ONDBLCLICK:
                        handleClickEvent(event);
                        break;
                    case Event.ONMOUSEDOWN:
                        if (targetCellOrRowFound){
                            ensureFocus();
                        }
                        handleFocusForWidget();
                        break;
                    case Event.ONMOUSEUP:
                        if (targetCellOrRowFound) {
                            mDown = false;
                        }
                        break;
                    case Event.ONCONTEXTMENU:
                        if (selectMode > com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE) {
                            selectRowForContextMenuActions();
                        }
                        handleRowClick(event);
                        showContextMenu(event);
                        break;
                    default:
                        break;
                }
                super.onBrowserEvent(event);
            }

            protected void handleRowClick(Event event) {
                sendSelectedRows();
            }

            protected void rowClick() {
                boolean currentlyJustThisRowSelected = selectedRowKeys
                        .size() == 1
                        && selectedRowKeys
                        .contains(getKey());

                if (!currentlyJustThisRowSelected) {
                    if (multiselectmode == MULTISELECT_MODE_DEFAULT) {
                        deselectAll();
                    }
                    toggleSelection();
                } else if (selectMode == SELECT_MODE_SINGLE
                        && !nullSelectionDisallowed) {
                    toggleSelection();
                }/*
                      * else NOP to avoid excessive server
                      * visits (selection is removed with
                      * CTRL/META click)
                      */

                selectionRangeStart = this;
                setRowFocus(this);
            }

            protected void selectRowForContextMenuActions() {
                boolean currentlyJustThisRowSelected = selectedRowKeys
                        .size() == 1
                        && selectedRowKeys
                        .contains(getKey());

                boolean selectionChanged = false;
                if (!isSelected()) {
                    if (!currentlyJustThisRowSelected) {
                        if (multiselectmode == MULTISELECT_MODE_DEFAULT) {
                            deselectAll();
                        }
                        toggleSelection();
                    } else if (selectMode == SELECT_MODE_SINGLE
                            && !nullSelectionDisallowed) {
                        toggleSelection();
                    }

                    selectionChanged = true;
                }/*
                      * else NOP to avoid excessive server
                      * visits (selection is removed with
                      * CTRL/META click)
                      */

                if (selectionChanged) {
                    selectionRangeStart = this;
                    setRowFocus(this);
                }
            }

            private void processRowSelection() {
                if (selectMode > com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE) {
                    if (!nullSelectionDisallowed || !isSelected()) {
                        toggleSelection();
                    }
                }
            }

            private boolean isSignificantMove(Event event) {
                if (touchStart == null) {
                    // no touch start
                    return false;
                }
                /*
                 * TODO calculate based on real distance instead of separate
                 * axis checks
                 */
                Touch touch = event.getChangedTouches().get(0);
                if (Math.abs(touch.getClientX() - touchStartX) > TouchScrollDelegate.SIGNIFICANT_MOVE_THRESHOLD) {
                    return true;
                }
                if (Math.abs(touch.getClientY() - touchStartY) > TouchScrollDelegate.SIGNIFICANT_MOVE_THRESHOLD) {
                    return true;
                }
                return false;
            }

            protected void startRowDrag(Event event, final int type,
                    Element targetTdOrTr) {
                mDown = true;
                VTransferable transferable = new VTransferable();
                transferable.setDragSource(Table.this);
                transferable.setData("itemId", "" + rowKey);
                NodeList<TableCellElement> cells = rowElement.getCells();
                for (int i = 0; i < cells.getLength(); i++) {
                    if (cells.getItem(i).isOrHasChild(targetTdOrTr)) {
                        HeaderCell headerCell = tHead.getHeaderCell(i);
                        transferable.setData("propertyId", headerCell.cid);
                        break;
                    }
                }

                VDragEvent ev = VDragAndDropManager.get().startDrag(
                        transferable, event, true);
                if (dragmode == DRAGMODE_MULTIROW
                        && selectMode == SELECT_MODE_MULTI
                        && selectedRowKeys.contains("" + rowKey)) {
                    ev.createDragImage(
                            (Element) getBody().tBody.cast(), true);
                    Element dragImage = ev.getDragImage();
                    int i = 0;
                    for (Iterator<Widget> iterator = getBody().iterator(); iterator
                            .hasNext();) {
                        ITableRow next = (ITableRow) iterator
                                .next();
                        Element child = (Element) dragImage.getChild(i++);
                        if (!selectedRowKeys.contains("" + next.rowKey)) {
                            child.getStyle().setVisibility(Style.Visibility.HIDDEN);
                        }
                    }
                } else {
                    ev.createDragImage(getElement(), true);
                }
                if (type == Event.ONMOUSEDOWN) {
                    event.preventDefault();
                }
                event.stopPropagation();
            }

            /**
             * Finds the TD that the event interacts with. Returns null if the
             * target of the event should not be handled. If the event target is
             * the row directly this method returns the TR element instead of
             * the TD.
             *
             * @param event Event
             * @return TD or TR element that the event targets (the actual event
             *         target is this element or a child of it)
             */
            protected Element getEventTargetTdOrTr(Event event) {
                Element targetTdOrTr = null;

                final Element eventTarget = DOM.eventGetTarget(event);
                final Element eventTargetParent = DOM.getParent(eventTarget);
                final Element eventTargetGrandParent = DOM
                        .getParent(eventTargetParent);

                final Element thisTrElement = getElement();

                if (eventTarget == thisTrElement) {
                    // This was a click on the TR element
                    targetTdOrTr = eventTarget;
                    // rowTarget = true;
                } else if (thisTrElement == eventTargetParent) {
                    // Target parent is the TR, so the actual target is the TD
                    targetTdOrTr = eventTarget;
                } else if (thisTrElement == eventTargetGrandParent) {
                    // Target grand parent is the TR, so the parent is the TD
                    targetTdOrTr = eventTargetParent;
                } else {
                    /*
                     * This is a workaround to make Labels, read only TextFields
                     * and Embedded in a Table clickable (see #2688). It is
                     * really not a fix as it does not work with a custom read
                     * only components (not extending VLabel/VEmbedded).
                     */
                    Widget widget = Util.findWidget(eventTarget, null);
                    if (widget != this) {
                        while (widget != null && widget.getParent() != this) {
                            widget = widget.getParent();
                        }
                        if (widget != null) {
                            // widget is now the closest widget to this row
                            int eventType = DOM.eventGetType(event);
                            if (widget instanceof VLabel
                                    || (widget instanceof VButton
                                        && (Event.ONMOUSEOVER == eventType
                                        || Event.ONMOUSEOUT == eventType
                                        || Event.ONMOUSEMOVE == eventType))
                                    || widget instanceof VEmbedded
                                    || (widget instanceof VTextField && ((VTextField) widget)
                                    .isReadOnly())) {
                                Element tdElement = eventTargetParent;
                                while (DOM.getParent(tdElement) != thisTrElement) {
                                    tdElement = DOM.getParent(tdElement);
                                }
                                targetTdOrTr = tdElement;
                            }
                        }
                    }
                }

                return targetTdOrTr;
            }

            public void showContextMenu(Event event) {
                if (enabled && actionKeys != null && actionKeys.length > 0) {
                    int left = event.getClientX();
                    int top = event.getClientY();
                    top += Window.getScrollTop();
                    left += Window.getScrollLeft();
                    client.getContextMenu().removeStyleName("v-tableColumnSelector");
                    client.getContextMenu().showAt(this, left, top);
                }
                event.stopPropagation();
                event.preventDefault();
            }

            public boolean isSelected() {
                return selected;
            }

            public void toggleSelection() {
                selected = !selected;
                selectionChanged = true;
                String key = String.valueOf(rowKey);
                if (selected) {
                    /*if (selectMode == com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_SINGLE) {
                        deselectAll();
                    }*/
                    selectedRowKeys.add(key);
                    if (navigation) {
                        selectedKey = key;
                    }
//                    disabled due to #PL-3049
//                    if (!childWidgets.isEmpty()) {
//                        int i = focusWidgetIndex > -1 && (focusWidgetIndex < childWidgets.size()) ? focusWidgetIndex : 0;
//                        Widget w = (Widget) childWidgets.get(i);
//                        if (w instanceof com.vaadin.terminal.gwt.client.Focusable) {
//                            ((com.vaadin.terminal.gwt.client.Focusable) w).focus();
//                        }
//                        VConsole.log("onSelect: Focus widget index: " + focusWidgetIndex);
//                    }

                    addStyleName("v-selected");
                } else {
                    selectedRowKeys.remove(key);
                    if (navigation) {
                        selectedKey = null;
                    }
                    removeStyleName("v-selected");
                }
            }
            /**
             * Is called when a user clicks an item when holding SHIFT key down.
             * This will select a new range from the last focused row
             *
             * @param deselectPrevious Should the previous selected range be deselected
             */
            private void toggleShiftSelection(boolean deselectPrevious) {

                /*
                 * Ensures that we are in multiselect mode and that we have a
                 * previous selection which was not a deselection
                 */
                if (selectMode == SELECT_MODE_SINGLE) {
                    // No previous selection found
                    deselectAll();
                    toggleSelection();
                    return;
                }

                // Set the selectable range
                ITableRow endRow = this;
                ITableRow startRow = selectionRangeStart;
                if (startRow == null) {
                    startRow = focusedRow;
                    // If start row is null then we have a multipage selection
                    // from
                    // above
                    if (startRow == null) {
                        startRow = (ITableRow) Table.this.tBody.iterator()
                                .next();
                        setRowFocus(endRow);
                    }
                }
                // Deselect previous items if so desired
                if (deselectPrevious) {
                    deselectAll();
                }

                // we'll ensure GUI state from top down even though selection
                // was the opposite way
                if (!startRow.isBefore(endRow)) {
                    ITableRow tmp = startRow;
                    startRow = endRow;
                    endRow = tmp;
                }
                SelectionRange range = new SelectionRange(startRow, endRow);

                for (Widget w : Table.this.tBody) {
                    ITableRow row = (ITableRow) w;
                    if (range.inRange(row)) {
                        if (!row.isSelected()) {
                            row.toggleSelection();
                        }
                        selectedRowKeys.add(row.getKey());
                    }
                }

                // Add range
                if (startRow != endRow) {
                    selectedRowRanges.add(range);
                }
            }

            /*
             * (non-Javadoc)
             *
             * @see
             * com.vaadin.terminal.gwt.client.ui.IActionOwner#getActions
             * ()
             */

            @Override
            public Action[] getActions() {
                if (actionKeys == null) {
                    return new Action[]{};
                }
                final Action[] actions = new Action[actionKeys.length];
                for (int i = 0; i < actions.length; i++) {
                    final String actionKey = actionKeys[i];
                    final TreeAction a = new TreeAction(this, String.valueOf(rowKey), actionKey);
                    a.setCaption(getActionCaption(actionKey));
                    a.setIconUrl(getActionIcon(actionKey));
                    actions[i] = a;
                }
                return actions;
            }

            @Override
            public ApplicationConnection getClient() {
                return client;
            }

            @Override
            public String getPaintableId() {
                return paintableId;
            }

            @Override
            public RenderSpace getAllocatedSpace(Widget child) {
                int w = 0;
                int i = getColIndexOf(child);
                HeaderCell headerCell = tHead.getHeaderCell(i);
                if (headerCell != null) {
                    RenderInformation.Size paddingBorders = getElementPaddingBorders(DOM.getParent(child.getElement()));
                    if (paddingBorders != null) {
                        if (initializedAndAttached) {
                            w = headerCell.getWidth() - paddingBorders.getWidth();
                        } else {
                            // header offset width is not absolutely correct value,
                            // but
                            // a best guess (expecting similar content in all
                            // columns ->
                            // if one component is relative width so are others)
                            w = headerCell.getOffsetWidth() - paddingBorders.getWidth();
                        }
                    }
                }
                return new RenderSpace(w, getRowHeight());
            }

            protected int getColIndexOf(Widget child) {
                int index = -1;
                if (widgetColumns != null) {
                    Integer i = widgetColumns.get(child);
                    if (i != null) {
                        index = i;
                    }
                }
                return index;
            }

            @Override
            public boolean hasChildComponent(Widget component) {
                return childWidgets.contains(component);
            }

            @Override
            public void replaceChildComponent(Widget oldComponent,
                                              Widget newComponent) {
                com.google.gwt.dom.client.Element parentElement = oldComponent
                        .getElement().getParentElement();
                int index = childWidgets.indexOf(oldComponent);
                oldComponent.removeFromParent();

                parentElement.appendChild(newComponent.getElement());
                childWidgets.insertElementAt(newComponent, index);
                if (widgetColumns == null) {
                    widgetColumns = new HashMap<Widget, Integer>();
                }
                widgetColumns.remove(oldComponent);
                widgetColumns.put(newComponent, index);
                adopt(newComponent);

            }

            @Override
            public boolean requestLayout(Set<Paintable> children) {
                // row size should never change and system wouldn't event
                // survive as this is a kind of fake paitable
                return true;
            }

            @Override
            public void updateCaption(Paintable component, UIDL uidl) {
                // NOP, not rendered
            }

            @Override
            public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
                // Should never be called,
                // Component container interface faked here to get layouts
                // render properly
            }

            public void onScroll() {
                if (childWidgets != null) {
                    for (Object widget : childWidgets) {
                        if (widget instanceof VCustomComponent && ((VCustomComponent) widget).getWidget() instanceof VCustomComponent) {
                            VCustomComponent root = (VCustomComponent) ((VCustomComponent) widget).getWidget();
                            if (root != null && root.getWidget() instanceof VGridLayout) {
                                for (Widget child : ((VGridLayout) root.getWidget()).getChildWidgets()) {
                                    if (child instanceof VFilterSelect && ((VFilterSelect) child).isOpened()) {
                                        ((VFilterSelect) child).close();
                                    }
                                }
                            }
                        } else if (widget instanceof VPopupButton && ((VPopupButton) widget).isOpened()) {
                            ((VPopupButton) widget).close();
                        }
                    }
                }
            }
        }
    }

    public void deselectAll() {
        if (tBody != null) {
            for (Widget w : tBody) {
                ITableBody.ITableRow row = (ITableBody.ITableRow) w;
                if (row.isSelected()) {
                    row.toggleSelection();
                }
            }
        }
        // still ensure all selects are removed from (not necessary rendered)
        selectedRowKeys.clear();
        selectedRowRanges.clear();
        // also notify server that it clears all previous selections (the client
        // side does not know about the invisible ones)
        instructServerToForgotPreviousSelections();
    }

    /**
     * Used in multiselect mode when the client side knows that all selections
     * are in the next request.
     */
    private void instructServerToForgotPreviousSelections() {
        client.updateVariable(paintableId, "clearSelections", true, false);
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

            if (scrollbarWidthReservedInColumnKey != null && oldWidth > newWidth
                    && (oldWidth - newWidth) < scrollbarWidthReserved) {

                String colKey = scrollbarWidthReservedInColumnKey;
                int colIndex = getColIndexByKey(colKey);

                if (colIndex > 0) {
                    setColWidth(colIndex, getColWidth(colKey)
                            - (oldWidth - newWidth));
                    scrollbarWidthReservedInColumnKey = null;
                }
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
     * @param uidl possibly pwith values caption and icon
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
        for (final Widget bagItem : lazyUnregistryBag) {
            if (bagItem instanceof ITableBody.ITableRow) {
                ((ITableBody.ITableRow) bagItem).removeCells();
            }
            client.unregisterChildPaintables((HasWidgets) bagItem);
        }
        lazyUnregistryBag.clear();
    }

    protected class AggregationRow extends FlowPanel implements Container {

        protected boolean initialized = false;

        protected List<Element> tableCells = new ArrayList<Element>();

        protected char[] aligns;
        protected Element tr;

        public AggregationRow() {
            setStyleName(CLASSNAME + "-arow");
            DOM.setStyleAttribute(getElement(), "overflow", "hidden");
        }

        public void updateFromUIDL(UIDL uidl) {
            if (getElement().hasChildNodes()) {
                clear();
            }

            aligns = tHead.getColumnAlignments();

            if (uidl.getChildCount() > 0) {
                final Element table = DOM.createTable();
                DOM.setElementAttribute(table, "cellpadding", "0");
                DOM.setElementAttribute(table, "cellspacing", "0");
                final Element tBody = DOM.createTBody();
                tr = DOM.createTR();

                Tools.setStylePrimaryName(tr, CLASSNAME + "-arow-row");

                paintRow(uidl);

                DOM.appendChild(tBody, tr);
                DOM.appendChild(table, tBody);
                DOM.appendChild(getElement(), table);
            }

            initialized = getElement().hasChildNodes();
        }

        protected void paintRow(UIDL uidl) {
            int col = 0;
            for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
                final Object cell = it.next();
                String columnId = visibleColOrder[col];
                String style = "";
                if (uidl.hasAttribute("style-" + columnId)) {
                    style = uidl.getStringAttribute("style-" + columnId);
                }
                if (cell instanceof String) {
                    addCell((String) cell, aligns[col], style);
                } else {
                    Paintable p = client.getPaintable((UIDL) cell);
                    addCell((Widget) p, aligns[col], style);
                    p.updateFromUIDL((UIDL) cell, client);
                }

                final String colKey = getColKeyByIndex(col);
                int colWidth;
                if ((colWidth = getColWidth(colKey)) > -1) {
                    setColWidth(col, colWidth);
                }

                col++;
            }
        }

        @Override
        public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
            Element container = DOM.getParent(oldComponent.getElement());
            if (remove(oldComponent)) {
                add(newComponent, container);
            }
        }

        @Override
        public boolean hasChildComponent(Widget component) {
            return getChildren().contains(component);
        }

        @Override
        public void updateCaption(Paintable component, UIDL uidl) {
            //do nothing
        }

        @Override
        public boolean requestLayout(Set<Paintable> children) {
            return true;
        }

        @Override
        public RenderSpace getAllocatedSpace(Widget child) {
            return new RenderSpace(child.getElement().getOffsetWidth(), child.getElement().getOffsetHeight());
        }

        @Override
        public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
            //do nothing
        }

        public void setColWidth(int colIndex, int w) {
            if (initialized && tr != null) {
                final Element cell = DOM.getChild(tr, colIndex);
                setWidthDependsOnStyle(DOM.getFirstChild(cell), w);
                DOM.setStyleAttribute(cell, "width", w + "px");
            }
        }

        protected void addCell(String text, char align, String style) {
            final Element td = DOM.createTD();
            final Element container = DOM.createDiv();

            Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
            initCellStylePaddingBorders(Tools.setStylePrimaryName(container, CLASSNAME + "-cell-content"));
            if (style != null && !style.equals("")) {
                Tools.addStyleDependentName(td, style);
                initCellStylePaddingBorders(Tools.addStyleDependentName(container, style));
            }

            setCellText(container, text, false);
            setCellAlignment(container, align);

            DOM.appendChild(td, container);
            DOM.appendChild(tr, td);

            tableCells.add(td);

            Tools.textSelectionEnable(td, textSelectionEnabled);
        }

        protected void addCell(Widget widget, char align, String style) {
            final Element td = DOM.createTD();
            final Element container = DOM.createDiv();

            Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
            initCellStylePaddingBorders(Tools.setStylePrimaryName(container, CLASSNAME + "-cell-content"));
            if (style != null && !style.equals("")) {
                Tools.addStyleDependentName(td, style);
                initCellStylePaddingBorders(Tools.addStyleDependentName(container, style));
            }

            add(widget, container);

            setCellAlignment(container, align);

            DOM.appendChild(td, container);
            DOM.appendChild(tr, td);

            tableCells.add(td);
        }

        public void setHorizontalScrollPosition(int scrollLeft) {
            DOM.setElementPropertyInt(getElement(), "scrollLeft", scrollLeft);
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

    /**
     * Moves the selection head to a specific row
     *
     * @param row The row to where the selection head should move, may be null
     * @return Returns true if focus was moved successfully, else false
     */
    protected boolean setRowFocus(ITableBody.ITableRow row) {

        if (selectMode == SELECT_MODE_NONE) {
            return false;
        }

        // Remove previous selection
        if (focusedRow != null && focusedRow != row) {
            focusedRow.removeStyleName(CLASSNAME_SELECTION_FOCUS);
        }

        if (row != null) {

            // Apply focus style to new selection
            row.addStyleName(CLASSNAME_SELECTION_FOCUS);

            // Trying to set focus on already focused row
            if (row == focusedRow) {
                return false;
            }

            // Set new focused row
            focusedRow = row;

            ensureRowIsVisible(row);

            return true;
        }

        return false;
    }

    /**
     * Ensures that the row is visible
     *
     * @param row The row to ensure is visible
     */
    private void ensureRowIsVisible(ITableBody.ITableRow row) {
        scrollIntoViewVertically(row.getElement());
    }

    /**
     * Scrolls an element into view vertically only. Modified version of
     * Element.scrollIntoView.
     *
     * @param elem The element to scroll into view
     */
    private native void scrollIntoViewVertically(Element elem)
        /*-{
           var top = elem.offsetTop;
           var height = elem.offsetHeight;

           if (elem.parentNode != elem.offsetParent) {
             top -= elem.parentNode.offsetTop;
           }

           var cur = elem.parentNode;
           while (cur && (cur.nodeType == 1)) {
             if (top < cur.scrollTop) {
               cur.scrollTop = top;
             }
             if (top + height > cur.scrollTop + cur.clientHeight) {
               cur.scrollTop = (top + height) - cur.clientHeight;
             }

             var offsetTop = cur.offsetTop;
             if (cur.parentNode != cur.offsetParent) {
               offsetTop -= cur.parentNode.offsetTop;
             }

             top += offsetTop - cur.scrollTop;
             cur = cur.parentNode;
           }
        }-*/;

    /**
     * Removes a key from a range if the key is found in a selected range
     *
     * @param row row to remove
     */
    private void removeRowFromUnsentSelectionRanges(ITableBody.ITableRow row) {
        Collection<SelectionRange> newRanges = null;
        for (Iterator<SelectionRange> iterator = selectedRowRanges.iterator(); iterator
                .hasNext();) {
            SelectionRange range = iterator.next();
            if (range.inRange(row)) {
                // Split the range if given row is in range
                Collection<SelectionRange> splitranges = range.split(row);
                if (newRanges == null) {
                    newRanges = new ArrayList<SelectionRange>();
                }
                newRanges.addAll(splitranges);
                iterator.remove();
            }
        }
        if (newRanges != null) {
            selectedRowRanges.addAll(newRanges);
        }
    }

    protected void runWebkitOverflowAutoFix() {
        if (BrowserInfo.get().getWebkitVersion() > 0) {
            // run overflow fix in window
            if (parentOverflowContainer != null) {
                if (parentOverflowContainer instanceof VWindow)
                    ((VWindow) parentOverflowContainer).runWebkitOverflowFix();
                else if (parentOverflowContainer instanceof VTabsheet)
                    ((VTabsheet) parentOverflowContainer).runWebkitOverflowAutoFix();
                if (parentOverflowContainer instanceof VGroupBox)
                    ((VGroupBox) parentOverflowContainer).runWebkitOverflowAutoFix();
            }
        }
    }

    /**
     * Handles the keyboard events handled by the table
     * @param keycode The keyboard event received
     * @param ctrl Whether ctrl pressed or not
     * @param shift Whether shift pressed or not
     * @return true iff the navigation event was handled
     */
    protected boolean handleNavigation(int keycode, boolean ctrl, boolean shift) {
        if (keycode == KeyCodes.KEY_TAB || keycode == KeyCodes.KEY_SHIFT) {
            // Do not handle tab key
            return false;
        }

        // Down navigation
        if (selectMode == SELECT_MODE_NONE && keycode == getNavigationDownKey()) {
            bodyContainer.setScrollPosition(bodyContainer
                    .getScrollPosition() + scrollingVelocity);
            return true;
        } else if (keycode == getNavigationDownKey()) {
            if (selectMode == SELECT_MODE_MULTI && moveFocusDown()) {
                selectFocusedRow(ctrl, shift);

            } else if (selectMode == SELECT_MODE_SINGLE && !shift
                    && moveFocusDown()) {
                selectFocusedRow(ctrl, shift);
            }
            return true;
        }

        // Up navigation
        if (selectMode == SELECT_MODE_NONE && keycode == getNavigationUpKey()) {
            bodyContainer.setScrollPosition(bodyContainer
                    .getScrollPosition() - scrollingVelocity);
            return true;
        } else if (keycode == getNavigationUpKey()) {
            if (selectMode == SELECT_MODE_MULTI && moveFocusUp()) {
                selectFocusedRow(ctrl, shift);
            } else if (selectMode == SELECT_MODE_SINGLE && !shift
                    && moveFocusUp()) {
                selectFocusedRow(ctrl, shift);
            }
            return true;
        }

        if (keycode == getNavigationLeftKey()) {
            // Left navigation
            bodyContainer.setHorizontalScrollPosition(bodyContainer
                    .getHorizontalScrollPosition() - scrollingVelocity);
            return true;

        } else if (keycode == getNavigationRightKey()) {
            // Right navigation
            bodyContainer.setHorizontalScrollPosition(bodyContainer
                    .getHorizontalScrollPosition() + scrollingVelocity);
            return true;
        }

        // Select navigation
        if (isSelectable() && keycode == getNavigationSelectKey()) {
            if (selectMode == SELECT_MODE_SINGLE) {
                boolean wasSelected = focusedRow.isSelected();
                deselectAll();
                if (!wasSelected || nullSelectionDisallowed) {
                    focusedRow.toggleSelection();
                }
            } else {
                focusedRow.toggleSelection();
                removeRowFromUnsentSelectionRanges(focusedRow);
            }

            sendSelectedRows();
            return true;
        }

        // Page Down navigation
        if (keycode == getNavigationPageDownKey()) {
            return handleNavigationPageDownKey(ctrl, shift);
        }

        // Page Up navigation
        if (keycode == getNavigationPageUpKey()) {
            return handleNavigationPageUpKey(ctrl, shift);
        }

        // Goto start navigation
        if (keycode == getNavigationStartKey()) {
            bodyContainer.setScrollPosition(0);
            if (isSelectable()) {
                if (focusedRow != null && focusedRow.getIndex() == 0) {
                    return false;
                } else {
                    ITableBody.ITableRow rowByRowIndex = (ITableBody.ITableRow) tBody
                            .iterator().next();
                    if (rowByRowIndex.getIndex() == 0) {
                        setRowFocus(rowByRowIndex);
                        selectFocusedRow(ctrl, shift);
                        sendSelectedRows();
                    } else {
                        // first row of table will come in next row fetch
                        if (ctrl) {
                            focusFirstItemInNextRender = true;
                        } else {
                            selectFirstItemInNextRender = true;
                            multiselectPending = shift;
                        }
                    }
                }
            }
            return true;
        }

        // Goto end navigation
        if (keycode == getNavigationEndKey()) {
            return handleNavigationEndKey(ctrl, shift);
        }

        return false;
    }

    protected abstract boolean handleNavigationPageDownKey( boolean ctrl, boolean shift );

    protected abstract boolean handleNavigationPageUpKey( boolean ctrl, boolean shift );

    protected abstract boolean handleNavigationEndKey( boolean ctrl, boolean shift );

    protected boolean isFocusAtTheBeginningOfTable() {
        return focusedRow.getIndex() == 0;
    }

    protected boolean isFocusAtTheEndOfTable() {
        return focusedRow.getIndex() + 1 >= totalRows;
    }

    protected int getFullyVisibleRowCount() {
        return (int) (bodyContainer.getOffsetHeight() / tBody
                .getRowHeight());
    }

    protected void scrollByPagelenght(int i) {
        int pixels = i
                * (int) (getFullyVisibleRowCount() * tBody.getRowHeight());
        int newPixels = bodyContainer.getScrollPosition() + pixels;
        if (newPixels < 0) {
            newPixels = 0;
        } // else if too high, NOP (all know browsers accept illegally big
          // values here)
        bodyContainer.setScrollPosition(newPixels);
    }

    @Override
    public void focus(){
        bodyContainer.focus();
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    /*
    * (non-Javadoc)
    *
    * @see
    * com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event
    * .dom.client.FocusEvent)
    */
    @Override
    public void onFocus(FocusEvent event) {
        if (isFocusable()) {
            hasFocus = true;

            // Focus a row if no row is in focus
            if (focusedRow == null) {
                focusRowFromBody();
//                selectFocusedRow(false,false);
//                sendSelectedRows();
            } else {
                setRowFocus(focusedRow);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event
     * .dom.client.BlurEvent)
     */
    @Override
    public void onBlur(BlurEvent event) {
        hasFocus = false;
        navKeyDown = false;

        if (isFocusable()) {
            // Unfocus any row
            setRowFocus(null);
        }
    }

    /**
     * Can the Table be focused?
     *
     * @return True if the table can be focused, else false
     */
    public boolean isFocusable() {
        if (tBody != null && enabled) {
            boolean hasVerticalScrollbars = tBody.getOffsetHeight() > bodyContainer
                    .getOffsetHeight();
            boolean hasHorizontalScrollbars = tBody.getOffsetWidth() > bodyContainer
                    .getOffsetWidth();
            return !(!hasHorizontalScrollbars && !hasVerticalScrollbars && selectMode == SELECT_MODE_NONE);
        }

        return false;
    }

    public void startScrollingVelocityTimer() {
        if (scrollingVelocityTimer == null) {
            scrollingVelocityTimer = new com.google.gwt.user.client.Timer() {
                @Override
                public void run() {
                    scrollingVelocity++;
                }
            };
            scrollingVelocityTimer.scheduleRepeating(100);
        }
    }

    public void cancelScrollingVelocityTimer() {
        if (scrollingVelocityTimer != null) {
            // Remove velocityTimer if it exists and the Table is disabled
            scrollingVelocityTimer.cancel();
            scrollingVelocityTimer = null;
            scrollingVelocity = 10;
        }
    }

    /**
     *
     * @param keyCode Key code
     * @return true if the given keyCode is used by the table for navigation
     */
    private boolean isNavigationKey(int keyCode) {
        return keyCode == getNavigationUpKey()
                || keyCode == getNavigationLeftKey()
                || keyCode == getNavigationRightKey()
                || keyCode == getNavigationDownKey()
                || keyCode == getNavigationPageUpKey()
                || keyCode == getNavigationPageDownKey()
                || keyCode == getNavigationEndKey()
                || keyCode == getNavigationStartKey();
    }

    /**
     * Add this to the element mouse down event by using element.setPropertyJSO
     * ("onselectstart",applyDisableTextSelectionIEHack()); Remove it then again
     * when the mouse is depressed in the mouse up event.
     *
     * @return Returns the JSO preventing text selection
     */
    private static native JavaScriptObject getPreventTextSelectionIEHack()
    /*-{
            return function(){ return false; };
    }-*/;
}
