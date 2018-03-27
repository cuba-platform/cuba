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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.compatibility.ResizeListenerWrapper;
import com.haulmont.cuba.web.widgets.CubaResizableTextAreaWrapper;
import com.haulmont.cuba.web.widgets.CubaTextArea;
import com.vaadin.v7.event.FieldEvents;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class WebResizableTextArea<V> extends WebAbstractTextArea<CubaTextArea, V> implements ResizableTextArea<V> {

    protected Datatype datatype;

    protected CubaResizableTextAreaWrapper wrapper;
    protected boolean settingsEnabled = true;

    protected FieldEvents.TextChangeListener textChangeListener;

    public WebResizableTextArea() {
        wrapper = new CubaResizableTextAreaWrapper(component);
        wrapper.addResizeListener((oldWidth, oldHeight, width, height) -> {
            ResizeEvent e = new ResizeEvent(this, oldWidth, width, oldHeight, height);
            getEventRouter().fireEvent(ResizeListener.class, ResizeListener::sizeChanged, e);
        });

        component.addValueChangeListener(event -> wrapper.markAsDirty());
    }

    @Override
    protected CubaTextArea createTextFieldImpl() {
        return new CubaTextArea() {
            @Override
            public void setComponentError(ErrorMessage componentError) {
                if (componentError instanceof UserError) {
                    super.setComponentError(componentError);
                } else {
                    wrapper.setComponentError(componentError);
                }
            }
        };
    }

    @Override
    public Component getComposition() {
        return wrapper;
    }

    @Override
    public boolean isResizable() {
        return getResizableDirection() != ResizeDirection.NONE;
    }

    @Override
    public void setResizable(boolean resizable) {
        ResizeDirection value = resizable ? ResizeDirection.BOTH : ResizeDirection.NONE;
        setResizableDirection(value);
    }

    @Override
    public void setHeight(String height) {
        wrapper.setHeight(height);
    }

    @Override
    public void setWidth(String width) {
        wrapper.setWidth(width);
    }

    @Override
    public void setCaption(String caption) {
        wrapper.setCaption(caption);
    }

    @Override
    public String getCaption() {
        return wrapper.getCaption();
    }

    @Override
    public String getDescription() {
        return wrapper.getDescription();
    }

    @Override
    public void setDescription(String description) {
        wrapper.setDescription(description);
    }

    @Override
    public String getContextHelpText() {
        return wrapper.getContextHelpText();
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        wrapper.setContextHelpText(contextHelpText);
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return wrapper.isContextHelpTextHtmlEnabled();
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        wrapper.setContextHelpTextHtmlEnabled(enabled);
    }

    @Override
    public boolean isRequired() {
        return wrapper.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        wrapper.setRequired(required);
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        wrapper.setEditable(editable);
    }

    @Override
    public void setRequiredMessage(String msg) {
        wrapper.setRequiredError(msg);
    }

    @Override
    public String getRequiredMessage() {
        return wrapper.getRequiredError();
    }

    @Override
    public void addResizeListener(ResizeListener resizeListener) {
        getEventRouter().addListener(ResizeListener.class, resizeListener);
    }

    @Override
    public void removeResizeListener(ResizeListener resizeListener) {
        getEventRouter().removeListener(ResizeListener.class, resizeListener);
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }

    @Override
    public void applySettings(Element element) {
        if (isSettingsEnabled() && isResizable()) {
            String width = element.attributeValue("width");
            String height = element.attributeValue("height");
            if (StringUtils.isNotEmpty(width) && StringUtils.isNotEmpty(height)) {
                setWidth(width);
                setHeight(height);
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled() || !isResizable()) {
            return false;
        }

        String width = String.valueOf(getWidth()) + wrapper.getWidthUnits().toString();
        String height = String.valueOf(getHeight()) + wrapper.getHeightUnits().toString();
        element.addAttribute("width", width);
        element.addAttribute("height", height);

        return true;
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
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
    public boolean isWordwrap() {
        return component.isWordwrap();
    }

    @Override
    public void setWordwrap(boolean wordwrap) {
        component.setWordwrap(wordwrap);
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
    public String getRawValue() {
        return component.getValue();
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
    public void setResizableDirection(ResizeDirection direction) {
        Preconditions.checkNotNullArgument(direction);
        wrapper.setResizableDirection(WebWrapperUtils.toVaadinResizeDirection(direction));
    }

    @Override
    public ResizeDirection getResizableDirection() {
        return WebWrapperUtils.toResizeDirection(wrapper.getResizableDirection());
    }
}