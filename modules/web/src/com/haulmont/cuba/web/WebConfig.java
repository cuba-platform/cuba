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
import com.haulmont.cuba.core.config.defaults.*;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.StringListTypeFactory;

import java.util.List;

/**
 * Common web layer configuration parameters. Can be set up in <code>conf/system.properties</code> file.
 */
@Source(type = SourceType.APP)
@Prefix("cuba.web.")
public interface WebConfig extends Config
{
    @Property("cuba.useLocalServiceInvocation")
    @DefaultBoolean(true)
    boolean getUseLocalServiceInvocation();

    /**
     * Password to use LoginService.loginTrusted() method
     */
    @Property("cuba.trustedClientPassword")
    @DefaultString("")
    String getTrustedClientPassword();

    /** Default user login to place into login dialog */
    String getLoginDialogDefaultUser();

    /** Default user password to place into login dialog */
    String getLoginDialogDefaultPassword();

    /**
     * HTTP session expiration timeout in seconds.<br/>
     * Should be equals or less than middleware user session timeout (cuba.userSessionExpirationTimeoutSec)
     */
    @Property("cuba.httpSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getHttpSessionExpirationTimeoutSec();

    /** Use ActiveDirectory authentication */
    @DefaultBoolean(false)
    boolean getUseActiveDirectory();

    /** ActiveDirectory domains configuration info */
    String getActiveDirectoryDomains();

    /** Make CubaHttpFilter to bypass these URLs (comma-separated list)*/
    @Default("/ws/")
    String getCubaHttpFilterBypassUrls();

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

    @DefaultBoolean(false)
    boolean getCloseCalendarWhenDateSelected();

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

    @Factory(factory = StringListTypeFactory.class)
    @Default("htm|html|jpg|png|jpeg|pdf")
    List<String> getViewFileExtensions();

    @DefaultBoolean(false)
    boolean getFoldersPaneVisibleByDefault();

    @DefaultInt(200)
    int getFoldersPaneDefaultWidth();

    String getResourcesRoot();

    /**
     * Allows generating of unique suffixes for Ids in testing mode
     */
    @Property("cuba.web.allowIdSuffix")
    @DefaultBoolean(false)
    boolean getAllowIdSuffix();

    /**
     * Allows cancel sorting in table cell
     */
    @Property("cuba.web.enableCancelTableSorting")
    @DefaultBoolean(false)
    boolean getEnableCancelTableSorting();

    /**
     * Used to support automatic testing. Contains a name of request parameter
     * that marks a request from an automatic testing tool, for example jMeter.
     */
    @Property("cuba.web.testModeParamName")
    @Default("jmeter")
    String getTestModeParamName();

    /**
     * List of entitys' id which can restore into the restore screen
     */
    @Property("cuba.restoreScreenEntityIds")
    String getRestoreEntityId();

    /**
     * Support e-mail. All feedback mails will be sent on this address.
     */
    @Property("cuba.supportEmail")
    @DefaultString("cubasupport@haulmont.com")
    String getSupportEmail();

    /**
     * Timeout for check changes from browser <br/>
     * Used by BackgroundWorker for timers
     *
     * @return Timeout in ms
     */
    @Property("cuba.backgroundWorker.uiCheckInterval")
    @DefaultInteger(1000)
    Integer getUiCheckInterval();

    /**
     * System ID. Use for identification. (Support emails)
     */
    @Property("cuba.systemId")
    @DefaultString("CUBA")
    String getSystemID();

    @Property("cuba.appLogoImagePath")
    String getAppLogoImagePath();

    @Property("cuba.loginLogoImagePath")
    String getLoginLogoImagePath();
}
