/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 9:52:04
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.OrderedLayout;
import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.data.DsContext;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Constructor;

public abstract class ContainerLoader extends ComponentLoader {
    protected ComponentsFactory factory;
    protected DsContext dsContext;
    protected LayoutLoaderConfig config;

    public ContainerLoader(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
        this.config = config;
        this.factory = factory;
        this.dsContext = dsContext;
    }

    protected Collection<Component> loadSubComponents(Component component, Element element, String ...exceptTags) {
        final List<Component> res = new ArrayList<Component>();

        final LayoutLoader loader = new LayoutLoader(factory, config, dsContext);
        for (Element subElement : (Collection<Element>)element.elements()) {
            final String name = subElement.getName();
            if (exceptTags != null && Arrays.binarySearch(exceptTags, name) < 0) {
                final Component subComponent = loader.loadComponent(subElement);
                ((Component.Container) component).add(subComponent);
                res.add(subComponent);
            }
        }

        return res;
    }

    protected void loadSubcomponentsAndExpand(OrderedLayout layout, Element element, String ...exceptTags) {
        final Collection<Component> components = loadSubComponents(layout, element, exceptTags);

        final String expand = element.attributeValue("expand");
        if (!StringUtils.isEmpty(expand)) {
            final String[] parts = expand.split(";");
            final Component componentToExpand = ((Component.Container) layout).getComponent(parts[0]);

            if (componentToExpand != null) {
                String height = find(parts, "height");
                String width = find(parts, "width");
                layout.expand(
                        componentToExpand,
                        height == null && width == null ? "100%" : height,
                        height == null && width == null ? "100%" : width);
            }
        }
    }

    private String find(String[] parts, String name) {
        for (String part : parts) {
            if (part.trim().startsWith(name + "=")) {
                return part.trim().substring((name + "=").length()).trim();
            }
        }
        return null;
    }


    protected com.haulmont.cuba.gui.xml.layout.ComponentLoader getLoader(String name) throws IllegalAccessException, InstantiationException {
        Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loaderClass = config.getLoader(name);
        if (loaderClass == null) {
            throw new IllegalStateException(String.format("Unknown component '%s'", name));
        }

        com.haulmont.cuba.gui.xml.layout.ComponentLoader loader;
        try {
            final Constructor<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> constructor =
                    loaderClass.getConstructor(LayoutLoaderConfig.class, ComponentsFactory.class, DsContext.class);
            loader = constructor.newInstance(config, factory, dsContext);

            loader.setLocale(locale);
        } catch (Throwable e) {
            loader = loaderClass.newInstance();
        }

        return loader;
    }
}
