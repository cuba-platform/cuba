/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
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
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CollectionDsHelper {

    public static List<MetaPropertyPath> createProperties(View view, MetaClass metaClass) {
        List<MetaPropertyPath> properties = new ArrayList<>();
        if (view != null) {
            for (ViewProperty property : view.getProperties()) {
                final String name = property.getName();

                final MetaProperty metaProperty = metaClass.getProperty(name);
                final Range range = metaProperty.getRange();
                if (range == null) continue;

                final Range.Cardinality cardinality = range.getCardinality();
                if (!cardinality.isMany()) {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }
        } else {
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                final Range range = metaProperty.getRange();
                if (range == null) continue;

                final Range.Cardinality cardinality = range.getCardinality();
                if (!cardinality.isMany()) {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }
        }
        return properties;
    }

    public static void autoRefreshInvalid(CollectionDatasource datasource, boolean autoRefresh) {
        if (autoRefresh && Datasource.State.INVALID.equals(datasource.getState())) {
            DsContext dsContext = datasource.getDsContext();
            if (dsContext == null || !WindowParams.DISABLE_AUTO_REFRESH.getBool(dsContext.getWindowContext())) {
                if (datasource instanceof CollectionDatasource.Suspendable)
                    ((CollectionDatasource.Suspendable) datasource).refreshIfNotSuspended();
                else
                    datasource.refresh();
            }
        }
    }

}
