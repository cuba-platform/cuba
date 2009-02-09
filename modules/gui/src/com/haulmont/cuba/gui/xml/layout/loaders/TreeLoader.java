/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.01.2009 14:58:19
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class TreeLoader extends ComponentLoader
{
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    public TreeLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    public Component loadComponent(ComponentsFactory factory, Element element)
            throws InstantiationException, IllegalAccessException
    {
        Tree component = factory.createComponent("tree");

        assignXmlDescriptor(component, element);
        loadId(component, element);

        Element itemsElem = element.element("treechildren");
        String datasource = itemsElem.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            CollectionDatasource ds = context.getDSContext().get(datasource);
            String showProperty = itemsElem.attributeValue("property");
            String parentProperty = itemsElem.attributeValue("parent");

            component.setDatasource(ds, showProperty, parentProperty);
        }

        addAssignWindowTask(component);

        return component;
    }
}
