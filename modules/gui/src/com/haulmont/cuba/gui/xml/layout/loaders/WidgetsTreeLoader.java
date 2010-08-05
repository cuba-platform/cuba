/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 03.08.2010 17:55:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class WidgetsTreeLoader extends TreeLoader {

    public WidgetsTreeLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        WidgetsTree component = factory.createComponent("widgetsTree");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        Element itemsElem = element.element("items");
        String datasource = itemsElem.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            HierarchicalDatasource ds = context.getDsContext().get(datasource);
            component.setDatasource(ds);
        }

        assignFrame(component);

        return component;
    }
}
