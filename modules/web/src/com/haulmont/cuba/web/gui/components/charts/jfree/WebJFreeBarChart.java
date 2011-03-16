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
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.charts.WebAbstractChart;
import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeBarChart;

public class WebJFreeBarChart extends WebAbstractChart<JFreeBarChart> implements BarChart {
    private static final long serialVersionUID = -1225096992495156855L;

    private Orientation orientation;

    public WebJFreeBarChart() {
        component = new JFreeBarChart();
        setOrientation(Orientation.VERTICAL);
    }

    public boolean is3D() {
        return component.is3D();
    }

    public void set3D(boolean b) {
        component.set3D(b);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        component.setOrientation(WebComponentsHelper.convertChartOrientation(orientation));
    }
}
