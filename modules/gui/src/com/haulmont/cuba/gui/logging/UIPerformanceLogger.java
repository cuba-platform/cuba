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
 *
 */

package com.haulmont.cuba.gui.logging;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.LoggerFactory;

/**
 * Logger class for UI performance stats.
 */
public final class UIPerformanceLogger {

    private UIPerformanceLogger() {
    }

    public static StopWatch createStopWatch(ScreenLifeCycle lifeCycle, String loggingId) {
        return new Slf4JStopWatch(loggingId + lifeCycle.getSuffix(), LoggerFactory.getLogger(UIPerformanceLogger.class));
    }
}