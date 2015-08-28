/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app;

import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.web.App;

import javax.annotation.ManagedBean;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ThemeConstantsManager.NAME)
public class WebThemeConstantsManager implements ThemeConstantsManager {
    @Override
    public ThemeConstants getConstants() {
        return App.getInstance().getThemeConstants();
    }

    @Override
    public String getThemeValue(String key) {
        return getConstants().get(key);
    }

    @Override
    public int getThemeValueInt(String key) {
        return getConstants().getInt(key);
    }
}