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

    private boolean immediate = false;

    private int selectMode = Table.SELECT_MODE_NONE;

    private final Vector<String> selectedRowKeys = new Vector<String>();

    private boolean sortAscending = true;

    private final HashMap rowKeysToTableRows = new HashMap();

    private final Panel panel = new FlowPanel();

    private final TableHeader tableHeader = new TableHeader();

    private final FlowPanel tablePanel = new FlowPanel();

    private final ScrollPanel bodyContainer = new ScrollPanel();
    private final TableBodiesContainer tableBody = new TableBodiesContainer();

    private String height = null;
    private String width = null;

    private Set<String> visibleColumns = new HashSet<String>(); //Contains visible columns ids. They need for a header and a body rendering

    private static Console log = ApplicationConnection.getConsole();

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

        immediate = uidl.getBooleanAttribute("immediate");

        if (uidl.hasAttribute("selectmode")) {
            if ("multi".equals(uidl.getStringAttribute("selectmode"))) {
                selectMode = Table.SELECT_MODE_MULTI;
            } else {
                selectMode = Table.SELECT_MODE_SINGLE;
            }

            if (uidl.hasAttribute("selected")) {
                final Set<String> selectedKeys = (Set<String>) uidl
                        .getStringArrayVariableAsSet("selected");
                selectedRowKeys.clear();
                for (String selectedKey : selectedKeys) {
                    selectedRowKeys.add(selectedKey);
                }
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
            if ("tbodies".equals(data.getTag())) {
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
        // TODO Auto-generated method stub

    }

    private void updateBodyFromUIDL(UIDL uidl) {
        if (uidl != null) {
            tableBody.updateFromUIDL(uidl);
        }
    }

    public void onClick(Widget sender) {
        log.log("click");
        if (sender instanceof TableBody.CrossSign) {
            log.log(sender.getParent().getClass().getName());
            log.log(sender.getParent().getParent().getParent().getClass().getName());
            TableBody.GroupRow row = ((TableBody.CrossSign) sender).getRow();
            if (row.isExpanded()) {
                client.updateVariable(uidlId, "collapse", row.getKey(), true);
            } else {
                client.updateVariable(uidlId, "expand", row.getKey(), true);
            }
        }
    }

    public void deselectAll() {
        //todo
//        final Object[] keys = selectedRowKeys.toArray();
//        for (int i = 0; i < keys.length; i++) {
//            final TableRow tableRow = (TableRow) rowKeysToTableRows
//                    .get(keys[i]);
//            if (tableRow != null) {
//                tableRow.setSelected(false);
//            }
//        }
        // still ensure all selects are removed from
        selectedRowKeys.clear();
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

            final int bw = tableBody.getColWidth(i);
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
/*
            if (Util.isIE()) {
                if (availPixels == 0) {
                    // In complex layouts IE sometimes rather randomly returns 0
                    // although container really has height. Use old value if
                    // one exits.
                    //todo what shall i must to do with it?
                    if (oldAvailPixels > 0) {
                        availPixels = oldAvailPixels;
                    }
                } else {
                    oldAvailPixels = availPixels;
                }
            }
*/
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
        //todo make a review for this logic
        int w;
        if (width != null) {
            int extra = getScrollbarWidth();
            if (extra == 0) {
                extra = tableHeader.getColumnsSelectorWidth();
            }
            w = Tools.parseSize(width) - extra;
        } else {
            w = tableBody.availableWidth();
            if (Util.isIE()) {
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

            visibleColumns.clear(); //clear visible columns

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
            if (DOM.compare(DOM.eventGetTarget(event), columnsSelector)) {
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
                if (!caption.equals(newCaption)) {
                    this.caption = newCaption;
                    DOM.setInnerHTML(captionContainer,
                            "<span class=\""
                                    + CLASSNAME
                                    + "-caption\">"
                                    + caption + "</span>");
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
                //todo
            }
        }
    }

    class TableBodiesContainer extends Panel {
        private final Element sizer = DOM.createDiv();

        private final Map<String, Widget> availableBodies = new HashMap<String, Widget>();
        private final Vector<Widget> tableBodies = new Vector<Widget>();

        TableBodiesContainer() {
            setElement(DOM.createDiv());

            DOM.setElementProperty(sizer, "className", CLASSNAME + "-body-sizer");
            DOM.appendChild(getElement(), sizer);
        }

        public void updateFromUIDL(UIDL uidl)
        {
            Iterator it = uidl.getChildIterator();
            while (it.hasNext()) {
                final UIDL bodyUidl = (UIDL) it.next();
                if ("tbody".equals(bodyUidl.getTag()))
                {
                    final String key = bodyUidl.getStringAttribute("key");
                    TableBody tBody = (TableBody) availableBodies.get(key);
                    if (tBody == null) {
                        tBody = new TableBody(key);
                        addTableBody(tBody);
                        log.log("Created tbody with key: " + key);
                    }
                    tBody.updateBodyFromUIDL(bodyUidl);
                    log.log("Table body has been updated");
                }
            }
        }

        private void addTableBody(TableBody tBody) {
            DOM.appendChild(getElement(), tBody.getElement());
            adopt(tBody);
            tableBodies.add(tBody);
            availableBodies.put(tBody.getKey(), tBody);
        }

        public int availableWidth() {
            return DOM.getElementPropertyInt(sizer, "offsetWidth");
        }

        public int getColWidth(int col) {
            if (tableBodies.isEmpty()) return 0;
            return ((TableBody) tableBodies.get(0)).getColWidth(col);
        }

        public void setColWidth(int colIndex, int w) {
            for (final Widget tBody : tableBodies) {
                ((TableBody) tBody).setColWidth(colIndex, w);
            }
        }

        public int getRowHeight() {
            if (tableBodies.isEmpty()) return 0;
            return ((TableBody) tableBodies.get(0)).getRowHeight();
        }

        public int getHeight() {
            return 0; //
        }

        public boolean remove(Widget child) {
            if (tableBodies.contains(child)) {
                log.log("Remove table body");
                tableBodies.remove(child);
                orphan(child);
                DOM.removeChild(DOM.getParent(child.getElement()), child.getElement());
                return true;
            }
            return false;
        }

        public Iterator<Widget> iterator() {
            return tableBodies.iterator();
        }
    }

    class TableBody extends Panel {

        private String key;
        private String caption;
        private String icon;

        private final Vector<Widget> rows = new Vector<Widget>();

        private Element captionContainer = null;
        private final RowsContainer bodyContent = new RowsContainer();

        TableBody(String key) {
            this.key = key;
            setElement(DOM.createDiv());
            bodyContent.setStyleName(CLASSNAME + "-content");
            DOM.appendChild(getElement(), bodyContent.getElement());
            adopt(bodyContent);
        }

        public String getKey() {
            return key;
        }

        void updateBodyFromUIDL(UIDL uidl) {
            caption = uidl.getStringAttribute("caption");

            if (caption != null) {
                if (captionContainer == null) {
                    Element captionWrapper = DOM.createDiv();
                    DOM.setElementProperty(captionWrapper, "className", CLASSNAME + "-body-caption");
                    captionContainer = DOM.createDiv();
                    DOM.appendChild(captionWrapper, captionContainer);
                    DOM.insertChild(getElement(), captionWrapper, 0);
                }
                DOM.setInnerHTML(captionContainer, caption);
            }

            clear(); //todo think about a caching the rows list

            bodyContent.updateFromUIDL(uidl);
        }

        private Cell getCell(int row, int col) {
            if (row < 0 || row >= rows.size()) {
                throw new IndexOutOfBoundsException();
            }
            return ((Row) rows.get(row)).getCell(col);
        }

        int getColWidth(int col) {
            return getCell(0, col).getWidth();
        }

        void setColWidth(int col, int w) {
            for (int i = 0; i < rows.size(); i++) {
                getCell(i, col).setWidth(w);
            }
        }

        int getRowHeight() {
            int rowHeight = getCell(0, 0).getHeight();
            if (rowHeight > 0) {
                return rowHeight;
            }
            return DEFAULT_ROW_HEIGHT;
        }

        public boolean remove(Widget w) {
            if (rows.contains(w)) {
                bodyContent.removeRow((Row) w);
            }
            return false;
        }

        public Iterator<Widget> iterator() {
            return rows.iterator();
        }

        public void clear() {
            final  Vector<Widget> v = new Vector<Widget>(rows);
            for (final Widget w : v) {
                remove(w);
            }
        }

        class RowsContainer
                extends FlowPanel
        {
            public void updateFromUIDL(UIDL uidl) {
                Iterator it = uidl.getChildIterator();
                while (it.hasNext()) {
                    final UIDL row = (UIDL) it.next();
                    if ("gr".equals(row.getTag())
                            || "tr".equals(row.getTag()))
                    {
                        final String key = row.getStringAttribute("key");
                        Row r;
                        if ("gr".equals(row.getTag())) {
                            r = new GroupRow(key,
                                    row.getBooleanAttribute("selected"),
                                    row.getBooleanAttribute("expanded")
                            );
                        } else {
                            r = new Row(key, row.getBooleanAttribute("selected"));
                        }
                        addRow(r);
                        r.updateRowFromUIDL(row);
                    }
                }
            }

            private void addRow(Row r) {
                String className;
                if (rows.size() % 2 == 1) {
                    className = "-row-odd";
                } else {
                    className = "-row";
                }
                r.addStyleName(CLASSNAME + className);
                add(r);
                rows.add(r);
            }

            public void removeRow(Row r) {
                remove(r);
                rows.remove(r);
            }

        }

        class GroupRow extends Row
        {
            private final CrossSign crossSign = new CrossSign();
            private final FlowPanel container = new FlowPanel();
            private final FlowPanel cellsContainer = new FlowPanel();
            private RowsContainer childrenContainer = null;

            GroupRow(String key, boolean exp) {
                this(key, false, exp);
            }

            GroupRow(String key, boolean sel, boolean exp) {
                super(key, sel);
                getRowContainer().add(crossSign);
                getRowContainer().add(container);
                container.add(cellsContainer);
                crossSign.setRow(this);
                crossSign.setExpanded(exp);
            }

            @Override
            public void updateRowFromUIDL(UIDL uidl)
            {
                Iterator it = uidl.getChildIterator();
                while (it.hasNext()) {
                    final UIDL data = (UIDL) it.next();
                    if ("c".equals(data.getTag())) {
                        updateCells(data);
                    }
                }

                if (isExpanded())
                {
                    getChildrenContainer().updateFromUIDL(uidl);
                }
            }

            protected void updateCells(UIDL uidl) {
                Iterator cells = uidl.getChildIterator();
                visibleCells.clear();
                int index = 0;
                while (cells.hasNext()) {
                    final Cell cell = createCell(cells.next(), index++);
                    if (cell != null) {
                        addCell(cell);
                    }
                }
            }

            private RowsContainer getChildrenContainer() {
                if (childrenContainer == null) {
                    childrenContainer = new RowsContainer();
                    childrenContainer.setStyleName(CLASSNAME + "-rows");
                    container.add(childrenContainer);
                }
                return childrenContainer;
            }

            private Cell createCell(Object o, int index) {
                Cell c = null;
                if (o instanceof String) {
                    c = index == 0 ? new GroupCell((String) o) : new Cell((String) o);
                } else if (o instanceof Widget) {
                    c = index == 0 ? new GroupCell((Widget) o) : new Cell((Widget) o);
                }
                return c;
            }

            @Override
            protected FlowPanel getCellsContainer() {
                return cellsContainer;
            }

            public boolean isExpanded() {
                return crossSign.isExpanded();
            }
        }

        class Row extends Composite {
            private final FlowPanel rowContainer = new FlowPanel();
            protected final Vector<Widget> visibleCells = new Vector<Widget>();
            private final String key;

            private boolean selected = false;

            Row(String key) {
                this(key, false);
            }

            Row(String key, boolean selected) {
                initWidget(rowContainer);

                this.key = key;
                setSelected(selected);
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
                if (selected) {
                    selectedRowKeys.add(key);
                    addStyleName(CLASSNAME_ROW_SELECTED);
                } else {
                    selectedRowKeys.remove(key);
                    removeStyleName(CLASSNAME_ROW_SELECTED);
                }
            }

            public void updateRowFromUIDL(UIDL uidl) {
                Iterator cells = uidl.getChildIterator();
                visibleCells.clear();
                while (cells.hasNext()) {
                    final Object c = cells.next();
                    Cell cell = null;
                    if (c instanceof String) {
                        cell = new Cell((String) c);
                    } else if (c instanceof Widget) {
                        cell = new Cell((Widget) c);
                    }
                    if (cell != null) {
                        addCell(cell);
                    }
                }
            }

            protected void addCell(Cell c) {
                getCellsContainer().add(c);
                visibleCells.add(c);
            }

            protected boolean removeCell(Cell c) {
                if (visibleCells.contains(c)) {
                    visibleCells.remove(c);
                    getCellsContainer().remove(c);
                    return true;
                }
                return false;
            }

            public Cell getCell(int index) {
                if (index < 0 || index >= visibleCells.size()) {
                    throw new IndexOutOfBoundsException();
                }
                return (Cell) visibleCells.get(index);
            }

            public String getKey() {
                return key;
            }

            protected FlowPanel getRowContainer() {
                return rowContainer;
            }

            protected FlowPanel getCellsContainer() {
                return getRowContainer();
            }
        }

        class GroupCell extends Cell {
            GroupCell(String text) {
                super(text);
            }

            GroupCell(Widget w) {
                super(w);
            }

            public void setWidth(int w) {
//                GrouplRow parentRow = (GrouplRow) getParent().getParent().getParent();
//                int shift = DOM.getElementPropertyInt(parentRow.crossSign.getElement(), "offsetWidth");
                super.setWidth(w - /*shift * (parentRow.level + 1)*/19);
            }
        }

        class Cell extends SimplePanel {
            private Element content;
            Cell(String text) {
                this(new Label(text));
            }

            Cell(Widget w) {
                super();
                DOM.setElementProperty(getElement(), "className", CLASSNAME + "-cell-wrap");

                content = DOM.createDiv();
                DOM.setElementProperty(content, "className", CLASSNAME + "-cell");
                DOM.appendChild(getElement(), content);

                setWidget(w);
            }

            @Override
            protected Element getContainerElement() {
                return content;
            }

            public int getWidth() {
                return DOM.getElementPropertyInt(getElement(), "offsetWidth");
            }

            public void setWidth(int w) {
                if (w < 0) w = 0;
                DOM.setStyleAttribute(getElement(), "width", w + "px");
            }

            public int getHeight() {
                return DOM.getElementPropertyInt(getElement(), "offsetHeight");
            }
        }

        class CrossSign
                extends Widget
                implements SourcesClickEvents
        {
            private ClickListenerCollection clickListeners = null;
            private boolean expanded = false;
            private GroupRow row = null;

            CrossSign() {
                setElement(DOM.createDiv());
                setStyleName(CLASSNAME + "-cell-cross");
                addClickListener(ITreeTable.this);
            }

            public GroupRow setRow(GroupRow r) {
                return row = r;
            }

            public GroupRow getRow() {
                return row;
            }

            void setExpanded(boolean b) {
                if (isExpanded() != b) {
                    if (b) {
                        addStyleName(CLASSNAME_ROW_EXPANDED);
                    } else {
                        removeStyleName(CLASSNAME_ROW_EXPANDED);
                    }
                    expanded = b;
                }
            }

            public boolean isExpanded() {
                return expanded;
            }

            public void addClickListener(ClickListener listener) {
                if (clickListeners == null) {
                    clickListeners = new ClickListenerCollection();
                    sinkEvents(Event.ONCLICK);
                }
                clickListeners.add(listener);
            }

            public void removeClickListener(ClickListener listener) {
                if (clickListeners != null) {
                    clickListeners.remove(listener);
                }
            }

            @Override
            public void onBrowserEvent(Event event) {
                switch (event.getTypeInt()) {
                    case Event.ONCLICK:
                        if (clickListeners != null) {
                            clickListeners.fireClick(this);
                        }
                        break;
                }
            }
        }
    }
}