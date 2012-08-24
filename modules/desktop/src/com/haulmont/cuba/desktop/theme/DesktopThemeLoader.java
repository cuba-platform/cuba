/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.theme;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public interface DesktopThemeLoader {

    public static final String NAME = "cuba_DesktopThemeLoader";

    public DesktopTheme loadTheme(String theme);
}
