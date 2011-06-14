package com.haulmont.cuba.web.toolkit.ui.charts;

import com.vaadin.data.Container;
import com.vaadin.terminal.Paintable;
import com.vaadin.ui.Component;
import org.apache.openjpa.lib.conf.ObjectValue;
import org.jfree.util.ObjectTable;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public interface XYChartRow extends Component, Container, Container.Viewer,
        Container.ItemSetChangeNotifier, Container.ItemSetChangeListener,
        Container.PropertySetChangeNotifier, Container.PropertySetChangeListener {

    Collection<?> getPointIds();
    void addPoint(Object id);

    Collection<?> getPropertyIds();
    void addProperty(Object id);

    Object getXPropertyId();
    void setXPropertyId(Object propertyId);

    Object getYPropertyId();
    void setYPropertyId(Object propertyId);

    Object getXValue(Object pointId);
    void setXValue(Object pointId, Object value);

    Object getYValue(Object pointId);
    void setYValue(Object pointId, Object value);
}
