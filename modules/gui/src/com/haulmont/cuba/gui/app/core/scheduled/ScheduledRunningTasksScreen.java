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

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("sys$ScheduledRunningTasksScreen")
@UiDescriptor("scheduled-running-tasks-screen.xml")
@LookupComponent("scheduledTasksTable")
@LoadDataBeforeShow
public class ScheduledRunningTasksScreen extends Screen {

    @Inject
    protected SchedulingService schedulingService;

    @Install(to = "scheduledTasksDl", target = Target.DATA_LOADER)
    protected List<ScheduledTask> scheduledTasksDlLoadDelegate(LoadContext<ScheduledTask> loadContext) {
        return schedulingService.getRunningTasks();
    }
}
