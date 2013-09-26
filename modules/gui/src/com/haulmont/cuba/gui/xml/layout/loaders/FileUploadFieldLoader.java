/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class FileUploadFieldLoader extends ComponentLoader {

    public FileUploadFieldLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Component component = factory.createComponent(element.getName());
        
        loadId(component, element);
        loadEnable(component, element);
        loadVisible(component, element);
     
        loadStyleName(component, element);
        loadAlign(component, element);

        loadHeight(component, element);
        loadWidth(component, element);
        
        loadCaption((Component.HasCaption) component, element);
        loadDescription((Component.HasCaption) component, element);

        assignFrame((Component.BelongToFrame)component);

        return component;
    }
}