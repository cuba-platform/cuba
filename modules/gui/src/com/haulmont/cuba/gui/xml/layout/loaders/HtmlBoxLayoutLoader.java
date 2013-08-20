/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HtmlBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author gorodnov
 * @version $Id$
 */
public class HtmlBoxLayoutLoader extends ContainerLoader implements ComponentLoader {

    public HtmlBoxLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException {

        final HtmlBoxLayout component = factory.createComponent(HtmlBoxLayout.NAME);

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);

        loadTemplate(component, element);
        loadSubComponents(component, element, "visible");

        loadHeight(component, element);
        loadWidth(component, element);

        assignFrame(component);

        return component;
    }

    protected void loadTemplate(HtmlBoxLayout htmlBox, Element element) {
        String template = element.attributeValue("template");
        if (!StringUtils.isEmpty(template)) {
            htmlBox.setTemplateName(template);
            return;
        }
        throw new DevelopmentException("'template' attribute is required", context.getFullFrameId());
    }
}
