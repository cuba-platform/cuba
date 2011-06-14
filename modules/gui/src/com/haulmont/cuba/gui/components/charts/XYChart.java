package com.haulmont.cuba.gui.components.charts;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public interface XYChart extends Chart, Chart.HasValueAxisType, Chart.HasArgumentAxisType {

    Collection<XYChartRow> getRows();
    void addRow(XYChartRow row);
}
