/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.app;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;

import org.springframework.stereotype.Component;

/**
 * @author artamonov
 * @version $Id$
 */
@Component(ThemeConstantsManager.NAME)
public class DesktopThemeConstantsManager implements ThemeConstantsManager {
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