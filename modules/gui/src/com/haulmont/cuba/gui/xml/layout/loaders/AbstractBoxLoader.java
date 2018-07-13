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
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;

import java.util.List;

public abstract class AbstractBoxLoader<T extends BoxLayout> extends ContainerLoader<T> {
    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadId(resultComponent, element);
        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadAlign(resultComponent, element);

        loadSpacing(resultComponent, element);
        loadMargin(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadSubComponentsAndExpand(resultComponent, element);
        loadResponsive(resultComponent, element);

        setComponentsRatio(resultComponent, element);
    }

    protected void setComponentsRatio(BoxLayout resultComponent, Element element) {
        List<Element> elements = element.elements();
        if (elements.isEmpty()) {
            return;
        }

        for (int i = 0; i < elements.size(); i++) {
            String stringRatio = elements.get(i).attributeValue("box.expandRatio");
            if (!Strings.isNullOrEmpty(stringRatio)) {

                Component subComponent = resultComponent.getComponent(i);
                if (subComponent != null) {
                    float ratio = Float.parseFloat(stringRatio);
                    resultComponent.setExpandRatio(subComponent, ratio);
                }
            }
        }
    }
}