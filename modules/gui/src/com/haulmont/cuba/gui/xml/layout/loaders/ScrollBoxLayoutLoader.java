/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class ScrollBoxLayoutLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {

    private Log log = LogFactory.getLog(getClass());

    public ScrollBoxLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final ScrollBoxLayout component = factory.createComponent(ScrollBoxLayout.NAME);

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(ScrollBoxLayout component, Element element, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);
        loadOrientation(component, element);
        loadScrollBars(component, element);

        loadSpacing(component, element);
        loadMargin(component, element);

        loadSubComponents(component, element, "visible");

        for (Component child : component.getOwnComponents()) {
            if (component.getOrientation() == ScrollBoxLayout.Orientation.VERTICAL && ComponentsHelper.hasFullHeight(child)) {
                child.setHeight("-1px");
                log.warn("100% height of " + child.getClass().getSimpleName() + " id=" + child.getId()
                        + " inside vertical scrollBox replaced with -1px height");
            }
            if (component.getOrientation() == ScrollBoxLayout.Orientation.HORIZONTAL && ComponentsHelper.hasFullWidth(child)) {
                child.setWidth("-1px");
                log.warn("100% width of " + child.getClass().getSimpleName() + " id=" + child.getId()
                        + " inside horizontal scrollBox replaced with -1px width");
            }
        }

        loadHeight(component, element);
        loadWidth(component, element);

        assignFrame(component);
    }

    protected void loadOrientation(ScrollBoxLayout component, Element element) {
        String orientation = element.attributeValue("orientation");
        if (orientation == null)
            return;

        if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(ScrollBoxLayout.Orientation.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(ScrollBoxLayout.Orientation.VERTICAL);
        } else {
            throw new GuiDevelopmentException("Invalid scrollbox orientation value: " + orientation, context.getFullFrameId());
        }
    }

    protected void loadScrollBars(ScrollBoxLayout component, Element element) {
        String scrollBars = element.attributeValue("scrollBars");
        if (scrollBars == null)
            return;

        if ("horizontal".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.VERTICAL);
        } else if ("both".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.BOTH);
        } else if ("none".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.NONE);
        } else {
            throw new GuiDevelopmentException("Invalid scrollbox 'scrollBars' value: " + scrollBars, context.getFullFrameId());
        }
    }
}