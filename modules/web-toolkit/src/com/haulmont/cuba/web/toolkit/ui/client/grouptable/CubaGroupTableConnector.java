/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.grouptable;

import com.google.gwt.dom.client.Element;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupTable;
import com.haulmont.cuba.web.toolkit.ui.client.table.CubaScrollTableConnector;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.TooltipInfo;
import com.vaadin.client.UIDL;
import com.vaadin.client.WidgetUtil;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaGroupTable.class)
public class CubaGroupTableConnector extends CubaScrollTableConnector {

    @Override
    public CubaGroupTableWidget getWidget() {
        return (CubaGroupTableWidget) super.getWidget();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (uidl.hasVariable("groupColumns")) {
            getWidget().updateGroupColumns(uidl.getStringArrayVariableAsSet("groupColumns"));
        } else {
            getWidget().updateGroupColumns(null);
        }

        super.updateFromUIDL(uidl, client);

        if (uidl.hasAttribute("focusedGroupRow")) {
            getWidget().setRowFocus(
                    getWidget().getRenderedGroupRowByKey(
                            uidl.getStringAttribute("focusedGroupRow")
                    )
            );
        }
    }

    @Override
    public TooltipInfo getTooltipInfo(Element element) {
        if (element != getWidget().getElement()) {
            Object node = WidgetUtil.findWidget(
                    element,
                    CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableRow.class);

            if (node != null) {
                CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableRow row
                        = (CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableRow) node;
                return row.getTooltip(element);
            }

            node = WidgetUtil.findWidget(
                    element,
                    CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableGroupRow.class);

            if (node != null) {
                CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableGroupRow row
                        = (CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableGroupRow) node;
                return row.getTooltip(element);
            }
        }

        return super.getTooltipInfo(element);
    }
}