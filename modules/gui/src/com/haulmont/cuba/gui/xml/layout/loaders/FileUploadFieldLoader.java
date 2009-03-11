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
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.xml.layout.*;
import org.dom4j.Element;

public class FileUploadFieldLoader extends ComponentLoader{
    protected FileUploadFieldLoader(Context context) {
        super(context);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        FileUploadField component = factory.createComponent("upload");
        
        loadId(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        return component;
    }
}
