/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class LabelLoader extends AbstractDatasourceComponentLoader {

    public LabelLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final Label component = factory.createComponent("label");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadDatasource(component, element);

        loadVisible(component, element);

        loadAlign(component, element);

        loadStyleName(component, element);

        String caption = element.attributeValue("value");
        if (!StringUtils.isEmpty(caption)) {
            caption = loadResourceString(caption);
            component.setValue(caption);
        }

        loadWidth(component, element, "-1px");
        loadHeight(component, element, "-1px");

        component.setFormatter(loadFormatter(element));

        assignFrame(component);

        return component;
    }
}