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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFieldLoader extends AbstractFieldLoader<DateField> {
    protected static final String DATE_PATTERN_DAY = "yyyy-MM-dd";
    protected static final String DATE_PATTERN_MIN = "yyyy-MM-dd HH:mm";

    @Override
    public void createComponent() {
        resultComponent = factory.create(DateField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadTabIndex(resultComponent, element);

        final String resolutionStr = element.attributeValue("resolution");
        String dateFormat = element.attributeValue("dateFormat");
        String mainDateFormat = null;
        DateField.Resolution resolution = null;
        if (StringUtils.isNotEmpty(resolutionStr)) {
            // We use resolution to get possible date time format
            resolution = DateField.Resolution.valueOf(resolutionStr);
            if (dateFormat == null) {
                switch (resolution) {
                    case YEAR:
                    case MONTH:
                    case DAY:
                        mainDateFormat = "dateFormat";
                        break;
                    case HOUR:
                    case MIN:
                    case SEC:
                        mainDateFormat = "dateTimeFormat";
                        break;
                }
            }
        }

        String formatStr = null;
        if (StringUtils.isNotEmpty(dateFormat)) {
            formatStr = loadResourceString(dateFormat);
        } else if (StringUtils.isNotEmpty(mainDateFormat)) {
            formatStr = getMessages().getMainMessage(mainDateFormat);
        }
        if (StringUtils.isNotEmpty(formatStr)) {
            resultComponent.setDateFormat(formatStr);
        }

        // Resolution must be set after date format to prevent overriding the time field mask
        if (resolution != null) {
            resultComponent.setResolution(resolution);
        }

        loadDatatype(resultComponent, element);

        loadBuffered(resultComponent, element);

        loadRangeStart(resultComponent, element);
        loadRangeEnd(resultComponent, element);
        loadTimeMode(resultComponent, element);

        loadAutofill(resultComponent, element);
    }

    protected void loadRangeStart(DateField resultComponent, Element element) {
        String rangeStart = element.attributeValue("rangeStart");
        if (StringUtils.isNotEmpty(rangeStart)) {
            try {
                Date date = parseDateOrDateTime(rangeStart);
                resultComponent.setRangeStart(getDateTimeTransformations().
                        transformToType(date, getJavaType(resultComponent), resultComponent.getZoneId()));
            } catch (ParseException e) {
                throw new GuiDevelopmentException("'rangeStart' parsing error for date picker: " +
                        rangeStart, context, "DateField ID", resultComponent.getId());
            }
        }
    }

    protected void loadRangeEnd(DateField resultComponent, Element element) {
        String rangeEnd = element.attributeValue("rangeEnd");
        if (StringUtils.isNotEmpty(rangeEnd)) {
            try {
                Date date = parseDateOrDateTime(rangeEnd);
                resultComponent.setRangeEnd(getDateTimeTransformations().
                        transformToType(date, getJavaType(resultComponent), resultComponent.getZoneId()));
            } catch (ParseException e) {
                throw new GuiDevelopmentException("'rangeEnd' parsing error for date picker: " +
                        rangeEnd, context, "DateField ID", resultComponent.getId());
            }
        }
    }

    protected void loadTimeMode(DateField resultComponent, Element element) {
        String timeMode = element.attributeValue("timeMode");
        if (StringUtils.isNotEmpty(timeMode)) {
            resultComponent.setTimeMode(TimeField.TimeMode.valueOf(timeMode));
        }
    }

    protected DateTimeTransformations getDateTimeTransformations() {
        return beanLocator.get(DateTimeTransformations.NAME);
    }

    protected Date parseDateOrDateTime(String value) throws ParseException {
        SimpleDateFormat rangeDF;
        if (value.length() == 10) {
            rangeDF = new SimpleDateFormat(DATE_PATTERN_DAY);
        } else {
            rangeDF = new SimpleDateFormat(DATE_PATTERN_MIN);
        }
        return rangeDF.parse(value);
    }

    protected Class getJavaType(DateField resultComponent) {
        ValueSource valueSource = resultComponent.getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaProperty metaProperty = ((EntityValueSource) valueSource).getMetaPropertyPath().getMetaProperty();
            return metaProperty.getRange().asDatatype().getJavaClass();
        }

        Datatype datatype = resultComponent.getDatatype();
        return datatype == null ? Date.class : datatype.getJavaClass();
    }

    protected void loadAutofill(DateField resultComponent, Element element) {
        String autofill = element.attributeValue("autofill");
        if (StringUtils.isNotEmpty(autofill)) {
            resultComponent.setAutofill(Boolean.parseBoolean(autofill));
        }
    }
}