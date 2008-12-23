/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:29:12
 * $Id$
 */
package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.xml.loaders.*;

import java.util.Map;
import java.util.HashMap;

public class ComponentsLoaderConfig {
    private Map<String, Class<? extends ComponentLoader>> loaders = new HashMap<String, Class<? extends ComponentLoader>>();

    private static ComponentsLoaderConfig windowLoaders = new ComponentsLoaderConfig();
    private static ComponentsLoaderConfig frameLoaders = new ComponentsLoaderConfig();

    static {
        windowLoaders.registerLoader("window", WindowLoader.class);
        windowLoaders.registerLoader("hbox", HBoxLoader.class);
        windowLoaders.registerLoader("vbox", VBoxLoader.class);
        windowLoaders.registerLoader("button", ButtonLoader.class);
        windowLoaders.registerLoader("iframe", FrameLoader.class);
        windowLoaders.registerLoader("groupbox", GroupBoxLoader.class);
        windowLoaders.registerLoader("label", LabelLoader.class);
        windowLoaders.registerLoader("textbox", TextBoxLoader.class);

        frameLoaders.registerLoader("window", IFrameLoader.class);
        frameLoaders.registerLoader("hbox", HBoxLoader.class);
        frameLoaders.registerLoader("vbox", VBoxLoader.class);
        frameLoaders.registerLoader("button", ButtonLoader.class);
        frameLoaders.registerLoader("iframe", FrameLoader.class);
        frameLoaders.registerLoader("groupbox", GroupBoxLoader.class);
        frameLoaders.registerLoader("label", LabelLoader.class);
        frameLoaders.registerLoader("textbox", TextBoxLoader.class);
    }

    public static ComponentsLoaderConfig getWindowLoaders() {
        return windowLoaders;
    }

    public static ComponentsLoaderConfig getFrameLoaders() {
        return frameLoaders;
    }

    public Class<? extends ComponentLoader> getLoader(String name) {
        return loaders.get(name);
    }

    public void registerLoader(String name, Class<? extends ComponentLoader> loader)
    {
        loaders.put(name, loader);
    }
}
