/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CollectionDsHelper {

    public static List<MetaPropertyPath> createProperties(View view, MetaClass metaClass) {
        List<MetaPropertyPath> properties = new ArrayList<>();
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

        if (view != null && metadataTools.isPersistent(metaClass)) {
            for (ViewProperty property : view.getProperties()) {
                final String name = property.getName();

                final MetaProperty metaProperty = metaClass.getProperty(name);
                if (metaProperty == null) {
                    String message = String.format("Unable to find property %s for entity %s", name, metaClass.getName());
                    throw new DevelopmentException(message);
                }

                if (!metadataTools.isPersistent(metaProperty)) {
                    String message = String.format(
                            "Specified transient property %s in view for datasource with persistent entity %s",
                            name, metaClass.getName());

                    LogFactory.getLog(CollectionDsHelper.class).warn(message);
                    continue;
                }

                final Range range = metaProperty.getRange();
                if (range == null) {
                    continue;
                }

                final Range.Cardinality cardinality = range.getCardinality();
                if (!cardinality.isMany()) {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }

            // add all non-persistent properties
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                if (metadataTools.isTransient(metaProperty)) {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }
        } else {
            if (view != null) {
                String message = String.format("Specified view %s for datasource with not persistent entity %s",
                        view.getName(), metaClass.getName());
                LogFactory.getLog(CollectionDsHelper.class).warn(message);
            }

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
        if (datasource instanceof PropertyDatasource) {
            return;
        }
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