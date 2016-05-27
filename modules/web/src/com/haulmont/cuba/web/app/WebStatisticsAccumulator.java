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

package com.haulmont.cuba.web.app;

import com.haulmont.cuba.core.sys.StatisticsAccumulator;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicLong;

@Component(WebStatisticsAccumulator.NAME)
public class WebStatisticsAccumulator extends StatisticsAccumulator {

    public static final String NAME = "cuba_WebStatisticsAccumulator";

    protected AtomicLong webRequestsCount = new AtomicLong();

    public void incWebRequestsCount() {
        webRequestsCount.incrementAndGet();
    }

    public Long getWebRequestsCount() {
        return webRequestsCount.get();
    }

    public double getWebRequestsPerSecond() {
        return getWebRequestsCount() / ((System.currentTimeMillis() - startTime) / 1000.0);
    }
}