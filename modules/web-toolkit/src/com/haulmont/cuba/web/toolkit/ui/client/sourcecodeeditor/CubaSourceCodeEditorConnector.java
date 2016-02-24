/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sourcecodeeditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaSourceCodeEditor;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;
import org.vaadin.aceeditor.client.AceEditorConnector;
import org.vaadin.aceeditor.client.AceEditorWidget;

/**
 * @author artamonov
 */
@Connect(CubaSourceCodeEditor.class)
public class CubaSourceCodeEditorConnector extends AceEditorConnector {

    @Override
    protected Widget createWidget() {
        AceEditorWidget widget = GWT.create(CubaSourceCodeEditorWidget.class);
        widget.addTextChangeListener(this);
        widget.addSelectionChangeListener(this);
        widget.setFocusChangeListener(this);
        return widget;
    }

    @Override
    public CubaSourceCodeEditorWidget getWidget() {
        return (CubaSourceCodeEditorWidget) super.getWidget();
    }

    @Override
    public CubaSourceCodeEditorState getState() {
        return (CubaSourceCodeEditorState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("handleTabKey")) {
            getWidget().setHandleTabKey(getState().handleTabKey);
        }
    }
}