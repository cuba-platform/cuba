package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.toolkit.gwt.client.Tools;
import com.itmill.toolkit.terminal.gwt.client.*;
import com.itmill.toolkit.terminal.gwt.client.ui.Action;
import com.itmill.toolkit.terminal.gwt.client.ui.ActionOwner;
import com.itmill.toolkit.terminal.gwt.client.ui.IContextMenu;
import com.itmill.toolkit.terminal.gwt.client.ui.Table;

import java.util.*;

public class ITreeTable
        extends Composite
        implements Table,
        Paintable,
        ClickListener,
        ContainerResizedListener
{
    public static final String CLASSNAME = "i-tree-table";
    public static final String CLASSNAME_ROW_SELECTED = "i-selected";
    public static final String CLASSNAME_ROW_EXPANDED = "i-expanded";

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

    private final Panel panel = new FlowPanel();

    private final TableHeader tableHeader = new TableHeader();

    private final FlowPanel tablePanel = new FlowPanel();

    private final ScrollPanel bodyContainer = new ScrollPanel();
    private final TableBody tableBody = new TableBody();

    private String height = null;
    private String width = null;

    private Set<String> visibleColumns = new HashSet<String>(); //Contains visible columns ids. They need for a header and a body rendering

    private boolean emitClickEvents;

    private static Console log = ApplicationConnection.getConsole();

    private int calculatedCrossSignWidth = 0;

    public ITreeTable() {
        tablePanel.setStyleName(CLASSNAME);
        tablePanel.add(tableHeader);
        tablePanel.add(bodyContainer);

        panel.add(tablePanel);

        bodyContainer.setStyleName(CLASSNAME + "-body");
        DOM.setStyleAttribute(bodyContainer.getElement(), "overflow", "auto");
        DOM.setStyleAttribute(bodyContainer.getElement(), "width", "100%");

        bodyContainer.add(tableBody);

        initWidget(panel);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        uidlId = uidl.getId();

        if (uidl.hasVariable("selected")) {
            final Set selectedKeys = uidl
                    .getStringArrayVariableAsSet("selected");
            selectedRowKeys.clear();
            for (final Object o : selectedKeys) {
                selectedRowKeys.add((String) o);
            }
        }

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
            width = uidl.getStringAttribute("width");
        }
        if (uidl.hasAttribute("height")) {
            height = uidl.getStringAttribute("height");
        }

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

    public void add(Widget w) {
        // TODO Auto-generated method stub

    }

    public void clear() {
        // TODO Auto-generated method stub

    }

    public Iterator<Widget> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean remove(Widget w) {
        // TODO Auto-generated method stub
        return false;
    }

    private void sizeInit()
    {
        Iterator headerCells = tableHeader.iterator();
        final int[] widths = new int[tableHeader.visibleCells.size()];

        int totalWidth = 0;
        int i = 0;

        if (width == null) {
            // if this is a re-init, remove old manually fixed size
            bodyContainer.setWidth("");
            tableHeader.setWidth("");
            tablePanel.setWidth("");
        }

        while (headerCells.hasNext())
        {
            final TableHeader.Cell cell =
                    (TableHeader.Cell) headerCells.next();

            if (cell.getPredefinedWidth() == -1) {
                cell.setWidth(-1); //reset column width
            }

            final int hw = cell.getOffsetWidth();
            log.log("[sizeInit] Header column " + i + " offsetwidth " + hw + "px");

            final int bw = tableBody.getColWidth(i) + (i == 0 ? tableBody.getDeep() * calculatedCrossSignWidth : 0);
            log.log("[sizeInit] Body column " + i + " width " + bw + "px");

            int w = (hw > bw ? hw : bw);

            totalWidth += w;
            widths[i++] = w;
        }

        if (height == null) {
//            bodyContainer.setHeight((tableBody.getRowHeight() * pageLength) + "px");
        } else {
            setInternalHeight(height);
        }

        if (width == null) {
            int w = totalWidth;
            w += getScrollbarWidth();
            tableHeader.setWidth(w + "px");
            tablePanel.setWidth(w + "px");
        } else {
            if (width.indexOf("px") > 0) {
                tableHeader.setWidth(width);
                tablePanel.setWidth(width);
            } else if (width.indexOf("%") > 0) {
                if (!width.equals("100%")) {
                    tablePanel.setWidth(width);
                }
                // contained blocks are relatively to container element
                tableHeader.setWidth("100%");
            }
        }

        int availableWidth = availableWidth();
        log.log("[sizeInit] Available width = " + availableWidth + "px");

        final int extraWidth = availableWidth - totalWidth;

        headerCells = tableHeader.iterator();
        i = 0;
        while (headerCells.hasNext()) {
            final TableHeader.Cell cell = (TableHeader.Cell) headerCells.next();
            if (cell.getWidth() == -1) {
                int w = widths[i];
                w += (extraWidth * w / totalWidth);
                widths[i] = w;
                log.log("[sizeInit] " + i + " column new width is " + w + "px");
            }
            i++;
        }

        //todo мин ширина колонки 50пкс
        //todo разрулить вариант когда ширина таблицы больше свободного места


        // last loop: set possibly modified values or reset if new tBody
        i = 0;
        headerCells = tableHeader.iterator();
        while (headerCells.hasNext()) {
            final TableHeader.Cell cell = (TableHeader.Cell) headerCells.next();
            log.log("[sizeInit] " + i + " header column width defined equal " + cell.getWidth() + "px");
            if (cell.getWidth() == -1) {
                cell.setWidth(widths[i]);
            }
            tableBody.updatePaddings();
            tableBody.setColWidth(i, widths[i]);
            log.log("[sizeInit] " + i + " column width sets " + widths[i] + "px");
            i++;
        }
    }

    public void setInternalHeight(String height) {
        int totalHeight;
        int availableHeight;

        log.log("[setInternalHeight] Primary Height = " + height);

        if (height.equals("100%")) {
            final int borders = getBorderHeight();
            final Element parentElem = DOM.getParent(getElement());

            // put table away from flow for a moment
            DOM.setStyleAttribute(getElement(), "position", "absolute");
            // get containers natural space for table
            availableHeight = DOM.getElementPropertyInt(parentElem,
                    "offsetHeight");

            // put table back to flow
            DOM.setStyleAttribute(getElement(), "position", "static");
            // set 100% height with borders

            availableHeight = (availableHeight - borders);
        }
        else {
            int h = Tools.parseSize(height);
            availableHeight = DOM.getElementPropertyInt(getElement(), "offsetHeight");
            if (availableHeight > h) {
                availableHeight = h;
            }
        }

        log.log("[setInternalHeight] Available Height = " + availableHeight);

        int headerHeight = DOM.getElementPropertyInt(
                tableHeader.getElement(), "offsetHeight");

        log.log("[setInternalHeight] Header Height = " + headerHeight);

        totalHeight = availableHeight - headerHeight;
        if (totalHeight < 0) {
            totalHeight = MIN_HEIGHT;
        }

        log.log("[setInternalHeight] Total Height = " + totalHeight);

        height = totalHeight + "px";

        bodyContainer.setHeight(height);
    }

    private int availableWidth() {
        int w;
        if (width != null) {
            int extra = getScrollbarWidth();
            if (extra == 0) {
                extra = tableHeader.getColumnsSelectorWidth();
            }
            w = Tools.parseSize(width) - extra;
        } else {
            w = tableBody.availableWidth();
            if (BrowserInfo.get().isIE()) {
                // Hey IE, are you really sure about this?
                w = tableBody.availableWidth();
            }
        }
        return w;
    }

    public void iLayout() {
        if (height != null) {
            setInternalHeight(height);
        }
    }

    private int getBorderHeight() {
        final Element el = tablePanel.getElement();
        return DOM.getElementPropertyInt(el, "offsetHeight")
                - DOM.getElementPropertyInt(el, "clientHeight");
    }

    public int getScrollbarWidth() {
        return bodyContainer.getOffsetWidth()
                - DOM.getElementPropertyInt(bodyContainer.getElement(),
                        "clientWidth");
    }

    public void onFirstPage() {
        client.updateVariable(uidlId, "curpage", 1, true);
    }

    class TableHeader extends Panel implements ActionOwner {

        private final Element headerBody = DOM.createDiv();
        private final Element columnsSelector = DOM.createDiv();

        private final Map<String, Widget> availableCells = new HashMap<String, Widget>();
        private final Vector<Widget> visibleCells = new Vector<Widget>();

        private VisibleColumnsMenu columnsMenu = null;

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

            visibleColumns.clear();

            while (it.hasNext()) {
                final UIDL col = (UIDL) it.next();
                if (!col.hasAttribute("collapsed")) {
                    final String cid = col.getStringAttribute("cid");
                    final String caption = col.getStringAttribute("caption");

                    Cell c = (Cell) availableCells.get(cid);
                    if (c == null) {
                        c = new Cell(cid, caption);
                        availableCells.put(cid, c);
                        addCell(c);
                    } else {
                        c.setCaption(caption);
                    }

                    visibleColumns.add(c.getCid());
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

        public Action[] getActions() {
            final Action[] actions = new Action[availableCells.size() + 1];

            final Iterator it = availableCells.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                final Cell cell = (Cell) it.next();
                final Action a = new VisibleColumnAction(cell.getCid(), false);
                a.setCaption(cell.getCaption());

                actions[i++] = a;
            }
            actions[i] = new ApplyVisibleColumnsAction("Apply");

            return actions;
        }

        public ApplicationConnection getClient() {
            return client;
        }

        public String getPaintableId() {
            return uidlId;
        }

        class VisibleColumnAction
                extends Action
        {
            private String cid;
            private boolean collapsed;

            VisibleColumnAction(String cid, boolean collapsed) {
                super(TableHeader.this);
                this.cid = cid;
                this.collapsed = collapsed;
            }

            public void execute() {
                Window.alert("Column " + cid + " click");
            }

            public String getCid() {
                return cid;
            }

            public boolean isCollapsed() {
                return collapsed;
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

        class ApplyVisibleColumnsAction
                extends Action
        {
            ApplyVisibleColumnsAction(String caption) {
                super(TableHeader.this);
                setCaption(caption);
            }

            public void execute() {
                getColumnsMenu().hide();
                Window.alert("Apply clicked");
            }

            public String getHTML() {
                return "<button class=\""
                        + CLASSNAME
                        + "-columns-apply\">"
                        + getCaption()
                        + "</button>";
            }
        }

        class VisibleColumnsMenu extends IContextMenu {
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

            private int predefinedWidth = -1;

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

            // a width predefined by a user
            public int getPredefinedWidth() {
                return predefinedWidth;
            }

            public void setPredefinedWidth(int predefinedWidth) {
                this.predefinedWidth = predefinedWidth;
                setWidth(predefinedWidth);
            }

            private void setWidth(int w) {
                DOM.setStyleAttribute(captionContainer, "overflow", "");
                if (w == -1) {
                    DOM.setStyleAttribute(captionContainer, "width", "");
                    super.setWidth("");
                } else {
                    DOM.setStyleAttribute(captionContainer, "width", (w
                            - DRAG_WIDGET_WIDTH)
                            + "px");
                    super.setWidth(w + "px");
                }
            }

            public int getWidth() {
                if (predefinedWidth > 0) {
                    return predefinedWidth;
                }
                return -1;
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
            private boolean selected = false;

            protected final Vector<Widget> visibleCells = new Vector<Widget>();

            Row(String key, int level) {
                super(key, level);
            }

            public boolean isSelected() {
                return selected;
            }

            private void toggleSelection() {
                selected = !selected;
                if (selected) {
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
                            log.log("Row: " + getKey() + " is " + (selected ? " selected" : " deselected"));
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