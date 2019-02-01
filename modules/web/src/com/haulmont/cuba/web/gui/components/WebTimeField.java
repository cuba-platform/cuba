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
import com.haulmont.cuba.web.widgets.CubaTimeField;
import com.haulmont.cuba.web.widgets.client.timefield.TimeResolution;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.time.LocalTime;
import java.util.Date;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebTimeField<V> extends WebV8AbstractField<CubaTimeField, LocalTime, V>
        implements TimeField<V>, InitializingBean {

    @Inject
    protected DateTimeTransformations dateTimeTransformations;

    protected Resolution resolution = Resolution.MIN;
    protected Datatype<V> datatype;

    protected DataAwareComponentsTools dataAwareComponentsTools;

    public WebTimeField() {
        component = new CubaTimeField();

        attachValueChangeListener(component);
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
    @SuppressWarnings("unchecked")
    protected V convertToModel(LocalTime componentRawValue) throws ConversionException {
        if (componentRawValue == null) {
            return null;
        }

        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaProperty metaProperty = ((EntityValueSource) valueSource).getMetaPropertyPath().getMetaProperty();
            return (V) dateTimeTransformations.transformFromLocalTime(componentRawValue,
                    metaProperty.getRange().asDatatype().getJavaClass());
        }

        return (V) dateTimeTransformations.transformFromLocalTime(componentRawValue,
                datatype == null ? Date.class : datatype.getJavaClass());
    }

    @Override
    protected LocalTime convertToPresentation(V modelValue) throws ConversionException {
        if (modelValue == null) {
            return null;
        }
        return dateTimeTransformations.transformToLocalTime(modelValue);
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
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        checkNotNullArgument(resolution);

        this.resolution = resolution;
        TimeResolution vResolution = WebWrapperUtils.convertTimeResolution(resolution);
        component.setResolution(vResolution);
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
        return resolution == Resolution.SEC;
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
}