/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Link;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class LinkLoader extends AbstractDatasourceComponentLoader {

    public LinkLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final Link component = factory.createComponent("link");

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadVisible(component, element);
        loadAlign(component, element);
        loadStyleName(component, element);

        String caption = element.attributeValue("value");
        if (StringUtils.isNotEmpty(caption)) {
            caption = loadResourceString(caption);
            component.setCaption(caption);
        }

        String url = element.attributeValue("url");
        if (StringUtils.isNotEmpty(url)) {
            component.setUrl(url);
        }

        String target = element.attributeValue("target");
        if (StringUtils.isNotEmpty(target)) {
            component.setTarget(target);
        }

        loadWidth(component, element, Component.AUTO_SIZE);
        loadHeight(component, element, Component.AUTO_SIZE);

        assignFrame(component);

        return component;
    }
}