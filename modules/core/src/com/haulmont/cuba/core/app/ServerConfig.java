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
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.*;
import com.haulmont.cuba.core.config.type.DataSizeTypeFactory;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.TokenizedStringListFactory;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.role.SecurityStorageMode;
import com.haulmont.cuba.security.role.SecurityStorageModeFactory;
import org.springframework.util.unit.DataSize;

import java.util.List;

/**
 * Configuration parameters interface used by the CORE layer.
 */
@Source(type = SourceType.APP)
public interface ServerConfig extends Config {

    String SYNC_NEW_USER_SESSION_REPLICATION_PROP = "cuba.syncNewUserSessionReplication";

    /**
     * @return URL of a user session provider - usually the main middleware unit.
     * This URL is used by middleware units which don't login themselves but get existing sessions from the main app.
     */
    @Property("cuba.userSessionProviderUrl")
    String getUserSessionProviderUrl();

    /**
     * @return Password used by LoginService.loginTrusted() method.
     * Trusted client may login without providing a user password. This is used for external authentication.
     *
     * <p>Must be equal to password set for the same property on the client.</p>
     */
    @Property("cuba.trustedClientPassword")
    @DefaultString("")
    String getTrustedClientPassword();

    @Property("cuba.trustedClientPermittedIpList")
    @DefaultString("127.0.0.1,0:0:0:0:0:0:0:1")
    @Source(type = SourceType.DATABASE)
    String getTrustedClientPermittedIpList();

    @Property("cuba.security.resetPasswordTemplateBody")
    @Default("/com/haulmont/cuba/security/app/email/reset-password-body.gsp")
    String getResetPasswordEmailBodyTemplate();

    @Property("cuba.security.resetPasswordTemplateSubject")
    @Default("/com/haulmont/cuba/security/app/email/reset-password-subject.gsp")
    String getResetPasswordEmailSubjectTemplate();

    /**
     * @return Path to the exception report email body template.
     */
    @Property("cuba.email.exceptionReportEmailTemplateBody")
    @Default("/com/haulmont/cuba/core/app/exceptionemail/exception-report-template-body.gsp")
    String getExceptionReportEmailBodyTemplate();

    /**
     * @return Path to the exception report email subject template.
     */
    @Property("cuba.email.exceptionReportEmailTemplateSubject")
    @Default("/com/haulmont/cuba/core/app/exceptionemail/exception-report-template-subject.gsp")
    String getExceptionReportEmailSubjectTemplate();

    /**
     * @return User session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.
     */
    @Property("cuba.userSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getUserSessionExpirationTimeoutSec();

    void setUserSessionExpirationTimeoutSec(int timeout);

    /**
     * @return User session ping timeout in cluster.
     * If ping is performed by {@link UserSessionsAPI#getAndRefresh}, user session is sent to the cluster only
     * after this timeout
     */
    @Property("cuba.userSessionSendTimeoutSec")
    @DefaultInt(10)
    int getUserSessionSendTimeoutSec();

    void setUserSessionSendTimeoutSec(int timeout);

    /**
     * {@link UserSessionsAPI} will update last used timestamp for a session only if the old value is older than
     * the current + timeout.
     */
    @Property("cuba.userSessionTouchTimeoutSec")
    @DefaultInt(1)
    int getUserSessionTouchTimeoutSec();

    /**
     * @return DB scripts directory.
     * Does not end with "/"
     */
    @Property("cuba.dbDir")
    String getDbDir();

    /**
     * @return Whether the server should try to init/update database on each startup.
     */
    @Property("cuba.automaticDatabaseUpdate")
    @DefaultBoolean(false)
    boolean getAutomaticDatabaseUpdate();

    /**
     * @return {@link FileStorageAPI} storage directory. If not set, <code>cuba.dataDir/filestorage</code> will be used.
     */
    @Property("cuba.fileStorageDir")
    String getFileStorageDir();

    /**
     * An immutable file storage throws exception on attempt to write an existing file.
     *
     * @return whether file storage is immutable.
     */
    @Property("cuba.immutableFileStorage")
    @DefaultBoolean(true)
    boolean getImmutableFileStorage();

    /**
     * Maximum allowable file size in file storage.
     */
    @Property("cuba.fileStorageMaxFileSize")
    @Factory(factory = DataSizeTypeFactory.class)
    @Default("100MB")
    DataSize getFileStorageMaxFileSize();

    /**
     * @return Scheduled tasks execution control.
     */
    @Property("cuba.schedulingActive")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getSchedulingActive();

    void setSchedulingActive(boolean value);

    /**
     * @return Scheduled tasks execution control.
     */
    @Property("cuba.schedulingInterval")
    @Source(type = SourceType.DATABASE)
    @DefaultLong(1000)
    long getSchedulingInterval();

    void setSchedulingInterval(long value);

    /**
     * @return Maximum size of thread pool which is used to process scheduled tasks
     */
    @Property("cuba.schedulingThreadPoolSize")
    @DefaultInt(10)
    int getSchedulingThreadPoolSize();

    void setSchedulingThreadPoolSize(int value);

    /**
     * @return Tells DataService to ensure distinct results by processing them in memory, instead of issue
     * 'select distinct' to the database.
     */
    @Property("cuba.inMemoryDistinct")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getInMemoryDistinct();

    void setInMemoryDistinct(boolean value);

    /**
     * @return Default database query timeout in seconds. If 0, middleware doesn't apply any timeout to queries.
     */
    @Property("cuba.defaultQueryTimeoutSec")
    @Source(type = SourceType.DATABASE)
    @DefaultInt(0)
    int getDefaultQueryTimeoutSec();

    void setDefaultQueryTimeoutSec(int timeout);

    /**
     * Indicates that a new user session created on login should be sent to the cluster synchronously.
     */
    @Property(SYNC_NEW_USER_SESSION_REPLICATION_PROP)
    @DefaultBoolean(false)
    boolean getSyncNewUserSessionReplication();

    /**
     * If set to false, attribute permissions are not enforced on Middleware. This is appropriate if only server-side
     * clients are used.
     */
    @Property("cuba.entityAttributePermissionChecking")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getEntityAttributePermissionChecking();

    /**
     * If set to true and query loading values is affected by security constraints, an exception is thrown
     */
    @Property("cuba.disableLoadValuesIfConstraints")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getDisableLoadValuesIfConstraints();

    /**
     * &lt;= 16 symbols string, used as key for AES encryption of security token
     */
    @Property("cuba.keyForSecurityTokenEncryption")
    @DefaultString("CUBA.Platform")
    String getKeyForSecurityTokenEncryption();

    /**
     * Indicates that {@code DataManager} should always apply security restrictions on the middleware.
     */
    @Property("cuba.dataManagerChecksSecurityOnMiddleware")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getDataManagerChecksSecurityOnMiddleware();

    /**
     * Whether the brute-force protection on user login is enabled.
     */
    @Property("cuba.bruteForceProtection.enabled")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getBruteForceProtectionEnabled();

    /**
     * @return a maximum number of unsuccessful login attempts
     */
    @Property("cuba.bruteForceProtection.maxLoginAttemptsNumber")
    @Source(type = SourceType.DATABASE)
    @DefaultInt(5)
    int getMaxLoginAttemptsNumber();

    /**
     * @return a time interval in seconds for which a user is blocked after a series of
     * unsuccessful login attempts
     */
    @Property("cuba.bruteForceProtection.blockIntervalSec")
    @Source(type = SourceType.DATABASE)
    @DefaultInt(60)
    int getBruteForceBlockIntervalSec();

    /**
     * Login name of the anonymous user.
     */
    @Property("cuba.anonymousLogin")
    @Source(type = SourceType.DATABASE)
    @Default("anonymous")
    String getAnonymousLogin();

    /**
     * Login name which is used by default in system authentication
     */
    @Property("cuba.jmxUserLogin")
    @Default("admin")
    String getJmxUserLogin();

    /**
     * Warning in the log when a service is invoked from inside middleware.
     */
    @Property("cuba.logInternalServiceInvocation")
    @DefaultBoolean(false)
    boolean getLogInternalServiceInvocation();

    /**
     * @return batch size for loading related entities from different data stores
     */
    @Property("cuba.crossDataStoreReferenceLoadingBatchSize")
    @Source(type = SourceType.DATABASE)
    @DefaultInt(50)
    int getCrossDataStoreReferenceLoadingBatchSize();

    /**
     * @return use read-only transactions in {@code DataManager} load operations and do not commit them
     */
    @Property("cuba.useReadOnlyTransactionForLoad")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getUseReadOnlyTransactionForLoad();

    /**
     * @return if true, sequences for BaseLongIdEntity and BaseIntegerIdEntity subclasses are created in data stores
     * of these entities. Otherwise (by default), sequences for all entities are created in the main data store.
     */
    @Property("cuba.useEntityDataStoreForIdSequence")
    @DefaultBoolean(false)
    boolean getUseEntityDataStoreForIdSequence();

    /**
     * @return if true, system allows to execute JPQL DELETE statement with enabled soft deletion.
     * Otherwise(by default) system throws an exception when JPQL DELETE statement with enabled soft deletion is executed
     */
    @Property("cuba.enableDeleteStatementInSoftDeleteMode")
    @DefaultBoolean(false)
    boolean getEnableDeleteStatementInSoftDeleteMode();

    /**
     * Compatibility toggle. If true, JPQL parameters are converted implicitly, e.g. an entity is converted to its ID.
     */
    @Property("cuba.implicitConversionOfJpqlParams")
    @DefaultBoolean(false)
    boolean getImplicitConversionOfJpqlParams();

    /**
     * Entity bean validation toggle for {@code DataManager}.
     * If true, entity bean validation rules are enforced on {@code DataManager} level.
     */
    @Property("cuba.dataManagerBeanValidation")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getDataManagerBeanValidation();

    /**
     * Deactivating switch for {@code EntityNotEnhancedException}. By default on application start entities
     * will be checked for Cuba and EclipseLink enhancing interfaces. If any interfaces are missing,
     * an exception will be thrown.
     * If true, detected problems will be logged instead of throwing {@code EntityNotEnhancedException}.
     * <p>
     * List of checked interfaces:
     * {@link com.haulmont.cuba.core.sys.CubaEnhanced}
     * {@link org.eclipse.persistence.internal.descriptors.PersistenceObject}
     * {@link org.eclipse.persistence.internal.weaving.PersistenceWeaved}
     * {@link org.eclipse.persistence.internal.weaving.PersistenceWeavedFetchGroups}
     */
    @Property("cuba.disableEntityEnhancementCheck")
    boolean getDisableEntityEnhancementCheck();

    /**
     * Defines the source from which roles are used in the application. There are 2 possible values:
     * SOURCE_CODE - only roles defined in the source code will be used;
     * MIXED - mixed mode, both sources will be used. If there are roles with equal names in the database and in
     * the source code, role from database will be used.
     * Application uses mixed mode by default.
     */
    @Property("cuba.rolesStorageMode")
    @Default("MIXED")
    @Factory(factory = SecurityStorageModeFactory.class)
    SecurityStorageMode getRolesStorageMode();

    /**
     * Whether the {@link com.haulmont.cuba.core.sys.DefaultPermissionValuesConfig} should be used when permissions are
     * calculated
     */
    @Property("cuba.security.defaultPermissionValuesConfigEnabled")
    @DefaultBoolean(false)
    boolean getDefaultPermissionValuesConfigEnabled();

    /**
     * Whether the {@link com.haulmont.cuba.security.app.role.MinimalRoleDefinition} should be default role.
     */
    @Property("cuba.security.minimalRoleIsDefault")
    @DefaultBoolean(true)
    boolean getMinimalRoleIsDefault();

    /**
     * Defines the source from which access groups are used in the application. There are 2 possible values:
     * SOURCE_CODE - only access groups defined in the source code will be used;
     * MIXED - mixed mode, both sources will be used. If there are access groups with equal names in the database and in
     * the source code, access group from database will be used.
     * Application uses mixed mode by default.
     */
    @Property("cuba.accessGroupsStorageMode")
    @Default("MIXED")
    @Factory(factory = SecurityStorageModeFactory.class)
    SecurityStorageMode getAccessGroupsStorageMode();

    /**
     * Represents list of all available security scopes for all client types.
     * Allows to select security scopes from the list in a UI and
     * associate roles with user and selected security scope
     */
    @Property("cuba.securityScopes")
    @Default("GENERIC_UI")
    @Factory(factory = TokenizedStringListFactory.class)
    List<String> getSecurityScopes();

    /**
     * Returns the roles policy version.
     * <ul>
     *     <li>1 - Security implementation used before CUBA 7.2: undefined permissions are resolved to allowed, roles
     *     types are used</li>
     *     <li>2 - New roles resolving implementation introduced in CUBA 7.2: undefined permissions are resolved to
     *     denied, the only possible permission is ALLOW (user cannot select DENY), design-time roles can be used</li>
     * </ul>
     */
    @Property("cuba.security.rolesPolicyVersion")
    @DefaultInt(2)
    int getRolesPolicyVersion();
}