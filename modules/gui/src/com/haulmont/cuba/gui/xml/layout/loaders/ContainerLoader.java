/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author abramov
 * @version $Id$
 */
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
        if (!StringUtils.isEmpty(spacing)) {
            layout.setSpacing(Boolean.valueOf(spacing));
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