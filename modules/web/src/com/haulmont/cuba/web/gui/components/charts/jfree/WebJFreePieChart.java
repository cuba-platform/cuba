/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 17:08:27
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.charts.jfree;

import com.haulmont.cuba.gui.components.charts.PieChart;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.charts.WebAbstractCategoryChart;
import com.haulmont.cuba.web.gui.components.charts.WebAbstractChart;
import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreePieChart;

public class WebJFreePieChart extends WebAbstractCategoryChart<JFreePieChart> implements PieChart {
    private static final long serialVersionUID = 2148678991758077072L;

    public WebJFreePieChart() {
        component = new JFreePieChart();
    }

    public boolean is3D() {
        return component.is3D();
    }

    public void set3D(boolean b) {
        component.set3D(b);
    }
}
