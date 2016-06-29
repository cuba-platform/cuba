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
import com.haulmont.cuba.gui.components.DatePicker;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatePickerLoader extends AbstractFieldLoader<DatePicker> {
    protected static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public void createComponent() {
        resultComponent = (DatePicker) factory.createComponent(DatePicker.NAME);
        loadId(resultComponent, element);

        loadRangeStart();
        loadRangeEnd();

        loadResolution();
    }

    protected void loadResolution() {
        final String resolution = element.attributeValue("resolution");
        if (StringUtils.isNotEmpty(resolution)) {
            DatePicker.Resolution res = DatePicker.Resolution.valueOf(resolution);
            resultComponent.setResolution(res);
        }
    }

    protected void loadRangeStart() {
        String rangeStart = element.attributeValue("rangeStart");
        if (StringUtils.isNotEmpty(rangeStart)) {
            try {
                SimpleDateFormat rangeDF = new SimpleDateFormat(DATE_PATTERN);
                resultComponent.setRangeStart(rangeDF.parse(rangeStart));
            } catch (ParseException e) {
                throw new GuiDevelopmentException(
                        "'rangeStart' parsing error for date picker: " +
                                rangeStart, context.getFullFrameId(), "DatePicker ID", resultComponent.getId());
            }
        }
    }

    protected void loadRangeEnd() {
        String rangeEnd = element.attributeValue("rangeEnd");
        if (StringUtils.isNotEmpty(rangeEnd)) {
            try {
                SimpleDateFormat rangeDF = new SimpleDateFormat(DATE_PATTERN);
                resultComponent.setRangeEnd(rangeDF.parse(rangeEnd));
            } catch (ParseException e) {
                throw new GuiDevelopmentException(
                        "'rangeEnd' parsing error for date picker: " +
                                rangeEnd, context.getFullFrameId(), "DatePicker ID", resultComponent.getId());
            }
        }
    }
}
