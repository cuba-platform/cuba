/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:20:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.xml.layout.*;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class LabelLoader extends ComponentLoader {
    public LabelLoader(Context context) {
        super(context);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Label component = factory.createComponent("label");

        assignXmlDescriptor(component, element);
        loadId(component, element);

        String caption = element.attributeValue("value");
        if (!StringUtils.isEmpty(caption)) {
            if (caption.startsWith("res://") && resourceBundle != null) {
                caption = resourceBundle.getString(caption.substring(6));
            }
            component.setCaption(caption);
        }

        addAssignWindowTask(component);

        return component;
    }
}
