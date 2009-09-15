package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.Action;
import com.vaadin.terminal.gwt.client.ui.ActionOwner;
import com.vaadin.terminal.gwt.client.ui.VContextMenu;
import com.vaadin.terminal.gwt.client.ui.Table;

import java.util.*;

public class ITreeTable
        extends FlowPanel
        implements Table,
        ClickListener
{
    public static final String CLASSNAME = "v-tree-table";
    public static final String CLASSNAME_ROW_SELECTED = "v-selected";
    public static final String CLASSNAME_ROW_EXPANDED = "v-expanded";

    public static final char ALIGN_CENTER = 'c';
    public static final char ALIGN_LEFT = 'b';
    public static final char ALIGN_RIGHT = 'e';

    public static final int DEFAULT_ROW_HEIGHT = 24;

    public static final int CELL_CONTENT_PADDING = 8;

    public static final int MIN_HEIGHT = 24;

    private ApplicationConnection client;
    private String uidlId;

    private boolean rowHeaders = false;

    private int selectMode = Table.SELECT_MODE_NONE;

    private final Vector<String> selectedRowKeys = new Vector<String>();

    private boolean sortAscending = true;

    private final HashMap rowKeysToTableRows = new HashMap();

    private final TableHeader tableHeader = new TableHeader();

//    private final FlowPanel tablePanel = new FlowPanel();

    private final ScrollPanel bodyContainer = new ScrollPanel();
    private final TableBody tableBody = new TableBody();

    private int totalRows;

    private String height;
    private String width = "";

    private boolean relativeWidth = false;

    private Set<String> collapsedColumns = null;

    private boolean emitClickEvents;

    private static Console log = ApplicationConnection.getConsole();

    private int calculatedCrossSignWidth = 0;

    public ITreeTable() {
        bodyContainer.setStyleName(CLASSNAME + "-body");
        DOM.setStyleAttribute(bodyContainer.getElement(), "overflow", "auto");
//        DOM.setStyleAttribute(bodyContainer.getElement(), "width", "100%");

        bodyContainer.add(tableBody);

        setStyleName(CLASSNAME);
        add(tableHeader);
        add(bodyContainer);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        uidlId = uidl.getId();

        selectedRowKeys.clear();

//        if (uidl.hasVariable("selected")) {
//            final Set selectedKeys = uidl
//                    .getStringArrayVariableAsSet("selected");
//            selectedRowKeys.clear();
//            for (final Object o : selectedKeys) {
//                selectedRowKeys.add((String) o);
//            }
//        }

        totalRows = uidl.getIntAttribute("rows");

        if (uidl.hasAttribute("selectmode")) {
/*if (uidl.getBooleanAttribute("readonly")) {
                selectMode = Table.SELECT_MODE_NONE;
            } else*/
            if (uidl.getStringAttribute("selectmode").equals("multi")) {
                selectMode = Table.SELECT_MODE_MULTI;
            } else if (uidl.getStringAttribute("selectmode").equals("single")) {
                selectMode = Table.SELECT_MODE_SINGLE;
            } else {
                selectMode = Table.SELECT_MODE_NONE;
            }
        }

        if (uidl.hasVariable("sortascending")) {
            sortAscending = uidl.getBooleanVariable("sortascending");
        }

        if (uidl.hasAttribute("rowheaders")) {
            rowHeaders = true;
        }

        if (uidl.hasVariable("collapsedcolumns")) {
            tableHeader.setColumnCollapsingAllowed(true);
            collapsedColumns = uidl.getStringArrayVariableAsSet("collapsedcolumns");
        } else {
            tableHeader.setColumnCollapsingAllowed(false);
            collapsedColumns = null;
        }

        UIDL bodyUidl = null;
        UIDL colsUidl = null;
        UIDL actionsUidl = null;
        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL data = (UIDL) it.next();
            if ("rows".equals(data.getTag())) {
                bodyUidl = data;
            } else if ("actions".equals(data.getTag())) {
                actionsUidl = data;
            } else if ("visiblecolumns".equals(data.getTag())) {
                colsUidl = data;
            }
        }
        if (uidl.hasAttribute("width")) {
            relativeWidth = uidl.getStringAttribute("width").endsWith("%");
        }
//        if (uidl.hasAttribute("height")) {
//            height = uidl.getStringAttribute("height");
//        }

        emitClickEvents = uidl.getBooleanAttribute("listenClicks");

        updateActionsFromUIDL(actionsUidl);
        updateHeaderFromUIDL(colsUidl);
        updateBodyFromUIDL(bodyUidl);

        if (isAttached()) {
            sizeInit();
        }
    }

    private void updateHeaderFromUIDL(UIDL uidl) {
        if (uidl != null) {
            tableHeader.updateHeaderFromUIDL(uidl);
        }
    }

    private void updateActionsFromUIDL(UIDL uidl) {
        // TODO
    }

    private void updateBodyFromUIDL(UIDL uidl) {
        if (uidl != null) {
            tableBody.updateBodyFromUIDL(uidl);
        }
    }

    public void onClick(Widget sender) {
        //todo
    }

    public void deselectAll() {
        //todo
    }

    private void sizeInit()
    {
        Iterator headerCells = tableHeader.iterator();
        final int[] widths = new int[tableHeader.visibleCells.size()];

        int totalWidth = 0;
        int i = 0;

//        if (width == null || "".equals(width)) {
//            setContentWidth(-1);
//        }

        while (headerCells.hasNext())
        {
            final TableHeader.Cell cell =
                    (TableHeader.Cell) headerCells.next();
            int w = cell.getWidth();
            if (w < 0) {
                int hw = cell.getOffsetWidth();
                int bw = tableBody.getColWidth(i) +
                        (i == 0 ? tableBody.getDeep() * calculatedCrossSignWidth : 0);
                w = (hw > bw ? hw : bw);
            }
            totalWidth += w;
            widths[i++] = w;
        }

        if (height == null || "".equals(height)) {
            int rowsCount = tableBody.getVisibleRowsCount();
            if (rowsCount == 0) {
                rowsCount = 1;
            }
            bodyContainer.setHeight(rowsCount * tableBody.getRowHeight() + "px");
        }

/*
        if (width == null || "".equals(width)) {
            int w = totalWidth;
            w += getScrollbarWidth();
            setContentWidth(w);
        }
*/

        int availableWidth = getBodyContainerWidth();

        final int extraWidth = availableWidth - totalWidth;

        headerCells = tableHeader.iterator();
        i = 0;
        while (headerCells.hasNext()) {
            final TableHeader.Cell cell = (TableHeader.Cell) headerCells.next();
            if (cell.getWidth() == -1) {
                int w = widths[i];
                w += (extraWidth * w / totalWidth);
                widths[i] = w;
            }
            i++;
        }

        i = 0;
        headerCells = tableHeader.iterator();
        while (headerCells.hasNext()) {
            final TableHeader.Cell cell = (TableHeader.Cell) headerCells.next();
            if (cell.getWidth() < 0) {
                cell.setWidth(widths[i]);
            }
            tableBody.setColWidth(i, widths[i]);
            i++;
        }

        tableBody.updatePaddings();
    }

    public void setWidth(String width) {
        if (this.width.equals(width)) {
            return;
        }
        this.width = width;
        if (width != null && !"".equals(width)) {
            super.setWidth(width);
        }
    }

    @Override
    public void setHeight(String height) {
        this.height = height;
        super.setHeight(height);
        setContainerHeight();
    }

    private boolean isCollapsedColumn(String colKey) {
        return collapsedColumns != null
                && collapsedColumns.contains(colKey);
    }

    private Set<String> getCollapsedColumns() {
        if (collapsedColumns == null) {
            collapsedColumns = new HashSet<String>();
        }
        return collapsedColumns;
    }

    protected void setContentWidth(int w) {
        if (w == -1) {
            tableHeader.setWidth("");
            tableBody.setWidth("");
        } else {
            tableHeader.setWidth(w + "px");
            tableBody.setWidth(w + "px");
        }
    }

//    private int tableBodyAvailableWidth() {
//        return tableBody.availableWidth();
//    }

    private int getBodyContainerHeight() {
        int h = getOffsetHeight() - tableHeader.getOffsetHeight();
        h -= getPaddingsAndBorderHeight();
        if (h < 0) {
            h = 0;
        }
        return h;
    }

    private int getBodyContainerWidth() {
        return getOffsetWidth() - getPaddingsAndBorderWidth(); //todo may be also need to use scroll bar width
    }

    protected void setContainerHeight() {
        if (height != null && !"".equals(height)) {
            int h = getBodyContainerHeight();
            bodyContainer.setHeight(h + "px");
        }
    }

    private int paddingsAndBorderHeight = -1;

    private int getPaddingsAndBorderHeight() {
        if (paddingsAndBorderHeight < 0) {
            paddingsAndBorderHeight = Util.measureVerticalPaddingAndBorder(getElement(), 2);
        }
        return paddingsAndBorderHeight;
    }

    private int paddingsAndBorderWidth = -1;

    private int getPaddingsAndBorderWidth() {
        if (paddingsAndBorderWidth < 0) {
            paddingsAndBorderWidth = Util.measureHorizontalPaddingAndBorder(getElement(), 2);
        }
        return paddingsAndBorderWidth;
    }

    public int getScrollbarWidth() {
        return bodyContainer.getOffsetWidth()
                - DOM.getElementPropertyInt(bodyContainer.getElement(),
                        "clientWidth");
    }

    class TableHeader extends Panel implements ActionOwner {

        private final Element headerBody = DOM.createDiv();
        private final Element columnsSelector = DOM.createDiv();

        private final Map<String, Widget> availableCells = new HashMap<String, Widget>(); //cache of available cells
        private final Vector<Widget> visibleCells = new Vector<Widget>(); //currently visible cells

        private VisibleColumnsMenu columnsMenu = null;

        private boolean columnCollapsingAllowed = false;

        TableHeader() {
            setElement(DOM.createDiv());
            setStyleName(CLASSNAME + "-header-wrap");

            DOM.setElementProperty(headerBody, "className", CLASSNAME + "-header");
            DOM.setStyleAttribute(headerBody, "overflow", "hidden");
            DOM.setElementProperty(columnsSelector, "className", CLASSNAME + "-column-selector");

            DOM.appendChild(getElement(), headerBody);
            DOM.appendChild(getElement(), columnsSelector);

            DOM.sinkEvents(columnsSelector, Event.ONCLICK);
        }

        public void updateHeaderFromUIDL(UIDL uidl) {
            final Iterator it = uidl.getChildIterator();

            clear(); //clear old visible cells

            while (it.hasNext())
            {
                final UIDL col = (UIDL) it.next();
                final String cid = col.getStringAttribute("cid");
                final String caption = col.getStringAttribute("caption");

                Cell c = (Cell) availableCells.get(cid);
                if (c == null) {
                    c = new Cell(cid, caption);
                    availableCells.put(cid, c);
                } else {
                    c.setCaption(caption);
                }
                if (!col.hasAttribute("collapsed")) //if the cell is visible then we must update it's properties
                {
                    if (col.hasAttribute("width")) {
                        final String width = col.getStringAttribute("width");
                        c.setWidth(Integer.parseInt(width));
                    } else {
                        c.setWidth(-1);
                    }
                    addCell(c);
                }
            }
        }

        private void addCell(Cell c) {
            DOM.appendChild(headerBody, c.getElement());
            adopt(c);
            visibleCells.add(c);
        }

        public int getColumnsSelectorWidth() {
            return DOM.getElementPropertyInt(columnsSelector, "offsetWidth");
        }

        private VisibleColumnsMenu getColumnsMenu() {
            if (columnsMenu == null) {
                columnsMenu = new VisibleColumnsMenu();
            }
            return columnsMenu;
        }

        @Override
        public void clear() {
            final Vector<Widget> v = new Vector<Widget>(visibleCells);
            for (final Widget w : v) {
                remove(w);
            }
        }

        public boolean remove(Widget child) {
            if (visibleCells.contains(child)) {
                visibleCells.remove(child);
                orphan(child);
                DOM.removeChild(DOM.getParent(child.getElement()), child.getElement());
                return true;
            }
            return false;
        }

        public Iterator<Widget> iterator() {
            return visibleCells.iterator();
        }

        public void onBrowserEvent(Event event) {
            if (event.getTarget() == columnsSelector) {
                final int left = DOM.getAbsoluteLeft(columnsSelector);
                final int top = DOM.getAbsoluteTop(columnsSelector)
                        + DOM.getElementPropertyInt(columnsSelector,
                        "offsetHeight");
                getColumnsMenu().showAt(this, left, top);
            }
        }

        public Action[] getActions()
        {
            final List<Action> actions = new LinkedList<Action>();

            for (final Map.Entry<String, Widget> entry
                    : availableCells.entrySet())
            {
                boolean collapsed = false;
                final Cell cell = (Cell) entry.getValue();
                final String key = entry.getKey();

                if (isCollapsedColumn(key)) {
                    collapsed = true;
                } else if (!visibleCells.contains(cell)) {
                    continue;
                }

                final VisibleColumnAction action = new VisibleColumnAction(key);
                action.setCaption(cell.getCaption());
                action.setCollapsed(collapsed);
                actions.add(action);
            }

            return actions.toArray(new Action[actions.size()]);
        }

        public ApplicationConnection getClient() {
            return client;
        }

        public String getPaintableId() {
            return uidlId;
        }

        public boolean isColumnCollapsingAllowed() {
            return columnCollapsingAllowed;
        }

        public void setColumnCollapsingAllowed(boolean columnCollapsingAllowed) {
            this.columnCollapsingAllowed = columnCollapsingAllowed;
            if (columnCollapsingAllowed) {
                DOM.setStyleAttribute(columnsSelector, "display", "block");
            } else {
                DOM.setStyleAttribute(columnsSelector, "display", "none");
            }
        }

        class VisibleColumnAction
                extends Action
        {
            private final String cid;
            private boolean collapsed;

            VisibleColumnAction(String cid) {
                super(TableHeader.this);
                this.cid = cid;
            }

            public void execute()
            {
                getColumnsMenu().hide();
                // toggle selected column
                if (isCollapsedColumn(cid)) {
                    getCollapsedColumns().remove(cid);
                } else {
                    getCollapsedColumns().add(cid);
                }
                // update variable to server
                client.updateVariable(uidlId, "collapsedcolumns",
                        getCollapsedColumns().toArray(), true);
            }

            public String getCid() {
                return cid;
            }

            public boolean isCollapsed() {
                return collapsed;
            }

            public void setCollapsed(boolean collapsed) {
                this.collapsed = collapsed;
            }

            public String getHTML() {
                final StringBuffer sb = new StringBuffer();
                if (collapsed) {
                    sb.append("<span class=\"" + CLASSNAME + "-column-off\">");
                } else {
                    sb.append("<span class=\"" + CLASSNAME + "-column-on\">");
                }
                sb.append(super.getHTML());
                sb.append("</span>");

                return sb.toString();
            }
        }

        class VisibleColumnsMenu extends VContextMenu {
            VisibleColumnsMenu() {
                super();
                setActionOwner(TableHeader.this);
                setStyleName(CLASSNAME + "-menu");
            }
        }

        class Cell extends Widget {
            private static final int DRAG_WIDGET_WIDTH = 4;

            private final String cid;
            private String caption;

            private final Element self = DOM.createDiv();
            private final Element resizer = DOM.createDiv();
            private final Element captionContainer = DOM.createDiv();

            private int width = -1;

            Cell(String cid, String caption) {
                this.cid = cid;
                setElement(self);
                DOM.setElementProperty(self, "className", CLASSNAME + "-header-cell");

                DOM.setElementProperty(resizer, "className", CLASSNAME + "-resizer");
                DOM.setStyleAttribute(resizer, "width", "4px");

                setCaption(caption);
                DOM.setElementProperty(captionContainer, "className", CLASSNAME + "-caption-container");

                DOM.appendChild(self, captionContainer);
                DOM.appendChild(self, resizer);

                DOM.sinkEvents(self, Event.MOUSEEVENTS);
            }

            public String getCid() {
                return cid;
            }

            public void setCaption(String newCaption) {
                if (caption == null || !caption.equals(newCaption)) {
                    this.caption = newCaption;
                    DOM.setInnerHTML(captionContainer,
                            "<span class=\""
                                    + CLASSNAME
                                    + "-caption\">"
                                    + caption != null ? caption : ""
                                    + "</span>"
                    );
                }
            }

            public String getCaption() {
                return caption;
            }

            public void setWidth(int w) {
                if (w == -1) {
                    DOM.setStyleAttribute(captionContainer, "overflow", "");
                }
                width = w;
                if (w == -1) {
                    DOM.setStyleAttribute(captionContainer, "width", "");
                    super.setWidth("");
                } else {
                    w -= (isLast() ? TableHeader.this.getColumnsSelectorWidth() : 0);
                    DOM.setStyleAttribute(captionContainer, "width", (w
                            - DRAG_WIDGET_WIDTH)
                            + "px");
                    super.setWidth(w + "px");
                }
            }

            public int getWidth() {
                return width;
            }

            private boolean isLast() {
                return (TableHeader.this.visibleCells.get(TableHeader.this.visibleCells.size() - 1) == this);
            }

            public void onBrowserEvent(Event event) {
            }
        }
    }

    class TableBody extends Panel {

        private String icon;

        private final Vector<Widget> children = new Vector<Widget>(); //contains rows and captions
        private final Vector<Row> rows = new Vector<Row>(); //contains only rows, needed for an implementation of a resize mechanism

        private final Element sizer = DOM.createDiv();
        private final Element bodyContent = DOM.createDiv();

        TableBody() {
            setElement(DOM.createDiv());

            DOM.setElementProperty(sizer, "className", CLASSNAME + "-body-sizer");
            DOM.appendChild(getElement(), sizer);

            DOM.setElementProperty(bodyContent, "className", CLASSNAME + "-content");
            DOM.appendChild(getElement(), bodyContent);
        }

        public int availableWidth() {
            return DOM.getElementPropertyInt(sizer, "offsetWidth");
        }

        public int getDeep() {
            int deep = 0;
            for (final Row r : rows) {
                deep = Math.max(deep, r.getLevel());
            }
            return deep;
        }

        void updateBodyFromUIDL(UIDL uidl) {
            clear();
            updateBodyRows(uidl.getChildIterator());
        }

        void updateBodyRows(Iterator rowsIterator) {
            while (rowsIterator.hasNext()) {
                final UIDL row = (UIDL) rowsIterator.next();
                if ("gr".equals(row.getTag())
                        || "tr".equals(row.getTag()))
                {
                    boolean expanded = false;
                    boolean groupped = ("gr".equals(row.getTag()));
                    boolean isCaption = (row.getStringAttribute("caption") != null);

                    final String key = row.getStringAttribute("key");
                    AbstractRow r;
                    if (groupped)
                    {
                        expanded = row.getBooleanAttribute("expanded");
                        if (isCaption) {
                            r = new GroupCaptionRow(key, row.getIntAttribute("level"),
                                    expanded
                            );
                        } else {
                            r = new GroupRow(key, row.getIntAttribute("level"),
                                    expanded
                            );
                        }
                    } else {
                        if (isCaption) {
                            r = new CaptionRow(key, row.getIntAttribute("level"));
                        } else {
                            r = new Row(key, row.getIntAttribute("level"));
                        }
                    }

                    if (isCaption) {
                        addCaptionRow((CaptionRow) r);
                    } else {
                        addRow((Row) r);
                    }

                    r.updateRowFromUIDL(row);

                    if (expanded)
                    {
                        updateBodyRows(row.getChildIterator());
                    }
                }
            }
        }

        private Cell getCell(int row, int col) {
            return getRow(row).getCell(col);
        }

        public Row getRow(int row) {
            if (row < 0 || row >= rows.size()) {
                throw new IndexOutOfBoundsException();
            }
            return rows.get(row);
        }

        int getColWidth(int col) {
            return getCell(0, col).getWidth();
        }

        void setColWidth(int col, int w) {
            for (int i = 0; i < rows.size(); i++) {
                final Row row = getRow(i);
                final Cell cell = row.getCell(col);

                int newWidth = w;
                if (col == 0) {
                    newWidth = w - calculatedCrossSignWidth * (row.getLevel() + 1);
                }

                cell.setWidth(newWidth);
            }
        }

        int getRowHeight() {
            int rowHeight = getCell(0, 0).getHeight();
            if (rowHeight > 0) {
                return rowHeight;
            }
            return DEFAULT_ROW_HEIGHT;
        }

        int getVisibleRowsCount() {
            return children.size();
        }

        void updatePaddings() {
            for (final Widget w : rows) {
                final Row row = (Row) w;

                int l = row.getLevel();
                if (!(row instanceof GroupRow)) {
                    l +=1;
                }
                DOM.setStyleAttribute(row.getElement(), "paddingLeft", l * calculatedCrossSignWidth + "px");
            }
        }

        protected void addRow(Row r) {
            String className;
            if (rows.size() % 2 == 1) {
                className = "-row-odd";
            } else {
                className = "-row";
            }
            DOM.setElementProperty(r.getElement(), "className", CLASSNAME + className);
            add(r);
            rows.add(r);
        }

        protected void addCaptionRow(CaptionRow r) {
            add(r);
        }

        public void add(Widget child) {
            DOM.appendChild(bodyContent, child.getElement());
            adopt(child);
            children.add(child);
        }

        public boolean remove(Widget child) {
            if (children.contains(child)) {
                orphan(child);
                DOM.removeChild(DOM.getParent(child.getElement()), child.getElement());
                children.remove(child);
                if (child instanceof Row) rows.remove(child);
                return true;
            }
            return false;
        }

        public Iterator<Widget> iterator() {
            return children.iterator();
        }

        public void clear() {
            final  Vector<Widget> v = new Vector<Widget>(children);
            for (final Widget w : v) {
                remove(w);
            }
        }

        class GroupCaptionRow
                extends CaptionRow
                implements Group
        {
            private Vector<Widget> children = new Vector<Widget>();
            private boolean expanded;

            private final CrossSign cross = new CrossSign();

            GroupCaptionRow(String key, int level, boolean expanded) {
                super(key, level);

                cross.setExpanded(expanded);
                DOM.insertChild(getElement(), cross.getElement(), 0);
                adopt(cross);

                this.expanded = expanded;

                DOM.sinkEvents(cross.getElement(), Event.ONCLICK);
            }

            @Override
            public void onBrowserEvent(Event event) {
                if (event.getTarget() == cross.getElement()) {
                    switch (event.getTypeInt()) {
                        case Event.ONCLICK:
                            if (expanded) {
                                client.updateVariable(uidlId, "collapse", getKey(), true);
                            } else {
                                client.updateVariable(uidlId, "expand", getKey(), true);
                            }
                            DOM.eventCancelBubble(event, true);
                            break;
                    }
                }
            }

            public boolean isExpanded() {
                return expanded;
            }

            public Vector<Widget> getChildren() {
                return children;
            }

            public boolean hasChildren() {
                return !getChildren().isEmpty();
            }
        }

        class CaptionRow extends AbstractRow {

            private Element inner = DOM.createDiv();

            CaptionRow(String key, int level) {
                super(key, level);
                setStyleName(CLASSNAME + "-row-caption");

                DOM.setElementProperty(inner, "className", CLASSNAME + "-row-caption-inner");
                DOM.appendChild(getElement(), inner);
            }

            @Override
            public void updateRowFromUIDL(UIDL uidl) {
                DOM.setInnerHTML(inner, uidl.getStringAttribute("caption"));
            }
        }

        class GroupRow extends Row
                implements Group
        {
            private Vector<Widget> children = new Vector<Widget>();
            private boolean expanded;

            private final CrossSign cross = new CrossSign();

            GroupRow(String key, int level, boolean expanded) {
                super(key, level);

                cross.setExpanded(expanded);
                DOM.appendChild(getElement(), cross.getElement());
                adopt(cross);

                this.expanded = expanded;

                DOM.sinkEvents(cross.getElement(), Event.ONCLICK);
            }

            @Override
            protected void updateCellsFromUIDL(UIDL uidl) {
                UIDL cellsUidl = null;
                final Iterator tags = uidl.getChildIterator();
                while (tags.hasNext()) {
                    final UIDL t = (UIDL) tags.next();
                    if ("c".equals(t.getTag())) {
                        cellsUidl = t;
                        break;
                    }
                }

                assert cellsUidl != null : "Cannot find tag <c>";

                super.updateCellsFromUIDL(cellsUidl);
            }

            @Override
            protected void onAttach() {
                super.onAttach();
                if (calculatedCrossSignWidth == 0) {
                    calculatedCrossSignWidth = cross.getOffsetWidth();
                }
            }

            @Override
            public void onBrowserEvent(Event event) {
                if (event.getTarget() == cross.getElement()) {
                    switch (event.getTypeInt()) {
                        case Event.ONCLICK:
                            if (expanded) {
                                client.updateVariable(uidlId, "collapse", getKey(), true);
                            } else {
                                client.updateVariable(uidlId, "expand", getKey(), true);
                            }
                            DOM.eventCancelBubble(event, true);
                            break;
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }

            public boolean isExpanded() {
                return expanded;
            }

            public Vector<Widget> getChildren() {
                return children;
            }

            public boolean hasChildren() {
                return !getChildren().isEmpty();
            }
        }

        class Row extends AbstractRow {

            protected final Vector<Widget> visibleCells = new Vector<Widget>();

            Row(String key, int level) {
                super(key, level);
            }

            public boolean isSelected() {
                return selectedRowKeys.contains(getKey());
            }

            private void toggleSelection() {
                if (!isSelected()) {
                    if (selectMode == Table.SELECT_MODE_SINGLE) {
                        deselectAll();
                    }
                    selectedRowKeys.add(getKey());
                    addStyleName(CLASSNAME_ROW_SELECTED);
                } else {
                    selectedRowKeys.remove(getKey());
                    removeStyleName(CLASSNAME_ROW_SELECTED);
                }
            }

            public void updateRowFromUIDL(UIDL uidl)
            {
                if (uidl.hasAttribute("selected") && !isSelected()) {
                    toggleSelection();
                }
                updateCellsFromUIDL(uidl);
            }

            protected void updateCellsFromUIDL(UIDL uidl) {
                Iterator cells = uidl.getChildIterator();
                visibleCells.clear();
                while (cells.hasNext()) {
                    final Object o = cells.next();
                    Cell cell;
                    Paintable cellWidget = null;
                    if (o instanceof String) {
                        cell = new Cell((String) o);
                    } else {
                        cellWidget = client.getPaintable((UIDL) o);
                        cell = new Cell((Widget) cellWidget);
                    }
                    add(cell);
                    if (cellWidget != null) {
                        paintCell(cellWidget, (UIDL) o);
                    }
                }
            }

            private void paintCell(Paintable p, UIDL uidl) {
                if (isAttached()) {
                    p.updateFromUIDL(uidl, client);
                }
            }


            public void add(Widget child) {
                DOM.appendChild(getElement(), child.getElement());
                adopt(child);
                visibleCells.add(child);
            }

            public boolean remove(Widget child) {
                if (visibleCells.contains(child)) {
                    visibleCells.remove(child);
                    orphan(child);
                    DOM.removeChild( DOM.getParent(child.getElement()), child.getElement());
                    return true;
                }
                return false;
            }

            public Iterator<Widget> iterator() {
                return visibleCells.iterator();
            }

            @Override
            public void onBrowserEvent(Event event) {
                if (event.getCurrentTarget() == getElement()) {
                    switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        handleClickEvent(event);
                        if (selectMode > Table.SELECT_MODE_NONE) {
                            toggleSelection();
                            log.log("Row: " + getKey() + " is " + (isSelected() ? " selected" : " deselected"));
                            client.updateVariable(uidlId, "selected",
                                    selectedRowKeys.toArray(), true);
                        }
                        break;
                    }
                }
            }

            public Cell getCell(int index) {
                if (index < 0 || index >= visibleCells.size()) {
                    throw new IndexOutOfBoundsException();
                }
                return (Cell) visibleCells.get(index);
            }
        }

        abstract class AbstractRow extends Panel {
            private final String key;
            private final int level;

            protected AbstractRow(String key, int level) {
                setElement(DOM.createDiv());

                this.key = key;
                this.level = level;

                DOM.sinkEvents(getElement(), Event.ONCLICK);
            }

            void handleClickEvent(Event event) {
                if (emitClickEvents) {
                    if (event.getCurrentTarget() == getElement()) {
                        boolean dbl = DOM.eventGetType(event) == Event.ONDBLCLICK;
                        client.updateVariable(uidlId, "clickedKey", key, false);
                        //todo Пока не знаю как найти ячейку
//                    final Element tdOrTr = DOM.getParent(DOM
//                            .eventGetTarget(event));
//                    if (getElement() == tdOrTr.getParentElement()) {
//                        int childIndex = DOM
//                                .getChildIndex(getElement(), tdOrTr);
//                        String colKey = null;
//                        colKey = tHead.getHeaderCell(childIndex).getColKey();
//                        client.updateVariable(uidlId, "clickedColKey",
//                                colKey, false);
//                    }
                        MouseEventDetails details = new MouseEventDetails(event);
                        client.updateVariable(uidlId, "clickEvent",
                                details.toString(), !(!dbl && selectMode > Table.SELECT_MODE_NONE));
                    }
                }
            }

            public String getKey() {
                return key;
            }

            public int getLevel() {
                return level;
            }

            public abstract void updateRowFromUIDL(UIDL uidl);

            public void onBrowserEvent(Event event) {
            }

            public boolean remove(Widget child) {
                return false;
            }

            public Iterator<Widget> iterator() {
                return new ArrayList<Widget>(0).iterator();
            }
        }

        class Cell extends SimplePanel implements Container {

            private final Element cell = DOM.createDiv();

            Cell(String text) {
                this(new Label(text));
            }

            Cell(Widget w) {
                super();
                setStyleName(CLASSNAME + "-cell-wrap");

                DOM.setElementProperty(cell, "className", CLASSNAME + "-cell");
                DOM.appendChild(getElement(), cell);

                setWidget(w);
            }

            public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
                if (oldComponent != getWidget()) {
                    return;
                }
                setWidget(newComponent);
            }

            public boolean hasChildComponent(Widget component) {
                return component != null && getWidget() == component;
            }

            public void updateCaption(Paintable component, UIDL uidl) {
                //do nothing
            }

            public boolean requestLayout(Set<Paintable> children) {
                return false;
            }

            public RenderSpace getAllocatedSpace(Widget child) {
                return null;
            }

            public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
                //do nothing
            }

            public Element getContainerElement() {
                return cell;
            }

            public int getWidth() {
                return DOM.getElementPropertyInt(getElement(), "offsetWidth");
            }

            public void setWidth(int w) {
                DOM.setStyleAttribute(getElement(), "width", w + "px");
            }

            public int getHeight() {
                return DOM.getElementPropertyInt(getElement(), "offsetHeight");
            }
        }
    }

    private interface Group
    {
        boolean isExpanded();

        Vector<Widget> getChildren();

        boolean hasChildren();

        class CrossSign extends Widget
        {
            private boolean expanded;

            CrossSign() {
                setElement(DOM.createDiv());
                setStyleName(CLASSNAME + "-cell-cross");
            }

            void setExpanded(boolean b) {
                if (expanded != b) {
                    if (b) {
                        addStyleName(CLASSNAME_ROW_EXPANDED);
                    } else {
                        removeStyleName(CLASSNAME_ROW_EXPANDED);
                    }
                    expanded = b;
                }
            }
        }
    }
}