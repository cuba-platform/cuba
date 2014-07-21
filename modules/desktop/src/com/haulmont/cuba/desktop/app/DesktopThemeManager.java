/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.app;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.theme.Theme;
import com.haulmont.cuba.gui.theme.ThemeManager;

import javax.annotation.ManagedBean;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ThemeManager.NAME)
public class DesktopThemeManager implements ThemeManager {
    @Override
    public Theme getTheme() {
        return App.getInstance().getUiTheme();
    }
}