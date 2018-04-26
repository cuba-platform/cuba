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
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.DataAwareGuiTools;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.widgets.CubaTextField;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.text.ParseException;
import java.util.Locale;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;

public class WebTextField<V> extends WebV8AbstractField<CubaTextField, String, V>
        implements TextField<V>, InitializingBean {

    protected Datatype<V> datatype;
    protected Formatter<? super V> formatter;

    protected boolean trimming = true;

    protected ShortcutListener enterShortcutListener;

    protected Locale locale;

    public WebTextField() {
        this.component = createTextFieldImpl();

        attachValueChangeListener(this.component);
    }

    @Override
    public void afterPropertiesSet() {
        UserSessionSource userSessionSource =
                applicationContext.getBean(UserSessionSource.NAME, UserSessionSource.class);

        this.locale = userSessionSource.getLocale();
    }

//    vaadin8
//    @Override
    // vaadin8 introduce convention for implementation factory methods
    protected CubaTextField createTextFieldImpl() {
        return new CubaTextField();
    }

    @Override
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            DataAwareGuiTools dataAwareGuiTools = applicationContext.getBean(DataAwareGuiTools.class);
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;

            dataAwareGuiTools.setupCaseConversion(this, entityValueSource);
            dataAwareGuiTools.setupMaxLength(this, entityValueSource);
        }
    }

    @Override
    protected String convertToPresentation(V modelValue) throws ConversionException {
        // Vaadin TextField does not permit `null` value

        if (formatter != null) {
            return nullToEmpty(formatter.format(modelValue));
        }

        if (datatype != null) {
            return nullToEmpty(datatype.format(modelValue, locale));
        }

        if (valueBinding != null
                && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            Datatype<V> propertyDataType = entityValueSource.getMetaPropertyPath().getRange().asDatatype();
            return nullToEmpty(propertyDataType.format(modelValue));
        }

        return nullToEmpty(super.convertToPresentation(modelValue));
    }

    @Override
    protected V convertToModel(String componentRawValue) throws ConversionException {
        String value = emptyToNull(componentRawValue);

        if (isTrimming()) {
            value = StringUtils.trimToNull(value);
        }

        if (datatype != null) {
            try {
                return datatype.parse(value, locale);
            } catch (ParseException e) {
                // vaadin8 localized message
                throw new ConversionException("Unable to convert value", e);
            }
        }

        if (valueBinding != null
                && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            Datatype<V> propertyDataType = entityValueSource.getMetaPropertyPath().getRange().asDatatype();
            try {
                return propertyDataType.parse(componentRawValue);
            } catch (ParseException e) {
                // vaadin8 localized message
                throw new ConversionException("Unable to convert value", e);
            }
        }

        return super.convertToModel(componentRawValue);
    }

    @Override
    protected void componentValueChanged(String prevComponentValue, String newComponentValue, boolean isUserOriginated) {
        if (isUserOriginated) {
            fireTextChangeEvent(newComponentValue);
        }

        super.componentValueChanged(prevComponentValue, newComponentValue, isUserOriginated);
    }

    @Override
    public Datatype<V> getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        this.datatype = datatype;
    }

    @Override
    public CaseConversion getCaseConversion() {
        return CaseConversion.NONE;
//        vaadin8
//        return CaseConversion.valueOf(component.getCaseConversion().name());
    }

    @Override
    public void setCaseConversion(CaseConversion caseConversion) {
//        vaadin8
//        com.haulmont.cuba.web.widgets.CaseConversion widgetCaseConversion =
//                com.haulmont.cuba.web.widgets.CaseConversion.valueOf(caseConversion.name());
//        component.setCaseConversion(widgetCaseConversion);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Formatter<V> getFormatter() {
        return (Formatter<V>) formatter;
    }

    @Override
    public void setFormatter(Formatter<? super V> formatter) {
        this.formatter = formatter;
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int maxLength) {
        component.setMaxLength(maxLength);
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
        return component.getPlaceholder();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        component.setPlaceholder(inputPrompt);
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
        component.setSelection(pos, length);
    }

    protected void fireTextChangeEvent(String newComponentValue) {
        // call it before value change due to compatibility with the previous versions
        TextChangeEvent event = new TextChangeEvent(this, newComponentValue, component.getCursorPosition());
        getEventRouter().fireEvent(TextChangeListener.class, TextChangeListener::textChange, event);
    }

    @Override
    public void addTextChangeListener(TextChangeListener listener) {
        getEventRouter().addListener(TextChangeListener.class, listener);
    }

    @Override
    public void removeTextChangeListener(TextChangeListener listener) {
        getEventRouter().removeListener(TextChangeListener.class, listener);
    }

    @Override
    public void setTextChangeTimeout(int timeout) {
        component.setValueChangeTimeout(timeout);
    }

    @Override
    public int getTextChangeTimeout() {
        return component.getValueChangeTimeout();
    }

    @Override
    public TextChangeEventMode getTextChangeEventMode() {
        return WebWrapperUtils.toTextChangeEventMode(component.getValueChangeMode());
    }

    @Override
    public void setTextChangeEventMode(TextChangeEventMode mode) {
        component.setValueChangeMode(WebWrapperUtils.toVaadinValueChangeEventMode(mode));
    }

    @Override
    public void addEnterPressListener(EnterPressListener listener) {
        getEventRouter().addListener(EnterPressListener.class, listener);

        if (enterShortcutListener == null) {
            enterShortcutListener = new ShortcutListenerDelegate("", KeyCode.ENTER, null)
                    .withHandler((sender, target) -> {
                        EnterPressEvent event = new EnterPressEvent(WebTextField.this);
                        getEventRouter().fireEvent(EnterPressListener.class, EnterPressListener::enterPressed, event);
                    });
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

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        // vaadin8
    }

    @Override
    public void discard() {
        // vaadin8
    }

    @Override
    public boolean isBuffered() {
        // vaadin8
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        // vaadin8
    }

    @Override
    public boolean isModified() {
        // vaadin8
        return false;
    }
}