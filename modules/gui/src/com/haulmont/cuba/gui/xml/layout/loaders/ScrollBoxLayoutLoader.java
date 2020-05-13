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
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import org.dom4j.Element;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ScrollBoxLayoutLoader extends ContainerLoader<ScrollBoxLayout> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(ScrollBoxLayout.NAME);

        loadId(resultComponent, element);
        loadOrientation(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);
        assignXmlDescriptor(resultComponent, element);

        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadCss(resultComponent, element);

        loadAlign(resultComponent, element);
        loadScrollBars(resultComponent, element);

        loadSpacing(resultComponent, element);
        loadMargin(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadHtmlSanitizerEnabled(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadContextHelp(resultComponent, element);
        loadRequiredIndicatorVisible(resultComponent, element);

        loadSubComponents();

        loadContentSize(resultComponent, element);
    }

    protected void loadScrollBars(ScrollBoxLayout component, Element element) {
        String scrollBars = element.attributeValue("scrollBars");
        if (scrollBars == null) {
            return;
        }

        if ("horizontal".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.VERTICAL);
        } else if ("both".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.BOTH);
        } else if ("none".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.NONE);
        } else {
            throw new GuiDevelopmentException("Invalid scrollBox 'scrollBars' value: " + scrollBars, context);
        }
    }

    protected void loadContentSize(ScrollBoxLayout resultComponent, Element element) {
        String contentWidth = element.attributeValue("contentWidth");
        if (isNotEmpty(contentWidth)) {
            resultComponent.setContentWidth(contentWidth);
        }

        String contentHeight = element.attributeValue("contentHeight");
        if (isNotEmpty(contentHeight)) {
            resultComponent.setContentHeight(contentHeight);
        }

        String contentMinWidth = element.attributeValue("contentMinWidth");
        if (isNotEmpty(contentMinWidth)) {
            resultComponent.setContentMinWidth(contentMinWidth);
        }

        String contentMaxWidth = element.attributeValue("contentMaxWidth");
        if (isNotEmpty(contentMaxWidth)) {
            resultComponent.setContentMaxWidth(contentMaxWidth);
        }

        String contentMinHeight = element.attributeValue("contentMinHeight");
        if (isNotEmpty(contentMinHeight)) {
            resultComponent.setContentMinHeight(contentMinHeight);
        }

        String contentMaxHeight = element.attributeValue("contentMaxHeight");
        if (isNotEmpty(contentMaxHeight)) {
            resultComponent.setContentMaxHeight(contentMaxHeight);
        }
    }
}