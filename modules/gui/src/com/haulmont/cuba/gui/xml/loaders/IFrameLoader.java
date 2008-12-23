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
import com.haulmont.cuba.gui.xml.ComponentsLoaderConfig;
import org.dom4j.Element;

public class IFrameLoader extends ContainerLoader implements ComponentLoader {

    public IFrameLoader(ComponentsLoaderConfig config, ComponentsFactory factory) {
        super(config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final IFrame frame = factory.createComponent("iframe");

        loadSubComponents(frame, element);

        return frame;
    }

}