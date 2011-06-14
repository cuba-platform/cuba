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

import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public abstract class AbstractChartLoader extends ComponentLoader {
    public AbstractChartLoader(Context context) {
        super(context);
    }

    public Chart loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException {

        String vendor = element.attributeValue("vendor");
        Chart component = factory.createChart(vendor, element.getName());

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadVisible(component, element);

        loadCaption(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadLegend(component, element);

        assignFrame(component);

        return component;
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
            component.setHasLegend(Boolean.valueOf(legend));
        }
    }

    protected void loadArgumentAxisType(Chart.HasArgumentAxisType component, Element element) {
        String axisType = element.attributeValue("argumentAxisType");
        if (!StringUtils.isEmpty(axisType)) {
            component.setArgumentAxisType(Chart.AxisType.valueOf(axisType));
        }
    }

    protected void loadValueAxisType(Chart.HasValueAxisType component, Element element) {
        String axisType = element.attributeValue("valueAxisType");
        if (!StringUtils.isEmpty(axisType)) {
            component.setValueAxisType(Chart.AxisType.valueOf(axisType));
        }
    }

    protected void loadAxisLabels(Chart.HasAxisLabels component, Element element) {
        String label = loadResourceString(element.attributeValue("xLabel"));
        if (!StringUtils.isEmpty(label)) {
            component.setArgumentAxisLabel(label);
        }
        label = loadResourceString(element.attributeValue("yLabel"));
        if (!StringUtils.isEmpty(label)) {
            component.setValueAxisLabel(label);
        }
    }

    protected void loadOrientation(Chart.HasOrientation component, Element element) {
        String orientation = element.attributeValue("orientation");
        if (!StringUtils.isEmpty(orientation)) {
            component.setOrientation(Chart.Orientation.valueOf(orientation));
        }
    }

    protected void load3D(Chart.ViewIn3D component, Element element) {
        String is3d = element.attributeValue("is3D");
        if (!StringUtils.isEmpty(is3d) && isBoolean(is3d)) {
            component.set3D(Boolean.valueOf(is3d));
        }
    }
}
