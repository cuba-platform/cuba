/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author Gorodnov
 * @version $Id$
 */
public class TwinColumnLoader extends AbstractFieldLoader {

    public TwinColumnLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        TwinColumn component = (TwinColumn) super.loadComponent(factory, element, parent);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        String columns = element.attributeValue("columns");
        if (!StringUtils.isEmpty(columns)) {
            component.setColumns(Integer.parseInt(columns));
        }

        String rows = element.attributeValue("rows");
        if (!StringUtils.isEmpty(rows)) {
            component.setRows(Integer.parseInt(rows));
        }

        String addBtnEnabled = element.attributeValue("addAllBtnEnabled");
        if (!StringUtils.isEmpty(addBtnEnabled)) {
            component.setAddAllBtnEnabled(Boolean.valueOf(addBtnEnabled));
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
