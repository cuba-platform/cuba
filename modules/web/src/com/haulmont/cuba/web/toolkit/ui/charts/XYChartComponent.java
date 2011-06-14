package com.haulmont.cuba.web.toolkit.ui.charts;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;

import java.io.Serializable;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public abstract class XYChartComponent extends ChartComponent implements XYChart {

    private static final long serialVersionUID = -7433062042194011796L;

    protected Collection<XYChartRow> rows = new ArrayList<XYChartRow>();

    protected AxisType argumentAxisType = AxisType.NUMBER;
    protected AxisType valueAxisType = AxisType.NUMBER;

    public Collection<XYChartRow> getRows() {
        return rows;
    }

    public void addRow(XYChartRow row) {
        rows.add(row);
        row.addListener((Container.ItemSetChangeListener)this);
        row.addListener((Container.PropertySetChangeListener)this);
        requestRepaint();
    }

    public AxisType getArgumentAxisType() {
        return argumentAxisType;
    }

    public void setArgumentAxisType(AxisType axisType) {
        this.argumentAxisType = axisType;
    }

    public AxisType getValueAxisType() {
        return valueAxisType;
    }

    public void setValueAxisType(AxisType valueAxisType) {
        this.valueAxisType = valueAxisType;
    }

    @Override
    public void containerItemSetChange(Container.ItemSetChangeEvent event) {
        requestRepaint();
    }

    @Override
    public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
        requestRepaint();
    }
}
