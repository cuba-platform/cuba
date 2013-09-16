/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
