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
 */

package com.haulmont.idp;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.type.CommaSeparatedStringListTypeFactory;
import com.haulmont.cuba.core.config.type.Factory;

import java.util.List;

/**
 * Configuration interface for IDP web service.
 */
@Source(type = SourceType.APP)
public interface IdpConfig extends Config {
    /**
     * @return trusted password of middleware
     */
    @Property("cuba.trustedClientPassword")
    String getTrustedClientPassword();

    /**
     * @return trusted password to grant access to services, must be equal to cuba.web.idp.trustedServicePassword on service
     */
    @Property("cuba.idp.trustedServicePassword")
    String getTrustedServicePassword();

    /**
     * @return all permitted service URLs separated by comma. <br>
     *         IDP is allowed to send redirects only on these URLs, or URLs, that match regexps from cuba.idp.serviceProviderUrlsMasks<br>
     *         First URL is used as default if user opened IDP login form directly.
     */
    @Property("cuba.idp.serviceProviderUrls")
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    List<String> getServiceProviderUrls();

    /**
     * @return java regexp masks for permitted service URLs separated by comma. <br>
     *         Should be carefully chosen to not to allow redirecting to untrusted URLs. <br>
     *         For example, http://your-domain.com.* unsafe mask, as would allow redirecting to http://your-domain.com.org,
     *         that might be owned by malefactor.
     *         For this case, safe mask would be http://your-domain.com/.*
     */
    @Property("cuba.idp.serviceProviderUrlMasks")
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    List<String> getServiceProviderUrlMasks();

    /**
     * @return all URLs that need to be notified on session logout or expiration separated by comma. <br>
     *         For example in CUBA it is http://localhost:8080/app/dispatch/idpc/logout. <br>
     *         IDP will pass to POST request two form-urlencoded parameters with names idpSessionId and trustedServicePassword.
     */
    @Property("cuba.idp.serviceProviderLogoutUrls")
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    List<String> getServiceProviderLogoutUrls();

    /**
     * @return service provider ticket expiration timeout in seconds.
     */
    @Property("cuba.idp.ticketExpirationTimeoutSec")
    @DefaultInt(180)
    int getTicketExpirationTimeoutSec();

    /**
     * @return IDP session expiration timeout in seconds.
     */
    @Property("cuba.idp.sessionExpirationTimeoutSec")
    @DefaultInt(18000)
    int getSessionExpirationTimeoutSec();

    /**
     * @return check interval for sessions and tickets expiration.
     */
    @Property("cuba.idp.sessionExpirationCheckIntervalMs")
    @DefaultInt(30000)
    int getSessionExpirationCheckIntervalMs();

    /**
     * @return IDP session cookie max age.
     */
    @Property("cuba.idp.cookieMaxAgeSec")
    @DefaultInt(365 * 24 * 60 * 60)
    int getIdpCookieMaxAge();

    /**
     * @return Whether to use http-only flag for IDP session cookie.
     */
    @Property("cuba.idp.cookieHttpOnly")
    @DefaultBoolean(true)
    boolean getIdpCookieHttpOnly();
}