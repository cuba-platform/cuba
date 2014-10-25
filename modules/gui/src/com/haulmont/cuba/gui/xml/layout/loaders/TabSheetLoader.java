/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class TabSheetLoader extends ContainerLoader {

    public TabSheetLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        TabSheet component = factory.createComponent(TabSheet.NAME);

        initComponent(component, factory, element, parent);

        return component;
    }

    protected void initComponent(TabSheet component, ComponentsFactory factory, Element element, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        @SuppressWarnings("unchecked")
        final List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            final String name = tabElement.attributeValue("id");

            boolean lazy = Boolean.valueOf(tabElement.attributeValue("lazy"));

            final ComponentLoader tabComponentLoader = getLoader(BoxLayout.VBOX);
            final TabSheet.Tab tab;

            if (lazy) {
                tab = component.addLazyTab(name, tabElement, tabComponentLoader);
            } else {
                Component tabComponent = tabComponentLoader.loadComponent(factory, tabElement, null);
                tabComponent.setEnabled(true);
                tabComponent.setVisible(true);

                tab = component.addTab(name, tabComponent);
            }

            final String detachable = tabElement.attributeValue("detachable");
            if (StringUtils.isNotEmpty(detachable)) {
                tab.setDetachable(Boolean.valueOf(detachable));
            }

            String caption = tabElement.attributeValue("caption");

            if (!StringUtils.isEmpty(caption)) {
                caption = loadResourceString(caption);
                tab.setCaption(caption);
            }

            String enable = tabElement.attributeValue("enable");
            if (enable == null) {
                final Element e = tabElement.element("enable");
                if (e != null) {
                    enable = e.getText();
                }
            }

            String style = tabElement.attributeValue("stylename");
            if (style != null) {
                tab.setStyleName(style);
            }

            if (!StringUtils.isEmpty(enable)) {
                tab.setEnabled(Boolean.valueOf(enable));
            }
        }

        assignFrame(component);
    }
}