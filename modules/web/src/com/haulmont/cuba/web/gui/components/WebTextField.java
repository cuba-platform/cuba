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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.web.widgets.CubaTextField;
import com.vaadin.v7.event.FieldEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;

public class WebTextField extends WebAbstractTextField<CubaTextField> implements TextField {

    protected Datatype datatype;
    protected Formatter formatter;

    protected boolean trimming = true;
    protected FieldEvents.TextChangeListener textChangeListener;
    protected ShortcutListener enterShortcutListener;

    public WebTextField() {
    }

    @Override
    protected CubaTextField createTextFieldImpl() {
        return new CubaTextField();
    }

    @Override
    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
        if (datatype == null) {
            initFieldConverter();
        } else {
            component.setConverter(new TextFieldStringToDatatypeConverter(datatype));
        }
    }

    @Override
    public CaseConversion getCaseConversion() {
        return CaseConversion.valueOf(component.getCaseConversion().name());
    }

    @Override
    public void setCaseConversion(CaseConversion caseConversion) {
        com.haulmont.cuba.web.widgets.CaseConversion widgetCaseConversion =
                com.haulmont.cuba.web.widgets.CaseConversion.valueOf(caseConversion.name());
        component.setCaseConversion(widgetCaseConversion);
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
    protected Datatype getActualDatatype() {
        if (metaProperty != null) {
            return metaProperty.getRange().isDatatype() ? metaProperty.getRange().asDatatype() : null;
        } else if (datatype != null) {
            return datatype;
        } else {
            return Datatypes.getNN(String.class);
        }
    }

    @Override
    public boolean isTrimming() {
        return trimming;
    }

    @Override
    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }

    @Override
    public String getInputPrompt() {
        return component.getInputPrompt();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        component.setInputPrompt(inputPrompt);
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }

    @Override
    public String getRawValue() {
        return component.getValue();
    }

    @Override
    public void selectAll() {
        component.selectAll();
    }

    @Override
    public void setSelectionRange(int pos, int length) {
        component.setSelectionRange(pos, length);
    }

    @Override
    public void addTextChangeListener(TextChangeListener listener) {
        getEventRouter().addListener(TextChangeListener.class, listener);

        if (textChangeListener == null) {
            textChangeListener = (FieldEvents.TextChangeListener) e -> {
                TextChangeEvent event = new TextChangeEvent(this, e.getText(), e.getCursorPosition());

                getEventRouter().fireEvent(TextChangeListener.class, TextChangeListener::textChange, event);
            };
            component.addTextChangeListener(textChangeListener);
        }
    }

    @Override
    public void removeTextChangeListener(TextChangeListener listener) {
        getEventRouter().removeListener(TextChangeListener.class, listener);

        if (textChangeListener != null && !getEventRouter().hasListeners(TextChangeListener.class)) {
            component.removeTextChangeListener(textChangeListener);
            textChangeListener = null;
        }
    }

    @Override
    public void setTextChangeTimeout(int timeout) {
        component.setTextChangeTimeout(timeout);
    }

    @Override
    public int getTextChangeTimeout() {
        return component.getTextChangeTimeout();
    }

    @Override
    public TextChangeEventMode getTextChangeEventMode() {
        return WebWrapperUtils.toTextChangeEventMode(component.getTextChangeEventMode());
    }

    @Override
    public void setTextChangeEventMode(TextChangeEventMode mode) {
        component.setTextChangeEventMode(WebWrapperUtils.toVaadinTextChangeEventMode(mode));
    }

    @Override
    public void addEnterPressListener(EnterPressListener listener) {
        getEventRouter().addListener(EnterPressListener.class, listener);

        if (enterShortcutListener == null) {
            enterShortcutListener = new ShortcutListener("", KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object sender, Object target) {
                    EnterPressEvent event = new EnterPressEvent(WebTextField.this);
                    getEventRouter().fireEvent(EnterPressListener.class, EnterPressListener::enterPressed, event);
                }
            };
            component.addShortcutListener(enterShortcutListener);
        }
    }

    @Override
    public void removeEnterPressListener(EnterPressListener listener) {
        getEventRouter().removeListener(EnterPressListener.class, listener);

        if (enterShortcutListener != null && !getEventRouter().hasListeners(EnterPressListener.class)) {
            component.removeShortcutListener(enterShortcutListener);
        }
    }
}