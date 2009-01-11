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

import java.util.Map;
import java.util.HashMap;

public class LayoutLoaderConfig {
    private Map<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>> loaders = new HashMap<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>>();

    private static LayoutLoaderConfig windowLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig frameLoaders = new LayoutLoaderConfig();

    static {
        windowLoaders.registerLoader("layout", WindowLoader.class);
        windowLoaders.registerLoader("hbox", HBoxLoader.class);
        windowLoaders.registerLoader("vbox", VBoxLoader.class);
        windowLoaders.registerLoader("button", ButtonLoader.class);
        windowLoaders.registerLoader("iframe", FrameLoader.class);
        windowLoaders.registerLoader("groupbox", GroupBoxLoader.class);
        windowLoaders.registerLoader("label", LabelLoader.class);
        windowLoaders.registerLoader("textbox", TextBoxLoader.class);
        windowLoaders.registerLoader("table", TableLoader.class);

        frameLoaders.registerLoader("layout", IFrameLoader.class);
        frameLoaders.registerLoader("hbox", HBoxLoader.class);
        frameLoaders.registerLoader("vbox", VBoxLoader.class);
        frameLoaders.registerLoader("button", ButtonLoader.class);
        frameLoaders.registerLoader("iframe", FrameLoader.class);
        frameLoaders.registerLoader("groupbox", GroupBoxLoader.class);
        frameLoaders.registerLoader("label", LabelLoader.class);
        frameLoaders.registerLoader("textbox", TextBoxLoader.class);
        frameLoaders.registerLoader("table", TableLoader.class);
    }

    public static LayoutLoaderConfig getWindowLoaders() {
        return windowLoaders;
    }

    public static LayoutLoaderConfig getFrameLoaders() {
        return frameLoaders;
    }

    public Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> getLoader(String name) {
        return loaders.get(name);
    }

    public void registerLoader(String name, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loader)
    {
        loaders.put(name, loader);
    }
}
