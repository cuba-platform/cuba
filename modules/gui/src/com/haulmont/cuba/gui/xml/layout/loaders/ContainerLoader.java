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
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ContainerLoader<T extends Component> extends AbstractComponentLoader<T> {

    protected List<ComponentLoader> pendingLoadComponents = new ArrayList<>();

    protected void loadSubComponents() {
        for (ComponentLoader componentLoader : pendingLoadComponents) {
            componentLoader.loadComponent();
        }

        pendingLoadComponents.clear();
    }

    @Override
    public void setMessagesPack(String messagesPack) {
        super.setMessagesPack(messagesPack);

        for (ComponentLoader loader : pendingLoadComponents) {
            if (!(loader instanceof FragmentComponentLoader) && !(loader instanceof RuntimePropertiesFrameLoader)) {
                loader.setMessagesPack(messagesPack);
            }
        }
    }

    protected void loadSpacing(HasSpacing layout, Element element) {
        String spacing = element.attributeValue("spacing");
        if (StringUtils.isNotEmpty(spacing)) {
            layout.setSpacing(Boolean.parseBoolean(spacing));
        }
    }

    protected void createSubComponents(ComponentContainer container, Element containerElement) {
        LayoutLoader loader = beanLocator.getPrototype(LayoutLoader.NAME, context);
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());

        for (Element subElement : containerElement.elements()) {
            if (!isChildElementIgnored(subElement)) {
                ComponentLoader componentLoader = loader.createComponent(subElement);
                pendingLoadComponents.add(componentLoader);

                container.add(componentLoader.getResultComponent());
            }
        }
    }

    /**
     * Checks if child element should have a separate loader or not.
     * For instance, if a child element doesn't represent a UI component.
     *
     * @param subElement the element to be checked
     * @return {@code true} if child element should have no separate loader, {@code false} otherwise
     */
    protected boolean isChildElementIgnored(Element subElement) {
        return false;
    }

    protected void loadSubComponentsAndExpand(ExpandingLayout layout, Element element) {
        loadSubComponents();

        String expand = element.attributeValue("expand");
        if (StringUtils.isNotEmpty(expand)) {
            if (!expand.contains(";")) {
                Component componentToExpand = layout.getOwnComponent(expand);
                if (componentToExpand == null) {
                    throw new GuiDevelopmentException(
                            String.format("There is no component with id '%s' to expand", expand), context);
                }
                layout.expand(componentToExpand);
            } else {
                // legacy behaviour

                String[] parts = expand.split(";");
                String targetId = parts[0];
                Component componentToExpand = layout.getOwnComponent(targetId);

                if (componentToExpand != null) {
                    String height = find(parts, "height");
                    String width = find(parts, "width");
                    layout.expand(componentToExpand, height, width);
                } else {
                    throw new GuiDevelopmentException(
                            String.format("Illegal expand target '%s' for container", targetId),
                            context, "component", targetId);
                }
            }
        }
    }

    protected void setComponentsRatio(ComponentContainer resultComponent, Element element) {
        if (!(resultComponent instanceof SupportsExpandRatio)) {
            return;
        }

        List<Element> elements = element.elements();
        if (elements.isEmpty()) {
            return;
        }

        SupportsExpandRatio supportsRatio = (SupportsExpandRatio) resultComponent;
        List<Component> ownComponents = resultComponent.getOwnComponentsStream().collect(Collectors.toList());
        if (ownComponents.size() != elements.size()) {
            return;
        }

        for (int i = 0; i < elements.size(); i++) {
            String stringRatio = elements.get(i).attributeValue("box.expandRatio");
            if (!Strings.isNullOrEmpty(stringRatio)) {

                Component subComponent = ownComponents.get(i);
                if (subComponent != null) {
                    float ratio = Float.parseFloat(stringRatio);
                    supportsRatio.setExpandRatio(subComponent, ratio);
                }
            }
        }
    }

    protected String find(String[] parts, String name) {
        String prefix = name + "=";

        for (String part : parts) {
            if (part.trim().startsWith(prefix)) {
                return part.trim().substring((prefix).length()).trim();
            }
        }
        return null;
    }
}