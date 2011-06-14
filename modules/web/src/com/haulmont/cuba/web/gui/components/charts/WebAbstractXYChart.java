package com.haulmont.cuba.web.gui.components.charts;

import com.haulmont.cuba.gui.components.charts.CategoryChart;
import com.haulmont.cuba.gui.components.charts.XYChart;
import com.haulmont.cuba.gui.components.charts.XYChartRow;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.charts.CategoryChartComponent;
import com.haulmont.cuba.web.toolkit.ui.charts.XYChartComponent;
import com.haulmont.cuba.web.toolkit.ui.charts.XYChartRowComponent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public abstract class WebAbstractXYChart<T extends XYChartComponent>
        extends WebAbstractChart<T>
        implements XYChart {

    private static final long serialVersionUID = -6675638557113699291L;

    protected Collection<XYChartRow> rows = new ArrayList<XYChartRow>();

    public Collection<XYChartRow> getRows() {
        return rows;
    }

    public void addRow(XYChartRow row) {
        component.addRow((XYChartRowComponent)row.getComponent());
        rows.add(row);
    }

    public AxisType getArgumentAxisType() {
        return WebComponentsHelper.convertChartAxisType(component.getArgumentAxisType());
    }

    public void setArgumentAxisType(AxisType axisType) {
        component.setArgumentAxisType(WebComponentsHelper.convertChartAxisType(axisType));
    }

    public AxisType getValueAxisType() {
        return WebComponentsHelper.convertChartAxisType(component.getValueAxisType());
    }

    public void setValueAxisType(AxisType axisType) {
        component.setValueAxisType(WebComponentsHelper.convertChartAxisType(axisType));
    }
}
