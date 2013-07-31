/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.treetable;

import com.haulmont.cuba.web.toolkit.ui.CubaTreeTable;
import com.haulmont.cuba.web.toolkit.ui.client.table.CubaTableClientRpc;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.treetable.TreeTableConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaTreeTable.class)
public class CubaTreeTableConnector extends TreeTableConnector {

    public CubaTreeTableConnector() {

        registerRpc(CubaTableClientRpc.class, new CubaTableClientRpc() {
            @Override
            public void hidePresentationsPopup() {
                if (getWidget().presentationsEditorPopup != null) {
                    getWidget().presentationsEditorPopup.hide();
                }
            }
        });
    }

    @Override
    public CubaTreeTableWidget getWidget() {
        return (CubaTreeTableWidget) super.getWidget();
    }

    @Override
    public CubaTreeTableState getState() {
        return (CubaTreeTableState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("textSelectionEnabled")) {
            getWidget().textSelectionEnabled = getState().textSelectionEnabled;
        }
        if (stateChangeEvent.hasPropertyChanged("allowPopupMenu")){
            getWidget().allowPopupMenu = getState().allowPopupMenu;
        }
        if (stateChangeEvent.hasPropertyChanged("presentations")) {
            if (getState().presentations != null) {
                getWidget().setPresentationsMenu(((ComponentConnector) getState().presentations).getWidget());
            } else {
                getWidget().setPresentationsMenu(null);
            }
        }
    }
}