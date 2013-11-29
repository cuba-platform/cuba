/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VButton;

/**
 * @author artamonov
 * @version $Id$
 */
public class VWindowOpenButton extends VButton {

    protected JavaScriptObject newWindow;

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (uidl.hasAttribute("openUrl") && newWindow != null) {
            navigateTo(newWindow, uidl.getStringAttribute("openUrl"));
            newWindow = null;
        }
    }

    protected static native JavaScriptObject openWindow() /*-{
        return $wnd.open('', '_blank');
    }-*/;

    protected static native void navigateTo(JavaScriptObject window, String url) /*-{
        window.location = url;
    }-*/;

    @Override
    public void onClick(ClickEvent event) {
        if (newWindow == null) {
            newWindow = openWindow();
        }

        if (id == null || client == null) {
            return;
        }
        if (BrowserInfo.get().isSafari()) {
            VWindowOpenButton.this.setFocus(true);
        }
        client.updateVariable(id, "state", true, false);

        // Add mouse details
        MouseEventDetails details = new MouseEventDetails(
                event.getNativeEvent(), getElement());
        client.updateVariable(id, "mousedetails", details.serialize(), true);

        clickPending = false;
    }
}