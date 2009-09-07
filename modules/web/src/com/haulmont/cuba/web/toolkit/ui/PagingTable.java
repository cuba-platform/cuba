/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 15.12.2008 15:04:31
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.itmill.toolkit.ui.AbstractSelect;
import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.terminal.PaintException;

import java.util.*;

@Deprecated
public class PagingTable extends AbstractSelect {

    public static final String TAG_NAME = "pagingtable";

    private static final int CELL_KEY = 0;

    private static final int CELL_HEADER = 1;

    private static final int CELL_ICON = 2;

    private static final int CELL_ITEMID = 3;

    private static final int CELL_FIRSTCOL = 4;

    private final List visibleColumns = new LinkedList();
    private final Set collapsedColumns = new HashSet();

    private final Map columnHeaders = new HashMap();

    private int pageLength = 15;

    private int currentPage = 1;
    private int pagesCount = -1;

    private boolean showPageLengthEditor = false;

    private boolean columnsCollapsingAllowed = true;

    private Object[][] pageBuffer = null;

    public PagingTable(String caption, Container dataSource) {
        super(caption, dataSource);
    }

    public Object[] getAvailableColumns() {
        return getContainerPropertyIds().toArray();
    }

    public void setVisibleColumns(Object[] columns) {
        setVisibleColumns(columns, true);
    }

    private void setVisibleColumns(Object[] columns, boolean rerender) {
        if (columns == null) {
            throw new NullPointerException();
        }

        final Collection propertyIds = getContainerPropertyIds();
        for (int i = 0; i < columns.length; i++)
        {
            if (columns[i] == null) {
                throw new NullPointerException();
            }
            else if (!propertyIds.contains(columns[i])) {
                throw new IllegalArgumentException();
            }
        }

        visibleColumns.clear();

        for (int i = 0; i < columns.length; i++) {
            visibleColumns.add(columns[i]);
        }

        if (rerender) refreshRenderedCells();
    }

    public Object[] getVisibleColumns() {
        if (visibleColumns == null) {
            return new Object[0];
        }
        return visibleColumns.toArray();
    }

    protected Object[][] getVisibleCells() {
        if (pageBuffer == null) {
            refreshRenderedCells();
        }
        return pageBuffer;
    }

    public void setPageLength(int length) {
        setPageLength(length, true);
    }

    private void setPageLength(int length, boolean rerender) {
        if (pageLength > 0 && pageLength != length) {
            pageLength = length;

            if (rerender) refreshRenderedCells();
        }
    }

    public int getPageLength() {
        return pageLength;
    }

//    public int getCurrentPage() {
//        return currentPage;
//    }

    private void setCurrentPage(int currentPage, boolean rerender) {
        this.currentPage = currentPage;
        if (rerender) refreshRenderedCells();
    }

    public void setColumnHeader(Object propertyId, String header) {
        setColumnHeader(propertyId, header, true);
    }

    private void setColumnHeader(Object propertyId, String header, boolean rerender) {
        if (header == null) {
            columnHeaders.remove(propertyId);
            return;
        }
        columnHeaders.put(propertyId, header);

        if (rerender) refreshRenderedCells();
    }

    public String getColumnHeader(Object propertyId) {
        String header = (String) columnHeaders.get(propertyId);
        if (header == null) {
            header = String.valueOf(propertyId);
        }
        return header;
    }

    public boolean isShowPageLengthEditor() {
        return showPageLengthEditor;
    }

    public void setShowPageLengthEditor(boolean b) {
        if (showPageLengthEditor != b) {
            showPageLengthEditor = b;

            refreshRenderedCells();
        }
    }

    protected void refreshRenderedCells()
    {
        int rows = getContainerDataSource().size();

        pagesCount = rows % pageLength == 0
                ? rows / pageLength
                : rows / pageLength + 1;
        if (currentPage > pagesCount) currentPage = pagesCount;

        Object[] colIds = getVisibleColumns();
        int cols = colIds.length;
        int firstRow = firstRowIndex();
        if (rows > 0 && firstRow >= 0) {
            rows -= firstRow;
        }
        if (pageLength > 0 && pageLength < rows) {
            rows = pageLength;
        }

        Object[][] cells = new Object[cols + CELL_FIRSTCOL][rows];
        if (rows == 0) {
            pageBuffer = cells;
            //todo unregister all old components
            return;
        }

        int filledRows = 0;
        final Iterator idIterator = iterator();
        for (int rowIndex = 0; rowIndex < rows && idIterator.hasNext(); rowIndex++)
        {
            final Object id = idIterator.next();
            cells[CELL_ITEMID][rowIndex] = id;
            cells[CELL_KEY][rowIndex] = id;
            cells[CELL_HEADER][rowIndex] = getItemCaption(id);
            cells[CELL_ICON][rowIndex] = null; //todo

            if (cols > 0) {
                for (int colIndex = 0; colIndex < cols; colIndex++)
                {
                    Object value = "";
                    final Property p = getContainerProperty(id, colIds[colIndex]);
                    if (p != null) {
                        value = p.getValue();
                    }
                    cells[CELL_FIRSTCOL + colIndex][rowIndex] = value;
                }
            }

            filledRows++;
        }

        // Assures that all the rows of the cell-buffer are valid
        if (filledRows != cells[0].length) {
            final Object[][] temp = new Object[cells.length][filledRows];
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < filledRows; j++) {
                    temp[i][j] = cells[i][j];
                }
            }
            cells = temp;
        }

        pageBuffer = cells;

        requestRepaint();
    }

    private int firstRowIndex() {
        int rows = getContainerDataSource().size();
        return (currentPage - 1) * pageLength;
    }

    protected Iterator iterator() {
        return new IdsIterator(getContainerDataSource(), firstRowIndex());
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
//        super.paintContent(target);

        Object[] colIds = getVisibleColumns();
        int cols = colIds.length;
        Object[][] cells = getVisibleCells();
        int rows = cells[0].length;

        target.addAttribute("selectmode", "none");

        target.addAttribute("cols", cols);
        target.addAttribute("rows", rows);

        target.addAttribute("pagelength", pageLength);
        target.addAttribute("pagelengtheditor", showPageLengthEditor);

        paintRows(target, cells);

        paintColumns(target);

        paintPager(target);
    }

    private void paintRows(
            PaintTarget target,
            Object[][] cells
    ) throws PaintException {
        target.startTag("rows");

        int start = 0, end = cells[0].length;

        for (int index = start; index < end; index++)
        {
            target.startTag("tr");

            target.addAttribute("key", Integer.parseInt(cells[CELL_KEY][index]
                    .toString()));

            int currentColumn = 0;
            for (final Iterator it = visibleColumns.iterator(); it.hasNext(); currentColumn++) {
                final Object columnId = it.next();
                if (columnId == null) {
                    continue;
                }
                target.addText((String) cells[CELL_FIRSTCOL + currentColumn][index]);
            }

            target.endTag("tr");
        }

        target.endTag("rows");
    }

    private void paintColumns(
            PaintTarget target
    ) throws PaintException {
        target.startTag("visiblecolumns");
        int i = 0;
        for (final Iterator it = visibleColumns.iterator(); it.hasNext(); i++) {
            final Object columnId = it.next();
            if (columnId != null) {
                target.startTag("column");
                target.addAttribute("cid", String.valueOf(columnId));
                final String head = getColumnHeader(columnId);
                target.addAttribute("caption", (head != null ? head : ""));

                target.endTag("column");
            }
        }
        target.endTag("visiblecolumns");
    }

    private void paintPager(PaintTarget target) throws PaintException {
        target.startTag("pager");
        target.addAttribute("pagescount", pagesCount);
        target.addAttribute("curpage", currentPage);
        target.endTag("pager");
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey("curpage")) {
            setCurrentPage(((Integer) variables.get("curpage")).intValue(), false);
        }

        if (variables.containsKey("pagelength")) {
            setPageLength(((Integer) variables.get("pagelength")).intValue(), false);
        }

        refreshRenderedCells();
    }

    class IdsIterator implements Iterator {
        private final List itemIds;
        private int currentIndex;

        public IdsIterator(Container dataSource, int index) {
            itemIds = new ArrayList(dataSource.getItemIds());
            currentIndex = index;
        }

        public boolean hasNext() {
            return currentIndex < itemIds.size();
        }

        public Object next() {
            Object id = itemIds.get(currentIndex);
            currentIndex++;
            return id;
        }

        public void remove() {
        }
    }

    @Override
    public String getTag() {
        return TAG_NAME;
    }
}
