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

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.components.calendar.EntityCalendarEventProvider;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarLoader extends AbstractComponentLoader<Calendar> {
    protected static final String DATE_PATTERN_DAY = "yyyy-MM-dd";
    protected static final String DATE_PATTERN_MIN = "yyyy-MM-dd hh:mm";

    protected static final String TIME_FORMAT_12H = "12H";
    protected static final String TIME_FORMAT_24H = "24H";

    @Override
    public void createComponent() {
        resultComponent = (Calendar) factory.createComponent(Calendar.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadIcon(resultComponent, element);

        loadEditable(resultComponent, element);
        loadDatasource(resultComponent, element);
        loadTimeFormat(resultComponent, element);
        loadEndDate(resultComponent, element);
        loadStartDate(resultComponent, element);
    }

    protected void loadDatasource(Calendar component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            CollectionDatasource ds = (CollectionDatasource) context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException(String.format("Datasource '%s' is not defined", datasource),
                        getContext().getFullFrameId(), "Component ID", component.getId());
            }

            component.setDatasource(ds);

            if (component.getEventProvider() instanceof EntityCalendarEventProvider) {
                EntityCalendarEventProvider eventProvider = (EntityCalendarEventProvider) component.getEventProvider();

                String startDateProperty = element.attributeValue("startDateProperty");
                if (StringUtils.isNotEmpty(startDateProperty)) {
                    eventProvider.setStartDateProperty(startDateProperty);
                }

                String endDateProperty = element.attributeValue("endDateProperty");
                if (StringUtils.isNotEmpty(endDateProperty)) {
                    eventProvider.setEndDateProperty(endDateProperty);
                }

                String captionProperty = element.attributeValue("captionProperty");
                if (StringUtils.isNotEmpty(captionProperty)) {
                    eventProvider.setCaptionProperty(captionProperty);
                }

                String descriptionProperty = element.attributeValue("descriptionProperty");
                if (StringUtils.isNotEmpty(descriptionProperty)) {
                    eventProvider.setDescriptionProperty(descriptionProperty);
                }

                String styleNameProperty = element.attributeValue("stylenameProperty");
                if (StringUtils.isNotEmpty(styleNameProperty)) {
                    eventProvider.setStyleNameProperty(styleNameProperty);
                }

                String allDayProperty = element.attributeValue("isAllDayProperty");
                if (StringUtils.isNotEmpty(allDayProperty)) {
                    eventProvider.setAllDayProperty(allDayProperty);
                }
            }
        }
    }

    protected void loadStartDate(Calendar resultComponent, Element element) {
        String startDate = element.attributeValue("startDate");
        if (StringUtils.isNotEmpty(startDate)) {
            try {
                resultComponent.setStartDate(parseDateOrDateTime(startDate));
            } catch (ParseException e) {
                throw new GuiDevelopmentException(
                        "'startDate' parsing error for calendar: " +
                                startDate, context.getFullFrameId(), "Calendar ID", resultComponent.getId());
            }
        }
    }

    protected void loadEndDate(Calendar resultComponent, Element element) {
        String endDate = element.attributeValue("endDate");
        if (StringUtils.isNotEmpty(endDate)) {
            try {
                resultComponent.setEndDate(parseDateOrDateTime(endDate));
            } catch (ParseException e) {
                throw new GuiDevelopmentException(
                        "'endDate' parsing error for calendar: " +
                                endDate, context.getFullFrameId(), "Calendar ID", resultComponent.getId());
            }
        }
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

    protected void loadTimeFormat(Calendar resultComponent, Element element) {
        String timeFormat = element.attributeValue("timeFormat");
        if (StringUtils.isNotEmpty(timeFormat)) {
            if (timeFormat.equals(TIME_FORMAT_12H)) {
                resultComponent.setTimeFormat(Calendar.TimeFormat.FORMAT_12H);
            } else if (timeFormat.equals(TIME_FORMAT_24H)) {
                resultComponent.setTimeFormat(Calendar.TimeFormat.FORMAT_24H);
            } else {
                throw new GuiDevelopmentException("'timeFormat' must be '12H' or '24H'",
                        context.getFullFrameId(), "Calendar ID", resultComponent.getId());
            }
        }
    }
}
