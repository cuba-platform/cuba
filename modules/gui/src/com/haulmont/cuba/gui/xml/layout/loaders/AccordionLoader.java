/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Accordion;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AccordionLoader extends ContainerLoader<Accordion> {
    protected Map<Element, Accordion.Tab> pendingLoadTabs = new LinkedHashMap<>();

    @Override
    public void createComponent() {
        resultComponent = factory.create(Accordion.NAME);
        loadId(resultComponent, element);

        LayoutLoader layoutLoader = getLayoutLoader();

        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            String name = tabElement.attributeValue("id");

            boolean lazy = Boolean.parseBoolean(tabElement.attributeValue("lazy"));

            ComponentLoader tabComponentLoader = layoutLoader.getLoader(tabElement, TabComponentLoader.class);
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

        loadHtmlSanitizerEnabled(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadContextHelp(resultComponent, element);
        loadResponsive(resultComponent, element);
        loadCss(resultComponent, element);

        loadTabCaptionsAsHtml(resultComponent, element);

        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            Accordion.Tab tab = pendingLoadTabs.remove(tabElement);
            if (tab != null) {
                loadIcon(tab, tabElement);

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

    protected void loadTabCaptionsAsHtml(Accordion resultComponent, Element element) {
        String tabCaptionsAsHtml = element.attributeValue("tabCaptionsAsHtml");
        if (StringUtils.isNotEmpty(tabCaptionsAsHtml)) {
            resultComponent.setTabCaptionsAsHtml(Boolean.parseBoolean(tabCaptionsAsHtml));
        }
    }
}