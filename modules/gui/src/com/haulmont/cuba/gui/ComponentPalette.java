/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;

import java.util.Map;

/**
 * Palette with UI components for screen descriptors
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ComponentPalette {

    /**
     * Get loaders for XML screen descriptors
     * @return Loaders
     */
    Map<String, Class<? extends ComponentLoader>> getLoaders();

    /**
     * Get components for register in ComponentsFactory
     * @return Components
     */
    Map<String, Class<? extends Component>> getComponents();
}