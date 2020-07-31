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

package com.haulmont.cuba.web.app.core.screenprofiler;

import com.haulmont.cuba.core.app.ScreenProfilerService;
import com.haulmont.cuba.core.entity.ScreenProfilerEvent;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ScreenProfilerWindow extends AbstractWindow {
    @Inject
    protected Button profilingButton;
    @Inject
    protected Button clearResultsButton;
    @Inject
    protected GridLayout grid;
    @Inject
    protected CheckBox refreshEventsCheck;
    @Inject
    protected TextField<Long> timeThreshold;
    @Inject
    protected Timer refreshEventsTimer;
    @Inject
    protected CollectionDatasource<ScreenProfilerEvent, UUID> profilerEventsDs;
    @Inject
    protected CollectionDatasource<User, UUID> usersDs;
    @Inject
    private CollectionDatasource<User, UUID> allUsersDs;
    @Inject
    protected ScreenProfilerService screenProfilerService;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        updateProfilingCaption(screenProfilerService.isProfilingEnabled());
        initProfilingComponents();
        disableComponents();
        refreshEventsCheck.addValueChangeListener(e -> {
            if (Boolean.TRUE.equals(e.getValue())) {
                refreshEventsTimer.start();
            } else {
                refreshEventsTimer.stop();
            }
        });
    }

    public void activateProfiling() {
        boolean enabled = !screenProfilerService.isProfilingEnabled();
        if (enabled) {
            screenProfilerService.enableProfiling(new HashSet<>(usersDs.getItemIds()),
                    timeThreshold.getValue() == null ? 0 : timeThreshold.getValue());
        } else {
            screenProfilerService.disableProfiling();
        }
        updateProfilingCaption(enabled);
        disableComponents();
    }

    public void disableComponents() {
        boolean enabled = screenProfilerService.isProfilingEnabled();
        clearResultsButton.setEnabled(!enabled);
        grid.setEnabled(!enabled);
    }

    protected void updateProfilingCaption(boolean enabled) {
        if (enabled) {
            profilingButton.setCaption(getMessage("deactivateProfiling"));
        } else {
            profilingButton.setCaption(getMessage("activateProfiling"));
        }
    }

    protected void initProfilingComponents() {
        if (screenProfilerService.isProfilingEnabled()) {
            timeThreshold.setValue(screenProfilerService.getTimeThreshold());
            Set<UUID> userIds = screenProfilerService.getUserIds();
            if (userIds != null && !userIds.isEmpty()) {
                allUsersDs.refresh();
                userIds.forEach(id -> {
                    User user = allUsersDs.getItem(id);
                    if (user != null) {
                        usersDs.addItem(user);
                    }
                });
            }
        }
    }


    public void clearResults() {
        screenProfilerService.clearEvents();
        profilerEventsDs.refresh();
    }

    public void refreshEvents(Timer source) {
        profilerEventsDs.refresh();
    }
}
