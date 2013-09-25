/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sourcecodeeditor;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import org.vaadin.aceeditor.client.AceEditorWidget;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaSourceCodeEditorWidget extends AceEditorWidget {

    public CubaSourceCodeEditorWidget() {
        sinkEvents(Event.ONKEYDOWN);
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
}