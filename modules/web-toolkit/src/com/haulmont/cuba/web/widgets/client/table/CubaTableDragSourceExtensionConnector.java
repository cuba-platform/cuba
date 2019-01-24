/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets.client.table;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.CubaTableDragSourceExtension;
import com.haulmont.cuba.web.widgets.client.tableshared.TableWidget;
import com.haulmont.cuba.web.widgets.client.tableshared.TableWidget.AfterBodyUpdateListener;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.DragSourceExtensionConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.VScrollTable.VScrollTableBody.VScrollTableRow;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Connect(CubaTableDragSourceExtension.class)
public class CubaTableDragSourceExtensionConnector extends DragSourceExtensionConnector {

    protected AfterBodyUpdateListener afterBodyUpdateHandler = this::initDraggableRows;

    @Override
    protected void extend(ServerConnector target) {
        super.extend(target);

        TableWidget tableWidget = getTableWidget();
        if (tableWidget != null) {
            tableWidget.addAfterBodyUpdateListener(afterBodyUpdateHandler);
        }
    }

    @Override
    protected void addDraggable(Element element) {
        Scheduler.get().scheduleDeferred(this::initDraggableRows);
    }

    protected void initDraggableRows() {
        TableWidget tableWidget = getTableWidget();
        if (tableWidget != null) {
            List<Widget> list = tableWidget.getRenderedRows();

            for (Widget row : list) {
                Element rowElement = row.getElement();
                rowElement.setDraggable(Element.DRAGGABLE_TRUE);

                String primaryDragSourceStyle = getStylePrimaryName(getDraggableElement()) + STYLE_SUFFIX_DRAGSOURCE;
                if (!isElementContainsClass(rowElement, primaryDragSourceStyle)) {
                    rowElement.addClassName(primaryDragSourceStyle);
                }

                if (!isElementContainsClass(rowElement, getStyleNameDraggable())) {
                    rowElement.addClassName(getStyleNameDraggable());
                }
            }
        }
    }

    protected boolean isElementContainsClass(Element element, String className) {
        return element.getClassName().contains(className);
    }

    @Override
    protected void removeDraggable(Element element) {
        if (getDragSourceWidget() instanceof TableWidget) {
            TableWidget widget = (TableWidget) getDragSourceWidget();
            List<Widget> list = widget.getRenderedRows();

            for (Widget row : list) {
                Element rowElement = row.getElement();
                rowElement.setDraggable(Element.DRAGGABLE_FALSE);

                String primaryDragSourceStyle = getStylePrimaryName(getDraggableElement()) + STYLE_SUFFIX_DRAGSOURCE;
                rowElement.removeClassName(primaryDragSourceStyle);
                rowElement.removeClassName(getStyleNameDraggable());
            }
        }
    }

    @Override
    public void onUnregister() {
        if (getTableWidget() != null) {
            getTableWidget().removeAfterBodyUpdateListener(afterBodyUpdateHandler);
        }

        super.onUnregister();
    }

    @Override
    protected Map<String, String> createDataTransferData(NativeEvent dragStartEvent) {
        getRpcProxy(CubaTableDragSourceExtensionServerRpc.class)
                .updateDraggedItems(getDraggedRows(dragStartEvent));

        return super.createDataTransferData(dragStartEvent);
    }

    private List<String> getDraggedRows(NativeEvent dragStartEvent) {
        List<String> draggedRows = new ArrayList<>();

        if (TableRowElement.is(dragStartEvent.getEventTarget())) {
            TableRowElement row = dragStartEvent.getEventTarget().cast();

            TableWidget tableWidget = getTableWidget();
            if (tableWidget == null) {
                draggedRows.add(String.valueOf(row.getSectionRowIndex()));
                return draggedRows;
            }

            if (isRowSelected(row)) {
                return getAllVisibleSelectedRows();
            }

            draggedRows.add(getDraggedRowKey(row));
        }

        return draggedRows;
    }

    protected boolean isRowSelected(TableRowElement row) {
        TableWidget tableWidget = getTableWidget();
        if (tableWidget == null) {
            return false;
        }

        List<Widget> rows = tableWidget.getRenderedRows();

        for (Widget w : rows) {
            TableRowElement rowElement = w.getElement().cast();
            if (rowElement.equals(row)
                    && ((VScrollTableRow) w).isSelected()) {

                return true;
            }
        }
        return false;
    }

    protected String getDraggedRowKey(TableRowElement row) {
        TableWidget tableWidget = getTableWidget();
        if (tableWidget != null) {
            for (Widget w : tableWidget.getRenderedRows()) {
                TableRowElement rowElement = w.getElement().cast();
                if (rowElement.equals(row)) {
                    return ((VScrollTableRow) w).getKey();
                }
            }
        }
        return null;
    }

    protected List<String> getAllVisibleSelectedRows() {
        List<String> selectedRows = new ArrayList<>();

        TableWidget tableWidget = getTableWidget();
        if (tableWidget == null) {
            return selectedRows;
        }

        for (Widget w : tableWidget.getRenderedRows()) {
            if (((VScrollTableRow) w).isSelected()) {
                selectedRows.add(((VScrollTableRow) w).getKey());
            }
        }

        return selectedRows;
    }

    @Nullable
    protected TableWidget getTableWidget() {
        Widget widget = getDragSourceWidget();
        if (widget instanceof TableWidget) {
            return (TableWidget) widget;
        }
        return null;
    }
}
