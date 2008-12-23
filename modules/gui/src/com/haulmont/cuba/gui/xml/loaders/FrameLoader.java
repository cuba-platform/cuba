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
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.ComponentLoader;
import com.haulmont.cuba.gui.xml.ComponentsFactory;
import com.haulmont.cuba.gui.xml.ComponentsLoader;
import com.haulmont.cuba.gui.xml.ComponentsLoaderConfig;
import org.dom4j.Element;

public class FrameLoader extends ContainerLoader implements ComponentLoader {

    public FrameLoader(ComponentsLoaderConfig config, ComponentsFactory factory) {
        super(config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final String src = element.attributeValue("src");
        final ComponentsLoader loader = new ComponentsLoader(factory, ComponentsLoaderConfig.getFrameLoaders());

        final IFrame frame = (IFrame) loader.loadComponent(getClass().getResource(src));
        loadAlign(frame, element);
        loadPack(frame, element);

        loadFlex(frame, element);

        return frame;
    }

}