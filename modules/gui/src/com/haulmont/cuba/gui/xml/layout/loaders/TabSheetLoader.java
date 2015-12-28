/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class TabSheetLoader extends ContainerLoader<TabSheet> {

    protected Map<Element, TabSheet.Tab> pendingLoadTabs = new LinkedHashMap<>();

    @Override
    public void createComponent() {
        resultComponent = (TabSheet) factory.createComponent(TabSheet.NAME);
        loadId(resultComponent, element);

        //noinspection unchecked
        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            final String name = tabElement.attributeValue("id");

            boolean lazy = Boolean.valueOf(tabElement.attributeValue("lazy"));
            ComponentLoader tabComponentLoader = getLoader(tabElement, TabComponentLoader.class);
            TabSheet.Tab tab;
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
        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        @SuppressWarnings("unchecked")
        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            TabSheet.Tab tab = pendingLoadTabs.remove(tabElement);
            if (tab != null) {
                String detachable = tabElement.attributeValue("detachable");
                if (StringUtils.isNotEmpty(detachable)) {
                    tab.setDetachable(Boolean.valueOf(detachable));
                }

                String caption = tabElement.attributeValue("caption");
                if (!StringUtils.isEmpty(caption)) {
                    caption = loadResourceString(caption);
                    tab.setCaption(caption);
                }

                String visible = tabElement.attributeValue("visible");
                if (StringUtils.isNotEmpty(visible)) {
                    tab.setVisible(Boolean.valueOf(visible));
                }

                String style = tabElement.attributeValue("stylename");
                if (style != null) {
                    tab.setStyleName(style);
                }

                String enable = tabElement.attributeValue("enable");
                if (!StringUtils.isEmpty(enable)) {
                    tab.setEnabled(Boolean.valueOf(enable));
                }
            }
        }

        loadSubComponents();
    }
}