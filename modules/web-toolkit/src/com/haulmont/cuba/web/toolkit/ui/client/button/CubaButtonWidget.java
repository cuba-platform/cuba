/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VButton;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaButtonWidget extends VButton {

    @Override
    public void onClick(ClickEvent event) {
        if (BrowserInfo.get().isIE() && BrowserInfo.get().getIEVersion() >= 11) {
            // fix focusing of button-wrap in IE11
            setFocus(true);
        }

        super.onClick(event);
    }
}