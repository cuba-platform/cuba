/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.*;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.StringListTypeFactory;

import java.util.List;

/**
 * Web Client configuration parameters interface.
 *
 * @author krivopustov
 * @version $Id$
 */
@Source(type = SourceType.APP)
public interface WebConfig extends Config {

    /**
     * @return Whether to use local invocations instead of HTTPInvoker. Makes sense for improving performance,
     * if the WEB and CORE applications started on the same JVM (same Tomcat instance).
     */
    @Property("cuba.useLocalServiceInvocation")
    @DefaultBoolean(true)
    boolean getUseLocalServiceInvocation();

    /**
     * @return Default user login to set in the login dialog.
     */
    @Property("cuba.web.loginDialogDefaultUser")
    String getLoginDialogDefaultUser();

    /**
     * @return Default user password to set in the login dialog.
     */
    @Property("cuba.web.loginDialogDefaultPassword")
    String getLoginDialogDefaultPassword();

    /**
     * @return HTTP session expiration timeout in seconds.<br/>
     * Should be equals or less than middleware user session timeout <code>cuba.userSessionExpirationTimeoutSec</code>
     */
    @Property("cuba.httpSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getHttpSessionExpirationTimeoutSec();

    /**
     *  @return Comma-separated list of URLs for CubaHttpFilter to bypass.
     */
    @Property("cuba.web.cubaHttpFilterBypassUrls")
    @Default("/ws/")
    String getCubaHttpFilterBypassUrls();

    /**
     * @return Default main window mode.
     * Takes place until the user did not change its own preference through user settings.
     */
    @Property("cuba.web.appWindowMode")
    @Default("TABBED")
    String getAppWindowMode();

    /**
     * @return Maximum number of open tabs. 0 for unlimited.
     */
    @Property("cuba.web.maxTabCount")
    @DefaultInt(7)
    int getMaxTabCount();

    /**
     * @return Request execution time in seconds, after which a message log will be logged.
     */
    @Property("cuba.web.logLongRequestsThresholdSec")
    @DefaultInt(5)
    int getLogLongRequestsThresholdSec();

    /**
     * @return Whether to enable the Folders Pane functionality.
     */
    @Property("cuba.web.foldersPaneEnabled")
    @DefaultBoolean(false)
    boolean getFoldersPaneEnabled();

    /**
     * @return AppFolders refresh period in seconds.
     */
    @Property("cuba.web.appFoldersRefreshPeriodSec")
    @DefaultInt(180)
    int getAppFoldersRefreshPeriodSec();

    /**
     * @return Whether to use icons for folders.
     */
    @Property("cuba.web.showFolderIcons")
    @DefaultBoolean(false)
    boolean getShowFolderIcons();

    /**
     * @return Whether to show {@link com.haulmont.cuba.web.app.folders.CubaFoldersPane} on first login.
     */
    @Property("cuba.web.foldersPaneVisibleByDefault")
    @DefaultBoolean(false)
    boolean getFoldersPaneVisibleByDefault();

    /**
     * @return Default {@link com.haulmont.cuba.web.app.folders.CubaFoldersPane} width.
     */
    @Property("cuba.web.foldersPaneDefaultWidth")
    @DefaultInt(200)
    int getFoldersPaneDefaultWidth();

    @Property("cuba.web.showBreadCrumbs")
    @DefaultBoolean(true)
    boolean getShowBreadCrumbs();

    /**
     * @return Maximum number of symbols in main tabs captions.
     */
    @Property("cuba.web.mainTabCaptionLength")
    @DefaultInt(25)
    int getMainTabCaptionLength();

    /**
     * @return Whether to handle back button click in browser on server-side.
     */
    @Property("cuba.web.allowHandleBrowserHistoryBack")
    @DefaultBoolean(true)
    boolean getAllowHandleBrowserHistoryBack();

    /**
     * @return Theme
     */
    @Default("havana")
    @Property("cuba.web.theme")
    String getAppWindowTheme();

    /**
     * @return Whether to use inverse header colors if it is supported by theme.
     */
    @DefaultBoolean(true)
    @Property("cuba.web.useInverseHeader")
    boolean getUseInverseHeader();

    /**
     * @return List of file extensions which should be shown in the browser instead of downloading as attachments.
     */
    @Property("cuba.web.viewFileExtensions")
    @Factory(factory = StringListTypeFactory.class)
    @Default("htm|html|jpg|png|jpeg|pdf")
    List<String> getViewFileExtensions();

    @Property("cuba.web.resourcesRoot")
    String getResourcesRoot();

    /**
     * Timeout for check changes from browser <br/>
     * Used by BackgroundWorker for timers
     *
     * @return Timeout in ms
     */
    @Property("cuba.backgroundWorker.uiCheckInterval")
    @DefaultInteger(2000)
    Integer getUiCheckInterval();

    /**
     * @return Maximum number of active background tasks
     */
    @Property("cuba.backgroundWorker.maxActiveTasksCount")
    @DefaultInteger(100)
    Integer getMaxActiveBackgroundTasksCount();

    @Property("cuba.backgroundWorker.maxClientLatencySeconds")
    @DefaultInt(60)
    int getClientBackgroundTasksLatencySeconds();

    /**
     * @return an action to force login.
     * <p/> An action is represented by the last part of URL.
     */
    @Property("cuba.web.loginAction")
    @DefaultString("login")
    String getLoginAction();

    /**
     * @return list of URL actions to call {@link com.haulmont.cuba.web.sys.LinkHandler}
     * <p/> An action is represented by the last part of URL.
     */
    @Property("cuba.web.linkHandlerActions")
    @Factory(factory = StringListTypeFactory.class)
    @Default("open|o")
    List<String> getLinkHandlerActions();

    /**
     * Reinitialize session after login to protect from Session Fixation attacks.
     */
    @Property("cuba.web.useSessionFixationProtection")
    @DefaultBoolean(true)
    boolean getUseSessionFixationProtection();

    @Property("cuba.web.rememberMeEnabled")
    @DefaultBoolean(true)
    boolean getRememberMeEnabled();

    @Property("cuba.web.useFontIcons")
    @DefaultBoolean(true)
    boolean getUseFontIcons();

    /**
     * Sets the page length for Table implementation - count of rows for first rendering of Table.
     * After first partial rendering Table will request rest of rows from the server.<br/>
     *
     * Setting page length 0 disables paging. <br/>
     *
     * If Table has fixed height the client side may update the page length automatically the correct value.
     */
    @Property("cuba.web.table.pageLength")
    @DefaultInt(15)
    int getTablePageLength();

    /**
     * This method adjusts a possible caching mechanism of table implementation. <br/>
     *
     * Table component may fetch and render some rows outside visible area. With
     * complex tables (for example containing layouts and components), the
     * client side may become unresponsive. Setting the value lower, UI will
     * become more responsive. With higher values scrolling in client will hit
     * server less frequently. <br/>
     *
     * The amount of cached rows will be cacheRate multiplied with pageLength
     * {@link #getTablePageLength()} both below and above visible area.
     */
    @Property("cuba.web.table.cacheRate")
    @DefaultDouble(2)
    double getTableCacheRate();

    /**
     * @return Whether to redirect by blank html page on getting URL request action
     */
    @Property("cuba.web.useRedirectWithBlankPageForLinkAction")
    @DefaultBoolean(false)
    boolean getUseRedirectWithBlankPageForLinkAction();

    /**
     * @return Maximum number of items stored in the {@link com.haulmont.cuba.web.log.AppLog} queue
     */
    @Property("cuba.web.appLogMaxItemsCount")
    @DefaultInt(10)
    int getAppLogMaxItemsCount();
}