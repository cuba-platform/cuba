/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 16:05:08
 * $Id$
 */
package com.haulmont.cuba.web.xml;

import com.haulmont.cuba.gui.xml.ComponentsFactory;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.ui.Screen;
import com.haulmont.cuba.web.components.*;

import java.util.Map;
import java.util.HashMap;

public class WebComponentsFactory implements ComponentsFactory {

    private static Map<String, Class<? extends Component>> classes = new HashMap<String, Class<?extends Component>>();

    static {
        classes.put("window", Screen.class);
        classes.put("hbox", HBox.class);
        classes.put("vbox", VBox.class);
        classes.put("button", Button.class);
        classes.put("label", Label.class);
        classes.put("groupbox", GroupBox.class);
        classes.put("textbox", TextBox.class);
        classes.put("iframe", IFrame.class);
    }

    public <T extends Component> T createComponent(String name) throws InstantiationException, IllegalAccessException {
        final Class<Component> componentClass = (Class<Component>) classes.get(name);
        return (T) componentClass.newInstance();
    }
}
