/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 03.09.2010 12:14:07
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.toolkit.gwt.client.charts.jfree.JFreeChartRenderer;
import com.haulmont.cuba.web.toolkit.ui.charts.BarChart;
import com.haulmont.cuba.web.toolkit.ui.charts.Chart;
import com.vaadin.data.Container;
import com.vaadin.ui.ClientWidget;

public class JFreeBarChart extends JFreeCategoryChart implements BarChart {
    private static final long serialVersionUID = 2496407681381514463L;

    private String argumentAxisLabel;
    private String valueAxisLabel;
    private AxisType valueAxisType = AxisType.NUMBER;
    private Orientation orientation = Orientation.VERTICAL;
    private boolean is3D = false;

    public String getArgumentAxisLabel() {
        return argumentAxisLabel;
    }

    public void setArgumentAxisLabel(String columnAxisLabel) {
        this.argumentAxisLabel = columnAxisLabel;
    }

    public String getValueAxisLabel() {
        return valueAxisLabel;
    }

    public void setValueAxisLabel(String valueAxisLabel) {
        this.valueAxisLabel = valueAxisLabel;
    }

    public AxisType getValueAxisType() {
        return valueAxisType;
    }

    public void setValueAxisType(AxisType valueAxisType) {
        this.valueAxisType = valueAxisType;
    }

    public boolean is3D() {
        return is3D;
    }

    public void set3D(boolean is3D) {
        this.is3D = is3D;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
