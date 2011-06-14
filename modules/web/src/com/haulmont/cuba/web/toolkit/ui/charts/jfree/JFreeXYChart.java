package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.toolkit.gwt.client.charts.jfree.JFreeChartRenderer;
import com.haulmont.cuba.web.toolkit.ui.charts.XYChartComponent;
import com.vaadin.data.Container;
import com.vaadin.ui.ClientWidget;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
@ClientWidget(JFreeChartRenderer.class)
public abstract class JFreeXYChart extends XYChartComponent implements JFreeChart {
    private static final long serialVersionUID = -8367704232221959253L;

    public String getVendor() {
        return VENDOR;
    }
}
