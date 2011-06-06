/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:29:12
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.xml.layout.loaders.*;
import com.haulmont.cuba.gui.xml.layout.loaders.charts.BarChartLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.charts.LineChartLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.charts.PieChartLoader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LayoutLoaderConfig implements Serializable {
    private Map<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>> loaders =
            new HashMap<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>>();

    private static LayoutLoaderConfig windowLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig editorLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig lookupLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig frameLoaders = new LayoutLoaderConfig();

    private static Map<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>> customLoaders =
            new HashMap<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>>();

    private static final long serialVersionUID = 7649042802592427312L;

    static {
        windowLoaders.register("window", WindowLoader.class);
        registerComponents(windowLoaders);

        editorLoaders.register("window", WindowLoader.Editor.class);
        registerComponents(editorLoaders);

        lookupLoaders.register("window", WindowLoader.Lookup.class);
        registerComponents(lookupLoaders);

        frameLoaders.register("window", FrameLoader.class);
        registerComponents(frameLoaders);
    }

    private static void registerComponents(LayoutLoaderConfig config) {
        config.register("hbox", HBoxLoader.class);
        config.register("vbox", VBoxLoader.class);
        config.register("grid", GridLayoutLoader.class);
        config.register("runtimePropertyGrid", RuntimePropertyGridLayoutLoader.class);
        config.register("scrollbox", ScrollBoxLayoutLoader.class);
        config.register("togglebox", ToggleBoxLoader.class);
        config.register("htmlbox", HtmlBoxLoader.class);
        config.register("flowbox", FlowBoxLoader.class);

        config.register("button", ButtonLoader.class);
        config.register("groupBox", GroupBoxLoader.class);
        config.register("checkBox", AbstractFieldLoader.class);
        config.register("label", LabelLoader.class);
        config.register("textField", TextFieldLoader.class);
        config.register("autoCompleteTextField", AutoCompleteTextFieldLoader.class);
        config.register("textArea", TextAreaLoader.class);
        config.register("dateField", DateFieldLoader.class);
        config.register("timeField", TimeFieldLoader.class);
        config.register("lookupField", LookupFieldLoader.class);
        config.register("pickerField", PickerFieldLoader.class);
        config.register("lookupPickerField", LookupPickerFieldLoader.class);
        config.register("optionsGroup", OptionsGroupLoader.class);
        config.register("upload", FileUploadFieldLoader.class);
        config.register("multiupload",FileUploadFieldLoader.class);
        config.register("table", TableLoader.class);
        config.register("treeTable", TreeTableLoader.class);
        config.register("groupTable", GroupTableLoader.class);
        config.register("iframe", IFrameLoader.class);
        config.register("runtimeProperties",RuntimePropertiesFrameLoader.class);
        config.register("split", SplitPanelLoader.class);
        config.register("tree", TreeLoader.class);
        config.register("tabsheet", TabsheetLoader.class);
        config.register("embedded", EmbeddedLoader.class);
        config.register("filter", FilterLoader.class);
        config.register("accessControl", AccessControlLoader.class);
        config.register("buttonsPanel", ButtonsPanelLoader.class);
        config.register("actionsField", ActionsFieldLoader.class);
        config.register("popupButton", PopupButtonLoader.class);
        config.register("fieldGroup", FieldGroupLoader.class);
        config.register("runtimeFieldGroup", FieldGroupRuntimeLoader.class);
        config.register("tokenList", TokenListLoader.class);
        config.register("widgetsTree", WidgetsTreeLoader.class);
        config.register("twinColumn", TwinColumnLoader.class);

        //charts
        config.register("pieChart", PieChartLoader.class);
        config.register("barChart", BarChartLoader.class);
        config.register("lineChart", LineChartLoader.class);

        config.register("scriptHost", ScriptHostLoader.class);
    }

    public static void registerLoader(String tagName, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> aClass) {
        customLoaders.put(tagName, aClass);
    }

    public static LayoutLoaderConfig getWindowLoaders() {
        return windowLoaders;
    }

    public static LayoutLoaderConfig getEditorLoaders() {
        return editorLoaders;
    }

    public static LayoutLoaderConfig getFrameLoaders() {
        return frameLoaders;
    }

    public static LayoutLoaderConfig getLookupLoaders() {
        return lookupLoaders;
    }

    public Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> getLoader(String name) {
        final Class<? extends ComponentLoader> loader = customLoaders.get(name);
        if (loader == null) {
            return loaders.get(name);
        }

        return loader;
    }

    protected void register(String name, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loader)
    {
        loaders.put(name, loader);
    }
}
