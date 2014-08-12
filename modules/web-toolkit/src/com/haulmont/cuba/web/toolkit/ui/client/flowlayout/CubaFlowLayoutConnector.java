/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.flowlayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaFlowLayout;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.MarginInfo;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaFlowLayout.class)
public class CubaFlowLayoutConnector extends CssLayoutConnector {

    @Override
    public CubaFlowLayoutState getState() {
        return (CubaFlowLayoutState) super.getState();
    }

    @Override
    public CubaFlowLayoutWidget getWidget() {
        return (CubaFlowLayoutWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CubaFlowLayoutWidget.class);
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setMargin(new MarginInfo(getState().marginsBitmask));
        getWidget().setSpacing(getState().spacing);
    }
}