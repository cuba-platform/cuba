package com.haulmont.cuba.gui.xml.layout.loaders.charts;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.charts.XYChart;
import com.haulmont.cuba.gui.components.charts.XYChartRow;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.XYChartRowDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public abstract class AbstractXYChartLoader extends AbstractChartLoader {
    public AbstractXYChartLoader(Context context) {
        super(context);
    }

    public XYChart loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException {

        XYChart component = (XYChart)super.loadComponent(factory, element, parent);

        loadRows(factory, component, element);

        loadArgumentAxisType(component, element);
        loadValueAxisType(component, element);

        return component;
    }

    protected void loadRows(ComponentsFactory factory, XYChart component, Element element)
            throws InstantiationException, IllegalAccessException {

        List<Element> rowElements = element.elements("row");
        for (final Element rowElement : rowElements) {
            loadRow(factory, component, rowElement);
        }
    }

    protected void loadRow(ComponentsFactory factory, XYChart component, Element element)
            throws InstantiationException, IllegalAccessException {

        XYChartRow row = (XYChartRow)factory.createComponent(XYChartRow.NAME);

        loadId(row, element);
        loadCaption(row, element);

        String datasourceString = element.attributeValue("datasource");
        CollectionDatasource datasource = context.getDsContext().get(datasourceString);
        if (datasource == null) {
            throw new IllegalStateException("Cannot find data source by name: " + datasourceString);
        }

        if (!(datasource instanceof XYChartRowDatasource)) {
            String xPropertyString = element.attributeValue("xProperty");
            String yPropertyString = element.attributeValue("yProperty");

            MetaProperty xProperty = datasource.getMetaClass().getProperty(xPropertyString);
            MetaProperty yProperty = datasource.getMetaClass().getProperty(yPropertyString);
            if (xProperty == null) {
                throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                        xProperty, datasource.getMetaClass().getName()));
            }
            if (yProperty == null) {
                throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                        yProperty, datasource.getMetaClass().getName()));
            }

            row.setXProperty(xProperty);
            row.setYProperty(yProperty);
        }

        row.setCollectionDatasource(datasource);

        component.addRow(row);
    }

    protected void loadArgumentAxisType(XYChart component, Element element) {
        String axisType = element.attributeValue("argumentAxisType");
        if (!StringUtils.isEmpty(axisType)) {
            component.setArgumentAxisType(Chart.AxisType.valueOf(axisType));
        }
    }
}
