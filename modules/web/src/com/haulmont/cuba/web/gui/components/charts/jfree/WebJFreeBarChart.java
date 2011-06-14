/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 17:08:45
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.charts.jfree;

import com.haulmont.cuba.gui.components.charts.BarChart;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.charts.WebAbstractCategoryChart;
import com.haulmont.cuba.web.gui.components.charts.WebAbstractChart;
import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeBarChart;

public class WebJFreeBarChart extends WebAbstractCategoryChart<JFreeBarChart> implements BarChart {
    private static final long serialVersionUID = -1225096992495156855L;

    public WebJFreeBarChart() {
        component = new JFreeBarChart();
    }

    public boolean is3D() {
        return component.is3D();
    }

    public void set3D(boolean b) {
        component.set3D(b);
    }

    public Orientation getOrientation() {
        return WebComponentsHelper.convertChartOrientation(component.getOrientation());
    }

    public void setOrientation(Orientation orientation) {
        component.setOrientation(WebComponentsHelper.convertChartOrientation(orientation));
    }

    public String getArgumentAxisLabel() {
        return component.getArgumentAxisLabel();
    }

    public void setArgumentAxisLabel(String label) {
        component.setArgumentAxisLabel(label);
    }

    public String getValueAxisLabel() {
        return component.getValueAxisLabel();
    }

    public void setValueAxisLabel(String label) {
        component.setValueAxisLabel(label);
    }

    public AxisType getValueAxisType() {
        return WebComponentsHelper.convertChartAxisType(component.getValueAxisType());
    }

    public void setValueAxisType(AxisType axisType) {
        component.setValueAxisType(WebComponentsHelper.convertChartAxisType(axisType));
    }
}
