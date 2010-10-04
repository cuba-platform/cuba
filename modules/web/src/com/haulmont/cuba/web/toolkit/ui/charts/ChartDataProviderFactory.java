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


import com.haulmont.cuba.web.toolkit.ui.charts.jfree.JFreeChartDataProvider;

import java.util.HashMap;
import java.util.Map;

public abstract class ChartDataProviderFactory {

    private static Map<String, ChartDataProvider> providers;

    private static Map<String, ChartDataProvider> customProviders;

    static {
        providers = new HashMap<String, ChartDataProvider>();
        providers.put("jfree", new JFreeChartDataProvider());
    }

    public static void register(String chartVendor, ChartDataProvider dataProvider) {
        if (customProviders == null) {
            customProviders = new HashMap<String, ChartDataProvider>();
        }
        customProviders.put(chartVendor, dataProvider);
    }

    public static ChartDataProvider getDataProvider(String chartVendor) {
        ChartDataProvider dataProvider = null;
        if (customProviders != null) {
            dataProvider = customProviders.get(chartVendor);
        }
        if (dataProvider == null) {
            dataProvider = providers.get(chartVendor);
        }
        return dataProvider;
    }

    protected ChartDataProviderFactory() {
    }
}
