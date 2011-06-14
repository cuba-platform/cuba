/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.charts.jfree;

import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.charts.XYLineChart;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.charts.WebAbstractXYChart;
import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeLineChart;
import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeXYLineChart;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public class WebJFreeXYLineChart extends WebAbstractXYChart<JFreeXYLineChart> implements XYLineChart {
    private static final long serialVersionUID = -3460128438079322004L;

    public WebJFreeXYLineChart() {
        component = new JFreeXYLineChart();
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
}
