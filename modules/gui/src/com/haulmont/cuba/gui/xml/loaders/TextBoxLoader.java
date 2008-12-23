/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:20:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextBox;
import com.haulmont.cuba.gui.xml.ComponentsFactory;
import org.dom4j.Element;

public class TextBoxLoader extends ComponentLoader {
    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final TextBox textBox = factory.createComponent("textbox");

        loadFlex(textBox, element);
        loadCaption(textBox, element);

        return textBox;
    }
}