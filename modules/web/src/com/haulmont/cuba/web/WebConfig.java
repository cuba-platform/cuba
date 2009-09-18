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

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * Common web layer configuration parameters. Can be set up in <code>conf/system.properties</code> file.
 */
@Source(type = SourceType.SYSTEM)
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

    /**
     * If true, {@link com.haulmont.cuba.web.WindowManager} will try to create generic screens
     * instead of undefined screens. See {@link WindowConfig#getWindowInfo(String)} 
     */
    @DefaultBoolean(false)
    boolean getEnableGenericScreens();
}
