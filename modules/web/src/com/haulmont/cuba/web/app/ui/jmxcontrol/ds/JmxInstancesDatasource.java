/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.ds;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
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

    public JmxInstancesDatasource(DsContext context, DataService dataservice, String id,
                                  MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

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