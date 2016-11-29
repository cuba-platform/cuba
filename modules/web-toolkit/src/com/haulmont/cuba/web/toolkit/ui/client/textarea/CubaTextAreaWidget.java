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
 */

package com.haulmont.cuba.web.toolkit.ui.client.textarea;

import com.google.gwt.dom.client.Element;
import com.vaadin.client.ui.VTextArea;

public class CubaTextAreaWidget extends VTextArea {

    protected String caseConversion = "NONE";

    protected CubaTextAreaWidget() {
        super();

        addInputHandler(getElement());
    }

    protected native void addInputHandler(Element elementID)/*-{
        var temp = this;  // hack to hold on to 'this' reference

        var listener = $entry(function (e) {
            temp.@com.haulmont.cuba.web.toolkit.ui.client.textarea.CubaTextAreaWidget::handleInput()();
        });

        if (elementID.addEventListener) {
            elementID.addEventListener("input", listener, false);
        } else {
            elementID.attachEvent("input", listener);
        }
    }-*/;

    public void handleInput() {
        String text = getText();

        if ("UPPER".equals(caseConversion)) {
            text = text.toUpperCase();
        } else if ("LOWER".equals(caseConversion)) {
            text = text.toLowerCase();
        } else {
            return;
        }

        int cursorPos = getCursorPos();
        setText(text);
        setCursorPos(cursorPos);
    }

    public void setCaseConversion(String caseConversion) {
        this.caseConversion = caseConversion;
    }
}