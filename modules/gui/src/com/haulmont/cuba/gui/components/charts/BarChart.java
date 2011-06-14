/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 17:44:02
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.charts;

/** Bar chart component */
public interface BarChart
        extends
        CategoryChart,
        Chart.HasAxisLabels,
        Chart.HasValueAxisType,
        Chart.ViewIn3D,
        Chart.HasOrientation {

    String NAME = "barChart";
}
