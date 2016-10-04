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
     * @return external authentication provider
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

    /**
     * @return Field of LDAP object for user login matching.
     */
    @Property("cuba.web.ldap.userLoginField")
    @DefaultString("sAMAccountName")
    String getLdapUserLoginField();

    @Property("cuba.web.ldap.password")
    String getLdapPassword();

    /**
     * @return Base URL of IDP server, e.g. http://localhost:8080/app/idp.
     */
    @Property("cuba.web.idp.baseUrl")
    String getIdpBaseURL();

    /**
     * @return Trusted password to access to IDP server.
     */
    @Property("cuba.web.idp.trustedServicePassword")
    String getIdpTrustedServicePassword();
}