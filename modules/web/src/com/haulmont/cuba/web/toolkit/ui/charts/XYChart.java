package com.haulmont.cuba.web.toolkit.ui.charts;

import com.vaadin.data.Container;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public interface XYChart extends Chart, Chart.HasValueAxisType, Chart.HasArgumentAxisType,
        Container.ItemSetChangeListener, Container.PropertySetChangeListener {

    Collection<XYChartRow> getRows();
    void addRow(XYChartRow row);
}
