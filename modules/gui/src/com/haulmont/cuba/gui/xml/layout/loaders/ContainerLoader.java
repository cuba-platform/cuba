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
import com.haulmont.cuba.gui.components.ExpandingLayout;
import com.haulmont.cuba.gui.components.QuasiComponent;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class ContainerLoader extends ComponentLoader {
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    public ContainerLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    protected Collection<Component> loadSubComponents(Component component, Element element, String ...exceptTags) {
        final List<Component> res = new ArrayList<Component>();

        final LayoutLoader loader = new LayoutLoader(context, factory, config);
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());
        
        for (Element subElement : (Collection<Element>)element.elements()) {
            final String name = subElement.getName();
            if (exceptTags != null && Arrays.binarySearch(exceptTags, name) < 0) {
                final Component subComponent = loader.loadComponent(subElement, component);
                if (subComponent instanceof QuasiComponent) {
                    for (Component realSubComponent : ((QuasiComponent) subComponent).getRealComponents()) {
                        ((Component.Container) component).add(realSubComponent);
                        res.add(realSubComponent);
                    }
                } else {
                    ((Component.Container) component).add(subComponent);
                    res.add(subComponent);
                }
            }
        }

        return res;
    }

    protected void loadSpacing(Component.Spacing layout, Element element) {
        final String spacing = element.attributeValue("spacing");
        if (!StringUtils.isEmpty(spacing) && isBoolean(spacing)) {
            layout.setSpacing(Boolean.valueOf(spacing));
        }
    }

    protected void loadMargin(Component.Margin layout, Element element) {
        final String margin = element.attributeValue("margin");
        if (!StringUtils.isEmpty(margin)) {
            if (margin.indexOf(";") > -1) {
                final String[] margins = margin.split(";");
                if (margins.length != 4) {
                    throw new IllegalStateException(
                            "Margin attribute must contains 1 or 4 boolean values separeated by \";\"");
                }
                layout.setMargin(Boolean.valueOf(margins[0]), Boolean.valueOf(margins[1]),
                        Boolean.valueOf(margins[2]), Boolean.valueOf(margins[3]));
            } else if (isBoolean(margin)){
                layout.setMargin(Boolean.valueOf(margin));
            }
        }
    }

    protected void loadSubComponentsAndExpand(ExpandingLayout layout, Element element, String ...exceptTags) {
        final Collection<Component> components = loadSubComponents(layout, element, exceptTags);
        if (components.size() == 1) {
            final Component component = components.iterator().next();
            if (!(component instanceof Component.Expandable)
                    || ((Component.Expandable) component).isExpandable())
            {
                if (component instanceof Component.HasXmlDescriptor) {
                    final Element xmlDescriptor = ((Component.HasXmlDescriptor) component).getXmlDescriptor();
                    if (xmlDescriptor != null) {
                        layout.expand(component,
                                xmlDescriptor.attributeValue("height"),
                                xmlDescriptor.attributeValue("width")
                        );
                        return;
                    }
                }
                layout.expand(component, null, null);
            }
        } else {
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
                    loaderClass.getConstructor(Context.class, LayoutLoaderConfig.class, ComponentsFactory.class);
            loader = constructor.newInstance(context, config, factory);

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        } catch (Throwable e) {
            loader = loaderClass.newInstance();
            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        }

        return loader;
    }
}
