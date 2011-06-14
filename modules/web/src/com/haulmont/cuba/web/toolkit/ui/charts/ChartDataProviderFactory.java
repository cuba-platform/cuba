/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2010 9:57:59
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts;


import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeChart;
import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeChartDataProvider;

import java.util.HashMap;
import java.util.Map;

public abstract class ChartDataProviderFactory {

    private static Map<String, ChartDataProvider> providers = new HashMap<String, ChartDataProvider>();

    static {
        providers.put(JFreeChart.VENDOR, new JFreeChartDataProvider());
    }

    public static void register(String chartVendor, ChartDataProvider dataProvider) {
        providers.put(chartVendor, dataProvider);
    }

    public static ChartDataProvider getDataProvider(String chartVendor) {
        return providers.get(chartVendor);
    }
}
