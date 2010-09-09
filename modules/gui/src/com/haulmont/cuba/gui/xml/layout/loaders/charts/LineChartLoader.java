/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 08.09.2010 10:35:46
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders.charts;

import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.charts.LineChart;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

public class LineChartLoader extends AbstractChartLoader {
    private static final long serialVersionUID = -8656847955537637060L;

    public LineChartLoader(Context context) {
        super(context);
    }

    @Override
    public Chart loadComponent(
            ComponentsFactory factory,
            Element element,
            Component parent
    ) throws InstantiationException, IllegalAccessException {
        LineChart component = (LineChart) super.loadComponent(factory, element, parent);

        loadAxisLabels(component, element);
        loadOrientation(component, element);
        loadAxisLabels(component, element);

        return component; 
    }
}
