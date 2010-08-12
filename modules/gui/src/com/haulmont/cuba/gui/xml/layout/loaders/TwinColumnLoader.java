/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.08.2010 16:57:59
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

@SuppressWarnings("serial")
public class TwinColumnLoader extends AbstractFieldLoader {
    public TwinColumnLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        TwinColumn component = (TwinColumn) super.loadComponent(factory, element, parent);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        String nullName = element.attributeValue("nullName");
        if (!StringUtils.isEmpty(captionProperty)) {
            nullName = loadResourceString(nullName);
            component.setNullOption(nullName);
        }

        String columns = element.attributeValue("columns");
        if (!StringUtils.isEmpty(columns)) {
            component.setColumns(Integer.parseInt(columns));
        }

        String rows = element.attributeValue("rows");
        if (!StringUtils.isEmpty(rows)) {
            component.setRows(Integer.parseInt(rows));
        }

        return component;
    }

    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        final String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDsContext().get(datasource);
            ((TwinColumn) component).setOptionsDatasource((CollectionDatasource) ds);
        }

        super.loadDatasource(component, element);
    }
}
