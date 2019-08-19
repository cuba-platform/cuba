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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.DatePicker;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerLoader extends AbstractFieldLoader<DatePicker> {
    protected static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public void createComponent() {
        resultComponent = factory.create(DatePicker.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadTabIndex(resultComponent, element);

        loadRangeStart(resultComponent, element);
        loadRangeEnd(resultComponent, element);

        loadResolution(resultComponent, element);
        loadDatatype(resultComponent, element);

        loadBuffered(resultComponent, element);
    }

    protected void loadResolution(DatePicker resultComponent, Element element) {
        final String resolution = element.attributeValue("resolution");
        if (StringUtils.isNotEmpty(resolution)) {
            DatePicker.Resolution res = DatePicker.Resolution.valueOf(resolution);
            resultComponent.setResolution(res);
        }
    }

    protected void loadRangeStart(DatePicker resultComponent, Element element) {
        String rangeStart = element.attributeValue("rangeStart");
        if (StringUtils.isNotEmpty(rangeStart)) {
            try {
                Date date = parseDate(rangeStart);
                resultComponent.setRangeStart(getDateTimeTransformations().
                        transformToType(date, getJavaType(resultComponent), null));
            } catch (ParseException e) {
                throw new GuiDevelopmentException(
                        "'rangeStart' parsing error for date picker: " +
                                rangeStart, context.getFullFrameId(), "DatePicker ID", resultComponent.getId());
            }
        }
    }

    protected void loadRangeEnd(DatePicker resultComponent, Element element) {
        String rangeEnd = element.attributeValue("rangeEnd");
        if (StringUtils.isNotEmpty(rangeEnd)) {
            try {
                Date date = parseDate(rangeEnd);
                resultComponent.setRangeEnd(getDateTimeTransformations().
                        transformToType(date, getJavaType(resultComponent), null));
            } catch (ParseException e) {
                throw new GuiDevelopmentException(
                        "'rangeEnd' parsing error for date picker: " +
                                rangeEnd, context.getFullFrameId(), "DatePicker ID", resultComponent.getId());
            }
        }
    }

    protected DateTimeTransformations getDateTimeTransformations() {
        return beanLocator.get(DateTimeTransformations.NAME);
    }

    protected Date parseDate(String rangeStart) throws ParseException {
        SimpleDateFormat rangeDF = new SimpleDateFormat(DATE_PATTERN);
        return rangeDF.parse(rangeStart);
    }

    protected Class getJavaType(DatePicker resultComponent) {
        ValueSource valueSource = resultComponent.getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaProperty metaProperty = ((EntityValueSource) valueSource).getMetaPropertyPath().getMetaProperty();
            return metaProperty.getRange().asDatatype().getJavaClass();
        }

        Datatype datatype = resultComponent.getDatatype();
        return datatype == null ? Date.class : datatype.getJavaClass();
    }
}