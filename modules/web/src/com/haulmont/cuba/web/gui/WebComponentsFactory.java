/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.charts.BarChart;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.charts.LineChart;
import com.haulmont.cuba.gui.components.charts.PieChart;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.WebTimer;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.gui.components.charts.jfree.WebJFreeBarChart;
import com.haulmont.cuba.web.gui.components.charts.jfree.WebJFreeLineChart;
import com.haulmont.cuba.web.gui.components.charts.jfree.WebJFreePieChart;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class WebComponentsFactory implements ComponentsFactory, Serializable {

    private static Map<String, Class<? extends Component>> classes = new HashMap<String, Class<?extends Component>>();

    private static final long serialVersionUID = -409350376523747015L;

    static {
        classes.put(Window.NAME, WebWindow.class);
        classes.put(Window.Editor.NAME, WebWindow.Editor.class);
        classes.put(Window.Lookup.NAME, WebWindow.Lookup.class);

        classes.put("hbox", WebHBoxLayout.class);
        classes.put("vbox", WebVBoxLayout.class);
        classes.put(GridLayout.NAME, WebGridLayout.class);
        classes.put(RuntimePropertyGridLayout.NAME, WebRuntimePropertyGridLayout.class);
        classes.put(ScrollBoxLayout.NAME, WebScrollBoxLayout.class);
        classes.put(ToggleBoxLayout.NAME, WebToggleBoxLayout.class);
        classes.put(HtmlBoxLayout.NAME, WebHtmlBoxLayout.class);
        classes.put("flowbox", WebFlowBoxLayout.class);

        classes.put(Button.NAME, WebButton.class);
        classes.put(Label.NAME, WebLabel.class);
        classes.put(CheckBox.NAME, WebCheckBox.class);
        classes.put(GroupBox.NAME, WebGroupBox.class);
        classes.put(TextField.NAME, WebTextField.class);
        classes.put(AutoCompleteTextField.NAME, WebAutoCompleteTextField.class);
        classes.put(TextArea.NAME, WebTextArea.class);
        classes.put(IFrame.NAME, WebFrame.class);
        classes.put(Table.NAME, WebTable.class);
        classes.put(TreeTable.NAME, WebTreeTable.class);
        classes.put(GroupTable.NAME, WebGroupTable.class);
        classes.put(DateField.NAME, WebDateField.class);
        classes.put(TimeField.NAME, WebTimeField.class);
        classes.put(LookupField.NAME, WebLookupField.class);
        classes.put(PickerField.NAME, WebPickerField.class);
        classes.put(OptionsGroup.NAME, WebOptionsGroup.class);
        classes.put(FileUploadField.NAME, WebFileUploadField.class);
        classes.put(FileMultiUploadField.NAME, WebFileMultiUploadField.class);
        classes.put(SplitPanel.NAME, WebSplitPanel.class);
        classes.put(Tree.NAME, WebTree.class);
        classes.put(Tabsheet.NAME, WebTabsheet.class);
        classes.put(Embedded.NAME, WebEmbedded.class);
        classes.put(Filter.NAME, WebFilter.class);
        classes.put(AccessControl.NAME, WebAccessControl.class);
        classes.put(ButtonsPanel.NAME, WebButtonsPanel.class);
        classes.put(ActionsField.NAME, WebActionsField.class);
        classes.put(PopupButton.NAME, WebPopupButton.class);

        classes.put(FieldGroup.NAME, WebFieldGroup.class);
        classes.put(TokenList.NAME, WebTokenList.class);
        classes.put(WidgetsTree.NAME, WebWidgetsTree.class);
        classes.put(TwinColumn.NAME, WebTwinColumn.class);
        classes.put(RowsCount.NAME, WebRowsCount.class);

        //JFree charts
        classes.put(PieChart.NAME, WebJFreePieChart.class);
        classes.put(BarChart.NAME, WebJFreeBarChart.class);
        classes.put(LineChart.NAME, WebJFreeLineChart.class);
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
        return (T) new WebTimer();
    }

    public <T extends Chart> T createChart(String vendor, String name) {
        final Class<Chart> chartClass = (Class<Chart>) classes.get(vendor + "@" + name);
        if (chartClass == null) {
            throw new IllegalStateException(String.format("Can't find chart class for '%s', vendor is %s",
                    name, vendor));
        }
        try {
            return (T) chartClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
