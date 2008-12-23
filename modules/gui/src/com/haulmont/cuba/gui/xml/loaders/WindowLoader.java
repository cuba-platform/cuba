/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:27:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.ComponentsFactory;
import com.haulmont.cuba.gui.xml.ComponentLoader;
import com.haulmont.cuba.gui.xml.ComponentsLoaderConfig;
import com.haulmont.cuba.gui.xml.ComponentsLoader;
import org.dom4j.Element;

import java.util.Collection;

public class WindowLoader extends ContainerLoader implements ComponentLoader {

    public WindowLoader(ComponentsLoaderConfig config, ComponentsFactory factory) {
        super(config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Window window = factory.createComponent("window");

        loadSubComponents(window, element);

        return window;
    }

}
