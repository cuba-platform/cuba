/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.config.*;
import com.haulmont.cuba.core.config.defaults.*;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.StringListTypeFactory;

import java.util.List;

/**
 * Configuration parameters interface used by the WEB layer.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Source(type = SourceType.APP)
@Prefix("cuba.web.")
public interface WebConfig extends Config
{
    /**
     * @return Whether to use local invocations instead of HTTPInvoker. Makes sense for improving performance,
     * if the WEB and CORE applications started on the same JVM (same Tomcat instance).
     */
    @Property("cuba.useLocalServiceInvocation")
    @DefaultBoolean(true)
    boolean getUseLocalServiceInvocation();

    /**
     * @return Password used by LoginService.loginTrusted() method.
     * Trusted client may login without providing a user password. This is used by ActiveDirectory integration.
     *
     * <p>Must be equal to password set for the same property on the CORE.</p>
     */
    @Property("cuba.trustedClientPassword")
    @DefaultString("")
    String getTrustedClientPassword();

    /**
     * @return Default user login to set in the login dialog.
     */
    String getLoginDialogDefaultUser();

    /**
     * @return Default user password to set in the login dialog.
     */
    String getLoginDialogDefaultPassword();

    /**
     * @return HTTP session expiration timeout in seconds.<br/>
     * Should be equals or less than middleware user session timeout <code>cuba.userSessionExpirationTimeoutSec</code>
     */
    @Property("cuba.httpSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getHttpSessionExpirationTimeoutSec();

    /**
     * @return Whether to use the ActiveDirectory authentication
     */
    @DefaultBoolean(false)
    boolean getUseActiveDirectory();

    /**
     * @return ActiveDirectory domains configuration info
     */
    String getActiveDirectoryDomains();

    /**
     * @return ActiveDirectory authentification filter
     */
    @DefaultString("com.haulmont.cuba.web.sys.filters.KerberosHttpFilter")
    String getActiveDirectoryFilterClass();

    /**
     * @return Kerberos domain and realms config (krb5.ini)
     */
    String getKerberosConf();

    /**
     * @return Kerberos login module config (jaas.conf)
     */
    String getKerberosJaasConf();

    /**
     * @return Domain controller name
     */
    String getKerberosKeyCenter();

    /**
     * @return Domain for kerberos key center
     */
    String getKerberosRealm();

    /**
     * @return Kerberos login module in JaasConf
     */
    String getKerberosLoginModule();
    
    /**
     * @return Service principal password
     */
    String getServicePrincipalPass();

    /**
     *  @return Comma-separated list of URLs for CubaHttpFilter to bypass.
     */
    @Default("/ws/")
    String getCubaHttpFilterBypassUrls();

    /**
     * @return Default main window mode.
     * Takes place until the user did not change its own preference through user settings.
     */
    @Default("TABBED")
    String getAppWindowMode();

    /**
     * @return Maximum number of open tabs. 0 for unlimited.
     */
    @DefaultInt(0)
    int getMaxTabCount();

    /**
     * @return Whether to use native HTML buttons
     */
    @DefaultBoolean(false)
    boolean getUseNativeButtons();

    /**
     * @return If true and if IE browser is used, we suggest to install Chrome frame IE plugin.
     */
    @DefaultBoolean(false)
    boolean getUseChromeFramePlugin();

    /**
     * @return Whether to close DateField calendar popup right after date is selected.
     * Default behaviour is the following: if the DateField contains time, the calendar popup allows to select time and
     * closes only when user clicks somewhere outside the popup.
     */
    @DefaultBoolean(false)
    boolean getCloseCalendarWhenDateSelected();

    /**
     * @return Request execution time in seconds, after which a message log will be logged.
     */
    @DefaultInt(5)
    int getLogLongRequestsThresholdSec();

    /**
     * @return AppFolders refresh period in seconds.
     */
    @DefaultInt(180)
    int getAppFoldersRefreshPeriodSec();

    /**
     * @return Whether to use icons for folders.
     */
    @DefaultBoolean(false)
    boolean getShowFolderIcons();

    /**
     * @return Maximum number of symbols in main tabs captions.
     */
    @DefaultInt(25)
    int getMainTabCaptionLength();

    /**
     * @return Whether to use disabling and dimming of browser window on long requests.
     */
    @Property("cuba.web.useUiBlocking")
    @DefaultBoolean(true)
    boolean getUseUiBlocking();

    /**
     * @return Theme
     */
    @Default("peyto")
    @Property("cuba.AppConfig.themeName")
    String getAppWindowTheme();

    /**
     * @return Path to an image for use as wallpaper. Not used in the platform.
     */
    @Default("default")
    String getAppWindowWallpaper();

    /**
     * @return List of file extensions which should be shown in the browser instead of downloading as attachments.
     */
    @Factory(factory = StringListTypeFactory.class)
    @Default("htm|html|jpg|png|jpeg|pdf")
    List<String> getViewFileExtensions();

    /**
     * @return Whether to show {@link com.haulmont.cuba.web.app.folders.FoldersPane} on first login.
     * This parameter can be overridden by user settings.
     */
    @DefaultBoolean(false)
    boolean getFoldersPaneVisibleByDefault();

    /**
     * @return Default {@link com.haulmont.cuba.web.app.folders.FoldersPane} width.
     * This parameter can be overridden by user settings.
     */
    @DefaultInt(200)
    int getFoldersPaneDefaultWidth();

    String getResourcesRoot();

    /**
     * @return Whether to generate unique suffixes for Ids in testing mode
     */
    @Property("cuba.web.allowIdSuffix")
    @DefaultBoolean(false)
    boolean getAllowIdSuffix();

    /**
     * @return Whether to enable cancel sorting of table columns. If true, each third click on the column will cancel
     * sorting instead of reversing it.
     */
    @Property("cuba.web.enableCancelTableSorting")
    @DefaultBoolean(false)
    boolean getEnableCancelTableSorting();

    /**
     * Supports automatic testing.
     * @return a name of request parameter that marks a request from an automatic testing tool, for example jMeter.
     */
    @Property("cuba.web.testModeParamName")
    @Default("jmeter")
    String getTestModeParamName();

    /**
     * List of entity ids which can be restored by means of the restore screen.
     * <p>Obsolete. Recommended way to specify this information is entity annotations
     * in <code>*-metadata.xml</code></p>
     * @return comma-separated list of entity ids
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
     * @return System ID. Use for identification (support emails).
     */
    @Property("cuba.systemId")
    @DefaultString("CUBA")
    String getSystemID();

    /**
     * @return Path to an image for use as application logo in the main window.
     */
    @Property("cuba.appLogoImagePath")
    String getAppLogoImagePath();


    /**
     * @return Path to an image for use as application logo in the login window.
     */
    @Property("cuba.loginLogoImagePath")
    String getLoginLogoImagePath();
}
