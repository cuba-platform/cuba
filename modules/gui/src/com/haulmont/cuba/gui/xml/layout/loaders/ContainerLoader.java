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
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.data.DsContext;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

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
}
