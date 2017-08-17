/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.restapi.ldap;

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
public interface RestLdapConfig extends Config {
    /**
     * @return true if LDAP authentication for REST API is enabled
     */
    @Property("cuba.rest.ldap.enabled")
    @DefaultBoolean(false)
    boolean getLdapEnabled();

    /**
     * @return the urls of the LDAP servers
     */
    @Property("cuba.rest.ldap.urls")
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    List<String> getLdapUrls();

    /**
     * @return the base LDAP suffix from which all operations should origin.
     * If a base suffix is set, you will not have to (and, indeed, must not) specify the full distinguished names in any
     * operations performed. For instance: dc=example,dc=com
     */
    @Property("cuba.rest.ldap.base")
    String getLdapBase();

    /**
     * @return user that is used to connect to LDAP server
     */
    @Property("cuba.rest.ldap.user")
    String getLdapUser();

    /**
     * @return password that is used to connect to LDAP server
     */
    @Property("cuba.rest.ldap.password")
    String getLdapPassword();

    /**
     * @return Field of LDAP object for user login matching.
     */
    @Property("cuba.rest.ldap.userLoginField")
    @DefaultString("sAMAccountName")
    String getLdapUserLoginField();
}