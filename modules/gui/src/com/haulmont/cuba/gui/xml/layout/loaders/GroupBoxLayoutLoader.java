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

import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.MarginInfo;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class GroupBoxLayoutLoader extends ContainerLoader<GroupBoxLayout> {

    @Override
    public void createComponent() {
        resultComponent = (GroupBoxLayout) factory.createComponent(GroupBoxLayout.NAME);
        loadId(resultComponent, element);
        loadOrientation(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadSettingsEnabled(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadAlign(resultComponent, element);
        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);

        loadOrientation(resultComponent, element);

        loadCollapsible(resultComponent, element, false);

        loadStyleName(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadSpacing(resultComponent, element);
        loadOuterMargin(resultComponent, element);

        loadSubComponentsAndExpand(resultComponent, element);
        loadShowAsPanel(resultComponent, element);
    }

    protected void loadOuterMargin(GroupBoxLayout resultComponent, Element element) {
        final String margin = element.attributeValue("outerMargin");
        if (!StringUtils.isEmpty(margin)) {
            MarginInfo marginInfo = parseMarginInfo(margin);
            resultComponent.setOuterMargin(marginInfo);
        }
    }

    protected void loadShowAsPanel(GroupBoxLayout component, Element element) {
        String showAsPanel = element.attributeValue("showAsPanel");
        if (StringUtils.isNotEmpty(showAsPanel)) {
            component.setShowAsPanel(Boolean.parseBoolean(showAsPanel));
        }
    }
}