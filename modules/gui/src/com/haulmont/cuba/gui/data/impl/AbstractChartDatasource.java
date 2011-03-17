/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 04.10.2010 15:06:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.ChartDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractChartDatasource<T extends Entity<K>, K>
        extends CollectionDatasourceImpl<T, K>
        implements ChartDatasource<T, K>
{
    protected AbstractChartDatasource(
            DsContext context,
            DataService dataservice,
            String id,
            MetaClass metaClass,
            String viewName
    ) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        StopWatch sw = new Log4JStopWatch("ChartDS " + id);

        final Collection<T> chartData = loadChartData(params);

        data.clear();
        for (final T entity : chartData) {
            data.put(entity.getId(), entity);
        }

        sw.stop();
    }

    public Collection<K> getRowIds() {
        return Collections.unmodifiableCollection(data.keySet());
    }

    public String getRowCaption(K rowId) {
        Object o =  data.get(rowId);
        if (o == null) {
            return "";
        } else {
            return String.valueOf(o);
        }
    }

    protected abstract Collection<T> loadChartData(Map<String, Object> params);
}
