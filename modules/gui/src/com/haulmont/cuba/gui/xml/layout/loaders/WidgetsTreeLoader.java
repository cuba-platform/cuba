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

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class WidgetsTreeLoader extends ActionsHolderLoader<WidgetsTree> {

    protected Element buttonsPanelElement;
    protected ComponentLoader buttonsPanelLoader;

    @Override
    public void createComponent() {
        resultComponent = (WidgetsTree) factory.createComponent(WidgetsTree.NAME);
        loadId(resultComponent, element);
        createButtonsPanel(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadButtonsPanel(resultComponent);

        Element itemsElement = element.element("items");
        String datasource = itemsElement.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            HierarchicalDatasource ds = (HierarchicalDatasource) context.getDsContext().get(datasource);
            resultComponent.setDatasource(ds);
        }
    }

    protected void createButtonsPanel(Tree resultComponent, Element element) {
        buttonsPanelElement = element.element("buttonsPanel");
        if (buttonsPanelElement != null) {
            ButtonsPanelLoader loader = (ButtonsPanelLoader) getLoader(buttonsPanelElement, ButtonsPanel.NAME);
            loader.createComponent();
            ButtonsPanel panel = loader.getResultComponent();

            resultComponent.setButtonsPanel(panel);

            buttonsPanelLoader = loader;
        }
    }

    protected void loadButtonsPanel(Tree component) {
        if (buttonsPanelLoader != null) {
            //noinspection unchecked
            buttonsPanelLoader.loadComponent();
            ButtonsPanel panel = (ButtonsPanel) buttonsPanelLoader.getResultComponent();

            Window window = ComponentsHelper.getWindowImplementation(component);
            String alwaysVisible = buttonsPanelElement.attributeValue("alwaysVisible");
            panel.setVisible(!(window instanceof Window.Lookup) || "true".equals(alwaysVisible));
        }
    }
}