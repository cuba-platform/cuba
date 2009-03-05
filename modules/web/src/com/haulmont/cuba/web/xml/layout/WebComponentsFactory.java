/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 16:05:08
 * $Id$
 */
package com.haulmont.cuba.web.xml.layout;

import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.gui.Window;
import com.haulmont.cuba.web.gui.components.*;

import java.util.Map;
import java.util.HashMap;

public class WebComponentsFactory implements ComponentsFactory {

    private static Map<String, Class<? extends Component>> classes = new HashMap<String, Class<?extends Component>>();

    static {
        classes.put("window", Window.class);
        classes.put("window.editor", Window.Editor.class);
        classes.put("window.lookup", Window.Lookup.class);

        classes.put("hbox", HBoxLayout.class);
        classes.put("vbox", VBoxLayout.class);
        classes.put("grid", GridLayout.class);

        classes.put("button", Button.class);
        classes.put("label", Label.class);
        classes.put("checkBox", CheckBox.class);
        classes.put("groupBox", GroupBox.class);
        classes.put("textField", TextField.class);
        classes.put("textArea", TextArea.class);
        classes.put("iframe", IFrame.class);
        classes.put("table", Table.class);
        classes.put("dateField", DateField.class);
        classes.put("lookupField", LookupField.class);
        classes.put("pickerField", PickerField.class);
        classes.put("split", SplitPanel.class);
        classes.put("tree", Tree.class);
        classes.put("tabsheet", Tabsheet.class);
    }

    public <T extends Component> T createComponent(String name) throws InstantiationException, IllegalAccessException {
        final Class<Component> componentClass = (Class<Component>) classes.get(name);
        if (componentClass == null) {
            throw new IllegalStateException(String.format("Can't find component class for '%s'", name));
        }
        return (T) componentClass.newInstance();
    }
}
