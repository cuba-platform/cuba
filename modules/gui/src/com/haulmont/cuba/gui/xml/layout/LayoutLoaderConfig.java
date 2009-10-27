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

import java.util.HashMap;
import java.util.Map;

public class LayoutLoaderConfig {
    private Map<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>> loaders =
            new HashMap<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>>();

    private static LayoutLoaderConfig windowLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig editorLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig lookupLoaders = new LayoutLoaderConfig();    
    private static LayoutLoaderConfig frameLoaders = new LayoutLoaderConfig();

    private static Map<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>> customLoaders =
            new HashMap<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>>();

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
        config.register("scrollbox", ScrollBoxLayoutLoader.class);
        config.register("togglebox", ToggleBoxLoader.class);
        config.register("htmlbox", HtmlBoxLoader.class);

        config.register("button", ButtonLoader.class);
        config.register("groupBox", GroupBoxLoader.class);
        config.register("checkBox", AbstractFieldLoader.class);
        config.register("label", LabelLoader.class);
        config.register("textField", TextFieldLoader.class);
        config.register("textArea", TextAreaLoader.class);
        config.register("dateField", DateFieldLoader.class);
        config.register("lookupField", LookupFieldLoader.class);
        config.register("pickerField", PickerFieldLoader.class);
        config.register("optionsGroup", OptionsGroupLoader.class);
        config.register("upload", FileUploadFieldLoader.class);
        config.register("table", TableLoader.class);
                config.register("treeTable", TreeTableLoader.class);
        config.register("iframe", IFrameLoader.class);
        config.register("split", SplitPanelLoader.class);
        config.register("tree", TreeLoader.class);
        config.register("tabsheet", TabsheetLoader.class);
        config.register("embedded", EmbeddedLoader.class);
        config.register("filter", FilterLoader.class);
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
