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

import com.haulmont.cuba.gui.components.HtmlBoxLayout;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class HtmlBoxLayoutLoader extends ContainerLoader<HtmlBoxLayout> {

    protected static final String TEMPLATE_CONTENTS_ELEMENT_NAME = "templateContents";

    @Override
    public void createComponent() {
        resultComponent = (HtmlBoxLayout) factory.createComponent(HtmlBoxLayout.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    protected boolean isChildElementIgnored(Element subElement) {
        return subElement.getName().equals(TEMPLATE_CONTENTS_ELEMENT_NAME);
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
        loadTemplateContents(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadSubComponents();
    }

    protected void loadTemplate(HtmlBoxLayout htmlBox, Element element) {
        String template = element.attributeValue("template");
        if (!StringUtils.isEmpty(template)) {
            htmlBox.setTemplateName(template);
        }
    }

    protected void loadTemplateContents(HtmlBoxLayout htmlBox, Element element) {
        Element templateContentsElement = element.element(TEMPLATE_CONTENTS_ELEMENT_NAME);
        if (templateContentsElement != null) {
            String templateContents = templateContentsElement.getText();
            if (StringUtils.isNotBlank(templateContents)) {
                htmlBox.setTemplateContents(templateContents);
            }
        }
    }
}