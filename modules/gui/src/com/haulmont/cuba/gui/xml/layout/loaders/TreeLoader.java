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

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.data.tree.ContainerTreeItems;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class TreeLoader extends ActionsHolderLoader<Tree> {

    protected Element buttonsPanelElement;
    protected ComponentLoader buttonsPanelLoader;

    @Override
    public void createComponent() {
        resultComponent = factory.create(Tree.NAME);
        loadId(resultComponent, element);
        createButtonsPanel(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);

        loadEnable(resultComponent, element);
        loadEditable(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadTabIndex(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadResponsive(resultComponent, element);
        loadCss(resultComponent, element);

        loadHtmlSanitizerEnabled(resultComponent, element);

        loadActions(resultComponent, element);

        loadButtonsPanel(resultComponent);

        String multiselect = element.attributeValue("multiselect");
        if (StringUtils.isNotEmpty(multiselect)) {
            resultComponent.setSelectionMode(Tree.SelectionMode.MULTI);
        }

        loadTreeChildren();

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadContextHelp(resultComponent, element);

        loadSelectionMode(resultComponent, element);

        loadRowHeight(resultComponent, element);
    }

    @SuppressWarnings("unchecked")
    protected void loadTreeChildren() {
        Element itemsElem = element.element("treechildren");

        String containerId = element.attributeValue("dataContainer");
        if (containerId != null) {
            FrameOwner frameOwner = getComponentContext().getFrame().getFrameOwner();
            ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);
            InstanceContainer container = screenData.getContainer(containerId);
            CollectionContainer collectionContainer;
            if (container instanceof CollectionContainer) {
                collectionContainer = (CollectionContainer) container;
            } else {
                throw new GuiDevelopmentException("Not a CollectionContainer: " + containerId, context);
            }
            String hierarchyProperty = element.attributeValue("hierarchyProperty");
            if (hierarchyProperty == null && itemsElem != null) {
                // legacy behaviour
                hierarchyProperty = itemsElem.attributeValue("hierarchyProperty");
            }

            if (Strings.isNullOrEmpty(hierarchyProperty)) {
                throw new GuiDevelopmentException("Tree doesn't have 'hierarchyProperty' attribute of the 'treechildren' element",
                        context, "Tree ID", element.attributeValue("id"));
            }
            resultComponent.setItems(new ContainerTreeItems(collectionContainer, hierarchyProperty));
        } else if (itemsElem != null) {
            String datasource = itemsElem.attributeValue("datasource");
            if (!StringUtils.isBlank(datasource)) {
                HierarchicalDatasource ds =
                        (HierarchicalDatasource) getComponentContext().getDsContext().get(datasource);
                resultComponent.setDatasource(ds);
            }
        }
        String captionProperty = element.attributeValue("captionProperty");
        if (captionProperty == null && itemsElem != null) {
            captionProperty = itemsElem.attributeValue("captionProperty");
        }

        if (!StringUtils.isEmpty(captionProperty)) {
            resultComponent.setCaptionProperty(captionProperty);
            resultComponent.setCaptionMode(CaptionMode.PROPERTY);
        }
    }

    protected void createButtonsPanel(Tree resultComponent, Element element) {
        buttonsPanelElement = element.element("buttonsPanel");
        if (buttonsPanelElement != null) {
            ButtonsPanelLoader loader = (ButtonsPanelLoader) getLayoutLoader().getLoader(buttonsPanelElement, ButtonsPanel.NAME);
            loader.createComponent();
            ButtonsPanel panel = loader.getResultComponent();

            resultComponent.setButtonsPanel(panel);

            buttonsPanelLoader = loader;
        }
    }

    protected void loadButtonsPanel(Tree component) {
        if (buttonsPanelLoader != null) {
            buttonsPanelLoader.loadComponent();
            ButtonsPanel panel = (ButtonsPanel) buttonsPanelLoader.getResultComponent();

            String alwaysVisible = buttonsPanelElement.attributeValue("alwaysVisible");
            if (alwaysVisible != null) {
                panel.setAlwaysVisible(Boolean.parseBoolean(alwaysVisible));
            }
        }
    }

    protected void loadSelectionMode(Tree component, Element element) {
        String selectionMode = element.attributeValue("selectionMode");
        if (StringUtils.isNotEmpty(selectionMode)) {
            component.setSelectionMode(Tree.SelectionMode.valueOf(selectionMode));
        }
    }

    protected void loadRowHeight(Tree component, Element element) {
        String rowHeight = element.attributeValue("rowHeight");
        if (!Strings.isNullOrEmpty(rowHeight)) {
            component.setRowHeight(Double.parseDouble(rowHeight));
        }
    }
}