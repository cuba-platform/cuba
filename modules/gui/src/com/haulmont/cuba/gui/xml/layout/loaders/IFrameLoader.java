/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:27:37
 * $Id: IFrameLoader.java 69 2009-01-22 12:19:45Z abramov $
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.data.DsContext;
import org.dom4j.Element;

public class IFrameLoader extends ContainerLoader implements ComponentLoader {

    public IFrameLoader(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
        super(config, factory, dsContext);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final String src = element.attributeValue("src");
        final LayoutLoader loader = new LayoutLoader(factory, LayoutLoaderConfig.getFrameLoaders(), dsContext);

        final IFrame frame = (IFrame) loader.loadComponent(getClass().getResource(src));
        if (frame.getResourceBundle() == null) {
            frame.setResourceBundle(resourceBundle);
        }

        assignXmlDescriptor(frame, element);
        loadId(frame, element);
        loadAlign(frame, element);
        loadPack(frame, element);

        loadHeight(frame, element);
        loadWidth(frame, element);

        return frame;
    }

}