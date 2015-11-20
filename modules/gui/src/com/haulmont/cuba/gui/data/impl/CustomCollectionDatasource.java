/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.TemplateHelper;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.core.global.filter.ParameterInfo;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.Collection;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class CustomCollectionDatasource<T extends Entity<K>, K>
        extends CollectionDatasourceImpl<T, K> {

    protected Scripting scripting = AppBeans.get(Scripting.NAME);

    @Override
    public void commit() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        StopWatch sw = new Log4JStopWatch(getLoggingTag("CCDS"), Logger.getLogger(UIPerformanceLogger.class));

        for (Object entity : data.values()) {
            detachListener((Instance) entity);
        }
        data.clear();

        final Map<String, Object> parameters = getQueryParameters(params);

        Collection<T> entities = scripting.evaluateGroovy(getGroovyScript(query, parameters), parameters);

        for (T entity : entities) {
            data.put(entity.getId(), entity);
            attachListener(entity);
        }

        sw.stop();
    }

    private String getGroovyScript(String query, Map<String, Object> parameterValues) {
        for (ParameterInfo info : queryParameters) {
            final String paramName = info.getName().replaceAll("\\$", "\\\\\\$");
            query = query.replaceAll(":" + paramName, info.getFlatName());
        }

        query = TemplateHelper.processTemplate(query, parameterValues);

        return query;
    }
}
