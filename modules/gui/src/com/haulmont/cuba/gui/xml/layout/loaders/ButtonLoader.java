/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:55:55
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ButtonLoader extends com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoader {
    public ButtonLoader(Context context) {
        super(context);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Button component = factory.createComponent("button");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadCaption(component, element);
        loadAction(component, element);

        addAssignWindowTask(component);

        return component;
    }

    protected void loadAction(Button component, Element element) {
        final String actionName = element.attributeValue("action");
        if (!StringUtils.isEmpty(actionName)) {
            context.addLazyTask(new AssignActionLazyTask(component, actionName));
        }
    }

}
