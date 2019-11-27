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

import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Date;

@UiController
@UiDescriptor("calendar-screen.xml")
public class CalendarScreen extends Screen {

    @Inject
    protected Metadata metadata;

    @Inject
    public CollectionContainer<ScheduledTask> tasksDc;

    @Inject
    protected Calendar calendarDefault;
    @Inject
    protected Calendar<Date> calendarDate;
    @Inject
    protected Calendar<Date> calendarDateTime;
    @Inject
    protected Calendar<LocalDate> calendarLocalDate;
    @Inject
    protected Calendar<LocalDateTime> calendarLocalDateTime;
    @Inject
    protected Calendar<OffsetDateTime> calendarOffsetDateTime;
    @Inject
    protected Calendar calendarWithContainer;

    @Subscribe
    protected void onInit(InitEvent event) {
        initDataContainer();
        initFields();
    }

    protected void initDataContainer() {
        ScheduledTask task = metadata.create(ScheduledTask.class);
        tasksDc.setItems(Collections.singletonList(task));
    }

    protected void initFields() {
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
