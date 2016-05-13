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

package com.haulmont.cuba.web.toolkit.ui.client.communication;

import com.haulmont.cuba.web.toolkit.ui.client.profiler.ScreenClientProfiler;
import com.vaadin.client.ValueMap;
import com.vaadin.client.communication.MessageHandler;


public class CubaMessageHandler extends MessageHandler {

    @Override
    protected void startHandlingJsonCommand(ValueMap json) {
        super.startHandlingJsonCommand(json);
        String profilerMarker = ScreenClientProfiler.getProfilerMarkerFromJson(json);
        ScreenClientProfiler profiler = ScreenClientProfiler.getInstance();
        if (profilerMarker != null) {
            profiler.setProfilerMarker(profilerMarker);
            profiler.setEnabled(true);
        } else {
            profiler.clearProfilerMarker();
        }
    }

    @Override
    protected void finishHandlingJsonCommand() {
        super.finishHandlingJsonCommand();
        ScreenClientProfiler profiler = ScreenClientProfiler.getInstance();
        String profilerMarker = profiler.getProfilerMarker();
        if (profilerMarker != null) {
            profiler.registerClientTime(profilerMarker, lastProcessingTime);
        }
        profiler.clearProfilerMarker();
    }
}
