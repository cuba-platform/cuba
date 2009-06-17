package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.itmill.toolkit.terminal.gwt.client.*;
import com.itmill.toolkit.terminal.gwt.client.ui.Table;
import com.itmill.toolkit.terminal.gwt.client.ui.ActionOwner;
import com.itmill.toolkit.terminal.gwt.client.ui.Action;
import com.itmill.toolkit.terminal.gwt.client.ui.IContextMenu;
import com.haulmont.cuba.toolkit.gwt.client.Tools;

import java.util.*;

public class IPagingTable
        extends Composite
        implements Table,
        Paintable,
        ClickListener,
        ContainerResizedListener,
        Pager.PageChangeListener
{
    public static final String CLASSNAME = "i-page-table";
    public static final String CLASSNAME_ROW_SELECTED = "i-selected";

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

    private final Vector selectedRowKeys = new Vector();

    private int pageLength = 15;

    private boolean sortAscending = true;

    private final HashMap rowKeysToTableRows = new HashMap();

    private final Panel panel = new FlowPanel();

    private final IPager pager = new IPager(); //todo make the method setPager()

    private final TableHeader tableHeader = new TableHeader();

    private final FlowPanel tablePanel = new FlowPanel();

    private final ScrollPanel bodyContainer = new ScrollPanel();

    private final TableBody tableBody = new TableBody();

    private final Panel pageLengthEditorContainer = new SimplePanel();

    private TablePageLengthEditor pageLengthEditor = null;

    private String height = null;
    private String width = null;

    private Set visibleColumns = new HashSet(); //Contains visible columns ids. They need for a header and a body rendering 

    private static Console log = ApplicationConnection.getConsole();

    public IPagingTable() {
        panel.add(pager);
        panel.add(pageLengthEditorContainer);

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
        pageLength = uidl.getIntAttribute("pagelength");

        if (uidl.hasAttribute("pagelengtheditor")) {
            if (uidl.getBooleanAttribute("pagelengtheditor")) {
                if (pageLengthEditor == null) {
                    pageLengthEditor = new TablePageLengthEditor();
                    pageLengthEditorContainer.add(pageLengthEditor);
                    pageLengthEditorContainer.setVisible(true);
                }
            } else {
                if (pageLengthEditor != null) {
                    pageLengthEditorContainer.setVisible(false);
                }
            }
        } else {
            pageLengthEditorContainer.setVisible(false);
        }

        if (uidl.hasAttribute("selectmode")) {
            if ("multi".equals(uidl.getStringAttribute("selectmode"))) {
                selectMode = Table.SELECT_MODE_MULTI;
            } else {
                selectMode = Table.SELECT_MODE_SINGLE;
            }

            if (uidl.hasAttribute("selected")) {
                final Set selectedKeys = uidl
                        .getStringArrayVariableAsSet("selected");
                selectedRowKeys.clear();
                for (final Iterator it = selectedKeys.iterator(); it.hasNext();) {
                    //todo see this and log
                    selectedRowKeys.add(it.next());
                }
            }
        }

        if (uidl.hasVariable("sortascending")) {
            sortAscending = uidl.getBooleanVariable("sortascending");
        }

        if (uidl.hasAttribute("rowheaders")) {
            rowHeaders = true;
        }

        UIDL rowsUidl = null;
        UIDL colsUidl = null;
        UIDL actionsUidl = null;
        UIDL pagerUidl = null;
        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL data = (UIDL) it.next();
            if ("rows".equals(data.getTag())) {
                rowsUidl = data;
            } else if ("actions".equals(data.getTag())) {
                actionsUidl = data;
            } else if ("visiblecolumns".equals(data.getTag())) {
                colsUidl = data;
            } else if ("pager".equals(data.getTag())) {
                pagerUidl = data;
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
        updateBodyFromUIDL(rowsUidl);
        updatePagerFromUIDL(pagerUidl);
        updatePageLengthEditor(uidl);

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
            tableBody.updateBodyFromUIDL(uidl);
        }
    }

    private void updatePagerFromUIDL(UIDL uidl) {
        if (uidl != null) {
            pager.updateFromUIDL(uidl);
        }
    }

    private void updatePageLengthEditor(UIDL uidl) {
        if (uidl != null && pageLengthEditor != null) {
            pageLengthEditor.updateFromUIDL(uidl);
        }
    }

    public void onClick(Widget sender) {
        if (sender instanceof Button) {
            if (pageLengthEditor != null
                    && sender == pageLengthEditor.applyButton)
            {
                final String s = pageLengthEditor.input.getText();
                try {
                    int intValue = Integer.parseInt(s);
                    if (intValue > 0) {
                        client.updateVariable(uidlId, "pagelength", intValue, true);
                    } else {
                        Window.alert("Sorry, but the page length must be more than 0");
                    }
                } catch (NumberFormatException e) {
                    Window.alert("Sorry, but the page length must be numeric");
                }
            }
        }
//        if (sender instanceof HeaderCell) {
//            final HeaderCell hCell = (HeaderCell) sender;
//            client.updateVariable(uidlId, "sortcolumn", hCell.getCid(), false);
//            client.updateVariable(uidlId, "sortascending", (sortAscending ? false
//                    : true), true);
//        }
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

    public Iterator iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean remove(Widget w) {
        // TODO Auto-generated method stub
        return false;
    }

    private void sizeInit() {

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
            bodyContainer.setHeight((tableBody.getRowHeight() * pageLength) + "px");
        } else {
            setInternalHeight(height);
        }

        if (width == null) {
            int w = totalWidth;
            w += getScrollbarWidth();
            tablePanel.setWidth(w + "px");
            tableHeader.setWidth(w + "px");
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

        int pagerHeight;
        if (pager != null) {
            pagerHeight = DOM.getElementPropertyInt(
                    pager.getElement(), "offsetHeight");
        } else {
            pagerHeight = 0;
        }

        log.log("[setInternalHeight] Pager Height = " + pagerHeight);

        int headerHeight = DOM.getElementPropertyInt(
                tableHeader.getElement(), "offsetHeight");

        log.log("[setInternalHeight] Header Height = " + headerHeight);

        totalHeight = availableHeight - headerHeight - pagerHeight;
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

    public void onPrevPage() {
        if (pager.getCurrentPage() > 1) {
            client.updateVariable(uidlId, "curpage", pager.getCurrentPage() - 1, true);
        }
    }

    public void onNextPage() {
        if (pager.getCurrentPage() < pager.getPagesCount()) {
            client.updateVariable(uidlId, "curpage", pager.getCurrentPage() + 1, true);
        }
    }

    public void onLastPage() {
        client.updateVariable(uidlId, "curpage", pager.getPagesCount(), true);
    }

    public void onPage(int page) {
        if (page != pager.getCurrentPage()
                && page > 0
                && page < pager.getPagesCount())
        {
            client.updateVariable(uidlId, "curpage", page, true);
        }
    }

    class TableHeader extends Panel implements ActionOwner {

        private final Element headerBody = DOM.createDiv();
        private final Element columnsSelector = DOM.createDiv();

        private final Map availableCells = new HashMap();
        private final Vector visibleCells = new Vector();

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

        public Iterator iterator() {
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

    class TableBody extends Panel {

        private String caption = "";
        private String icon;

        private final Vector rows = new Vector();
        private final HashMap availableRows = new HashMap();

        private final Element bodyContent = DOM.createDiv();

        private final Element sizer = DOM.createDiv();

        TableBody() {
            setElement(DOM.createDiv());

            DOM.setElementProperty(sizer, "className", CLASSNAME + "-body-sizer");
            DOM.appendChild(getElement(), sizer);

            DOM.setElementProperty(bodyContent, "className", CLASSNAME + "-content");
            DOM.appendChild(getElement(), bodyContent);
        }

        TableBody(String caption, String icon) {
            this();
            this.caption = caption; //todo add body caption later
            this.icon = icon;
        }

        public int availableWidth() {
            return DOM.getElementPropertyInt(sizer, "offsetWidth");
        }
        
        public void updateBodyFromUIDL(UIDL uidl) {
            final Iterator it = uidl.getChildIterator();
//            rows.clear();
            clear(); //todo think about a caching the rows list
            int rowIndex = 0;
            while (it.hasNext()) {
                final UIDL row = (UIDL) it.next();
                final String key = String.valueOf(row.getIntAttribute("key"));
                Row r = null;//(Row) availableRows.get(key); todo
                if (r == null) {
                    r = new Row(key, row.getBooleanAttribute("selected"));
                    availableRows.put(key, r);
                    addRow(rowIndex++, r);
                }
                r.updateRowFromUIDL(row);
            }
        }

        private void addRow(int rowIndex, Row r) {
            adopt(r);
            DOM.appendChild(bodyContent, r.getElement());
            String className;
            if (rowIndex % 2 == 1) {
                className = "-row-odd";
            } else {
                className = "-row";
            }
            DOM.setElementProperty(r.getElement(), "className", CLASSNAME + className);
            rows.add(r);
            log.log("add row:" + r.key);
        }

        public int getRowHeight() {
            int rowHeight = DOM.getElementPropertyInt(getCell(0, 0), "offsetHeight");
            if (rowHeight > 0) {
                return rowHeight;
            }
            return DEFAULT_ROW_HEIGHT;
        }

        public int getColWidth(int col) {
            int colWidth = DOM.getElementPropertyInt(getCell(0, col), "offsetWidth");
            return colWidth;
        }

        private Element getCell(int row, int col) {
            final Element e = DOM.getChild(DOM.getChild(bodyContent, row), col);
            return e;
        }

        public void setColWidth(int colIndex, int w) {
            final int rows = DOM.getChildCount(bodyContent);
            for (int i = 0; i < rows; i++) {
                final Element cell = DOM.getChild(DOM.getChild(bodyContent, i),
                        colIndex);
                DOM.setStyleAttribute(cell, "width", w + "px");
            }
        }

        public boolean remove(Widget child) {
            if (rows.contains(child)) {
                log.log("remove:" + ((Row)child).key);
                rows.remove(child);
                orphan(child);
                DOM.removeChild(DOM.getParent(child.getElement()), child.getElement());
                return true;
            }
            return false;
        }

        public Iterator iterator() {
            return rows.iterator();
        }

        public void clear() {
            final  Vector v = new Vector(rows);
            for (final Iterator it = v.iterator(); it.hasNext();) {
                final Widget w = (Widget) it.next();
                remove(w);
            }
        }

        class Row extends Panel {
            private final String key;
            private boolean selected = false;

            private final Vector visibleCells = new Vector();

            Row(String key) {
                this(key, false);
            }

            Row(String key, boolean selected) {
                setElement(DOM.createDiv());

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

            private void addCell(Cell c) {
                adopt(c);
                final Element div = DOM.createDiv();
                DOM.setElementProperty(div, "className", CLASSNAME + "-cell");
                DOM.appendChild(div, c.getElement());
                DOM.appendChild(getElement(), div);
                visibleCells.add(c);
            }

            public boolean remove(Widget child) {
                if (visibleCells.contains(child)) {
                    visibleCells.remove(child);
                    orphan(child);
                    final Element td = DOM.getParent(child.getElement());
                    DOM.removeChild(DOM.getParent(td), td);
                    return true;
                }
                return false;
            }

            public Iterator iterator() {
                return visibleCells.iterator();
            }
        }

        class Cell extends SimplePanel {
            Cell(String text) {
                this(new Label(text));
            }

            Cell(Widget w) {
                super();
                DOM.setElementProperty(getElement(), "className", CLASSNAME + "-cell-content");
                setWidget(w);
            }
        }
    }

    class IPager extends Pager implements HasWidgets {
        private static final int SHOWED_PAGES_COUNT = 3;

        private int pagesCount = -1;
        private int currentPage = -1;

        private final Panel pagerRoot = new FlowPanel();
        private final Panel pagesContainer = new FlowPanel();

        private final Vector pages = new Vector();

        private Link prev = new Link("<<Prev", "Page:prev");
        private Link next = new Link("Next>>", "Page:next");

        public static final String CLASSNAME = "i-pager";

        public IPager() {
            pagerRoot.setStyleName(CLASSNAME);
            pagerRoot.add(prev);
            pagerRoot.add(pagesContainer);
            pagerRoot.add(next);

            prev.addClickListener(this);
            next.addClickListener(this);

            updatePager();

            addPageChangeListener(IPagingTable.this);

            initWidget(pagerRoot);
        }

        public void setCurrentPage(int page) {
            if (page < 1 || page > pagesCount) {
                throw new IllegalArgumentException("Illegal current page argument [" + page + "]");
            }
            currentPage = page;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setPagesCount(int pagesCount) {
            if (pagesCount < 1) {
                throw new IllegalArgumentException("Illegal page count argument [" + pagesCount + "]");
            }
            this.pagesCount = pagesCount;
        }

        public int getPagesCount() {
            return pagesCount;
        }

        public void updateFromUIDL(UIDL uidl) {
            if (uidl.hasAttribute("pagescount")) {
                pagesCount = uidl.getIntAttribute("pagescount");
            }

            log.log("[Pager] pages count:" + String.valueOf(pagesCount));

            if (uidl.hasAttribute("curpage")) {
                currentPage = uidl.getIntAttribute("curpage");
            }

            log.log("[Pager] current page:" + String.valueOf(currentPage));

            updatePager();
        }

        public void updatePager() {
            clear();
            updateVisibility();
        }

        private void updateVisibility()
        {
            if (pagesCount > 0)
            {
                if (currentPage  < 0) {
                    currentPage = 1;
                } else if (currentPage > pagesCount) {
                    currentPage = pagesCount;
                }

                if (currentPage > 1) {
                    prev.setVisible(true);
                } else {
                    prev.setVisible(false);
                }

                if (currentPage < pagesCount) {
                    next.setVisible(true);
                } else {
                    next.setVisible(false);
                }

                collectPages();

                if (!pagerRoot.isVisible()) pagerRoot.setVisible(true);
            }
            else {
                pagerRoot.setVisible(false);
            }
        }

        private void collectPages() {
            int start;
            int end;
            if (pagesCount > (SHOWED_PAGES_COUNT * 2 + 3)) {
                if (currentPage > SHOWED_PAGES_COUNT + 2) {
                    start = currentPage - SHOWED_PAGES_COUNT;
                } else {
                    start = 1;
                }

                if (currentPage < pagesCount - (SHOWED_PAGES_COUNT + 2)) {
                    end = currentPage + SHOWED_PAGES_COUNT;
                } else {
                    end = pagesCount;
                }
            } else {
                start = 1;
                end = pagesCount;
            }

            if (start != 1) {
                add(createPage(1));
                addSpacer();
            }

            collectPages(start, end);

            if (end != pagesCount) {
                addSpacer();
                add(createPage(pagesCount));
            }
        }

        private void collectPages(int start, int end) {
            for (int pageIndex = start; pageIndex <= end; pageIndex++) {
                final Widget page = createPage(pageIndex);
                add(page);
            }
        }

        private void addSpacer() {
            final Element el = DOM.createSpan();
            DOM.setInnerText(el, "...");
            DOM.setElementProperty(el, "className", CLASSNAME + "-spacer");
            DOM.appendChild(pagesContainer.getElement(), el);
        }

        private Label createPage(int pageNum) {
            final String s = String.valueOf(pageNum);
            Label page;
            if (pageNum != currentPage) {
                page = new Link(s, s);
            } else {
                page = new Label(s);
                page.setStyleName(CLASSNAME + "-page");
            }
            return page;
        }

        public void add(Widget w) {
            pagesContainer.add(w);
            pages.add(w);
            if (w instanceof Link) {
                ((Link) w).addClickListener(this);
            }
        }

        public void clear() {
            if (pages.isEmpty()) return;

            final Vector removedPages = new Vector(pages);
            final Iterator it = removedPages.iterator();
            while (it.hasNext()) {
                final Widget w = (Widget) it.next();
                remove(w);
            }

            Tools.removeChildren(pagesContainer.getElement());
        }

        public Iterator iterator() {
            return pages.iterator();
        }

        public boolean remove(Widget w) {
            if (pages.contains(w)) {
                pagesContainer.remove(w);
                pages.remove(w);
                if (w instanceof Link) {
                    ((Link) w).removeClickListener(this);
                }
                return true;
            }
            return false;
        }

        public void onClick(Widget sender) {
            if (sender != null
                    && sender instanceof Link
                    && pageChangeListeners != null)
            {
                log.log("[Pager] Page " + ((Link) sender).getHref() + " has been clicked");
                if (sender == pages.firstElement()) {
                    pageChangeListeners.fireFirstPage();
                } else if (sender == prev) {
                    pageChangeListeners.firePrevPage();
                } else if (sender == next) {
                    pageChangeListeners.fireNextPage();
                } else if (sender == pages.lastElement()) {
                    pageChangeListeners.fireLastPage();
                } else {
                    final Link link = (Link) sender;
                    final int page = Integer.parseInt(link.getHref());
                    if (page < 1 || page > pagesCount) {
                        throw new IllegalStateException("Illegal page number");
                    }
                    pageChangeListeners.firePage(page);
                }
            }
        }
    }

    class TablePageLengthEditor
            extends Composite
    {
        public static final String CLASSNAME = "i-page-length-editor";

        private final Button applyButton = new Button("Apply");

        private final TextBox input = new TextBox();

        public TablePageLengthEditor() {
            final Panel rootPanel = new FlowPanel();
            DOM.setElementProperty(rootPanel.getElement(), "className", CLASSNAME);

            rootPanel.add(applyButton);
            rootPanel.add(input);

            applyButton.addClickListener(IPagingTable.this);

            initWidget(rootPanel);
        }

        public void updateFromUIDL(UIDL uidl) {
            input.setText(String.valueOf(uidl.getIntAttribute("pagelength")));
        }
    }
}