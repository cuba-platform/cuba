/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.aggregation;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VScrollTable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Special aggregation row for {@link com.haulmont.cuba.web.toolkit.ui.client.table.CubaScrollTableWidget} and
 * {@link com.haulmont.cuba.web.toolkit.ui.client.treetable.CubaTreeTableWidget}
 *
 * @author artamonov
 * @version $Id$
 */
public class TableAggregationRow extends Panel {

    protected boolean initialized = false;

    protected char[] aligns;
    protected Element tr;

    protected AggregatableTable tableWidget;

    public TableAggregationRow(AggregatableTable tableWidget) {
        this.tableWidget = tableWidget;

        setElement(Document.get().createDivElement());

        getElement().setClassName(tableWidget.getStylePrimaryName() + "-arow");
        getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
    }

    @Override
    public Iterator<Widget> iterator() {
        return new LinkedList<Widget>().iterator();
    }

    @Override
    public boolean remove(Widget child) {
        return false;
    }

    public void updateFromUIDL(UIDL uidl) {
        if (getElement().hasChildNodes()) {
            getElement().removeAllChildren();
        }

        aligns = tableWidget.getHead().getColumnAlignments();

        if (uidl.getChildCount() > 0) {
            final Element table = DOM.createTable();
            table.setAttribute("cellpadding", "0");
            table.setAttribute("cellspacing", "0");

            final Element tBody = DOM.createTBody();
            tr = DOM.createTR();

            tr.setClassName(tableWidget.getStylePrimaryName() + "-arow-row");

            addCellsFromUIDL(uidl);

            tBody.appendChild(tr);
            table.appendChild(tBody);
            getElement().appendChild(table);
        }

        initialized = getElement().hasChildNodes();
    }

    protected void addCellsFromUIDL(UIDL uidl) {
        int colIndex = 0;
        final Iterator cells = uidl.getChildIterator();
        while (cells.hasNext() && colIndex < tableWidget.getVisibleColOrder().length) {
            String columnId = tableWidget.getVisibleColOrder()[colIndex];

            if (addSpecificCell(columnId, colIndex)) {
                colIndex++;
                continue;
            }

            final Object cell = cells.next();

            String style = "";
            if (uidl.hasAttribute("style-" + columnId)) {
                style = uidl.getStringAttribute("style-" + columnId);
            }

            boolean sorted = tableWidget.getHead().getHeaderCell(colIndex).isSorted();

            if (cell instanceof String) {
                addCell((String) cell, aligns[colIndex], style, sorted);
            }

            final String colKey = tableWidget.getColKeyByIndex(colIndex);
            int colWidth;
            if ((colWidth = tableWidget.getColWidth(colKey)) > -1) {
                tableWidget.setColWidth(colIndex, colWidth, false);
            }

            colIndex++;
        }
    }

    // Extension point for GroupTable divider column
    protected boolean addSpecificCell(String columnId, int colIndex) {
        return false;
    }

    protected void addCell(String text, char align, String style, boolean sorted) {
        final TableCellElement td = DOM.createTD().cast();

        final Element container = DOM.createDiv();
        container.setClassName(tableWidget.getStylePrimaryName() + "-cell-wrapper");

        td.setClassName(tableWidget.getStylePrimaryName() + "-cell-content");

        if (style != null && !style.equals("")) {
            td.addClassName(tableWidget.getStylePrimaryName() + "-cell-content-" + style);
        }

        if (sorted) {
            td.addClassName(tableWidget.getStylePrimaryName() + "-cell-content-sorted");
        }

        container.setInnerText(text);

        setAlign(align, container);

        td.appendChild(container);
        tr.appendChild(td);

        Tools.textSelectionEnable(td, tableWidget.isTextSelectionEnabled());
    }

    protected void setAlign(char align, final Element container) {
        // CAUTION: copied from VScrollTableRow
        switch (align) {
            case VScrollTable.ALIGN_CENTER:
                container.getStyle().setProperty("textAlign", "center");
                break;
            case VScrollTable.ALIGN_LEFT:
                container.getStyle().setProperty("textAlign", "left");
                break;
            case VScrollTable.ALIGN_RIGHT:
            default:
                container.getStyle().setProperty("textAlign", "right");
                break;
        }
    }

    public void setCellWidth(int cellIx, int width) {
        // CAUTION: copied from VScrollTableRow with small changes
        final Element cell = DOM.getChild(tr, cellIx);
        Style wrapperStyle = cell.getFirstChildElement().getStyle();
        int wrapperWidth = width;
        if (BrowserInfo.get().isWebkit()
                || BrowserInfo.get().isOpera10()) {
                    /*
                     * Some versions of Webkit and Opera ignore the width
                     * definition of zero width table cells. Instead, use 1px
                     * and compensate with a negative margin.
                     */
            if (width == 0) {
                wrapperWidth = 1;
                wrapperStyle.setMarginRight(-1, Style.Unit.PX);
            } else {
                wrapperStyle.clearMarginRight();
            }
        }
        wrapperStyle.setPropertyPx("width", wrapperWidth);
        cell.getStyle().setPropertyPx("width", width);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setHorizontalScrollPosition(int scrollLeft) {
        getElement().setPropertyInt("scrollLeft", scrollLeft);
    }
}