/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 19:11:28
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders.charts;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.xml.layout.loaders.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;

public abstract class AbstractChartLoader extends com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoader {
    public AbstractChartLoader(Context context) {
        super(context);
    }

    public Chart loadComponent(
            ComponentsFactory factory,
            Element element,
            Component parent
    ) throws InstantiationException, IllegalAccessException {
        String vendor = element.attributeValue("vendor");
        Chart component = factory.createChart(vendor, element.getName());

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadVisible(component, element);

        loadCaption(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadLegend(component, element);

        loadDatasource(component, element);

        assignFrame(component);

        return component;
    }

    protected void load3D(Chart.ViewIn3D component, Element element) {
        String is3d = element.attributeValue("is3D");
        if (!StringUtils.isEmpty(is3d) && isBoolean(is3d)) {
            component.set3D(Boolean.valueOf(is3d));
        }
    }

    protected void loadDatasource(Chart component, Element element) {
        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            CollectionDatasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new IllegalStateException("Cannot find data source by name: " + datasource);
            }

            loadColumns(component, element, ds);

            String captionProperty = element.attributeValue("captionProperty");
            if (!StringUtils.isEmpty(captionProperty)) {
                MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyPath(captionProperty);
                if (propertyPath == null) {
                    throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                            captionProperty, ds.getMetaClass().getName()));
                }
                component.setRowCaptionPropertyId(propertyPath);
            }

            component.setCollectionDatasource(ds);

        }
    }

    protected void loadColumns(Chart component, Element element, CollectionDatasource ds) {
        List<Element> columnElements = element.elements("column");
        for (final Element columnElement : columnElements) {
            loadColumn(component, columnElement, ds);
        }
    }

    protected void loadColumn(Chart component, Element element, CollectionDatasource ds) {
        String id = element.attributeValue("id");
        MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyPath(id);
        if (propertyPath == null) {
            throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                    id, ds.getMetaClass().getName()));
        }

        String caption = element.attributeValue("caption");
        if (!StringUtils.isEmpty(caption)) {
            caption = loadResourceString(caption);
        } else {
            caption = null;
        }

        component.addColumn(propertyPath, caption);
    }

    protected void loadOrientation(Chart.HasOrientation component, Element element) {
        String orientation = element.attributeValue("orientation");
        if (!StringUtils.isEmpty(orientation)) {
            component.setOrientation(Chart.Orientation.valueOf(orientation));
        }
    }

    @Override
    protected void loadWidth(Component component, Element element, String defaultValue) {
        final String width = element.attributeValue("width");
        if (!StringUtils.isBlank(width)) {
            if (StringUtils.isNumeric(width)) {
                component.setWidth(width);
            } else {
                throw new IllegalArgumentException("'width' attribute must contains numeric values only");
            }
        }
    }

    @Override
    protected void loadHeight(Component component, Element element, String defaultValue) {
        final String height = element.attributeValue("height");
        if (!StringUtils.isBlank(height)) {
            if (StringUtils.isNumeric(height)) {
                component.setHeight(height);
            } else {
                throw new IllegalArgumentException("'height' attribute must contains numeric values only");
            }
        }
    }

    protected void loadLegend(Chart component, Element element) {
        String legend = element.attributeValue("legend");
        if (!StringUtils.isEmpty(legend) && isBoolean(legend)) {
            component.setLegend(Boolean.valueOf(legend));
        }
    }

    protected void loadAxisLabels(Chart component, Element element) {
        String label = element.attributeValue("xLabel");
        if (!StringUtils.isEmpty(label)) {
            component.setColumnAxisLabel(label);
        }
        label = element.attributeValue("yLabel");
        if (!StringUtils.isEmpty(label)) {
            component.setValueAxisLabel(label);
        }
    }
}
