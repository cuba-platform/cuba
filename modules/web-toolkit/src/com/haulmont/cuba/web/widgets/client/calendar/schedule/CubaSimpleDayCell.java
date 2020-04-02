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

package com.haulmont.cuba.web.widgets.client.calendar.schedule;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.haulmont.cuba.web.widgets.client.calendar.CubaCalendarWidget;
import com.vaadin.client.WidgetUtil;
import com.vaadin.v7.client.ui.VCalendar;
import com.vaadin.v7.client.ui.calendar.schedule.SimpleDayCell;

import java.util.Date;

public class CubaSimpleDayCell extends SimpleDayCell {

    public CubaSimpleDayCell(VCalendar calendar, int row, int cell) {
        super(calendar, row, cell);

        sinkEvents(Event.ONCLICK);
    }

    @Override
    protected boolean isEndDate(Date date, Date to, boolean allDay) {
        return super.isEndDate(date, to, allDay)
                || !allDay && getPreviousDate(to).compareTo(date) == 0;
    }

    protected Date getPreviousDate(Date date) {
        Date prev = new Date(date.getTime());
        CalendarUtil.addDaysToDate(prev, -1);
        return prev;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        // if not click event or some other event was fired
        if (event.getTypeInt() != Event.ONCLICK) {
            return;
        }

        // if day cell has expanded events
        if (getElement().getClassName().contains("scrollable")) {
            return;
        }

        Element target = Element.as(event.getEventTarget());
        Widget widget = WidgetUtil.findWidget(target);
        if ((widget instanceof CubaSimpleDayCell) || target.getClassName().contains("spacer")) {
            CubaCalendarWidget cubaCalendar = (CubaCalendarWidget) calendar;
            if (cubaCalendar.getDayClickListener() != null) {
                cubaCalendar.getDayClickListener().accept(getDate());
            }
        }
    }
}
