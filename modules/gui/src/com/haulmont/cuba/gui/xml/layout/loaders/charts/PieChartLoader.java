/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 08.09.2010 10:35:22
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders.charts;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.charts.PieChart;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class PieChartLoader extends AbstractChartLoader {
    private static final long serialVersionUID = -8559322020967118466L;

    public PieChartLoader(Context context) {
        super(context);
    }

    @Override
    public Chart loadComponent(
            ComponentsFactory factory,
            Element element,
            Component parent
    ) throws InstantiationException, IllegalAccessException {
        PieChart component = (PieChart) super.loadComponent(factory, element, parent);

        load3D(component, element);

        return component;
    }

    @Override
    protected void loadDatasource(Chart component, Element element) {
        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            CollectionDatasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new IllegalStateException("Cannot find data source by name: " + datasource);
            }

            String valueProperty = element.attributeValue("valueProperty");
            if (StringUtils.isEmpty(valueProperty)) {
                throw new IllegalAccessError("PieChart must contains non-empty 'valueProperty' attribute");
            }

            MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyPath(valueProperty);
            if (propertyPath == null) {
                throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                        valueProperty, ds.getMetaClass().getName()));
            }

            component.addColumn(propertyPath, null);

            String captionProperty = element.attributeValue("captionProperty");
            if (!StringUtils.isEmpty(captionProperty)) {
                MetaPropertyPath captionPropertyPath = ds.getMetaClass().getPropertyPath(captionProperty);
                if (captionPropertyPath == null) {
                    throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                            captionProperty, ds.getMetaClass().getName()));
                }
                component.setRowCaptionPropertyId(captionPropertyPath);
            }

            component.setCollectionDatasource(ds);
        }
    }
}
