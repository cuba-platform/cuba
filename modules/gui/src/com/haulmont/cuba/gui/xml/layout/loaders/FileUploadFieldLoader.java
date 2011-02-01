/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 11.03.2009 18:14:23
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

public class FileUploadFieldLoader extends ComponentLoader{
    private static final long serialVersionUID = 5698387638929530175L;

    public FileUploadFieldLoader(Context context) {
        super(context);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        Component component = factory.createComponent(element.getName());
        
        loadId(component, element);
        loadVisible(component, element);
     
        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);
        
        loadCaption((Component.HasCaption) component, element);
        loadDescription((Component.HasCaption) component, element);

        loadExpandable((Component.Expandable)component, element);

        assignFrame((Component.BelongToFrame)component);

        return component;
    }
}
