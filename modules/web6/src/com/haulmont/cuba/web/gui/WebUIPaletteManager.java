/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;

/**
 * Helper class to register component palettes on the web client.
 *
 * @author artamonov
 * @version $Id$
 */
public class WebUIPaletteManager {

    public static void registerPalettes(ComponentPalette ... palettes) {
        LayoutLoaderConfig.registerLoaders(palettes);
        WebComponentsFactory.registerComponents(palettes);
    }
}