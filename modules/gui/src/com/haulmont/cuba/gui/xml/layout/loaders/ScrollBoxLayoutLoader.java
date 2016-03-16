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

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ScrollBoxLayoutLoader extends ContainerLoader<ScrollBoxLayout> {

    private Logger log = LoggerFactory.getLogger(getClass());

    protected void loadOrientation(ScrollBoxLayout component, Element element) {
        String orientation = element.attributeValue("orientation");
        if (orientation == null)
            return;

        if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(ScrollBoxLayout.Orientation.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(ScrollBoxLayout.Orientation.VERTICAL);
        } else {
            throw new GuiDevelopmentException("Invalid scrollbox orientation value: " + orientation, context.getFullFrameId());
        }
    }

    protected void loadScrollBars(ScrollBoxLayout component, Element element) {
        String scrollBars = element.attributeValue("scrollBars");
        if (scrollBars == null)
            return;

        if ("horizontal".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.VERTICAL);
        } else if ("both".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.BOTH);
        } else if ("none".equalsIgnoreCase(scrollBars)) {
            component.setScrollBarPolicy(ScrollBoxLayout.ScrollBarPolicy.NONE);
        } else {
            throw new GuiDevelopmentException("Invalid scrollbox 'scrollBars' value: " + scrollBars, context.getFullFrameId());
        }
    }

    @Override
    public void createComponent() {
        resultComponent = (ScrollBoxLayout) factory.createComponent(ScrollBoxLayout.NAME);
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

        loadAlign(resultComponent, element);
        loadScrollBars(resultComponent, element);

        loadSpacing(resultComponent, element);
        loadMargin(resultComponent, element);

        for (Component child : resultComponent.getOwnComponents()) {
            if (resultComponent.getOrientation() == ScrollBoxLayout.Orientation.VERTICAL && ComponentsHelper.hasFullHeight(child)) {
                child.setHeight("-1px");
                log.warn("100% height of " + child.getClass().getSimpleName() + " id=" + child.getId()
                        + " inside vertical scrollBox replaced with -1px height");
            }
            if (resultComponent.getOrientation() == ScrollBoxLayout.Orientation.HORIZONTAL && ComponentsHelper.hasFullWidth(child)) {
                child.setWidth("-1px");
                log.warn("100% width of " + child.getClass().getSimpleName() + " id=" + child.getId()
                        + " inside horizontal scrollBox replaced with -1px width");
            }
        }

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadSubComponents();
    }
}