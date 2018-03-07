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

package com.haulmont.cuba.web.widgets.client.textfield;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.v7.client.ui.VTextField;

import java.util.Iterator;
import java.util.LinkedList;

public class CubaTextFieldWidget extends VTextField implements ShortcutActionHandler.ShortcutActionHandlerOwner {
    protected static final String PROMPT_STYLE = "prompt";
    protected static final String CUBA_DISABLED_OR_READONLY = "c-disabled-or-readonly";
    protected static final String CUBA_EMPTY_VALUE = "c-empty-value";

    protected static final String CASE_CONVERSION_MODE_NONE = "NONE";

    protected ShortcutActionHandler shortcutHandler;

    protected boolean readOnlyFocusable = false;
    protected String caseConversion = CASE_CONVERSION_MODE_NONE;

    public CubaTextFieldWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);

        addInputHandler(getElement());
    }

    protected native void addInputHandler(Element elementID)/*-{
        var temp = this;  // hack to hold on to 'this' reference

        var listener = $entry(function (e) {
            temp.@com.haulmont.cuba.web.widgets.client.textfield.CubaTextFieldWidget::handleInput()();
        });

        if (elementID.addEventListener) {
            elementID.addEventListener("input", listener, false);
        } else {
            elementID.attachEvent("input", listener);
        }
    }-*/;

    public void handleInput() {
        if (CASE_CONVERSION_MODE_NONE.equals(caseConversion))
            return;

        String text = applyCaseConversion(getText());

        int cursorPos = getCursorPos();
        setText(text);
        setCursorPos(cursorPos);
    }

    protected String applyCaseConversion(String text) {
        if ("UPPER".equals(caseConversion)) {
            return text.toUpperCase();
        } else if ("LOWER".equals(caseConversion)) {
            return text.toLowerCase();
        } else {
            return text;
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);

        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        this.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    @Override
    public void add(Widget w) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Iterator<Widget> iterator() {
        return new LinkedList<Widget>().iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return false;
    }

    /**
     * If {@code readOnlyFocusable} property is set then component is focusable
     * in readOnly mode
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        if (!readOnlyFocusable) {
            super.setReadOnly(readOnly);
        } else {
            getElement().setPropertyBoolean("readOnly", readOnly);
            String readOnlyStyle = "readonly";
            if (readOnly) {
                addStyleDependentName(readOnlyStyle);
            } else {
                removeStyleDependentName(readOnlyStyle);
            }
        }

        refreshEnabledOrReadonly();
    }

    public boolean isReadOnlyFocusable() {
        return readOnlyFocusable;
    }

    public void setReadOnlyFocusable(boolean readOnlyFocusable) {
        this.readOnlyFocusable = readOnlyFocusable;
        if (readOnlyFocusable && getTabIndex() == -1) {
            setTabIndex(0);
        }
    }

    @Override
    public void setText(String text) {
        String styleName = getStyleName();
        if (prompting) {
            super.setText(text);
        } else {
            String convertedText = applyCaseConversion(text);

            super.setText(convertedText);

            if (!convertedText.equals(text)) {
                valueChange(false);
            }
        }

        if ("".equals(text) || text == null) {
            addStyleName(CUBA_EMPTY_VALUE);
        } else {
            if (styleName.contains(PROMPT_STYLE)) {
                addStyleName(CUBA_EMPTY_VALUE);
            } else {
                removeStyleName(CUBA_EMPTY_VALUE);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        refreshEnabledOrReadonly();
    }

    protected void refreshEnabledOrReadonly() {
        if (!isEnabled() || isReadOnly()) {
            addStyleName(CUBA_DISABLED_OR_READONLY);
        } else {
            removeStyleName(CUBA_DISABLED_OR_READONLY);
        }
    }

    public void setCaseConversion(String caseConversion) {
        this.caseConversion = caseConversion;
    }
}