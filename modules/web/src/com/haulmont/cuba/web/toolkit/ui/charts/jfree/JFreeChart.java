/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 01.09.2010 15:44:48
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.toolkit.gwt.client.charts.jfree.JFreeChartRenderer;
import com.haulmont.cuba.web.toolkit.ui.charts.Chart;
import com.haulmont.cuba.web.toolkit.ui.charts.ChartComponent;
import com.haulmont.cuba.web.toolkit.ui.charts.ChartImplementation;
import com.vaadin.data.Container;
import com.vaadin.ui.ClientWidget;

@ClientWidget(JFreeChartRenderer.class)
public abstract class JFreeChart extends ChartComponent implements ChartImplementation, Chart {
    protected JFreeChart() {
    }

    protected JFreeChart(String caption) {
        super(caption);
    }

    protected JFreeChart(String caption, Container datasource) {
        super(caption, datasource);
    }

    protected JFreeChart(Container datasource) {
        super(datasource);
    }

    public String getVendor() {
        return "jfree";
    }
}
