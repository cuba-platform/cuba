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

package com.haulmont.cuba.web.toolkit.ui.client.grouptable;

import com.google.gwt.dom.client.Element;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupTable;
import com.haulmont.cuba.web.toolkit.ui.client.table.CubaScrollTableConnector;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.TooltipInfo;
import com.vaadin.client.UIDL;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VScrollTable;
import com.vaadin.shared.ui.Connect;

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

        VScrollTable.VScrollTableBody.VScrollTableRow row = getWidget().focusedRow;

        super.updateFromUIDL(uidl, client);

        if (row instanceof CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableGroupRow) {
            getWidget().setRowFocus(
                    getWidget().getRenderedGroupRowByKey(
                            ((CubaGroupTableWidget.CubaGroupTableBody.CubaGroupTableGroupRow) row).getGroupKey()
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