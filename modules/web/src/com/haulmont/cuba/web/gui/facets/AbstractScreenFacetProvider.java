/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.gui.facets;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.ScreenFacet;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.sys.UiControllerProperty;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Base class for screen facet providers.
 *
 * @param <T> screen facet type
 */
public abstract class AbstractScreenFacetProvider<T extends ScreenFacet>
        implements FacetProvider<T> {

    @Override
    public void loadFromXml(T facet, Element element, ComponentLoader.ComponentContext context) {
        loadId(facet, element);
        loadScreenId(facet, element);
        loadScreenClass(facet, element, context);
        loadOpenMode(facet, element);
        loadProperties(facet, element, context);
        loadTarget(facet, element, context);
    }

    protected void loadId(T facet, Element element) {
        String id = element.attributeValue("id");
        if (isNotEmpty(id)) {
            facet.setId(id);
        }
    }

    protected void loadScreenId(T facet, Element element) {
        String screenId = element.attributeValue("screenId");
        if (isNotEmpty(screenId)) {
            facet.setScreenId(screenId);
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadScreenClass(T facet, Element element, ComponentLoader.ComponentContext context) {
        String screenClassFqn = element.attributeValue("screenClass");
        if (isNotEmpty(screenClassFqn)) {
            try {
                Class clazz = ReflectionHelper.loadClass(screenClassFqn);

                if (Screen.class.isAssignableFrom(clazz)) {
                    facet.setScreenClass(((Class<? extends Screen>) clazz));
                } else {
                    throw new GuiDevelopmentException(
                            String.format("Facet screen class '%s' does not extend Screen class", screenClassFqn),
                            context);
                }
            } catch (ClassNotFoundException e) {
                throw new GuiDevelopmentException(
                        String.format("Unable to load screen class: '%s'", screenClassFqn),
                        context);
            }
        }
    }

    protected void loadOpenMode(T facet, Element element) {
        String openMode = element.attributeValue("openMode");
        if (isNotEmpty(openMode)) {
            facet.setLaunchMode(OpenMode.valueOf(openMode));
        }
    }

    protected void loadProperties(T facet, Element element,
                                  ComponentLoader.ComponentContext context) {
        Element propertiesElement = element.element("properties");
        if (propertiesElement == null) {
            return;
        }

        List<Element> propertyElements = propertiesElement.elements("property");
        if (propertyElements.isEmpty()) {
            return;
        }

        List<UiControllerProperty> properties = new ArrayList<>(propertyElements.size());
        for (Element propElement : propertyElements) {
            properties.add(loadProperty(propElement, context));
        }

        facet.setProperties(properties);
    }

    protected UiControllerProperty loadProperty(Element property, ComponentLoader.ComponentContext context) {
        String name = property.attributeValue("name");
        if (name == null || name.isEmpty()) {
            throw new GuiDevelopmentException("Screen property cannot have empty name", context);
        }

        String value = property.attributeValue("value");
        String ref = property.attributeValue("ref");

        if (StringUtils.isNotEmpty(value)
                && StringUtils.isNotEmpty(ref)) {
            throw new GuiDevelopmentException(
                    "Screen property can have either a value or a reference. Property: " + name, context);
        }

        if (StringUtils.isNotEmpty(value)) {
            return new UiControllerProperty(name, value, UiControllerProperty.Type.VALUE);
        } else if (StringUtils.isNotEmpty(ref)) {
            return new UiControllerProperty(name, ref, UiControllerProperty.Type.REFERENCE);
        } else {
            throw new GuiDevelopmentException("No value or reference found for screen property: " + name,
                    context);
        }
    }

    protected void loadTarget(T facet, Element element,
                              ComponentLoader.ComponentContext context) {
        String actionTarget = element.attributeValue("onAction");
        String buttonTarget = element.attributeValue("onButton");

        if (isNotEmpty(actionTarget)
                && isNotEmpty(buttonTarget)) {
            throw new GuiDevelopmentException(
                    "Screen facet should have either action or button target",
                    context);
        }

        if (isNotEmpty(actionTarget)) {
            facet.setActionTarget(actionTarget);
        } else if (isNotEmpty(buttonTarget)) {
            facet.setButtonTarget(buttonTarget);
        }
    }
}
