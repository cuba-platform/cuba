/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultString;

/**
 * @author kozyaikin
 * @version $Id$
 */
@Source(type = SourceType.APP)
public interface WebAuthConfig extends Config {

    /**
     * @return Short/User-friendly domain aliases for login window form
     */
    @Property("cuba.web.activeDirectoryAliases")
    String getActiveDirectoryAliases();

    /**
     * @return ActiveDirectory domains configuration info
     */
    @Property("cuba.web.activeDirectoryDomains")
    String getActiveDirectoryDomains();

    /**
     * @return Kerberos auth module in JaasConf
     */
    @Property("cuba.web.kerberosAuthModule")
    String getKerberosAuthModule();

    /**
     * @return Kerberos domain and realms config (krb5.ini)
     */
    @Property("cuba.web.kerberosConf")
    String getKerberosConf();

    /**
     * @return Kerberos login module config (jaas.conf)
     */
    @Property("cuba.web.kerberosJaasConf")
    String getKerberosJaasConf();

    /**
     * @return Kerberos single-sign-on module in JaasConf
     */
    @Property("cuba.web.kerberosLoginModule")
    String getKerberosLoginModule();

    @Property("cuba.web.activeDirectoryDebug")
    @DefaultBoolean(false)
    boolean getActiveDirectoryDebug();

    /**
     * @return Whether to use the ActiveDirectory authentication
     */
    @Property("cuba.web.useActiveDirectory")
    @DefaultBoolean(false)
    boolean getUseActiveDirectory();

    /**
     * @return ActiveDirectory authentification provider
     */
    @Property("cuba.web.activeDirectoryAuthClass")
    @DefaultString("com.haulmont.cuba.web.auth.KerberosAuthProvider")
    String getActiveDirectoryAuthClass();

    /**
     * @return Password used by LoginService.loginTrusted() method.
     * Trusted client may login without providing a user password. This is used by ActiveDirectory integration.
     *
     * <p>Must be equal to password set for the same property on the CORE.</p>
     */
    @Property("cuba.trustedClientPassword")
    @DefaultString("")
    String getTrustedClientPassword();
}
