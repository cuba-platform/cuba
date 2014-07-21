/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app;

import com.haulmont.cuba.gui.theme.Theme;
import com.haulmont.cuba.gui.theme.ThemeManager;
import com.haulmont.cuba.web.App;

import javax.annotation.ManagedBean;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ThemeManager.NAME)
public class WebThemeManager implements ThemeManager {
    @Override
    public Theme getTheme() {
        return App.getInstance().getUiTheme();
    }
}