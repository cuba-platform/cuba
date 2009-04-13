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
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

public class WindowLoader extends FrameLoader implements ComponentLoader {

    public WindowLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Window window = createComponent(factory);

        assignXmlDescriptor(window, element);
        loadMessagesPack(window, element);
        loadCaption(window, element);

        final Element layoutElement = element.element("layout");
        loadSubcomponentsAndExpand(window, layoutElement);

        return window;
    }

    protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
        return factory.createComponent("window");
    }

    public static class Editor extends WindowLoader {
        public Editor(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
            super(context, config, factory);
        }

        @Override
        protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
            return factory.createComponent("window.editor");
        }
    }

    public static class Lookup extends WindowLoader {
        public Lookup(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
            super(context, config, factory);
        }

        @Override
        protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
            return factory.createComponent("window.lookup");
        }
    }
}
