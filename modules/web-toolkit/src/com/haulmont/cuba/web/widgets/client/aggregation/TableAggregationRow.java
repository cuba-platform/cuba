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

package com.haulmont.cuba.web.widgets.client.aggregation;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.Tools;
import com.haulmont.cuba.web.widgets.client.tableshared.TableWidget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.UIDL;
import com.vaadin.v7.client.ui.VScrollTable;

import java.util.Collections;
import java.util.Iterator;

/**
 * Special aggregation row for {@link com.haulmont.cuba.web.widgets.client.table.CubaScrollTableWidget} and
 * {@link com.haulmont.cuba.web.widgets.client.treetable.CubaTreeTableWidget}
 */
public class TableAggregationRow extends Panel {

    protected boolean initialized = false;

    protected char[] aligns;
    protected Element tr;

    protected TableWidget tableWidget;

    public TableAggregationRow(TableWidget tableWidget) {
        this.tableWidget = tableWidget;

        setElement(Document.get().createDivElement());

        getElement().setClassName(tableWidget.getStylePrimaryName() + "-arow");
        getElement().getStyle().setOverflow(Overflow.HIDDEN);
    }

    @Override
    public Iterator<Widget> iterator() {
        return Collections.<Widget>emptyList().iterator();
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

        td.addClassName(tableWidget.getStylePrimaryName() + "-aggregation-cell");

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