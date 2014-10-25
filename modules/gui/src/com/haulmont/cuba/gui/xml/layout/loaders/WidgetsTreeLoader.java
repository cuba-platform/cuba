/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WidgetsTreeLoader extends TreeLoader {

    public WidgetsTreeLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Tree tree, Element element, Component parent) {
        WidgetsTree component = (WidgetsTree) tree;

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadWidth(component, element);
        loadHeight(component, element);

        loadStyleName(component, element);

        Element itemsElement = element.element("items");
        String datasource = itemsElement.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            HierarchicalDatasource ds = context.getDsContext().get(datasource);
            component.setDatasource(ds);
        }

        assignFrame(component);
    }
}