/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

/**
 * @author subbotin
 * @version $Id$
 */
public class ResizableTextFieldLoader extends TextAreaLoader {

    public ResizableTextFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Component component = super.loadComponent(factory, element, parent);
        if (component instanceof ResizableTextArea) {
            ResizableTextArea textField = (ResizableTextArea) super.loadComponent(factory, element, parent);
            textField.setResizable(BooleanUtils.toBoolean(element.attributeValue("resizable")));
            return textField;
        } else {
            return component;
        }
    }
}