/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextArea;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textarea.TextAreaConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaResizableTextArea.class)
public class CubaResizableTextAreaConnector extends TextAreaConnector {

    @Override
    public CubaResizableTextAreaState getState() {
        return (CubaResizableTextAreaState) super.getState();
    }

    @Override
    public CubaResizableTextAreaWidget getWidget() {
        return (CubaResizableTextAreaWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("resizable")) {
            getWidget().setResizable(getState().resizable);
        }
    }

    @Override
    protected void updateWidgetStyleNames() {
        super.updateWidgetStyleNames();
        getWidget().resizeElement.setClassName(CubaResizableTextAreaWidget.RESIZE_ELEMENT);
    }
}