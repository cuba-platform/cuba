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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.DatePicker;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.web.widgets.CubaDatePicker;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.InlineDateField;

import javax.inject.Inject;
import java.time.*;
import java.util.Date;

public class WebDatePicker<V> extends WebV8AbstractField<InlineDateField, LocalDate, V> implements DatePicker<V> {

    @Inject
    protected DateTimeTransformations dateTimeTransformations;

    protected Resolution resolution = Resolution.DAY;
    protected Datatype<V> datatype;
    protected V rangeStart;
    protected V rangeEnd;

    public WebDatePicker() {
        this.component = new CubaDatePicker();

        attachValueChangeListener(component);
    }

    @Inject
    public void setMessages(Messages messages) {
        component.setDateOutOfRangeMessage(messages.getMainMessage("datePicker.dateOutOfRangeMessage"));
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        Preconditions.checkNotNullArgument(resolution);

        this.resolution = resolution;
        DateResolution vResolution = WebWrapperUtils.convertDateResolution(resolution);
        component.setResolution(vResolution);
    }

    // VAADIN8: gg, need to use?
    /*protected boolean checkRange(Date value) {
        if (updatingInstance) {
            return true;
        }

        if (value != null) {
            if (component.getRangeStart() != null && value.before(component.getRangeStart())) {
                handleDateOutOfRange(value);
                return false;
            }

            if (component.getRangeEnd() != null && value.after(component.getRangeEnd())) {
                handleDateOutOfRange(value);
                return false;
            }
        }

        return true;
    }*/

    // VAADIN8: gg, need to use?
    /*protected void handleDateOutOfRange(Date value) {
        if (getFrame() != null) {
            Messages messages = AppBeans.get(Messages.NAME);
            getFrame().showNotification(messages.getMainMessage("datePicker.dateOutOfRangeMessage"),
                    Frame.NotificationType.TRAY);
        }

        updatingInstance = true;
        try {
            component.setValue(internalValue);
        } finally {
            updatingInstance = false;
        }
    }*/

    @Override
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;
            DataAwareComponentsTools dataAwareComponentsTools = beanLocator.get(DataAwareComponentsTools.class);
            dataAwareComponentsTools.setupDateRange(this, entityValueSource);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected V convertToModel(LocalDate componentRawValue) throws ConversionException {
        if (componentRawValue == null) {
            return null;
        }

        LocalDateTime localDateTime = LocalDateTime.of(componentRawValue, LocalTime.MIDNIGHT);

        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaProperty metaProperty = ((EntityValueSource) valueSource).getMetaPropertyPath().getMetaProperty();
            return (V) convertFromLocalDateTime(localDateTime, metaProperty.getRange().asDatatype().getJavaClass());
        }
        return (V) convertFromLocalDateTime(localDateTime, datatype == null ? Date.class : datatype.getJavaClass());
    }

    @Override
    protected LocalDate convertToPresentation(V modelValue) throws ConversionException {
        if (modelValue == null) {
            return null;
        }
        return convertToLocalDateTime(modelValue).toLocalDate();
    }

    protected LocalDateTime convertToLocalDateTime(Object date) {
        Preconditions.checkNotNullArgument(date);
        ZonedDateTime zonedDateTime = dateTimeTransformations.transformToZDT(date);
        return zonedDateTime.toLocalDateTime();

    }

    protected Object convertFromLocalDateTime(LocalDateTime localDateTime, Class javaType) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return dateTimeTransformations.transformFromZDT(zonedDateTime, javaType);
    }

    @Override
    public V getRangeStart() {
        return rangeStart;
    }

    @Override
    public void setRangeStart(V value) {
        this.rangeStart = value;
        component.setRangeStart(value == null ? null : convertToLocalDateTime(rangeStart).toLocalDate());
    }

    @Override
    public V getRangeEnd() {
        return rangeEnd;
    }

    @Override
    public void setRangeEnd(V value) {
        this.rangeEnd = value;
        component.setRangeEnd(value == null ? null : convertToLocalDateTime(rangeEnd).toLocalDate());
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
    public Datatype<V> getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        this.datatype = datatype;
    }
}