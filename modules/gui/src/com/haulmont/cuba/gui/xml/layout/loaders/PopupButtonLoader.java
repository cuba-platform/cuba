/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author pavlov
 * @version $Id$
 */
public class PopupButtonLoader extends ComponentLoader {

    protected LayoutLoaderConfig config;
    protected ComponentsFactory factory;

    public PopupButtonLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        PopupButton component = factory.createComponent(element.getName());

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);
        loadEnable(component, element);
        loadAlign(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);
        loadDescription(component, element);
        loadIcon(component, element);

        loadWidth(component, element);

        assignFrame(component);

        loadActions(component, element);

        String menuWidth = element.attributeValue("menuWidth");
        if (!StringUtils.isEmpty(menuWidth)) {
            component.setMenuWidth(menuWidth);
        }

        return component;
    }
}