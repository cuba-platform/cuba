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

package com.haulmont.cuba.portal.sys.cache;

import com.haulmont.cuba.client.sys.cache.CacheUserSessionProvider;
import com.haulmont.cuba.portal.config.PortalConfig;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(CacheUserSessionProvider.NAME)
public class PortalCacheUserSessionProvider implements CacheUserSessionProvider {

    @Inject
    protected LoginService loginService;

    @Inject
    protected PortalConfig portalConfig;

    @Override
    public UserSession getUserSession() {
        String trustedClientPassword = portalConfig.getTrustedClientPassword();

        try {
            return loginService.getSystemSession(trustedClientPassword);
        } catch (LoginException e) {
            throw new IllegalStateException("Unable to login with trusted client password", e);
        }
    }
}