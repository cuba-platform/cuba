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
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

public class FrameLoader extends ContainerLoader implements ComponentLoader {

    public FrameLoader(LayoutLoaderConfig config, ComponentsFactory factory) {
        super(config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final String src = element.attributeValue("src");
        final LayoutLoader loader = new LayoutLoader(factory, LayoutLoaderConfig.getFrameLoaders());

        final IFrame frame = (IFrame) loader.loadComponent(getClass().getResource(src));
        loadAlign(frame, element);
        loadPack(frame, element);

        loadFlex(frame, element);

        return frame;
    }

}