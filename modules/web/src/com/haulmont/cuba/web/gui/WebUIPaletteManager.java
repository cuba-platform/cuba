/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class WebUIPaletteManager {

    public static void registerPalettes(ComponentPalette ... palettes) {
        LayoutLoaderConfig.registerLoaders(palettes);
        WebComponentsFactory.registerComponents(palettes);
    }
}