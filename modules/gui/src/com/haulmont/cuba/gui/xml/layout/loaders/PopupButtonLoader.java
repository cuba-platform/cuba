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

import com.haulmont.cuba.gui.components.PopupButton;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 */
public class PopupButtonLoader extends AbstractComponentLoader<PopupButton> {
    @Override
    public void createComponent() {
        resultComponent = (PopupButton) factory.createComponent(PopupButton.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadAlign(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadIcon(resultComponent, element);

        loadWidth(resultComponent, element);

        loadActions(resultComponent, element);
        loadIconsEnabled(resultComponent, element);

        String menuWidth = element.attributeValue("menuWidth");
        if (!StringUtils.isEmpty(menuWidth)) {
            resultComponent.setMenuWidth(menuWidth);
        }
    }

    protected void loadIconsEnabled(PopupButton component, Element element) {
        String iconsEnabled = element.attributeValue("iconsEnabled");
        if (!StringUtils.isEmpty(iconsEnabled)) {
            component.setIconsEnabled(Boolean.parseBoolean(iconsEnabled));
        }
    }
}