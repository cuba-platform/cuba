/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.button;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.vaadin.client.ui.VButton;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaButtonWidget extends VButton {

    private boolean focused = false;

    public CubaButtonWidget() {
        addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                focused = true;
            }
        });
        addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                focused = false;
            }
        });
    }

    public boolean isFocused() {
        return focused;
    }
}