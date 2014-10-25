/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class LabelLoader extends AbstractDatasourceComponentLoader {

    public LabelLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Label component = factory.createComponent(element.getName());

        initComponent(element, component, parent);

        return component;
    }

    protected void initComponent(Element element, Label component, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadDatasource(component, element);

        loadVisible(component, element);

        loadAlign(component, element);

        loadStyleName(component, element);

        String htmlEnabled = element.attributeValue("htmlEnabled");
        if (StringUtils.isNotEmpty(htmlEnabled)) {
            component.setHtmlEnabled(Boolean.valueOf(htmlEnabled));
        }

        String caption = element.attributeValue("value");
        if (StringUtils.isNotEmpty(caption)) {
            caption = loadResourceString(caption);
            component.setValue(caption);
        }

        loadWidth(component, element, Component.AUTO_SIZE);
        loadHeight(component, element, Component.AUTO_SIZE);

        component.setFormatter(loadFormatter(element));

        assignFrame(component);
    }
}