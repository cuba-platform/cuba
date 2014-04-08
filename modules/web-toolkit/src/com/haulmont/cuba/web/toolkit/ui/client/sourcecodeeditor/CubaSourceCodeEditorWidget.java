/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sourcecodeeditor;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import org.vaadin.aceeditor.client.AceEditorWidget;
import org.vaadin.aceeditor.client.gwt.GwtAceEvent;
import org.vaadin.aceeditor.client.gwt.GwtAceFocusBlurHandler;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaSourceCodeEditorWidget extends AceEditorWidget {

    private boolean enabled = true;
    private boolean readOnly = false;

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

        if (type == Event.ONFOCUS) {
            editor.focus();
            return;
        }

        super.onBrowserEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.enabled = enabled;

        super.setReadOnly(!this.enabled || readOnly);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        super.setReadOnly(!this.enabled || readOnly);
    }
}