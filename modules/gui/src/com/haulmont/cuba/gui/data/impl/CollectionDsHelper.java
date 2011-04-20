/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import org.apache.commons.lang.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CollectionDsHelper {

    public static List<MetaPropertyPath> createProperties(View view, MetaClass metaClass) {
        List<MetaPropertyPath> properties = new ArrayList<MetaPropertyPath>();
        if (view != null) {
            for (ViewProperty property : view.getProperties()) {
                final String name = property.getName();

                final MetaProperty metaProperty = metaClass.getProperty(name);
                final Range range = metaProperty.getRange();
                if (range == null) continue;

                final Range.Cardinality cardinality = range.getCardinality();
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_ONE.equals(cardinality))
                {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }
        } else {
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                final Range range = metaProperty.getRange();
                if (range == null) continue;

                final Range.Cardinality cardinality = range.getCardinality();
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_ONE.equals(cardinality))
                {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }
        }
        return properties;
    }


    public static void autoRefreshInvalid(CollectionDatasource datasource, boolean autoRefresh) {
        if (autoRefresh && Datasource.State.INVALID.equals(datasource.getState())) {
            DsContext dsContext = datasource.getDsContext();
            Map<String, Object> params = null;
            if (dsContext != null && dsContext.getWindowContext() != null) {
                params = dsContext.getWindowContext().getParams();
            }
            if (params == null || !BooleanUtils.isTrue((Boolean) params.get("disableAutoRefresh"))) {
                if (datasource instanceof CollectionDatasource.Suspendable)
                    ((CollectionDatasource.Suspendable) datasource).refreshIfNotSuspended();
                else
                    datasource.refresh();
            }
        }
    }

}
