/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sourcecodeeditor;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import org.vaadin.aceeditor.client.AceEditorWidget;
import org.vaadin.aceeditor.client.gwt.GwtAceEvent;
import org.vaadin.aceeditor.client.gwt.GwtAceFocusBlurHandler;

/**
 * @author artamonov
 */
public class CubaSourceCodeEditorWidget extends AceEditorWidget {

    protected int tabIndex = 0;

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

        if (isEnabled() && !readOnly) {
            if (type == Event.ONFOCUS) {
                editor.focus();
                return;
            }
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

        if (!enabled) {
            super.setTabIndex(-1);
        } else {
            super.setTabIndex(tabIndex);
        }

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
        if (enabled && !readOnly) {
            super.setTabIndex(index);
        }

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

        if (!enabled) {
            super.setTabIndex(-1);
        } else {
            super.setTabIndex(tabIndex);
        }
    }
}