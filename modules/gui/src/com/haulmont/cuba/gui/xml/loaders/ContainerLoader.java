/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 9:52:04
 * $Id$
 */
package com.haulmont.cuba.gui.xml.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.ComponentsFactory;
import com.haulmont.cuba.gui.xml.ComponentsLoaderConfig;
import com.haulmont.cuba.gui.xml.ComponentsLoader;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.Collection;

public abstract class ContainerLoader extends ComponentLoader {
    protected ComponentsFactory factory;
    protected ComponentsLoaderConfig config;

    public ContainerLoader(ComponentsLoaderConfig config, ComponentsFactory factory) {
        this.config = config;
        this.factory = factory;
    }

    protected void loadSubComponents(Component component, Element element, String ...exceptTags) {
        final ComponentsLoader loader = new ComponentsLoader(factory, config);
        for (Element subElement : (Collection<Element>)element.elements()) {
            final String name = subElement.getName();
            if (exceptTags != null && Arrays.binarySearch(exceptTags, name) < 0) {
                final Component subComponent = loader.loadComponent(subElement);
                ((Component.Container) component).add(subComponent);
            }
        }
    }
}
