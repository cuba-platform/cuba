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

package com.haulmont.cuba.web.widgets.client.sourcecodeeditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.CubaSourceCodeEditor;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.TooltipInfo;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.HasContextHelpConnector;
import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.hascontexthelp.HasContextHelpServerRpc;
import org.vaadin.aceeditor.client.AceEditorConnector;
import org.vaadin.aceeditor.client.AceEditorWidget;

@Connect(CubaSourceCodeEditor.class)
public class CubaSourceCodeEditorConnector extends AceEditorConnector implements HasContextHelpConnector {
    private boolean resetEditHistory = false;

    public CubaSourceCodeEditorConnector() {
        registerRpc(CubaSourceCodeEditorClientRpc.class, new CubaSourceCodeEditorClientRpc() {
            @Override
            public void resetEditHistory() {
                resetEditHistory = true;
            }
        });
    }

    @Override
    protected Widget createWidget() {
        AceEditorWidget widget = GWT.create(CubaSourceCodeEditorWidget.class);
        widget.addTextChangeListener(this);
        widget.addSelectionChangeListener(this);
        widget.setFocusChangeListener(this);
        return widget;
    }

    @Override
    protected void sendToServer(SendCond send, boolean immediately) {
        super.sendToServer(send, immediately);

        if (send == SendCond.NO && resetEditHistory) {
            getWidget().resetEditHistory();
            resetEditHistory = false;
        }
    }

    @Override
    public boolean hasTooltip() {
        return super.hasTooltip() || isContextHelpTooltipEnabled();
    }

    @Override
    public TooltipInfo getTooltipInfo(Element element) {
        TooltipInfo info = super.getTooltipInfo(element);

        if (isContextHelpTooltipEnabled()) {
            info.setContextHelp(getState().contextHelpText);
            info.setContextHelpHtmlEnabled(getState().contextHelpTextHtmlEnabled);
        }

        return info;
    }

    protected boolean isContextHelpTooltipEnabled() {
        boolean hasListeners = getState().registeredEventListeners != null
                && getState().registeredEventListeners.contains(AbstractFieldState.CONTEXT_HELP_ICON_CLICK_EVENT);

        return !hasListeners && getState().contextHelpText != null
                && !getState().contextHelpText.isEmpty();
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
        if (stateChangeEvent.hasPropertyChanged("printMarginColumn")) {
            getWidget().setPrintMarginColumn(getState().printMarginColumn);
        }

        if (stateChangeEvent.isInitialStateChange()) {
            getWidget().resetEditHistory();
        }
    }

    @Override
    public void contextHelpIconClick(NativeEvent event) {
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event, getWidget().getElement());

        getRpcProxy(HasContextHelpServerRpc.class).iconClick(details);
    }

    @Override
    public void contextHelpIconClick(MouseEvent event) {
        contextHelpIconClick(event.getNativeEvent());
    }
}