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

package com.haulmont.cuba.web.testsupport.proxy;

import com.haulmont.cuba.core.app.ScreenProfilerService;
import com.haulmont.cuba.core.entity.ScreenProfilerEvent;
import com.haulmont.cuba.web.testsupport.TestContainer;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ScreenProfilerServiceProxy implements ScreenProfilerService {
    protected TestContainer container;

    public ScreenProfilerServiceProxy(TestContainer container) {
        this.container = container;
    }

    @Override
    public boolean isProfilingEnabled() {
        return false;
    }

    @Override
    public boolean isProfilingEnabledForUser(UUID userId) {
        return false;
    }

    @Override
    public long getTimeThreshold() {
        return 0;
    }

    @Override
    public Set<UUID> getUserIds() {
        return null;
    }

    @Override
    public void enableProfiling(Set<UUID> userIds, long timeThreshold) {
    }

    @Override
    public void disableProfiling() {
    }

    @Override
    public void saveEvents(List<ScreenProfilerEvent> events) {
    }

    @Override
    public List<ScreenProfilerEvent> getProfilerEvents() {
        return Collections.emptyList();
    }

    @Override
    public void clearEvents() {
    }
}
