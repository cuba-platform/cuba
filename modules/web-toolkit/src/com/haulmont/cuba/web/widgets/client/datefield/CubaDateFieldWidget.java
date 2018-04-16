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

package com.haulmont.cuba.web.widgets.client.datefield;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.client.textfield.CubaMaskedFieldWidget;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.v7.client.ui.VPopupCalendar;

public class CubaDateFieldWidget extends VPopupCalendar implements ShortcutActionHandler.ShortcutActionHandlerOwner {

    protected ShortcutActionHandler shortcutHandler;

    protected static final String EMPTY_FIELD_CLASS = "c-datefield-empty";

    public CubaDateFieldWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
    }

    @Override
    public void setTextFieldEnabled(boolean textFieldEnabled) {
        super.setTextFieldEnabled(textFieldEnabled);

        calendarToggle.getElement().setTabIndex(-1);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        // Always set -1 tab index for calendarToggle
        calendarToggle.setTabIndex(-1);
    }

    @Override
    protected void buildDate(boolean forceValid) {
        super.buildDate(forceValid);
        // Update valueBeforeEdit and send onChange
        // in case of selecting date using Calendar popup
//        vaadin8
//        getImpl().valueChange(false);
    }

    @Override
    public CubaMaskedFieldWidget getImpl() {
        return (CubaMaskedFieldWidget) super.getImpl();
    }

//    vaadin8
    /*@Override
    protected CubaMaskedFieldWidget createImpl() {
        CubaMaskedFieldWidget cubaMaskedFieldWidget = new CubaMaskedFieldWidget() {

            @Override
            protected boolean validateText(String text) {
                if (text.equals(nullRepresentation)) {
                    return true;
                }

                if (!super.validateText(text)) {
                    return false;
                }

                try {
                    getDateTimeService().parseDate(getText(), getFormatString(), lenient);
                } catch (Exception e) {
                    return false;
                }

                return true;
            }

            @Override
            public void valueChange(boolean blurred) {
                String newText = getText();
                if (!prompting && newText != null
                        && !newText.equals(valueBeforeEdit)) {
                    if (validateText(newText)) {
                        if (!newText.equals(nullRepresentation)) {
                            getElement().removeClassName(CubaDateFieldWidget.EMPTY_FIELD_CLASS);
                        }
                        CubaDateFieldWidget.this.onChange(null);

                        valueBeforeEdit = newText;
                    } else {
                        setText(valueBeforeEdit);
                    }
                }
            }
        };
        cubaMaskedFieldWidget.setImmediate(isImmediate());

        return cubaMaskedFieldWidget;
    }*/

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

    public void updateTextState() {
//        vaadin8
//        getImpl().updateTextState();
    }
}