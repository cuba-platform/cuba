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
import com.haulmont.cuba.gui.components.OrderedLayout;
import com.haulmont.cuba.gui.data.DsContext;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class VBoxLoader extends ContainerLoader implements ComponentLoader {
    public VBoxLoader(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
        super(config, factory, dsContext);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final OrderedLayout component =
                StringUtils.isEmpty(element.attributeValue("expand")) ?
                        factory.<OrderedLayout>createComponent("vbox") :
                        factory.<OrderedLayout>createComponent("expandable-vbox");

        loadId(component, element);
        loadAlign(component, element);
        loadPack(component, element);

        loadSubcomponentsAndExpand(component, element);

        loadHeight(component, element);
        loadWidth(component, element);
        
        return component;
    }
}
