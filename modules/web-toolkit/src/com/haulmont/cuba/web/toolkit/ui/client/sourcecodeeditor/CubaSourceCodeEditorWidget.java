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

package com.haulmont.cuba.web.toolkit.ui.client.sourcecodeeditor;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import org.vaadin.aceeditor.client.AceEditorWidget;
import org.vaadin.aceeditor.client.gwt.GwtAceEvent;
import org.vaadin.aceeditor.client.gwt.GwtAceFocusBlurHandler;

/**
 */
public class CubaSourceCodeEditorWidget extends AceEditorWidget {

    protected boolean readOnly = false;
    protected int tabIndex = 0;
    protected boolean handleTabKey = true;

    public CubaSourceCodeEditorWidget() {
        sinkEvents(Event.ONKEYDOWN | Event.ONFOCUS);
    }

    @Override
    public void initialize() {
        super.initialize();

        editor.addFocusListener(new GwtAceFocusBlurHandler() {
            @Override
            public void onFocus(GwtAceEvent e) {
                addStyleDependentName("focus");
            }

            @Override
            public void onBlur(GwtAceEvent e) {
                removeStyleDependentName("focus");
            }
        });

        super.setTabIndex(-1);
    }

    @Override
    public void onBrowserEvent(Event event) {
        int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN
                && event.getKeyCode() == KeyCodes.KEY_ENTER
                && !event.getAltKey()
                && !event.getShiftKey()
                && !event.getCtrlKey()) {
            event.stopPropagation();
            return;
        }

        super.onBrowserEvent(event);
    }

    protected Element getTextAreaElement() {
        return getElement().getFirstChildElement().getFirstChildElement();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        super.setReadOnly(!enabled || readOnly);

        if (editor != null) {
            if (enabled) {
                getTextAreaElement().removeAttribute("disabled");
            } else {
                getTextAreaElement().setAttribute("disabled", "disabled");
            }
        }
    }

    @Override
    public void setTabIndex(int index) {
        this.tabIndex = index;
    }

    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        super.setReadOnly(!this.enabled || readOnly);
        if (readOnly) {
            getTextAreaElement().setTabIndex(-1);
        } else {
            getTextAreaElement().removeAttribute("tabindex");
        }
    }

    public void setHandleTabKey(boolean handleTabKey) {
        this.handleTabKey = handleTabKey;
        if (!handleTabKey) {
            unbindTabKey(editor);
        }
    }

    public native void unbindTabKey(JavaScriptObject editor) /*-{
        editor.commands.bindKey("Shift-Tab", null);
        editor.commands.bindKey("Tab", null);
	}-*/;
}