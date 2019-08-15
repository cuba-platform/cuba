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

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultString;
import com.haulmont.cuba.core.config.type.*;
import com.haulmont.cuba.core.sys.AvailableLocalesFactory;
import com.haulmont.cuba.security.entity.RememberMeToken;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Configuration parameters interface used by all layers: CORE, WEB, DESKTOP.
 */
@Source(type = SourceType.APP)
public interface GlobalConfig extends Config {

    /**
     * @return This web application host name. Makes sense for CORE and WEB modules.
     */
    @Property("cuba.webHostName")
    @DefaultString("localhost")
    String getWebHostName();

    /**
     * @return This web application port. Makes sense for CORE and WEB modules.
     */
    @Property("cuba.webPort")
    @DefaultString("8080")
    String getWebPort();

    /**
     * @return This web application context name. Makes sense for CORE and WEB modules.
     */
    @Property("cuba.webContextName")
    @DefaultString("cuba")
    String getWebContextName();

    /**
     * @return Web-client connection URL. Used for making external links to the application screens and for other purposes.
     */
    @Property("cuba.webAppUrl")
    @Source(type = SourceType.DATABASE)
    @DefaultString("http://localhost:8080/app")
    String getWebAppUrl();

    /**
     * @return Base url for dispatcher servlet of web application. Specific for core, web and portal blocks.
     */
    @Property("cuba.dispatcherBaseUrl")
    String getDispatcherBaseUrl();

    /**
     * @return Configuration directory. {@link Scripting} searches for dynamic resources here.
     * Must not end with "/"
     */
    @Property("cuba.confDir")
    String getConfDir();

    /**
     * @return Logs directory. Place app-specific log files here.
     * Must not end with "/"
     */
    @Property("cuba.logDir")
    String getLogDir();

    /**
     * @return Temporary files directory. Place app-specific temp files here.
     * Must not end with "/"
     */
    @Property("cuba.tempDir")
    String getTempDir();

    /**
     * @return Data directory. Place persistent app-specific data files here.
     * Must not end with "/"
     */
    @Property("cuba.dataDir")
    String getDataDir();

    /**
     * Automatic testing mode indication.
     * @return true if in test mode
     */
    @Property("cuba.testMode")
    @DefaultBoolean(false)
    boolean getTestMode();

    /**
     * Performance testing mode indication.
     *
     * @return true if in test mode
     */
    @Property("cuba.performanceTestMode")
    @DefaultBoolean(false)
    boolean getPerformanceTestMode();

    /**
     * Supported locales. List of locales is shown on user login.
     * @return map of labels to locales
     */
    @Property("cuba.availableLocales")
    @Factory(factory = AvailableLocalesFactory.class)
    @Default("English|en;Russian|ru")
    Map<String, Locale> getAvailableLocales();

    /**
     * Show locale select in LoginWindow.
     * @return true if show
     */
    @Property("cuba.localeSelectVisible")
    @DefaultBoolean(true)
    boolean getLocaleSelectVisible();

    /**
     * @return Overridden AppFolderEditWindow class name
     */
    @Property("cuba.web.appFolderEditWindow")
    String getAppFolderEditWindowClassName();

    /**
     * @return Overridden FolderEditWindow class name
     */
    @Property("cuba.web.folderEditWindow")
    String getFolderEditWindowClassName();

    /**
     * @return Allows to apply a filter to previously selected rows
     */
    @Property("cuba.allowQueryFromSelected")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getAllowQueryFromSelected();

    /**
     * @return Classpath directories for dynamic class loader. Separated by ;
     */
    @Property("cuba.classpath.directories")
    String getCubaClasspathDirectories();

    /**
     * @return the maximum number of idle instances of compiled groovy expressions in {@code Scripting.evaluateGroovy()}
     * @see GenericKeyedObjectPoolConfig#setMaxIdlePerKey(int)
     */
    @Property("cuba.groovyEvaluationPoolMaxIdle")
    @DefaultInt(8)
    int getGroovyEvaluationPoolMaxIdle();

    @Property("cuba.numberIdCacheSize")
    @DefaultInt(100)
    int getNumberIdCacheSize();

    @Property("cuba.anonymousSessionId")
    @Factory(factory = UuidTypeFactory.class)
    UUID getAnonymousSessionId();

    /**
     * @return response of the HTTP GET request on the health check URL
     */
    @Property("cuba.healthCheckResponse")
    @Default("ok")
    String getHealthCheckResponse();

    /**
     * @return whether the user session log is enabled
     */
    @Property("cuba.userSessionLogEnabled")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getUserSessionLogEnabled();
    void setUserSessionLogEnabled(boolean enabled);

    /**
     * @return whether the new (since 6.7) behavior regarding session parameters in query filter is enabled
     */
    @Property("cuba.enableSessionParamsInQueryFilter")
    @DefaultBoolean(true)
    boolean getEnableSessionParamsInQueryFilter();

    /**
     * @return whether to log incorrect web app properties in RemotingServlet
     */
    @Property("cuba.logIncorrectWebAppPropertiesEnabled")
    @DefaultBoolean(true)
    boolean getLogIncorrectWebAppPropertiesEnabled();

    /**
     * @return whether to generate identifiers for entities located in additional data stores
     */
    @Property("cuba.enableIdGenerationForEntitiesInAdditionalDataStores")
    @DefaultBoolean(true)
    boolean getEnableIdGenerationForEntitiesInAdditionalDataStores();

    /**
     * @return true if REST doesn't check security token for entities with security constraints
     */
    @Property("cuba.rest.requiresSecurityToken")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getRestRequiresSecurityToken();

    /**
     * Whether {@code MetadataTools.deepCopy()} should copy non-persistent reference attributes.
     */
    @Property("cuba.deepCopyNonPersistentReferences")
    @DefaultBoolean(true)
    boolean getDeepCopyNonPersistentReferences();

    /**
     * Disable ESCAPE in queries with LIKE for specified data stores.
     */
    @Property("cuba.disableEscapingLikeForDataStores")
    @Source(type = SourceType.DATABASE)
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    @Stringify(stringify = CommaSeparatedStringListStringify.class)
    List<String> getDisableEscapingLikeForDataStores();

    /**
     * @return max recalculation level for dynamic attributes
     */
    @Property("cuba.dynamicAttributes.maxRecalculationLevel")
    @DefaultInt(10)
    int getMaxRecalculationLevel();

    /**
     * @return max columns count for DynamicAttributesPanel
     */
    @Property("cuba.dynamicAttributes.dynamicAttributesPanelMaxColumns")
    @DefaultInt(4)
    int getDynamicAttributesPanelMaxColumnsCount();

    /**
     * Defines expiration timeout for remember me cookie and {@link RememberMeToken}
     * in seconds.
     * <p>
     * Default expiration timeout is one month.
     *
     * @return remember me expiration timeout in seconds
     */
    @Property("cuba.rememberMeExpirationTimeoutSec")
    @DefaultInt(30 * 24 * 60 * 60)
    int getRememberMeExpirationTimeoutSec();
}