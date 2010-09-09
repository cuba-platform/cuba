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

import com.haulmont.cuba.web.toolkit.ui.charts.PieChart;
import com.vaadin.data.Container;

public class JFreePieChart extends JFreeChart implements PieChart {
    private static final long serialVersionUID = 1881367032236295730L;

    private boolean ignoreZeroValues;
    private boolean ignoreNullValues;

    public JFreePieChart() {
    }

    public JFreePieChart(String caption) {
        super(caption);
    }

    public JFreePieChart(String caption, Container datasource) {
        super(caption, datasource);
    }

    public JFreePieChart(Container datasource) {
        super(datasource);
    }

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
}
