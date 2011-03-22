/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 11:41:24
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.config.*;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.web.sys.AvailableLocalesFactory;

import java.util.Locale;
import java.util.Map;

/**
 * Common web layer configuration parameters. Can be set up in <code>conf/system.properties</code> file.
 */
@Source(type = SourceType.APP)
@Prefix("cuba.web.")
public interface WebConfig extends Config
{
    /** Default user login to place into login dialog */
    String getLoginDialogDefaultUser();

    /** Default user password to place into login dialog */
    String getLoginDialogDefaultPassword();

    /** Use ActiveDirectory authentication */
    @DefaultBoolean(false)
    boolean getUseActiveDirectory();

    /** ActiveDirectory domains configuration info */
    String getActiveDirectoryDomains();

    /**
     * Default main window mode.
     * Takes place until the user did not change its own preference through user settings
    */
    @Default("TABBED")
    String getAppWindowMode();

    /** Max number of open tabs. 0 for unlimited. */
    @DefaultInt(0)
    int getMaxTabCount();

    /**
     * If true, {@link WebWindowManager} will try to create generic screens
     * instead of undefined screens. See {@link WebWindowConfig#getWindowInfo(String)}
     */
    @DefaultBoolean(false)
    boolean getEnableGenericScreens();

    @DefaultBoolean(true)
    boolean getUseNativeButtons();

    @DefaultBoolean(false)
    boolean getUseChromeFramePlugin();

    /** Used to show alternative locales on user login */
    @Factory(factory = AvailableLocalesFactory.class)
    @Default("English|en;Russian|ru")
    Map<String, Locale> getAvailableLocales();

    @DefaultInt(5)
    int getLogLongRequestsThresholdSec();

    @DefaultInt(180)
    int getAppFoldersRefreshPeriodSec();

    @DefaultInt(25)
    int getMainTabCaptionLength();

    @Default("peyto")
    @Property("cuba.AppConfig.themeName")
    String getAppWindowTheme();

    @Default("default")
    String getAppWindowWallpaper();

    @DefaultBoolean(true)
    boolean getGenericFilterManualApplyRequired();

    /** If true, then check filter conditions(empty or not) before apply filter */
    @DefaultBoolean(true)
    boolean getGenericFilterChecking();
}
