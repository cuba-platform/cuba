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

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.HtmlBoxLayout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 */
public class HtmlBoxLayoutLoader extends ContainerLoader<HtmlBoxLayout> {

    @Override
    public void createComponent() {
        resultComponent = (HtmlBoxLayout) factory.createComponent(HtmlBoxLayout.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadAlign(resultComponent, element);

        loadTemplate(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
    }

    protected void loadTemplate(HtmlBoxLayout htmlBox, Element element) {
        String template = element.attributeValue("template");
        if (!StringUtils.isEmpty(template)) {
            htmlBox.setTemplateName(template);
            return;
        }
        throw new GuiDevelopmentException("'template' attribute is required", context.getFullFrameId());
    }
}