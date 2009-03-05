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

    static {
        windowLoaders.registerLoader("window", WindowLoader.class);
        registerComponents(windowLoaders);

        editorLoaders.registerLoader("window", WindowLoader.Editor.class);
        registerComponents(editorLoaders);

        lookupLoaders.registerLoader("window", WindowLoader.Lookup.class);
        registerComponents(lookupLoaders);

        frameLoaders.registerLoader("frame", FrameLoader.class);
        registerComponents(frameLoaders);
    }

    private static void registerComponents(LayoutLoaderConfig config) {
        config.registerLoader("hbox", HBoxLoader.class);
        config.registerLoader("vbox", VBoxLoader.class);
        config.registerLoader("grid", GridLayoutLoader.class);
        config.registerLoader("button", ButtonLoader.class);
        config.registerLoader("groupBox", GroupBoxLoader.class);
        config.registerLoader("checkBox", AbstractFieldLoader.class);
        config.registerLoader("label", LabelLoader.class);
        config.registerLoader("textField", TextFieldLoader.class);
        config.registerLoader("textArea", TextAreaLoader.class);
        config.registerLoader("dateField", AbstractFieldLoader.class);
        config.registerLoader("lookupField", LookupFieldLoader.class);
        config.registerLoader("pickerField", PickerFieldLoader.class);
        config.registerLoader("table", TableLoader.class);
        config.registerLoader("iframe", IFrameLoader.class);
        config.registerLoader("split", SplitPanelLoader.class);
        config.registerLoader("tree", TreeLoader.class);
        config.registerLoader("tabsheet", TabsheetLoader.class);
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
        return loaders.get(name);
    }

    public void registerLoader(String name, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loader)
    {
        loaders.put(name, loader);
    }
}
