/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextAreaWrapper;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.customfield.CustomFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author gorelov
 * @version $Id$
 */
@Connect(CubaResizableTextAreaWrapper.class)
public class CubaResizableTextAreaWrapperConnector extends CustomFieldConnector {

    @Override
    protected void init() {
        super.init();

        getWidget().resizeHandler = new CubaResizableTextAreaWrapperWidget.ResizeHandler() {
            @Override
            public void handleResize() {
                getLayoutManager().setNeedsMeasure(CubaResizableTextAreaWrapperConnector.this);
            }

            @Override
            public void sizeChanged(String width, String height) {
                getRpcProxy(CubaResizableTextAreaWrapperServerRpc.class).sizeChanged(width, height);
            }

            @Override
            public void textChanged(String text) {
                getRpcProxy(CubaResizableTextAreaWrapperServerRpc.class).textChanged(text);
            }
        };
    }

    @Override
    public CubaResizableTextAreaWrapperState getState() {
        return (CubaResizableTextAreaWrapperState) super.getState();
    }

    @Override
    public CubaResizableTextAreaWrapperWidget getWidget() {
        return (CubaResizableTextAreaWrapperWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("resizable")) {
            getWidget().setResizable(getState().resizable);
        }

        if (stateChangeEvent.hasPropertyChanged("enabled")) {
            getWidget().setEnabled(isEnabled());
        }
    }
}
