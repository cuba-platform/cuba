/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.*;
import com.haulmont.cuba.core.config.type.CommaSeparatedStringListTypeFactory;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.StringListTypeFactory;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.toolkit.ui.MainTabSheetMode;
import com.haulmont.cuba.web.toolkit.ui.MainTabSheetModeFactory;
import com.haulmont.cuba.web.toolkit.ui.ManagedMainTabSheetMode;
import com.haulmont.cuba.web.toolkit.ui.ManagedMainTabSheetModeFactory;

import java.util.List;

/**
 * Web Client configuration parameters interface.
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
    @Default("admin")
    String getLoginDialogDefaultUser();

    /**
     * @return Default user password to set in the login dialog.
     */
    @Property("cuba.web.loginDialogDefaultPassword")
    @Default("admin")
    String getLoginDialogDefaultPassword();

    /**
     * @return true if powered by link on loginWindow is shown
     */
    @DefaultBoolean(true)
    @Property("cuba.web.loginDialogPoweredByLinkVisible")
    boolean getLoginDialogPoweredByLinkVisible();

    /**
     * @return HTTP session expiration timeout in seconds.<br>
     * Should be equals or less than middleware user session timeout {@code cuba.userSessionExpirationTimeoutSec}
     */
    @Property("cuba.httpSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getHttpSessionExpirationTimeoutSec();

    /**
     *  @return Comma-separated list of URLs for CubaHttpFilter to bypass.
     */
    @Property("cuba.web.cubaHttpFilterBypassUrls")
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    @Default("/ws/,/dispatch/,/rest/,/idp/")
    List<String> getCubaHttpFilterBypassUrls();

    /**
     * @return Default main window mode.
     * Takes place until the user did not change its own preference through user settings.
     */
    @Property("cuba.web.appWindowMode")
    @Default("TABBED")
    String getAppWindowMode();

    /**
     * @return Maximum number of opened tabs. 0 for unlimited.
     */
    @Property("cuba.web.maxTabCount")
    @DefaultInt(20)
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

    /**
     * @return true if WindowBreadCrumbs is shown, false - otherwise
     */
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
    @Default("halo")
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
     * @return minimum number of background task threads.
     */
    @Property("cuba.backgroundWorker.minBackgroundThreadsCount")
    @DefaultInteger(4)
    Integer getMinBackgroundThreadsCount();

    /**
     * @return maximum number of active background tasks.
     */
    @Property("cuba.backgroundWorker.maxActiveTasksCount")
    @DefaultInteger(100)
    Integer getMaxActiveBackgroundTasksCount();

    @Property("cuba.backgroundWorker.maxClientLatencySeconds")
    @DefaultInt(60)
    int getClientBackgroundTasksLatencySeconds();

    /**
     * @return an action to force login.
     * <br> An action is represented by the last part of URL.
     */
    @Property("cuba.web.loginAction")
    @DefaultString("login")
    String getLoginAction();

    /**
     * @return list of URL actions to call {@link com.haulmont.cuba.web.sys.LinkHandler}
     * <br> An action is represented by the last part of URL.
     */
    @Property("cuba.web.linkHandlerActions")
    @Factory(factory = StringListTypeFactory.class)
    @Default("open|o")
    List<String> getLinkHandlerActions();

    /**
     * Reinitialize session after login to protect from Session Fixation attacks. <br>
     * This parameter is ignored if {@link WebAuthConfig#getExternalAuthentication()} is set to true.
     */
    @Property("cuba.web.useSessionFixationProtection")
    @DefaultBoolean(true)
    boolean getUseSessionFixationProtection();

    /**
     * @return true if remember me checkbox is enabled on login window
     */
    @Property("cuba.web.rememberMeEnabled")
    @DefaultBoolean(true)
    boolean getRememberMeEnabled();

    /**
     * @return true if PNG icon names are converted to font icons if there is a mapping png -&gt; FontAwesome in -theme.properties
     */
    @Property("cuba.web.useFontIcons")
    @DefaultBoolean(true)
    boolean getUseFontIcons();

    /**
     * Sets the page length for Table implementation - count of rows for first rendering of Table.
     * After first partial rendering Table will request rest of rows from the server.<br>
     *
     * Setting page length 0 disables paging. <br>
     *
     * If Table has fixed height the client side may update the page length automatically the correct value.
     */
    @Property("cuba.web.table.pageLength")
    @DefaultInt(15)
    int getTablePageLength();

    /**
     * This method adjusts a possible caching mechanism of table implementation. <br>
     *
     * Table component may fetch and render some rows outside visible area. With
     * complex tables (for example containing layouts and components), the
     * client side may become unresponsive. Setting the value lower, UI will
     * become more responsive. With higher values scrolling in client will hit
     * server less frequently. <br>
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

    /**
     * @return true if push should use long polling transport instead of websocket+xhr
     */
    @Property("cuba.web.pushLongPolling")
    @DefaultBoolean(false)
    boolean getUsePushLongPolling();

    /**
     * @return true if push is enabled
     */
    @Property("cuba.web.pushEnabled")
    @DefaultBoolean(true)
    boolean getPushEnabled();

    /**
     * @return true if production mode is enabled
     */
    @Property("cuba.web.productionMode")
    @DefaultBoolean(false)
    boolean getProductionMode();

    /**
     * @return GWT widgetset class
     */
    @Property("cuba.web.widgetSet")
    @Default("com.haulmont.cuba.web.toolkit.ui.WidgetSet")
    String getWidgetSet();

    /**
     * @return true if device width is used as view port width. Affects "viewport" meta tag of Vaadin HTML pages.
     */
    @Property("cuba.web.useDeviceWidthForViewport")
    @DefaultBoolean(false)
    boolean getUseDeviceWidthForViewport();

    /**
     * @return custom view port width for HTML page. Affects "viewport" meta tag of Vaadin HTML pages.
     */
    @Property("cuba.web.customDeviceWidthForViewport")
    @DefaultInt(-1)
    int getCustomDeviceWidthForViewport();

    /**
     * @return initial scale of HTML page if cuba.web.customDeviceWidthForViewport is set or cuba.web.useDeviceWidthForViewport is true.
     *         Affects "viewport" meta tag of Vaadin HTML pages.
     */
    @Property("cuba.web.pageInitialScale")
    @DefaultString("0.8")
    String getPageInitialScale();

    /**
     * Sets whether default {@link com.haulmont.cuba.web.toolkit.ui.CubaTabSheet} or
     * {@link com.haulmont.cuba.web.toolkit.ui.CubaManagedTabSheet} will be used in AppWorkArea.
     *
     * @return one of {@link MainTabSheetMode} values
     */
    @Property("cuba.web.mainTabSheetMode")
    @Default("DEFAULT")
    @Factory(factory = MainTabSheetModeFactory.class)
    MainTabSheetMode getMainTabSheetMode();

    /**
     * Sets how the managed main TabSheet switches its tabs: hides or unloads them.
     *
     * @return one of {@link ManagedMainTabSheetMode} values
     */
    @Property("cuba.web.managedMainTabSheetMode")
    @Default("HIDE_TABS")
    @Factory(factory = ManagedMainTabSheetModeFactory.class)
    ManagedMainTabSheetMode getManagedMainTabSheetMode();

    /**
     * @return Template path for Internal Server Error page (HTTP Status 500).
     */
    @Property("cuba.web.serverErrorPageTemplate")
    @Default("/com/haulmont/cuba/web/sys/errors/server-error.html")
    String getServerErrorPageTemplate();

    /**
     * @return Path to jQuery library which is used in a project as WebJar.
     */
    @Property("cuba.web.webjars.jqueryPath")
    @Default("jquery/1.12.4/jquery.min.js")
    String getWebJarJQueryPath();

    /**
     * Defines which screen should be opened after login. This setting will be applied to all users.
     */
    @Property("cuba.web.defaultScreenId")
    @Source(type = SourceType.DATABASE)
    String getDefaultScreenId();
    void setDefaultScreenId(String screenId);

    /**
     * Defines whether a user is able to choose the default screen or not.
     */
    @Property("cuba.web.userCanChooseDefaultScreen")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getUserCanChooseDefaultScreen();
    void setUserCanChooseDefaultScreen(boolean b);

    /**
     * Defines whether default screen can be closed or not when TABBED work area mode is used.
     */
    @Property("cuba.web.defaultScreenCanBeClosed")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getDefaultScreenCanBeClosed();
    void setDefaultScreenCanBeClosed(boolean b);
}