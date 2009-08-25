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
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class LabelLoader extends AbstractDatasourceComponentLoader {
    public LabelLoader(Context context) {
        super(context);
    }

    public Component loadComponent(
            ComponentsFactory factory,
            Element element, Component parent
    ) throws InstantiationException, IllegalAccessException {
        final Label component = factory.createComponent("label");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        String caption = element.attributeValue("value");
        if (!StringUtils.isEmpty(caption)) {
            caption = loadResourceString(caption);
            component.setValue(caption);
        }

        loadWidth(component, element, "-1px");

        loadDatasource(component, element);

        component.setFormatter(loadFormatter(element));

        addAssignWindowTask(component);

        return component;
    }
}
