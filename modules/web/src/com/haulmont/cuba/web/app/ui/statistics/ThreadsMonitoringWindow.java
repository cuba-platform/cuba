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

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Timer;

import javax.inject.Inject;
import java.util.Map;

/**
 */
public class ThreadsMonitoringWindow extends AbstractWindow {

    @Inject
    protected ThreadsDatasource threadsDs;

    @Inject
    protected Table threadsTable;

    @Override
    public void init(Map<String, Object> params) {
        threadsDs.refresh(params);
        threadsDs.addItemChangeListener(e -> updateStacktrace(e.getItem()));

        JmxInstance node = (JmxInstance) params.get("node");
        setCaption(formatMessage("threadsMonitoring.caption", node.getNodeName()));

        threadsTable.getColumn("cpu").setFormatter(new PercentFormatter());
    }

    protected void updateStacktrace(ThreadSnapshot item) {
        if (item != null) {
            item.setStackTrace(threadsDs.getStackTrace(item.getThreadId()));
        }
    }

    @SuppressWarnings("unused")
    public void onRefresh(Timer timer) {
        threadsDs.refresh();
        updateStacktrace(threadsDs.getItem());
    }

    protected static class PercentFormatter implements Formatter {
        @Override
        public String format(Object value) {
            String res = null;
            if (value instanceof Double) {
                double doubleValue = (double) value;
                res = String.format("%.1f %%", doubleValue * 100);
            }
            return res != null ? res : (value != null ? value.toString() : null);
        }
    }
}