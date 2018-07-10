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

import com.haulmont.bali.util.DateTimeUtils;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.web.widgets.CubaTimeField;
import com.haulmont.cuba.web.widgets.client.timefield.TimeResolution;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.widgets.CubaMaskedTextField;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

public class WebTimeField extends WebV8AbstractField<CubaTimeField, LocalTime, Date>
        implements TimeField, InitializingBean {

    protected Resolution resolution = Resolution.MIN;

    public WebTimeField() {
        component = new CubaTimeField();

        attachValueChangeListener(component);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserSessionSource userSessionSource = applicationContext.getBean(UserSessionSource.class);
        String timeFormat = Datatypes.getFormatStringsNN(userSessionSource.getLocale()).getTimeFormat();
        setFormat(timeFormat);
    }

    public boolean isAmPmUsed() {
        // FIXME: gg, actually, not working
        return component.getTimeFormat().contains("a");
    }

    @Override
    protected Date convertToModel(LocalTime componentRawValue) throws ConversionException {
        if (componentRawValue == null) {
            return null;
        }

        Date date = DateTimeUtils.asDate(componentRawValue);

        ValueSource<Date> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaPropertyPath metaPropertyPath = ((DatasourceValueSource) valueSource).getMetaPropertyPath();
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
            if (metaProperty != null) {
                Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
                if (javaClass.equals(java.sql.Time.class)) {
                    return new Time(date.getTime());
                }

                if (javaClass.equals(java.sql.Date.class)) {
                    LoggerFactory.getLogger(WebTimeField.class).warn("Do not use java.sql.Date with time field");
                    return new java.sql.Date(date.getTime());
                }
            }
        }

        return date;
    }

    @Override
    protected LocalTime convertToPresentation(Date modelValue) throws ConversionException {
        return modelValue != null ? DateTimeUtils.asLocalTime(modelValue) : null;
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
        Preconditions.checkNotNullArgument(resolution);

        this.resolution = resolution;
        TimeResolution vResolution = WebWrapperUtils.convertTimeResolution(resolution);
        component.setResolution(vResolution);
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