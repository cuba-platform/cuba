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

import com.haulmont.cuba.gui.components.CssLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 */
public class CssLayoutLoader extends ContainerLoader<CssLayout> {
    @Override
    public void createComponent() {
        resultComponent = (CssLayout) factory.createComponent(CssLayout.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadId(resultComponent, element);
        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadResponsive(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadSubComponents();
    }

    protected void loadResponsive(CssLayout component, Element element) {
        String responsive = element.attributeValue("responsive");
        if (StringUtils.isNotEmpty(responsive)) {
            component.setResponsive(BooleanUtils.toBoolean(element.attributeValue("responsive")));
        }
    }
}
