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
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.PopupView;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.List;

public class PopupViewLoader extends AbstractComponentLoader<PopupView> {
    protected ComponentLoader popupComponentLoader;

    @Override
    public void createComponent() {
        resultComponent = factory.create(PopupView.NAME);
        loadId(resultComponent, element);
        createContent();
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadPopupComponent();
        loadMinimizedValue(resultComponent, element);
        loadHideOnMouseOut(resultComponent, element);
        loadPopupVisible(resultComponent, element);
        loadCaptionAsHtml(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadPopupPosition(resultComponent, element);

        loadHtmlSanitizerEnabled(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadContextHelp(resultComponent, element);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadIcon(resultComponent, element);
        loadResponsive(resultComponent, element);
        loadCss(resultComponent, element);
    }

    protected void loadCaptionAsHtml(PopupView component, Element element) {
        String captionAsHtml = element.attributeValue("captionAsHtml");
        if (StringUtils.isNotEmpty(captionAsHtml)) {
            component.setCaptionAsHtml(Boolean.parseBoolean(captionAsHtml));
        }
    }

    protected void loadMinimizedValue(PopupView component, Element element) {
        String minimizedValue = element.attributeValue("minimizedValue");
        if (StringUtils.isNotEmpty(minimizedValue)) {
            component.setMinimizedValue(loadResourceString(minimizedValue));
        }
    }

    protected void loadPopupVisible(PopupView component, Element element) {
        String popupVisible = element.attributeValue("popupVisible");
        if (StringUtils.isNotEmpty(popupVisible)) {
            component.setPopupVisible(Boolean.parseBoolean(popupVisible));
        }
    }

    protected void loadHideOnMouseOut(PopupView component, Element element) {
        String hideOnMouseOut = element.attributeValue("hideOnMouseOut");
        if (StringUtils.isNotEmpty(hideOnMouseOut)) {
            component.setHideOnMouseOut(Boolean.parseBoolean(hideOnMouseOut));
        }
    }

    protected void loadPopupPosition(PopupView component, Element element) {
        String popupPosition = element.attributeValue("popupPosition");
        String popupTop = element.attributeValue("popupTop");
        String popupLeft = element.attributeValue("popupLeft");

        if (StringUtils.isNotEmpty(popupPosition)
                && (StringUtils.isNotEmpty(popupTop) || StringUtils.isNotEmpty(popupLeft))) {
            throw new GuiDevelopmentException(
                    "'popupTop' / 'popupLeft' and 'popupPosition' cannot be used in the same time for PopupView",
                    context);
        }

        if (StringUtils.isNotEmpty(popupPosition)) {
            PopupView.PopupPosition position = PopupView.PopupPosition.fromId(popupPosition);
            if (position != null) {
                component.setPopupPosition(position);
            }
        } else {
            if (StringUtils.isNotEmpty(popupTop)) {
                int top = Integer.parseInt(popupTop);

                if (top < 0) {
                    throw new GuiDevelopmentException("'popupTop' must be positive or zero for PopupView", context);
                }
                component.setPopupPositionTop(top);
            }

            if (StringUtils.isNotEmpty(popupLeft)) {
                int left = Integer.parseInt(popupLeft);

                if (left < 0) {
                    throw new GuiDevelopmentException("'popupLeft' must be positive or zero for PopupView", context);
                }
                component.setPopupPositionLeft(left);
            }
        }
    }

    protected void createContent() {
        if (element != null) {
            LayoutLoader loader = getLayoutLoader();

            List<Element> elements = element.elements();
            if (elements.size() != 0) {
                Element innerElement = elements.get(0);
                if (innerElement != null) {
                    popupComponentLoader = loader.createComponent(innerElement);
                    resultComponent.setPopupContent(popupComponentLoader.getResultComponent());
                }
            }
        }
    }

    protected void loadPopupComponent() {
        if (popupComponentLoader != null) {
            popupComponentLoader.loadComponent();
        }
    }
}