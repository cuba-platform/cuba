/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.theme;

import com.haulmont.cuba.desktop.Resources;

import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public interface DesktopTheme {

    String getName();

    // set look and feel, init ui defaults
    void init();

    /*
     * Can be a cuba component or swing or awt component
     */
    void applyStyle(Object component, String styleName);

    /*
     * Used for table style providers
     */
    void applyStyle(Object component, String styleName, Set<String> state);

    /*
     * Returns resources associated with the theme
     */
    Resources getResources();
}
