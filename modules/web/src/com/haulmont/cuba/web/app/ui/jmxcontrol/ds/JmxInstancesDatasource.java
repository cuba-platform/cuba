/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author artamonov
 * @version $Id$
 */
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