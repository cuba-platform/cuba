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
package com.haulmont.cuba.gui.components;

import java.util.Date;
import java.util.TimeZone;

public interface DateField extends Field {
    String NAME = "dateField";

    enum Resolution {
        SEC,
        MIN,
        HOUR,
        DAY,
        MONTH,
        YEAR
    }

    Resolution getResolution();
    void setResolution(Resolution resolution);

    String getDateFormat();
    void setDateFormat(String dateFormat);

    TimeZone getTimeZone();
    void setTimeZone(TimeZone timeZone);

    /**
     * Set start of range. If the value is set before this
     * date, the component will not validate.
     *
     * @param rangeStart allowed start of range
     */
    void setRangeStart(Date rangeStart);
    /**
     * Return start of range for a certain resolution.
     *
     * @return start of allowed range
     */
    Date getRangeStart();

    /**
     * Set end of range. If the value is set after this
     * date, the component will not validate.
     *
     * @param rangeEnd end of allowed range
     */
    void setRangeEnd(Date rangeEnd);
    /**
     * Return end of range for a certain resolution.
     *
     * @return end of allowed range
     */
    Date getRangeEnd();

    @SuppressWarnings("unchecked")
    @Override
    Date getValue();
}