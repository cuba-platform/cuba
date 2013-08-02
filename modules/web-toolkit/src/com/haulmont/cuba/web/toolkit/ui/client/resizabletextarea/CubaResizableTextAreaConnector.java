/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextArea;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textarea.TextAreaConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = CubaResizableTextArea.class, loadStyle = Connect.LoadStyle.DEFERRED)
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
    protected Widget createWidget() {
        return GWT.create(CubaResizableTextAreaWidget.class);
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

        getWidget().resizeElement.setClassName("cuba-resizabletextarea-resize-corner");
    }
}