/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui;

import com.haulmont.cuba.desktop.gui.components.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopComponentsFactory implements ComponentsFactory {

    private static Map<String, Class<? extends Component>> classes = new HashMap<String, Class<?extends Component>>();

    static {
        classes.put(Window.NAME, DesktopWindow.class);
        classes.put(Window.Editor.NAME, DesktopWindow.Editor.class);
        classes.put(Window.Lookup.NAME, DesktopWindow.Lookup.class);

        classes.put(IFrame.NAME, DesktopFrame.class);
        classes.put("hbox", DesktopHBox.class);
        classes.put("vbox", DesktopVBox.class);
        classes.put(GridLayout.NAME, DesktopGridLayout.class);
        classes.put(SplitPanel.NAME, DesktopSplitPanel.class);

        classes.put(Button.NAME, DesktopButton.class);
        classes.put(Label.NAME, DesktopLabel.class);
        classes.put(CheckBox.NAME, DesktopCheckBox.class);
        classes.put(TextField.NAME, DesktopTextField.class);
        classes.put(Table.NAME, DesktopTable.class);
        classes.put(GroupTable.NAME, DesktopGroupTable.class);
        classes.put(ButtonsPanel.NAME, DesktopButtonsPanel.class);
        classes.put(Filter.NAME, DesktopFilter.class);
        classes.put(FieldGroup.NAME, DesktopFieldGroup.class);
        classes.put(RowsCount.NAME, DesktopRowsCount.class);
        classes.put(PopupButton.NAME, DesktopPopupButton.class);
        classes.put(LookupField.NAME, DesktopLookupField.class);
    }

    public static void registerComponent(String element, Class<? extends Component> componentClass) {
        classes.put(element, componentClass);
    }

    public <T extends Component> T createComponent(String name) {
        final Class<Component> componentClass = (Class<Component>) classes.get(name);
        if (componentClass == null) {
            throw new IllegalStateException(String.format("Can't find component class for '%s'", name));
        }
        try {
            return (T) componentClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Timer> T createTimer() {
        return null;
    }

    public <T extends Chart> T createChart(String vendor, String name) {
        return null;
    }
}
