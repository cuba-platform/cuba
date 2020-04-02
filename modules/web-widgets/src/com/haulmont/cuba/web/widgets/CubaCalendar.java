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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.calendar.CubaCalendarEventId;
import com.haulmont.cuba.web.widgets.client.calendar.CubaCalendarServerRpc;
import com.vaadin.util.ReflectTools;
import com.vaadin.v7.ui.Calendar;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.EventListener;
import java.util.EventObject;

public class CubaCalendar extends Calendar {
    protected String[] dayNamesShort;
    protected String[] monthNamesShort;

    public CubaCalendar() {
        super();

        registerRpc(new CubaCalendarServerRpc() {
            @Override
            public void dayClick(Date date) {
                fireDayClickEvent(date);
            }
        });
    }

    @Override
    public String[] getDayNamesShort() {
        if (dayNamesShort != null) {
            return dayNamesShort;
        } else {
            return super.getDayNamesShort();
        }
    }

    public void setDayNamesShort(String[] dayNamesShort) {
        this.dayNamesShort = dayNamesShort;
    }

    @Override
    public String[] getMonthNamesShort() {
        if (monthNamesShort != null) {
            return monthNamesShort;
        } else {
            return super.getMonthNamesShort();
        }
    }

    public void setMonthNamesShort(String[] monthNamesShort) {
        this.monthNamesShort = monthNamesShort;
    }

    public int getFirstDayOfWeek() {
        return currentCalendar.getFirstDayOfWeek();
    }

    public void setDayClickHandler(DayClickHandler dayClickHandler) {
        setHandler(CubaCalendarEventId.DAYCLICK,
                CubaCalendarDayClickEvent.class,
                dayClickHandler,
                DayClickHandler.method);
    }

    protected void fireDayClickEvent(Date date) {
        fireEvent(new CubaCalendarDayClickEvent(this, date));
    }

    public interface DayClickHandler extends EventListener {

        Method method = ReflectTools.findMethod(
                DayClickHandler.class, "onDayClick", CubaCalendarDayClickEvent.class);

        void onDayClick(CubaCalendarDayClickEvent event);
    }

    public static class CubaCalendarDayClickEvent extends EventObject {

        protected Date date;

        public CubaCalendarDayClickEvent(Calendar calendar, Date date) {
            super(calendar);

            this.date = date;
        }

        public Date getDate() {
            return date;
        }
    }
}