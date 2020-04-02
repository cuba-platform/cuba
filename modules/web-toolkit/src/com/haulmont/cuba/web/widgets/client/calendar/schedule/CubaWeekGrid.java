/*
 * Copyright (c) 2008-2020 Haulmont.
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

import com.vaadin.v7.client.ui.VCalendar;
import com.vaadin.v7.client.ui.calendar.schedule.DateCell;
import com.vaadin.v7.client.ui.calendar.schedule.WeekGrid;

import java.util.Date;

public class CubaWeekGrid extends WeekGrid {

    public CubaWeekGrid(VCalendar parent, boolean format24h) {
        super(parent, format24h);
    }

    @Override
    protected DateCell createDateCell(WeekGrid parent, Date date) {
        return new CubaDateCell(parent, date);
    }
}
