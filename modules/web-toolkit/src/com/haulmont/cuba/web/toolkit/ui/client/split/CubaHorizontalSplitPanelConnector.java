/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.split;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalSplitPanel;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.splitpanel.HorizontalSplitPanelConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = CubaHorizontalSplitPanel.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaHorizontalSplitPanelConnector extends HorizontalSplitPanelConnector {

    @Override
    public CubaHorizontalSplitPanelState getState() {
        return (CubaHorizontalSplitPanelState) super.getState();
    }

    @Override
    public CubaHorizontalSplitPanelWidget getWidget() {
        return (CubaHorizontalSplitPanelWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CubaHorizontalSplitPanelWidget.class);
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("dockable")) {
            getWidget().setDockable(getState().dockable);
        }
        if (stateChangeEvent.hasPropertyChanged("dockMode")) {
            getWidget().setDockMode(getState().dockMode);
        }
        if (stateChangeEvent.hasPropertyChanged("defaultPosition")) {
            getWidget().defaultPosition = getState().defaultPosition;
        }
    }
}