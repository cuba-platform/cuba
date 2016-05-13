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

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.app.ScreenProfilerService;
import com.haulmont.cuba.core.entity.ScreenProfilerEvent;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.web.toolkit.ui.client.profiler.ScreenProfilerClientEvent;
import com.vaadin.ui.UI;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Component(ScreenProfiler.NAME)
public class ScreenProfiler {

    public static final String NAME = "cuba_ScreenProfiler";

    @Inject
    protected ScreenProfilerService screenProfilerService;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected Metadata metadata;

    public void initProfilerMarkerForWindow(String screenId) {
        UUID userId = userSessionSource.currentOrSubstitutedUserId();
        if (screenProfilerService.isProfilingEnabledForUser(userId)) {
            String profilerMarker = UuidProvider.createUuid().toString();
            AppUI appUI = AppUI.getCurrent();
            appUI.setProfilerMarker(profilerMarker);
            appUI.setProfiledScreen(profilerMarker, screenId);
        }
    }

    public String getCurrentProfilerMarker(UI ui) {
        if (ui instanceof AppUI) {
            return ((AppUI) ui).getProfilerMarker();
        }
        return null;
    }

    public void setCurrentProfilerMarker(UI ui, String profilerMarker) {
        if (ui instanceof AppUI) {
            ((AppUI) ui).setProfilerMarker(profilerMarker);
        }
    }

    public void flush(ScreenProfilerClientEvent[] clientEvents) {
        if (clientEvents != null) {
            List<ScreenProfilerEvent> events = new LinkedList<>();
            List<String> clearedMarkers = new LinkedList<>();
            AppUI appUI = AppUI.getCurrent();
            for (ScreenProfilerClientEvent clientEvent : clientEvents) {
                events.add(transformEvent(clientEvent));
                clearedMarkers.add(clientEvent.getProfilerMarker());
            }
            screenProfilerService.saveEvents(events);
            appUI.clearProfiledScreens(clearedMarkers);
        }
    }

    protected ScreenProfilerEvent transformEvent(ScreenProfilerClientEvent clientEvent) {
        AppUI appUI = AppUI.getCurrent();
        ScreenProfilerEvent event = metadata.create(ScreenProfilerEvent.class);
        event.setScreen(appUI.getProfiledScreen(clientEvent.getProfilerMarker()));
        event.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser().getInstanceName());
        if (clientEvent.getEventTs() > 0) {
            event.setEventTs(new Date(clientEvent.getEventTs()));
        }
        event.setClientTime(clientEvent.getClientTime());
        event.setServerTime(clientEvent.getServerTime());
        event.setNetworkTime(clientEvent.getNetworkTime());
        event.setTotalTime(clientEvent.getClientTime() + clientEvent.getServerTime() + clientEvent.getNetworkTime());
        return event;
    }
}
