/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
  * Haulmont Technology proprietary and confidential.
  * Use is subject to license terms.

  * Author: Dmitry Abramov
  * Created: 22.03.2009 15:09:39
  * $Id$
  */

package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.GroovyHelper;
import com.haulmont.cuba.gui.TemplateHelper;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.xml.ParametersHelper;

import java.util.Collection;
import java.util.Map;

import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

public class CustomCollectionDatasource<T extends Entity, K>
    extends
        CollectionDatasourceImpl<T, K>
{
    public CustomCollectionDatasource(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public void commit() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        StopWatch sw = new Log4JStopWatch("CCDS " + id);

        final Map<String, Object> parameters = getQueryParameters(params);

        Collection<T> entities = GroovyHelper.evaluate(getGroovyScript(query, parameters), parameters);

        for (T entity : entities) {
            data.put(entity.getId(), entity);
        }

        sw.stop();
    }

    private String getGroovyScript(String query, Map<String, Object> parameterValues) {
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            final String paramName = info.getName().replaceAll("\\$", "\\\\\\$");
            query = query.replaceAll(":" + paramName, info.getFlatName());
        }

        query = TemplateHelper.processTemplate(query, parameterValues);

        return query;
    }
}
