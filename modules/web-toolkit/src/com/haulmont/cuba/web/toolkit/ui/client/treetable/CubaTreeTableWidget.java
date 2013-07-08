/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.treetable;

import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VTreeTable;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTreeTableWidget extends VTreeTable {

    protected static final String WIDGET_CELL_CLASSNAME = "widget-container";

    protected boolean textSelectionEnabled = false;

    @Override
    protected VScrollTableBody createScrollBody() {
        return new CubaTreeTableBody();
    }

    protected class CubaTreeTableBody extends VTreeTableScrollBody {

        @Override
        protected VScrollTableRow createRow(UIDL uidl, char[] aligns2) {
            if (uidl.hasAttribute("gen_html")) {
                // This is a generated row.
                return new VTreeTableGeneratedRow(uidl, aligns2);
            }
            return new CubaTreeTableRow(uidl, aligns2);
        }

        protected class CubaTreeTableRow extends VTreeTableRow {

            public CubaTreeTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void initCellWithWidget(Widget w, char align,
                                              String style, boolean sorted, TableCellElement td) {
                super.initCellWithWidget(w, align, style, sorted, td);

                td.getFirstChildElement().addClassName(WIDGET_CELL_CLASSNAME);
            }

            @Override
            protected void initCellWithText(String text, char align, String style, boolean textIsHTML,
                                            boolean sorted, String description, TableCellElement td) {
                super.initCellWithText(text, align, style, textIsHTML, sorted, description, td);

                Element tdElement = td.cast();
                Tools.textSelectionEnable(tdElement, textSelectionEnabled);
            }

            @Override
            protected void updateCellStyleNames(TableCellElement td, String primaryStyleName) {
                Element container = td.getFirstChild().cast();
                boolean isWidget = container.getClassName() != null
                        && container.getClassName().contains(WIDGET_CELL_CLASSNAME);

                super.updateCellStyleNames(td, primaryStyleName);

                if (isWidget)
                    container.addClassName(WIDGET_CELL_CLASSNAME);
            }
        }
    }
}