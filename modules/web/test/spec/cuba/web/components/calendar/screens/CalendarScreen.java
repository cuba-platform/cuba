/*
 * Copyright (c) 2008-2019 Haulmont.
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

package spec.cuba.web.components.calendar.screens;

import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@UiController
@UiDescriptor("calendar-screen.xml")
public class CalendarScreen extends Screen {

    @Inject
    private Calendar calendarDefault;
    @Inject
    private Calendar<Date> calendarDate;
    @Inject
    private Calendar<Date> calendarDateTime;
    @Inject
    private Calendar<LocalDate> calendarLocalDate;
    @Inject
    private Calendar<LocalDateTime> calendarLocalDateTime;
    @Inject
    private Calendar<OffsetDateTime> calendarOffsetDateTime;

    @Subscribe
    protected void onInit(InitEvent event) {
        calendarDefault.setStartDate(new Date());
        calendarDefault.setEndDate(new Date());

        calendarDate.setStartDate(new Date());
        calendarDate.setEndDate(new Date());

        calendarDateTime.setStartDate(new Date());
        calendarDateTime.setEndDate(new Date());

        calendarLocalDate.setEndDate(LocalDate.now());
        calendarLocalDate.setStartDate(LocalDate.now());

        calendarLocalDateTime.setStartDate(LocalDateTime.now());
        calendarLocalDateTime.setEndDate(LocalDateTime.now());

        calendarOffsetDateTime.setStartDate(OffsetDateTime.now());
        calendarOffsetDateTime.setEndDate(OffsetDateTime.now());
    }
}
