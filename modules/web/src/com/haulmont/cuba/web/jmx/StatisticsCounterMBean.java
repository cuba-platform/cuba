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

package com.haulmont.cuba.web.jmx;

import org.springframework.jmx.export.annotation.ManagedResource;

@SuppressWarnings("unused")
@ManagedResource(description = "Provides information about web-client requests statistics")
public interface StatisticsCounterMBean {

    double getWebRequestsCount();

    double getWebRequestsPerSecond();

    double getSpringScheduledTasksCount();

    double getSpringScheduledTasksPerSecond();

    double getAvgHeapMemoryUsage();

    double getAvgNonHeapMemoryUsage();

    double getAvgFreePhysicalMemorySize();

    double getAvgFreeSwapSpaceSize();

    double getAvgSystemCpuLoad();

    double getAvgProcessCpuLoad();

    double getAvgThreadCount();
}