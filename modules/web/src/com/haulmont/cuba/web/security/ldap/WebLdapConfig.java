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

package com.haulmont.cuba.web.security.ldap;

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
public interface WebLdapConfig extends Config {
    /**
     * @return true if LDAP authentication for REST API is enabled
     */
    @Property("cuba.web.ldap.enabled")
    @DefaultBoolean(false)
    boolean getLdapEnabled();

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
}