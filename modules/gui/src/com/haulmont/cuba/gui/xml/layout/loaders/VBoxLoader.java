/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 17:24:51
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;

public class VBoxLoader extends ContainerLoader implements ComponentLoader {
    public VBoxLoader(LayoutLoaderConfig config, ComponentsFactory factory) {
        super(config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Component component = factory.createComponent("vbox");

        loadAlign(component, element);
        loadPack(component, element);

        loadSubComponents(component, element);

        return component;
    }
}
