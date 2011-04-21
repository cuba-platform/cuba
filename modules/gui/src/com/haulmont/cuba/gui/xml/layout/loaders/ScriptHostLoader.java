/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ScriptHostLoader extends ComponentLoader {
    private static final long serialVersionUID = -5747403317360243260L;

    public ScriptHostLoader(Context context) {
        super(context);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        Component component = factory.createComponent(element.getName());

        loadId(component, element);

        assignFrame((Component.BelongToFrame) component);

        return component;
    }
}