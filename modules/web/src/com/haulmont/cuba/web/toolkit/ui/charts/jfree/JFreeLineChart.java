/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 03.09.2010 12:15:01
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.toolkit.gwt.client.charts.jfree.JFreeChartRenderer;
import com.haulmont.cuba.web.toolkit.ui.charts.Chart;
import com.haulmont.cuba.web.toolkit.ui.charts.LineChart;
import com.vaadin.data.Container;
import com.vaadin.ui.ClientWidget;

public class JFreeLineChart extends JFreeCategoryChart implements LineChart {
    private static final long serialVersionUID = 7118566359236292560L;

    private String argumentAxisLabel;
    private String valueAxisLabel;
    private AxisType valueAxisType = AxisType.NUMBER;
    private Orientation orientation = Orientation.VERTICAL;

    public String getArgumentAxisLabel() {
        return argumentAxisLabel;
    }

    public void setArgumentAxisLabel(String argumentAxisLabel) {
        this.argumentAxisLabel = argumentAxisLabel;
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

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
