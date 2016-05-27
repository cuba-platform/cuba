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

package com.haulmont.cuba.web.app.ui.jmxcontrol.ds;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.Map;
import java.util.UUID;

public class JmxInstancesDatasource extends CollectionDatasourceImpl<JmxInstance, UUID> {

    private JmxControlAPI jmxControlAPI = AppBeans.get(JmxControlAPI.NAME);

    @Override
    protected void loadData(Map<String, Object> params) {
        String tag = getLoggingTag("CDS");
        StopWatch sw = new Log4JStopWatch(tag, Logger.getLogger(UIPerformanceLogger.class));

        detachListener(data.values());
        data.clear();

        for (JmxInstance jmxInstance : jmxControlAPI.getInstances()) {
            data.put(jmxInstance.getId(), jmxInstance);
            attachListener(jmxInstance);
        }

        sw.stop();
    }
}