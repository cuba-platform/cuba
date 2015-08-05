/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TreeLoader extends ActionsHolderLoader {

    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    public TreeLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Tree component = factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(Tree component, Element element, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadEnable(component, element);
        loadEditable(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadStyleName(component, element);

        assignFrame(component);

        loadActions(component, element);

        String multiselect = element.attributeValue("multiselect");
        if (StringUtils.isNotEmpty(multiselect)) {
            component.setMultiSelect(Boolean.valueOf(multiselect));
        }

        Element itemsElem = element.element("treechildren");
        String datasource = itemsElem.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            HierarchicalDatasource ds = context.getDsContext().get(datasource);
            component.setDatasource(ds);

            String captionProperty = itemsElem.attributeValue("captionProperty");
            if (!StringUtils.isEmpty(captionProperty)) {
                component.setCaptionProperty(captionProperty);
                component.setCaptionMode(CaptionMode.PROPERTY);
            }
        }
    }
}