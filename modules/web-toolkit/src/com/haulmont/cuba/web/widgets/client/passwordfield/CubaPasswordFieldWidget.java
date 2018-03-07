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

package com.haulmont.cuba.web.widgets.client.passwordfield;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.capslockindicator.CapsLockChangeHandler;
import com.vaadin.client.BrowserInfo;
import com.vaadin.v7.client.ui.VPasswordField;

public class CubaPasswordFieldWidget extends VPasswordField implements KeyPressHandler, KeyDownHandler {

    protected Boolean capsLock;
    protected Widget capslockIndicator;

    protected HandlerRegistration pressHandlerRegistration = null;
    protected HandlerRegistration downHandlerRegistration = null;

    public void setAutocomplete(boolean autocomplete) {
        if (autocomplete) {
            getElement().removeAttribute("autocomplete");
        } else {
            BrowserInfo browser = BrowserInfo.get();

            if (browser.isIE()
                    || (browser.isGecko() && browser.getGeckoVersion() < 47)
                    || (browser.isChrome() && browser.getBrowserMajorVersion() < 49)) {
                getElement().setAttribute("autocomplete", "off");
            } else {
                getElement().setAttribute("autocomplete", "new-password");
            }
        }
    }

    public void setIndicateCapsLock(Widget capslockIndicator) {
        this.capslockIndicator = capslockIndicator;

        if (capslockIndicator != null) {
            if (pressHandlerRegistration == null) {
                pressHandlerRegistration = addKeyPressHandler(this);
                downHandlerRegistration = addKeyDownHandler(this);
            }
        } else if (pressHandlerRegistration != null) {
            downHandlerRegistration.removeHandler();
            downHandlerRegistration = null;

            pressHandlerRegistration.removeHandler();
            pressHandlerRegistration = null;
        }
    }

    protected boolean isMacOS() {
        String userAgent = Window.Navigator.getUserAgent();
        return userAgent.contains("Mac");
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        char charCode = event.getCharCode();

        if (charCode == 0) {
            return;
        }

        if (Character.toLowerCase(charCode) == Character.toUpperCase(charCode)) {
            return;
        }

        capsLock = (Character.toLowerCase(charCode) == charCode && event.isShiftKeyDown())
                || (Character.toUpperCase(charCode) == charCode && !event.isShiftKeyDown());

        if (pressHandlerRegistration != null) {
            showCapsLockStatus(capsLock);
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == 20 && capsLock != null && !isMacOS()) {

            capsLock = !capsLock;

            if (pressHandlerRegistration != null) {
                showCapsLockStatus(capsLock);
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        capsLock = null;

        if (pressHandlerRegistration != null) {
            showCapsLockStatus(false);
        }

        super.onBlur(event);
    }

    protected void showCapsLockStatus(boolean isCapsLock) {
        if (capslockIndicator instanceof CapsLockChangeHandler) {
            ((CapsLockChangeHandler) capslockIndicator).showCapsLockStatus(isCapsLock);
        }
    }
}