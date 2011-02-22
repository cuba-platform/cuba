/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 16:05:08
 * $Id$
 */
package com.haulmont.cuba.web.xml.layout;

import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.web.gui.WebFileMultiUploadField;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.WebTimer;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.gui.components.charts.jfree.WebJFreeBarChart;
import com.haulmont.cuba.web.gui.components.charts.jfree.WebJFreeLineChart;
import com.haulmont.cuba.web.gui.components.charts.jfree.WebJFreePieChart;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class WebComponentsFactory implements ComponentsFactory, Serializable {

    private static Map<String, Class<? extends Component>> classes = new HashMap<String, Class<?extends Component>>();

    private static final long serialVersionUID = -409350376523747015L;

    static {
        classes.put("window", WebWindow.class);
        classes.put("window.editor", WebWindow.Editor.class);
        classes.put("window.lookup", WebWindow.Lookup.class);

        classes.put("hbox", WebHBoxLayout.class);
        classes.put("vbox", WebVBoxLayout.class);
        classes.put("grid", WebGridLayout.class);
        classes.put("runtimePropertyGrid", WebRuntimePropertyGridLayout.class);
        classes.put("scrollbox", WebScrollBoxLayout.class);
        classes.put("togglebox", WebToggleBoxLayout.class);
        classes.put("htmlbox", WebHtmlBoxLayout.class);
        classes.put("flowbox", WebFlowBoxLayout.class);

        classes.put("button", WebButton.class);
        classes.put("label", WebLabel.class);
        classes.put("checkBox", WebCheckBox.class);
        classes.put("groupBox", WebGroupBox.class);
        classes.put("textField", WebTextField.class);
        classes.put("textArea", WebTextArea.class);
        classes.put("iframe", WebFrame.class);
        classes.put("table", WebTable.class);
        classes.put("treeTable", WebTreeTable.class);
        classes.put("groupTable", WebGroupTable.class);
        classes.put("dateField", WebDateField.class);
        classes.put("timeField", WebTimeField.class);
        classes.put("lookupField", WebLookupField.class);
        classes.put("pickerField", WebPickerField.class);
        classes.put("optionsGroup", WebOptionsGroup.class);
        classes.put("upload", WebFileUploadField.class);
        classes.put("multiupload", WebFileMultiUploadField.class);
        classes.put("split", WebSplitPanel.class);
        classes.put("tree", WebTree.class);
        classes.put("tabsheet", WebTabsheet.class);
        classes.put("embedded", WebEmbedded.class);
        classes.put("filter", WebFilter.class);
        classes.put("accessControl", WebAccessControl.class);
        classes.put("buttonsPanel", WebButtonsPanel.class);
        classes.put("actionsField", WebActionsField.class);
        classes.put("popupButton", WebPopupButton.class);

        classes.put("fieldGroup", WebFieldGroup.class);
        classes.put("tokenList", WebTokenList.class);
        classes.put("widgetsTree", WebWidgetsTree.class);
        classes.put("twinColumn", WebTwinColumn.class);
        classes.put("rowsCount", WebRowsCount.class);

        //JFree charts
        classes.put("jfree@pieChart", WebJFreePieChart.class);
        classes.put("jfree@barChart", WebJFreeBarChart.class);
        classes.put("jfree@lineChart", WebJFreeLineChart.class);
    }

    public static void registerComponent(String element, Class<? extends Component> componentClass) {
        classes.put(element, componentClass);
    }

    public <T extends Component> T createComponent(String name) throws InstantiationException, IllegalAccessException {
        final Class<Component> componentClass = (Class<Component>) classes.get(name);
        if (componentClass == null) {
            throw new IllegalStateException(String.format("Can't find component class for '%s'", name));
        }
        return (T) componentClass.newInstance();
    }

    public <T extends Timer> T createTimer() throws InstantiationException {
        return (T) new WebTimer();
    }

    public <T extends Chart> T createChart(String vendor, String name) throws InstantiationException, IllegalAccessException {
        final Class<Chart> chartClass = (Class<Chart>) classes.get(vendor + "@" + name);
        if (chartClass == null) {
            throw new IllegalStateException(String.format("Can't find chart class for '%s', vendor is %s",
                    name, vendor));
        }
        return (T) chartClass.newInstance();
    }
}
