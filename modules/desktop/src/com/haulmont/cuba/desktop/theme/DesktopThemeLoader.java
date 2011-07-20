/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.theme;

import com.haulmont.cuba.desktop.theme.impl.DesktopThemeLoaderImpl;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public abstract class DesktopThemeLoader {

    private static DesktopThemeLoader instance;

    public static synchronized DesktopThemeLoader getInstance() {
        if (instance == null) {
            instance = new DesktopThemeLoaderImpl();
        }
        return instance;
    }

    public abstract DesktopTheme loadTheme(String theme);
}
