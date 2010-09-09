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

import com.haulmont.cuba.web.toolkit.ui.charts.LineChart;
import com.vaadin.data.Container;

public class JFreeLineChart extends JFreeChart implements LineChart {
    private static final long serialVersionUID = 7118566359236292560L;

    public JFreeLineChart() {
    }

    public JFreeLineChart(String caption) {
        super(caption);
    }

    public JFreeLineChart(String caption, Container datasource) {
        super(caption, datasource);
    }

    public JFreeLineChart(Container datasource) {
        super(datasource);
    }
}
