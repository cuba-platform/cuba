package com.haulmont.cuba.gui.xml.layout.loaders.charts;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.charts.CategoryChart;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.data.CategoryChartDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public abstract class AbstractCategoryChartLoader extends AbstractChartLoader {
    public AbstractCategoryChartLoader(Context context) {
        super(context);
    }

    public CategoryChart loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException{

        CategoryChart component = (CategoryChart)super.loadComponent(factory, element, parent);

        loadDatasource(component, element);

        return component;
    }

    protected void loadDatasource(CategoryChart component, Element element) {
        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            CollectionDatasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new IllegalStateException("Cannot find data source by name: " + datasource);
            }

            if (!(ds instanceof CategoryChartDatasource)) {
                loadCategories(component, element, ds);

                String captionProperty = element.attributeValue("captionProperty");
                if (!StringUtils.isEmpty(captionProperty)) {
                    MetaProperty property = ds.getMetaClass().getProperty(captionProperty);
                    if (property == null) {
                        throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                                captionProperty, ds.getMetaClass().getName()));
                    }
                    component.setRowCaptionProperty(property);
                }
            }

            component.setCollectionDatasource(ds);
        }
    }

    protected void loadCategories(CategoryChart component, Element element, CollectionDatasource ds) {
        List<Element> categoryElements = element.elements("category");
        for (final Element categoryElement : categoryElements) {
            loadCategory(component, categoryElement, ds);
        }
    }

    protected void loadCategory(CategoryChart component, Element element, CollectionDatasource ds) {
        String valueProperty = element.attributeValue("valueProperty");
        MetaProperty property = ds.getMetaClass().getProperty(valueProperty);
        if (property == null) {
            throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                    valueProperty, ds.getMetaClass().getName()));
        }

        String caption = element.attributeValue("caption");
        if (!StringUtils.isEmpty(caption)) {
            caption = loadResourceString(caption);
        } else {
            caption = MessageUtils.getPropertyCaption(property);
        }

        component.addCategory(property, caption);
    }
}
