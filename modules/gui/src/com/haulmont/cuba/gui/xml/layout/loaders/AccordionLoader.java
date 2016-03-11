/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Accordion;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author petunin
 */
public class AccordionLoader extends ContainerLoader<Accordion> {
    protected Map<Element, Accordion.Tab> pendingLoadTabs = new LinkedHashMap<>();

    @Override
    public void createComponent() {
        resultComponent = (Accordion) factory.createComponent(Accordion.NAME);
        loadId(resultComponent, element);

        //noinspection unchecked
        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            final String name = tabElement.attributeValue("id");

            boolean lazy = Boolean.parseBoolean(tabElement.attributeValue("lazy"));
            ComponentLoader tabComponentLoader = getLoader(tabElement, TabComponentLoader.class);
            Accordion.Tab tab;
            if (lazy) {
                tab = resultComponent.addLazyTab(name, tabElement, tabComponentLoader);
            } else {
                tabComponentLoader.createComponent();

                Component tabComponent = tabComponentLoader.getResultComponent();

                tab = resultComponent.addTab(name, tabComponent);

                pendingLoadComponents.add(tabComponentLoader);
            }

            pendingLoadTabs.put(tabElement, tab);
        }
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);
        assignXmlDescriptor(resultComponent, element);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        @SuppressWarnings("unchecked")
        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            Accordion.Tab tab = pendingLoadTabs.remove(tabElement);
            if (tab != null) {
                String caption = tabElement.attributeValue("caption");
                if (!StringUtils.isEmpty(caption)) {
                    caption = loadResourceString(caption);
                    tab.setCaption(caption);
                }

                String visible = tabElement.attributeValue("visible");
                if (StringUtils.isNotEmpty(visible)) {
                    tab.setVisible(Boolean.parseBoolean(visible));
                }

                String style = tabElement.attributeValue("stylename");
                if (style != null) {
                    tab.setStyleName(style);
                }

                String enable = tabElement.attributeValue("enable");
                if (StringUtils.isNotEmpty(enable)) {
                    tab.setEnabled(Boolean.parseBoolean(enable));
                }
            }
        }

        loadSubComponents();
    }
}
