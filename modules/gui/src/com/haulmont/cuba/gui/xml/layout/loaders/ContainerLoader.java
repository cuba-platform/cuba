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
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.data.DsContext;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.Collection;

public abstract class ContainerLoader extends ComponentLoader {
    protected ComponentsFactory factory;
    protected DsContext dsContext;
    protected LayoutLoaderConfig config;

    public ContainerLoader(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
        this.config = config;
        this.factory = factory;
        this.dsContext = dsContext;
    }

    protected void loadSubComponents(Component component, Element element, String ...exceptTags) {
        final LayoutLoader loader = new LayoutLoader(factory, config, dsContext);
        for (Element subElement : (Collection<Element>)element.elements()) {
            final String name = subElement.getName();
            if (exceptTags != null && Arrays.binarySearch(exceptTags, name) < 0) {
                final Component subComponent = loader.loadComponent(subElement);
                ((Component.Container) component).add(subComponent);
            }
        }
    }
}
