/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2009 18:21:02
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.Console;
import com.itmill.toolkit.terminal.gwt.client.ApplicationConnection;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.haulmont.cuba.toolkit.gwt.client.Tools;

import java.util.Vector;
import java.util.Iterator;

public class IPageTable extends Table implements Pager.PageChangeListener, ScrollListener {

    protected IPager pager;

    private static Console log = ApplicationConnection.getConsole();

    public IPageTable() {
        bodyContainer.addScrollListener(this);
    }

    protected ITableBody createBody() {
        return new IPageTableBody();
    }

    protected TableHead createHead() {
        return new TableHead();
    }

    protected boolean updateImmediate() {
        return true;
    }

    @Override
    protected void updateFromUIDL(UIDL uidl) {
        super.updateFromUIDL(uidl);

        if (pager == null) {
            pager = new IPager("", "", "", "");
            DOM.insertChild(getElement(), pager.getElement(), 0);
            adopt(pager);
        }
        pager.updateFromUIDL(uidl);

        purgeUnregistryBag();
    }

    protected void updateBody(UIDL uidl) {
        UIDL rowData = null;
        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL c = (UIDL) it.next();
            if (c.getTag().equals("rows")) {
                rowData = c;
                break;
            }
        }

        if (!recalcWidths && initializedAndAttached) {
            ((IPageTableBody) tBody).renderRows(rowData);
        } else {
            if (tBody != null) {
                tBody.removeFromParent();
                lazyUnregistryBag.add(tBody);
            }
            tBody = createBody();
            ((IPageTableBody) tBody).renderInitialRows(rowData);
            bodyContainer.add(tBody);
            initialContentReceived = true;
            if (isAttached()) {
                sizeInit();
            }
        }
    }

    public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
        // fix headers horizontal scrolling
        tHead.setHorizontalScrollPosition(scrollLeft);
    }

    @Override
    protected void setContainerHeight() {
        if (height != null && !"".equals(height)) {
            int contentH = getOffsetHeight() - tHead.getOffsetHeight();
            if (pager != null) {
                contentH -= pager.getOffsetHeight();
            }
            contentH -= getContentAreaBorderHeight();
            if (contentH < 0) {
                contentH = 0;
            }
            bodyContainer.setHeight(contentH + "px");
        }
    }

    protected class IPageTableBody extends ITableBody {

        protected Element sizer = DOM.createDiv();

        protected IPageTableBody() {
            super();
            DOM.setStyleAttribute(sizer, "height", "0");
            DOM.appendChild(getElement(), sizer);

            DOM.setElementProperty(table, "className", CLASSNAME + "-table");

            DOM.appendChild(table, tBody);
            DOM.appendChild(container, sizer);
            DOM.appendChild(container, table);
        }

        public int getAvailableWidth() {
            return DOM.getElementPropertyInt(sizer, "offsetWidth");
        }

        public void renderRows(UIDL uidl) {
            aligns = tHead.getColumnAlignments();
            clear();
            Iterator it = uidl.getChildIterator();
            while (it.hasNext()) {
                final ITableRow row = createRow((UIDL) it.next());
                addRow(row);
            }
        }

        public void renderInitialRows(UIDL uidl) {
            aligns = tHead.getColumnAlignments();
            Iterator it = uidl.getChildIterator();
            while (it.hasNext()) {
                final ITableRow row = createRowInstance((UIDL) it.next());
                addRow(row);
            }
        }

        public void clear() {
            final  Vector v = new Vector(renderedRows);
            for (final Iterator it = v.iterator(); it.hasNext();) {
                final Widget w = (Widget) it.next();
                remove(w);
            }
        }

        @Override
        public boolean remove(Widget w) {
            if (renderedRows.contains(w)) {
                lazyUnregistryBag.add(w);
                renderedRows.remove(w);
                orphan(w);
                DOM.removeChild(DOM.getParent(w.getElement()), w.getElement());
                return true;
            }
            return false;
        }

        @Override
        public void setContainerHeight() {
            if (!allowMultiStingCells) {
                containerHeight = pageLength * getRowHeight();
            } else {
                containerHeight = 0;
                for (final Object o : renderedRows) {
                    final ITableRow row = (ITableRow) o;
                    containerHeight += row.getHeight();
                }
            }
            DOM.setStyleAttribute(container, "height", containerHeight + "px");
        }

        protected ITableRow createRow(UIDL uidl) {
            final ITableRow row = new ITableRow(uidl, aligns);
            final int cells = DOM.getChildCount(row.getElement());
            for (int i = 0; i < cells; i++) {
                final Element cell = DOM.getChild(row.getElement(), i);
                final int w = IPageTable.this
                        .getColWidth(getColKeyByIndex(i));
                DOM.setStyleAttribute(DOM.getFirstChild(cell), "width",
                        (w - CELL_CONTENT_PADDING) + "px");
                DOM.setStyleAttribute(cell, "width", w + "px");
            }
            return row;
        }

        protected ITableRow createRowInstance(UIDL uidl) {
            return new ITableRow(uidl, aligns);
        }

        protected void addRow(ITableRow row) {
            ITableRow last = null;
            if (renderedRows.size() > 0) {
                last = (ITableRow) renderedRows
                        .get(renderedRows.size() - 1);
            }
            if (last != null && last.getStyleName().indexOf("-odd") == -1) {
                applyAlternatingRowColor(row, "-row-odd");
            } else {
                applyAlternatingRowColor(row, "-row");
            }
            if (row.isSelected()) {
                row.addStyleName("i-selected");
            }
            DOM.appendChild(tBody, row.getElement());
            adopt(row);
            renderedRows.add(row);
        }

        protected void applyAlternatingRowColor(ITableRow row, String style) {
            row.addStyleName(CLASSNAME + style);
        }

    }

    public void onFirstPage() {
        client.updateVariable(paintableId, "curpage", 1, true);
    }

    public void onPrevPage() {
        if (pager.getCurrentPage() > 1) {
            client.updateVariable(paintableId, "curpage", pager.getCurrentPage() - 1, true);
        }
    }

    public void onNextPage() {
        if (pager.getCurrentPage() < pager.getPagesCount()) {
            client.updateVariable(paintableId, "curpage", pager.getCurrentPage() + 1, true);
        }
    }

    public void onLastPage() {
        client.updateVariable(paintableId, "curpage", pager.getPagesCount(), true);
    }

    public void onPage(int page) {
        if (page != pager.getCurrentPage()
                && page > 0
                && page < pager.getPagesCount())
        {
            client.updateVariable(paintableId, "curpage", page, true);
        }
    }

    protected class IPager extends Pager implements HasWidgets {
        private static final int SHOWED_PAGES_COUNT = 3;

        private int pagesCount = -1;
        private int currentPage = -1;

        private final Panel pagerRoot = new FlowPanel();
        private final Panel pagesContainer = new FlowPanel();

        private final Vector pages = new Vector();

        private Label prev;
        private Label next;

        private TablePageLengthEditor editor;

        public static final String CLASSNAME = "i-pager";

        public IPager(String prevCaption, String nextCaption, String firstCaption, String lastCaption) {
            pagerRoot.setStyleName(CLASSNAME);

            prev = new Label(isEmpty(prevCaption) ? "<<Prev" : prevCaption);
            next = new Label(isEmpty(nextCaption) ? "Next>>" : nextCaption);

            pagerRoot.add(prev);
            pagerRoot.add(pagesContainer);
            pagerRoot.add(next);

            prev.addClickListener(this);
            prev.setStyleName(CLASSNAME + "-link");
            next.addClickListener(this);
            next.setStyleName(CLASSNAME + "-link");

            updatePages();

            addPageChangeListener(IPageTable.this);

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

            if (uidl.hasAttribute("style")) {
                setStyle(uidl.getStringAttribute("style"));
            }

            log.log("[Pager] current page:" + String.valueOf(currentPage));

            for (Iterator it = uidl.getChildIterator(); it.hasNext();) {
                final UIDL data = (UIDL) it.next();
                if ("paging".equals(data.getTag())) {
                    updatePaging(data);

                    if (data.hasAttribute("lengths")) {
                        if (editor == null) {
                            editor = new TablePageLengthEditor();
                            pagerRoot.add(editor);
                        }
                        editor.updateFromUIDL(uidl);
                    } else if (editor != null) {
                        pagerRoot.remove(editor);
                        editor = null;
                    }
                    break;
                }
            }

            updatePages();
        }

        protected void updatePaging(UIDL uidl) {
            if (uidl.hasAttribute("pc")) {
                prev.setText(uidl.getStringAttribute("pc"));
            }
            if (uidl.hasAttribute("nc")) {
                next.setText(uidl.getStringAttribute("nc"));
            }
        }

        protected void setStyle(String style) {
            StringBuffer styleBuf = new StringBuffer(CLASSNAME);

            final String[] styles = style.split(" ");
            for (int i = 0; i < styles.length; i++) {
                styleBuf.append(" ");
                styleBuf.append(CLASSNAME);
                styleBuf.append("-");
                styleBuf.append(styles[i]);
            }

            DOM.setElementProperty(getElement(), "className", styleBuf.toString());
        }

        protected void updatePages() {
            clear();
            updateVisibility();
            IPageTable.this.setContainerHeight();
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
                page = new Label(s);
                page.setStyleName(CLASSNAME + "-link");
            } else {
                page = new Label(s);
                page.setStyleName(CLASSNAME + "-page");
            }
            return page;
        }

        public void add(Widget w) {
            pagesContainer.add(w);
            pages.add(w);
            if (w instanceof Label) {
                ((Label) w).addClickListener(this);
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
                if (w instanceof Label) {
                    ((Label) w).removeClickListener(this);
                }
                return true;
            }
            return false;
        }

        public void onClick(Widget sender) {
            if (sender != null
                    && sender instanceof Label
                    && pageChangeListeners != null)
            {
                log.log("[Pager] Page " + ((Label) sender).getText() + " has been clicked");
                if (sender == pages.firstElement()) {
                    pageChangeListeners.fireFirstPage();
                } else if (sender == prev) {
                    pageChangeListeners.firePrevPage();
                } else if (sender == next) {
                    pageChangeListeners.fireNextPage();
                } else if (sender == pages.lastElement()) {
                    pageChangeListeners.fireLastPage();
                } else {
                    final Label link = (Label) sender;
                    final int page = Integer.parseInt(link.getText());
                    if (page < 1 || page > pagesCount) {
                        throw new IllegalStateException("Illegal page number");
                    }
                    pageChangeListeners.firePage(page);
                }
            }
        }
    }

    class TablePageLengthEditor
            extends Composite implements ChangeListener
    {
        public static final String CLASSNAME = "i-pager-editor";

        private final ListBox select = new ListBox(false);
        private final Element captionContainer = DOM.createSpan();

        public TablePageLengthEditor() {
            final Panel rootPanel = new FlowPanel();
            DOM.setElementProperty(rootPanel.getElement(), "className", CLASSNAME);
            DOM.setElementProperty(captionContainer, "className", CLASSNAME + "-caption");

            rootPanel.add(select);

            select.addChangeListener(this);

            initWidget(rootPanel);

            DOM.insertChild(getElement(), captionContainer, 0);
        }

        public void updateFromUIDL(UIDL uidl) {
            int pageLength = uidl.getIntAttribute("pagelength");

            for (Iterator it = uidl.getChildIterator(); it.hasNext();) {
                final UIDL data = (UIDL) it.next();
                if (data.getTag().equals("paging")) {
                    select.clear();
                    boolean pageLengthUsed = false;
                    int[] lengths = data.getIntArrayAttribute("lengths");
                    int selectedIndex = 0;
                    for (int i = 0; i < lengths.length; i++) {
                        if (pageLength == lengths[i]) {
                            pageLengthUsed = true;
                            selectedIndex = i;
                        } else if (pageLength < lengths[i] && !pageLengthUsed) {
                            select.addItem(String.valueOf(pageLength));
                            pageLengthUsed = true;
                            selectedIndex = i;
                        }
                        select.addItem(String.valueOf(lengths[i]));
                    }
                    select.setSelectedIndex(selectedIndex);

                    if (data.hasAttribute("sc")) {
                        captionContainer.setInnerText(data.getStringAttribute("sc"));
                    }
                    
                    break;
                }
            }
        }

        public void onChange(Widget sender) {
            if (sender != null && sender.equals(select)) {
                client.updateVariable(paintableId, "pagelength", Integer.parseInt(select.getValue(select.getSelectedIndex())),
                        true);
            }
        }
    }


    private static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

}
