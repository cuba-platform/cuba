/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.table;

import com.google.gwt.dom.client.Element;
import com.haulmont.cuba.web.toolkit.ui.CubaTable;
import com.vaadin.client.*;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.table.TableConnector;
import com.vaadin.shared.ui.Connect;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(value = CubaTable.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaScrollTableConnector extends TableConnector {

    public CubaScrollTableConnector() {
        registerRpc(CubaTableClientRpc.class, new CubaTableClientRpc() {
            @Override
            public void hidePresentationsPopup() {
                if (getWidget().presentationsEditorPopup != null) {
                    getWidget().presentationsEditorPopup.hide();
                }
            }

            @Override
            public void hideContextMenuPopup() {
                if (getWidget().customContextMenuPopup != null) {
                    getWidget().customContextMenuPopup.hide();
                }
            }

            @Override
            public void showCustomPopup() {
                getWidget().showCustomPopup();
            }
        });
    }

    @Override
    public CubaTableState getState() {
        return (CubaTableState) super.getState();
    }

    @Override
    public CubaScrollTableWidget getWidget() {
        return (CubaScrollTableWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("textSelectionEnabled")) {
            getWidget().textSelectionEnabled = getState().textSelectionEnabled;

            if (getWidget().getTotalRows() > 0) {
                getWidget().updateTextSelection();
            }
        }
        if (stateChangeEvent.hasPropertyChanged("contextMenuEnabled")) {
            getWidget().contextMenuEnabled = getState().contextMenuEnabled;
        }
        if (stateChangeEvent.hasPropertyChanged("presentations")) {
            if (getState().presentations != null) {
                ComponentConnector presentations = (ComponentConnector) getState().presentations;
                getWidget().setPresentationsMenu(presentations.getWidget());
            } else {
                getWidget().setPresentationsMenu(null);
            }
        }
        if (stateChangeEvent.hasPropertyChanged("contextMenu")) {
            if (getState().contextMenu != null) {
                ComponentConnector contextMenu = (ComponentConnector) getState().contextMenu;
                getWidget().customContextMenu = contextMenu.getWidget();
            } else {
                getWidget().customContextMenu = null;
            }
        }
        if (stateChangeEvent.hasPropertyChanged("multiLineCells")) {
            getWidget().multiLineCells = getState().multiLineCells;
        }
        if (stateChangeEvent.hasPropertyChanged("clickableColumnKeys")) {
            if (getState().clickableColumnKeys != null) {
                getWidget().clickableColumns = new HashSet<String>(Arrays.asList(getState().clickableColumnKeys));
            } else {
                getWidget().clickableColumns = null;
            }
        }
        if (stateChangeEvent.hasPropertyChanged("customPopup")) {
            if (getState().customPopup != null) {
                ComponentConnector customPopup = (ComponentConnector) getState().customPopup;
                getWidget().customPopupWidget = customPopup.getWidget();
            } else {
                getWidget().customPopupWidget = null;
            }
        }
    }

    @Override
    public TooltipInfo getTooltipInfo(Element element) {
        if (element != getWidget().getElement()) {
            Object node = WidgetUtil.findWidget(element, CubaScrollTableWidget.CubaScrollTableBody.CubaScrollTableRow.class);

            if (node != null) {
                CubaScrollTableWidget.CubaScrollTableBody.CubaScrollTableRow row
                        = (CubaScrollTableWidget.CubaScrollTableBody.CubaScrollTableRow) node;
                return row.getTooltip(element);
            }
        }

        return super.getTooltipInfo(element);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (uidl.hasVariable("collapsedcolumns")) {
            getWidget().addStyleName("collapsing-allowed");
        } else {
            getWidget().removeStyleName("collapsing-allowed");
        }

        // We may have actions attached to this table
        if (uidl.getChildCount() > 1) {
            final int cnt = uidl.getChildCount();
            for (int i = 1; i < cnt; i++) {
                UIDL childUidl = uidl.getChildUIDL(i);
                if (childUidl.getTag().equals("shortcuts")) {
                    if (getWidget().getShortcutActionHandler() == null) {
                        getWidget().setShortcutActionHandler(new ShortcutActionHandler(uidl.getId(), client));
                    }
                    getWidget().getShortcutActionHandler().updateActionMap(childUidl);
                }
            }
        }
    }

    @Override
    protected void updateAdditionalRowData(UIDL uidl) {
        UIDL arow = uidl.getChildByTagName("arow");
        if (arow != null) {
            getWidget().updateAggregationRow(arow);
        }
    }

    @Override
    protected void init() {
        super.init();

        getWidget().cellClickListener = new TableCellClickListener() {
            @Override
            public void onClick(String columnKey, int rowKey) {
                getRpcProxy(CubaTableServerRpc.class).onClick(columnKey, String.valueOf(rowKey));
            }
        };
    }
}