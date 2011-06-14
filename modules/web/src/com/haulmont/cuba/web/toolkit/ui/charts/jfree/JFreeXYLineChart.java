package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.toolkit.gwt.client.charts.jfree.JFreeChartRenderer;
import com.haulmont.cuba.web.toolkit.ui.charts.Chart;
import com.haulmont.cuba.web.toolkit.ui.charts.XYLineChart;
import com.vaadin.ui.ClientWidget;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public class JFreeXYLineChart extends JFreeXYChart implements XYLineChart {
    private static final long serialVersionUID = 8197598406862479049L;

    private String argumentAxisLabel;
    private String valueAxisLabel;
    private Orientation orientation = Orientation.VERTICAL;

    public String getArgumentAxisLabel() {
        return argumentAxisLabel;
    }

    public void setArgumentAxisLabel(String argumentAxisLabel) {
        this.argumentAxisLabel = argumentAxisLabel;
    }

    public Chart.Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String getValueAxisLabel() {
        return valueAxisLabel;
    }

    public void setValueAxisLabel(String valueAxisLabel) {
        this.valueAxisLabel = valueAxisLabel;
    }
}
