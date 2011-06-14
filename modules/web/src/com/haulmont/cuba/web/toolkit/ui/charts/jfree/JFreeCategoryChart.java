package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.toolkit.gwt.client.charts.jfree.JFreeChartRenderer;
import com.haulmont.cuba.web.toolkit.ui.charts.CategoryChartComponent;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ClientWidget;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
@ClientWidget(JFreeChartRenderer.class)
public abstract class JFreeCategoryChart extends CategoryChartComponent implements JFreeChart {
    private static final long serialVersionUID = -7073713483941791212L;

    public String getVendor() {
        return VENDOR;
    }
}
