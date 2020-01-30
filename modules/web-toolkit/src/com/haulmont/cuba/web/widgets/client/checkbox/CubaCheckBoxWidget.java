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

package com.haulmont.cuba.web.widgets.client.checkbox;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.HasDirection;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VCheckBox;

public class CubaCheckBoxWidget extends VCheckBox implements FocusHandler, BlurHandler, ClickHandler, KeyDownHandler {

    protected boolean captionManagedByLayout = false;
    protected boolean readOnly;

    protected Element contextHelpIcon;

    public CubaCheckBoxWidget() {
        addBlurHandler(this);
        addFocusHandler(this);
        addClickHandler(this);
        addKeyDownHandler(this);

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

    @Override
    public void onClick(ClickEvent event) {
        if (!isEnabled() || isReadOnly()) {
            event.preventDefault();

            if (isReadOnly()
                    && (BrowserInfo.get().isIE() || BrowserInfo.get().isEdge())) {
                // IE and Edge do not focus read-only checkbox on click
                setFocus(true);
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if ((!isEnabled() || isReadOnly())
                && event.getNativeKeyCode() != KeyCodes.KEY_TAB) {
            event.preventDefault();
        }
    }

    public void setReadOnly(boolean readOnly) {
        if (this.readOnly != readOnly) {
            this.readOnly = readOnly;
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    protected void updateCaptionStyle() {
        if (getText() == null || getText().isEmpty()) {
            addStyleDependentName("empty-caption");
        } else {
            removeStyleDependentName("empty-caption");
        }
    }
}