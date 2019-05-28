/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractComponentLoader;
import com.haulmont.cuba.web.gui.components.dynamicattributes.DynamicAttributesPanel;
import org.dom4j.Element;

public class DynamicAttributesPanelLoader extends AbstractComponentLoader<DynamicAttributesPanel> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(DynamicAttributesPanel.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadDataContainer(resultComponent, element);
        loadColumnsCount(resultComponent, element);
        loadRowsCount(resultComponent, element);
        loadFieldWidth(resultComponent, element);
        loadFieldCaptionWidth(resultComponent, element);
    }

    protected void loadDataContainer(DynamicAttributesPanel resultComponent, Element element) {
        String containerId = element.attributeValue("dataContainer");
        if (Strings.isNullOrEmpty(containerId)) {
            throw new GuiDevelopmentException("DynamicAttributesPanel element doesn't have 'dataContainer' attribute",
                    context, "DynamicAttributesPanel ID", element.attributeValue("id"));
        }
        FrameOwner frameOwner = getComponentContext().getFrame().getFrameOwner();
        ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);
        InstanceContainer container = screenData.getContainer(containerId);
        //noinspection unchecked
        resultComponent.setInstanceContainer(container);
    }

    protected void loadColumnsCount(DynamicAttributesPanel resultComponent, Element element) {
        resultComponent.setColumnsCount(getIntegerAttribute("cols", element));
    }

    protected void loadRowsCount(DynamicAttributesPanel resultComponent, Element element) {
        resultComponent.setRowsCount(getIntegerAttribute("rows", element));
    }

    protected void loadFieldWidth(DynamicAttributesPanel resultComponent, Element element) {
        String fieldWidth = element.attributeValue("fieldWidth");
        if (!Strings.isNullOrEmpty(fieldWidth)) {
            resultComponent.setFieldWidth(fieldWidth);
        }
    }

    protected void loadFieldCaptionWidth(DynamicAttributesPanel resultComponent, Element element) {
        String fieldWidth = element.attributeValue("fieldCaptionWidth");
        if (!Strings.isNullOrEmpty(fieldWidth)) {
            resultComponent.setFieldCaptionWidth(fieldWidth);
        }
    }

    protected Integer getIntegerAttribute(String attributeName, Element element) {
        String columnsCountStr = element.attributeValue(attributeName);
        if (!Strings.isNullOrEmpty(columnsCountStr)) {
            return Integer.parseInt(columnsCountStr);
        }
        return null;
    }
}