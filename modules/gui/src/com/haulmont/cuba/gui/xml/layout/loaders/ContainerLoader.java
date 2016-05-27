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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ExpandingLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            if (!(loader instanceof FrameComponentLoader) && !(loader instanceof RuntimePropertiesFrameLoader)) {
                loader.setMessagesPack(messagesPack);
            }
        }
    }

    protected void loadSpacing(Component.Spacing layout, Element element) {
        String spacing = element.attributeValue("spacing");
        if (StringUtils.isNotEmpty(spacing)) {
            layout.setSpacing(Boolean.parseBoolean(spacing));
        }
    }

    protected void createSubComponents(Component.Container container, Element containerElement) {
        LayoutLoader loader = new LayoutLoader(context, factory, layoutLoaderConfig);
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());

        //noinspection unchecked
        for (Element subElement : (Collection<Element>) containerElement.elements()) {
            ComponentLoader componentLoader = loader.createComponent(subElement);
            pendingLoadComponents.add(componentLoader);

            container.add(componentLoader.getResultComponent());
        }
    }

    protected void loadSubComponentsAndExpand(ExpandingLayout layout, Element element) {
        loadSubComponents();

        String expand = element.attributeValue("expand");
        if (!StringUtils.isEmpty(expand)) {
            String[] parts = expand.split(";");
            String targetId = parts[0];
            Component componentToExpand = layout.getOwnComponent(targetId);

            if (componentToExpand != null) {
                String height = find(parts, "height");
                String width = find(parts, "width");
                layout.expand(componentToExpand, height, width);
            } else {
                throw new GuiDevelopmentException(
                        "Illegal expand target '" + targetId + "' for container",
                        context.getFullFrameId(), "component", targetId);
            }
        }
    }

    protected final String find(String[] parts, String name) {
        for (String part : parts) {
            if (part.trim().startsWith(name + "=")) {
                return part.trim().substring((name + "=").length()).trim();
            }
        }
        return null;
    }
}