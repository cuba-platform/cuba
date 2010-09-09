/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 17:08:59
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.charts.jfree;

import com.haulmont.cuba.gui.components.charts.LineChart;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.charts.WebAbstractChart;
import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeLineChart;

public class WebJFreeLineChart extends WebAbstractChart<JFreeLineChart> implements LineChart {
    private static final long serialVersionUID = 6270988135198002719L;

    private Orientation orientation;

    public WebJFreeLineChart() {
        component = new JFreeLineChart();
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        component.setOrientation(WebComponentsHelper.convertChartOrientation(orientation));
    }
}
