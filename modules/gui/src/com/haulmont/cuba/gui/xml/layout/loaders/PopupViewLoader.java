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

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PopupView;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class PopupViewLoader extends AbstractComponentLoader<PopupView> {
    private ComponentLoader popupComponentLoader;

    @Override
    public void createComponent() {
        resultComponent = (PopupView) factory.createComponent(PopupView.NAME);
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
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadIcon(resultComponent, element);
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

    protected void createContent() {
        if (element != null) {
            LayoutLoader loader = new LayoutLoader(context, factory, layoutLoaderConfig);
            loader.setLocale(getLocale());
            loader.setMessagesPack(getMessagesPack());

            Element innerElement = (Element) element.elements().get(0);
            if (innerElement != null) {
                popupComponentLoader = loader.createComponent(innerElement);
                resultComponent.setPopupContent(popupComponentLoader.getResultComponent());
            }
        }
    }

    protected void loadPopupComponent() {
        if (popupComponentLoader != null) {
            popupComponentLoader.loadComponent();
        }
    }
}
