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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.datepicker.CubaDatePickerState;
import com.vaadin.ui.InlineDateField;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

public class CubaDatePicker extends InlineDateField {

    protected static final FastDateFormat RANGE_FORMATTER = FastDateFormat.getInstance("yyyy/MM/dd HH:mm:ss");

    public CubaDatePicker() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
    }

    @Override
    public void setRangeStart(Date startDate) {
        super.setRangeStart(startDate);

        getState().textualRangeStart = RANGE_FORMATTER.format(startDate);
    }

    @Override
    public void setRangeEnd(Date endDate) {
        super.setRangeEnd(endDate);

        getState().textualRangeEnd = RANGE_FORMATTER.format(endDate);
    }

    @Override
    protected CubaDatePickerState getState() {
        return (CubaDatePickerState) super.getState();
    }
}