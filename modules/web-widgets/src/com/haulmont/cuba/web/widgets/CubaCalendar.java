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

import com.vaadin.v7.ui.Calendar;

public class CubaCalendar extends Calendar {
    protected String[] dayNamesShort;
    protected String[] monthNamesShort;

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
}