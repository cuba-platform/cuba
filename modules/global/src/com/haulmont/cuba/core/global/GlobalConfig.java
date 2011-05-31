/*
 * Author: Konstantin Krivopustov
 * Created: 22.11.2009 18:05:54
 * 
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultString;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.sys.AvailableLocalesFactory;

import java.util.Locale;
import java.util.Map;

@Source(type = SourceType.APP)
public interface GlobalConfig extends Config {

    @Property("cuba.webHostName")
    @DefaultString("localhost")
    String getWebHostName();

    @Property("cuba.webPort")
    @DefaultString("8080")
    String getWebPort();

    @Property("cuba.webContextName")
    @DefaultString("cuba")
    String getWebContextName();

    /**
     * List of entitys' id which can restore into the restore screen
     */
    @Property("cuba.restoreScreenEntityIds")
    String getRestoreEntityId();

    /**
     * Config directory. Root of all not deployable application configuration and logic.
     * Does not end with "/"
     */
    @Property("cuba.confDir")
    String getConfDir();

    /**
     * Logs directory. Place app-specific log files here.
     * Does not end with "/"
     */
    @Property("cuba.logDir")
    String getLogDir();

    /**
     * Temporary files directory. Place app-specific temp files under this directory.
     * Does not end with "/"
     */
    @Property("cuba.tempDir")
    String getTempDir();

    /**
     * Data directory. Place persistent app-specific data files under this directory.
     * Does not end with "/"
     */
    @Property("cuba.dataDir")
    String getDataDir();

    /**
     * Support e-mail. All feedback mails will be sent on this address.
     */
    @Property("cuba.supportEmail")
    @DefaultString("cubasupport@haulmont.com")
    String getSupportEmail();

    /**
     * System ID. Use for identification. (Support emails)
     */
    @Property("cuba.systemId")
    @DefaultString("CUBA")
    String getSystemID();

    /**
     * Used to support automatic testing
     */
    @Property("cuba.testMode")
    @DefaultBoolean(false)
    boolean getTestMode();

    /**
     * Enable class and resource loading by Groovy
     */
    @Property("cuba.groovyClassLoaderEnabled")
    @DefaultBoolean(true)
    boolean isGroovyClassLoaderEnabled();

    /**
     * User session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.
     */
    @Property("cuba.userSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getUserSessionExpirationTimeoutSec();

    /**
     * Password to use LoginService.loginTrusted() method
     */
    @Property("cuba.trustedClientPassword")
    @DefaultString("")
    String getTrustedClientPassword();

    @Property("cuba.collectionDatasourceDbSortEnabled")
    @DefaultBoolean(true)
    boolean getCollectionDatasourceDbSortEnabled();

    /** Used to show alternative locales on user login */
    @Factory(factory = AvailableLocalesFactory.class)
    @Default("English|en;Russian|ru")
    Map<String, Locale> getAvailableLocales();

    @Property("cuba.screenIdsToSaveHistory")
    @Default("sec$User.edit,sec$Group.edit,sec$Role.edit")
    String getScreenIdsToSaveHistory();

    @Property("cuba.useAstBasedJpqlTransformer")
    @DefaultBoolean(false)
    boolean getUseAstBasedJpqlTransformer();
}

