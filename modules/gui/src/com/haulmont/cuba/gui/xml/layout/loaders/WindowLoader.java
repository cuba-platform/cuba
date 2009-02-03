/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:27:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.data.DsContext;
import org.dom4j.Element;

public class WindowLoader extends FrameLoader implements ComponentLoader {

    public WindowLoader(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
        super(config, factory, dsContext);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Window window = createComponent(factory);

        assignXmlDescriptor(window, element);
        loadResourceBundle(window, element);
        loadCaption(window, element);

        loadSubComponents(window, element.element("layout"));

        return window;
    }

    protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
        return factory.createComponent("window");
    }

    public static class Editor extends WindowLoader {
        public Editor(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
            super(config, factory, dsContext);
        }

        @Override
        protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
            return factory.createComponent("window.editor");
        }
    }
}
