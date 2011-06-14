/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2010 19:09:00
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.toolkit.gwt.client.charts.jfree.JFreeChartRenderer;
import com.haulmont.cuba.web.toolkit.ui.charts.Chart;
import com.haulmont.cuba.web.toolkit.ui.charts.PieChart;
import com.vaadin.data.Container;
import com.vaadin.ui.ClientWidget;

public class JFreePieChart extends JFreeCategoryChart implements PieChart {
    private static final long serialVersionUID = 1881367032236295730L;

    private boolean is3D = false;

    private boolean ignoreZeroValues = false;
    private boolean ignoreNullValues = false;

    public boolean isIgnoreNullValues() {
        return ignoreNullValues;
    }

    public void setIgnoreNullValues(boolean ignoreNullValues) {
        this.ignoreNullValues = ignoreNullValues;
        requestRepaint();
    }

    public boolean isIgnoreZeroValues() {
        return ignoreZeroValues;
    }

    public void setIgnoreZeroValues(boolean ignoreZeroValues) {
        this.ignoreZeroValues = ignoreZeroValues;
        requestRepaint();
    }

    public boolean is3D() {
        return is3D;
    }

    public void set3D(boolean is3D) {
        this.is3D = is3D;
    }
}
