/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.window;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.toolkit.ui.client.appui.ValidationErrorHolder;
import com.vaadin.client.ui.VWindow;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaWindowWidget extends VWindow {

    public static final String MODAL_WINDOW_CLASSNAME = "v-window-modal";
    public static final String NONMODAL_WINDOW_CLASSNAME = "v-window-nonmodal";

    public interface ContextMenuHandler {
        void onContextMenu(Event event);
    }

    protected ContextMenuHandler contextMenuHandler;

    public CubaWindowWidget() {
        DOM.sinkEvents(header, DOM.getEventsSunk(header) | Event.ONCONTEXTMENU);
        addStyleName(NONMODAL_WINDOW_CLASSNAME);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (contextMenuHandler != null && event.getTypeInt() == Event.ONCONTEXTMENU) {
            contextMenuHandler.onContextMenu(event);
            return;
        }

        if ((event.getTypeInt() == Event.ONCLICK
                || event.getTypeInt() == Event.ONMOUSEDOWN)
                && event.getButton() != NativeEvent.BUTTON_LEFT) {
            event.preventDefault();
            event.stopPropagation();
            return;
        }

        super.onBrowserEvent(event);
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        // disabled Vaadin close by ESCAPE #PL-4355
    }

    @Override
    protected void constructDOM() {
        super.constructDOM();

        DOM.sinkEvents(closeBox, Event.FOCUSEVENTS);
    }

    @Override
    protected void onCloseClick() {
        if (ValidationErrorHolder.hasValidationErrors()) {
            return;
        }

        super.onCloseClick();
    }

    @Override
    public void setVaadinModality(boolean modality) {
        super.setVaadinModality(modality);
        if (modality) {
            removeStyleName(NONMODAL_WINDOW_CLASSNAME);
            if (!getStyleName().contains(MODAL_WINDOW_CLASSNAME)) {
                addStyleName(MODAL_WINDOW_CLASSNAME);
            }
        } else {
            removeStyleName(MODAL_WINDOW_CLASSNAME);
            if (!getStyleName().contains(NONMODAL_WINDOW_CLASSNAME)) {
                addStyleName(NONMODAL_WINDOW_CLASSNAME);
            }
        }
    }

    @Override
    public void setCaption(String c, String iconURL, boolean asHtml) {
        if (isBlank(c)) {
            c = "&nbsp";
            asHtml = true;
        }
        super.setCaption(c, iconURL, asHtml);
    }

    protected boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    protected boolean isWhitespace(int ch) {
        char[] whitespaces = {
                ' ',        // space
                '\u00A0',   // a non-breaking space
                '\u2007',   // a non-breaking space
                '\u202F',   // a non-breaking space
                '\t',       // U+0009 HORIZONTAL TABULATION
                '\n',       // U+000A LINE FEED
                '\u000B',   // U+000B VERTICAL TABULATION
                '\f',       // U+000C FORM FEED
                '\r',       // U+000D CARRIAGE RETURN
                '\u001C',   // U+001C FILE SEPARATOR
                '\u001D',   // U+001D GROUP SEPARATOR
                '\u001E',   // U+001E RECORD SEPARATOR
                '\u001F',   // U+001F UNIT SEPARATOR
        };
        for (int i = 0; i < whitespaces.length; i++) {
            if (ch == whitespaces[i]) {
                return true;
            }
        }
        return false;
    }
}