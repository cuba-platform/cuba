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

/**
 * Configuration parameters interface used by the CORE layer.
 *
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
    String getTrustedClientPermittedIpList();

    @Property("cuba.security.resetPasswordTemplateBody")
    @Default("/com/haulmont/cuba/security/app/email/reset-password-body.gsp")
    String getResetPasswordEmailBodyTemplate();

    @Property("cuba.security.resetPasswordTemplateSubject")
    @Default("/com/haulmont/cuba/security/app/email/reset-password-subject.gsp")
    String getResetPasswordEmailSubjectTemplate();

    /**
     * @return User session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.
     */
    @Property("cuba.userSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getUserSessionExpirationTimeoutSec();
    void setUserSessionExpirationTimeoutSec(int timeout);

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

    @Property("cuba.prettyTimeProperties")
    @DefaultString("")
    String getPrettyTimeProperties();

    /**
     * If set to false, attribute permissions are not enforced on Middleware. This is appropriate if only server-side
     * clients are used.
     */
    @Property("cuba.entityAttributePermissionChecking")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getEntityAttributePermissionChecking();

    /**
     * <= 16 symbols string, used as key for AES encryption of security token
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
}
