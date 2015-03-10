/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.checkbox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.i18n.client.HasDirection;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VCheckBox;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaCheckBoxWidget extends VCheckBox implements FocusHandler, BlurHandler {

    protected boolean captionManagedByLayout = false;

    public CubaCheckBoxWidget() {
        addBlurHandler(this);
        addFocusHandler(this);

        updateCaptionStyle();
    }

    @Override
    public void setText(String text) {
        if (!captionManagedByLayout) {
            super.setText(text);

            updateCaptionStyle();
        }
    }

    @Override
    public void setText(String text, HasDirection.Direction dir) {
        if (!captionManagedByLayout) {
            super.setText(text, dir);

            updateCaptionStyle();
        }
    }

    @Override
    public void setFocus(boolean focused) {
        super.setFocus(focused);

        if (BrowserInfo.get().isWebkit()) {
            clearWebkitTextSelection();
        }
    }

    @Override
    public void onFocus(FocusEvent arg) {
        addStyleDependentName("focus");
    }

    public static native void clearWebkitTextSelection()/*-{
        if ($wnd.getSelection) {
            if ($wnd.getSelection().empty) {  // only for Chrome
                $wnd.getSelection().empty();
            }
        }
    }-*/ ;

    @Override
    public void onBlur(BlurEvent arg) {
        removeStyleDependentName("focus");
    }

    protected void updateCaptionStyle() {
        if (getText() == null || "".equals(getText())) {
            addStyleDependentName("empty-caption");
        } else {
            removeStyleDependentName("empty-caption");
        }
    }
}