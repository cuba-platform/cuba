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

import com.haulmont.cuba.web.toolkit.ui.charts.BarChart;
import com.vaadin.data.Container;

public class JFreeBarChart extends JFreeChart implements BarChart {
    private static final long serialVersionUID = 2496407681381514463L;

    public JFreeBarChart() {
    }

    public JFreeBarChart(String caption) {
        super(caption);
    }

    public JFreeBarChart(String caption, Container datasource) {
        super(caption, datasource);
    }

    public JFreeBarChart(Container datasource) {
        super(datasource);
    }
}
