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
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.web.widgets.CubaTimeFieldWrapper;
import com.vaadin.data.HasValue;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.time.LocalTime;
import java.util.Date;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.web.gui.components.WebWrapperUtils.*;

public class WebTimeField<V> extends WebV8AbstractField<CubaTimeFieldWrapper, LocalTime, V>
        implements TimeField<V>, InitializingBean {

    @Inject
    protected DateTimeTransformations dateTimeTransformations;
    protected DataAwareComponentsTools dataAwareComponentsTools;

    protected Datatype<V> datatype;

    public WebTimeField() {
        component = createComponent();
        component.addValueChangeListener(this::componentValueChanged);
    }

    @Inject
    public void setDataAwareComponentsTools(DataAwareComponentsTools dataAwareComponentsTools) {
        this.dataAwareComponentsTools = dataAwareComponentsTools;
    }

    @Override
    public void afterPropertiesSet() {
        UserSessionSource userSessionSource = beanLocator.get(UserSessionSource.NAME);
        FormatStringsRegistry formatStringsRegistry = beanLocator.get(FormatStringsRegistry.NAME);
        String timeFormat = formatStringsRegistry.getFormatStringsNN(userSessionSource.getLocale()).getTimeFormat();
        setFormat(timeFormat);
    }

    @Override
    public void setFormat(String format) {
        component.setTimeFormat(format);
    }

    @Override
    public String getFormat() {
        return component.getTimeFormat();
    }

    @Override
    public Resolution getResolution() {
        return fromVaadinTimeResolution(component.getResolution());
    }

    @Override
    public void setResolution(Resolution resolution) {
        checkNotNullArgument(resolution);

        component.setResolution(toVaadinTimeResolution(resolution));
    }

    @Override
    public Datatype<V> getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        dataAwareComponentsTools.checkValueSourceDatatypeMismatch(datatype, getValueSource());

        this.datatype = datatype;
    }

    @Override
    public boolean getShowSeconds() {
        return fromVaadinTimeResolution(component.getResolution()) == Resolution.SEC;
    }

    @Override
    public void setShowSeconds(boolean showSeconds) {
        setResolution(Resolution.SEC);
    }

    @Override
    public void focus() {
        component.focus();
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
    public void setTimeMode(TimeMode timeMode) {
        checkNotNullArgument("Time mode cannot be null");

        component.setTimeMode(toVaadinTimeMode(timeMode));
    }

    @Override
    public TimeMode getTimeMode() {
        return fromVaadinTimeMode(component.getTimeMode());
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    @Override
    protected LocalTime convertToPresentation(V modelValue) throws ConversionException {
        if (modelValue == null) {
            return null;
        }
        return dateTimeTransformations.transformToLocalTime(modelValue);
    }

    protected CubaTimeFieldWrapper createComponent() {
        return new CubaTimeFieldWrapper();
    }

    protected void componentValueChanged(HasValue.ValueChangeEvent<LocalTime> e) {
        if (e.isUserOriginated()) {
            V value;

            try {
                value = constructModelValue();

                setValueToPresentation(convertToPresentation(value));
            } catch (ConversionException ce) {
                LoggerFactory.getLogger(WebDateField.class)
                        .trace("Unable to convert presentation value to model", ce);

                setValidationError(ce.getLocalizedMessage());
                return;
            }

            V oldValue = internalValue;
            internalValue = value;

            if (!fieldValueEquals(value, oldValue)) {
                ValueChangeEvent<V> event = new ValueChangeEvent<>(this, oldValue, value, true);
                publish(ValueChangeEvent.class, event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected V constructModelValue() {
        LocalTime timeValue = component.getValue() != null
                ? component.getValue()
                : LocalTime.MIDNIGHT;

        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaProperty metaProperty = ((EntityValueSource) valueSource)
                    .getMetaPropertyPath().getMetaProperty();
            return (V) convertFromLocalTime(timeValue,
                    metaProperty.getRange().asDatatype().getJavaClass());
        }

        return (V) convertFromLocalTime(timeValue,
                datatype == null ? Date.class : datatype.getJavaClass());
    }

    protected Object convertFromLocalTime(LocalTime localTime, Class javaType) {
        return dateTimeTransformations.transformFromLocalTime(localTime, javaType);
    }
}