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
import com.haulmont.cuba.core.config.type.CommaSeparatedStringListTypeFactory;
import com.haulmont.cuba.core.config.type.Factory;

import java.util.List;

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
     * @return Whether to use an external authentication
     */
    @Property("cuba.web.externalAuthentication")
    @DefaultBoolean(false)
    boolean getExternalAuthentication();

    /**
     * @return external authentification provider
     */
    @Property("cuba.web.externalAuthenticationProviderClass")
    @DefaultString("com.haulmont.cuba.web.auth.LdapAuthProvider")
    String getExternalAuthenticationProviderClass();

    /**
     * @return Password used by LoginService.loginTrusted() method.
     * Trusted client may login without providing a user password. This is used for external authentication.
     *
     * <p>Must be equal to password set for the same property on the CORE.</p>
     */
    @Property("cuba.trustedClientPassword")
    @DefaultString("")
    String getTrustedClientPassword();

    @Property("cuba.web.ldap.urls")
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    List<String> getLdapUrls();

    @Property("cuba.web.ldap.base")
    String getLdapBase();

    @Property("cuba.web.ldap.user")
    String getLdapUser();

    @Property("cuba.web.ldap.password")
    String getLdapPassword();
}