/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.calendar;

import com.haulmont.cuba.web.widgets.client.calendar.schedule.CubaSimpleDayCell;
import com.vaadin.v7.client.ui.VCalendar;
import com.vaadin.v7.client.ui.calendar.schedule.SimpleDayCell;

import java.util.Date;

public class CubaCalendarWidget extends VCalendar {

    /*
     * We must also handle the special case when the event lasts exactly for 24
     * hours, thus spanning two days e.g. from 1.1.2001 00:00 to 2.1.2001 00:00.
     * That special case still should span one day when rendered.
     */
    @SuppressWarnings("deprecation")
    // Date methods are not deprecated in GWT
    @Override
    protected boolean isEventInDayWithTime(Date from, Date to, Date date, Date endTime, boolean isAllDay) {
        return (isAllDay || !(to.compareTo(date) == 0
                && from.compareTo(to) != 0 && isMidnight(endTime)));
    }

    @Override
    protected SimpleDayCell createSimpleDayCell(int y, int x) {
        return new CubaSimpleDayCell(this, y, x);
    }
}
