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
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.text.ParseException;
import java.util.Locale;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;

public abstract class WebAbstractTextArea<T extends com.vaadin.ui.TextArea, V>
        extends WebV8AbstractField<T, String, V>
        implements TextArea<V>, InitializingBean {

    protected Datatype<V> datatype;
    protected Locale locale;

    protected boolean trimming = true;

    @Override
    protected String convertToPresentation(V modelValue) throws ConversionException {
        // Vaadin TextField does not permit `null` value

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
    public void afterPropertiesSet() {
        UserSessionSource userSessionSource =
                applicationContext.getBean(UserSessionSource.NAME, UserSessionSource.class);

        this.locale = userSessionSource.getLocale();
    }

    @Override
    protected void componentValueChanged(String prevComponentValue, String newComponentValue, boolean isUserOriginated) {
        if (isUserOriginated) {
            fireTextChangeEvent(newComponentValue);
        }

        super.componentValueChanged(prevComponentValue, newComponentValue, isUserOriginated);
    }

    protected void fireTextChangeEvent(String newComponentValue) {
        // call it before value change due to compatibility with the previous versions
        TextChangeEvent event = new TextChangeEvent(this, newComponentValue, component.getCursorPosition());
        getEventRouter().fireEvent(TextChangeListener.class, TextChangeListener::textChange, event);
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
    public int getRows() {
        return component.getRows();
    }

    @Override
    public void setRows(int rows) {
        component.setRows(rows);
    }

    @Override
    public int getColumns() {
        // vaadin8
        return 0;
    }

    @Override
    public void setColumns(int columns) {
        // vaadin8
    }

    @Override
    public boolean isWordwrap() {
        return component.isWordWrap();
    }

    @Override
    public void setWordwrap(boolean wordwrap) {
        component.setWordWrap(wordwrap);
    }

    @Override
    public Datatype<V> getDatatype() {
        return null;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        this.datatype = datatype;
    }

    @Override
    public String getRawValue() {
        return component.getValue();
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
    public CaseConversion getCaseConversion() {
        return CaseConversion.NONE; // vaadin8
    }

    @Override
    public void setCaseConversion(CaseConversion caseConversion) {
        // vaadin8
    }

    @Override
    public void selectAll() {
        component.selectAll();
    }

    @Override
    public void setSelectionRange(int pos, int length) {
        component.setSelection(pos, length);
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
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            DataAwareGuiTools dataAwareGuiTools = applicationContext.getBean(DataAwareGuiTools.class);
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;

            dataAwareGuiTools.setupCaseConversion(this, entityValueSource);
            dataAwareGuiTools.setupMaxLength(this, entityValueSource);
        }
    }
}